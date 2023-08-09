package org.openmrs.module.mycarehub.api.service;


import java.util.Date;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.mycarehub.model.MyCareHubSetting;
import org.openmrs.test.BaseModuleContextSensitiveTest;

public class MyCareHubSettingServiceTest extends BaseModuleContextSensitiveTest {

  MyCareHubSettingsService settingsService;

  @Before
  public void setup() throws Exception {
    settingsService = Context.getService(MyCareHubSettingsService.class);
    initializeInMemoryDatabase();
    authenticate();
  }

  @Test
  public void createMyCareHubSetting_shouldCreateSettingOfMatchingTypeAndDate() {
    Date syncDate = new Date();
    String settingType = "dummySettingType";
    MyCareHubSetting myCareHubSetting =
        settingsService.createMyCareHubSetting(settingType, syncDate);
    Assert.assertEquals(syncDate, myCareHubSetting.getLastSyncTime());
    Assert.assertEquals(settingType, myCareHubSetting.getSettingType());
  }

  @Test
  public void getMyCareHubSettingByType_shouldGetLatestSettingBySyncTime() {
    String settingType = "dummySettingType";
    MyCareHubSetting myCareHubSetting1 =
        settingsService.createMyCareHubSetting(settingType, new Date(1000000));
    Assert.assertEquals(
        myCareHubSetting1, settingsService.getLatestMyCareHubSettingByType(settingType));

    MyCareHubSetting myCareHubSetting2 =
        settingsService.createMyCareHubSetting(settingType, new Date());
    Assert.assertEquals(
        myCareHubSetting2, settingsService.getLatestMyCareHubSettingByType(settingType));

    settingsService.createMyCareHubSetting(settingType, new Date(0));
    Assert.assertEquals(
        myCareHubSetting2, settingsService.getLatestMyCareHubSettingByType(settingType));
  }
}
