package com.me.Health.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.me.Health.shared.PatientInfo;

public interface HealthServiceAsync {

  void getBlobStoreUploadUrl(AsyncCallback<String> callback);

  void getPicture(String id, AsyncCallback<PatientInfo> callback);

}