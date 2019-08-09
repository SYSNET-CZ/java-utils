package cz.sysnet.utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnvUtils {

	
	public static String getEnvString(String variableName) {
		String out = "";
		try {
			out = System.getenv(variableName);
			if (out == null) out = "";
	
		} catch (Exception e) {
			System.out.println("EnvUtils.getEnvString: " + e.getMessage());
			e.printStackTrace();
			out = "";
		}
		return out;		
	}
	
	public static int getEnvInteger(String variableName) {
		int out = 0;
		String s = getEnvString(variableName);
		if (s.isEmpty()) return out;
		try {
			out = Integer.parseInt(s);
			
		} catch (Exception e) {
			System.out.println("EnvUtils.getEnvInteger: " + e.getMessage());
			e.printStackTrace();
			out = 0;
		}
		return out;
	}
	
	public static long getEnvLong(String variableName) {
		long out = 0;
		String s = getEnvString(variableName);
		if (s.isEmpty()) return out;
		try {
			out = Long.parseLong(s);
			
		} catch (Exception e) {
			System.out.println("EnvUtils.getEnvInteger: " + e.getMessage());
			e.printStackTrace();
			out = 0;
		}
		return out;
	}

	
	public static boolean getEnvBoolean(String variableName) {
		boolean out = false;
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
		float out = 0;
		String s = getEnvString(variableName);
		if (s.isEmpty()) return out;
		try {
			out = Float.parseFloat(s);
			
		} catch (Exception e) {
			System.out.println("EnvUtils.getEnvFloat: " + e.getMessage());
			e.printStackTrace();
			out = 0;
		}
		return out;
	}
	
	public static double getEnvDouble(String variableName) {
		double out = 0;
		String s = getEnvString(variableName);
		if (s.isEmpty()) return out;
		try {
			out = Double.parseDouble(s);
			
		} catch (Exception e) {
			System.out.println("EnvUtils.getEnvDouble: " + e.getMessage());
			e.printStackTrace();
			out = 0;
		}
		return out;
	}
	
	public static Map<String, String> getEnv() {
		return System.getenv();
	}
	
	public static List<String> getEnvList(String variableName) {
		return getEnvList(variableName, null);
	}		
	
	public static List<String> getEnvList(String variableName, String delimiter) {
		String env = getEnvString(variableName);
		return parseStringToList(env, delimiter);
	}
	
	public static List<String> parseStringToList(String source) {
		return parseStringToList(source, null);
	}
	
	public static List<String> parseStringToList(String source, String delimiter) {
		List<String> out = new ArrayList<String>();
		String d = ":";
		try {
			if (delimiter != null) if (!delimiter.isEmpty()) d = delimiter;
			if(!source.isEmpty()) {
				String[] a = source.split(d);
				out = Arrays.asList(a);				
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
		return getEnvMap(variableName, null);
	}
	
	public static Map<String, String> getEnvMap(String variableName, String delimiter) {
		String env = getEnvString(variableName);
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

