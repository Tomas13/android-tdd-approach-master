package com.example.androidtdd.data.remote;

import com.example.androidtdd.data.model.Comment;
import com.example.androidtdd.data.model.Photo;
import com.example.androidtdd.data.model.User;
import com.example.androidtdd.util.Constants;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by quanlt on 19/01/2017.
 */

public interface ApiService {
    @GET("users")
    Observable<List<User>> getUsers();

    @GET("comments")
    Observable<List<Comment>> getComments(@Query("userId") int userId);

    @GET("photos?_limit=20")
    Observable<List<Photo>> getPhotos(@Query("userId") int userId);

    class Creator {

        public static Retrofit newRetrofitInstance() {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build();


            return new Retrofit.Builder()
                    .baseUrl(Constants.ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(client)
                    .build();

        }

    }
}
