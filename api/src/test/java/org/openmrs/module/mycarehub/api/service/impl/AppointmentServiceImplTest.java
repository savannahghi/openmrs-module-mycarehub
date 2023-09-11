package org.openmrs.module.mycarehub.api.service.impl;

import static org.hibernate.validator.util.Contracts.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;
import static org.openmrs.module.mycarehub.utils.Constants.APPOINTMENT_DATE_CONCEPT_ID;
import static org.springframework.test.util.AssertionErrors.*;

import com.google.gson.JsonObject;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openmrs.Concept;
import org.openmrs.ConceptDatatype;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.User;
import org.openmrs.module.mycarehub.api.db.AppointmentDao;
import org.openmrs.module.mycarehub.api.service.MyCareHubSettingsService;
import org.openmrs.module.mycarehub.model.AppointmentRequests;
import org.openmrs.module.mycarehub.model.MyCareHubSetting;

public class AppointmentServiceImplTest {

  // Setup test data
  public static Date CURRENT_DATE = new Date();
  public static final String UUID1 = "4b41de8e-7680-47e1-a376-77682f99bf12";
  public static final String UUID2 = "11db5593-5099-4b66-a7ea-e6180728bf07";

  public static final String MYCAREHUB_ID_1 = "4b41de8e-7680-47e1-a376-77682f99bf12";
  public static final String MYCAREHUB_ID_2 = "11db5593-5099-4b66-a7ea-e6180728bf07";

  private static User userFactory() {
    User user = new User();
    user.setId(1);

    return user;
  }

  public static final User USER = userFactory();

  private static PatientIdentifierType patientIdentifierTypeFactory() {
    PatientIdentifierType identifierType = new PatientIdentifierType();

    identifierType.setId(1);
    identifierType.setPatientIdentifierTypeId(1);
    identifierType.setUuid("uuid");

    return identifierType;
  }

  private static PatientIdentifier patientIdentifierFactory() {
    PatientIdentifier identifier = new PatientIdentifier();

    identifier.setId(1);
    identifier.setIdentifier("CCC");
    identifier.setUuid("uuid");
    identifier.setIdentifierType(patientIdentifierTypeFactory());

    return identifier;
  }

  private static Patient patientFactory() {
    Patient patient = new Patient();
    patient.setId(1);
    patient.setIdentifiers(patient.getIdentifiers());
    patient.addIdentifier(patientIdentifierFactory());

    return patient;
  }

  private static Encounter encounterFactory() {
    Encounter encounter = new Encounter();
    encounter.setId(1);
    encounter.setEncounterId(1);
    encounter.setPatient(patientFactory());

    return encounter;
  }

  private static ConceptDatatype conceptDataTypeFactory() {
    ConceptDatatype conceptDataType = new ConceptDatatype();
    conceptDataType.setId(1);
    conceptDataType.setConceptDatatypeId(1);
    conceptDataType.setHl7Abbreviation("BIT");
    return conceptDataType;
  }

  private static Concept conceptFactory() {
    Concept concept = new Concept();
    concept.setConceptId(APPOINTMENT_DATE_CONCEPT_ID);
    concept.setId(1);
    concept.setDatatype(conceptDataTypeFactory());

    return concept;
  }

  private static List<Obs> obsFactory() {
    Obs ob1 = new Obs();
    ob1.setCreator(USER);
    ob1.setId(1);
    ob1.setObsId(1);
    ob1.setComment("test");
    ob1.setDateCreated(CURRENT_DATE);
    ob1.setEncounter(encounterFactory());
    ob1.setConcept(conceptFactory());

    Obs ob2 = new Obs();
    ob2.setCreator(USER);
    ob2.setId(2);
    ob2.setObsId(2);
    ob2.setComment("test");
    ob2.setDateCreated(CURRENT_DATE);
    ob2.setEncounter(encounterFactory());
    ob2.setConcept(conceptFactory());

    List<Obs> obs = Arrays.asList(ob1, ob2);
    return obs;
  }

