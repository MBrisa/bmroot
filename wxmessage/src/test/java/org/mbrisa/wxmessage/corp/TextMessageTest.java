package org.mbrisa.wxmessage.corp;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSON;

public class TextMessageTest {

	public TextMessageTest() {
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void normal()throws Exception{
		TextMessage tm = new TextMessage(1, "helloBaby");
		String p = tm.pack();
		Map<String,Object> m = (Map<String,Object>)JSON.parse(p);
		assertEquals(5,m.size());
		assertEquals(0,m.get("safe"));
		assertEquals("text",m.get("msgtype"));
		assertEquals("@all",m.get("touser"));
		assertEquals(1,m.get("agentid"));
		Map<String,String> text = (Map<String,String>)m.get("text");
		assertEquals(1,text.size());
		assertEquals("helloBaby",text.get("content"));
	}
	
	@Test(expected=InvalidContentException.class)
	public void large()throws Exception{
		int max = 1024;
		StringBuilder sb = new StringBuilder();
		for(int i = 0;i < max;i++){
			sb.append("号");
		}
		
		new TextMessage(1, sb.toString());
	}
	
	

}
