package org.openmrs.module.mycarehub.api.db.hibernate;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.openmrs.module.mycarehub.api.db.hibernate.Common.GET_MYCAREHUB_CONSENTED_PATIENT;
import static org.powermock.api.mockito.PowerMockito.*;

import java.util.*;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.mycarehub.api.db.AppointmentDao;
import org.openmrs.module.mycarehub.model.AppointmentRequests;
import org.openmrs.module.mycarehub.model.ConsentedPatient;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Restrictions.class})
public class HibernateAppointmentDaoTest {

  @Mock private DbSessionFactory sessionFactory;

  @Mock private DbSession dbSession;

  @Mock private AppointmentDao appointmentDao;

  @InjectMocks HibernateAppointmentDao hibernateAppointmentDao;

  @Mock Criteria criteria;

  @Before
  public void setup() {
    mockStatic(Restrictions.class);
    mockStatic(SQLQuery.class);
    mockStatic(Criteria.class);
    when(sessionFactory.getCurrentSession()).thenReturn(dbSession);
    appointmentDao = new HibernateAppointmentDao(sessionFactory);
  }

  @Test
  public void testGetAppointmentsByLastSyncDate() {
    Date lastSyncDate = new Date();

    List<Integer> consentedPatientIds = consentedPatientIds();
    when(dbSession.createSQLQuery(anyString())).thenReturn(mock(org.hibernate.SQLQuery.class));
    when(dbSession.createCriteria(Obs.class)).thenReturn(mock(org.hibernate.Criteria.class));

    when(dbSession.createSQLQuery(GET_MYCAREHUB_CONSENTED_PATIENT).list())
        .thenReturn(consentedPatientIds);

    List<Obs> expectedAppointments = obsFactory();

    when(dbSession.createCriteria(Obs.class).list()).thenReturn(expectedAppointments);

    List<Obs> result = hibernateAppointmentDao.getAppointmentsByLastSyncDate(lastSyncDate);

    assertEquals(expectedAppointments, result);
  }

  @Test
  public void testGetAppointmentsByLastSyncDateShouldReturnNullObs() {
    List<Obs> result = hibernateAppointmentDao.getAppointmentsByLastSyncDate(null);

    assertEquals(0, result.size());
  }

  @Test
  public void getAllAppointmentRequests() {
    List<AppointmentRequests> appointmentRequestsList = appointmentRequestsListFactory();

    when(dbSession.createCriteria(AppointmentRequests.class)).thenReturn(criteria);
    when(criteria.add(any(Criterion.class))).thenReturn(criteria);
    when(criteria.list()).thenReturn(appointmentRequestsList);

    List<AppointmentRequests> requests = hibernateAppointmentDao.getAllAppointmentRequests();
    assertEquals(1, requests.size());
  }

  @Test
  public void getAllAppointmentRequestsByLastSyncDate() {
    Date lastSyncDate = new Date();
    List<AppointmentRequests> appointmentRequestsList = appointmentRequestsListFactory();
    assertNotNull(appointmentRequestsList);

    when(dbSession.createCriteria(AppointmentRequests.class)).thenReturn(criteria);
    when(criteria.add(any(Criterion.class))).thenReturn(criteria);
    when(criteria.list()).thenReturn(appointmentRequestsList);

    List<AppointmentRequests> requests =
        hibernateAppointmentDao.getAllAppointmentRequestsByLastSyncDate(lastSyncDate);
    assertEquals(1, requests.size());
  }

  @Test
  public void saveAppointmentRequestsList() {
    List<AppointmentRequests> appointmentRequestsList = appointmentRequestsListFactory();
    assertNotNull(appointmentRequestsList);

    doNothing().when(dbSession).saveOrUpdate(AppointmentRequests.class);

    List<AppointmentRequests> result =
        appointmentDao.saveAppointmentRequests(appointmentRequestsList);
    assertEquals(appointmentRequestsList, result);
  }

  @Test
  public void saveAppointmentRequests() {
    AppointmentRequests appointmentRequests = appointmentRequestsListFactory().get(0);
    assertNotNull(appointmentRequests);

    doNothing().when(dbSession).saveOrUpdate(AppointmentRequests.class);

    AppointmentRequests result =
        hibernateAppointmentDao.saveAppointmentRequests(appointmentRequests);
    assertNotNull(result);
  }

