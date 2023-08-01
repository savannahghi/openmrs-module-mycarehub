package org.openmrs.module.mycarehub.api.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openmrs.Patient;
import org.openmrs.module.mycarehub.api.db.MyCareHubPatientDao;
import org.openmrs.module.mycarehub.api.service.MyCareHubSettingsService;
import org.openmrs.module.mycarehub.model.ConsentedPatient;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MyCareHubPatientServiceImplTest {
	
	@Mock
	MyCareHubPatientDao myCareHubPatientDao;

	@Mock
	MyCareHubSettingsService settingsService;

	@InjectMocks
	MyCareHubPatientServiceImpl myCareHubPatientServiceImpl;
	
	public static final Patient PATIENT = testPatientFactory();
	public static final ConsentedPatient CONSENTED_PATIENT = testConsentedPatientFactory();
	
	@Before
	public void setUp() {

	}

	@Test
	public void testSaveConsentedPatients() {
		List<Patient> patientListFromMycareHub = new ArrayList<Patient>();
		patientListFromMycareHub.add(PATIENT);

		List<ConsentedPatient> previouslySyncedPatients = new ArrayList<ConsentedPatient>();
		ConsentedPatient consentedPatient = CONSENTED_PATIENT;
		previouslySyncedPatients.add(consentedPatient);

		List<Integer> patientIDs = new ArrayList<Integer>();
		patientIDs.add(patientListFromMycareHub.get(0).getPatientId());

		when(myCareHubPatientDao.getConsentedPatientsInList(patientIDs)).thenReturn(previouslySyncedPatients);
		List<ConsentedPatient> consentedPatientsList = myCareHubPatientDao.getConsentedPatientsInList(patientIDs);
		assertEquals(1, consentedPatientsList.size());

		myCareHubPatientServiceImpl.saveConsentedPatients(patientListFromMycareHub);
	}
	
	private static Patient testPatientFactory() {
		Patient patient = new Patient();
		patient.setId(1);
		
		return patient;
	}

	private static ConsentedPatient testConsentedPatientFactory() {
		ConsentedPatient patient = new ConsentedPatient();
		patient.setId(1);

		return patient;
	}
}
