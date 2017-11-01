package com.example.androidtdd.ui.detail;

import com.example.androidtdd.MockHelper;
import com.example.androidtdd.data.model.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

/**
 * Created by quanlt on 23/01/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class DetailPresenterTest {
    @Mock
    DetailMvpView mMockDetailMvpView;
    private DetailPresenter mDetailPresenter;
    private final int USER_ID = 10;
    @Before
    public void setUp() throws Exception {
        mDetailPresenter = new DetailPresenter();
        mDetailPresenter.attachView(mMockDetailMvpView);
    }

    @Test
    public void testViewComments() throws Exception {
        User user = MockHelper.makeUser(USER_ID);
        mDetailPresenter.showComments(user);
        verify(mMockDetailMvpView).showComments(user);
    }

    @Test
    public void testViewPhotos() throws Exception {
        User user = MockHelper.makeUser(USER_ID);
        mDetailPresenter.showPhotos(user);
        verify(mMockDetailMvpView).showPhotos(user);
    }

    @After
    public void tearDown() throws Exception {
        mDetailPresenter.detachView();
    }
}
