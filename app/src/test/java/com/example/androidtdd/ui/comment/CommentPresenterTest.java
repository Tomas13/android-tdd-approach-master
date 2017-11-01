package com.example.androidtdd.ui.comment;

import com.example.androidtdd.MockHelper;
import com.example.androidtdd.data.PlaceHolderRepository;
import com.example.androidtdd.data.model.Comment;
import com.example.androidtdd.data.model.User;
import com.example.androidtdd.util.RxSchedulerOverrideRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import rx.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by quanlt on 23/01/2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class CommentPresenterTest {
    @Mock
    PlaceHolderRepository mMockPlaceHolderRepository;
    @Mock
    CommentMvpView mMockCommentMvpView;

    @Rule
    public RxSchedulerOverrideRule rule = new RxSchedulerOverrideRule();

    private CommentPresenter mCommentPresenter;
    private final int USER_ID = 10;

    @Before
    public void setUp() throws Exception {
        mCommentPresenter = new CommentPresenter(mMockPlaceHolderRepository);
        mCommentPresenter.attachView(mMockCommentMvpView);
    }

    @Test
    public void testGetCommentSuccess() throws Exception {
        List<Comment> comments = MockHelper.getComments();
        User user = MockHelper.makeUser(USER_ID);
        when(mMockPlaceHolderRepository.getComments(USER_ID))
                .thenReturn(Observable.just(comments));
        mCommentPresenter.getComments(user);
        verify(mMockCommentMvpView).showLoading();
        verify(mMockCommentMvpView).hideLoading();
        verify(mMockCommentMvpView).showComments(comments);
        verify(mMockCommentMvpView, times(0)).showError(anyString());
    }

    @Test
    public void testGetCommentFailed() throws Exception {
        String message = "Unknown error";
        User user = MockHelper.makeUser(USER_ID);
        when(mMockPlaceHolderRepository.getComments(USER_ID))
                .thenReturn(Observable.error(new Throwable(message)));
        mCommentPresenter.getComments(user);
        verify(mMockCommentMvpView).showLoading();
        verify(mMockCommentMvpView).hideLoading();
        verify(mMockCommentMvpView, times(0)).showComments(any(List.class));
        verify(mMockCommentMvpView, times(1)).showError(message);
    }
    @Test
    public void testGetCommentsReturnNull() throws Exception {
        when(mMockPlaceHolderRepository.getComments(USER_ID))
                .thenReturn(Observable.just(null));
        User user = MockHelper.makeUser(USER_ID);
        mCommentPresenter.getComments(user);
        verify(mMockCommentMvpView).showLoading();
        verify(mMockCommentMvpView).hideLoading();
        verify(mMockCommentMvpView, times(0)).showComments(any(List.class));
        verify(mMockCommentMvpView, times(0)).showError(anyString());
        verify(mMockCommentMvpView, times((1))).showEmpty();

    }
    @Test
    public void testGetCommentsReturnEmptyList() throws Exception {
        when(mMockPlaceHolderRepository.getComments(USER_ID))
                .thenReturn(Observable.just(mock(List.class)));
        User user = MockHelper.makeUser(USER_ID);
        mCommentPresenter.getComments(user);
        verify(mMockCommentMvpView).showLoading();
        verify(mMockCommentMvpView).hideLoading();
        verify(mMockCommentMvpView, times(0)).showComments(any(List.class));
        verify(mMockCommentMvpView, times(0)).showError(anyString());
        verify(mMockCommentMvpView, times((1))).showEmpty();
    }

    @After
    public void tearDown() throws Exception {
        mCommentPresenter.detachView();
    }
}
