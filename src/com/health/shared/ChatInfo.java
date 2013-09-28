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
