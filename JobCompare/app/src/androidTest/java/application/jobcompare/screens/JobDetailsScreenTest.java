package application.jobcompare.screens;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

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
import edu.gatech.seclass.jobcompare.models.Errors;
import edu.gatech.seclass.jobcompare.models.Job;


@RunWith(AndroidJUnit4.class)
public class JobDetailsScreenTest {

    @Rule
    public ActivityScenarioRule<MainActivity> tActivityRule = new ActivityScenarioRule<>(
            MainActivity.class);

    @Before
    public void setUp() {
        Intents.init();
        DbTest.resetDb();
    }

    public static void setInputs(Job job) {
        Utils.replaceTextHelper(R.id.titleInput, job.getTitle());
        Utils.replaceTextHelper(R.id.companyNameInput, job.getCompanyName());
        Utils.replaceTextHelper(R.id.locationInput, job.getLocation());
        Utils.replaceTextHelper(R.id.costOfLivingIndexInput, String.valueOf(job.getCostOfLivingIndex()));
        Utils.replaceTextHelper(R.id.yearlySalaryInput, String.valueOf(job.getYearlySalary()));
        Utils.replaceTextHelper(R.id.yearlyBonusInput, String.valueOf(job.getYearlyBonus()));
        Utils.replaceTextHelper(R.id.gymMembershipAnnualInput, String.valueOf(job.getGymMembershipAnnual()));
        Utils.replaceTextHelper(R.id.leaveTimeDaysInput, String.valueOf(job.getLeaveTimeDays()));
        Utils.replaceTextHelper(R.id.match401kPercentageInput, String.valueOf(job.getMatch401kPercentage()));
        Utils.replaceTextHelper(R.id.petInsuranceAnnualInput, String.valueOf(job.getPetInsuranceAnnual()));
    }

    public static void emptyScreenTest() {
        // Layout.
        onView(withId(R.id.jobDetailsLayout)).check(matches(ViewMatchers.isDisplayed()));

        // Inputs
        onView(withId(R.id.titleInput)).check(matches(ViewMatchers.isDisplayed()));
        onView(withId(R.id.companyNameInput)).check(matches(ViewMatchers.isDisplayed()));
        onView(withId(R.id.locationInput)).check(matches(ViewMatchers.isDisplayed()));
        onView(withId(R.id.costOfLivingIndexInput)).check(matches(ViewMatchers.isDisplayed()));
        onView(withId(R.id.yearlySalaryInput)).check(matches(ViewMatchers.isDisplayed()));
        onView(withId(R.id.yearlyBonusInput)).check(matches(ViewMatchers.isDisplayed()));
        onView(withId(R.id.gymMembershipAnnualInput)).check(matches(ViewMatchers.isDisplayed()));
        onView(withId(R.id.leaveTimeDaysInput)).check(matches(ViewMatchers.isDisplayed()));
        onView(withId(R.id.match401kPercentageInput)).check(matches(ViewMatchers.isDisplayed()));
        onView(withId(R.id.petInsuranceAnnualInput)).check(matches(ViewMatchers.isDisplayed()));

        // Buttons
        onView(withId(R.id.saveButton)).check(matches(ViewMatchers.isDisplayed()));
        onView(withId(R.id.cancelButton)).check(matches(ViewMatchers.isDisplayed()));
        onView(withId(R.id.compareButton)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        onView(withId(R.id.enterAnother)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        onView(withId(R.id.returnToMenu)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
    }

    @Test
    public void inputsTest() {
        onView(withId(R.id.jobDetailsMenuButton)).perform(click());

        // Fill it all out.
        JobDetailsScreenTest.setInputs(DbTest.job1);

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

    @Test
    public void saveButtonInvalidTest1() {
        onView(withId(R.id.jobDetailsMenuButton)).perform(click());

        // Fill it all out.
        JobDetailsScreenTest.setInputs(DbTest.job1);

        // Make some fields empty.
        Utils.replaceTextHelper(R.id.costOfLivingIndexInput, "");
        Utils.replaceTextHelper(R.id.yearlySalaryInput, "");
        Utils.replaceTextHelper(R.id.yearlyBonusInput, "");
        Utils.replaceTextHelper(R.id.gymMembershipAnnualInput, "");
        Utils.replaceTextHelper(R.id.leaveTimeDaysInput, "");
        Utils.replaceTextHelper(R.id.match401kPercentageInput, "");
        Utils.replaceTextHelper(R.id.petInsuranceAnnualInput, "");

        // Save.
        onView(withId(R.id.saveButton)).perform(click());

        // Make sure it did not save.
        assertEquals(0, DbTest.dbManager.getJobs().size());

        // Check Errors
        onView(withId(R.id.costOfLivingIndexInput)).check(matches(hasErrorText(Errors.CANNOT_BE_ZERO_OR_NEGATIVE)));
        onView(withId(R.id.yearlySalaryInput)).check(matches(hasErrorText(Errors.CANNOT_BE_ZERO_OR_NEGATIVE)));
        onView(withId(R.id.yearlyBonusInput)).check(matches(hasErrorText(Errors.CANNOT_BE_ZERO_OR_NEGATIVE)));
        onView(withId(R.id.gymMembershipAnnualInput)).check(matches(hasErrorText(Errors.CANNOT_BE_ZERO_OR_NEGATIVE)));
        onView(withId(R.id.leaveTimeDaysInput)).check(matches(hasErrorText(Errors.CANNOT_BE_ZERO_OR_NEGATIVE)));
        onView(withId(R.id.match401kPercentageInput)).check(matches(hasErrorText(Errors.CANNOT_BE_ZERO_OR_NEGATIVE)));
        onView(withId(R.id.petInsuranceAnnualInput)).check(matches(hasErrorText(Errors.CANNOT_BE_ZERO_OR_NEGATIVE)));
    }

    @Test
    public void saveButtonInvalidTest2() {
        onView(withId(R.id.jobDetailsMenuButton)).perform(click());

        // Fill it all out.
        JobDetailsScreenTest.setInputs(DbTest.job1);

        // Make some invalid
        Utils.replaceTextHelper(R.id.gymMembershipAnnualInput, "5000");
        Utils.replaceTextHelper(R.id.match401kPercentageInput, "100");
        Utils.replaceTextHelper(R.id.petInsuranceAnnualInput, "10000");

        // Save.
        onView(withId(R.id.saveButton)).perform(click());

        // Make sure it did not save.
        assertEquals(0, DbTest.dbManager.getJobs().size());

        // Check Errors
        onView(withId(R.id.gymMembershipAnnualInput)).check(matches(hasErrorText(Errors.EXCEEDS_MAX_VALUE + " 500")));
        onView(withId(R.id.match401kPercentageInput)).check(matches(hasErrorText(Errors.EXCEEDS_MAX_VALUE + " 20")));
        onView(withId(R.id.petInsuranceAnnualInput)).check(matches(hasErrorText(Errors.EXCEEDS_MAX_VALUE + " 5000")));
    }

    @After
    public void tearDown() {
        Intents.release();
        DbTest.closeDb();
    }

}
