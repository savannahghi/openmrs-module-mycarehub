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
import java.util.Date;
import java.util.List;

import static org.openmrs.module.mycarehub.utils.Constants.CCC_NUMBER_IDENTIFIER_TYPE_UUID;
import static org.openmrs.module.mycarehub.utils.Constants.MedicalRecordConcepts.Allergies.ALLERGEN;
import static org.openmrs.module.mycarehub.utils.Constants.MedicalRecordConcepts.Allergies.ALLERGY_DATE;
import static org.openmrs.module.mycarehub.utils.Constants.MedicalRecordConcepts.Allergies.ALLERGY_OTHER_REACTION;
import static org.openmrs.module.mycarehub.utils.Constants.MedicalRecordConcepts.Allergies.ALLERGY_REACTION;
import static org.openmrs.module.mycarehub.utils.Constants.MedicalRecordConcepts.Allergies.ALLERGY_SEVERITY;
import static org.openmrs.module.mycarehub.utils.Constants.MedicalRecordConcepts.Tests.TESTS_ORDERED;
import static org.openmrs.module.mycarehub.utils.MyCareHubUtil.getMedicalRecordConceptsList;
import static org.openmrs.module.mycarehub.utils.MyCareHubUtil.getTestsConceptsList;
import static org.openmrs.module.mycarehub.utils.MyCareHubUtil.getVitalSignsConceptsList;
import static org.openmrs.module.mycarehub.utils.MyCareHubUtil.getPersonAttributeTypesList;

public class HibernateMyCareHubPatientDao implements MyCareHubPatientDao {
	
	private DbSessionFactory sessionFactory;
	
	public HibernateMyCareHubPatientDao(DbSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	private DbSession getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public List<Patient> getCccPatientsCreatedOrUpdatedSinceDate(Date lastSyncDate) {
		String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lastSyncDate);
		
		Query query = sessionFactory
		        .getCurrentSession()
		        .createSQLQuery(
		            "SELECT DISTINCT patient.patient_id FROM patient "
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
		                    + "AND patient_identifier.identifier_type IN ( "
		                    + "SELECT patient_identifier_type_id FROM patient_identifier_type WHERE uuid = :cccIdentifierTypeUuid)"
		                    + "AND patient.voided=0");
		query.setParameter("formattedDate", formattedDate);
		query.setParameterList("personAttributeTypeUuids", getPersonAttributeTypesList());
		query.setParameter("cccIdentifierTypeUuid", CCC_NUMBER_IDENTIFIER_TYPE_UUID);
		query.setResultTransformer(Transformers.aliasToBean(Patient.class));
		return query.list();
	}
	
	public List<Patient> getCccPatientsByIdentifier(String cccNumber) {
		Query query = sessionFactory.getCurrentSession().createSQLQuery(
		    "SELECT patient_id FROM patient "
		            + "INNER JOIN patient_identifier ON patient.patient_id = patient_identifier.patient_id "
		            + "WHERE patient_identifier.identifier_type IN ("
		            + "SELECT patient_identifier_type_id FROM patient_identifier_type "
		            + "WHERE uuid = :cccIdentifierTypeUuid) " + "AND patient_identifier.identifier = :cccNumber");
		query.setParameter("cccNumber", cccNumber);
		query.setParameter("cccIdentifierTypeUuid", CCC_NUMBER_IDENTIFIER_TYPE_UUID);
		query.setResultTransformer(Transformers.aliasToBean(Patient.class));
		return query.list();
	}
	
	public List<Patient> getCccPatientsWithUpdatedMedicalRecordsSinceDate(Date lastSyncDate) {
		
		SQLQuery query = getSession().createSQLQuery(
		    "SELECT patient.patient_id FROM patient "
		            + "INNER JOIN patient_identifier ON patient.patient_id = patient_identifier.patient_id "
		            + "INNER JOIN obs ON obs.person_id = patient.patient_id "
		            + "WHERE patient_identifier.identifier_type IN ( "
		            + "SELECT patient_identifier_type_id FROM patient_identifier_type WHERE uuid = :cccIdentifierTypeUuid) "
		            + "AND patient.voided=0 " + "AND obs.voided=0 " + "AND obs.concept_id IN (:conceptIds) "
		            + "AND obs.date_created >=:formattedLastSyncDate");
		query.setParameter("cccIdentifierTypeUuid", CCC_NUMBER_IDENTIFIER_TYPE_UUID);
		query.setParameterList("conceptIds", getMedicalRecordConceptsList());
		
		String formattedLastSyncDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lastSyncDate);
		query.setParameter("formattedLastSyncDate", formattedLastSyncDate);
		
