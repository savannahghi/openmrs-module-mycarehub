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
			Criteria criteria = session().createCriteria(Appointment.class);
			criteria.add(Restrictions.or(Restrictions.ge("dateCreated", lastSyncDate),
			    Restrictions.ge("dateChanged", lastSyncDate)));
			criteria.add(Restrictions.eq("voided", false));
			return criteria.list();
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
