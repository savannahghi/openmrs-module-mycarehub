package org.openmrs.module.mycarehub.api.service.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.openmrs.module.mycarehub.utils.Constants.MyCareHubSettingType.*;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.util.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.api.PatientService;
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
  public void syncPatientData_null_setting() {
    when(myCareHubSettingsService.getLatestMyCareHubSettingByType(KENYAEMR_PATIENT_REGISTRATIONS))
        .thenReturn(null);

    myCareHubPatientServiceImpl.syncPatientData();
  }

  @Test
  public void syncPatientData_fetchRegisteredClientIdentifiersSinceLastSyncDate() {
    setting.setSettingType(KENYAEMR_PATIENT_REGISTRATIONS);
    Date date = new Date();
    setting.setLastSyncTime(date);
    when(myCareHubSettingsService.getLatestMyCareHubSettingByType(KENYAEMR_PATIENT_REGISTRATIONS))
        .thenReturn(setting);

    myCareHubPatientServiceImpl.syncPatientData();
  }

  private static Patient testPatientFactory() {
    Patient patient = new Patient();
    patient.setId(1);
    patient.setPatientId(1);

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
}
