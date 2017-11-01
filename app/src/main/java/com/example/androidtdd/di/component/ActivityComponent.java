package com.example.androidtdd.di.component;

import com.example.androidtdd.di.PerActivity;
import com.example.androidtdd.di.module.ActivityModule;
import com.example.androidtdd.ui.comment.CommentActivity;
import com.example.androidtdd.ui.detail.DetailActivity;
import com.example.androidtdd.ui.home.HomeActivity;
import com.example.androidtdd.ui.photo.PhotoActivity;

import dagger.Component;

/**
 * Created by quanlt on 19/01/2017.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(HomeActivity homeActivity);

    void inject(DetailActivity detailActivity);

    void inject(CommentActivity commentActivity);

    void inject(PhotoActivity photoActivity);
}
