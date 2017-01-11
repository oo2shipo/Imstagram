package com.im.imstagram;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by vioooiv on 2017-01-11.
 */

@RunWith(AndroidJUnit4.class)
public class ListViewActivityTest extends ActivityInstrumentationTestCase2<ListViewActivity>
{
    @Rule
    public ActivityTestRule<ListViewActivity> mActivityRule = new ActivityTestRule(ListViewActivity.class);

    private ListViewActivity mActivity;
    private ListView mListviewImage = null;

    public ListViewActivityTest() {
        super(ListViewActivity.class);
    }

    @Before
    public void setUp() throws Exception
    {
        super.setUp();

        // Injecting the Instrumentation instance is required
        // for your test to run with AndroidJUnitRunner.
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
    }

    @After
    public void tearDown() throws Exception
    {

    }

    @Test
    public void testButton() throws Exception
    {
        String userId = "odd";

        // userId
        onView(withId(R.id.edittext_search_input)).perform(typeText(userId), closeSoftKeyboard());

        //click button
        onView(withId(R.id.imageview_search)).perform(click());
    }

    @Test
    public void onCreate() throws Exception
    {
        onView(withId(R.id.listview_photo)).check(matches(isDisplayed()));

        //mActivity = this.getActivity();
        //mListviewImage = (ListView) mActivity.findViewById(R.id.listview_photo); /* 리스트뷰 */
        //Assert.assertNotNull("mListviewPhoto is null", mListviewImage); // Not Null
    }

}