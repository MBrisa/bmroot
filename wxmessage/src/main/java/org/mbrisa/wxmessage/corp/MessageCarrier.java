package org.mbrisa.wxmessage.corp;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;

public class MessageCarrier {
	
	private static final Logger LOGGER = LogManager.getLogger(MessageCarrier.class);
	
	private static final int MAX_ACCESS = 2;
	
	private final CloseableHttpClient client = createSSLClient();
	
	private final Message message;
	private final WeixinCorpInfo corpInfo;

	public MessageCarrier(WeixinCorpInfo corpInfo,Message message) {
		this.message = message;
		this.corpInfo = corpInfo;
	}
	
	@SuppressWarnings("unchecked")
	public boolean send(){
		String token;
		try {
			token = getToken();
		} catch (Exception e) {
			LOGGER.error(e);
			return false;
		}
		
		String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token="+token;
		
		HttpPost httpPost = new HttpPost(url);
		StringEntity entity = new StringEntity(message.pack(), "UTF-8");
		httpPost.setEntity(entity);
		
		CloseableHttpResponse response;
		try {
			response = request(httpPost, 1);
		} catch (IOException | InterruptedException e) {
			LOGGER.error(e);
			return false;
		}
		
		if(response.getStatusLine().getStatusCode() != 200){
			LOGGER.error("receive invalid status code ["+response.getStatusLine().getStatusCode()+"] on access "+url);
			return false;
		}
		
//		{
//		   "errcode": 0,
//		   "errmsg": "ok",
//		   "invaliduser": "UserID1",
//		   "invalidparty":"PartyID1",
//		   "invalidtag":"TagID1"
//		}
		int errorCode;
		String rs;
		try {
			rs = EntityUtils.toString(response.getEntity());
			errorCode = (Integer)((Map<String,Object>)JSON.parse(rs)).get("errcode");
		} catch (Exception e) {
			LOGGER.error("parse error. ",e);
			return false;
		}
		if(errorCode == 0){
			LOGGER.info("success. response was "+rs);
			return true;
		}
		LOGGER.error("unsuccess. response was "+rs);
		return false;

	}
	
	public String getToken()throws Exception{
		String url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid="+
				corpInfo.getCorpId()+"&corpsecret="+corpInfo.getSecure();
		HttpGet get = new HttpGet(url);
		
		CloseableHttpResponse response = request(get, MAX_ACCESS);
		assert(response != null);
		
		if(response.getStatusLine().getStatusCode() != 200){
			throw new Exception("receive invalid status code ["+response.getStatusLine().getStatusCode()+"] on access "+url);
		}
		
		@SuppressWarnings("unchecked")
		Map<String,Object> r = (Map<String,Object>)JSON.parse(EntityUtils.toString(response.getEntity()));
//		{
//			   "errcode": 43003,
//			   "errmsg": "require https"
//			}
		String access_token = (String)r.get("access_token");
		if(access_token == null){
			throw new Exception("receive invalid data '"+r.toString()+"' on access "+url);
		}
		
		return access_token;
	}

	private CloseableHttpResponse request(HttpRequestBase req, int maxAccess) throws IOException, InterruptedException {
		if(maxAccess < 1){
			throw new Error("max access must right 1");
		}
		req.setConfig(RequestConfig.custom().setSocketTimeout(5000).build());
		CloseableHttpResponse response = null;
		for(int i = 0;i<maxAccess;i++){
			try {
				response = client.execute(req);
				break;
			} catch (IOException e) {
				if(i + 1 == maxAccess){
					throw e;
				}
				Thread.sleep(2000);
			}
		}
		return response;
	}
	
	private static CloseableHttpClient createSSLClient() {
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				@Override
				public boolean isTrusted(java.security.cert.X509Certificate[] chain, String authType)
						throws java.security.cert.CertificateException {
					return true;
				}
			}).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
			return HttpClients.custom().setSSLSocketFactory(sslsf).build();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}

		return HttpClients.createDefault();
	}

}
