package com.health.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.health.client.chat.ChatService;
import com.health.shared.ChatInfo;

public class ChatServiceImpl extends RemoteServiceServlet implements
	ChatService {
	
	private static final long serialVersionUID = 1L;
	Objectify ofy = ObjectifyService.begin();
	
	//Register the Objectify Service for the Picture entity
	static {
		ObjectifyService.register(ChatInfo.class);
	}
	final int MAX_CHAT_LIMIT = 25;
	
	public String sentInfoToServer(String chatInfo, String userName) throws IllegalArgumentException {
		
		String lowerCase  = chatInfo.toLowerCase();
		if(lowerCase.contains("sex") || lowerCase.contains("fuck") || lowerCase.contains("shit")) {
			return "Error : please dont entery bad words, you chat messsage is " + chatInfo;
		}
		ChatInfo chat = new ChatInfo();
		chat.setChatInfo(chatInfo);
		chat.setUserName(userName);
		Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+530"));
		cal.setTime(new Date());
		chat.setCreateDate(cal.getTime());
		
		SimpleDateFormat sdf = 
			      new SimpleDateFormat ("E dd-MM-yyyy hh:mm:ss a zzz");
		sdf.setTimeZone(TimeZone.getTimeZone("IST"));
		chat.setMsgDate(sdf.format(new Date()));

		ofy.put(chat);
		
		List<ChatInfo>  chatList = ofy.query(ChatInfo.class).order("createDate").list();
		int row = 0 ;
		
//		List<Key<ChatInfo>> chatInfoGreaterThan25RowsKeys = new ArrayList<Key<ChatInfo>>();
		if (chatList.size() > MAX_CHAT_LIMIT) {
			int count = chatList.size();
			while (count >= MAX_CHAT_LIMIT) {
				chatList.get(row).setDelete(true);
//				chatInfoGreaterThan25RowsKeys.add(new Key<ChatInfo>(ChatInfo.class, chatList.get(row).id));
				count--;
				row++;
			}
		}
//		ofy.delete(chatInfoGreaterThan25RowsKeys);
		ofy.put(chatList);
		return "Added " + chatInfo + " successfully";
	}
	
	@Override
	public List<ChatInfo> getInfoFromServer() {
		List<ChatInfo>  chatList = ofy.query(ChatInfo.class).filter("delete", false).order("createDate").list();
		for(ChatInfo  chat : chatList) {
			log("Retreived chat " + chat.getChatInfo());
		}
		//Loop the query results and add to the array
		log("----------Retreived " + chatList.size() + " rows");
		return chatList;
	}
}
