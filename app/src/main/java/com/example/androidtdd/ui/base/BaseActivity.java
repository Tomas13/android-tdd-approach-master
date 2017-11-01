package com.example.androidtdd.ui.base;

import android.support.v7.app.AppCompatActivity;

import com.example.androidtdd.PlaceHolderApplication;
import com.example.androidtdd.di.component.ActivityComponent;
import com.example.androidtdd.di.component.DaggerActivityComponent;


/**
 * Created by quanlt on 19/01/2017.
 */

public class BaseActivity extends AppCompatActivity {
    private ActivityComponent mActivityComponent;

    public ActivityComponent getActivityComponent() {
        if (mActivityComponent == null) {
            mActivityComponent = DaggerActivityComponent.builder()
                    .applicationComponent(PlaceHolderApplication.get(this).getComponent())
                    .build();
        }
        return mActivityComponent;
    }
}
