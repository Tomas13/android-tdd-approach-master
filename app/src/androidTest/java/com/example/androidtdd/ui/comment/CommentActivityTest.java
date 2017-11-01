package com.example.androidtdd.ui.comment;

import android.app.Activity;
import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.androidtdd.EspressoDaggerMockRule;
import com.example.androidtdd.MockHelper;
import com.example.androidtdd.R;
import com.example.androidtdd.data.local.LocalDataSource;
import com.example.androidtdd.data.model.Comment;
import com.example.androidtdd.data.model.User;
import com.example.androidtdd.ui.detail.DetailActivity;
import com.example.androidtdd.util.Constants;
import com.example.androidtdd.util.EspressoIdlingResource;
import com.example.androidtdd.util.RecyclerViewInteraction;
import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import rx.Observable;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

/**
 * Created by quanlt on 23/01/2017.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CommentActivityTest {
    @Rule
    public ActivityTestRule mActivityTestRule = new ActivityTestRule(CommentActivity.class, true, false);
    @Rule
    public EspressoDaggerMockRule mEspressoDaggerMockRule = new EspressoDaggerMockRule();
    private final int USER_ID = 10;
    private MockWebServer mServer = new MockWebServer();
    @Mock
    private LocalDataSource mMockLocalDataSource;

    @Before
    public void setUp() throws Exception {
        mServer.start(1212);
        Constants.ENDPOINT = mServer.url("/").toString();
        when(mMockLocalDataSource.getComments(anyInt())).thenReturn(Observable.empty());
    }

    @Test
    public void testGetCommentsAndDisplayData() throws Exception {
        User user = MockHelper.makeUser(USER_ID);
        String commentsString = MockHelper.readCommentsString();
        mServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(commentsString)
                .setBodyDelay(2000, TimeUnit.MILLISECONDS));
        List<Comment> comments = MockHelper.getComments(commentsString, new Gson());
        Intent intent = new Intent();
        intent.putExtra(DetailActivity.ARG_USER, user);
        mActivityTestRule.launchActivity(intent);
        onView(withId(R.id.text_loading)).check(matches(isDisplayed()));
        onView(withId(R.id.recycler_comments)).check(matches(not(isDisplayed())));
        Espresso.registerIdlingResources(EspressoIdlingResource.getIdlingResource());
        onView(withId(R.id.text_loading)).check(matches(not(isDisplayed())));
        RecyclerViewInteraction.<Comment>onRecyclerView(withId(R.id.recycler_comments))
                .withItems(comments)
                .check((item, view, e) -> matches(hasDescendant(withText(item.getName()))));
        Espresso.unregisterIdlingResources(EspressoIdlingResource.getIdlingResource());
    }

    @Test
    public void testGetCommentsAndDisplayError() throws Exception {
        User user = MockHelper.makeUser(USER_ID);
        mServer.enqueue(new MockResponse()
                .setResponseCode(404)
                .setBody("Generic Error")
                .setBodyDelay(2000, TimeUnit.MILLISECONDS));
        String message = "HTTP 404 Client Error";
        Intent intent = new Intent();
        intent.putExtra(DetailActivity.ARG_USER, user);
        mActivityTestRule.launchActivity(intent);
        onView(withId(R.id.text_loading)).check(matches(is(isDisplayed())));
        onView(withId(R.id.recycler_comments)).check(matches(not(isDisplayed())));
        Espresso.registerIdlingResources(EspressoIdlingResource.getIdlingResource());
        onView(withId(R.id.text_loading)).check(matches(not(isDisplayed())));
        onView(withId(R.id.text_empty)).check(matches(is(isDisplayed())));
        Activity activity = mActivityTestRule.getActivity();
        onView(withText(message)).inRoot(withDecorView(not(is(activity.getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
        Espresso.unregisterIdlingResources(EspressoIdlingResource.getIdlingResource());
    }

    @After
    public void tearDown() throws Exception {
        Espresso.unregisterIdlingResources(EspressoIdlingResource.getIdlingResource());
        mServer.shutdown();

    }
}
