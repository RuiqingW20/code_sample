package application.jobcompare.screens;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.times;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static org.junit.Assert.assertEquals;
import static edu.gatech.seclass.jobcompare.screens.JobOfferScreen.resetJobOffer;

import android.os.SystemClock;

import androidx.test.espresso.ViewInteraction;
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
import edu.gatech.seclass.jobcompare.db.DbHelper;
import edu.gatech.seclass.jobcompare.db.DbManager;
import edu.gatech.seclass.jobcompare.models.Job;


@RunWith(AndroidJUnit4.class)
public class JobOfferScreenTest {

    @Rule
    public ActivityScenarioRule<MainActivity> tActivityRule = new ActivityScenarioRule<>(
            MainActivity.class);

    @Before
    public void setUp() {
        Intents.init();
        DbTest.resetDb();
    }

    @Test
    public void navigationTest() {
        onView(withId(R.id.enterJobOfferMenuButton)).perform(click());

        JobDetailsScreenTest.emptyScreenTest();
    }

    @Test
    public void cancelButtonTest() {
        onView(withId(R.id.enterJobOfferMenuButton)).perform(click());
        JobDetailsScreenTest.setInputs(DbTest.job1);
        onView(withId(R.id.cancelButton)).perform(click());

        // Make sure nothing was saved to DB.
        assertEquals(0, DbTest.dbManager.getJobs().size());

        // Check to see if we are back on the Main Menu.
        intended(hasComponent("edu.gatech.seclass.jobcompare.screens.MainMenuScreen"));
    }

    /**
     * Test Scenario: No current job, no other job offer does exist.
     * Save job should result in a job in the DB.
     * Action buttons should appear, except the compareButton.
     * We should remain on the Job Offer Screen.
     * Canceling and returning should result in an Job Offer Screen with empty fields.
     */
    @Test
    public void saveButton1JobTest() {
        DbTest.resetDb();
        resetJobOffer();

        // Go to Job Offer Screen.
        onView(withId(R.id.enterJobOfferMenuButton)).perform(click());
        intended(hasComponent("edu.gatech.seclass.jobcompare.screens.JobOfferScreen"));

        // Input the stuffs
        JobDetailsScreenTest.setInputs(DbTest.job2);
        onView(withId(R.id.saveButton)).perform(click());

        // Make sure snackbar does its thing.
        testSnackbar(DbTest.job2);

        // Only 1 job in DB.
        assertEquals(1, DbTest.dbManager.getJobs().size());

        // Make sure the action buttons appear (except compareButton).
        onView(withId(R.id.compareButton)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        onView(withId(R.id.enterAnother)).check(matches(ViewMatchers.isDisplayed()));
        onView(withId(R.id.returnToMenu)).check(matches(ViewMatchers.isDisplayed()));

        // Check to see if we are still on the Job Offer Screen.
        intended(hasComponent("edu.gatech.seclass.jobcompare.screens.JobOfferScreen"));
        onView(withId(R.id.cancelButton)).perform(click());
        intended(hasComponent("edu.gatech.seclass.jobcompare.screens.MainMenuScreen"));
        onView(withId(R.id.enterJobOfferMenuButton)).perform(click());
        JobDetailsScreenTest.emptyScreenTest();

        // Return to Main Menu.
        onView(withId(R.id.cancelButton)).perform(click());
    }

    public void testSnackbar(Job job) {
        // Make sure Snackbar appears.
        ViewInteraction snackBar = onView(ViewMatchers.withText(job.toString() + " Saved"));
        snackBar.check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        // Wait for Snackbar to disappear.
        // Not the best way to do this but I couldn't figure out IdlingResource.
        SystemClock.sleep(3100);
        snackBar.check(doesNotExist());
    }

    /**
     * Test Scenario: No no other job offer exists, but a current job does exist.
     * Save job should result in a job in the DB.
     * Snackbar notifying the user that the job was saved should appear.
     * All action buttons should appear.
     * We should remain on the Job Offer Screen.
     * Compare button should result in the current Job Offer and the Current Job being compared.
     */
    @Test
    public void saveButton2JobsTest() {
        DbManager.clearTable(DbHelper.JOBS_TABLE);
        resetJobOffer();

        // Add current job.
        DbTest.dbManager.addJob(DbTest.job1);
        assertEquals(1, DbTest.dbManager.getJobs().size());

        // Add new job offer.
        onView(withId(R.id.enterJobOfferMenuButton)).perform(click());
        JobDetailsScreenTest.setInputs(DbTest.job3);
        onView(withId(R.id.saveButton)).perform(click());

        // Check to see if we are still on the Job Offer Screen.
        intended(hasComponent("edu.gatech.seclass.jobcompare.screens.JobOfferScreen"));

        // Make sure the offer saved.
        assertEquals(2, DbTest.dbManager.getJobs().size());

        // Make sure snackbar does its thing.
        testSnackbar(DbTest.job3);

        // Make sure all action buttons appear.
        onView(withId(R.id.compareButton)).check(matches(ViewMatchers.isDisplayed()));
        onView(withId(R.id.enterAnother)).check(matches(ViewMatchers.isDisplayed()));
        onView(withId(R.id.returnToMenu)).check(matches(ViewMatchers.isDisplayed()));

        // Make sure compare button is enabled.
        onView(withId(R.id.compareButton)).check(matches(ViewMatchers.isEnabled()));
        onView(withId(R.id.compareButton)).check(matches(ViewMatchers.isClickable()));

        // Make sure we are on the Jobs Comparison Screen.
        onView(withId(R.id.compareButton)).perform(scrollTo(), click());
        intended(hasComponent("edu.gatech.seclass.jobcompare.screens.JobsComparisonScreen"));
    }

    /**
     * Test Scenario: No other job offer exist, one does not have a current job.
     * Save job should result in a job in the DB.
     * Snackbar notifying the user that the job was saved should appear.
     * Enter Another button should appear enabled.
     * We should remain on the Job Offer Screen.
     * Add Another should result in the Job Offer screen emptying everything.
     */
    @Test
    public void saveButtonAddAnotherButtonTest() {
        DbManager.clearTable(DbHelper.JOBS_TABLE);
        resetJobOffer();

        // Add new job offer.
        onView(withId(R.id.enterJobOfferMenuButton)).perform(click());
        JobDetailsScreenTest.setInputs(DbTest.job4);

        // Click save button.
        onView(withId(R.id.saveButton)).perform(click());
        assertEquals(1, DbTest.dbManager.getJobs().size());

        // Check to see if we are still on the Job Offer Screen.
        intended(hasComponent("edu.gatech.seclass.jobcompare.screens.JobOfferScreen"));

        // Make sure snackbar does its thing.
        testSnackbar(DbTest.job4);

        // Make sure all action buttons appear, except compare button.
        onView(withId(R.id.compareButton)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        onView(withId(R.id.enterAnother)).check(matches(ViewMatchers.isDisplayed()));
        onView(withId(R.id.enterAnother)).check(matches(ViewMatchers.isEnabled()));
        onView(withId(R.id.returnToMenu)).check(matches(ViewMatchers.isDisplayed()));

        // Click Add Another.
        onView(withId(R.id.enterAnother)).perform(scrollTo(), click());

        // Check to see if we are still on the Job Offer Screen.
        intended(hasComponent("edu.gatech.seclass.jobcompare.screens.JobOfferScreen"), times(2));

        // Make sure everything is empty.
        JobDetailsScreenTest.emptyScreenTest();
    }

    @After
    public void tearDown() {
        Intents.release();
        DbTest.closeDb();
    }
}

