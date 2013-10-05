package com.health.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.health.shared.PatientInfo;

public class Health implements EntryPoint {
	final FormPanel uploadForm = new FormPanel();
	
	HealthServiceAsync healthService = GWT.create(HealthService.class);
	
	VerticalPanel mainVerticalPanel = new VerticalPanel();
	AbsolutePanel UploadProfilePicPanel = new AbsolutePanel ();
	
	HorizontalPanel adduserHorizontalPanel = new HorizontalPanel();
	HTML nameLabel = new HTML("<b>Full Name :</b>");
	HTML profileImageUrlLabel = new HTML("<b>Upload Image Url :</b>");
	HTML descriptionLabel = new HTML("<b>Description :</b>");
	HTML addressLabel = new HTML("<b>Address :</b>");
	HTML mobileNumberLabel = new HTML("<b>Mobile Number :</b>");
	HTML ageLabel = new HTML("<b>Age :</b>");
	HTML emailIdLabel = new HTML("<b>Email Id :</b>");
	HTML diagnosisSpecifiedLabel = new HTML("<b> Diagnosis Specified :</b>");
	HTML helpNeedLabel = new HTML("<b> Help Need :</b>");
	HTML xrayLabel = new HTML("<b> Xray or other Document upload :</b>");
	 
	TextBox nameTextBox = new TextBox();
	TextArea descriptionTextBox = new TextArea();
	TextArea addressTextBox = new TextArea();
	TextBox mobileNumberTextBox = new TextBox();
	TextBox ageTextBox = new TextBox();
	TextBox emailIdTextBox = new TextBox();
	TextArea diagnosisSpecifiedTextBox = new TextArea();
	TextArea helpNeedTextBox = new TextArea();
	  
	FileUpload uploadProfile = new FileUpload();
	FileUpload uploadSupportingDoc = new FileUpload();

	Button submitProfileButton = new Button("Upload");
	Button submitDocButton = new Button("Upload");
	Button submitButton = new Button ("Submit ");
	
	FlexTable addUserTable = new FlexTable();
	FlexTable resultsTable = new FlexTable();
	final Image loadingImage = new Image();
	
	PatientInfo patientInfo =  new PatientInfo();
//	boolean profilePic = false;

