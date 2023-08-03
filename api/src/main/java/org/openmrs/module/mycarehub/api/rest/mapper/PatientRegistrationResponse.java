package org.openmrs.module.mycarehub.api.rest.mapper;


import com.google.gson.annotations.SerializedName;

public class PatientRegistrationResponse {

  @SerializedName("success")
  private Boolean success;

  public PatientRegistrationResponse(Boolean success) {
    this.success = success;
  }
}
