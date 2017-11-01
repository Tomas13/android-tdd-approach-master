package com.example.androidtdd.ui.home;

import android.app.Activity;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.androidtdd.MockHelper;
import com.example.androidtdd.R;
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

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Created by quanlt on 23/01/2017.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HomeActivityTest {
    @Rule
    public IntentsTestRule mActivityTestRule = new IntentsTestRule(HomeActivity.class, true, false);
    private MockWebServer server;

    @Before
    public void setUp() throws Exception {
        server = new MockWebServer();
        server.start(1212);
        Constants.ENDPOINT = server.url("/").toString();
    }

    @Test
    public void testGetUsersAndDisplayData() throws Exception {
        String usersInput = MockHelper.readUsersString();
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(usersInput)
                .setBodyDelay(2000, TimeUnit.MILLISECONDS));
        List<User> users = MockHelper.getUsers(usersInput, new Gson());
        mActivityTestRule.launchActivity(null);
        onView(withId(R.id.text_loading)).check(matches(isDisplayed()));
        Espresso.registerIdlingResources(EspressoIdlingResource.getIdlingResource());
        onView(withId(R.id.text_loading)).check(matches(not(isDisplayed())));
        RecyclerViewInteraction.<User>onRecyclerView(withId(R.id.recycler_users))
                .withItems(users)
                .check((item, view, e) -> matches(allOf(hasDescendant(withText(item.getName())),
                        hasDescendant(withText(item.getPhone())),
                        hasDescendant(withText(item.getEmail())),
                        hasDescendant(withText(item.getWebsite())))).check(view, e));
        Espresso.unregisterIdlingResources(EspressoIdlingResource.getIdlingResource());
    }

    @Test
    public void testGetUsersAndDisplayError() throws Exception {
        String message = "HTTP 404 Client Error";
        server.enqueue(new MockResponse()
                .setResponseCode(404)
                .setBody("blah blah")
                .setBodyDelay(1000, TimeUnit.MILLISECONDS));
        mActivityTestRule.launchActivity(null);
        onView(withId(R.id.text_loading)).check(matches(isDisplayed()));
        Espresso.registerIdlingResources(EspressoIdlingResource.getIdlingResource());
        onView(withId(R.id.text_loading)).check(matches(not(isDisplayed())));
        Activity activity = mActivityTestRule.getActivity();
        onView(withId(R.id.text_empty)).check(matches(isDisplayed()));
        onView(withText(message)).inRoot(withDecorView(not(is(activity.getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
        Espresso.unregisterIdlingResources(EspressoIdlingResource.getIdlingResource());
    }

    @Test
    public void testClickOnUserNavigateToDetail() throws Exception {
        Espresso.registerIdlingResources(EspressoIdlingResource.getIdlingResource());
        String usersInput = MockHelper.readUsersString();
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(usersInput)
                .setBodyDelay(0, TimeUnit.MILLISECONDS));
        List<User> users = MockHelper.getUsers(usersInput, new Gson());
        mActivityTestRule.launchActivity(null);
        onView(withId(R.id.recycler_users))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        intended(allOf(IntentMatchers.toPackage("com.example.androidtdd"),
                IntentMatchers.hasExtra(DetailActivity.ARG_USER, users.get(0))));
        Espresso.unregisterIdlingResources(EspressoIdlingResource.getIdlingResource());
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }

}
