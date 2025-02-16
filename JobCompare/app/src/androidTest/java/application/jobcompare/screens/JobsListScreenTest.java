package application.jobcompare.screens;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

import android.view.View;
import android.widget.CheckBox;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import application.jobcompare.db.DbTest;
import edu.gatech.seclass.jobcompare.MainActivity;


@RunWith(AndroidJUnit4.class)
public class JobsListScreenTest {

    @Rule
    public ActivityScenarioRule<MainActivity> tActivityRule = new ActivityScenarioRule<>(
            MainActivity.class);

    @Before
    public void setUp() {
        Intents.init();
        DbTest.resetDb();

        // Add jobs
        DbTest.dbManager.addJob(DbTest.job1);
        DbTest.dbManager.addJob(DbTest.job2);
        DbTest.dbManager.addJob(DbTest.job3);
        assertEquals(3, DbTest.dbManager.getJobs().size());
    }

    @Test
    public void navigationTest() {
        // Navigate to the Jobs List Screen.
        onView(withId(id.jobComparisonMenuButton)).perform(click());

        // Make sure the jobs list is available.
        onView(withId(id.listView)).check(matches(ViewMatchers.isDisplayed()));

        // Make sure we have 3 items in a specific order.
        onView(withId(id.listView)).check(new RecyclerViewItemCountAssertion(3));
        onView(ViewMatchers.withId(id.listView)).check(atPosition(0, hasDescendant(withText("Microsoft"))));
        onView(ViewMatchers.withId(id.listView)).check(atPosition(1, hasDescendant(withText("Amazon"))));
        onView(ViewMatchers.withId(id.listView)).check(atPosition(2, hasDescendant(withText("IBM"))));

        // Make sure the buttons appear.
        onView(withId(id.compareButton)).check(matches(ViewMatchers.isDisplayed()));
        onView(withId(id.cancelButton)).check(matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void cancelButtonTest1() {
        // Navigate to the Jobs List Screen.
        onView(withId(id.jobComparisonMenuButton)).perform(click());

        // Click cancel button.
        onView(withId(id.cancelButton)).perform(click());

        // Make sure we are back to Main Menu.
        intended(hasComponent("edu.gatech.seclass.jobcompare.screens.MainMenuScreen"));

    }

    @Test
    public void cancelButtonTest2() {
        // 1. Check Job 1 => CANCEL
        // No jobs checked
        // Navigate to the Jobs List Screen.
        onView(withId(id.jobComparisonMenuButton)).perform(click());

        // Check jobs.
        onView(ViewMatchers.withId(id.listView)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(ViewMatchers.withId(id.listView)).check(itemAtPositionHasCheckboxState(1, true));

        // Click cancel button.
        onView(withId(id.cancelButton)).perform(click());

        // Make sure we are back to Main Menu.
        intended(hasComponent("edu.gatech.seclass.jobcompare.screens.MainMenuScreen"));

        // Go back.
        onView(withId(id.jobComparisonMenuButton)).perform(click());

        // Item should not be checked.
        onView(ViewMatchers.withId(id.listView)).check(itemAtPositionHasCheckboxState(1, false));
    }

    @Test
    public void compareJobsButtonAppearsTest() {
        // 1. Check Job 1
        // 2. Check Job 2
        //      COMPARE JOBS shows

        // Navigate to the Jobs List Screen.
        onView(withId(id.jobComparisonMenuButton)).perform(click());

        // Check jobs.
        onView(ViewMatchers.withId(id.listView)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(ViewMatchers.withId(id.listView)).check(itemAtPositionHasCheckboxState(1, true));
        onView(ViewMatchers.withId(id.listView)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));
        onView(ViewMatchers.withId(id.listView)).check(itemAtPositionHasCheckboxState(2, true));

        // Check compare button is displayed and enabled.
        onView(withId(id.compareButton)).check(matches(ViewMatchers.isDisplayed()));
        onView(withId(id.compareButton)).check(matches(ViewMatchers.isEnabled()));
    }

    @Test
    public void compareJobsButtonTest() {
        // Navigate to the Jobs List Screen.
        onView(withId(id.jobComparisonMenuButton)).perform(click());

        // check job 1
        onView(withId(id.listView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // check job 2
        onView(withId(id.listView)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        // compare jobs
        onView(withId(id.compareButton)).perform(click());
        intended(hasComponent("edu.gatech.seclass.jobcompare.screens.JobsComparisonScreen"));
    }

    @Test
    public void deleteJobSwipeTest() {
        // Navigate to the Jobs List Screen.
        onView(withId(id.jobComparisonMenuButton)).perform(click());

        // check three jobs exist
        assertEquals(3, DbTest.dbManager.getJobs().size());

        // delete job (swipe left)
        onView(withId(id.listView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, swipeLeft()));

        // check two jobs left
        assertEquals(2, DbTest.dbManager.getJobs().size());
    }

    @Test
    public void editJobSwipeTest() {
        // Navigate to the Jobs List Screen.
        onView(withId(id.jobComparisonMenuButton)).perform(click());

        // check three jobs exist
        assertEquals(3, DbTest.dbManager.getJobs().size());

        // edit job (swipe right)
        onView(withId(id.listView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, swipeRight()));

        // check we are at the edit job offer screen
        intended(hasComponent("edu.gatech.seclass.jobcompare.screens.JobOfferScreen"));
    }

    @After
    public void tearDown() {
        Intents.release();
        DbTest.closeDb();
    }

    public static ViewAssertion atPosition(final int position, final Matcher<View> itemMatcher) {
        return new ViewAssertion() {
            @Override
            public void check(View view, NoMatchingViewException noViewFoundException) {
                if (noViewFoundException != null) {
                    throw noViewFoundException;
                }

                RecyclerView recyclerView = (RecyclerView) view;
                RecyclerView.Adapter adapter = recyclerView.getAdapter();
                int itemCount = adapter.getItemCount();
                if (position < 0 || position >= itemCount) {
                    throw new AssertionError("Position " + position + " is not valid for the adapter with item count " + itemCount);
                }

                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    throw new AssertionError("No view is at position " + position);
                }

                Matcher<View> itemViewMatcher = new TypeSafeMatcher<View>() {
                    @Override
                    protected boolean matchesSafely(View view) {
                        return itemMatcher.matches(view);
                    }

                    @Override
                    public void describeTo(Description description) {
                        description.appendText("item at position " + position + " matches: ");
                        itemMatcher.describeTo(description);
                    }
                };

                assertThat(viewHolder.itemView, itemViewMatcher);
            }
        };
    }

    /**
     * Taken from https://stackoverflow.com/questions/36399787/how-to-count-recyclerview-items-with-espresso
     */
    public class RecyclerViewItemCountAssertion implements ViewAssertion {
        private final int expectedCount;

        public RecyclerViewItemCountAssertion(int expectedCount) {
            this.expectedCount = expectedCount;
        }

        @Override
        public void check(View view, NoMatchingViewException noViewFoundException) {
            if (noViewFoundException != null) {
                throw noViewFoundException;
            }

            RecyclerView recyclerView = (RecyclerView) view;
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            assertThat(adapter.getItemCount(), is(expectedCount));
        }
    }

    public static ViewAssertion itemAtPositionHasCheckboxState(int position, boolean isChecked) {
        return (view, noViewFoundException) -> {
            RecyclerView recyclerView = (RecyclerView) view;

            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
            if (viewHolder == null) {
                throw new NoMatchingViewException.Builder()
                        .withViewMatcher(Matchers.allOf(ViewMatchers.isAssignableFrom(RecyclerView.class), ViewMatchers.withId(recyclerView.getId())))
                        .withRootView(recyclerView)
                        .build();
            }

            Matcher<View> checkboxMatcher = new TypeSafeMatcher<View>() {
                @Override
                protected boolean matchesSafely(View view) {
                    if (view instanceof CheckBox) {
                        CheckBox checkBox = (CheckBox) view;
                        if (isChecked) {
                            return checkBox.isChecked();
                        } else {
                            return !checkBox.isChecked();
                        }
                    }
                    return false;
                }

                @Override
                public void describeTo(Description description) {
                    if (isChecked) {
                        description.appendText("item at position " + position + " has a checked checkbox");
                    } else {
                        description.appendText("item at position " + position + " has an unchecked checkbox");
                    }
                }
            };

            assertThat(((ConstraintLayout) viewHolder.itemView).getChildAt(0).findViewById(id.jobsIsSelected), checkboxMatcher);
        };
    }
}
