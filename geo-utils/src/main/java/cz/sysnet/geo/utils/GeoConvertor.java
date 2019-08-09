package cz.sysnet.geo.utils;

@SuppressWarnings("unused")
class GeoConvertor {
	public static final double EPS = 1e-4; // relative accuracy

	/**
	 * Conversion from JTSK to WGS-84 (by iteration)
	 *
	 * @param x
	 * @param y
	 * @return array
	 */
	public static double[] JTSKtoWGS84(double x, double y) {
		// if (!(x && y)) {
		// return array('lat' => 0, 'lon' => 0);
		// }

		double delta = 5;
		double latitude = 49;
		double longitude = 14;
		double steps = 0;
		double v1 = 0;
		double v2 = 0;
		double v3 = 0;
		double v4 = 0;
		double[] jtsk;
		do {
			jtsk = WGS84toJTSK(latitude - delta, longitude - delta);
			if (jtsk[0] != 0 && jtsk[1] != 0) {
				v1 = distPoints(jtsk[0], jtsk[1], x, y);
			} else {
				v1 = 1e32;
			}

			jtsk = WGS84toJTSK(latitude - delta, longitude + delta);
			if (jtsk[0] != 0 && jtsk[1] != 0) {
				v2 = distPoints(jtsk[0], jtsk[1], x, y);
			} else {
				v2 = 1e32;
			}

			jtsk = WGS84toJTSK(latitude + delta, longitude - delta);
			if (jtsk[0] != 0 && jtsk[1] != 0) {
				v3 = distPoints(jtsk[0], jtsk[1], x, y);
			} else {
				v3 = 1e32;
			}

			jtsk = WGS84toJTSK(latitude + delta, longitude + delta);
			if (jtsk[0] != 0 && jtsk[1] != 0) {
				v4 = distPoints(jtsk[0], jtsk[1], x, y);
			} else {
				v4 = 1e32;
			}

			if ((v1 <= v2) && (v1 <= v3) && (v1 <= v4)) {
				latitude = latitude - delta / 2;
				longitude = longitude - delta / 2;
			}

			if ((v2 <= v1) && (v2 <= v3) && (v2 <= v4)) {
				latitude = latitude - delta / 2;
				longitude = longitude + delta / 2;
			}

			if ((v3 <= v1) && (v3 <= v2) && (v3 <= v4)) {
				latitude = latitude + delta / 2;
				longitude = longitude - delta / 2;
			}

			if ((v4 <= v1) && (v4 <= v2) && (v4 <= v3)) {
				latitude = latitude + delta / 2;
				longitude = longitude + delta / 2;
			}

			delta *= 0.55;
			steps += 4;
		} while (!((delta < 0.00001) || (steps > 1000)));

		double[] r = { latitude, longitude };
		return r;
	}

	/**
	 * Conversion from WGS-84 to JTSK
	 *
	 * @param latitude
	 * @param longitude
	 * @return array
	 */
	public static double[] WGS84toJTSK(double latitude, double longitude) {
		if ((latitude < 40) || (latitude > 60) || (longitude < 5) || (longitude > 25)) {
			double[] r = { 0, 0 };
			return r;
		} else {
			double[] r = WGS84toBessel(latitude, longitude, 200);
			return BesseltoJTSK(r[0], r[1]);
		}
	}

	/**
	 * Conversion from ellipsoid WGS-84 to Bessel's ellipsoid
	 *
	 * @param latitude
	 * @param longitude
	 * @param           int altitude
	 * @return array
	 */
	public static double[] WGS84toBessel(double latitude, double longitude, double altitude) {
		double B = deg2rad(latitude);
		double L = deg2rad(longitude);
		double H = altitude;

		double[] xyz1 = BLHToGeoCoords(B, L, H);
		double[] xyz2 = transformCoords(xyz1[0], xyz1[1], xyz1[2]);
		double[] BLH = geoCoordsToBLH(xyz2[0], xyz2[1], xyz2[2]);

		latitude = rad2deg(B);
		longitude = rad2deg(L);
		// Altitude = H;
		double[] r = { latitude, longitude };
		return r;
	}

