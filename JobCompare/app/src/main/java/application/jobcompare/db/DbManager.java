package application.jobcompare.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import application.jobcompare.models.ComparisonSetting;
import application.jobcompare.models.Job;


public class DbManager {

    private static DbManager instance;

    private DbHelper databaseHelper;

    private Context context;

    private SQLiteDatabase database;

    public static DbManager getInstance() {
        if (instance == null) {
            synchronized (DbManager.class) {
                if (instance == null) {
                    instance = new DbManager();
                }
            }
        }
        return instance;
    }

    public DbManager() {
    }

    public void setContext(Context ctx) {
        this.context = ctx;
    }

    public DbManager open() throws SQLException {
        if (databaseHelper == null) {
            databaseHelper = new DbHelper(context);
        }
        if (database == null || !database.isOpen()) {
            database = databaseHelper.getWritableDatabase();
        }

        return this;
    }

    public DbHelper getDatabaseHelper() {
        return databaseHelper;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    /**
     * Adds jobs to JOBS_TABLE.
     *
     * @param job Job to add.
     * @return Job that was added with DB ID.
     */
    public Job addJob(Job job) {
        this.open();
        ContentValues values = new ContentValues();
        if (job.getID() != 0) {
            values.put(DbHelper.ID, job.getID());
        }
        values.put(DbHelper.TITLE, job.getTitle());
        values.put(DbHelper.COMPANY_NAME, job.getCompanyName());
        values.put(DbHelper.LOCATION, job.getLocation());
        values.put(DbHelper.COST_OF_LIVING_INDEX, job.getCostOfLivingIndex());
        values.put(DbHelper.YEARLY_SALARY, job.getYearlySalary());
        values.put(DbHelper.YEARLY_BONUS, job.getYearlyBonus());
        values.put(DbHelper.GYM_MEMBERSHIP_ANNUAL, job.getGymMembershipAnnual());
        values.put(DbHelper.LEAVE_TIME_DAYS, job.getLeaveTimeDays());
        values.put(DbHelper.MATCH_401_PERCENTAGE, job.getMatch401kPercentage());
        values.put(DbHelper.PET_INSURANCE_ANNUAL, job.getPetInsuranceAnnual());
        values.put(DbHelper.CURRENT_JOB, job.isCurrentJob());
        values.put(DbHelper.SCORE, job.getScore());
        values.put(DbHelper.IS_SELECTED, job.isSelected());
        long id = database.insert(DbHelper.JOBS_TABLE, null, values);
        job.setID(id);

//        database.close();
        return job;
    }

    /**
     * Updates a job in JOBS_TABLE.
     *
     * @param job Job to update.
     * @return Whether job updated.
     */
    public boolean updateJob(Job job) {
        this.open();
        ContentValues values = new ContentValues();
        values.put(DbHelper.TITLE, job.getTitle());
        values.put(DbHelper.COMPANY_NAME, job.getCompanyName());
        values.put(DbHelper.LOCATION, job.getLocation());
        values.put(DbHelper.COST_OF_LIVING_INDEX, job.getCostOfLivingIndex());
        values.put(DbHelper.YEARLY_SALARY, job.getYearlySalary());
        values.put(DbHelper.YEARLY_BONUS, job.getYearlyBonus());
        values.put(DbHelper.GYM_MEMBERSHIP_ANNUAL, job.getGymMembershipAnnual());
        values.put(DbHelper.LEAVE_TIME_DAYS, job.getLeaveTimeDays());
        values.put(DbHelper.MATCH_401_PERCENTAGE, job.getMatch401kPercentage());
        values.put(DbHelper.PET_INSURANCE_ANNUAL, job.getPetInsuranceAnnual());
        values.put(DbHelper.CURRENT_JOB, job.isCurrentJob());
        values.put(DbHelper.SCORE, job.getScore());
        values.put(DbHelper.IS_SELECTED, job.isSelected());

        int updated = database.update(DbHelper.JOBS_TABLE, values,
                DbHelper.ID + " = " + job.getID(), null);

//        database.close();
        if (updated > 0) {
            return true;
        }

        return false;
    }

    /**
     * Gets all jobs.
     *
     * @return All Jobs.
     */
    public ArrayList<Job> getJobs() {
        ArrayList<Job> jobs = new ArrayList<>();

        this.open();

        Cursor cursor = database.query(DbHelper.JOBS_TABLE, null, null, null, null, null, DbHelper.SCORE + " DESC");

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Job job = readJob(cursor);
                jobs.add(job);
            }
        }

