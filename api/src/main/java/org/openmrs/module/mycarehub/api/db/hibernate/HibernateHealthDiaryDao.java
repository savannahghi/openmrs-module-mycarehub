package org.openmrs.module.mycarehub.api.db.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.mycarehub.api.db.HealthDiaryDao;
import org.openmrs.module.mycarehub.model.HealthDiary;

import java.util.List;

public class HibernateHealthDiaryDao implements HealthDiaryDao {
	
	private DbSessionFactory sessionFactory;
	
	public HibernateHealthDiaryDao(DbSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public List<HealthDiary> saveHealthDiaries(List<HealthDiary> healthDiary) {
		session().saveOrUpdate(healthDiary);
		return healthDiary;
	}
	
	@Override
	public Number countHealthDiaries() {
		Criteria criteria = session().createCriteria(HealthDiary.class);
		criteria.setProjection(Projections.rowCount());
		return (Number) criteria.uniqueResult();
	}
	
	@Override
	public List<HealthDiary> getPagedHealthDiaries(Integer pageNumber, Integer pageSize) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(HealthDiary.class);
		if (pageNumber != null && pageNumber > 0) {
			criteria.setFirstResult((pageNumber - 1) * pageSize);
		}
		if (pageSize != null) {
			criteria.setMaxResults(pageSize);
		}
		criteria.add(Restrictions.eq("voided", Boolean.FALSE));
		return (List<HealthDiary>) criteria.list();
	}
	
	private DbSession session() {
		return sessionFactory.getCurrentSession();
	}
}
