package org.openmrs.module.mycarehub.api.db.hibernate;

import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.mycarehub.api.db.MyCareHubRedFlagDao;
import org.openmrs.module.mycarehub.model.RedFlags;
import org.springframework.transaction.annotation.Transactional;

public class HibernateMyCareHubRedFlagDao implements MyCareHubRedFlagDao {
	
	private DbSessionFactory sessionFactory;
	
	public HibernateMyCareHubRedFlagDao(DbSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public List<RedFlags> getAllRedFlagRequests() {
		Criteria criteria = session().createCriteria(RedFlags.class);
		criteria.add(Restrictions.eq("voided", false));
		return criteria.list();
	}
	
	@Override
	public RedFlags getRedFlagByUuid(String uuid) {
		Criteria criteria = session().createCriteria(RedFlags.class);
		criteria.add(Restrictions.eq("uuid", uuid));
		return (RedFlags) criteria.uniqueResult();
	}
	
	@Override
	public List<RedFlags> getAllRedFlagRequestsByLastSyncDate(Date lastSyncDate) {
		Criteria criteria = session().createCriteria(RedFlags.class);
		criteria.add(Restrictions.eq("voided", false));
		criteria.add(Restrictions.or(Restrictions.ge("progressDate", lastSyncDate),
		    Restrictions.ge("dateResolved", lastSyncDate)));
		return criteria.list();
	}
	
	@Override
	public List<RedFlags> getPagedRedFlagsByRequestType(String searchString, String requestType, Integer pageNumber,
	        Integer pageSize) {
		Criteria criteria = session().createCriteria(RedFlags.class);
		criteria.add(Restrictions.eq("voided", false));
		if (StringUtils.isNotEmpty(searchString)) {
			Disjunction disjunction = Restrictions.disjunction();
			disjunction.add(Restrictions.ilike("clientName", searchString, MatchMode.ANYWHERE));
			disjunction.add(Restrictions.ilike("clientContact", searchString, MatchMode.ANYWHERE));
			disjunction.add(Restrictions.ilike("cccNumber", searchString, MatchMode.ANYWHERE));
			disjunction.add(Restrictions.ilike("status", searchString, MatchMode.ANYWHERE));
			disjunction.add(Restrictions.ilike("request", searchString, MatchMode.ANYWHERE));
			criteria.add(disjunction);
		}
		if (StringUtils.isNotEmpty(requestType)) {
			criteria.add(Restrictions.eq("requestType", requestType));
		}
		if (pageNumber != null && pageNumber > 0) {
			criteria.setFirstResult((pageNumber - 1) * pageSize);
		}
		if (pageSize != null) {
			criteria.setMaxResults(pageSize);
		}
		criteria.add(Restrictions.eq("voided", Boolean.FALSE));
		criteria.addOrder(Order.desc("dateCreated"));
		return (List<RedFlags>) criteria.list();
	}
	
	@Override
	@Transactional
	public List<RedFlags> saveRedFlagRequests(List<RedFlags> redFlags) {
		for (RedFlags redFlag : redFlags) {
			session().saveOrUpdate(redFlag);
		}
		return redFlags;
	}
	
	@Override
	@Transactional
	public RedFlags saveRedFlagRequests(RedFlags redFlag) {
		session().saveOrUpdate(redFlag);
		return redFlag;
	}
	
	@Override
	public RedFlags getRedFlagRequestByMycarehubId(String mycarehubId) {
		Criteria criteria = session().createCriteria(RedFlags.class);
		criteria.add(Restrictions.eq("mycarehubId", mycarehubId));
		return (RedFlags) criteria.uniqueResult();
	}
	
	@Override
	public Number countRedFlagsByType(String searchString, String requestType) {
		Criteria criteria = session().createCriteria(RedFlags.class);
		if (StringUtils.isNotEmpty(searchString)) {
			Disjunction disjunction = Restrictions.disjunction();
			disjunction.add(Restrictions.ilike("clientName", searchString, MatchMode.ANYWHERE));
			disjunction.add(Restrictions.ilike("clientContact", searchString, MatchMode.ANYWHERE));
			disjunction.add(Restrictions.ilike("cccNumber", searchString, MatchMode.ANYWHERE));
			disjunction.add(Restrictions.ilike("status", searchString, MatchMode.ANYWHERE));
			disjunction.add(Restrictions.ilike("request", searchString, MatchMode.ANYWHERE));
			criteria.add(disjunction);
		}
		if (StringUtils.isNotEmpty(requestType)) {
			criteria.add(Restrictions.eq("requestType", requestType));
		}
		criteria.setProjection(Projections.rowCount());
		return (Number) criteria.uniqueResult();
	}
	
	private DbSession session() {
		return sessionFactory.getCurrentSession();
	}
}
