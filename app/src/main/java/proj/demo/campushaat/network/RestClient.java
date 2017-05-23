package proj.demo.campushaat.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by stpl on 5/23/2017.
 */

public class RestClient {
    private static Retrofit retrofit;
    private static String baseUrl="s";
    static {
        retrofit=new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl).build();
    }

    public static <T> T getServiceAuth(Class<T> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
