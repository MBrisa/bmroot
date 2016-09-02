package org.mbrisa.wxmessage.corp;

public class WeixinCorpInfo {
	
	private final String corpId;
	private final String secure;
	
	
	public WeixinCorpInfo(String corpId, String secure) {
		super();
		this.corpId = corpId;
		this.secure = secure;
	}

	public String getCorpId() {
		return corpId;
	}

	public String getSecure() {
		return secure;
	}

}
