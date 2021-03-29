package cz.sysnet.utils.test;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import cz.sysnet.utils.EnvUtils;

public class TestEnvUtils {
	String TEST_MAP = "key1=Value1:key2=Value2:key3=Value3:key4=Value4:key5=Value5:key6=Value6:key7=Value7:key8=Value8";
	String TEST_LIST = "item1:item2:item3:item4";

	@Test
	public void testGetEnv() {
		
		Map<String, String> out = EnvUtils.getEnv();
		assertTrue(out != null);
		assertTrue(!out.isEmpty());
	}
	
	@Test
	public void testGetEnvString() {
		String out = EnvUtils.getEnvString("PATH");
		assertTrue(out != null);
		assertTrue(!out.isEmpty());
		//System.out.println(out);		
	}
	
	@Test
	public void testGetEnvStringDefault() {
		String out = EnvUtils.getEnvString("XXX", "AAA");
		assertTrue(out != null);
		assertTrue(out.equalsIgnoreCase("AAA"));
		//System.out.println(out);		
	}
	
	@Test
	public void testGetEnvList() {
		String os = System.getProperty("os.name");
		String del = ":";
		if(os.toLowerCase().contains("win")) del=";"; 
		String src = EnvUtils.getEnvString("PATH");
		assertTrue(!src.isEmpty());
		
		List<String> out = EnvUtils.parseStringToList(src, del);
		assertTrue(out != null);
		assertTrue(!out.isEmpty());
		
		out = EnvUtils.getEnvList("PATH", del);
		assertTrue(out != null);
		assertTrue(!out.isEmpty());
		//System.out.println(out.toString());	
	}
	
	@Test
	public void testGetEnvListDefault() {
		List<String> dl = EnvUtils.parseStringToList(TEST_LIST, ":");
		assertTrue(dl != null);
		assertTrue(!dl.isEmpty());
		
		List<String> out = EnvUtils.getEnvList("XXX", ":", dl);
		assertTrue(out != null);
		assertTrue(!out.isEmpty());
		assertTrue(out.size() == 4);		
		//System.out.println(out.toString());
	}
	
	@Test
	public void testGetEnvMapDefault() {
		Map<String, String> dm = EnvUtils.parseStringToMap(TEST_MAP);
		assertTrue(dm != null);
		assertTrue(!dm.isEmpty());
		Map<String, String> out = EnvUtils.getEnvMap("XXX", ":", dm);
		assertTrue(out != null);
		assertTrue(!out.isEmpty());
		assertTrue(out.size() == 8);				
	}	
}