	/**
	 * Conversion from Bessel's lat/lon to WGS-84
	 *
	 * @param latitude
	 * @param longitude
	 * @return array
	 */
	public static double[] BesseltoJTSK(double latitude, double longitude) {
		double a = 6377397.15508;
		double e = 0.081696831215303;
		double n = 0.97992470462083;
		double rho_0 = 12310230.12797036;
		double sinUQ = 0.863499969506341;
		double cosUQ = 0.504348889819882;
		double sinVQ = 0.420215144586493;
		double cosVQ = 0.907424504992097;
		double alfa = 1.000597498371542;
		double k_2 = 1.00685001861538;

		double B = deg2rad(latitude);
		double L = deg2rad(longitude);

		double sinB = Math.sin(B);
		double t = (1 - e * sinB) / (1 + e * sinB);
		t = Math.pow(1 + sinB, 2) / (1 - Math.pow(sinB, 2)) * Math.exp(e * Math.log(t));
		t = k_2 * Math.exp(alfa * Math.log(t));

		double sinU = (t - 1) / (t + 1);
		double cosU = Math.sqrt(1 - sinU * sinU);
		double V = alfa * L;
		double sinV = Math.sin(V);
		double cosV = Math.cos(V);
		double cosDV = cosVQ * cosV + sinVQ * sinV;
		double sinDV = sinVQ * cosV - cosVQ * sinV;
		double sinS = sinUQ * sinU + cosUQ * cosU * cosDV;
		double cosS = Math.sqrt(1 - sinS * sinS);
		double sinD = sinDV * cosU / cosS;
		double cosD = Math.sqrt(1 - sinD * sinD);

		double eps = n * Math.atan(sinD / cosD);
		double rho = rho_0 * Math.exp(-n * Math.log((1 + sinS) / cosS));

		double[] r = { rho * Math.cos(eps), rho * Math.sin(eps) };
		return r;
	}

	/**
	 * Conversion from geodetic coordinates to Cartesian coordinates
	 *
	 * @param B
	 * @param L
	 * @param H
	 * @return array
	 */
	public static double[] BLHToGeoCoords(double B, double L, double H) {
		// WGS-84 ellipsoid parameters
		double a = 6378137.0;
		double f_1 = 298.257223563;
		double e2 = 1 - Math.pow(1 - 1 / f_1, 2);
		double rho = a / Math.sqrt(1 - e2 * Math.pow(Math.sin(B), 2));
		double x = (rho + H) * Math.cos(B) * Math.cos(L);
		double y = (rho + H) * Math.cos(B) * Math.sin(L);
		double z = ((1 - e2) * rho + H) * Math.sin(B);

		double[] r = { x, y, z };
		return r;
	}

	/**
	 * Conversion from Cartesian coordinates to geodetic coordinates
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return array
	 */
	public static double[] geoCoordsToBLH(double x, double y, double z) {
		// Bessel's ellipsoid parameters
		double a = 6377397.15508;
		double f_1 = 299.152812853;
		double a_b = f_1 / (f_1 - 1);
		double p = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		double e2 = 1 - Math.pow(1 - 1 / f_1, 2);
		double th = Math.atan(z * a_b / p);
		double st = Math.sin(th);
		double ct = Math.cos(th);
		double t = (z + e2 * a_b * a * Math.pow(st, 3)) / (p - e2 * a * Math.pow(ct, 3));

		double B = Math.atan(t);
		double H = Math.sqrt(1 + t * t) * (p - a / Math.sqrt(1 + (1 - e2) * t * t));
		double L = 2 * Math.atan(y / (p + x));

		double[] r = { B, L, H };
		return r;
	}

	/**
	 * Distance between two points
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return float|int
	 */
	private static double distPoints(double x1, double y1, double x2, double y2) {
		double dist = hypot(x1 - x2, y1 - y2);
		if (dist < EPS) {
			return 0;
		}
		return dist;
	}

	/**
	 * Coordinates transformation
	 *
	 * @param xs
	 * @param ys
	 * @param zs
	 * @return array
	 */
	private static double[] transformCoords(double xs, double ys, double zs) {
		// coeficients of transformation from WGS-84 to JTSK
		double dx = -570.69;
		double dy = -85.69;
		double dz = -462.84; // shift
		double wx = 4.99821 / 3600 * Math.PI / 180;
		double wy = 1.58676 / 3600 * Math.PI / 180;
		double wz = 5.2611 / 3600 * Math.PI / 180; // rotation
		double m = -3.543e-6; // scale

		double xn = dx + (1 + m) * (+xs + wz * ys - wy * zs);
		double yn = dy + (1 + m) * (-wz * xs + ys + wx * zs);
		double zn = dz + (1 + m) * (+wy * xs - wx * ys + zs);

		double[] r = { xn, yn, zn };
		return r;
	}

	private static double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		if (unit == 'K') {
			dist = dist * 1.609344;
		} else if (unit == 'N') {
			dist = dist * 0.8684;
		}
		return (dist);
	}

	// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	// :: This converts decimal degrees to radians :::
	// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	// :: This converts radians to decimal degrees :::
	// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	private static double rad2deg(double rad) {
		return (rad / Math.PI * 180.0);
	}

	public static double hypot(double a, double b) {

		a = Math.abs(a);
		b = Math.abs(b);

		if (a < b) {

			double temp = a;

			a = b;

			b = temp;

		}

		if (a == 0.0)

			return 0.0;

		else {

			double ba = b / a;

			return a * Math.sqrt(1.0 + ba * ba);

		}
	}
}