package org.mbrisa.wxmessage.corp;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public class TextMessage implements Message {
	
//	touser 	否 	成员ID列表（消息接收者，多个接收者用‘|’分隔，最多支持1000个）。特殊情况：指定为@all，则向关注该企业应用的全部成员发送
//	toparty 	否 	部门ID列表，多个接收者用‘|’分隔，最多支持100个。当touser为@all时忽略本参数
//	totag 	否 	标签ID列表，多个接收者用‘|’分隔，最多支持100个。当touser为@all时忽略本参数
//	msgtype 	是 	消息类型，此时固定为：text （支持消息型应用跟主页型应用）
//	agentid 	是 	企业应用的id，整型。可在应用的设置页面查看
//	content 	是 	消息内容，最长不超过2048个字节，注意：主页型应用推送的文本消息在微信端最多只显示20个字（包含中英文）
//	safe 	否 	表示是否是保密消息，0表示否，1表示是，默认0
	
	private final String msgtype = "text";
	private final int agentid;
	private final String content;
	private final byte safe;
	
	private HashSet<String> users = new HashSet<>();
	private HashSet<String> departments = new HashSet<>();
	private HashSet<String> tags = new HashSet<>();
	
	/**
	 * @param agentid 企业应用的id，可在应用的设置页面查看
	 * @param content	消息内容，不能为 null 或空，且最长不超过2048个字节，注意：主页型应用推送的文本消息在微信端最多只显示20个字（包含中英文）
	 * @throws InvalidContentException 如果 content 为空，或超过 2048 个字节
	 */
	public TextMessage(int agentid, String content)throws InvalidContentException {
		this(agentid, content, false);
	}

	/**
	 * 
	 * @param agentid 企业应用的id，可在应用的设置页面查看
	 * @param content 消息内容，不能为 null 或空，且最长不超过2048个字节，注意：主页型应用推送的文本消息在微信端最多只显示20个字（包含中英文）
	 * @param safe 表示是否是保密消息，
	 * @throws InvalidContentException 如果 content 为空，或超过 2048 个字节
	 */
	public TextMessage(int agentid, String content, boolean safe)throws InvalidContentException {
		super();
		if(content.isEmpty()){
			throw new InvalidContentException("content is empty");
		}
		try {
			if(content.getBytes("UTF-8").length > 2048){
				throw new InvalidContentException("content is to large");
			}
		} catch (UnsupportedEncodingException e) {
			assert(false);
		}
		this.agentid = agentid;
		this.content = content;
		this.safe = (byte)(safe ? 1 : 0);
	}

	public void addUser(String uid){
		users.add(uid);
	}
	
	public void addDepartment(String department){
		departments.add(department);
	}
	
	public void addTag(String tag){
		tags.add(tag);
	}


	@Override
	public String pack() {
//		{
//			   "touser": "UserID1|UserID2|UserID3",
//			   "toparty": " PartyID1 | PartyID2 ",
//			   "totag": " TagID1 | TagID2 ",
//			   "msgtype": "text",
//			   "agentid": 1,
//			   "text": {
//			       "content": "Holiday Request For Pony(http://xxxxx)"
//			   },
//			   "safe":0
//			}
		Map<String,Object> temp = new HashMap<>();
		if(this.users.size() == 0 && this.departments.size() == 0 && this.tags.size() == 0){
			temp.put("touser", "@all");
		}else{
			if(this.users.size() > 0){
				temp.put("touser", link(users));
			}
			if(this.departments.size() > 0){
				temp.put("toparty", link(departments));
			}
			if(this.tags.size() > 0){
				temp.put("totag", link(tags));
			}
		}
		temp.put("msgtype", msgtype);
		temp.put("agentid", agentid);
		Map<String,String> content = new HashMap<>();
		content.put("content",this.content);
		temp.put("text", content);
		temp.put("safe", safe);
		return JSON.toJSONString(temp);
	}
	
	private String link(Collection<String> col){
		StringBuilder sb = new StringBuilder();
		int index = 0;
		for(String el : col){
			if(index > 0){
				sb.append("|");
			}
			sb.append(el);
			index++;
		}
		return sb.toString();
	}

	
}
