package com.me.health.gwt.server;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.me.health.shared.PatientInfo;

//The FormPanel must submit to a servlet that extends HttpServlet  
//RemoteServiceServlet cannot be used
@SuppressWarnings("serial")
public class UploadServiceImpl extends HttpServlet {

  //Start Blobstore and Objectify Sessions
  BlobstoreService blobstoreService = BlobstoreServiceFactory
      .getBlobstoreService();
  Objectify ofy = ObjectifyService.begin();

  static {
   // ObjectifyService.register(PatienceInfo.class);
  }

  //Override the doPost method to store the Blob's meta-data
  public void doPost(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {

    Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(req);
    BlobKey profileBlobKey = blobs.get("uploadProfilePic");
    BlobKey supportingDocBlobKey = blobs.get("supportingDoc");

    //Get the paramters from the request to populate the Picture object
    PatientInfo patienceInfo = new PatientInfo();
    //Map the ImageURL to the blobservice servlet, which will serve the image
    patienceInfo.setProfileImageUrl("/health/healthservice?blob-key=" + profileBlobKey.getKeyString());

    patienceInfo.setXrayOrDocumentToSupport("/health/healthservice?blob-key=" + profileBlobKey.getKeyString());
    ofy.put(patienceInfo);

    //Redirect recursively to this servlet (calls doGet)
    res.sendRedirect("/health/uploadservice?id=" + patienceInfo.id);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    //Send the meta-data id back to the client in the HttpServletResponse response
    String id = req.getParameter("id");
    resp.setHeader("Content-Type", "text/html");
    resp.getWriter().println(id);

  }

}
