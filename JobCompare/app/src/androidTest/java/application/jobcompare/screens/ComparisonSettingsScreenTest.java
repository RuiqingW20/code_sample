package application.jobcompare.screens;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

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
public class ComparisonSettingsScreenTest {

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
        onView(withId(R.id.comparisonSettingsMenuButton)).perform(click());

        // Ensure stuff displays.
        onView(withId(R.id.yearlySalarySettingInput)).check(matches(ViewMatchers.isDisplayed()));
        onView(withId(R.id.yearlyBonusSettingInput)).check(matches(ViewMatchers.isDisplayed()));
        onView(withId(R.id.gymMembershipAnnualSettingInput)).check(matches(ViewMatchers.isDisplayed()));
        onView(withId(R.id.leaveTimeDaysSettingInput)).check(matches(ViewMatchers.isDisplayed()));
        onView(withId(R.id.match401kPercentageSettingInput)).check(matches(ViewMatchers.isDisplayed()));
        onView(withId(R.id.petInsuranceAnnualSettingInput)).check(matches(ViewMatchers.isDisplayed()));
        onView(withId(R.id.saveButton)).check(matches(ViewMatchers.isDisplayed()));
        onView(withId(R.id.cancelButton)).check(matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void cancelButtonTest() {
        // Navigate to settings page.
        onView(withId(R.id.comparisonSettingsMenuButton)).perform(click());

        // Enter change a setting to a different value.
        Utils.replaceTextHelper(R.id.yearlySalarySettingInput, "2");

        // Click Cancel.
        onView(withId(R.id.cancelButton)).perform(click());

        // Navigate back to settings page.
        onView(withId(R.id.comparisonSettingsMenuButton)).perform(click());

        // Check input is default value of 1.
        onView(withId(R.id.yearlySalarySettingInput)).check(matches(withText("1")));
    }

    private void testInput(int rIDInput) {
        // Navigate to settings page.
        onView(withId(R.id.comparisonSettingsMenuButton)).perform(click());

        // Enter change a setting to a different value.
        Utils.replaceTextHelper(rIDInput, "2");

        // Click Cancel.
        onView(withId(R.id.saveButton)).perform(click());

        // Navigate back to settings page.
        onView(withId(R.id.comparisonSettingsMenuButton)).perform(click());

        // Check input is default value of 1.
        onView(withId(rIDInput)).check(matches(withText("2")));

        // Return to menu.
        onView(withId(R.id.cancelButton)).perform(click());
    }

    @Test
    public void saveButtonYearlySalaryTest() {
        testInput(R.id.yearlySalarySettingInput);
        testInput(R.id.yearlyBonusSettingInput);
        testInput(R.id.gymMembershipAnnualSettingInput);
        testInput(R.id.leaveTimeDaysSettingInput);
        testInput(R.id.match401kPercentageSettingInput);
        testInput(R.id.petInsuranceAnnualSettingInput);
    }

    @After
    public void tearDown() {
        Intents.release();
        DbTest.closeDb();
    }

}
