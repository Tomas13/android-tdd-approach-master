package com.example.androidtdd.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.androidtdd.R;
import com.example.androidtdd.data.model.User;
import com.example.androidtdd.ui.base.BaseActivity;
import com.example.androidtdd.ui.comment.CommentActivity;
import com.example.androidtdd.ui.photo.PhotoActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailActivity extends BaseActivity implements DetailMvpView {
    public static final String ARG_USER = "SELECTED_USER";
    @Inject
    DetailPresenter mDetailPresenter;
    @BindView(R.id.text_name)
    TextView mNameTextView;
    @BindView(R.id.text_email)
    TextView mEmailTextView;
    @BindView(R.id.text_phone)
    TextView mPhoneTextView;
    @BindView(R.id.text_website)
    TextView mWebsiteTextView;
    @BindView(R.id.text_suit)
    TextView mSuitTextView;
    @BindView(R.id.text_street)
    TextView mStreetTextView;
    @BindView(R.id.text_city)
    TextView mCityTextView;
    @BindView(R.id.text_company_name)
    TextView mCompanyNameTextView;
    @BindView(R.id.text_bs)
    TextView mBsTextView;
    @BindView(R.id.text_catchphrase)
    TextView mCatchphraseTextView;
    private User mSelectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        mDetailPresenter.attachView(this);
        mSelectedUser = getIntent().getParcelableExtra(ARG_USER);
        mNameTextView.setText(mSelectedUser.getName());
        mEmailTextView.setText(mSelectedUser.getEmail());
        mPhoneTextView.setText(mSelectedUser.getPhone());
        mWebsiteTextView.setText(mSelectedUser.getWebsite());
        mSuitTextView.setText(mSelectedUser.getAddress().getSuite());
        mStreetTextView.setText(mSelectedUser.getAddress().getStreet());
        mCityTextView.setText(mSelectedUser.getAddress().getCity());
        mCompanyNameTextView.setText(mSelectedUser.getCompany().getName());
        mBsTextView.setText(mSelectedUser.getCompany().getBs());
        mCatchphraseTextView.setText(mSelectedUser.getCompany().getCatchPhrase());
    }

    @Override
    public void showPhotos(User user) {
        Intent intent = new Intent(this, PhotoActivity.class);
        intent.putExtra(ARG_USER, mSelectedUser);
        startActivity(intent);
    }

    @Override
    public void showComments(User user) {
        Intent intent = new Intent(this, CommentActivity.class);
        intent.putExtra(ARG_USER, mSelectedUser);
        startActivity(intent);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    protected void onDestroy() {
        mDetailPresenter.detachView();
        super.onDestroy();
    }

    @OnClick({R.id.button_photos, R.id.button_comments})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_photos:
                mDetailPresenter.showPhotos(mSelectedUser);
                break;
            case R.id.button_comments:
                mDetailPresenter.showComments(mSelectedUser);
                break;
        }
    }
}