	@Override
	public void onModuleLoad() {
		loadImage();
		mainVerticalPanel.add(new HTML("<H1> <div align=\"center\"> Add Patient </div></H1>"));
		adduserHorizontalPanel.add(addUserTable);
		adduserHorizontalPanel.add(UploadProfilePicPanel);
		mainVerticalPanel.add(adduserHorizontalPanel);
		addUserTable.setWidget(0, 0, nameLabel);
		addUserTable.setWidget(0, 1, nameTextBox);
		addUserTable.setWidget(1, 0, profileImageUrlLabel );
		addUserTable.setWidget(1, 1, uploadProfile);
		addUserTable.setWidget(1, 2, submitProfileButton);
		addUserTable.setWidget(2, 0, descriptionLabel);
		addUserTable.setWidget(2, 1, descriptionTextBox);
		addUserTable.setWidget(3, 0, addressLabel);
		addUserTable.setWidget(3, 1, addressTextBox);
		addUserTable.setWidget(4, 0, mobileNumberLabel);
		addUserTable.setWidget(4, 1, mobileNumberTextBox);
		addUserTable.setWidget(5, 0, ageLabel);
		addUserTable.setWidget(5, 1, ageTextBox);
		addUserTable.setWidget(6, 0, emailIdLabel);
		addUserTable.setWidget(6, 1, emailIdTextBox);
		addUserTable.setWidget(7, 0, diagnosisSpecifiedLabel);
		addUserTable.setWidget(7, 1, diagnosisSpecifiedTextBox);
		addUserTable.setWidget(8, 0, helpNeedLabel);
		addUserTable.setWidget(8, 1, helpNeedTextBox);
//		addUserTable.setWidget(9, 0, xrayLabel);
//		addUserTable.setWidget(9, 1, uploadSupportingDoc);
//		addUserTable.setWidget(9, 2, submitDocButton);
		addUserTable.setWidget(10, 1, submitButton);
		
		mainVerticalPanel.setSpacing(5);
		uploadForm.setWidget(mainVerticalPanel);
		
		// The upload form, when submitted, will trigger an HTTP call to the
		// servlet.  The following parameters must be set
		uploadForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		uploadForm.setMethod(FormPanel.METHOD_POST);
		
		// Set Names for the text boxes so that they can be retrieved from the
		// HTTP call as parameters
		
	//	
		uploadProfile.setName("uploadProfilePic");
	
		RootPanel.get("container").add(uploadForm);
		
		submitProfileButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				uploadRpc();
			}
		});
		
		submitDocButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				uploadSupportingDoc.setName("uploadProfilePic");
				uploadRpc();
			}
		});
		
		uploadForm
		    .addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			  @Override
			  public void onSubmitComplete(SubmitCompleteEvent event) {
			    	 
				//The submit complete Event Results will contain the unique
				//identifier for the picture's meta-data.  Trim it to remove
				//trailing spaces and line breaks
			    getPicture(event.getResults().trim());
			    loadingImage.setVisible(false);
			  }
		});
		
		
		submitButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				loadingImage.setVisible(true);
				patientInfo.setName(nameTextBox.getValue());
				patientInfo.setAddress(addressTextBox.getValue());
				if (ageTextBox.getValue().length() ==0) {
					patientInfo.setAge(0);
				} else {
					patientInfo.setAge(Integer.valueOf(ageTextBox.getValue()));
				}
				patientInfo.setDescription(descriptionTextBox.getValue());
				patientInfo.setDiagnosisSpecified(diagnosisSpecifiedTextBox.getValue());
				patientInfo.setEmailId(emailIdTextBox.getValue());
				patientInfo.setHelpNeed(helpNeedTextBox.getValue());
				patientInfo.setMobileNumber(mobileNumberTextBox.getValue());
				healthService
				        .storePatienceInfo(patientInfo, new AsyncCallback<PatientInfo>() {
					  @Override
					  public void onSuccess(PatientInfo result) {
						  mainVerticalPanel.clear();
						  Window.alert("Successfully Added");
						  Window.Location.reload();	
					  }
					
					  @Override
					  public void onFailure(Throwable caught) {
						  mainVerticalPanel.add(new HTML("<div style=\"color: red\" > <H1> Error while updating</H1> </div>"));
					  }
				  });
		}
		});
	}

	private void uploadRpc() {
		loadingImage.setVisible(true);
		healthService
		        .getBlobStoreUploadUrl(new AsyncCallback<String>() {
		
			  @Override
			  public void onSuccess(String result) {
			        	 
	            // Set the form action to the newly created
				// blobstore upload URL
				uploadForm.setAction(result.toString());
				// Submit the form to complete the upload
			    uploadForm.submit();
			    uploadForm.reset();
			  }
			
			  @Override
			  public void onFailure(Throwable caught) {
			    caught.printStackTrace();
			  }
		  });
	}
	
	private void loadImage() {
		loadingImage.setUrl("/images/loading.gif");
		loadingImage.setVisible(false);
		AbsolutePanel loadImagePanel = new AbsolutePanel ();
		loadImagePanel.add(loadingImage);
		RootPanel.get("container").add(loadImagePanel);
	}

	public void getPicture(String id) {
		//Make another call to the Blob Service to retrieve the meta-data
		healthService.getPicture(id, new AsyncCallback<PatientInfo>() {
		
			@Override
			public void onSuccess(PatientInfo result) {
				patientInfo= result;
				Image image = new Image();
				String url = result.getProfileImageUrl();
				image.setUrl(url);
				
					addUserTable.setWidget(1, 3, image);
					patientInfo.setProfileImageUrl(url);
//				} else {
//					addUserTable.setWidget(9, 3, image);
//					patientInfo.setXrayOrDocumentToSupport(url);
//				}
				//Use Getters from the Picture object to load the FlexTable
			}
			
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}
		});
	}
}