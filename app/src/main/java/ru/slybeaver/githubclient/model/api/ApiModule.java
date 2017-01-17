package ru.slybeaver.githubclient.model.api;

import okhttp3.OkHttpClient;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by psinetron on 10.01.2017.
 * http://slybeaver.ru
 * slybeaver@slybeaver.ru
 */
public class ApiModule {

    private ApiModule() {
    }

    public static ApiInterface getApiInterface(String url) {
        OkHttpClient httpClient = new OkHttpClient();
        Retrofit.Builder builder = new Retrofit.Builder().
                baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        builder.client(httpClient);
        return builder.build().create(ApiInterface.class);
    }

}