        return jobs;
    }

    /**
     * Gets selected jobs.
     *
     * @return Selected Jobs.
     */
    public ArrayList<Job> getSelectedJobs() {
        ArrayList<Job> jobs = new ArrayList<>();

        this.open();

        Cursor cursor = database.query(DbHelper.JOBS_TABLE, null,
                DbHelper.IS_SELECTED + "=true",
                null, null, null, DbHelper.SCORE + " DESC");

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Job job = readJob(cursor);
                jobs.add(job);
            }
        }

        return jobs;
    }

    @SuppressLint("Range")
    private Job readJob(Cursor cursor) {
        Job job = new Job();
        job.setID(cursor.getInt(cursor.getColumnIndex(DbHelper.ID)));
        job.setTitle(cursor.getString(cursor.getColumnIndex(DbHelper.TITLE)));
        job.setCompanyName(cursor.getString(cursor.getColumnIndex(DbHelper.COMPANY_NAME)));
        job.setLocation(cursor.getString(cursor.getColumnIndex(DbHelper.LOCATION)));
        job.setCostOfLivingIndex(cursor.getInt(cursor.getColumnIndex(DbHelper.COST_OF_LIVING_INDEX)));
        job.setYearlySalary(cursor.getInt(cursor.getColumnIndex(DbHelper.YEARLY_SALARY)));
        job.setYearlyBonus(cursor.getInt(cursor.getColumnIndex(DbHelper.YEARLY_BONUS)));
        job.setGymMembershipAnnual(cursor.getInt(cursor.getColumnIndex(DbHelper.GYM_MEMBERSHIP_ANNUAL)));
        job.setLeaveTimeDays(cursor.getInt(cursor.getColumnIndex(DbHelper.LEAVE_TIME_DAYS)));
        job.setMatch401kPercentage(cursor.getInt(cursor.getColumnIndex(DbHelper.MATCH_401_PERCENTAGE)));
        job.setPetInsuranceAnnual(cursor.getInt(cursor.getColumnIndex(DbHelper.PET_INSURANCE_ANNUAL)));
        if (cursor.getInt(cursor.getColumnIndex(DbHelper.CURRENT_JOB)) == 1) {
            job.setAsCurrentJob();
        }
        if (cursor.getInt(cursor.getColumnIndex(DbHelper.IS_SELECTED)) == 1) {
            job.setSelected(true);
        }
        double score = cursor.getDouble(cursor.getColumnIndex(DbHelper.SCORE));
        if (score ==0) {
            job.setScore(Job.calculateScore(job));
        } else {
            job.setScore(score);
        }

        return job;
    }

    /**
     * Gets the current job and limits one current job to only 1.
     *
     * @return Job
     */
    public Job getCurrentJob() {
        this.open();
        Cursor cursor = database.query(DbHelper.JOBS_TABLE,
                null,
                DbHelper.CURRENT_JOB + "=true",
                null, null, null, null, "1");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Job job = readJob(cursor);
                return job;
            }
        }

        return null;
    }

    /**
     * Deletes a Job by ID.
     *
     * @param id
     */
    public boolean deleteJob(long id) {
        this.open();
        int rowsAffected = database.delete(DbHelper.JOBS_TABLE, DbHelper.ID + "=" + id, null);
//        database.close();
        if (rowsAffected > 0) {
            return true;
        }

        return false;
    }

    /**
     * Resets all jobs to not selected.
     *
     * @return Whether reset was successful.
     */
    public boolean resetSelected() {
        this.open();
        try {
            database.execSQL("UPDATE " + DbHelper.JOBS_TABLE + " SET " + DbHelper.IS_SELECTED + " = false;");
//            database.close();
            return true;
        } catch (SQLException e) {
//            database.close();
            return false;
        }
    }

    public static void clearTable(String table) {
        SQLiteDatabase db = DbManager.getInstance().getDatabase();
        db.delete(table, null, null);
    }

    /**
     * Gets all COMPARISON_SETTINGS_TABLE settings.
     *
     * @return Settings object.
     */
    public ArrayList<ComparisonSetting> getSettings() {
        ArrayList<ComparisonSetting> comparisonSettings = new ArrayList<>();

        this.open();

        Cursor cursor = database.query(DbHelper.COMPARISON_SETTINGS_TABLE, null, null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                ComparisonSetting setting = readSetting(cursor);
                comparisonSettings.add(setting);
            }
        }

        return comparisonSettings;
    }

    @SuppressLint("Range")
    private ComparisonSetting readSetting(Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndex(DbHelper.SETTING_NAME));
        int value = cursor.getInt(cursor.getColumnIndex(DbHelper.SETTING_VALUE));

        return new ComparisonSetting(name, value);
    }

    /**
     * Updates COMPARISON_SETTINGS_TABLE based on a setting name.
     *
     * @param name  Name of the setting.
     * @param value New value of the setting.
     * @return Whether update was successful.
     */
    public boolean updateComparisonSetting(String name, int value) {
        this.open();

        // Create a ContentValues object to hold the new value.
        ContentValues values = new ContentValues();
        values.put("value", value);

        // Define the WHERE clause to find the row with the specified name.
        String whereClause = "name=?";
        String[] whereArgs = {name};

        try {
            // Update the row with the new value.
            int numRowsUpdated = database.update(DbHelper.COMPARISON_SETTINGS_TABLE, values, whereClause, whereArgs);

            // Check if the update was successful.
            if (numRowsUpdated > 0) {
                // Row updated successfully.
                return true;
            } else {
                // No rows were updated (no row with the specified name was found).
                return false;
            }
        } catch (SQLException e) {
            // Handle any exceptions here.
            Log.e("db", e.getStackTrace().toString());
            return false;
        } finally {
            // Close the database.
//            this.close();
            return true;
        }
    }
}