  @Test
  public void getAppointmentRequestByMycarehubId() {
    List<AppointmentRequests> appointmentRequestsList = appointmentRequestsListFactory();
    String myCareHubId = appointmentRequestsList.get(0).getMycarehubId();
    assertNotNull(myCareHubId);

    when(dbSession.createCriteria(AppointmentRequests.class)).thenReturn(criteria);
    when(criteria.add(any(Criterion.class))).thenReturn(criteria);
    when(criteria.uniqueResult()).thenReturn(appointmentRequestsList.get(0));

    AppointmentRequests appointmentRequests =
        hibernateAppointmentDao.getAppointmentRequestByMycarehubId(
            appointmentRequestsList.get(0).getAppointmentUUID());
    assertNotNull(appointmentRequests);
  }

  @Test
  public void getAppointmentRequestByUuid() {
    List<AppointmentRequests> appointmentRequestsList = appointmentRequestsListFactory();
    String appointmentUUID = appointmentRequestsList.get(0).getAppointmentUUID();
    assertNotNull(appointmentUUID);

    when(dbSession.createCriteria(AppointmentRequests.class)).thenReturn(criteria);
    when(criteria.add(any(Criterion.class))).thenReturn(criteria);
    when(criteria.uniqueResult()).thenReturn(appointmentRequestsList.get(0));

    AppointmentRequests appointmentRequests =
        hibernateAppointmentDao.getAppointmentRequestByUuid(
            appointmentRequestsList.get(0).getAppointmentUUID());
    assertNotNull(appointmentRequests);
  }

  @Test
  public void testCountAppointmentsNullSearchString() {
    String searchString = "";

    when(dbSession.createCriteria(AppointmentRequests.class)).thenReturn(criteria);
    when(criteria.setProjection(any(Projection.class))).thenReturn(criteria);
    Number result = appointmentDao.countAppointments(searchString);
    assertNull(result);
  }

  @Test
  public void getObsByEncounterAndConcept() {

    Concept concept = new Concept();
    concept.setConceptId(1);
    concept.setId(1);

    Obs obs = obsFactory().get(0);

    when(dbSession.createCriteria(Obs.class)).thenReturn(criteria);
    when(criteria.add(any(Criterion.class))).thenReturn(criteria);
    when(criteria.uniqueResult()).thenReturn(obs);

    Obs obsOne = hibernateAppointmentDao.getObsByEncounterAndConcept(1, 1);
    assertNotNull(obsOne);
  }

  private static List<Integer> consentedPatientIds() {
    ConsentedPatient patient = new ConsentedPatient();
    patient.setPatientId(1);
    patient.setId(1);

    ConsentedPatient patient2 = new ConsentedPatient();
    patient2.setPatientId(1);
    patient2.setId(1);

    List<ConsentedPatient> consentedPatients = new ArrayList<ConsentedPatient>();
    consentedPatients.add(patient);
    consentedPatients.add(patient2);

    return Collections.singletonList(consentedPatients.size());
  }

  private static List<Obs> obsFactory() {
    Obs obsOne = new Obs();
    obsOne.setId(1);
    obsOne.setObsId(1);
    obsOne.setConcept(conceptFactory());
    obsOne.setEncounter(encounterFactory());

    Obs obsTwo = new Obs();
    obsTwo.setId(1);
    obsTwo.setObsId(1);

    List<Obs> obsList = new ArrayList<Obs>();
    obsList.add(obsOne);
    obsList.add(obsTwo);

    return obsList;
  }

  private static List<AppointmentRequests> appointmentRequestsListFactory() {
    AppointmentRequests appointmentRequests = new AppointmentRequests();
    appointmentRequests.setAppointmentUUID(String.valueOf(UUID.randomUUID()));
    appointmentRequests.setId(1);
    appointmentRequests.setMycarehubId(String.valueOf(UUID.randomUUID()));
    appointmentRequests.setClientName("MtuKazi");
    appointmentRequests.setCccNumber("12345");
    appointmentRequests.setAppointmentReason("away");
    appointmentRequests.setStatus("PENDING");
    appointmentRequests.setClientContact("12345");

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
}
