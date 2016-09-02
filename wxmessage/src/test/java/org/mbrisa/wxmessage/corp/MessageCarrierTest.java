package org.mbrisa.wxmessage.corp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MessageCarrierTest {
	
	private static final String CORP_ID = "CORP_ID";
	private static final String SECURE = "SECURE";
	
	
//	@Test
	public void normal(){
		TextMessage tm = new TextMessage(0, "helloBaby");
		
		WeixinCorpInfo corpInfo = new WeixinCorpInfo(CORP_ID, SECURE);
		
		MessageCarrier carrier = new MessageCarrier(corpInfo, tm);
		assertTrue(carrier.send());
	}
	
//	@Test
	public void large(){
		TextMessage tm = new TextMessage(0, "号",true);
		
		WeixinCorpInfo corpInfo = new WeixinCorpInfo(CORP_ID, SECURE);
		
		MessageCarrier carrier = new MessageCarrier(corpInfo, tm);
		assertFalse(carrier.send());
	}

}
