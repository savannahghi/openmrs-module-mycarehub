package org.openmrs.module.mycarehub.api.db.hibernate;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.mycarehub.api.db.MyCareHubPatientDao;
import org.openmrs.module.mycarehub.api.rest.mapper.MyCareHubAllergy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.openmrs.module.mycarehub.utils.Constants.APPOINTMENT_DATE_CONCEPT_ID;
import static org.openmrs.module.mycarehub.utils.Constants.CCC_NUMBER_IDENTIFIER_TYPE_UUID;
import static org.openmrs.module.mycarehub.utils.Constants.MedicalRecordConcepts.Allergies.ALLERGEN_CONCEPTS;
import static org.openmrs.module.mycarehub.utils.Constants.MedicalRecordConcepts.Allergies.ALLERGY_DATE;
import static org.openmrs.module.mycarehub.utils.Constants.MedicalRecordConcepts.Allergies.ALLERGY_GROUP_CONCEPTS;
import static org.openmrs.module.mycarehub.utils.Constants.MedicalRecordConcepts.Allergies.ALLERGY_OTHER_REACTION;
import static org.openmrs.module.mycarehub.utils.Constants.MedicalRecordConcepts.Allergies.ALLERGY_REACTION_CONCEPTS;
import static org.openmrs.module.mycarehub.utils.Constants.MedicalRecordConcepts.Allergies.ALLERGY_SEVERITY_CONCEPTS;
import static org.openmrs.module.mycarehub.utils.Constants.MedicalRecordConcepts.Medications.REGIMEN;
import static org.openmrs.module.mycarehub.utils.Constants.MedicalRecordConcepts.Tests.TESTS_ORDERED;
import static org.openmrs.module.mycarehub.utils.Constants.MedicalRecordConcepts.VitalSigns.BMI;
import static org.openmrs.module.mycarehub.utils.Constants.MedicalRecordConcepts.VitalSigns.CD4_COUNT;
import static org.openmrs.module.mycarehub.utils.Constants.MedicalRecordConcepts.VitalSigns.HEIGHT;
import static org.openmrs.module.mycarehub.utils.Constants.MedicalRecordConcepts.VitalSigns.PULSE;
import static org.openmrs.module.mycarehub.utils.Constants.MedicalRecordConcepts.VitalSigns.RESPIRATORY_RATE;
import static org.openmrs.module.mycarehub.utils.Constants.MedicalRecordConcepts.VitalSigns.SPO2;
import static org.openmrs.module.mycarehub.utils.Constants.MedicalRecordConcepts.VitalSigns.TEMPERATURE;
import static org.openmrs.module.mycarehub.utils.Constants.MedicalRecordConcepts.VitalSigns.VIRAL_LOAD;
import static org.openmrs.module.mycarehub.utils.Constants.MedicalRecordConcepts.VitalSigns.WEIGHT;
import static org.openmrs.module.mycarehub.utils.Constants._PersonAttributeType.NEXT_OF_KIN_CONTACT;
import static org.openmrs.module.mycarehub.utils.Constants._PersonAttributeType.NEXT_OF_KIN_NAME;
import static org.openmrs.module.mycarehub.utils.Constants._PersonAttributeType.NEXT_OF_KIN_RELATIONSHIP;
import static org.openmrs.module.mycarehub.utils.Constants._PersonAttributeType.TELEPHONE_CONTACT;

public class HibernateMyCareHubPatientDao implements MyCareHubPatientDao {
	
	private DbSessionFactory sessionFactory;
	
