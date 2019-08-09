package cz.sysnet.geo.utils;

public class Jtsk {

	private double x;

	private double y;

	public Jtsk(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	/**
	 * převádí WGS-84 do S-JTSK
	 * 
	 * @param WGS84
	 * @return JTSK
	 */
	public static Jtsk convert(Wgs84 wgs84) {
		final double altitude = wgs84.getAltitude();

		double d2r = Math.PI / 180;
		double a = 6378137.0;
		double f1 = 298.257223563;
		double dx = -570.69;
		double dy = -85.69;
		double dz = -462.84;
		double wx = 4.99821 / 3600 * Math.PI / 180;
		double wy = 1.58676 / 3600 * Math.PI / 180;
		double wz = 5.2611 / 3600 * Math.PI / 180;
		double m = -3.543e-6;

		double latitude = wgs84.getLatitude();
		double longtitude = wgs84.getLongitude();
		double B = latitude * d2r;
		double L = longtitude * d2r;
		double H = altitude;

		double e2 = 1 - Math.pow(1 - 1 / f1, 2);
		double rho = a / Math.sqrt(1 - e2 * Math.pow(Math.sin(B), 2));
		double x1 = (rho + H) * Math.cos(B) * Math.cos(L);
		double y1 = (rho + H) * Math.cos(B) * Math.sin(L);
		double z1 = ((1 - e2) * rho + H) * Math.sin(B);

		double x2 = dx + (1 + m) * (x1 + wz * y1 - wy * z1);
		double y2 = dy + (1 + m) * (-wz * x1 + y1 + wx * z1);
		double z2 = dz + (1 + m) * (wy * x1 - wx * y1 + z1);

		a = 6377397.15508;
		f1 = 299.152812853;
		double ab = f1 / (f1 - 1);
		double p = Math.sqrt(Math.pow(x2, 2) + Math.pow(y2, 2));
		e2 = 1 - Math.pow(1 - 1 / f1, 2);
		double th = Math.atan(z2 * ab / p);
		double st = Math.sin(th);
		double ct = Math.cos(th);
		double t = (z2 + e2 * ab * a * (st * st * st)) / (p - e2 * a * (ct * ct * ct));

		B = Math.atan(t);
		H = Math.sqrt(1 + t * t) * (p - a / Math.sqrt(1 + (1 - e2) * t * t));
		L = 2 * Math.atan(y2 / (p + x2));

		a = 6377397.15508;
		double e = 0.081696831215303;
		double n = 0.97992470462083;
		double rho0 = 12310230.12797036;
		double sinUQ = 0.863499969506341;
		double cosUQ = 0.504348889819882;
		double sinVQ = 0.420215144586493;
		double cosVQ = 0.907424504992097;
		double alpha = 1.000597498371542;
		double k2 = 1.00685001861538;

		double sinB = Math.sin(B);
		t = (1 - e * sinB) / (1 + e * sinB);
		t = Math.pow(1 + sinB, 2) / (1 - Math.pow(sinB, 2))	* Math.exp(e * Math.log(t));
		t = k2 * Math.exp(alpha * Math.log(t));

		double sinU = (t - 1) / (t + 1);
		double cosU = Math.sqrt(1 - sinU * sinU);
		double V = alpha * L;
		double sinV = Math.sin(V);
		double cosV = Math.cos(V);
		double cosDV = cosVQ * cosV + sinVQ * sinV;
		double sinDV = sinVQ * cosV - cosVQ * sinV;
		double sinS = sinUQ * sinU + cosUQ * cosU * cosDV;
		double cosS = Math.sqrt(1 - sinS * sinS);
		double sinD = sinDV * cosU / cosS;
		double cosD = Math.sqrt(1 - sinD * sinD);

		double eps = n * Math.atan(sinD / cosD);
		rho = rho0 * Math.exp(-n * Math.log((1 + sinS) / cosS));

		double CX = rho * Math.sin(eps);
		double CY = rho * Math.cos(eps);

		return new Jtsk(-CX, -CY);
	}
}