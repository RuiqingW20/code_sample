package application.jobcompare.screens;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.times;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

import android.os.SystemClock;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
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
public class JobsComparisonScreenTest {

    @Rule
    public ActivityScenarioRule<MainActivity> tActivityRule = new ActivityScenarioRule<>(
            MainActivity.class);

    @Before
    public void setUp() {
        Intents.init();
        DbTest.resetDb();

        // add two jobs
        DbTest.dbManager.addJob(DbTest.job1);
        onView(withId(R.id.enterJobOfferMenuButton)).perform(click());
        JobDetailsScreenTest.setInputs(DbTest.job2);
        onView(withId(R.id.saveButton)).perform(click());
        SystemClock.sleep(3100);
        onView(withId(R.id.returnToMenu)).perform(scrollTo(), click());

        // select job comparison
        onView(withId(R.id.jobComparisonMenuButton)).perform(click());

        // check job 1
        onView(withId(R.id.listView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // check job 2
        onView(withId(R.id.listView)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        // open job comparison screen
        onView(withId(R.id.compareButton)).perform(click());
    }

    @After
    public void tearDown() {
        Intents.release();
        DbTest.closeDb();
    }

    @Test
    public void returnToMenuButtonTest() {
        intended(hasComponent("edu.gatech.seclass.jobcompare.screens.JobsComparisonScreen"));

        // return to menu
        onView(withId(R.id.cancelButton)).perform(click());
        intended(hasComponent("edu.gatech.seclass.jobcompare.screens.MainMenuScreen"), times(2));
    }

    @Test
    public void compareAnotherOfferButtonTest() {
        intended(hasComponent("edu.gatech.seclass.jobcompare.screens.JobsComparisonScreen"));

        // compare another offer
        onView(withId(R.id.saveButton)).perform(click());

        // verify return to job list
        intended(hasComponent("edu.gatech.seclass.jobcompare.screens.JobsListScreen"), times(2));
    }
}