	public HibernateMyCareHubPatientDao(DbSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	private DbSession getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public List<Integer> getCccPatientIdsCreatedOrUpdatedSinceDate(Date lastSyncDate) {
		String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lastSyncDate);
		
		Query query = sessionFactory
		        .getCurrentSession()
		        .createSQLQuery(
		            "SELECT DISTINCT patient.patient_id as patientId FROM patient "
		                    + "INNER JOIN person ON patient.patient_id = person.person_id "
		                    + "INNER JOIN person_name ON person_name.person_id = patient.patient_id "
		                    + "INNER JOIN patient_identifier ON patient.patient_id = patient_identifier.patient_id "
		                    + "WHERE ("
		                    + "patient.date_created >=:formattedDate "
		                    + "OR person.date_changed >=:formattedDate "
		                    + "OR person_name.date_changed >=:formattedDate "
		                    + "OR patient.patient_id in ("
		                    + "SELECT person_id FROM person_attribute "
		                    + "INNER JOIN person_attribute_type ON person_attribute.person_attribute_type_id = person_attribute_type.person_attribute_type_id "
		                    + "WHERE ( person_attribute.date_created >=:formattedDate OR person_attribute.date_changed >=:formattedDate) "
		                    + "AND person_attribute_type.uuid IN (:personAttributeTypeUuids) )) "
		                    + "OR patient.patient_id in ( "
		                    + "SELECT patient_id FROM patient_identifier  "
		                    + "WHERE ( patient_identifier.date_created >=:formattedDate OR patient_identifier.date_changed >=:formattedDate))"
		                    + "AND patient_identifier.identifier_type IN ( "
		                    + "SELECT patient_identifier_type_id FROM patient_identifier_type WHERE uuid = :cccIdentifierTypeUuid)"
		                    + "AND patient.voided=0");
		query.setParameter("formattedDate", formattedDate);
		query.setParameterList("personAttributeTypeUuids", getPersonAttributeTypesList());
		query.setParameter("cccIdentifierTypeUuid", CCC_NUMBER_IDENTIFIER_TYPE_UUID);
		return query.list();
	}
	
	public List<Integer> getCccPatientIdsByIdentifier(String cccNumber) {
		Query query = sessionFactory.getCurrentSession().createSQLQuery(
		    "SELECT patient.patient_id as patientId FROM patient "
		            + "INNER JOIN patient_identifier ON patient.patient_id = patient_identifier.patient_id "
		            + "WHERE patient_identifier.identifier_type IN ("
		            + "SELECT patient_identifier_type_id FROM patient_identifier_type "
		            + "WHERE uuid = :cccIdentifierTypeUuid) " + "AND patient_identifier.identifier = :cccNumber");
		query.setParameter("cccNumber", cccNumber);
		query.setParameter("cccIdentifierTypeUuid", CCC_NUMBER_IDENTIFIER_TYPE_UUID);
		return query.list();
	}
	
