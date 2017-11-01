package com.example.androidtdd.di.module;


import android.app.Application;
import android.content.Context;

import com.example.androidtdd.data.PlaceHolderRepository;
import com.example.androidtdd.data.PlaceHolderRepositoryImpl;
import com.example.androidtdd.data.local.LocalDataSource;
import com.example.androidtdd.data.local.LocalDataSourceImpl;
import com.example.androidtdd.data.remote.ApiService;
import com.example.androidtdd.data.remote.RemoteDataSource;
import com.example.androidtdd.data.remote.RemoteDataSourceImpl;
import com.example.androidtdd.di.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import retrofit2.Retrofit;

/**
 * Created by quanlt on 19/01/2017.
 */
@Module
public class ApplicationModule {
    protected final Application mApplication;

    public ApplicationModule(Application mApplication) {
        this.mApplication = mApplication;
    }

    @Provides
    @Singleton
    public Application provideApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    @ApplicationContext
    public Context provideApplicationContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit() {
        return ApiService.Creator.newRetrofitInstance();
    }

    @Provides
    public Realm provideRealm() {
        return Realm.getDefaultInstance();
    }

    @Provides
    public LocalDataSource provideLocalDataSource() {
        return new LocalDataSourceImpl();
    }

    @Provides
    @Singleton
    public ApiService provideInnovatubeService(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }

    @Provides
    @Singleton
    public RemoteDataSource provideRemoteDataSource(ApiService innovatubeService) {
        return new RemoteDataSourceImpl(innovatubeService);
    }

    @Provides
    @Singleton
    public PlaceHolderRepository provideInnovatubeRepository(LocalDataSource localDataSource,
                                                             RemoteDataSource remoteDataSource) {
        return new PlaceHolderRepositoryImpl(localDataSource, remoteDataSource);
    }
}
