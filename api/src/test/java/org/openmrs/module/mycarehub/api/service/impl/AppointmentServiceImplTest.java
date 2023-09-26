package org.openmrs.module.mycarehub.api.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.openmrs.module.mycarehub.utils.Constants.APPOINTMENT_DATE_CONCEPT_ID;
import static org.openmrs.module.mycarehub.utils.Constants.MyCareHubSettingType.*;
import static org.powermock.api.mockito.PowerMockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.User;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.mycarehub.api.db.AppointmentDao;
import org.openmrs.module.mycarehub.api.service.MyCareHubSettingsService;
import org.openmrs.module.mycarehub.model.AppointmentRequests;
import org.openmrs.module.mycarehub.model.MyCareHubSetting;
import org.openmrs.module.mycarehub.utils.MyCareHubUtil;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class, MyCareHubUtil.class})
public class AppointmentServiceImplTest {

  @Mock private AppointmentDao appointmentDao;

  @InjectMocks private AppointmentServiceImpl fakeAppointmentImpl;

  public static final User USER = testUserFactory();

  @Mock public static MyCareHubSettingsService myCareHubSettingsService;

  public static Date CURRENT_DATE = new Date();

  private static MyCareHubSetting setting;

  private static final String mflCode = "232343434";

  @Before
  public void setUp() {
    myCareHubSettingsService = mock(MyCareHubSettingsService.class);
    setting = mock(MyCareHubSetting.class);
    Date dt = new Date();
    CURRENT_DATE = mock(dt.getClass());

    assertNotNull(setting);
    assertNotNull(CURRENT_DATE);

    mockStatic(Context.class);
    mockStatic(MyCareHubUtil.class);
    when(Context.getService(MyCareHubSettingsService.class)).thenReturn(myCareHubSettingsService);
    when(Context.getPatientService()).thenReturn(mock(PatientService.class));
    when(MyCareHubUtil.getDefaultLocationMflCode()).thenReturn(mflCode);
  }

  @Test
  public void testGetAppointmentsByLastSyncDate() {
    Obs recordOne = createObs();

    List<Obs> obsList = Arrays.asList(recordOne);
    when(appointmentDao.getAppointmentsByLastSyncDate(recordOne.getDateCreated()))
        .thenReturn(obsList);
    List<Obs> obsRecords =
        fakeAppointmentImpl.getAppointmentsByLastSyncDate(recordOne.getDateCreated());
    assertEquals(1, obsRecords.size());
  }

  @Test
  public void testGetAllAppointmentRequests() {
    List<AppointmentRequests> requests = createAppointmentRequests();
    when(appointmentDao.getAllAppointmentRequests()).thenReturn(requests);

    List<AppointmentRequests> appointmentRequests = fakeAppointmentImpl.getAllAppointmentRequests();
    assertEquals(1, appointmentRequests.size());
  }

  @Test
  public void testGetAllAppointmentRequests_Not_more_than_one() {
    List<AppointmentRequests> requests = createAppointmentRequests();
    when(appointmentDao.getAllAppointmentRequests()).thenReturn(requests);

    List<AppointmentRequests> appointmentRequests = fakeAppointmentImpl.getAllAppointmentRequests();
    assertNotEquals(4, appointmentRequests.size());
  }

  @Test
  public void testGetAppointmentRequestByUuid() {
    List<AppointmentRequests> requests = createAppointmentRequests();
    when(appointmentDao.getAppointmentRequestByUuid(requests.get(0).getAppointmentUUID()))
        .thenReturn(requests.get(0));

    AppointmentRequests appointmentRequests =
        fakeAppointmentImpl.getAppointmentRequestByUuid(requests.get(0).getAppointmentUUID());
    assertEquals(appointmentRequests, requests.get(0));
  }

  @Test
  public void testGetAllAppointmentRequestsByLastSyncDate() {
    List<AppointmentRequests> requests = createAppointmentRequests();
    when(appointmentDao.getAllAppointmentRequestsByLastSyncDate(CURRENT_DATE)).thenReturn(requests);

    List<AppointmentRequests> appointmentRequestsList =
        fakeAppointmentImpl.getAllAppointmentRequestsByLastSyncDate(CURRENT_DATE);
    assertEquals(appointmentRequestsList, requests);
    assertEquals(1, appointmentRequestsList.size());
  }

