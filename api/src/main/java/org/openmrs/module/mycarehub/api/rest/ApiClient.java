package org.openmrs.module.mycarehub.api.rest;

import static org.openmrs.module.mycarehub.utils.Constants.YEAR_MONTH_DAY_PATTERN;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.routines.UrlValidator;
import org.openmrs.module.mycarehub.utils.MyCareHubUtil;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

  private static final String TAG = ApiClient.class.getSimpleName();

  private static final Log log = LogFactory.getLog(ApiClient.class);

  private static Retrofit retrofit = null;

  private static final long timeoutDuration = 2;

  public static RestApiService getRestService() {
    if (retrofit == null) {
      Gson gson =
          new GsonBuilder()
              .setDateFormat(YEAR_MONTH_DAY_PATTERN)
              .create(); // TODO: lastSyncTime have @Mokaya
      String apiUrl = MyCareHubUtil.getApiUrl();

      if (new UrlValidator().isValid(apiUrl)) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client =
            new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(timeoutDuration, TimeUnit.MINUTES)
                .readTimeout(timeoutDuration, TimeUnit.MINUTES)
                .build();
        retrofit =
            new Retrofit.Builder()
                .baseUrl(apiUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
      } else log.error("You have a Malformed URL");
    }

    if (retrofit != null) {
      try {
        return retrofit.create(RestApiService.class);
      } catch (Exception ex) {
        log.error(TAG, ex.getCause());
      }
    }
    return null;
  }
}
