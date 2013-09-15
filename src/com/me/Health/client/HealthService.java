package com.me.Health.client;


import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.me.Health.shared.PatienceInfo;

@RemoteServiceRelativePath("healthservice")
public interface HealthService extends RemoteService {

  String getBlobStoreUploadUrl();

  PatienceInfo getPicture(String id);

}