  @Test
  public void testSaveAppointmentRequests() {
    List<AppointmentRequests> requests = createAppointmentRequests();
    when(appointmentDao.saveAppointmentRequests(requests)).thenReturn(requests);

    List<AppointmentRequests> savedAppointments =
        fakeAppointmentImpl.saveAppointmentRequests(requests);
    assertEquals(savedAppointments, requests);
  }

  @Test
  public void testSaveSingleAppointmentRequest() {
    List<AppointmentRequests> requests = createAppointmentRequests();
    AppointmentRequests firstRequest = requests.get(0);

    when(appointmentDao.saveAppointmentRequests(firstRequest)).thenReturn(firstRequest);
    AppointmentRequests savedAppointment =
        fakeAppointmentImpl.saveAppointmentRequests(firstRequest);
    assertEquals(savedAppointment, firstRequest);
  }

  @Test
  public void testGetAppointmentRequestByMycarehubId() {
    List<AppointmentRequests> requests = createAppointmentRequests();
    when(appointmentDao.getAppointmentRequestByMycarehubId(requests.get(0).getMycarehubId()))
        .thenReturn(requests.get(0));

    AppointmentRequests appointmentList =
        fakeAppointmentImpl.getAppointmentRequestByMycarehubId(requests.get(0).getMycarehubId());
    assertEquals(appointmentList, requests.get(0));
  }

  @Test
  public void testCountAppointments() {
    List<AppointmentRequests> requests = createAppointmentRequests();
    when(appointmentDao.countAppointments("TEST")).thenReturn(requests.size());

    Number appointments = fakeAppointmentImpl.countAppointments("TEST");
    assertEquals(1, appointments);
  }

  @Test
  public void testGetPagedAppointments() {
    List<AppointmentRequests> requests = createAppointmentRequests();
    when(appointmentDao.getPagedAppointments("TEST", 1, 5)).thenReturn(requests);

    List<AppointmentRequests> appointments = fakeAppointmentImpl.getPagedAppointments("TEST", 1, 5);
    assertEquals(1, appointments.size());
    assertEquals(appointments, requests);
  }

  @Test
  public void syncPatientAppointmentRequestsNullSetting() {
    when(myCareHubSettingsService.getLatestMyCareHubSettingByType(
            PATIENT_APPOINTMENTS_REQUESTS_POST))
        .thenReturn(null);
    when(myCareHubSettingsService.createMyCareHubSetting(
            PATIENT_APPOINTMENTS_REQUESTS_POST, CURRENT_DATE))
        .thenReturn(setting);
    fakeAppointmentImpl.syncPatientAppointmentRequests();
  }

  @Test
  public void syncPatientAppointmentsNullSetting() {
    when(myCareHubSettingsService.getLatestMyCareHubSettingByType(PATIENT_APPOINTMENTS))
        .thenReturn(null);

    fakeAppointmentImpl.syncPatientAppointments();
    assertNotNull(setting);
  }

  @Test
  public void syncPatientAppointmentsWithNullRequests() {
    when(myCareHubSettingsService.getLatestMyCareHubSettingByType(
            PATIENT_APPOINTMENTS_REQUESTS_POST))
        .thenReturn(setting);
    Date lastSyncTime = new Date();
    when(setting.getLastSyncTime()).thenReturn(lastSyncTime);
    List<AppointmentRequests> appointments = new ArrayList<AppointmentRequests>();
    when(appointmentDao.getAllAppointmentRequestsByLastSyncDate(eq(lastSyncTime)))
        .thenReturn(appointments);

    fakeAppointmentImpl.syncPatientAppointmentRequests();
  }

