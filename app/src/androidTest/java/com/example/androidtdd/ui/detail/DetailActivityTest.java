package com.example.androidtdd.ui.detail;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.androidtdd.EspressoDaggerMockRule;
import com.example.androidtdd.MockHelper;
import com.example.androidtdd.R;
import com.example.androidtdd.data.local.LocalDataSource;
import com.example.androidtdd.data.model.User;
import com.example.androidtdd.util.Constants;
import com.example.androidtdd.util.EspressoIdlingResource;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import rx.Observable;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Created by quanlt on 23/01/2017.
 */
@RunWith(AndroidJUnit4.class)
public class DetailActivityTest {
    private static final int USER_ID = 10;
    @Rule
    public IntentsTestRule mIntentsTestRule = new IntentsTestRule(DetailActivity.class, true, false);

    @Rule
    public EspressoDaggerMockRule mEspressoDaggerMockRule = new EspressoDaggerMockRule();

    @Mock
    private LocalDataSource mMockLocalDataSource;

    private MockWebServer server = new MockWebServer();

    final Dispatcher dispatcher = new Dispatcher() {
        @Override
        public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
            if (request.getPath().equals("/comments?userId=" + USER_ID)) {
                try {
                    String comments = MockHelper.readCommentsString();
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody(comments)
                            .setBodyDelay(0, TimeUnit.MILLISECONDS);
                } catch (IOException e) {
                    e.printStackTrace();
                    return new MockResponse()
                            .setResponseCode(404)
                            .setBody("blah blah")
                            .setBodyDelay(0, TimeUnit.MILLISECONDS);
                }
            } else if (request.getPath().equals("/photos?_limit=20&userId=" + USER_ID)) {
                try {
                    String photos = MockHelper.readPhotosString();
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody(photos)
                            .setBodyDelay(0, TimeUnit.MILLISECONDS);
                } catch (IOException e) {
                    e.printStackTrace();
                    return new MockResponse()
                            .setResponseCode(404)
                            .setBody("blah blah")
                            .setBodyDelay(0, TimeUnit.MILLISECONDS);
                }
            } else {
                return new MockResponse()
                        .setResponseCode(404)
                        .setBody("blah blah")
                        .setBodyDelay(0, TimeUnit.MILLISECONDS);
            }
        }
    };


    @Before
    public void setUp() throws Exception {
        Espresso.registerIdlingResources(EspressoIdlingResource.getIdlingResource());
        List comments = MockHelper.getComments();
        List photos = MockHelper.getPhotos();
        server.setDispatcher(dispatcher);
        server.start(1212);
        Constants.ENDPOINT = server.url("/").toString();
        when(mMockLocalDataSource.getComments(USER_ID)).thenReturn(Observable.empty());
        when(mMockLocalDataSource.getPhotos(USER_ID)).thenReturn(Observable.empty());

    }

    @Test
    public void testShowUserInfo() throws Exception {
        User user = MockHelper.makeUser(USER_ID);
        Intent intent = new Intent();
        intent.putExtra(DetailActivity.ARG_USER, user);
        mIntentsTestRule.launchActivity(intent);
        onView(withId(R.id.text_name)).check(matches(withText(user.getName())));
        onView(withId(R.id.text_email)).check(matches(withText(user.getEmail())));
        onView(withId(R.id.text_phone)).check(matches(withText(user.getPhone())));
        onView(withId(R.id.text_website)).check(matches(withText(user.getWebsite())));
        onView(withId(R.id.text_city)).check(matches(withText(user.getAddress().getCity())));
        onView(withId(R.id.text_suit)).check(matches(withText(user.getAddress().getSuite())));
        onView(withId(R.id.text_street)).check(matches(withText(user.getAddress().getStreet())));
        onView(withId(R.id.text_bs)).check(matches(withText(user.getCompany().getBs())));
        onView(withId(R.id.text_company_name)).check(matches(withText(user.getCompany().getName())));
        onView(withId(R.id.text_catchphrase)).check(matches(withText(user.getCompany().getCatchPhrase())));
    }

    @Test
    public void testNavigateToComments() throws Exception {

        User user = MockHelper.makeUser(USER_ID);
        Intent intent = new Intent();
        intent.putExtra(DetailActivity.ARG_USER, user);
        mIntentsTestRule.launchActivity(intent);
        onView(withId(R.id.button_comments))
                .perform(click());
        intended(allOf(IntentMatchers.hasComponent("com.example.androidtdd.ui.comment.CommentActivity")
                , IntentMatchers.hasExtra(is(DetailActivity.ARG_USER), Matchers.isA(User.class))));
    }

    @Test
    public void testNavigateToPhotos() throws Exception {

        User user = MockHelper.makeUser(USER_ID);
        Intent intent = new Intent();
        intent.putExtra(DetailActivity.ARG_USER, user);
        mIntentsTestRule.launchActivity(intent);
        onView(withId(R.id.button_photos))
                .perform(click());
        intended(allOf(IntentMatchers.hasComponent("com.example.androidtdd.ui.photo.PhotoActivity")
                , IntentMatchers.hasExtra(is(DetailActivity.ARG_USER), Matchers.isA(User.class))));
    }

    @After
    public void tearDown() throws Exception {
        Espresso.unregisterIdlingResources(EspressoIdlingResource.getIdlingResource());
        server.shutdown();
    }
}