		query.setResultTransformer(Transformers.aliasToBean(Patient.class));
		return query.list();
	}
	
	public List<Obs> getUpdatedVitalSignsSinceDate(Patient patient, Date lastSyncDate) {
		Criteria criteria = getSession().createCriteria(Obs.class);
		criteria.add(Restrictions.eq("person_id", patient.getPatientId()));
		criteria.add(Restrictions.ge("date_created", lastSyncDate));
		criteria.add(Restrictions.in("concept_id", getVitalSignsConceptsList()));
		return criteria.list();
	}
	
	public List<MyCareHubAllergy> getUpdatedAllergiesSinceDate(Patient patient, Date lastSyncDate) {
		SQLQuery obsGroupsQuery = getSession().createSQLQuery(
		    "SELECT obs_group_id FROM obs "
		            + "WHERE concept_id = :allergenConcept AND date_created >= :formattedLastSyncDate "
		            + "AND voided = 0 AND person_id = :patientId");
		
		SQLQuery allergenQuery = getSession()
		        .createSQLQuery(
		            "SELECT allergyName,allergyConceptId,reaction,severity,allergyDateTime FROM ( "
		                    + "SELECT "
		                    + "(SELECT concept_name.name FROM concept_name JOIN obs ON obs.value_coded = concept_name.concept_id "
		                    + "WHERE obs.concept_id= :allergenConcept AND obs.obs_group_id = obsgroups.obs_group_id  AND obs.voided=0 AND concept_name.locale='en' LIMIT 1) AS allergyName,"
		                    + "(SELECT value_coded FROM obs "
		                    + "WHERE obs.concept_id= :allergenConcept AND obs.obs_group_id = obsgroups.obs_group_id  AND obs.voided=0 AND LIMIT 1) AS allergyConceptId,"
		                    + "(SELECT concept_name.name FROM concept_name JOIN obs ON obs.value_coded = concept_name.concept_id "
		                    + "WHERE obs.concept_id= :reactionConcept AND obs.obs_group_id = obsgroups.obs_group_id  AND obs.voided=0 AND concept_name.locale='en' LIMIT 1) AS reaction,"
		                    + "(SELECT obs.value_coded FROM obs "
		                    + "WHERE obs.concept_id= :reactionConcept AND obs.obs_group_id = obsgroups.obs_group_id  AND obs.voided=0 AND LIMIT 1) AS reactionConceptId,"
		                    + "(SELECT value_text FROM obs WHERE obs.concept_id=:otherReactionConcept AND obs.obs_group_id = obsgroups.obs_group_id  AND obs.voided=0 LIMIT 1) AS otherReaction "
		                    + "(SELECT concept_name.name FROM concept_name JOIN obs ON obs.value_coded = concept_name.concept_id "
		                    + "WHERE obs.concept_id=:severityConcept AND obs.obs_group_id = obsgroups.obs_group_id  AND obs.voided=0 AND concept_name.locale='en' LIMIT 1 ) AS severity,"
		                    + "(SELECT obs.value_coded FROM obs WHERE obs.concept_id=:severityConcept AND obs.obs_group_id = obsgroups.obs_group_id  AND obs.voided=0 LIMIT 1 ) AS severityConceptId,"
		                    + "(SELECT value_datetime FROM obs WHERE obs.concept_id=:allergyDateConcept AND obs.obs_group_id = obsgroups.obs_group_id  AND obs.voided=0 LIMIT 1) AS allergyDateTime "
		                    + "FROM ("
		                    + "SELECT obs_group_id FROM obs WHERE concept_id = :allergenConcept AND date_created >= :formattedLastSyncDate AND voided = 0 AND person_id = :patientId"
		                    + ") AS obsgroups" + ") AS allergies ");
		
		obsGroupsQuery.setParameter("allergenConcept", ALLERGEN);
		obsGroupsQuery.setParameter("reactionConcept", ALLERGY_REACTION);
		obsGroupsQuery.setParameter("otherReactionConcept", ALLERGY_OTHER_REACTION);
		obsGroupsQuery.setParameter("severityConcept", ALLERGY_SEVERITY);
		obsGroupsQuery.setParameter("allergyDateConcept", ALLERGY_DATE);
		obsGroupsQuery.setParameter("patientId", patient.getPatientId());
		
		String formattedLastSyncDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lastSyncDate);
		obsGroupsQuery.setParameter("formattedLastSyncDate", formattedLastSyncDate);
		allergenQuery.setResultTransformer(Transformers.aliasToBean(MyCareHubAllergy.class));
		
		return allergenQuery.list();
	}
	
	public List<Obs> getUpdatedTestsSinceDate(Patient patient, Date lastSyncDate) {
		SQLQuery testsConcepts = getSession().createSQLQuery(
		    "SELECT concept_id FROM concept WHERE class_id = 1 AND voided=0");
		
		Criteria criteria = getSession().createCriteria(Obs.class);
		criteria.add(Restrictions.eq("person_id", patient.getPatientId()));
		criteria.add(Restrictions.ge("date_created", lastSyncDate));
		criteria.add(Restrictions.in("concept_id", testsConcepts.list()));
		criteria.add(Restrictions.eq("voided", false));
		return criteria.list();
	}
	
	public List<Obs> getUpdatedTestOrdersSinceDate(Patient patient, Date lastSyncDate) {
		Criteria criteria = getSession().createCriteria(Obs.class);
		criteria.add(Restrictions.eq("person_id", patient.getPatientId()));
		criteria.add(Restrictions.ge("date_created", lastSyncDate));
		criteria.add(Restrictions.eq("concept_id", TESTS_ORDERED));
		criteria.add(Restrictions.eq("voided", false));
		return criteria.list();
	}
	
	public List<Obs> getUpdatedMedicationsSinceDate(Patient patient, Date lastSyncDate) {
		SQLQuery drugsConcepts = getSession().createSQLQuery(
		    "SELECT concept_id FROM concept "
		            + "WHERE (class_id = 3 OR concept_id In (SELECT concept_id FROM drug)) AND voided=0");
		
		Criteria criteria = getSession().createCriteria(Obs.class);
		criteria.add(Restrictions.eq("person_id", patient.getPatientId()));
		criteria.add(Restrictions.ge("date_created", lastSyncDate));
		criteria.add(Restrictions.in("value_coded", drugsConcepts.list()));
		criteria.add(Restrictions.eq("voided", false));
		return criteria.list();
	}
}
