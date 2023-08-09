package org.openmrs.module.mycarehub.model;


import java.util.Date;
import org.openmrs.BaseOpenmrsData;

/**
 * A record of how the patient is feeling, selected by their mood How the patient is feeling at a
 * particular moment. Their mood The mood options follow a Likert scale like nature and have 2
 * extremes For example,the mood options on myCareHub are: 1. VERY_HAPPY 2. HAPPY 3. MEH 4. SAD 5.
 * VERY_SAD Aside from the mood, the client also enters a `Note` which describes their mood If a
 * client has selected MEH, SAD or VERY_SAD as their mood, a service request is triggered by the
 * myCareHub platform and the service request is synchronized to KenyaEMR via the myCareHub module
 * Health Diary entries are recorded once a day (every 24hrs)
 */
public class HealthDiary extends BaseOpenmrsData {

  private Integer id;

  /** The patient's Comprehensive Care Clinic(CCC) number */
  private String cccNumber;

  // The actual mood; how they are feeling
  private String mood;

  // A description of their mood
  private String note;

  // The date the mood was recorded
  private Date dateRecorded;

  private String entryType;

  private Date sharedOn;

  // The client's contact information; their phone number
  private String clientContact;

  // The client's names. e.g John Doe
  private String clientName;

  @Override
  public Integer getId() {
    return id;
  }

  @Override
  public void setId(Integer id) {
    this.id = id;
  }

  public String getMood() {
    return mood;
  }

  public void setMood(String mood) {
    this.mood = mood;
  }

  public Date getDateRecorded() {
    return dateRecorded;
  }

  public void setDateRecorded(Date dateRecorded) {
    this.dateRecorded = dateRecorded;
  }

  public String getCccNumber() {
    return cccNumber;
  }

  public void setCccNumber(String cccNumber) {
    this.cccNumber = cccNumber;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public String getEntryType() {
    return entryType;
  }

  public void setEntryType(String entryType) {
    this.entryType = entryType;
  }

  public Date getSharedOn() {
    return sharedOn;
  }

  public void setSharedOn(Date sharedOn) {
    this.sharedOn = sharedOn;
  }

  public String getClientContact() {
    return clientContact;
  }

  public void setClientContact(String clientContact) {
    this.clientContact = clientContact;
  }

  public String getClientName() {
    return clientName;
  }

  public void setClientName(String clientName) {
    this.clientName = clientName;
  }
}
