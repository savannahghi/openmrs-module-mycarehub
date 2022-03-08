package org.openmrs.module.mycarehub.api.db.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.appointmentscheduling.Appointment;
import org.openmrs.module.mycarehub.api.db.AppointmentDao;
import org.openmrs.module.mycarehub.model.AppointmentRequests;

import java.util.Date;
import java.util.List;

public class HibernateAppointmentDao implements AppointmentDao {
	
	private DbSessionFactory sessionFactory;
	
	public HibernateAppointmentDao(DbSessionFactory sessionFactory) {
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
	public List<AppointmentRequests> getAllAppointmentRequestsByLastSyncDate(Date lastSyncDate) {
		Criteria criteria = session().createCriteria(AppointmentRequests.class);
		criteria.add(Restrictions.eq("retired", false));
		criteria.add(Restrictions.or(Restrictions.ge("progressDate", lastSyncDate),
		    Restrictions.ge("dateResolved", lastSyncDate)));
		return criteria.list();
	}
	
	@Override
	public List<AppointmentRequests> saveAppointmentRequests(List<AppointmentRequests> appointmentRequests) {
		session().saveOrUpdate(appointmentRequests);
		return appointmentRequests;
	}
	
	@Override
	public AppointmentRequests getAppointmentRequestByMycarehubId(String mycarehubId) {
		Criteria criteria = session().createCriteria(AppointmentRequests.class);
		criteria.add(Restrictions.eq("mycarehubId", mycarehubId));
		return (AppointmentRequests) criteria.uniqueResult();
	}
	
	@Override
	public Number countAppointments() {
		Criteria criteria = session().createCriteria(AppointmentRequests.class);
		criteria.setProjection(Projections.rowCount());
		return (Number) criteria.uniqueResult();
	}
	
	@Override
	public List<AppointmentRequests> getPagedAppointments(Integer pageNumber, Integer pageSize) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(AppointmentRequests.class);
		if (pageNumber != null && pageNumber > 0) {
			criteria.setFirstResult((pageNumber - 1) * pageSize);
		}
		if (pageSize != null) {
			criteria.setMaxResults(pageSize);
		}
		criteria.add(Restrictions.eq("voided", Boolean.FALSE));
		return (List<AppointmentRequests>) criteria.list();
	}
	
	private DbSession session() {
		return sessionFactory.getCurrentSession();
	}
}
