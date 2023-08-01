package org.openmrs.module.mycarehub.api.db;

import java.util.Date;
import java.util.List;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.module.mycarehub.api.rest.mapper.MyCareHubAllergy;
import org.openmrs.module.mycarehub.model.ConsentedPatient;

/**
 * Handles storage and retrieval of patient information to/from KenyaEMR. A consented patient is a
 * patient who has given consent to use myCareHub. Any patient from the myCareHub API is considered
 * consented
 */
public interface MyCareHubPatientDao {
	
	/**
	 * Queries myCareHub for any new patients since the lastSyncDate.
	 * 
	 * @param lastSyncDate The last time a successful synchronization was done.
	 * @return A list of patient IDs
	 */
	List<Integer> getConsentedPatientIdsUpdatedSinceDate(Date lastSyncDate);
	
	List<Integer> getCccPatientIdsByIdentifier(String cccNumber);
	
	/**
	 * Retrieves myCareHub patients that have received new updates to their medical record
	 * 
	 * @param lastSyncDate The last time a successful synchronization was done.
	 * @return A list of patient IDs
	 */
	List<Integer> getConsentedPatientsWithUpdatedMedicalRecordsSinceDate(Date lastSyncDate);
	
	/**
	 * Retrieves a patient's updated vital signs since the lastSyncDate
	 * 
	 * @param patient The patient whose information to retrieve
	 * @param lastSyncDate The last time a successful synchronization was done.
	 * @return A list of patient vitals in the form of Observations(Obs)
	 */
	List<Obs> getUpdatedVitalSignsSinceDate(Patient patient, Date lastSyncDate);
	
	/**
	 * Retrieves a patient's updated allergies since the lastSyncDate
	 * 
	 * @param patient The patient whose information to retrieve
	 * @param lastSyncDate The last time a successful synchronization was done.
	 * @return A list of patient allergies
	 */
	List<MyCareHubAllergy> getUpdatedAllergiesSinceDate(Patient patient, Date lastSyncDate);
	
	/**
	 * Retrieves a patient's updated tests since the lastSyncDate
	 * 
	 * @param patient The patient whose information to retrieve
	 * @param lastSyncDate The last time a successful synchronization was done.
	 * @return A list of tests in the form of Observations(Obs)
	 */
	List<Obs> getUpdatedTestsSinceDate(Patient patient, Date lastSyncDate);
	
	/**
	 * Retrieves a patient's updated test orders since the lastSyncDate
	 * 
	 * @param patient The patient whose information to retrieve
	 * @param lastSyncDate The last time a successful synchronization was done.
	 * @return A list of patient test orders in the form of Observations(Obs)
	 */
	List<Obs> getUpdatedTestOrdersSinceDate(Patient patient, Date lastSyncDate);
	
	/**
	 * Retrieves a patient's updated medications since the lastSyncDate
	 * 
	 * @param patient The patient whose information to retrieve
	 * @param lastSyncDate The last time a successful synchronization was done.
	 * @return A list of patient medications in the form of Observations(Obs).
	 */
	List<Obs> getUpdatedMedicationsSinceDate(Patient patient, Date lastSyncDate);
	
	/**
	 * Retrieves consented patients in a list form.
	 * 
	 * @param patientIdList The list of patients from myCareHub to retrieve.
	 * @return A list of patients.
	 */
	List<ConsentedPatient> getConsentedPatientsInList(List<Integer> patientIdList);
	
	/**
	 * Saves a consented patient.
	 * 
	 * @param consentedPatient The patient to save
	 * @return A list of patients
	 */
	void saveNewConsentedPatients(ConsentedPatient consentedPatient);
}
