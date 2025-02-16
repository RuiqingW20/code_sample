package application.jobcompare.screens;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import application.jobcompare.db.DbTest;
import edu.gatech.seclass.jobcompare.MainActivity;
import edu.gatech.seclass.jobcompare.R;


@RunWith(AndroidJUnit4.class)
public class MainMenuScreenTest {

    @Rule
    public ActivityScenarioRule<MainActivity> tActivityRule = new ActivityScenarioRule<>(
            MainActivity.class);

    @Before
    public void setUp() {
        Intents.init();
        DbTest.resetDb();
    }

    @Test
    public void onCreateTest() {
        onView(withId(R.id.jobDetailsMenuButton)).check(matches(ViewMatchers.isDisplayed()));
        onView(withId(R.id.enterJobOfferMenuButton)).check(matches(ViewMatchers.isDisplayed()));
        onView(withId(R.id.comparisonSettingsMenuButton)).check(matches(ViewMatchers.isDisplayed()));
        onView(withId(R.id.jobComparisonMenuButton)).check(matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void compareButtonNoJobTest() {
        // Make sure the comparison button is not enabled.
        onView(withId(R.id.jobComparisonMenuButton)).check(matches(ViewMatchers.isNotEnabled()));
    }

    @Test
    public void compareButton1JobTest() {
        DbTest.resetDb();

        // Add a job.
        DbTest.dbManager.addJob(DbTest.job1);

        // Go to Job Offer and back.
        onView(withId(R.id.jobComparisonMenuButton)).perform(click());
        onView(withId(R.id.cancelButton)).perform(click());

        // Make sure the comparison button is not enabled.
        onView(withId(R.id.jobComparisonMenuButton)).check(matches(ViewMatchers.isNotEnabled()));
    }

    @Test
    public void compareButton2JobsTest() {
        DbTest.resetDb();

        // Add 2 jobs.
        DbTest.dbManager.addJob(DbTest.job1);
        DbTest.dbManager.addJob(DbTest.job2);

        // Go to Job Offer and back.
        onView(withId(R.id.jobComparisonMenuButton)).perform(click());
        onView(withId(R.id.cancelButton)).perform(click());

        // Make sure the comparison button is not enabled.
        onView(withId(R.id.jobComparisonMenuButton)).check(matches(ViewMatchers.isEnabled()));
    }

    @After
    public void tearDown() {
        Intents.release();
        DbTest.closeDb();
    }

}
