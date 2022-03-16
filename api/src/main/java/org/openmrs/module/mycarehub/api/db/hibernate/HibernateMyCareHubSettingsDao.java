package org.openmrs.module.mycarehub.api.db.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.mycarehub.api.db.MyCareHubSettingsDao;
import org.openmrs.module.mycarehub.model.MyCareHubSetting;

public class HibernateMyCareHubSettingsDao implements MyCareHubSettingsDao {
	
	private DbSessionFactory sessionFactory;
	
	protected Class mappedClass = MyCareHubSetting.class;
	
	public HibernateMyCareHubSettingsDao(DbSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public MyCareHubSetting getMyCareHubSettingByType(String settingType) {
		Criteria criteria = session().createCriteria(mappedClass);
		criteria.add(Restrictions.eq("settingType", settingType));
		criteria.add(Restrictions.eq("voided", Boolean.FALSE));
		criteria.addOrder(Order.desc("lastSyncTime"));
		criteria.setMaxResults(1);
		return (MyCareHubSetting) criteria.uniqueResult();
	}
	
	@Override
	public MyCareHubSetting saveOrUpdateMyCareHubSetting(MyCareHubSetting myCareHubSetting) {
		session().saveOrUpdate(myCareHubSetting);
		return myCareHubSetting;
	}
	
	private DbSession session() {
		return sessionFactory.getCurrentSession();
	}
}
