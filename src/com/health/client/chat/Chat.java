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
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.health.shared.ChatInfo;

public class Chat implements EntryPoint {

	  private final ChatServiceAsync chatService = GWT
			.create(ChatService.class);

	  FlexTable setInfoFt = new FlexTable();
	  FlexTable getChatInfoFt = new FlexTable();
	  static FlexTable infoFlexTable = new FlexTable();
	  final Button sendButton = new Button("Send");
	  final Button insertSmiley = new Button("Insert  Smiley");
	  VerticalPanel smileyPanel = new VerticalPanel();
	  HTMLPanel smileyLabel = new HTMLPanel("<b>Please click the smiley icon down to insert.</b> <br> ");
	
	  final static TextArea  chatText = new TextArea ();
	  final HTML serverResponseLabel = new HTML();
	  final Image image = new Image();
	  VerticalPanel inputPanel = new VerticalPanel();
	  VerticalPanel displayPanel = new VerticalPanel();
	  Timer timer;
	  final int TIMER_MILISECONDS = 2000;
	
	  final HTMLPanel enterUserNamehtmlPanel = new HTMLPanel("<div style=' background-color: #FFFFFF; padding: 5px;font-weight: bold;'>Enter User Name</div>");
	  final TextBox userNameTxt = new TextBox();
	  final Button addUserButton = new Button("Add User");
	  HorizontalPanel addUserPanel = new HorizontalPanel();
	  String userName = "";
	  VerticalPanel infoPanel = new VerticalPanel();
		
	  public void onModuleLoad() {
		RootPanel.get("main").add(addUserPanel);
		addUser();
		getChatInfoFt.addStyleName("FlexTable");
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
		
		smileyPanel.add(smileyLabel);
		smileyPanel.add(infoFlexTable);
		smileyPanel.setVisible(false);
		RootPanel.get("main").add(smileyPanel);
		infoPanel.add(new HTMLPanel("<b>This is chat to everyone, use chrome for better perfomance. Any technical issue please email to : sureshgbabu85@gmail.com.</b> <br> "));
		RootPanel.get("main").add(infoPanel);
	}
	
	public void displayChatIfUserPresent() {
		 userName = userNameTxt.getText();
		 if(userName!= null &&  userName.length() >= 1) {
			 displayChat();
		 } else {
			 Window.alert("Please enter user name ");
		 }
	}
	
	public void displayChat() {
		displayPanel.setVisible(true);
		inputPanel.setVisible(true);
		addUserPanel.setVisible(false);
		chatText.setFocus(true);
	}
	
	public void displayAddUser() {
		displayPanel.setVisible(false);
		inputPanel.setVisible(false);
		addUserPanel.setVisible(true);
	}

	private void addUser() {
		displayAddUser();
		addUserPanel.add(enterUserNamehtmlPanel);
		addUserPanel.add(userNameTxt);
		addUserPanel.add(addUserButton);
		
		userNameTxt.addKeyDownHandler(new KeyDownHandler() {
		    @Override
		    public void onKeyDown(KeyDownEvent event) {
		    	if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
		    		displayChatIfUserPresent();
		    	}
		    }
		});
		
