package com.health.shared;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Transient;

import com.google.appengine.api.datastore.PostLoad;

public class ChatInfo implements Serializable {

	@Id
	public Long id;
	private String chatInfo;
	private Date createDate;
	private boolean delete = false;
	private String msgDate = "";
	public String userName;
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMsgDate() {
		return msgDate;
	}

	public void setMsgDate(String msgDate) {
		this.msgDate = msgDate;
	}

	public boolean isDelete() {
		return delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@Transient Date loaded;
    @PostLoad void trackLoadedDate() { this.loaded = new Date(); }
	
	public String getChatInfo() {
		return chatInfo;
	}
	public void setChatInfo(String chatInfo) {
		this.chatInfo = chatInfo;
	}
	

}
