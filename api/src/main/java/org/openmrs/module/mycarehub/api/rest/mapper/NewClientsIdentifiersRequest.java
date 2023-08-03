package org.openmrs.module.mycarehub.api.rest.mapper;


import com.google.gson.annotations.SerializedName;

public class NewClientsIdentifiersRequest {

  @SerializedName("facility")
  private String facility;

  @SerializedName("lastsynctime")
  private String lastsynctime;

  public NewClientsIdentifiersRequest(String facility, String lastsynctime) {
    this.facility = facility;
    this.lastsynctime = lastsynctime;
  }

  public String getFacility() {
    return facility;
  }

  public void setFacility(String facility) {
    this.facility = facility;
  }

  public String getLastsynctime() {
    return lastsynctime;
  }

  public void setLastsynctime(String lastsynctime) {
    this.lastsynctime = lastsynctime;
  }
}
