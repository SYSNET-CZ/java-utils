package cz.sysnet.geo.utils;

public class Wgs84 {

	private double latitude;

	private double longitude;
	
	/**
	 * nadmořská výška v metrech
	 */	 
	private double altitude = 200;
	
	public Wgs84(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public Wgs84(double latitude, double longitude, double altitude) {
		this(latitude, longitude);
		this.altitude = altitude;
	}

	public double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}
	
	public static Wgs84 convert(Jtsk jtsk) {
		Wgs84 out = new Wgs84(0.0, 0.0, 0.0);
		try {
			double x = jtsk.getX();
			double y = jtsk.getY();
			double[] w = GeoConvertor.JTSKtoWGS84(x, y);
			if (w != null)
				if (w.length == 2) {
					out.setLatitude(w[0]);
					out.setLongitude(w[1]);
					out.setAltitude(200);
				}
		} catch (Exception e) {
			out = new Wgs84(0.0, 0.0, 0.0);
		}
		return out;
	}
}