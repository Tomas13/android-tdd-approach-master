package com.example.androidtdd.ui.photo;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidtdd.R;
import com.example.androidtdd.data.model.Photo;
import com.example.androidtdd.data.model.User;
import com.example.androidtdd.ui.base.BaseActivity;
import com.example.androidtdd.ui.detail.DetailActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoActivity extends BaseActivity implements PhotoMvpView {

    @BindView(R.id.recycler_photos)
    RecyclerView mPhotosRecycler;
    @BindView(R.id.text_loading)
    TextView mLoadingTextView;
    @BindView(R.id.text_empty)
    TextView mEmptyTextView;
    @Inject
    PhotoPresenter mPhotoPresenter;

    private PhotoAdapter mPhotoAdapter;
    private User mSelectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);
        mSelectedUser = getIntent().getParcelableExtra(DetailActivity.ARG_USER);
        getActivityComponent().inject(this);
        mPhotoPresenter.attachView(this);
        mPhotoPresenter.getPhotos(mSelectedUser);
        mPhotoAdapter = new PhotoAdapter();
        mPhotosRecycler.setAdapter(mPhotoAdapter);
        RecyclerView.LayoutManager layoutManager =
                new GridLayoutManager(this, getResources().getInteger(R.integer.photo_count));
        mPhotosRecycler.setLayoutManager(layoutManager);
    }

    @Override
    public void showPhotos(List<Photo> photos) {
        mPhotosRecycler.setVisibility(View.VISIBLE);
        mEmptyTextView.setVisibility(View.GONE);
        mPhotoAdapter.setPhotos(photos);

    }

    @Override
    public void showLoading() {
        mLoadingTextView.setVisibility(View.VISIBLE);
        mPhotosRecycler.setVisibility(View.GONE);
        mEmptyTextView.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        mLoadingTextView.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEmpty() {
        mEmptyTextView.setVisibility(View.VISIBLE);
    }
}
