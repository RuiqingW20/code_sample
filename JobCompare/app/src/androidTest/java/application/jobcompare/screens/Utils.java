package application.jobcompare.screens;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import edu.gatech.seclass.jobcompare.db.DbHelper;
import edu.gatech.seclass.jobcompare.db.DbManager;

public class Utils {

    public static void replaceTextHelper(int viewId, String stringToBeSet) {
        // to reduce flaky test, https://stackoverflow.com/a/53430379/1326678
        onView(withId(viewId)).perform(clearText(), replaceText(stringToBeSet), closeSoftKeyboard());
    }

    public static void clearJobsTable() {
        DbManager.clearTable(DbHelper.JOBS_TABLE);
    }
}
