package com.health.client.chat;

import java.util.Date;
import java.util.List;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.health.shared.ChatInfo;

public class Chat implements EntryPoint {

	private final ChatServiceAsync chatService = GWT
			.create(ChatService.class);

	FlexTable setInfoFt = new FlexTable();
	FlexTable getChatInfoFt = new FlexTable();
	final Button sendButton = new Button("Send");
	final TextBox chatText = new TextBox();
	final HTML serverResponseLabel = new HTML();
	final Image image = new Image();
	VerticalPanel inputPanel = new VerticalPanel();
	VerticalPanel displayPanel = new VerticalPanel();
	Timer timer;
	final int TIMER_MILISECONDS = 2000;

	public void onModuleLoad() {
		loadImage();
		getChatInfoFt.addStyleName("FlexTable");
		getChatInfoFt.setWidth("500px");
		RootPanel.get("main").add(displayPanel);
		sendChat();
		RootPanel.get("main").add(inputPanel);
		displayPanel.add(getChatInfoFt);
		 getChatInfoRpc();
		 timer = new Timer() {
		      public void run() {
		    	  getChatInfoWithoutImageRpc();
		      }
		};
	}

	private void loadImage() {
		image.setUrl("/images/loading.gif");
		image.setVisible(false);
		AbsolutePanel loadImagePanel = new AbsolutePanel ();
		loadImagePanel.add(image);
		displayPanel.add(loadImagePanel);
	}

	private void getChatInfoRpc() {
		image.setVisible(true);
		chatService.getInfoFromServer(new AsyncCallback<List<ChatInfo>>() {

			@Override
			public void onFailure(Throwable caught) {
				image.setVisible(false);
		        Window.alert("Failure!");
			}
	
			public void onSuccess(List<ChatInfo> result) {
				image.setVisible(false);
				loadChaWithoutLoadImage(result);
			    timer.schedule(TIMER_MILISECONDS);
			}
		});
	}
	
	private void getChatInfoWithoutImageRpc() {
		chatService.getInfoFromServer(new AsyncCallback<List<ChatInfo>>() {
			@Override
			public void onFailure(Throwable caught) {
		        Window.alert("Failure!");
			}
	
			public void onSuccess(List<ChatInfo> result) {
				loadChaWithoutLoadImage(result);
			}
		});
	}

	private void loadChaWithoutLoadImage(List<ChatInfo> result) {
		int row = 1;
		getChatInfoFt.removeAllRows();
		getChatInfoFt.setText(0, 0, "Chat Message");
		getChatInfoFt.getRowFormatter().addStyleName(0,"FlexTable-Header");
		for (ChatInfo chat : result) {
			try {
			row = getChatInfoFt.getRowCount();
			getChatInfoFt.setText(row, 0, validTxt(chat.getChatInfo()));
			} catch (Exception e) {
				continue;
			}
			
			 HTMLTable.RowFormatter rf = getChatInfoFt.getRowFormatter();
			 for ( row = 1; row < getChatInfoFt.getRowCount(); ++row) {
			      if ((row % 2) != 0) {
			    	  rf.addStyleName(row, "FlexTable-OddRow");
			      }
			      else {
			    	  rf.addStyleName(row, "FlexTable-EvenRow");
			      }
			 }
		}
		chatText.setFocus(true);
	}
	
	public String validTxt(String str) {
		if (str == null || str.length() == 0) {
			return "";
		}
		return str;
	}
	
	public String validDate(Date date) {
		if (date == null || date.toString().length() == 0) {
			return "";
		}
		return DateTimeFormat.getFormat("dd/MM/yyyy").format(date);
	}
	
	private void sendChat() {
		inputPanel.add(setInfoFt);
		sendButton.addStyleName("sendButton");
		chatText.setFocus(true);
		sendButton.addStyleName("sendButton");
		setInfoFt.setWidget(0, 0, new HTML("<div align=\"center\"><b>Enter Text Information</b></div>"));
		setInfoFt.setWidget(0, 1, chatText);
		setInfoFt.setWidget(0, 3, sendButton);
	
		class MyHandler implements ClickHandler, KeyUpHandler {
			public void onClick(ClickEvent event) {
				sendChatInformation();
			}

			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					sendChatInformation();
				}
			}
		}
		chatText.addKeyDownHandler(new KeyDownHandler() {
		    @Override
		    public void onKeyDown(KeyDownEvent event) {
			     if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
			    	 sendChatInformation();
	           }
		    }
		});
		chatText.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				timer.schedule(TIMER_MILISECONDS);
				chatText.setFocus(true);
			}
		});
		MyHandler handler = new MyHandler();
		sendButton.addClickHandler(handler);
	}

	/**
	 * Send the name from the nameField to the server and wait for a response.
	 */
	private void sendChatInformation() {
		image.setVisible(true);
		// First, we validate the input.
		String chatInfo = chatText.getText();
		if (chatInfo.length()  > 0 ) {
		chatService.sentInfoToServer(chatInfo,
				new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						image.setVisible(false);
					}

					public void onSuccess(String result) {
						if (result.contains("Error")) {
							Window.alert(result);
							image.setVisible(false);
							return;
						}
						chatText.setText("");
						getChatInfoRpc();
						chatText.setFocus(true);
					}
				});
		} else {
			image.setVisible(false);
			Window.alert("Empty string, please enter valid name ");
		}
	}
}