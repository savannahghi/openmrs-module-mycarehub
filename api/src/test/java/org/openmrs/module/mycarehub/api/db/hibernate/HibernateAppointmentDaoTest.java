package org.openmrs.module.mycarehub.api.db.hibernate;

import static org.junit.Assert.assertEquals;
import static org.openmrs.module.mycarehub.api.db.hibernate.MyCareHubPatientDaoTest.MCH_CONSENTED_PATIENT_XML_FILENAME;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.mycarehub.api.db.AppointmentDao;
import org.openmrs.module.mycarehub.model.AppointmentRequests;
import org.openmrs.module.mycarehub.model.ConsentedPatient;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

public class HibernateAppointmentDaoTest extends BaseModuleContextSensitiveTest {

  private AppointmentDao appointmentDao;
  @Autowired public DbSessionFactory sessionFactory;

  //  protected static final String STANDARD_DATASET_XML_FILENAME =
  //      "org.openmrs.module.mycarehub/api/include/StandardDataset.xml";
  protected static final String INITIAL_APPOINTMENT_XML_FILENAME =
      "org.openmrs.module.mycarehub/api/include/AppointmentRequest_Initial.xml";

  @Override
  public Boolean useInMemoryDatabase() {
    // Defaults to true. If you want to use your own database specified in your runtime properties
    // return false instead, otherwise return super.useInMemoryDatabase();.
    return false;
  }

  @Before
  public void setup() throws Exception {
    appointmentDao = new HibernateAppointmentDao(sessionFactory);
  }

  @Test
  public void getAppointmentsByLastSyncDate_shouldReturnEmptyObsWhenLastSyncIsNull() {
    List<Obs> results = appointmentDao.getAppointmentsByLastSyncDate(null);
    assertEquals(0, results.size());
  }

  @Test
  public void getAppointmentsByLastSyncDate() throws Exception {
    executeDataSet(MCH_CONSENTED_PATIENT_XML_FILENAME);

    List<AppointmentRequests> appointmentRequestsList = appointmentDao.getAllAppointmentRequests();
    assertEquals(1, appointmentRequestsList.size());

    for (AppointmentRequests appointmentRequest : appointmentRequestsList) {
      List<Obs> results =
          appointmentDao.getAppointmentsByLastSyncDate(appointmentRequest.getDateCreated());
      assertEquals(0, results.size());
    }
  }

  @Test
  public void getAllAppointmentRequests() {
    List<AppointmentRequests> allAppointments = appointmentDao.getAllAppointmentRequests();
    assertEquals(1, allAppointments.size());
  }

  @Test
  public void getAllAppointmentRequestsByLastSyncDate_returnZeroAppointments() {
    List<AppointmentRequests> allAppointments =
        appointmentDao.getAllAppointmentRequestsByLastSyncDate(new Date());
    assertEquals(0, allAppointments.size());
  }

  @Test
  @Rollback(value = false)
  public void saveAListOfAppointmentRequests() {
    List<AppointmentRequests> appointmentRequestsList = appointmentRequestsListFactory();

    List<AppointmentRequests> allAppointments =
        appointmentDao.saveAppointmentRequests(appointmentRequestsList);
    assertEquals(1, allAppointments.size());
  }

  @Test
  @Rollback(value = false)
  public void saveOneInstanceOfAppointmentRequests() {
    Date dt = new Date();
    AppointmentRequests appointmentRequest = new AppointmentRequests();
    appointmentRequest.setAppointmentUUID(String.valueOf(UUID.randomUUID()));
    appointmentRequest.setId(41);
    appointmentRequest.setMycarehubId(String.valueOf(UUID.randomUUID()));
    appointmentRequest.setClientName("Test kazi");
    appointmentRequest.setCccNumber("9876");
    appointmentRequest.setAppointmentReason("travel");
    appointmentRequest.setStatus("PENDING");
    appointmentRequest.setClientContact("098765");
    appointmentRequest.setDateCreated(dt);
    appointmentRequest.setVoided(false);
    appointmentRequest.setProgressDate(dt);
    appointmentRequest.setDateResolved(dt);

    AppointmentRequests result = appointmentDao.saveAppointmentRequests(appointmentRequest);
    Integer expected = 41;
    assertEquals(expected, result.getId());
  }

  @Test
  public void getAppointmentRequestByMycarehubId() {
    AppointmentRequests mycarehubAppointment =
        appointmentDao.getAppointmentRequestByMycarehubId("cf25712e-f193-48d5-a85a-5fd8d0244c71");
    Integer expectedID = 1;
    assertEquals(expectedID, mycarehubAppointment.getId());
  }

  @Test
  public void getAppointmentRequestByUuid() {
    AppointmentRequests mycarehubAppointment =
        appointmentDao.getAppointmentRequestByUuid("cf25712e-f193-48d5-a85a-5fd8d0244c71");
    String expectedUUID = "cf25712e-f193-48d5-a85a-5fd8d0244c71";
    assertEquals(expectedUUID, mycarehubAppointment.getUuid());
  }

  @Test
  public void countAppointment() {
    String clientPhoneNumber = "+254711223344";
    Number appointmentsCount = appointmentDao.countAppointments(clientPhoneNumber);

    Long expectedCount = 1L;
    assertEquals(expectedCount, appointmentsCount);
  }

  @Test
  public void getPagedAppointments() {
    String clientPhoneNumber = "+254711223344";
    List<AppointmentRequests> appointmentRequestsList =
        appointmentDao.getPagedAppointments(clientPhoneNumber, 1, 10);

    Number expectedCount = 1;
    assertEquals(expectedCount, appointmentRequestsList.size());
  }

  @Test
  public void getObsByEncounterAndConcept() throws Exception {
    Obs obs = appointmentDao.getObsByEncounterAndConcept(3, 5089);
    Integer expectedObsID = 7;
    assertEquals(expectedObsID, obs.getObsId());
  }

  public static List<AppointmentRequests> appointmentRequestsListFactory() {
    Date dt = new Date();
    AppointmentRequests appointmentRequests = new AppointmentRequests();
    appointmentRequests.setAppointmentUUID("1234");
    appointmentRequests.setId(1234);
    appointmentRequests.setMycarehubId(String.valueOf(UUID.randomUUID()));
    appointmentRequests.setClientName("MtuKazi");
    appointmentRequests.setCccNumber("12345");
    appointmentRequests.setAppointmentReason("away");
    appointmentRequests.setStatus("RESOLVED");
    appointmentRequests.setClientContact("12345");
    appointmentRequests.setDateCreated(dt);
    appointmentRequests.setVoided(false);
    appointmentRequests.setProgressDate(dt);
    appointmentRequests.setDateResolved(dt);
    appointmentRequests.setUuid("1234");

    List<AppointmentRequests> appointmentRequestsList = new ArrayList<AppointmentRequests>();
    appointmentRequestsList.add(appointmentRequests);

    return appointmentRequestsList;
  }

  private static Encounter encounterFactory() {
    Encounter encounter = new Encounter();
    encounter.setId(1);
    encounter.setEncounterId(1);

    return encounter;
  }

  private static Concept conceptFactory() {
    Concept concept = new Concept();
    concept.setConceptId(1);
    concept.setId(1);

    return concept;
  }

  public static ConsentedPatient consentedPatientFactory() {
    ConsentedPatient consentedPatient = new ConsentedPatient();
    consentedPatient.setId(1);
    consentedPatient.setPatientId(1);
    consentedPatient.setDateCreated(new Date());
    consentedPatient.setUuid(String.valueOf(UUID.randomUUID()));

    return consentedPatient;
  }
}
