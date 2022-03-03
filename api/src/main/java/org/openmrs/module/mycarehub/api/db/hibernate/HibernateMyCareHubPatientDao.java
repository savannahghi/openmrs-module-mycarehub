package org.openmrs.module.mycarehub.api.db.hibernate;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.openmrs.Patient;
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

public class HibernateMyCareHubPatientDao implements MyCareHubPatientDao {
	
	private DbSessionFactory sessionFactory;
	
	public HibernateMyCareHubPatientDao(DbSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
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
		return null;
	}
}
