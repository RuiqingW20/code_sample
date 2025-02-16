package application.jobcompare.screens;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

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
public class CurrentJobDetailsScreenTest {

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
        onView(withId(R.id.jobDetailsMenuButton)).perform(click());

        JobDetailsScreenTest.emptyScreenTest();
    }

    @Test
    public void cancelButtonTest() {
        onView(withId(R.id.jobDetailsMenuButton)).perform(click());

        // Fill it all out.
        JobDetailsScreenTest.setInputs(DbTest.job1);

        // Cancel.
        onView(withId(R.id.cancelButton)).perform(click());

        // Make sure we are on Main Menu.
        intended(hasComponent("edu.gatech.seclass.jobcompare.screens.MainMenuScreen"));

        // Go back.
        onView(withId(R.id.jobDetailsMenuButton)).perform(click());

        // Make sure all is empty.
        onView(withId(R.id.titleInput)).check(matches(withText("")));
        onView(withId(R.id.companyNameInput)).check(matches(withText("")));
        onView(withId(R.id.locationInput)).check(matches(withText("")));
        onView(withId(R.id.costOfLivingIndexInput)).check(matches(withText("")));
        onView(withId(R.id.yearlySalaryInput)).check(matches(withText("")));
        onView(withId(R.id.yearlyBonusInput)).check(matches(withText("")));
        onView(withId(R.id.gymMembershipAnnualInput)).check(matches(withText("")));
        onView(withId(R.id.leaveTimeDaysInput)).check(matches(withText("")));
        onView(withId(R.id.match401kPercentageInput)).check(matches(withText("")));
        onView(withId(R.id.petInsuranceAnnualInput)).check(matches(withText("")));
    }

    @Test
    public void saveButtonTest() {
        onView(withId(R.id.jobDetailsMenuButton)).perform(click());

        // Fill it all out.
        JobDetailsScreenTest.setInputs(DbTest.job1);

        // Save.
        onView(withId(R.id.saveButton)).perform(click());

        // Go back.
        onView(withId(R.id.jobDetailsMenuButton)).perform(click());

        // Make sure all is empty.
        onView(withId(R.id.titleInput)).check(matches(withText("Software Engineer")));
        onView(withId(R.id.companyNameInput)).check(matches(withText("Microsoft")));
        onView(withId(R.id.locationInput)).check(matches(withText("Seattle, WA")));
        onView(withId(R.id.costOfLivingIndexInput)).check(matches(withText("170")));
        onView(withId(R.id.yearlySalaryInput)).check(matches(withText("160,000")));
        onView(withId(R.id.yearlyBonusInput)).check(matches(withText("6,000")));
        onView(withId(R.id.gymMembershipAnnualInput)).check(matches(withText("500")));
        onView(withId(R.id.leaveTimeDaysInput)).check(matches(withText("12")));
        onView(withId(R.id.match401kPercentageInput)).check(matches(withText("6")));
        onView(withId(R.id.petInsuranceAnnualInput)).check(matches(withText("1,000")));
    }

    @After
    public void tearDown() {
        Intents.release();
        DbTest.closeDb();
    }

}