  @Test
  public void syncPatientAppointmentsEmptyAppointments() {
    when(myCareHubSettingsService.getLatestMyCareHubSettingByType(PATIENT_APPOINTMENTS))
        .thenReturn(setting);

    Date lastSyncTime = new Date();
    when(setting.getLastSyncTime()).thenReturn(lastSyncTime);
    List<Obs> appointments = new ArrayList<Obs>();

    when(appointmentDao.getAppointmentsByLastSyncDate(eq(lastSyncTime))).thenReturn(appointments);

    fakeAppointmentImpl.syncPatientAppointments();
  }

  @Test
  public void fetchPatientAppointmentRequestsWithNullSetting() {
    when(myCareHubSettingsService.createMyCareHubSetting(
            PATIENT_APPOINTMENTS_REQUESTS_GET, CURRENT_DATE))
        .thenReturn(null);

    fakeAppointmentImpl.fetchPatientAppointmentRequests();
  }

  @Test
  public void fetchPatientAppointmentRequests() {
    MyCareHubSetting myCareHubSetting = new MyCareHubSetting();
    myCareHubSetting.setSettingType(PATIENT_APPOINTMENTS_REQUESTS_GET);
    myCareHubSetting.setLastSyncTime(CURRENT_DATE);

    when(myCareHubSettingsService.getLatestMyCareHubSettingByType(
            PATIENT_APPOINTMENTS_REQUESTS_GET))
        .thenReturn(myCareHubSetting);

    JsonArray jsonArray = getJsonElements();

    when(MyCareHubUtil.fetchPatientAppointmentRequests(
            Mockito.any(Date.class), Mockito.any(Date.class)))
        .thenReturn(jsonArray);

    when(appointmentDao.getAppointmentRequestByMycarehubId(Mockito.anyString()))
        .thenReturn(createAppointmentRequests().get(0));

    fakeAppointmentImpl.fetchPatientAppointmentRequests();
  }

  @Test
  public void fetchPatientAppointmentRequestsWithNoExistingMyCareHubRequests() {
    MyCareHubSetting myCareHubSetting = new MyCareHubSetting();
    myCareHubSetting.setSettingType(PATIENT_APPOINTMENTS_REQUESTS_GET);
    myCareHubSetting.setLastSyncTime(CURRENT_DATE);

    when(myCareHubSettingsService.getLatestMyCareHubSettingByType(
            PATIENT_APPOINTMENTS_REQUESTS_GET))
        .thenReturn(myCareHubSetting);

    JsonArray jsonArray = getJsonElements();

    when(MyCareHubUtil.fetchPatientAppointmentRequests(
            Mockito.any(Date.class), Mockito.any(Date.class)))
        .thenReturn(jsonArray);

    when(appointmentDao.getAppointmentRequestByMycarehubId(Mockito.anyString()))
        .thenReturn(new AppointmentRequests());

    fakeAppointmentImpl.fetchPatientAppointmentRequests();
  }

  private static JsonArray getJsonElements() {
    JsonArray jsonArray = new JsonArray();

    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("id", String.valueOf(UUID.randomUUID()));
    jsonObject.addProperty("AppointmentID", String.valueOf(UUID.randomUUID()));
    jsonObject.addProperty("AppointmentReason", "LAB test");
    jsonObject.addProperty("SuggestedDate", String.valueOf(CURRENT_DATE));
    jsonObject.addProperty("InProgressAt", String.valueOf(CURRENT_DATE));
    jsonObject.addProperty("InProgressBy", String.valueOf(UUID.randomUUID()));
    jsonObject.addProperty("ResolvedAt", String.valueOf(CURRENT_DATE));
    jsonObject.addProperty("ResolvedBy", String.valueOf(UUID.randomUUID()));
    jsonObject.addProperty("ClientName", "test");
    jsonObject.addProperty("ClientContact", "0711223344");
    jsonObject.addProperty("CCCNumber", "123456789");
    jsonObject.addProperty("MFLCODE", "11094");
    jsonObject.addProperty("Status", "PENDING");

    jsonArray.add(jsonObject);
    return jsonArray;
  }

