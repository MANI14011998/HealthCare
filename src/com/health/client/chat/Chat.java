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
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.Image;
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
		loadImage();
	}

	private void loadImage() {
		image.setUrl("/images/loading.gif");
		image.setVisible(false);
		AbsolutePanel loadImagePanel = new AbsolutePanel ();
		loadImagePanel.add(image);
		RootPanel.get("main").add(loadImagePanel);
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
				String htmlStr = validTxt(chat.getChatInfo());
				HTMLPanel htmlPanel = new HTMLPanel(htmlStr);
				
				getChatInfoFt.setWidget(row, 0, htmlPanel);
			} catch (Exception e) {
				System.out.println("The out put are " + e);
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

	private void sendChatInformation() {
		image.setVisible(true);
		String chatInfo = chatText.getText();
		
		chatInfo = chatInfo.replaceAll(":D", "<img  class=\"laugh\"></img>");
		chatInfo = chatInfo.replaceAll("\\(blub\\)", "<img  class=\"blub\"></img>");
		chatInfo = chatInfo.replaceAll("\\(fever\\)", "<img  class=\"fever\"></img>");
		chatInfo = chatInfo.replaceAll("\\(music\\)", "<img  class=\"music\"></img>");
		chatInfo = chatInfo.replaceAll("\\(question\\)", "<img  class=\"question\"></img>");
		
		chatInfo = chatInfo.replaceAll("\\(exclamator\\)", "<img  class=\"exclamator\"></img>");
		chatInfo = chatInfo.replaceAll("\\(coin\\)", "<img  class=\"coin\"></img>");
		chatInfo = chatInfo.replaceAll("\\(thumsup\\)", "<img  class=\"thumsup\"></img>");
		chatInfo = chatInfo.replaceAll("\\(call\\)", "<img  class=\"call\"></img>");
		chatInfo = chatInfo.replaceAll("\\(rofl\\)", "<img  class=\"rofl\"></img>");
		
		chatInfo = chatInfo.replaceAll("\\(cry\\)", "<img  class=\"cry\"></img>");
		chatInfo = chatInfo.replaceAll(":'\\(", "<img  class=\"cry\"></img>");
		chatInfo = chatInfo.replaceAll("\\(oh\\)", "<img  class=\"oh\"></img>");
		chatInfo = chatInfo.replaceAll("\\(ah\\)", "<img  class=\"ah\"></img>");
		chatInfo = chatInfo.replaceAll("\\(ahh\\)", "<img  class=\"ahh\"></img>");
		chatInfo = chatInfo.replaceAll("\\(cool\\)", "<img  class=\"cool\"></img>");
		
		chatInfo = chatInfo.replaceAll("\\(angry\\)", "<img  class=\"angry\"></img>");
		chatInfo = chatInfo.replaceAll("\\(angry1\\)", "<img  class=\"angry1\"></img>");
		chatInfo = chatInfo.replaceAll("\\(wink\\)", "<img  class=\"wink\"></img>");
		chatInfo = chatInfo.replaceAll("\\(love\\)", "<img  class=\"love\"></img>");
		chatInfo = chatInfo.replaceAll("<3", "<img  class=\"love\"></img>");
		chatInfo = chatInfo.replaceAll("\\(crazy\\)", "<img  class=\"crazy\"></img>");
		
		chatInfo = chatInfo.replaceAll("\\(smile\\)", "<img  class=\"smile\"></img>");
		chatInfo = chatInfo.replaceAll(":\\)", "<img  class=\"smile\"></img>");
		chatInfo = chatInfo.replaceAll("\\(smile1\\)", "<img  class=\"smile1\"></img>");
		chatInfo = chatInfo.replaceAll("\\(ps\\)", "<img  class=\"ps\"></img>");
		chatInfo = chatInfo.replaceAll(":p", "<img  class=\"ps\"></img>");
		chatInfo = chatInfo.replaceAll("\\(sad\\)", "<img  class=\"sad\"></img>");
		chatInfo = chatInfo.replaceAll(":\\(", "<img  class=\"sad\"></img>");
		chatInfo = chatInfo.replaceAll("\\(worry\\)", "<img  class=\"worry\"></img>");

		chatInfo = chatInfo.replaceAll("\\(mad\\)", "<img  class=\"mad\"></img>");
		chatInfo = chatInfo.replaceAll("\\(confused\\)", "<img  class=\"confused\"></img>");
		chatInfo = chatInfo.replaceAll("\\(smirk\\)", "<img  class=\"smirk\"></img>");
		chatInfo = chatInfo.replaceAll("\\(kiss\\)", "<img  class=\"kiss\"></img>");
		chatInfo = chatInfo.replaceAll("\\(shut\\)", "<img  class=\"shut\"></img>");

		chatInfo = chatInfo.replaceAll("\\(party\\)", "<img  class=\"party\"></img>");
		chatInfo = chatInfo.replaceAll("\\(cat\\)", "<img  class=\"cat\"></img>");
		chatInfo = chatInfo.replaceAll("\\(nerd\\)", "<img  class=\"nerd\"></img>");
		chatInfo = chatInfo.replaceAll("\\(devil\\)", "<img  class=\"devil\"></img>");
		chatInfo = chatInfo.replaceAll("\\(angel\\)", "<img  class=\"angel\"></img>");
		
		chatInfo = chatInfo.replaceAll("\\(kissed\\)", "<img  class=\"kissed\"></img>");
		chatInfo = chatInfo.replaceAll("\\(money\\)", "<img  class=\"money\"></img>");
		chatInfo = chatInfo.replaceAll("\\(tense\\)", "<img  class=\"tense\"></img>");
		chatInfo = chatInfo.replaceAll("\\(cap\\)", "<img  class=\"cap\"></img>");
		chatInfo = chatInfo.replaceAll("\\(gloom\\)", "<img  class=\"gloom\"></img>");
		
		chatInfo = "<div  class=\"img\">" + chatInfo + "</div>";
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