package cz.sysnet.utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnvUtils {

	public static String getEnvString(String variableName) {
		return getEnvString(variableName, "");
	}
	
	public static String getEnvString(String variableName, String defaultValue) {
		String d = defaultValue;
		String out = d;
		try {
			out = System.getenv(variableName);
			if (out == null) out = d;
	
		} catch (Exception e) {
			System.out.println("EnvUtils.getEnvString: " + e.getMessage());
			e.printStackTrace();
			out = d;
		}
		return out;		
	}
	
	
	public static int getEnvInteger(String variableName) {
		return getEnvInteger(variableName, 0);
	}
	
	public static int getEnvInteger(String variableName, int defaultValue) {
		int d = defaultValue;
		int out = d;
		String s = getEnvString(variableName);
		if (s.isEmpty()) return out;
		try {
			out = Integer.parseInt(s);
			
		} catch (Exception e) {
			System.out.println("EnvUtils.getEnvInteger: " + e.getMessage());
			e.printStackTrace();
			out = d;
		}
		return out;
	}
	
	public static long getEnvLong(String variableName) {
		return getEnvLong(variableName, 0);
	}
	
	public static long getEnvLong(String variableName, long defaultValue) {
		long d = defaultValue;
		long out = d;
		String s = getEnvString(variableName);
		if (s.isEmpty()) return out;
		try {
			out = Long.parseLong(s);
			
		} catch (Exception e) {
			System.out.println("EnvUtils.getEnvInteger: " + e.getMessage());
			e.printStackTrace();
			out = d;
		}
		return out;
	}
	
	public static boolean getEnvBoolean(String variableName) {
		return getEnvBoolean(variableName, false);
	}

	public static boolean getEnvBoolean(String variableName, boolean defaultValue) {
		boolean out = defaultValue;
		String s = getEnvString(variableName);
		if (s.isEmpty()) return out;
		try {
			out = Boolean.parseBoolean(s);
			
		} catch (Exception e) {
			System.out.println("EnvUtils.getEnvBoolean: " + e.getMessage());
			e.printStackTrace();
			out = false;
		}
		return out;
	}
	
	public static float getEnvFloat(String variableName) {
		return getEnvFloat(variableName, 0);
	}
	
	public static float getEnvFloat(String variableName, float defaultValue) {
		float d = defaultValue;
		float out = d;
		String s = getEnvString(variableName);
		if (s.isEmpty()) return out;
		try {
			out = Float.parseFloat(s);
			
		} catch (Exception e) {
			System.out.println("EnvUtils.getEnvFloat: " + e.getMessage());
			e.printStackTrace();
			out = d;
		}
		return out;
	}
	
	public static double getEnvDouble(String variableName) {
		return getEnvDouble(variableName, 0);
	}
	
	public static double getEnvDouble(String variableName, double defaultValue) {
		double d = defaultValue;
		double out = d;
		String s = getEnvString(variableName);
		if (s.isEmpty()) return out;
		try {
			out = Double.parseDouble(s);
			
		} catch (Exception e) {
			System.out.println("EnvUtils.getEnvDouble: " + e.getMessage());
			e.printStackTrace();
			out = d;
		}
		return out;
	}
	
	public static Map<String, String> getEnv() {
		return System.getenv();
	}
	
	public static List<String> getEnvList(String variableName) {
		return getEnvList(variableName, null, null);
	}
	
	public static List<String> getEnvList(String variableName, List<String> defaultValue) {
		return getEnvList(variableName, null, defaultValue);
	}
	
	public static List<String> getEnvList(String variableName, String delimiter) {
		return getEnvList(variableName, delimiter, null);
	}

	public static List<String> getEnvList(String variableName, String delimiter, List<String> defaultValue) {
		String env = getEnvString(variableName);
		List<String> out = parseStringToList(env, delimiter);
		if (out == null) out = defaultValue;
		if (out == null) out = new ArrayList<String>();
		return out;
	}
	
	public static List<String> parseStringToList(String source) {
		return parseStringToList(source, null);
	}
	
	public static List<String> parseStringToList(String source, String delimiter) {
		List<String> out = new ArrayList<String>();
		String d = ":";
		try {
			if (source == null) return null; 
			if (delimiter != null) if (!delimiter.isEmpty()) d = delimiter;
			if(!source.isEmpty()) {
				String[] a = source.split(d);
				out = Arrays.asList(a);				
			} else {
				return null;
			}
		} catch (Exception e) {
			System.out.println("EnvUtils.parseStringToList: " + e.getMessage());
			e.printStackTrace();
			out = new ArrayList<String>();
			out.add(source);
		}		
		return out;
	}
	
	public static Map<String, String> getEnvMap(String variableName) {
		return getEnvMap(variableName, null, null);
	}
	
	public static Map<String, String> getEnvMap(String variableName, Map<String, String> defaultValue) {
		return getEnvMap(variableName, null, defaultValue);
	}
	
	public static Map<String, String> getEnvMap(String variableName, String delimiter) {
		return getEnvMap(variableName, delimiter, null);
	}
	
	public static Map<String, String> getEnvMap(String variableName, String delimiter, Map<String, String> defaultValue) {
		Map<String, String> out = defaultValue;
		if (out == null) out = new HashMap<String, String>(); 
		String env = getEnvString(variableName);
		if (env == null) return out;
		if (env.isEmpty()) return out;		
		return parseStringToMap(env, delimiter);
	}
	
	public static Map<String, String> parseStringToMap(String source) {
		return parseStringToMap(source, null);
	}
	
	public static Map<String, String> parseStringToMap(String source, String delimiter) {
		Map<String, String> out = new HashMap<String, String>();
		List<String> list = parseStringToList(source, delimiter);
		try {
			if(!list.isEmpty()) {
				for(String item:list) {
					String[] pair = item.split("=");
					out.put(pair[0], pair[1]);
				}				
			}			
		} catch (Exception e) {
			System.out.println("EnvUtils.parseStringToMap: " + e.getMessage());
			e.printStackTrace();
			out = new HashMap<String, String>();
		}
		return out;
	}
}


