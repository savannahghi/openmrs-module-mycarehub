package org.openmrs.module.mycarehub.api.service.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.openmrs.module.mycarehub.utils.Constants.MyCareHubSettingType.*;
import static org.openmrs.module.mycarehub.utils.Constants._PersonAttributeType.TELEPHONE_CONTACT;
import static org.powermock.api.mockito.PowerMockito.*;

import java.util.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openmrs.*;
import org.openmrs.api.PatientService;
import org.openmrs.api.ProgramWorkflowService;
import org.openmrs.api.context.Context;
import org.openmrs.module.mycarehub.api.db.MyCareHubPatientDao;
import org.openmrs.module.mycarehub.api.rest.mapper.PatientRegistration;
import org.openmrs.module.mycarehub.api.service.MyCareHubSettingsService;
import org.openmrs.module.mycarehub.model.ConsentedPatient;
import org.openmrs.module.mycarehub.model.MyCareHubSetting;
import org.openmrs.module.mycarehub.utils.MyCareHubUtil;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class, MyCareHubUtil.class})
public class MyCareHubPatientServiceImplTest {

  @Mock MyCareHubPatientDao myCareHubPatientDao;

  @Mock MyCareHubSettingsService settingsService;

  @InjectMocks MyCareHubPatientServiceImpl myCareHubPatientServiceImpl;

  public static final Patient PATIENT = testPatientFactory();

  public static final ConsentedPatient CONSENTED_PATIENT = testConsentedPatientFactory();

  public static final List<PatientRegistration> PATIENT_REGISTRATION = patientRegistrationFactory();

  private static MyCareHubSetting setting = null;

  private MyCareHubSettingsService myCareHubSettingsService;

  @Before
  public void setUp() {
    myCareHubSettingsService = mock(MyCareHubSettingsService.class);
    setting = mock(MyCareHubSetting.class);

    mockStatic(Context.class);
    mockStatic(MyCareHubUtil.class);
    when(Context.getService(MyCareHubSettingsService.class)).thenReturn(myCareHubSettingsService);

    when(Context.getPatientService()).thenReturn(mock(PatientService.class));

    when(Context.getProgramWorkflowService()).thenReturn(mock(ProgramWorkflowService.class));
  }

  @Test
  public void testSaveConsentedPatients() {
    List<Patient> patientListFromMycareHub = new ArrayList<Patient>();
    patientListFromMycareHub.add(PATIENT);

    List<ConsentedPatient> previouslySyncedPatients = new ArrayList<ConsentedPatient>();
    previouslySyncedPatients.add(CONSENTED_PATIENT);

    List<Integer> patientIDs = new ArrayList<Integer>();
    patientIDs.add(patientListFromMycareHub.get(0).getPatientId());

    when(myCareHubPatientDao.getConsentedPatientsInList(patientIDs))
        .thenReturn(previouslySyncedPatients);
    List<ConsentedPatient> consentedPatientsList =
        myCareHubPatientDao.getConsentedPatientsInList(patientIDs);
    assertEquals(1, consentedPatientsList.size());

    myCareHubPatientServiceImpl.saveConsentedPatients(patientListFromMycareHub);
  }

  @Test
  public void uploadUpdatedPatientDemographicsSinceLastSyncDate_null_setting() {
    when(myCareHubSettingsService.getLatestMyCareHubSettingByType(PATIENT_RED_FLAGS_REQUESTS_POST))
        .thenReturn(null);

    myCareHubPatientServiceImpl.uploadUpdatedPatientDemographicsSinceLastSyncDate();
  }

  @Test
  public void uploadUpdatedPatientDemographicsSinceLastSyncDate() {
    setting.setSettingType(KENYAEMR_PATIENT_REGISTRATIONS);
    when(myCareHubSettingsService.getLatestMyCareHubSettingByType(KENYAEMR_PATIENT_REGISTRATIONS))
        .thenReturn(setting);

    myCareHubPatientServiceImpl.uploadUpdatedPatientDemographicsSinceLastSyncDate();
  }

  @Test
  public void syncPatientDataNullSetting() {
    when(myCareHubSettingsService.getLatestMyCareHubSettingByType(KENYAEMR_PATIENT_REGISTRATIONS))
        .thenReturn(null);

    myCareHubPatientServiceImpl.syncPatientData();
  }

