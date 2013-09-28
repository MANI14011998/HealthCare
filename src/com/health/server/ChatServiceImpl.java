package com.health.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;
import com.health.client.chat.ChatService;
import com.health.shared.ChatInfo;


public class ChatServiceImpl extends RemoteServiceServlet implements
	ChatService {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Objectify ofy = ObjectifyService.begin();
	
	//Register the Objectify Service for the Picture entity
	static {
	ObjectifyService.register(ChatInfo.class);
	}
	
	public String sentInfoToServer(String chatInfo) throws IllegalArgumentException {
		ChatInfo chat = new ChatInfo();
		chat.setChatInfo(chatInfo);
		chat.setCreateDate(new Date());
		ofy.put(chat);
		return "Added " + chatInfo + " successfully";
	}
	
	@Override
	public List<ChatInfo> getInfoFromServer() {
		List<ChatInfo>  chatList = ofy.query(ChatInfo.class).order("createDate").list();
		for(ChatInfo  chat : chatList) {
			log("Retreived chat " + chat.getChatInfo());
		}
		//Loop the query results and add to the array
		log("----------Retreived " + chatList.size() + " rows");
		return chatList;
	}

}
