package com.example.androidtdd.data.remote;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by quanlt on 20/01/2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class RemoteDataSourceImplTest {
    @Mock
    private ApiService mMockApiService;
    private RemoteDataSource mRemoteDataSource;
    private final int USER_ID = 10;

    @Before
    public void setUp() throws Exception {
        mRemoteDataSource = new RemoteDataSourceImpl(mMockApiService);
    }

    @Test
    public void testGetUsersSuccess() throws Exception {
        mRemoteDataSource.getUsers();
        verify(mMockApiService, times(1)).getUsers();

    }

    @Test(expected = Exception.class)
    public void testGetUsersFailed() throws Exception {
        when(mRemoteDataSource.getUsers())
                .thenReturn(Observable.error(new Throwable("Error")));
        mRemoteDataSource.getUsers().toBlocking().first();
    }

    @Test
    public void testGetUserPhotos() throws Exception {
        mRemoteDataSource.getPhotos(USER_ID);
        verify(mMockApiService, times(1)).getPhotos(USER_ID);
    }

    @Test(expected = Exception.class)
    public void testGetUserPhotosShouldThrowException() {
        when(mRemoteDataSource.getPhotos(USER_ID))
                .thenReturn(Observable.error(new Throwable("Error")));
        mRemoteDataSource.getPhotos(USER_ID).toBlocking().first();

    }

    @Test
    public void testGetUserComments() throws Exception {
        mRemoteDataSource.getComments(USER_ID);
        verify(mMockApiService, times(1)).getComments(USER_ID);
    }

    @Test(expected = Exception.class)
    public void testGetUsersCommentsShouldThrowException() {
        when(mRemoteDataSource.getComments(USER_ID))
                .thenReturn(Observable.error(new Throwable("Error")));
        mRemoteDataSource.getComments(USER_ID).toBlocking().first();
        verify(mMockApiService, times(1)).getComments(USER_ID);
    }
}