	public List<Integer> getCccPatientsWithUpdatedMedicalRecordsSinceDate(Date lastSyncDate) {
		
		SQLQuery query = getSession().createSQLQuery(
		    "SELECT distinct patient.patient_id as patientId FROM patient "
		            + "INNER JOIN patient_identifier ON patient.patient_id = patient_identifier.patient_id "
		            + "INNER JOIN obs ON obs.person_id = patient.patient_id "
		            + "WHERE patient_identifier.identifier_type IN ( "
		            + "SELECT patient_identifier_type_id FROM patient_identifier_type WHERE uuid = :cccIdentifierTypeUuid) "
		            + "AND (obs.concept_id IN :conceptIds OR obs.value_coded IN (:drugConceptIds))"
		            + "AND patient.voided=0 " + "AND obs.voided=0 " + "AND obs.date_created >=:formattedLastSyncDate");
		query.setParameter("cccIdentifierTypeUuid", CCC_NUMBER_IDENTIFIER_TYPE_UUID);
		query.setParameterList("conceptIds", getMedicalRecordConceptsList());
		query.setParameterList("drugConceptIds", getDrugsConceptsList());
		
		String formattedLastSyncDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lastSyncDate);
		query.setParameter("formattedLastSyncDate", formattedLastSyncDate);
		return query.list();
	}
	
	public List<Obs> getUpdatedVitalSignsSinceDate(Patient patient, Date lastSyncDate) {
		Criteria criteria = getSession().createCriteria(Obs.class);
		criteria.add(Restrictions.eq("personId", patient.getPatientId()));
		criteria.add(Restrictions.ge("dateCreated", lastSyncDate));
		criteria.add(Restrictions.eq("voided", false));
		criteria.add(Restrictions.in("concept.conceptId", getVitalSignsConceptsList()));
		return criteria.list();
	}
	
	public List<MyCareHubAllergy> getUpdatedAllergiesSinceDate(Patient patient, Date lastSyncDate) {
		SQLQuery allergenQuery = getSession()
		        .createSQLQuery(
		            "SELECT allergyName,allergyAnswerConceptId,reaction,reactionAnswerConceptId,severity,severityAnswerConceptId,allergyDateTimeObj FROM ( "
		                    + "SELECT "
		                    + "(SELECT concept_name.name FROM concept_name JOIN obs ON obs.value_coded = concept_name.concept_id "
		                    + "WHERE obs.concept_id IN :allergenConcept AND obs.obs_group_id = obsgroups.obs_group_id  AND obs.voided=0 AND concept_name.locale='en' LIMIT 1) AS allergyName,"
		                    + "(SELECT value_coded FROM obs "
		                    + "WHERE obs.concept_id IN :allergenConcept AND obs.obs_group_id = obsgroups.obs_group_id  AND obs.voided=0 LIMIT 1) AS allergyAnswerConceptId,"
		                    + "(SELECT concept_name.name FROM concept_name JOIN obs ON obs.value_coded = concept_name.concept_id "
		                    + "WHERE obs.concept_id IN :reactionConcepts AND obs.obs_group_id = obsgroups.obs_group_id  AND obs.voided=0 AND concept_name.locale='en' LIMIT 1) AS reaction,"
		                    + "(SELECT obs.value_coded FROM obs "
		                    + "WHERE obs.concept_id IN :reactionConcepts AND obs.obs_group_id = obsgroups.obs_group_id  AND obs.voided=0 LIMIT 1) AS reactionAnswerConceptId,"
		                    + "(SELECT value_text FROM obs WHERE obs.concept_id=:otherReactionConcept AND obs.obs_group_id = obsgroups.obs_group_id  AND obs.voided=0 LIMIT 1) AS otherReaction, "
		                    + "(SELECT concept_name.name FROM concept_name JOIN obs ON obs.value_coded = concept_name.concept_id "
		                    + "WHERE obs.concept_id IN :severityConcepts AND obs.obs_group_id = obsgroups.obs_group_id  AND obs.voided=0 AND concept_name.locale='en' LIMIT 1 ) AS severity,"
		                    + "(SELECT obs.value_coded FROM obs WHERE obs.concept_id IN :severityConcepts AND obs.obs_group_id = obsgroups.obs_group_id  AND obs.voided=0 LIMIT 1 ) AS severityAnswerConceptId,"
		                    + "(SELECT value_datetime FROM obs WHERE obs.concept_id=:allergyDateConcept AND obs.obs_group_id = obsgroups.obs_group_id  AND obs.voided=0 LIMIT 1) AS allergyDateTimeObj "
		                    + "FROM ("
		                    + "SELECT obs_id AS obs_group_id FROM obs WHERE concept_id IN :allergyGroupConcepts AND date_created >= :formattedLastSyncDate AND voided = 0 AND person_id = :patientId"
		                    + ") AS obsgroups) AS allergies ");
		
		allergenQuery.setParameterList("allergenConcept", ALLERGEN_CONCEPTS);
		allergenQuery.setParameterList("reactionConcepts", ALLERGY_REACTION_CONCEPTS);
		allergenQuery.setParameter("otherReactionConcept", ALLERGY_OTHER_REACTION);
		allergenQuery.setParameterList("severityConcepts", ALLERGY_SEVERITY_CONCEPTS);
		allergenQuery.setParameter("allergyDateConcept", ALLERGY_DATE);
		allergenQuery.setParameterList("allergyGroupConcepts", ALLERGY_GROUP_CONCEPTS);
		allergenQuery.setParameter("patientId", patient.getPatientId());
		
		String formattedLastSyncDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lastSyncDate);
		allergenQuery.setParameter("formattedLastSyncDate", formattedLastSyncDate);
		
		allergenQuery.setResultTransformer(Transformers.aliasToBean(MyCareHubAllergy.class));
		
		return allergenQuery.list();
	}
	
	public List<Obs> getUpdatedTestsSinceDate(Patient patient, Date lastSyncDate) {
		List<Integer> testsConceptsList = getTestsConceptsList();
		Criteria criteria = getSession().createCriteria(Obs.class);
		criteria.add(Restrictions.eq("personId", patient.getPatientId()));
		criteria.add(Restrictions.ge("dateCreated", lastSyncDate));
		criteria.add(Restrictions.eq("voided", false));
		criteria.add(Restrictions.in("concept.conceptId", testsConceptsList));
		return criteria.list();
	}
	
	public List<Obs> getUpdatedTestOrdersSinceDate(Patient patient, Date lastSyncDate) {
		Criteria criteria = getSession().createCriteria(Obs.class);
		criteria.add(Restrictions.eq("personId", patient.getPatientId()));
		criteria.add(Restrictions.ge("dateCreated", lastSyncDate));
		criteria.add(Restrictions.eq("voided", false));
		criteria.add(Restrictions.eq("concept.conceptId", TESTS_ORDERED));
		return criteria.list();
	}
	
	public List<Obs> getUpdatedMedicationsSinceDate(Patient patient, Date lastSyncDate) {
		List<Integer> fullDrugConcepts = getDrugsConceptsList();
		Criteria criteria = getSession().createCriteria(Obs.class);
		criteria.add(Restrictions.eq("personId", patient.getPatientId()));
		criteria.add(Restrictions.ge("dateCreated", lastSyncDate));
		criteria.add(Restrictions.in("valueCoded.conceptId", fullDrugConcepts));
		criteria.add(Restrictions.eq("voided", false));
		return criteria.list();
	}
	
	private List<Integer> getDrugsConceptsList() {
		SQLQuery drugsConcepts = getSession().createSQLQuery(
		    "SELECT concept_id FROM concept "
		            + "WHERE (class_id = 3 OR concept_id In (SELECT concept_id FROM drug)) AND retired=0");
		List<Integer> fullDrugConcepts = new ArrayList<Integer>();
		fullDrugConcepts.addAll(drugsConcepts.list());
		fullDrugConcepts.add(164505);
		fullDrugConcepts.add(162565);
		return fullDrugConcepts;
	}
	
	private List<Integer> getMedicalRecordConceptsList() {
		List<Integer> medicalRecordConceptsList = new ArrayList<Integer>();
		medicalRecordConceptsList.addAll(getMedicationsConceptsList());
		medicalRecordConceptsList.addAll(getTestsConceptsList());
		medicalRecordConceptsList.addAll(getVitalSignsConceptsList());
		medicalRecordConceptsList.addAll(getAllergyDetailsConceptsList());
		return medicalRecordConceptsList;
	}
	
	private List<Integer> getMedicationsConceptsList() {
		List<Integer> medicationsConceptsList = new ArrayList<Integer>();
		medicationsConceptsList.add(REGIMEN);
		return medicationsConceptsList;
	}
	
	private List<Integer> getTestsConceptsList() {
		SQLQuery testsConcepts = getSession().createSQLQuery(
		    "SELECT concept_id FROM concept WHERE class_id = 1 AND retired=0");
		return testsConcepts.list();
	}
	
	private List<Integer> getVitalSignsConceptsList() {
		List<Integer> vitalSignsConceptsList = new ArrayList<Integer>();
		vitalSignsConceptsList.add(PULSE);
		vitalSignsConceptsList.add(TEMPERATURE);
		vitalSignsConceptsList.add(WEIGHT);
		vitalSignsConceptsList.add(HEIGHT);
		vitalSignsConceptsList.add(BMI);
		vitalSignsConceptsList.add(SPO2);
		vitalSignsConceptsList.add(CD4_COUNT);
		vitalSignsConceptsList.add(VIRAL_LOAD);
		vitalSignsConceptsList.add(RESPIRATORY_RATE);
		vitalSignsConceptsList.add(APPOINTMENT_DATE_CONCEPT_ID);
		return vitalSignsConceptsList;
	}
	
	private List<Integer> getAllergyDetailsConceptsList() {
		List<Integer> allergyDetailsConceptsList = new ArrayList<Integer>();
		allergyDetailsConceptsList.addAll(ALLERGEN_CONCEPTS);
		allergyDetailsConceptsList.addAll(ALLERGY_REACTION_CONCEPTS);
		allergyDetailsConceptsList.addAll(ALLERGY_SEVERITY_CONCEPTS);
		allergyDetailsConceptsList.addAll(ALLERGY_GROUP_CONCEPTS);
		allergyDetailsConceptsList.add(ALLERGY_OTHER_REACTION);
		allergyDetailsConceptsList.add(ALLERGY_DATE);
		return allergyDetailsConceptsList;
	}
	
	private List<String> getPersonAttributeTypesList() {
		List<String> personAttributeTypeUuids = new ArrayList<String>();
		personAttributeTypeUuids.add(TELEPHONE_CONTACT);
		personAttributeTypeUuids.add(NEXT_OF_KIN_NAME);
		personAttributeTypeUuids.add(NEXT_OF_KIN_CONTACT);
		personAttributeTypeUuids.add(NEXT_OF_KIN_RELATIONSHIP);
		return personAttributeTypeUuids;
	}
}
