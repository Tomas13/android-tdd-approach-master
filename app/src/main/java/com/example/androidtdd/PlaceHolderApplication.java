package com.example.androidtdd;

import android.app.Application;
import android.content.Context;
import android.support.annotation.VisibleForTesting;

import com.example.androidtdd.di.component.ApplicationComponent;
import com.example.androidtdd.di.component.DaggerApplicationComponent;
import com.example.androidtdd.di.module.ApplicationModule;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by quanlt on 20/01/2017.
 */

public class PlaceHolderApplication extends Application {
    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    public static PlaceHolderApplication get(Context context) {
        return (PlaceHolderApplication) context.getApplicationContext();
    }

    public synchronized ApplicationComponent getComponent() {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return mApplicationComponent;

    }

    @VisibleForTesting
    public void setApplicationComponent(ApplicationComponent mApplicationComponent) {
        this.mApplicationComponent = mApplicationComponent;
    }
}
