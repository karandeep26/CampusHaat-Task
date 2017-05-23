package proj.demo.campushaat.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by stpl on 5/23/2017.
 */

public class RestClient {
    private static Retrofit retrofit;
    private static String baseUrl="http://ec2-35-154-15-217.ap-south-1.compute.amazonaws.com:8080/campushaatTestAPI/webapi/users/";
    static {
        OkHttpClient clientAuth = new OkHttpClient.Builder()
                .addInterceptor(
                        chain -> {
                            Request request = chain.request().newBuilder()
                                    .addHeader("Content-Type", "application/json").build();
                            return chain.proceed(request);
                        })
                .build();
        retrofit=new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).client(clientAuth)
                .baseUrl(baseUrl).build();
    }

    public static <T> T getServiceAuth(Class<T> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
