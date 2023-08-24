package org.openmrs.module.mycarehub.api.db.hibernate;

import static org.junit.Assert.*;

import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.User;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.mycarehub.api.db.MyCareHubSettingsDao;
import org.openmrs.module.mycarehub.model.MyCareHubSetting;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

public class HibernateMyCareHubSettingDaoTest extends BaseModuleContextSensitiveTest {

  private MyCareHubSettingsDao myCareHubSettingsDao;

  @Autowired DbSessionFactory sessionFactory;

  @Override
  public Boolean useInMemoryDatabase() {
    // Defaults to true. If you want to use your own database specified in your runtime properties
    // return false instead, otherwise return super.useInMemoryDatabase();.
    return false;
  }

  @Before
  public void setup() {
    myCareHubSettingsDao = new HibernateMyCareHubSettingsDao(sessionFactory);
  }

  @Test
  public void getMyCareHubSettingByType() {
    String settingType = "TEST SETTING TYPE";

    MyCareHubSetting retrievedSetting = myCareHubSettingsDao.getMyCareHubSettingByType(settingType);
    assertEquals(settingType, retrievedSetting.getSettingType());
  }

  @Test
  @Rollback(value = false)
  public void createMyCareHubSetting() {
    MyCareHubSetting setting = new MyCareHubSetting();
    setting.setSettingType("TEST SETTING TYPE");
    setting.setLastSyncTime(new Date());
    setting.setCreator(userFactory());

    MyCareHubSetting myCareHubSetting = myCareHubSettingsDao.saveOrUpdateMyCareHubSetting(setting);
    assertEquals(setting, myCareHubSetting);
  }

  private static User userFactory() {
    User user = new User();
    user.setUserId(1);
    user.setId(1);

    return user;
  }
}