  private Obs createSampleAppointmentObs(Integer encounterId, Date appointmentDate) {
    Obs appointmentObs = new Obs();
    appointmentObs.setValueDatetime(appointmentDate);
    appointmentObs.setId(APPOINTMENT_DATE_CONCEPT_ID);

    PatientIdentifierType patientIdentifierType = getPatientIdentifierType();

    PatientIdentifier patientIdentifier = getPatientIdentifier(patientIdentifierType);

    Collection<PatientIdentifier> identifiers = getIdentifiers(patientIdentifier);

    Set<PatientIdentifier> patientIdentifierSet = getPatientIdentifiers(patientIdentifier);

    Patient patient = getPatient(identifiers, patientIdentifierSet);

    Encounter encounter = getEncounter(encounterId, patient);

    Concept concept = getConcept();

    appointmentObs.setEncounter(encounter);
    appointmentObs.setConcept(concept);
    appointmentObs.getEncounter();

    return appointmentObs;
  }

  private static PatientIdentifierType getPatientIdentifierType() {
    PatientIdentifierType patientIdentifierType = new PatientIdentifierType();
    patientIdentifierType.setPatientIdentifierTypeId(1234);
    patientIdentifierType.setId(1234);
    return patientIdentifierType;
  }

  private static Collection<PatientIdentifier> getIdentifiers(PatientIdentifier patientIdentifier) {
    Collection<PatientIdentifier> identifiers = new ArrayList<PatientIdentifier>();
    identifiers.add(patientIdentifier);
    return identifiers;
  }

  private static Set<PatientIdentifier> getPatientIdentifiers(PatientIdentifier patientIdentifier) {
    Set<PatientIdentifier> patientIdentifierSet = new HashSet<PatientIdentifier>();
    patientIdentifierSet.add(patientIdentifier);
    return patientIdentifierSet;
  }

  private static Concept getConcept() {
    Concept concept = new Concept();
    concept.setConceptId(APPOINTMENT_DATE_CONCEPT_ID);
    concept.setId(APPOINTMENT_DATE_CONCEPT_ID);
    return concept;
  }

  private static Encounter getEncounter(Integer encounterId, Patient patient) {
    Encounter encounter = new Encounter();
    encounter.setEncounterId(encounterId);
    encounter.setPatient(patient);
    encounter.setId(1234);
    encounter.setEncounterType(new EncounterType());
    return encounter;
  }

  private static Patient getPatient(
      Collection<PatientIdentifier> identifiers, Set<PatientIdentifier> patientIdentifierSet) {
    Patient patient = new Patient();
    patient.setId(1);
    patient.setPatientId(1234);
    patient.addIdentifiers(identifiers);
    patient.setIdentifiers(patientIdentifierSet);
    return patient;
  }

  private static PatientIdentifier getPatientIdentifier(
      PatientIdentifierType patientIdentifierType) {
    PatientIdentifier patientIdentifier = new PatientIdentifier();
    patientIdentifier.setIdentifierType(patientIdentifierType);
    patientIdentifier.setIdentifier("ccc_number");
    patientIdentifier.setPatientIdentifierId(1234);
    patientIdentifier.setId(1234);
    patientIdentifier.setPreferred(true);
    patientIdentifier.setVoided(true);
    return patientIdentifier;
  }

  private static Obs createObs() {
    Obs ob = new Obs();

    ob.setCreator(USER);
    ob.setId(1);
    ob.setObsId(1);
    ob.setComment("test");
    ob.setDateCreated(CURRENT_DATE);

    return ob;
  }

  private static List<AppointmentRequests> createAppointmentRequests() {
    AppointmentRequests appointment = new AppointmentRequests();
    appointment.setAppointmentReason("Very far");
    appointment.setAppointmentUUID(String.valueOf(UUID.randomUUID()));
    appointment.setCccNumber("12345");
    appointment.setId(12);
    appointment.setCreator(USER);
    appointment.setMycarehubId(String.valueOf(UUID.randomUUID()));

    return Collections.singletonList(appointment);
  }

  private static User testUserFactory() {
    User user = new User();
    user.setId(1);

    return user;
  }
}
