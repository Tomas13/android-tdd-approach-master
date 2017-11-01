package com.example.androidtdd.di.component;

import android.app.Application;
import android.content.Context;

import com.example.androidtdd.data.PlaceHolderRepository;
import com.example.androidtdd.di.ApplicationContext;
import com.example.androidtdd.di.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * Created by quanlt on 19/01/2017.
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    @ApplicationContext
    Context context();

    Application application();

    PlaceHolderRepository innovatubeRepository();
}
