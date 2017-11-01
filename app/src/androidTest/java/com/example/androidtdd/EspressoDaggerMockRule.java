package com.example.androidtdd;

import android.support.test.InstrumentationRegistry;

import com.example.androidtdd.di.component.ApplicationComponent;
import com.example.androidtdd.di.module.ApplicationModule;

import it.cosenonjaviste.daggermock.DaggerMockRule;

/**
 * Created by quanlt on 23/01/2017.
 */

public class EspressoDaggerMockRule extends DaggerMockRule<ApplicationComponent> {
    public EspressoDaggerMockRule() {
        super(ApplicationComponent.class, new ApplicationModule(getApp()));
        set(applicationComponent -> getApp().setApplicationComponent(applicationComponent));
    }

    public static PlaceHolderApplication getApp() {
        return (PlaceHolderApplication) InstrumentationRegistry
                .getInstrumentation()
                .getTargetContext()
                .getApplicationContext();
    }
}
