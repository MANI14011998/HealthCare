package com.me.Health.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.me.Health.client.HealthService;
import com.me.Health.shared.PatientInfo;

@SuppressWarnings("serial")
public class HealthServiceImpl extends RemoteServiceServlet implements
    HealthService {

  //Start a GAE BlobstoreService session and Objectify session
  BlobstoreService blobstoreService = BlobstoreServiceFactory
      .getBlobstoreService();
  Objectify ofy = ObjectifyService.begin();
  
  //Register the Objectify Service for the PatienceInfo entity
  static {
    ObjectifyService.register(PatientInfo.class);
  }

  //Generate a Blobstore Upload URL from the GAE BlobstoreService
  @Override
  public String getBlobStoreUploadUrl() {

    //Map the UploadURL to the uploadservice which will be called by
    //submitting the FormPanel
    return blobstoreService
        .createUploadUrl("/health/uploadservice");
  }

  //Retrieve the Blob's meta-data from the Datastore using Objectify
  @Override
  public PatientInfo getPicture(String id) {
    
    long l = Long.parseLong(id);
    PatientInfo picture = ofy.get(PatientInfo.class, l);
    return picture;
  }
  
  //Override doGet to serve blobs.  This will be called automatically by the Image Widget
  //in the client
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

        BlobKey blobKey = new BlobKey(req.getParameter("blob-key"));
        blobstoreService.serve(blobKey, resp);

  }
}