package com.example.androidtdd.util;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.schedulers.Schedulers;

/**
 * Created by quanlt on 20/01/2017.
 */

public class RxSchedulerOverrideRule implements TestRule {

    private final RxJavaSchedulersHook mRxJavaSchedulersHook = new RxJavaSchedulersHook() {
        @Override
        public Scheduler getIOScheduler() {
            return Schedulers.immediate();
        }

        @Override
        public Scheduler getNewThreadScheduler() {
            return Schedulers.immediate();
        }
    };

    private final RxAndroidSchedulersHook mRxAndroidSchedulersHook = new RxAndroidSchedulersHook() {
        @Override
        public Scheduler getMainThreadScheduler() {
            return Schedulers.immediate();
        }
    };

    private void callResetViaReflectionIn(RxJavaPlugins rxJavaPlugins)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method method = rxJavaPlugins.getClass().getDeclaredMethod("reset");
        method.setAccessible(true);
        method.invoke(rxJavaPlugins);
    }

    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                RxAndroidPlugins.getInstance().reset();
                RxAndroidPlugins.getInstance().registerSchedulersHook(mRxAndroidSchedulersHook);
                callResetViaReflectionIn(RxJavaPlugins.getInstance());
                RxJavaPlugins.getInstance().registerSchedulersHook(mRxJavaSchedulersHook);

                base.evaluate();

                RxAndroidPlugins.getInstance().reset();
                callResetViaReflectionIn(RxJavaPlugins.getInstance());
            }
        };
    }
}