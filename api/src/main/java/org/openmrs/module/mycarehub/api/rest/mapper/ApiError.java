package org.openmrs.module.mycarehub.api.rest.mapper;


import com.google.gson.annotations.SerializedName;

public class ApiError {

  @SerializedName("message")
  private String message;

  public String getMessage() {
    return message;
  }
}
