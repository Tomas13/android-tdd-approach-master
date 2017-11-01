package com.example.androidtdd.data.local;

import com.example.androidtdd.BuildConfig;
import com.example.androidtdd.MockHelper;
import com.example.androidtdd.data.model.Comment;
import com.example.androidtdd.data.model.Photo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.log.RealmLog;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doCallRealMethod;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by quanlt on 20/01/2017.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
@SuppressStaticInitializationFor("io.realm.internal.Util")
@PrepareForTest({Realm.class, RealmLog.class, RealmResults.class, RealmQuery.class})
public class LocalDataSourceImplTest {
    Realm mMockRealm;
    @Rule
    public PowerMockRule rule = new PowerMockRule();
    private LocalDataSource mLocalDataSource;
    private static final int USER_ID = 2;

    @Before
    public void setUp() throws Exception {
        mockStatic(RealmLog.class);
        mockStatic(Realm.class);
        mMockRealm = mock(Realm.class);
        when(Realm.getDefaultInstance()).thenReturn(mMockRealm);
        List<Comment> comments = MockHelper.getComments();
        List<Photo> photos = MockHelper.getPhotos();

        RealmQuery<Comment> commentQuery = mock(RealmQuery.class);
        RealmQuery<Photo> photoQuery = mock(RealmQuery.class);

        RealmResults commentResult = mock(RealmResults.class);
        RealmResults photoResult = mock(RealmResults.class);

        when(mMockRealm.where(Comment.class)).thenReturn(commentQuery);
        when(mMockRealm.where(Photo.class)).thenReturn(photoQuery);

        when(commentQuery.findAll()).thenReturn(commentResult);
        when(photoQuery.findAll()).thenReturn(photoResult);

        when(commentQuery.equalTo("userId", USER_ID)).thenReturn(commentQuery);
        when(photoQuery.equalTo("userId", USER_ID)).thenReturn(photoQuery);

        doCallRealMethod().when(mMockRealm)
                .executeTransaction(Mockito.any(Realm.Transaction.class));
        when(commentResult.iterator()).thenReturn(comments.iterator());
        when(photoResult.iterator()).thenReturn(photos.iterator());

        mLocalDataSource = new LocalDataSourceImpl();
    }

    @Test
    public void testGetInstance() throws Exception {
        assertEquals(Realm.getDefaultInstance(), mMockRealm);
    }

    @Test
    public void testSaveComments() throws Exception {
        List<Comment> comments = MockHelper.getComments();
        mLocalDataSource.saveComment(comments, USER_ID);
        verify(mMockRealm, times(1)).copyToRealmOrUpdate(comments);
        verify(mMockRealm, times(1)).close();
    }

    @Test
    public void testSavePhotos() throws Exception {
        List<Photo> photos = MockHelper.getPhotos();
        mLocalDataSource.savePhotos(photos, USER_ID);
        verify(mMockRealm, times(1)).copyToRealmOrUpdate(photos);
        verify(mMockRealm, times(1)).close();
    }

    @Test
    public void testGetComments() throws Exception {
        mLocalDataSource.getComments(USER_ID);
        verify(mMockRealm.where(Comment.class).equalTo("userId", USER_ID)).findAll();
        verify(mMockRealm).copyFromRealm(any(Iterable.class));
        verify(mMockRealm).close();
    }

    @Test
    public void testGetPhotos() throws Exception {
        mLocalDataSource.getPhotos(USER_ID);
        verify(mMockRealm.where(Photo.class).equalTo("userId", USER_ID)).findAll();
        verify(mMockRealm).copyFromRealm(any(Iterable.class));
        verify(mMockRealm).close();
    }

}
