package com.health.shared;

import java.io.Serializable;

import javax.persistence.Id;

@SuppressWarnings("serial")
public class PatientInfo implements Serializable {

	@Id
	public Long id;
	private String name;
	public String profileImageUrl;
	private String diagnosisSpecified;
	public String description;
	public String mobileNumber;
	public int age;
	public String emailId;
	public String address;
	public String helpNeed;
	public String xrayOrDocumentToSupport;

	public String getXrayOrDocumentToSupport() {
		return xrayOrDocumentToSupport;
	}

	public void setXrayOrDocumentToSupport(String xrayOrDocumentToSupport) {
		this.xrayOrDocumentToSupport = xrayOrDocumentToSupport;
	}

	public String getHelpNeed() {
		return helpNeed;
	}

	public void setHelpNeed(String helpNeed) {
		this.helpNeed = helpNeed;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
	public String getDiagnosisSpecified() {
		return diagnosisSpecified;
	}

	public void setDiagnosisSpecified(String diagnosisSpecified) {
		this.diagnosisSpecified = diagnosisSpecified;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	
	public String getMobileNumber() {
		return mobileNumber;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getProfileImageUrl() {
	return profileImageUrl;
	}
	
	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}
	  
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}
}