package org.openmrs.module.mycarehub.api.db.hibernate;

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
import org.openmrs.module.mycarehub.api.db.HealthDiaryDao;
import org.openmrs.module.mycarehub.model.HealthDiary;

public class HibernateHealthDiaryDao implements HealthDiaryDao {
	
	private DbSessionFactory sessionFactory;
	
	public HibernateHealthDiaryDao(DbSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public List<HealthDiary> saveHealthDiaries(List<HealthDiary> healthDiary) {
		for (HealthDiary healthDiary1 : healthDiary) {
			session().saveOrUpdate(healthDiary1);
		}
		return healthDiary;
	}
	
	@Override
	public Number countHealthDiaries(String searchString) {
		Criteria criteria = session().createCriteria(HealthDiary.class);
		if (StringUtils.isNotEmpty(searchString)) {
			Disjunction disjunction = Restrictions.disjunction();
			disjunction.add(Restrictions.ilike("mood", searchString, MatchMode.ANYWHERE));
			disjunction.add(Restrictions.ilike("cccNumber", searchString, MatchMode.ANYWHERE));
			disjunction.add(Restrictions.ilike("note", searchString, MatchMode.ANYWHERE));
			disjunction.add(Restrictions.ilike("entryType", searchString, MatchMode.ANYWHERE));
			criteria.add(disjunction);
		}
		criteria.setProjection(Projections.rowCount());
		return (Number) criteria.uniqueResult();
	}
	
	@Override
	public List<HealthDiary> getPagedHealthDiaries(String searchString, Integer pageNumber, Integer pageSize) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(HealthDiary.class);
		if (StringUtils.isNotEmpty(searchString)) {
			Disjunction disjunction = Restrictions.disjunction();
			disjunction.add(Restrictions.ilike("mood", searchString, MatchMode.ANYWHERE));
			disjunction.add(Restrictions.ilike("cccNumber", searchString, MatchMode.ANYWHERE));
			disjunction.add(Restrictions.ilike("note", searchString, MatchMode.ANYWHERE));
			disjunction.add(Restrictions.ilike("entryType", searchString, MatchMode.ANYWHERE));
			criteria.add(disjunction);
		}
		if (pageNumber != null && pageNumber > 0) {
			criteria.setFirstResult((pageNumber - 1) * pageSize);
		}
		if (pageSize != null) {
			criteria.setMaxResults(pageSize);
		}
		criteria.add(Restrictions.eq("voided", Boolean.FALSE));
		criteria.addOrder(Order.desc("dateRecorded"));
		List<HealthDiary> healthDiaries = (List<HealthDiary>) criteria.list();
		return healthDiaries;
	}
	
	private DbSession session() {
		return sessionFactory.getCurrentSession();
	}
}