		class AddUserHandler implements ClickHandler, KeyUpHandler {
			public void onClick(ClickEvent event) {
				 displayChatIfUserPresent();
			}

			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					 displayChatIfUserPresent();
				}
			}
		}
		AddUserHandler handler = new AddUserHandler();
		addUserButton.addClickHandler(handler);
	}

	private void loadImage() {
		image.setUrl("/images/loading.gif");
		image.setVisible(false);
		AbsolutePanel loadImagePanel = new AbsolutePanel ();
		loadImagePanel.add(image);
		RootPanel.get("main").add(loadImagePanel);
		

		//VerticalPanel infoPanel = new VerticalPanel();
		
		
		infoPanel.add(new HTMLPanel("<b>This is chat to everyone, use Chrome for better perfomance.</b> <br>  <h2>Smileys you can use are as follows :<h2>"));
		infoPanel.add(infoFlexTable);

		infoFlexTable.addStyleName("infoStyle");
		infoFlexTable.setWidget(0, 0, new HTMLPanel("<img  class=\"laugh\"></img>"));
		infoFlexTable.setWidget(0, 1, new HTMLPanel("<img  class=\"bulb\"></img>"));
		infoFlexTable.setWidget(0, 2 , new HTMLPanel("<img  class=\"fever\"></img>"));
		infoFlexTable.setWidget(0, 3, new HTMLPanel("<img  class=\"music\"></img>"));
		infoFlexTable.setWidget(0, 4, new HTMLPanel("<img  class=\"question\"></img>"));
		
		infoFlexTable.setWidget(1, 0, new HTMLPanel("<img  class=\"exclamator\"></img>"));
		infoFlexTable.setWidget(1, 1, new HTMLPanel("<img  class=\"thumbsdown\"></img>"));
		infoFlexTable.setWidget(1, 2 , new HTMLPanel("<img  class=\"thumbsup\"></img>"));
		infoFlexTable.setWidget(1, 3, new HTMLPanel("<img  class=\"call\"></img>"));
		infoFlexTable.setWidget(1, 4, new HTMLPanel("<img  class=\"rofl\"></img>"));

		infoFlexTable.setWidget(2, 0, new HTMLPanel("<img  class=\"cry\"></img>"));
		infoFlexTable.setWidget(2, 1, new HTMLPanel("<img  class=\"oh\"></img>"));
		infoFlexTable.setWidget(2, 2 , new HTMLPanel("<img  class=\"ah\"></img>"));
		infoFlexTable.setWidget(2, 3, new HTMLPanel("<img  class=\"ahh\"></img>"));
		infoFlexTable.setWidget(2, 4, new HTMLPanel("<img  class=\"cool\"></img>"));
		
		infoFlexTable.setWidget(3, 0, new HTMLPanel("<img  class=\"angry\"></img>"));
		infoFlexTable.setWidget(3, 1, new HTMLPanel("<img  class=\"angry1\"></img>"));
		infoFlexTable.setWidget(3, 2 , new HTMLPanel("<img  class=\"wink\"></img>"));
		infoFlexTable.setWidget(3, 3, new HTMLPanel("<img  class=\"love\"></img>"));
		infoFlexTable.setWidget(3, 4, new HTMLPanel("<img  class=\"crazy\"></img>"));
		
		infoFlexTable.setWidget(4, 0, new HTMLPanel("<img  class=\"smile\"></img>"));
		infoFlexTable.setWidget(4, 1, new HTMLPanel("<img  class=\"smile1\"></img>"));
		infoFlexTable.setWidget(4, 2 , new HTMLPanel("<img  class=\"ps\"></img>"));
		infoFlexTable.setWidget(4, 3, new HTMLPanel("<img  class=\"sad\"></img>"));
		infoFlexTable.setWidget(4, 4, new HTMLPanel("<img  class=\"worry\"></img>"));
		
		infoFlexTable.setWidget(5, 0, new HTMLPanel("<img  class=\"mad\"></img>"));
		infoFlexTable.setWidget(5, 1, new HTMLPanel("<img  class=\"confused\"></img>"));
		infoFlexTable.setWidget(5, 2 , new HTMLPanel("<img  class=\"smirk\"></img>"));
		infoFlexTable.setWidget(5, 3, new HTMLPanel("<img  class=\"kiss\"></img>"));
		infoFlexTable.setWidget(5, 4, new HTMLPanel("<img  class=\"shut\"></img>"));

		infoFlexTable.setWidget(6, 0, new HTMLPanel("<img  class=\"party\"></img>"));
		infoFlexTable.setWidget(6, 1, new HTMLPanel("<img  class=\"cat\"></img>"));
		infoFlexTable.setWidget(6, 2 , new HTMLPanel("<img  class=\"nerd\"></img>"));
		infoFlexTable.setWidget(6, 3, new HTMLPanel("<img  class=\"devil\"></img>"));
		infoFlexTable.setWidget(6, 4, new HTMLPanel("<img  class=\"angel\"></img>"));
		
		infoFlexTable.setWidget(7, 0, new HTMLPanel("<img  class=\"kissed\"></img>"));
		infoFlexTable.setWidget(7, 1, new HTMLPanel("<img  class=\"money\"></img>"));
		infoFlexTable.setWidget(7, 2 , new HTMLPanel("<img  class=\"tense\"></img>"));
		infoFlexTable.setWidget(7, 3, new HTMLPanel("<img  class=\"cap\"></img>"));
		infoFlexTable.setWidget(7, 4, new HTMLPanel("<img  class=\"gloom\"></img>"));
		
		infoFlexTable.addClickHandler( new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                int cellIndex = infoFlexTable.getCellForEvent(event).getCellIndex();
                int rowIndex = infoFlexTable.getCellForEvent(event).getRowIndex();
                chatText.setText(chatText.getText() + smileysText[rowIndex][cellIndex]);
                smileyPanel.setVisible(false);
            }	
        });

	}
	
	static String[][] smileysText = {
		{ ":D", "(bulb)", "(fever)", "(music)", "(question)" },
		{ "(exclamator)", "(thumbsdown)", "(thumbsup)", "(call)", "(rofl)" },
		{ "(cry)", "(oh)", "(ah)", "(ahh)", "(cool)" },
		{ "(angry)", "(angry1)", "(wink)", "(love)", "(crazy)" },
		{ "(smile)", "(smile1)", "(ps)", "(sad)", "(worry)" },
		{ "(mad)", "(confused)", "(smirk)", "(kiss)", "(shut)" },
		{ "(party)", "(cat)", "(nerd)", "(devil)", "(angel)" },
		{ "(kissed)", "(money)", "(tense)", "(cap)", "(gloom)" }
	};
	
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
		getChatInfoFt.setText(0, 0,  "User Name(Nick Name)");
		getChatInfoFt.setText(0, 1,  "Chat Message");
		getChatInfoFt.setText(0, 2,  "Time Stamp");
		getChatInfoFt.getRowFormatter().addStyleName(0,"FlexTable-Header");
		for (ChatInfo chat : result) {
			try {
				row = getChatInfoFt.getRowCount();
				String htmlStr = validTxt(chat.getChatInfo());
				HTMLPanel htmlPanel = new HTMLPanel(htmlStr);
				getChatInfoFt.setText(row, 0, chat.getUserName());
				getChatInfoFt.getCellFormatter().addStyleName(row, 0,  "userNameColumn");
				getChatInfoFt.setWidget(row, 1, htmlPanel);
				getChatInfoFt.getFlexCellFormatter().setWidth(row, 1, "600px");
				getChatInfoFt.setText(row, 2, chat.getMsgDate());
				
				getChatInfoFt.getCellFormatter().addStyleName(row, 2,   "timeStampColumn");
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
		if(addUserPanel.isVisible() == false) {
			chatText.setFocus(true);
		}
		
		Window.scrollTo (Window.getClientWidth() ,Window.getClientWidth());
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
		chatText.setCharacterWidth(50);
		chatText.setVisibleLines(5);
		inputPanel.add(setInfoFt);
		sendButton.addStyleName("sendButton");
		chatText.setFocus(true);
		sendButton.addStyleName("sendButton");
		setInfoFt.setWidget(0, 0, new HTML("<div align=\"center\"><b>Enter Chat Message Here :</b></div>"));
		setInfoFt.setWidget(0, 1, chatText);
		setInfoFt.setWidget(0, 3, sendButton);
		setInfoFt.setWidget(1, 1, insertSmiley);
		insertSmiley.addClickHandler(new ClickHandler() {
	         @Override
	         public void onClick(ClickEvent event) {
	        	 smileyPanel.setVisible(true);
	        	 Window.scrollTo (Window.getClientWidth() ,Window.getClientWidth());
	         }
	      });
		
	
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
		String chatInfo = chatText.getText().trim();
		boolean notEmpty = chatInfo.length() > 0;
		
		chatInfo = chatInfo.replaceAll(":D", "<img  class=\"laugh\"></img>");
		chatInfo = chatInfo.replaceAll("\\(bulb\\)", "<img  class=\"bulb\"></img>");
		chatInfo = chatInfo.replaceAll("\\(fever\\)", "<img  class=\"fever\"></img>");
		chatInfo = chatInfo.replaceAll("\\(music\\)", "<img  class=\"music\"></img>");
		chatInfo = chatInfo.replaceAll("\\(question\\)", "<img  class=\"question\"></img>");
		
		chatInfo = chatInfo.replaceAll("\\(exclamator\\)", "<img  class=\"exclamator\"></img>");
		chatInfo = chatInfo.replaceAll("\\(thumbsdown\\)", "<img  class=\"thumbsdown\"></img>");
		chatInfo = chatInfo.replaceAll("\\(thumbsup\\)", "<img  class=\"thumbsup\"></img>");
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
		chatInfo = chatInfo.replaceAll("\\(:x\\)", "<img  class=\"shut\"></img>");
		

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
		if (notEmpty) {
		chatService.sentInfoToServer(chatInfo, userName,
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