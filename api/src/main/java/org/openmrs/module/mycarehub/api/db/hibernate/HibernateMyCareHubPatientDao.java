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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.openmrs.module.mycarehub.utils.Constants.CCC_NUMBER_IDENTIFIER_TYPE_UUID;
import static org.openmrs.module.mycarehub.utils.Constants._PersonAttributeType.NEXT_OF_KIN_CONTACT;
import static org.openmrs.module.mycarehub.utils.Constants._PersonAttributeType.NEXT_OF_KIN_NAME;
import static org.openmrs.module.mycarehub.utils.Constants._PersonAttributeType.NEXT_OF_KIN_RELATIONSHIP;
import static org.openmrs.module.mycarehub.utils.Constants._PersonAttributeType.TELEPHONE_CONTACT;
import static org.openmrs.module.mycarehub.utils.MyCareHubUtil.getMedicalRecordConceptsList;
import static org.openmrs.module.mycarehub.utils.MyCareHubUtil.getMyCareHubObservationsConceptsList;

public class HibernateMyCareHubPatientDao implements MyCareHubPatientDao {
	
	private DbSessionFactory sessionFactory;
	
	public HibernateMyCareHubPatientDao(DbSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private DbSession getSession(){
		return sessionFactory.getCurrentSession();
	}
	@Override
	public List<Patient> getCccPatientsCreatedOrUpdatedSinceDate(Date lastSyncDate) {
		String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lastSyncDate);
		String personAttributeTypeUuids = new StringBuilder().append('"').append(TELEPHONE_CONTACT).append("\",\"")
		        .append(NEXT_OF_KIN_NAME).append("\",\"").append(NEXT_OF_KIN_CONTACT).append("\",\"")
		        .append(NEXT_OF_KIN_RELATIONSHIP).append('"').toString();
		Query query = sessionFactory
		        .getCurrentSession()
		        .createSQLQuery(
		            "SELECT DISTINCT patient.patient_id FROM patient "
		                    + "INNER JOIN person ON patient.patient_id = person.person_id "
		                    + "INNER JOIN person_name ON person_name.person_id = patient.patient_id "
		                    + "INNER JOIN patient_identifier ON patient.patient_id = patient_identifier.patient_id "
		                    + "WHERE (" + "patient.date_created >='"
		                    + formattedDate
		                    + "' "
		                    + "OR person.date_changed >='"
		                    + formattedDate
		                    + "' "
		                    + "OR person_name.date_changed >='"
		                    + formattedDate
		                    + "' "
		                    + "OR patient.patient_id in ("
		                    + "SELECT person_id FROM person_attribute "
		                    + "INNER JOIN person_attribute_type ON person_attribute.person_attribute_type_id = person_attribute_type.person_attribute_type_id "
		                    + "WHERE ("
		                    + "person_attribute.date_created >='"
		                    + formattedDate
		                    + "' "
		                    + "OR person_attribute.date_changed >='"
		                    + formattedDate
		                    + "'"
		                    + ") AND person_attribute_type.uuid IN ("
		                    + personAttributeTypeUuids
		                    + ") "
		                    + ")"
		                    + ") "
		                    + "AND patient_identifier.identifier_type IN ("
		                    + "SELECT patient_identifier_type_id FROM patient_identifier_type "
		                    + "WHERE uuid = '"
		                    + CCC_NUMBER_IDENTIFIER_TYPE_UUID + "'" + ")" + "AND patient.voided=0");
		query.setResultTransformer(Transformers.aliasToBean(Patient.class));
		return query.list();
	}
	
	public List<Patient> getCccPatientsByIdentifier(String cccNumber) {
		Query query = sessionFactory.getCurrentSession().createSQLQuery(
		    "SELECT patient_id FROM patient "
		            + "INNER JOIN patient_identifier ON patient.patient_id = patient_identifier.patient_id "
		            + "WHERE patient_identifier.identifier_type IN ("
		            + "SELECT patient_identifier_type_id FROM patient_identifier_type " + "WHERE uuid = '"
		            + CCC_NUMBER_IDENTIFIER_TYPE_UUID + "'" + ") AND patient_identifier.identifier = '" + cccNumber + "'");
		query.setResultTransformer(Transformers.aliasToBean(Patient.class));
		return query.list();
	}
	
	public List<Patient> getCccPatientsWithUpdatedMedicalRecordsSinceDate(Date lastSyncDate) {

		SQLQuery query = getSession().createSQLQuery("SELECT patient.patient_id FROM patient " +
				"INNER JOIN patient_identifier ON patient.patient_id = patient_identifier.patient_id " +
				"INNER JOIN obs ON obs.person_id = patient.patient_id " +
				"WHERE patient_identifier.identifier_type IN ( " +
				"SELECT patient_identifier_type_id FROM patient_identifier_type WHERE uuid = :cccIdentifierTypeUuid) " +
				"AND patient.voided=0 " +
				"AND obs.voided=0 " +
				"AND obs.concept_id IN (:conceptIds) " +
				"AND obs.date_created >=:formattedLastSyncDate");
		query.setParameter("cccIdentifierTypeUuid",CCC_NUMBER_IDENTIFIER_TYPE_UUID);
		query.setParameterList("conceptIds",getMedicalRecordConceptsList());

		String formattedLastSyncDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lastSyncDate);
		query.setParameter("formattedLastSyncDate",formattedLastSyncDate);

		query.setResultTransformer(Transformers.aliasToBean(Patient.class));
		return query.list();
	}

	public List<Obs> getUpdatedVitalSignsSinceDate(Patient patient, Date lastSyncDate){
		Criteria criteria = getSession().createCriteria(Obs.class);
		criteria.add(Restrictions.eq("person_id",patient.getPatientId()));
		criteria.add(Restrictions.ge("date_created",lastSyncDate));
		criteria.add(Restrictions.in("concept_id",getMyCareHubObservationsConceptsList()));
		return criteria.list();
	}
	public List<Obs> getUpdatedAllergiesSinceDate(Patient patient, Date lastSyncDate){

	}
}
