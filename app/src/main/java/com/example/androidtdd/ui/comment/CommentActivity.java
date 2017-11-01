package com.example.androidtdd.ui.comment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidtdd.R;
import com.example.androidtdd.data.model.Comment;
import com.example.androidtdd.data.model.User;
import com.example.androidtdd.ui.base.BaseActivity;
import com.example.androidtdd.ui.detail.DetailActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentActivity extends BaseActivity implements CommentMvpView {

    @BindView(R.id.recycler_comments)
    RecyclerView mCommentRecycler;
    @BindView(R.id.text_loading)
    TextView mLoadingTextView;
    @BindView(R.id.text_empty)
    TextView mEmptyTextView;

    @Inject
    CommentPresenter mCommentPresenter;
    private User mSelectedUser;
    private CommentAdapter mCommentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        getActivityComponent().inject(this);
        ButterKnife.bind(this);
        mSelectedUser = getIntent().getParcelableExtra(DetailActivity.ARG_USER);
        mCommentPresenter.attachView(this);
        mCommentPresenter.getComments(mSelectedUser);
        mCommentAdapter = new CommentAdapter();
        mCommentRecycler.setLayoutManager(new LinearLayoutManager(this));
        mCommentRecycler.setAdapter(mCommentAdapter);
    }

    @Override
    public void showComments(List<Comment> comments) {
        mCommentRecycler.setVisibility(View.VISIBLE);
        mEmptyTextView.setVisibility(View.GONE);
        mCommentAdapter.setComments(comments);
        mCommentAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLoading() {
        mLoadingTextView.setVisibility(View.VISIBLE);
        mCommentRecycler.setVisibility(View.GONE);
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
    protected void onDestroy() {
        mCommentPresenter.detachView();
        super.onDestroy();
    }
}
