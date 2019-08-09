package cz.sysnet.geo.utils;

import org.elasticsearch.common.geo.GeoPoint;

/**
 * Nástroje pro zpracování prostorových dat
 * 
 * @author Radim Jaeger
 *
 */
public class Utils {
	/**
	 * Parsuje textovy obsah ve formatu 0.0000 nebo DMS a vrací číslo. V případě chyby vrací 0.0 
	 * 
	 * @param input	Testový vstup 
	 * @return	číselná hodnota
	 */
	public static double parseToDouble(String input) {
		double out = 0.0;
		if (input == null) return out;
		if (input.isEmpty()) return out;
		try {
			out = Double.parseDouble(input);
			return out;
		} catch (NumberFormatException e) {
			out = 0.0;
		}
		try {
			String inp = input.replaceAll("[^0-9.]", " ");
			String[] array = inp.split(" ");
			int degree = Integer.parseInt(array[0]);
			int minute = Integer.parseInt(array[1]);
			double second = Double.parseDouble(array[2]);
			out = degree + (double) minute/60 + (double) second/3600;
		} catch (Exception e) {
			System.out.println("cz.sysnet.geo.utils.Utils.parseToDouble" + e.getMessage());
			out = 0.0;
		}
		return out;
	}	
	
	public static GeoPoint parseToGeoPoint(String latitude, String longitude) {
		GeoPoint out = new GeoPoint();
		try {
			double lat = parseToDouble(latitude);
			double lon = parseToDouble(longitude);
			out.reset(lat, lon);
			
		} catch (Exception e) {
			System.out.println("cz.sysnet.geo.utils.Utils.parseToGeoPoint" + e.getMessage());
			out = new GeoPoint(0.0, 0.0);
		}
		return out;
	}
	
	public static GeoPoint parseToGeoPoint(Wgs84 wgs84) {
		GeoPoint out = new GeoPoint(0.0, 0.0);
		if (wgs84 != null) {
			out.reset(wgs84.getLatitude(), wgs84.getLongitude());
		}
		return out;
	}
	
	public static GeoPoint parseToGeoPoint(Jtsk jtsk) {
		GeoPoint out = new GeoPoint(0.0, 0.0);
		if (jtsk != null) {
			Wgs84 wgs84 = Wgs84.convert(jtsk);
			out.reset(wgs84.getLatitude(), wgs84.getLongitude());
		}
		return out;
	}
	
	public static GeoPoint parseToGeoPointWgs84(String latitude, String longitude) {
		GeoPoint out = parseToGeoPoint(latitude, longitude);
		if ((Math.abs(out.getLat()) > 90.0) || (Math.abs(out.getLon()) > 180.0)) { 
			out.reset(0.0, 0.0);
		} 	
		return out;
	}
	
	public static Wgs84 parseToWgs84(String latitude, String longitude) {
		Wgs84 out = new Wgs84(0.0, 0.0, 0.0);
		try {
			double lat = parseToDouble(latitude);
			double lon = parseToDouble(longitude);
			if ((Math.abs(lat) <= 90.0) && (Math.abs(lon) <= 180.0)) {
				out.setAltitude(200);
				out.setLatitude(lat);
				out.setLongitude(lon);				
			}
		} catch (Exception e) {
			out = new Wgs84(0.0, 0.0, 0.0);
		}
		return out;	
	}
	
	public static Jtsk parseToJtsk(String xvalue, String yvalue) {
		Jtsk out = new Jtsk(0.0, 0.0);
		try {
			double x = parseToDouble(xvalue);
			double y = parseToDouble(yvalue);
			if (Math.abs(x) >= Math.abs(y)) {
				out.setX(x);
				out.setY(y);
			} else {
				out.setX(y);
				out.setY(x);
			}
		} catch (Exception e) {
			out = new Jtsk(0.0, 0.0);
		}
		return out;
	}	
}
