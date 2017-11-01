package com.example.androidtdd.ui.photo;

import android.app.Activity;
import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.ImageView;

import com.example.androidtdd.EspressoDaggerMockRule;
import com.example.androidtdd.MockHelper;
import com.example.androidtdd.R;
import com.example.androidtdd.data.local.LocalDataSource;
import com.example.androidtdd.data.model.Photo;
import com.example.androidtdd.data.model.User;
import com.example.androidtdd.ui.detail.DetailActivity;
import com.example.androidtdd.util.Constants;
import com.example.androidtdd.util.EspressoIdlingResource;
import com.example.androidtdd.util.RecyclerViewInteraction;
import com.google.gson.Gson;

import org.hamcrest.Description;
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
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

/**
 * Created by quanlt on 24/01/2017.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PhotoActivityTest {
    @Rule
    public EspressoDaggerMockRule daggerMockRule = new EspressoDaggerMockRule();
    @Rule
    public ActivityTestRule mActivityTestRule =
            new ActivityTestRule(PhotoActivity.class, true, false);
    private final int USER_ID = 10;
    private MockWebServer mServer = new MockWebServer();
    @Mock
    private LocalDataSource mMockLocalDataSource;

    @Before
    public void setUp() throws Exception {
        when(mMockLocalDataSource.getPhotos(anyInt())).thenReturn(Observable.empty());
        mServer.start(1212);
        Constants.ENDPOINT = mServer.url("/").toString();
    }

    @Test
    public void testGetPhotosAndDisplayError() throws Exception {
        User user = MockHelper.makeUser(USER_ID);
        mServer.enqueue(new MockResponse()
                .setResponseCode(404)
                .setBody("Generic Error")
                .setBodyDelay(2000, TimeUnit.MILLISECONDS));
        String message = "HTTP 404 Client Error";
        Intent intent = new Intent();
        intent.putExtra(DetailActivity.ARG_USER, user);
        mActivityTestRule.launchActivity(intent);
        onView(withId(R.id.text_loading)).check(matches(isDisplayed()));
        onView(withId(R.id.recycler_photos)).check(matches(not(isDisplayed())));
        Espresso.registerIdlingResources(EspressoIdlingResource.getIdlingResource());
        onView(withId(R.id.text_loading)).check(matches(not(isDisplayed())));
        onView(withId(R.id.text_empty)).check(matches(isDisplayed()));
        Activity activity = mActivityTestRule.getActivity();
        onView(withText(message)).inRoot(withDecorView(not(is(activity.getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
        Espresso.unregisterIdlingResources(EspressoIdlingResource.getIdlingResource());
    }

    @Test
    public void testGetPhotosAndDisplayData() throws Exception {
        User user = MockHelper.makeUser(USER_ID);
        String photoString = MockHelper.readPhotosString();
        mServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(photoString)
                .setBodyDelay(2000, TimeUnit.MILLISECONDS));
        List<Photo> photos = MockHelper.getPhotos(photoString, new Gson());
        Intent intent = new Intent();
        intent.putExtra(DetailActivity.ARG_USER, user);
        mActivityTestRule.launchActivity(intent);
        onView(withId(R.id.text_loading)).check(matches(isDisplayed()));
        onView(withId(R.id.recycler_photos)).check(matches(not(isDisplayed())));
        Espresso.registerIdlingResources(EspressoIdlingResource.getIdlingResource());
        onView(withId(R.id.text_loading)).check(matches(not(isDisplayed())));
        onView(withId(R.id.text_empty)).check(matches(not(isDisplayed())));
        RecyclerViewInteraction.<Photo>onRecyclerView(withId(R.id.recycler_photos))
                .withItems(photos).check(((item, view, e) -> hasDrawable()));
        Espresso.unregisterIdlingResources(EspressoIdlingResource.getIdlingResource());
    }

    public static BoundedMatcher<View, ImageView> hasDrawable() {
        return new BoundedMatcher<View, ImageView>(ImageView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has drawable");
            }

            @Override
            public boolean matchesSafely(ImageView imageView) {
                return imageView.getDrawable() != null;
            }
        };
    }

    @After
    public void tearDown() throws Exception {
        mServer.shutdown();

    }
}
