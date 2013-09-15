package com.me.Health.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.me.Health.shared.PatienceInfo;


public class Health implements EntryPoint {
    // You must use a FormPanel to create a blobstore upload form
	final FormPanel uploadForm = new FormPanel();
	
	  // Use an RPC call to the Blob Service to get the blobstore upload url
	HealthServiceAsync healthService = GWT.create(HealthService.class);
	
	VerticalPanel mainVerticalPanel = new VerticalPanel();
	AbsolutePanel UploadProfilePicPanel = new AbsolutePanel ();
	
	HorizontalPanel adduserHorizontalPanel = new HorizontalPanel();
	HorizontalPanel hp2 = new HorizontalPanel();
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
	TextBox descriptionTextBox = new TextBox();
	TextBox addressTextBox = new TextBox();
	TextBox mobileNumberTextBox = new TextBox();
	TextBox ageTextBox = new TextBox();
	TextBox emailIdTextBox = new TextBox();
	TextBox diagnosisSpecifiedTextBox = new TextBox();
	TextBox helpNeedTextBox = new TextBox();
	  
	FileUpload uploadProfile = new FileUpload();
	Button submitProfileButton = new Button("Submit");
	  
	FileUpload uploadSupportingDoc = new FileUpload();
	Button submitDocButton = new Button("Submit");
	
	FlexTable addUserTable = new FlexTable();
	FlexTable resultsTable = new FlexTable();
	final Image image = new Image();

	@Override
	public void onModuleLoad() {
		loadImage();
		RootPanel.get("container").add(new HTML("<H1>Patience details</H1>"));
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
		addUserTable.setWidget(9, 0, xrayLabel);
		addUserTable.setWidget(9, 1, uploadSupportingDoc);
		addUserTable.setWidget(9, 2, submitDocButton);
		addUserTable.setHeight("50px");
		mainVerticalPanel.setSpacing(5);
		uploadForm.setWidget(mainVerticalPanel);
		
		// The upload form, when submitted, will trigger an HTTP call to the
		// servlet.  The following parameters must be set
		uploadForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		uploadForm.setMethod(FormPanel.METHOD_POST);
		
		// Set Names for the text boxes so that they can be retrieved from the
		// HTTP call as parameters
		uploadProfile.setName("uploadProfilePic");
		uploadSupportingDoc.setName("supportingDoc");
		
		RootPanel.get("container").add(uploadForm);
		
			submitProfileButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
				image.setVisible(true);
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
		});
		
		uploadForm
		    .addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			  @Override
			  public void onSubmitComplete(SubmitCompleteEvent event) {
			    	 
				//The submit complete Event Results will contain the unique
				//identifier for the picture's meta-data.  Trim it to remove
				//trailing spaces and line breaks
			    getPicture(event.getResults().trim());
			    image.setVisible(false);
			  }
			
		});
	}

	private void loadImage() {
		image.setUrl("/images/loading.gif");
		image.setVisible(false);
		AbsolutePanel loadImagePanel = new AbsolutePanel ();
		loadImagePanel.add(image);
		RootPanel.get("container").add(loadImagePanel);
	}

	public void getPicture(String id) {
		//Make another call to the Blob Service to retrieve the meta-data
		healthService.getPicture(id, new AsyncCallback<PatienceInfo>() {
		
			@Override
			public void onSuccess(PatienceInfo result) {
			Image image = new Image();
			image.setUrl(result.getProfileImageUrl());
			UploadProfilePicPanel.add(image);
			//Use Getters from the Picture object to load the FlexTable
			addUserTable.setWidget(1, 3, UploadProfilePicPanel);
			}
			
			@Override
			public void onFailure(Throwable caught) {
			caught.printStackTrace();
			}
		});
	}
}