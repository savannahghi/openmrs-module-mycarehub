package org.openmrs.module.mycarehub.api.db.hibernate;

import static org.openmrs.module.mycarehub.api.db.hibernate.Common.GET_MYCAREHUB_CONSENTED_PATIENT;
import static org.openmrs.module.mycarehub.utils.Constants.APPOINTMENT_DATE_CONCEPT_ID;
import static org.openmrs.module.mycarehub.utils.Constants.APPOINTMENT_REASON_CONCEPT_ID;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Obs;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.mycarehub.api.db.AppointmentDao;
import org.openmrs.module.mycarehub.model.AppointmentRequests;
import org.springframework.transaction.annotation.Transactional;

public class HibernateAppointmentDao implements AppointmentDao {

  private DbSessionFactory sessionFactory;

  public HibernateAppointmentDao(DbSessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  private DbSession session() {
    return sessionFactory.getCurrentSession();
  }

  @Override
  public List<Obs> getAppointmentsByLastSyncDate(Date lastSyncDate) {
    if (lastSyncDate != null) {
      List<Integer> consentedPatientIds =
          sessionFactory.getCurrentSession().createSQLQuery(GET_MYCAREHUB_CONSENTED_PATIENT).list();

      if (!consentedPatientIds.isEmpty()) {
        Criteria criteria = session().createCriteria(Obs.class);
        criteria.add(
            Restrictions.or(
                Restrictions.eq("concept.conceptId", APPOINTMENT_DATE_CONCEPT_ID),
                Restrictions.eq("concept.conceptId", APPOINTMENT_REASON_CONCEPT_ID)));
        criteria.add(Restrictions.ge("dateCreated", lastSyncDate));
        criteria.add(Restrictions.eq("voided", false));
        criteria.add(Restrictions.in("personId", consentedPatientIds));
        return criteria.list();
      }
    }

    return new ArrayList<Obs>();
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
    criteria.add(
        Restrictions.or(
            Restrictions.ge("progressDate", lastSyncDate),
            Restrictions.ge("dateResolved", lastSyncDate)));
    return criteria.list();
  }

  @Override
  @Transactional
  public List<AppointmentRequests> saveAppointmentRequests(
      List<AppointmentRequests> appointmentRequests) {
    for (AppointmentRequests appointmentRequest : appointmentRequests) {
      session().saveOrUpdate(appointmentRequest);
    }

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
  public Number countAppointments(String searchString) {
    Criteria criteria = session().createCriteria(AppointmentRequests.class);
    if (StringUtils.isNotEmpty(searchString)) {
      Disjunction disjunction = Restrictions.disjunction();
      disjunction.add(Restrictions.ilike("clientName", searchString, MatchMode.ANYWHERE));
      disjunction.add(Restrictions.ilike("cccNumber", searchString, MatchMode.ANYWHERE));
      disjunction.add(Restrictions.ilike("appointmentReason", searchString, MatchMode.ANYWHERE));
      disjunction.add(Restrictions.ilike("status", searchString, MatchMode.ANYWHERE));
      disjunction.add(Restrictions.ilike("clientContact", searchString, MatchMode.ANYWHERE));
      criteria.add(disjunction);
    }
    criteria.setProjection(Projections.rowCount());
    return (Number) criteria.uniqueResult();
  }

  @Override
  public List<AppointmentRequests> getPagedAppointments(
      String searchString, Integer pageNumber, Integer pageSize) {
    Criteria criteria =
        sessionFactory.getCurrentSession().createCriteria(AppointmentRequests.class);
    if (StringUtils.isNotEmpty(searchString)) {
      Disjunction disjunction = Restrictions.disjunction();
      disjunction.add(Restrictions.ilike("clientName", searchString, MatchMode.ANYWHERE));
      disjunction.add(Restrictions.ilike("cccNumber", searchString, MatchMode.ANYWHERE));
      disjunction.add(Restrictions.ilike("appointmentReason", searchString, MatchMode.ANYWHERE));
      disjunction.add(Restrictions.ilike("status", searchString, MatchMode.ANYWHERE));
      disjunction.add(Restrictions.ilike("clientContact", searchString, MatchMode.ANYWHERE));
      criteria.add(disjunction);
    }

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
    criteria.add(Restrictions.eq("encounter.encounterId", encounterId));
    criteria.add(Restrictions.eq("concept.conceptId", conceptId));
    criteria.addOrder(Order.desc("obsId"));
    criteria.setMaxResults(1);
    return (Obs) criteria.uniqueResult();
  }
}
