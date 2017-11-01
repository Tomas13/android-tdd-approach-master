package com.example.androidtdd.di.module;

import android.app.Activity;
import android.content.Context;

import com.example.androidtdd.di.ActivityContext;

import dagger.Module;
import dagger.Provides;

/**
 * Created by quanlt on 19/01/2017.
 */
@Module
public class ActivityModule {
    private Activity mActivity;

    public ActivityModule(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @Provides
    public Activity provideActivity() {
        return mActivity;
    }

    @Provides
    @ActivityContext
    public Context provideActivityContext() {
        return mActivity;
    }
}
