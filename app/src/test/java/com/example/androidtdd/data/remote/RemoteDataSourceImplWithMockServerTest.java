package com.example.androidtdd.data.remote;

import com.example.androidtdd.MockHelper;
import com.example.androidtdd.data.model.Comment;
import com.example.androidtdd.data.model.Photo;
import com.example.androidtdd.data.model.User;
import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

import static org.junit.Assert.assertEquals;

/**
 * Created by quanlt on 20/01/2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class RemoteDataSourceImplWithMockServerTest {
    private RemoteDataSource mRemoteDataSource;
    private final int USER_ID = 10;
    private MockWebServer server = new MockWebServer();

    @Before
    public void setUp() throws Exception {
        String url = server.url("/").toString();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        mRemoteDataSource = new RemoteDataSourceImpl(apiService);
    }

    @Test
    public void testGetUsersSuccess() throws Exception {
        String usersString = MockHelper.readUsersString();
        server.enqueue(new MockResponse().setBody(MockHelper.readUsersString()));
        Observable<List<User>> result = mRemoteDataSource.getUsers();
        List<User> actual = result.toBlocking().first();
        List<User> expected = MockHelper.getUsers(usersString, new Gson());
        assertEquals(expected.get(0), actual.get(0));
    }

    @Test(expected = RuntimeException.class)
    public void testGetUserFail() {
        server.enqueue(new MockResponse().setResponseCode(404).setBody("Generic Error"));
        Observable result = mRemoteDataSource.getUsers();
        result.toBlocking().first();
    }

    @Test
    public void testGetPhotosSuccess() throws Exception {
        String input = MockHelper.readPhotosString();
        server.enqueue(new MockResponse().setBody(input));
        Observable<List<Photo>> result = mRemoteDataSource.getPhotos(USER_ID);
        List<Photo> actual = result.toBlocking().first();
        List<Photo> expected = MockHelper.getPhotos(input, new Gson());
        assertEquals(expected, actual);
    }

    @Test(expected = RuntimeException.class)
    public void testGetPhotosFail() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(404));
        mRemoteDataSource.getPhotos(USER_ID).toBlocking().first();
    }

    @Test(expected = RuntimeException.class)
    public void testGetCommentsFail() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(404));
        mRemoteDataSource.getComments(USER_ID).toBlocking().first();
    }

    @Test
    public void testGetCommentsSuccess() throws Exception {
        String input = MockHelper.readCommentsString();
        server.enqueue(new MockResponse().setBody(input));
        Observable<List<Comment>> result = mRemoteDataSource.getComments(USER_ID);
        List<Comment> actual = result.toBlocking().first();
        List<Comment> expected = MockHelper.getComments(input, new Gson());
        assertEquals(expected, actual);
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }
}