  private static List<AppointmentRequests> appointmentRequestsFactory() {
    AppointmentRequests request1 = new AppointmentRequests();
    request1.setId(1);
    request1.setUuid(UUID1);
    request1.setMycarehubId(MYCAREHUB_ID_1);

    AppointmentRequests request2 = new AppointmentRequests();
    request2.setId(2);
    request2.setUuid(UUID2);
    request2.setMycarehubId(MYCAREHUB_ID_2);

    List<AppointmentRequests> appointmentRequests = Arrays.asList(request1, request2);

    return appointmentRequests;
  }

  @Mock private AppointmentDao mockAppointmentDao;
  @Mock private MyCareHubSettingsService mockMyCareHubSettingsService;

  @Mock private AppointmentServiceImpl mockAppointmentServiceImpl;

  @InjectMocks private AppointmentServiceImpl appointmentService;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    appointmentService = new AppointmentServiceImpl(mockAppointmentDao);
  }

  // Method Tests
  @Test
  public void testGetAppointmentsByLastSyncDate() {
    // Create mock data
    List<Obs> mockObservations = obsFactory();

    // Mock setup
    when(mockAppointmentDao.getAppointmentsByLastSyncDate(mockObservations.get(0).getDateCreated()))
        .thenReturn(mockObservations);

    // Test
    List<Obs> appointments =
        appointmentService.getAppointmentsByLastSyncDate(mockObservations.get(0).getDateCreated());
    List<Obs> noAppointments = appointmentService.getAppointmentsByLastSyncDate(new Date());

    // Assertions
    assertEquals("Happy Case: Gets at least one appointment", 2, appointments.size());
    assertEquals("Sad Case: Last sync time in the future", 0, noAppointments.size());
  }

  @Test
  public void testGetAllAppointmentRequests() {
    // Create mock data
    List<AppointmentRequests> mockAppointmentRequests = appointmentRequestsFactory();

    // Mock setup
    when(mockAppointmentDao.getAllAppointmentRequests()).thenReturn(mockAppointmentRequests);

    // Test
    List<AppointmentRequests> result = appointmentService.getAllAppointmentRequests();

    // Assertions
    assertEquals("Happy case: Number of appointment requests should match", 2, result.size());
    assertEquals(
        "Happy case: Appointment request ID 1 should match",
        Integer.valueOf(1),
        result.get(0).getId());
    assertEquals(
        "Happy Case: Appointment request ID 2 should match",
        Integer.valueOf(2),
        result.get(1).getId());
    try {
      assertEquals(
          "Sad Case: Appointment request ID 3 does not in list",
          Integer.valueOf(3),
          result.get(2).getId());
      fail("Expected ArrayIndexOutOfBoundsException");
    } catch (ArrayIndexOutOfBoundsException e) {
      // The exception is expected, so the test passes
    }
  }

  @Test
  public void testGetAppointmentRequestByUuid() {
    // Create mock data
    List<AppointmentRequests> mockAppointmentRequests = appointmentRequestsFactory();
    AppointmentRequests mockRequest = mockAppointmentRequests.get(0);
    String nonExistentUuid = UUID.randomUUID().toString();

    // Mock setup
    when(mockAppointmentDao.getAppointmentRequestByUuid(UUID1)).thenReturn(mockRequest);
    when(mockAppointmentDao.getAppointmentRequestByUuid(nonExistentUuid)).thenReturn(null);

    // Test
    AppointmentRequests result = appointmentService.getAppointmentRequestByUuid(UUID1);
    AppointmentRequests resultNonExistingUUID =
        appointmentService.getAppointmentRequestByUuid(nonExistentUuid);

    // Assertions
    assertEquals("Happy Case: Appointment request UUID should match", UUID1, result.getUuid());
    assertNull("Sad Case: Should return null for non-existent UUID", resultNonExistingUUID);
  }

  @Test
  public void testGetAllAppointmentRequestsByLastSyncDate() {
    // Create mock data
    Date lastSyncDate = new Date();
    Date InvalidLastSyncDate = new Date();
    InvalidLastSyncDate.setTime(lastSyncDate.getTime() + 10000);
    List<AppointmentRequests> mockAppointmentRequests = appointmentRequestsFactory();

    // Mock setup
    when(mockAppointmentDao.getAllAppointmentRequestsByLastSyncDate(lastSyncDate))
        .thenReturn(mockAppointmentRequests);
    when(mockAppointmentDao.getAllAppointmentRequestsByLastSyncDate(InvalidLastSyncDate))
        .thenReturn(Collections.<AppointmentRequests>emptyList());

    // Test
    List<AppointmentRequests> result =
        appointmentService.getAllAppointmentRequestsByLastSyncDate(lastSyncDate);
    List<AppointmentRequests> resultInvalidLastSyncDate =
        appointmentService.getAllAppointmentRequestsByLastSyncDate(InvalidLastSyncDate);

    // Assertions
    assertEquals("Happy Case: Number of appointment requests should match", 2, result.size());
    assertEquals(
        "Happy Case: Appointment request ID should match",
        Integer.valueOf(1),
        result.get(0).getId());
    assertEquals(
        "Happy Case: Appointment request ID should match",
        Integer.valueOf(2),
        result.get(1).getId());
    assertTrue(
        "Sad Case: List of appointment requests should be empty",
        resultInvalidLastSyncDate.isEmpty());
  }

  @Test
  public void testSaveAppointmentRequests() {
    // Create mock data
    List<AppointmentRequests> mockAppointmentRequests = appointmentRequestsFactory();
    List<AppointmentRequests> emptyList = Collections.emptyList();

    // Mock setup
    when(mockAppointmentDao.saveAppointmentRequests(mockAppointmentRequests))
        .thenReturn(mockAppointmentRequests);
    when(mockAppointmentDao.saveAppointmentRequests(emptyList)).thenReturn(emptyList);

    // Test
    List<AppointmentRequests> result =
        appointmentService.saveAppointmentRequests(mockAppointmentRequests);
    List<AppointmentRequests> resultEmptyList =
        appointmentService.saveAppointmentRequests(emptyList);

    // Assertions
    assertEquals("Happy Case: Number of saved appointment requests should match", 2, result.size());
    assertEquals(
        "Happy Case: Saved appointment request ID should match",
        Integer.valueOf(1),
        result.get(0).getId());
    assertEquals(
        "Happy Case: Saved appointment request ID should match",
        Integer.valueOf(2),
        result.get(1).getId());
    verify(mockAppointmentDao).saveAppointmentRequests(mockAppointmentRequests);

    assertTrue(
        "Sad Case: List of saved appointment requests should be empty", resultEmptyList.isEmpty());
    verify(mockAppointmentDao).saveAppointmentRequests(emptyList);
  }

  @Test
  public void testSaveAppointmentRequests2() {
    // Create mock data
    List<AppointmentRequests> mockAppointmentRequests = appointmentRequestsFactory();
    AppointmentRequests mockAppointmentRequest = mockAppointmentRequests.get(0);

    // Mock setup
    when(mockAppointmentDao.saveAppointmentRequests(mockAppointmentRequest))
        .thenReturn(mockAppointmentRequest);
    when(mockAppointmentDao.saveAppointmentRequests((List<AppointmentRequests>) null))
        .thenThrow(new IllegalArgumentException("Invalid input"));

    // Test
    AppointmentRequests result = appointmentService.saveAppointmentRequests(mockAppointmentRequest);

    // Assertions
    assertEquals(
        "Happy Case: Saved appointment request ID should match",
        Integer.valueOf(1),
        result.getId());

    // Verify mock behavior
    verify(mockAppointmentDao).saveAppointmentRequests(mockAppointmentRequest);

    try {
      appointmentService.saveAppointmentRequests((List<AppointmentRequests>) null);
      fail("Sad Case: Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // The exception is expected, so the test passes
    }
  }

  @Test
  public void testGetAppointmentRequestByMycarehubId() {
    // Create mock data
    String mycarehubIDNonExistent = "nonexistent";
    List<AppointmentRequests> mockAppointmentRequests = appointmentRequestsFactory();
    AppointmentRequests mockAppointmentRequest = mockAppointmentRequests.get(0);

    // Mock setup
    when(mockAppointmentDao.getAppointmentRequestByMycarehubId(MYCAREHUB_ID_1))
        .thenReturn(mockAppointmentRequest);
    when(mockAppointmentDao.getAppointmentRequestByMycarehubId(mycarehubIDNonExistent))
        .thenReturn(null);

    // Test
    AppointmentRequests result =
        appointmentService.getAppointmentRequestByMycarehubId(MYCAREHUB_ID_1);
    AppointmentRequests resultNonExistentID =
        appointmentService.getAppointmentRequestByMycarehubId(mycarehubIDNonExistent);

    // Assertions
    assertEquals(
        "Happy Case: Returned appointment request should match", mockAppointmentRequest, result);
    assertNull("Sad Case: Returned appointment request should be null", resultNonExistentID);
  }

  @Test
  public void testCountAppointments() {
    // Create mock data
    String searchString = "example search string";
    Number mockCount = 100;
    String searchStringNonExistent = "nonexistent";
    Number mockCountNonExistent = 0;

    // Mock setup
    when(mockAppointmentDao.countAppointments(searchString)).thenReturn(mockCount);
    when(mockAppointmentDao.countAppointments(searchStringNonExistent))
        .thenReturn(mockCountNonExistent);

    // Test
    Number result = appointmentService.countAppointments(searchString);
    Number resultNonExistent = appointmentService.countAppointments(searchStringNonExistent);

    // Assertion
    assertEquals("Happy Case: Returned count should match", mockCount, result);
    assertEquals("Sad Case: Returned count should be 0", mockCountNonExistent, resultNonExistent);
  }

  @Test
  public void testGetPagedAppointments() {
    // Create mock data
    String searchString = "example search string";
    String searchStringNonExistent = "nonExistent";
    Integer pageNumber = 1;
    Integer pageSize = 10;
    List<AppointmentRequests> mockAppointmentRequests = appointmentRequestsFactory();
    List<AppointmentRequests> mockAppointmentsNonExistent = new ArrayList<AppointmentRequests>();

    // Mock setup
    when(mockAppointmentDao.getPagedAppointments(searchString, pageNumber, pageSize))
        .thenReturn(mockAppointmentRequests);
    when(mockAppointmentDao.getPagedAppointments(searchStringNonExistent, pageNumber, pageSize))
        .thenReturn(mockAppointmentsNonExistent);

    // Test
    List<AppointmentRequests> result =
        appointmentService.getPagedAppointments(searchString, pageNumber, pageSize);
    List<AppointmentRequests> resultNonExistent =
        appointmentService.getPagedAppointments(searchStringNonExistent, pageNumber, pageSize);

    // Assertions
    assertEquals("Happy Case: Number of returned appointments should match", 2, result.size());
    assertEquals(
        "Sad Case: Number of returned appointments should be 0", 0, resultNonExistent.size());
  }

  @Test
  public void testSyncPatientAppointments() throws Exception {
    // Create mock data
    MyCareHubSetting mockSetting = new MyCareHubSetting();
    mockSetting.setLastSyncTime(new Date());
    mockSetting.setId(1);
    mockSetting.setSettingType("PATIENT_APPOINTMENTS");
    List<Obs> mockObservations = obsFactory();
    Obs mockObservation = mockObservations.get(0);

    // Mock setup
    when(mockMyCareHubSettingsService.getLatestMyCareHubSettingByType(
            "APPOINTMENT_DATE_CONCEPT_ID"))
        .thenReturn(mockSetting);
    when(mockAppointmentDao.getAppointmentsByLastSyncDate(mockSetting.getLastSyncTime()))
        .thenReturn(mockObservations);
    when(mockAppointmentDao.getObsByEncounterAndConcept(
            encounterFactory().getEncounterId(), conceptFactory().getConceptId()))
        .thenReturn(mockObservation);

    // Use Reflections to access and test the private method
    Method createAppointmentObjectMethod =
        appointmentService
            .getClass()
            .getDeclaredMethod(
                "createAppointmentObject", mockObservation.getClass(), PatientIdentifierType.class);
    createAppointmentObjectMethod.setAccessible(true);

    // Call the private method
    JsonObject appointmentObject =
        (JsonObject)
            createAppointmentObjectMethod.invoke(
                appointmentService, mockObservation, patientIdentifierTypeFactory());

    assertNotNull(appointmentObject);
  }
}
