package application.jobcompare.db;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import application.jobcompare.screens.Utils;
import edu.gatech.seclass.jobcompare.db.DbHelper;
import edu.gatech.seclass.jobcompare.db.DbManager;
import edu.gatech.seclass.jobcompare.models.ComparisonSetting;
import edu.gatech.seclass.jobcompare.models.ComparisonSettings;
import edu.gatech.seclass.jobcompare.models.Job;

@RunWith(AndroidJUnit4.class)
public class DbTest {
    public static edu.gatech.seclass.jobcompare.db.DbManager dbManager;

    public static Job job1 = new Job("Software Engineer", "Microsoft", "Seattle, WA", 170, 160000, 6000, 500, 12, 6, 1000, true);
    public static Job job2 = new Job("Software Engineer II", "IBM", "Armonk, NY", 220, 180000, 4000, 400, 10, 10, 750, false);
    public static Job job3 = new Job("Software Engineer III", "Amazon", "Seattle, WA", 170, 250000, 30000, 300, 8, 3, 500, false);
    public static Job job4 = new Job("Software Engineer III", "Netflix", "Palo Alto, CA", 203, 300000, 30000, 200, 5, 4, 200, false);
    public static Job job5 = new Job("Software Engineer III", "Facebook", "Menlo Park, CA", 210, 275000, 90000, 100, 15, 8, 100, false);

    public static void clearDb() {
        Utils.clearJobsTable();
        dbManager.updateComparisonSetting(edu.gatech.seclass.jobcompare.db.DbHelper.YEARLY_SALARY, 1);
        dbManager.updateComparisonSetting(edu.gatech.seclass.jobcompare.db.DbHelper.YEARLY_BONUS, 1);
        dbManager.updateComparisonSetting(edu.gatech.seclass.jobcompare.db.DbHelper.GYM_MEMBERSHIP_ANNUAL, 1);
        dbManager.updateComparisonSetting(edu.gatech.seclass.jobcompare.db.DbHelper.LEAVE_TIME_DAYS, 1);
        dbManager.updateComparisonSetting(edu.gatech.seclass.jobcompare.db.DbHelper.MATCH_401_PERCENTAGE, 1);
        dbManager.updateComparisonSetting(edu.gatech.seclass.jobcompare.db.DbHelper.PET_INSURANCE_ANNUAL, 1);
    }

    public static void resetDb() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        dbManager = DbManager.getInstance();
        dbManager.setContext(appContext);
        dbManager.open();

        // Let's clear the DB.
        clearDb();
    }

    public static void closeDb() {
        if (dbManager.getDatabase() != null && dbManager.getDatabase().isOpen()) {
            dbManager.getDatabase().close();
        }
    }

    @Before
    public void create() {
        resetDb();
    }

    @Test
    public void addJobTest() {
        dbManager.addJob(job1);
        assertEquals(1, dbManager.getJobs().size());
    }

    @Test
    public void getJobsTest() {
        ArrayList<Job> jobsArray = new ArrayList<>();

        dbManager.addJob(job1);
        assertEquals(dbManager.getJobs().size(), 1);

        dbManager.addJob(job2);
        assertEquals(dbManager.getJobs().size(), 2);

        dbManager.addJob(job3);
        assertEquals(dbManager.getJobs().size(), 3);

        dbManager.addJob(job4);
        assertEquals(dbManager.getJobs().size(), 4);

        dbManager.addJob(job5);
        assertEquals(dbManager.getJobs().size(), 5);

        jobsArray = dbManager.getJobs();

        // 1,3,5,4,2
        assertEquals(jobsArray.get(0), job1);
        assertEquals(jobsArray.get(1), job3);
        assertEquals(jobsArray.get(2), job5);
        assertEquals(jobsArray.get(3), job4);
        assertEquals(jobsArray.get(4), job2);
    }

    @Test
    public void deleteJobTest() {
        dbManager.addJob(job1);
        Log.i("deleteJobTest", "added job1: " + job1.toString());
        dbManager.addJob(job2);
        Log.i("deleteJobTest", "added job2: " + job2.toString());
        assertEquals(dbManager.getJobs().size(), 2);

        // Delete Job1.
        dbManager.deleteJob(job1.getID());
        assertEquals(dbManager.getJobs().size(), 1);

        ArrayList<Job> jobsArray = dbManager.getJobs();
        assertEquals(jobsArray.get(0), job2);
    }

    @Test
    public void getSettingsTest() {
        ArrayList<ComparisonSetting> settings = dbManager.getSettings();
        assertEquals(settings.size(), 6);
    }

    @Test
    public void updateComparisonSettingTest() {
        dbManager.updateComparisonSetting(edu.gatech.seclass.jobcompare.db.DbHelper.YEARLY_SALARY, 2);

        ArrayList<ComparisonSetting> settings = dbManager.getSettings();
        assertEquals(settings.size(), 6);

        for (ComparisonSetting setting : settings) {
            if (setting.getName() == edu.gatech.seclass.jobcompare.db.DbHelper.YEARLY_SALARY) {
                assertEquals(2, setting.getValue());
            }
        }

        ComparisonSettings comparisonSettings = ComparisonSettings.getInstance().setFromDB(settings);
        assertEquals(2, comparisonSettings.getYearlySalary());
        assertEquals(7, comparisonSettings.getTotal());

        // Reset
        dbManager.updateComparisonSetting(DbHelper.YEARLY_SALARY, 1);
    }

    @Test
    public void getCurrentJobTest() {
        dbManager.addJob(job1);
        dbManager.addJob(job2);
        Job currentJob = dbManager.getCurrentJob();
        assertEquals(job1, currentJob);
    }

    @Test
    public void updateJobTest() {
        dbManager.addJob(job1);
        dbManager.addJob(job2);
        job1.removeAsCurrentJob();
        job2.setAsCurrentJob();
        dbManager.updateJob(job1);
        dbManager.updateJob(job2);

        Job currentJob = dbManager.getCurrentJob();
        assertEquals(job2, currentJob);
    }

    @Test
    public void getSortedJobsTest() {
        dbManager.addJob(job1);
        dbManager.addJob(job2);
        dbManager.addJob(job3);
        dbManager.addJob(job4);
        dbManager.addJob(job5);

        // Get sorted jobs.
        ArrayList<Job> jobsArray = dbManager.getJobs();

        // Check the sorting.
        assertEquals(jobsArray.get(0), job1);
        assertEquals(jobsArray.get(1), job3);
        assertEquals(jobsArray.get(2), job5);
        assertEquals(jobsArray.get(3), job4);
        assertEquals(jobsArray.get(4), job2);
    }

    @Test
    public void isSelectedTest() {
        job2.setSelected(true);
        dbManager.addJob(job1);
        dbManager.addJob(job2);
        dbManager.addJob(job3);
        dbManager.addJob(job4);
        dbManager.addJob(job5);

        assertEquals(dbManager.getSelectedJobs().size(), 1);
        job3.setSelected(true);
        dbManager.updateJob(job3);
        assertEquals(dbManager.getSelectedJobs().size(), 2);
    }
}
