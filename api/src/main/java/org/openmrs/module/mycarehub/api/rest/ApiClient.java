package org.openmrs.module.mycarehub.api.rest;

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
	
	/**
	 * check if the retrofit instance is null and call initializeRetrofit() to initialize it if
	 * needed. This ensures that retrofit is configured only once, and subsequent calls to
	 * getRestService() will reuse the same instance.
	 */
	public static RestApiService getRestService() {
		if (retrofit == null) {
			initializeRetrofit();
		}
		
		if (retrofit != null) {
			try {
				return retrofit.create(RestApiService.class);
			}
			catch (Exception ex) {
				log.error(TAG, ex);
			}
		}
		
		return null;
	}
	
	// Separate setup and initialization of retrofit
	private static void initializeRetrofit() {
		GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create();
		String apiUrl = MyCareHubUtil.getApiUrl();
		
		if (isUrlValid(apiUrl)) {
			HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
			loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
			
			OkHttpClient client = new OkHttpClient.Builder().addInterceptor(loggingInterceptor).build();
			
			retrofit = new Retrofit.Builder().baseUrl(apiUrl).client(client).addConverterFactory(gsonConverterFactory)
			        .build();
		} else {
			log.error("Malformed URL: " + apiUrl);
		}
	}
	
	// Encapsulate url validation
	private static boolean isUrlValid(String url) {
		UrlValidator urlValidator = new UrlValidator();
		return urlValidator.isValid(url);
	}
}
