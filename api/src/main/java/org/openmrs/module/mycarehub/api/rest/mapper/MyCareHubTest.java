package org.openmrs.module.mycarehub.api.rest.mapper;


import com.google.gson.annotations.SerializedName;

public class MyCareHubTest {

  @SerializedName("test")
  private String testName;

  @SerializedName("testConceptId")
  private String testConceptId;

  @SerializedName("testDateTime")
  private String testDateTime;

  @SerializedName("result")
  private String result;

  @SerializedName("resultConceptId")
  private String resultConceptId;

  public void setTestName(String testName) {
    this.testName = testName;
  }

  public String getTestName() {
    return testName;
  }

  public void setTestConceptId(Integer testConceptId) {
    setTestConceptId(String.valueOf(testConceptId));
  }

  public void setTestConceptId(String testConceptId) {
    this.testConceptId = testConceptId;
  }

  public String getTestConceptId() {
    return testConceptId;
  }

  public void setTestDateTime(String testDateTime) {
    this.testDateTime = testDateTime;
  }

  public String getTestDateTime() {
    return testDateTime;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public String getResult() {
    return result;
  }

  public void setResultConceptId(Integer resultConceptId) {
    setResultConceptId(String.valueOf(resultConceptId));
  }

  public void setResultConceptId(String resultConceptId) {
    this.resultConceptId = resultConceptId;
  }

  public String getResultConceptId() {
    return resultConceptId;
  }
}
