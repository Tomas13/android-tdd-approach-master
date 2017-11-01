package com.example.androidtdd.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidtdd.R;
import com.example.androidtdd.data.model.User;
import com.example.androidtdd.ui.base.BaseActivity;
import com.example.androidtdd.ui.detail.DetailActivity;
import com.example.androidtdd.util.UserListener;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity implements HomeMvpView, UserListener {
    @Inject
    HomePresenter mHomePresenter;
    @BindView(R.id.recycler_users)
    RecyclerView mUserRecycler;
    @BindView(R.id.text_loading)
    TextView mLoadingTextView;
    @BindView(R.id.text_empty)
    TextView mEmptyTextView;

    private UserAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        mHomePresenter.attachView(this);
        mHomePresenter.getUsers();
        mAdapter = new UserAdapter(this);
        mUserRecycler.setAdapter(mAdapter);
        mUserRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void showUsers(List<User> users) {
        mUserRecycler.setVisibility(View.VISIBLE);
        mEmptyTextView.setVisibility(View.GONE);
        mAdapter.setUsers(users);
    }

    @Override
    public void viewDetail(User user) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.ARG_USER, user);
        startActivity(intent);
    }

    @Override
    public void showLoading() {
        mLoadingTextView.setVisibility(View.VISIBLE);
        mUserRecycler.setVisibility(View.GONE);
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

    @Override
    public void onUserClick(User user) {
        mHomePresenter.showDetail(user);
    }

    @Override
    protected void onDestroy() {
        mHomePresenter.detachView();
        super.onDestroy();
    }
}
