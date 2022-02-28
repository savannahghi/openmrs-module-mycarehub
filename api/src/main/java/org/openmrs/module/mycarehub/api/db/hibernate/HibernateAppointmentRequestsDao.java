package org.openmrs.module.mycarehub.api.db.hibernate;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.appointmentscheduling.Appointment;
import org.openmrs.module.mycarehub.api.db.AppointmentRequestsDao;
import org.openmrs.module.mycarehub.model.AppointmentRequests;

import java.util.Date;
import java.util.List;

public class HibernateAppointmentRequestsDao implements AppointmentRequestsDao {
	
	private DbSessionFactory sessionFactory;
	
	public HibernateAppointmentRequestsDao(DbSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public List<Appointment> getAppointmentsByLastSyncDate(Date lastSyncDate) {
		if (lastSyncDate != null) {
			String sql = "select aa.appointment_id, aa.uuid, ats.start_date, ats.enddate, aa.patient_id, aat.name  "
			        + "from appointmentscheduling_appointment aa "
			        + "left join appointmentscheduling_time_slot ats on ats.time_slot_id = aa.time_slot_id "
			        + "left join appointmentscheduling_appointment_type aat on aat.appointment_type_id = aa.appointment_type_id "
			        + "where date_created >= :lastSyncDate " + "or date_changed >= :lastSyncDate";
			SQLQuery myquery = session().createSQLQuery(sql);
			myquery.setParameter("lastSyncDate", lastSyncDate);
			return myquery.list();
		}
		return null;
	}
	
	@Override
	public List<AppointmentRequests> getAllAppointmentRequests() {
		Criteria criteria = session().createCriteria(AppointmentRequests.class);
		criteria.add(Restrictions.eq("retired", false));
		return criteria.list();
	}
	
	@Override
	public List<AppointmentRequests> saveAppointmentRequests(List<AppointmentRequests> appointmentRequests) {
		session().saveOrUpdate(appointmentRequests);
		return appointmentRequests;
	}
	
	private DbSession session() {
		return sessionFactory.getCurrentSession();
	}
}