  @Test
  public void syncPatientData_fetchRegisteredClientIdentifiersSinceLastSyncDate() {
    MyCareHubSetting myCareHubSetting = new MyCareHubSetting();
    myCareHubSetting.setSettingType(PATIENT_HEALTH_DIARY_GET);
    myCareHubSetting.setLastSyncTime(new Date());

    when(myCareHubSettingsService.getLatestMyCareHubSettingByType(KENYAEMR_PATIENT_REGISTRATIONS))
        .thenReturn(setting);

    myCareHubPatientServiceImpl.syncPatientData();
  }

  @Test
  public void getUpdatedPatientRegistrationsSinceLastSyncDate() {
    MyCareHubSetting myCareHubSetting = new MyCareHubSetting();
    myCareHubSetting.setSettingType(KENYAEMR_PATIENT_REGISTRATIONS);
    myCareHubSetting.setLastSyncTime(new Date());

    when(myCareHubSettingsService.getLatestMyCareHubSettingByType(KENYAEMR_PATIENT_REGISTRATIONS))
        .thenReturn(setting);

    List<ConsentedPatient> consentedPatientList = Collections.singletonList(CONSENTED_PATIENT);
    when(myCareHubPatientDao.getConsentedPatientIdsUpdatedSinceDate(
            myCareHubSetting.getLastSyncTime()))
        .thenReturn(Collections.singletonList(consentedPatientList.get(0).getPatientId()));

    when(Context.getPatientService().getPatient(1)).thenReturn(PATIENT);

    // TODO: Relook at this test later to to cover
    // ``Context.getPersonService().getPersonAttributeTypeByUuid(TELEPHONE_CONTACT);``
    //
    // when(myCareHubPatientServiceImpl.getUpdatedPatientRegistrationsSinceLastSyncDate(myCareHubSetting.getLastSyncTime())).thenReturn(PATIENT_REGISTRATION);
  }

  private static PersonAttribute personAttributeFactory() {
    PersonAttribute personAttribute = new PersonAttribute();
    personAttribute.setId(1);
    personAttribute.setPersonAttributeId(1);
    personAttribute.setUuid(TELEPHONE_CONTACT);
    return personAttribute;
  }

  private static PatientProgram patientProgramFactory() {
    PatientProgram patientProgram = new PatientProgram();
    patientProgram.setPatient(testPatientFactory());
    return patientProgram;
  }

  private static Patient testPatientFactory() {
    Patient patient = new Patient();
    patient.setId(1);
    patient.setPatientId(1);
    patient.setNames(getPersonNames());
    patient.setBirthdate(new Date());
    patient.setBirthdateEstimated(true);
    patient.setDateCreated(new Date());

    PatientIdentifierType patientIdentifierType = getPatientIdentifierType();

    PatientIdentifier patientIdentifier = getPatientIdentifier(patientIdentifierType);

    Collection<PatientIdentifier> identifiers = getIdentifiers(patientIdentifier);

    Set<PatientIdentifier> patientIdentifierSet = getPatientIdentifiers(patientIdentifier);

    patient.addIdentifiers(identifiers);
    patient.setIdentifiers(patientIdentifierSet);

    return patient;
  }

  private static ConsentedPatient testConsentedPatientFactory() {
    ConsentedPatient patient = new ConsentedPatient();
    patient.setId(1);
    patient.setPatientId(1);

    return patient;
  }

  private static List<PatientRegistration> patientRegistrationFactory() {
    List<PatientRegistration> patientRegistrationList = new ArrayList<PatientRegistration>();

    PatientRegistration patientRegistration = new PatientRegistration();
    patientRegistration.setCounseled(true);
    patientRegistration.setCccNumber("12345");
    patientRegistration.setClientType("PMTCT");
    patientRegistration.setMFLCODE("1234");
    patientRegistration.setName("Hojlund");

    patientRegistrationList.add(patientRegistration);

    return patientRegistrationList;
  }

  private static PatientIdentifier getPatientIdentifier(
      PatientIdentifierType patientIdentifierType) {
    PatientIdentifier patientIdentifier = new PatientIdentifier();
    patientIdentifier.setIdentifierType(patientIdentifierType);
    patientIdentifier.setIdentifier("ccc_number");
    patientIdentifier.setPatientIdentifierId(1234);
    patientIdentifier.setId(1234);
    patientIdentifier.setPreferred(true);
    patientIdentifier.setVoided(false);
    return patientIdentifier;
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

  private static Set<PersonName> getPersonNames() {
    PersonName personName = new PersonName();
    personName.setFamilyName("Hojlund");
    personName.setGivenName("Hojlund");

    Set<PersonName> personNameSet = new HashSet<PersonName>();
    personNameSet.add(personName);

    return personNameSet;
  }
}
