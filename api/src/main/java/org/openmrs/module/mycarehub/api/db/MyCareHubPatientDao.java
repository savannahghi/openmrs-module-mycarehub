package org.openmrs.module.mycarehub.api.db;

import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.module.mycarehub.api.rest.mapper.MyCareHubAllergy;

import java.util.Date;
import java.util.List;

public interface MyCareHubPatientDao {
	
	List<Patient> getCccPatientsCreatedOrUpdatedSinceDate(Date lastSyncDate);
	
	List<Patient> getCccPatientsByIdentifier(String cccNumber);
	
	List<Patient> getCccPatientsWithUpdatedMedicalRecordsSinceDate(Date lastSyncDate);
	List<Obs> getUpdatedVitalSignsSinceDate(Patient patient, Date lastSyncDate);
	List<Obs> getUpdatedTestsSinceDate(Patient patient, Date lastSyncDate);
	List<Obs> getUpdatedMedicationsSinceDate(Patient patient, Date lastSyncDate);
	List<MyCareHubAllergy> getUpdatedAllergiesSinceDate(Patient patient, Date lastSyncDate);
}
