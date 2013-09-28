package com.health.client.chat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.health.shared.ChatInfo;


@RemoteServiceRelativePath("chatservice")
public interface ChatService extends RemoteService {
	String sentInfoToServer(String name) throws IllegalArgumentException;
	List<ChatInfo> getInfoFromServer();
}
