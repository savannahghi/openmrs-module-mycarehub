package org.openmrs.module.mycarehub.api.db;

import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.module.mycarehub.api.rest.mapper.MyCareHubAllergy;
import org.openmrs.module.mycarehub.model.ConsentedPatient;

import java.util.Date;
import java.util.List;

public interface MyCareHubPatientDao {
	
	List<Integer> getConsentedPatientIdsUpdatedSinceDate(Date lastSyncDate);
	
	List<Integer> getCccPatientIdsByIdentifier(String cccNumber);
	
	List<Integer> getConsentedPatientsWithUpdatedMedicalRecordsSinceDate(Date lastSyncDate);
	
	List<Obs> getUpdatedVitalSignsSinceDate(Patient patient, Date lastSyncDate);
	
	List<MyCareHubAllergy> getUpdatedAllergiesSinceDate(Patient patient, Date lastSyncDate);
	
	List<Obs> getUpdatedTestsSinceDate(Patient patient, Date lastSyncDate);
	
	List<Obs> getUpdatedTestOrdersSinceDate(Patient patient, Date lastSyncDate);
	
	List<Obs> getUpdatedMedicationsSinceDate(Patient patient, Date lastSyncDate);

	List<ConsentedPatient> getConsentedPatientsInList(List<Integer> patientIdList);

	void saveNewConsentedPatients(ConsentedPatient consentedPatient);
}
