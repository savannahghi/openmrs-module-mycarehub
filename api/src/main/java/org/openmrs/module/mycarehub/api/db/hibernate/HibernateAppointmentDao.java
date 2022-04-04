package org.openmrs.module.mycarehub.api.db.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.mycarehub.api.db.AppointmentDao;
import org.openmrs.module.mycarehub.model.AppointmentRequests;
import org.openmrs.module.mycarehub.model.MyCareHubSetting;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.openmrs.module.mycarehub.utils.Constants.APPOINTMENT_DATE_CONCEPT_ID;
import static org.openmrs.module.mycarehub.utils.Constants.APPOINTMENT_REASON_CONCEPT_ID;

public class HibernateAppointmentDao implements AppointmentDao {
	
	private DbSessionFactory sessionFactory;
	
	public HibernateAppointmentDao(DbSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public List<Obs> getAppointmentsByLastSyncDate(Date lastSyncDate) {
		if (lastSyncDate != null) {
			ConceptService conceptService = Context.getConceptService();
			Concept appointmentDate = conceptService.getConcept(APPOINTMENT_DATE_CONCEPT_ID);
			Concept appointmentReason = conceptService.getConcept(APPOINTMENT_REASON_CONCEPT_ID);
			Criteria criteria = session().createCriteria(Obs.class);
			criteria.add(Restrictions.or(Restrictions.eq("concept", appointmentDate),
			    Restrictions.eq("concept", appointmentReason)));
			criteria.add(Restrictions.eq("dateCreated", lastSyncDate));
			criteria.add(Restrictions.eq("voided", false));
			return criteria.list();
		}
		return null;
	}
	
	@Override
	public List<AppointmentRequests> getAllAppointmentRequests() {
		Criteria criteria = session().createCriteria(AppointmentRequests.class);
		criteria.add(Restrictions.eq("voided", false));
		return criteria.list();
	}
	
	@Override
	public List<AppointmentRequests> getAllAppointmentRequestsByLastSyncDate(Date lastSyncDate) {
		Criteria criteria = session().createCriteria(AppointmentRequests.class);
		criteria.add(Restrictions.eq("voided", false));
		criteria.add(Restrictions.or(Restrictions.ge("progressDate", lastSyncDate),
		    Restrictions.ge("dateResolved", lastSyncDate)));
		return criteria.list();
	}
	
	@Override
	@Transactional
	public List<AppointmentRequests> saveAppointmentRequests(List<AppointmentRequests> appointmentRequests) {
		session().saveOrUpdate(appointmentRequests);
		return appointmentRequests;
	}
	
	@Override
	@Transactional
	public AppointmentRequests saveAppointmentRequests(AppointmentRequests appointmentRequest) {
		session().saveOrUpdate(appointmentRequest);
		return appointmentRequest;
	}
	
	@Override
	public AppointmentRequests getAppointmentRequestByMycarehubId(String mycarehubId) {
		Criteria criteria = session().createCriteria(AppointmentRequests.class);
		criteria.add(Restrictions.eq("mycarehubId", mycarehubId));
		return (AppointmentRequests) criteria.uniqueResult();
	}
	
	@Override
	public AppointmentRequests getAppointmentRequestByUuid(String uuid) {
		Criteria criteria = session().createCriteria(AppointmentRequests.class);
		criteria.add(Restrictions.eq("uuid", uuid));
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
		criteria.addOrder(Order.desc("dateCreated"));
		return (List<AppointmentRequests>) criteria.list();
	}
	
	@Override
	public Obs getObsByEncounterAndConcept(Integer encounterId, Integer conceptId) {
		Criteria criteria = session().createCriteria(Obs.class);
		criteria.add(Restrictions.eq("encounterId", encounterId));
		criteria.add(Restrictions.eq("conceptId", conceptId));
		criteria.addOrder(Order.desc("obsId"));
		criteria.setMaxResults(1);
		return (Obs) criteria.uniqueResult();
	}
	
	private DbSession session() {
		return sessionFactory.getCurrentSession();
	}
}
