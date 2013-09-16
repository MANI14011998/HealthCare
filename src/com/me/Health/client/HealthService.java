package com.me.health.client;


import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.me.health.shared.PatientInfo;

@RemoteServiceRelativePath("healthservice")
public interface HealthService extends RemoteService {

  String getBlobStoreUploadUrl();

  PatientInfo getPicture(String id);

}
