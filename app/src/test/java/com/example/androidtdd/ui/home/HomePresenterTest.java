package com.example.androidtdd.ui.home;

import com.example.androidtdd.MockHelper;
import com.example.androidtdd.data.PlaceHolderRepository;
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
 * Created by quanlt on 20/01/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class HomePresenterTest {
    @Mock
    HomeMvpView mMockHomeMvpView;
    @Mock
    PlaceHolderRepository mMockRepository;

    private HomePresenter mHomePresenter;

    @Rule
    public final RxSchedulerOverrideRule rule = new RxSchedulerOverrideRule();

    @Before
    public void setUp() throws Exception {
        mHomePresenter = new HomePresenter(mMockRepository);
        mHomePresenter.attachView(mMockHomeMvpView);
    }

    @Test
    public void testGetUsersSuccess() throws Exception {
        List<User> expected = MockHelper.getUsers();
        when(mMockRepository.getUsers()).thenReturn(Observable.just(expected));
        mHomePresenter.getUsers();
        verify(mMockRepository).getUsers();
        verify(mMockHomeMvpView).showLoading();
        verify(mMockHomeMvpView).hideLoading();
        verify(mMockHomeMvpView, times(0)).showError(anyString());
        verify(mMockHomeMvpView).showUsers(expected);
    }

    @Test
    public void testGetUsersReturnNull() throws Exception {
        when(mMockRepository.getUsers()).thenReturn(Observable.just(null));
        mHomePresenter.getUsers();
        verify(mMockRepository).getUsers();
        verify(mMockHomeMvpView).showLoading();
        verify(mMockHomeMvpView).hideLoading();
        verify(mMockHomeMvpView, times(0)).showError(anyString());
        verify(mMockHomeMvpView, times(0)).showUsers(any(List.class));
    }

    @Test
    public void testGetUsersReturnEmptyList() throws Exception {
        List users = mock(List.class);
        when(mMockRepository.getUsers()).thenReturn(Observable.just(users));
        mHomePresenter.getUsers();
        verify(mMockRepository).getUsers();
        verify(mMockHomeMvpView).showLoading();
        verify(mMockHomeMvpView).hideLoading();
        verify(mMockHomeMvpView, times(0)).showError(anyString());
        verify(mMockHomeMvpView, times(0)).showUsers(any(List.class));
    }

    @Test
    public void testGetUsersFailed() throws Exception {
        String message = "Unknown Error";
        when(mMockRepository.getUsers()).thenReturn(Observable.error(new RuntimeException(message)));
        mHomePresenter.getUsers();
        verify(mMockHomeMvpView).showLoading();
        verify(mMockHomeMvpView, times(0)).showUsers(any(List.class));
        verify(mMockHomeMvpView).hideLoading();
        verify(mMockHomeMvpView).showError(message);
        verify(mMockHomeMvpView, times(0)).showUsers(any(List.class));
        verify(mMockHomeMvpView).showEmpty();
    }

    @Test
    public void testViewDetail() throws Exception {
        int userId = 1;
        User user = MockHelper.makeUser(userId);
        mHomePresenter.showDetail(user);
        verify(mMockHomeMvpView).viewDetail(user);

    }

    @After
    public void tearDown() throws Exception {
        mHomePresenter.detachView();
    }
}
