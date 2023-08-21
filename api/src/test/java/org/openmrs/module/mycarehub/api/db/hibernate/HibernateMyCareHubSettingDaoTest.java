package org.openmrs.module.mycarehub.api.db.hibernate;

import static org.junit.Assert.*;

import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.User;
import org.openmrs.module.mycarehub.api.db.MyCareHubSettingsDao;
import org.openmrs.module.mycarehub.model.MyCareHubSetting;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

public class HibernateMyCareHubSettingDaoTest extends BaseModuleContextSensitiveTest {

  @Override
  public Boolean useInMemoryDatabase() {
    // Defaults to true. If you want to use your own database specified in your runtime properties,
    // return false instead.
    return super.useInMemoryDatabase();
  }

  @Autowired private MyCareHubSettingsDao myCareHubSettingsDao;

  @Before
  public void setup() throws Exception {}

  @Test
  public void getMyCareHubSettingByType() {
    MyCareHubSetting setting = new MyCareHubSetting();
    setting.setSettingType("TEST SETTING TYPE");
    setting.setLastSyncTime(new Date());
    setting.setCreator(userFactory());

    // Save setting type
    MyCareHubSetting myCareHubSetting = myCareHubSettingsDao.saveOrUpdateMyCareHubSetting(setting);
    assertNotNull(myCareHubSetting);
    assertEquals(setting, myCareHubSetting);

    MyCareHubSetting retrievedSetting =
        myCareHubSettingsDao.getMyCareHubSettingByType(setting.getSettingType());
    assertEquals(myCareHubSetting, retrievedSetting);
  }

  @Test
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
