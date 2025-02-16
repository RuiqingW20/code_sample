package application.jobcompare.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {
    /**
     * DB Name.
     */
    private static final String DATABASE_NAME = "JobsDatabase.db";

    /**
     * DB Version.
     */
    private static final int DATABASE_VERSION = 8;

    /**
     * Jobs Table.
     */
    public static final String JOBS_TABLE = "jobs";

    /**
     * `id` column for JOBS_TABLE table.
     */
    public static final String ID = "id";

    /**
     * Job Title for JOBS_TABLE table.
     */
    public static final String TITLE = "title";

    /**
     * Company Name for JOBS_TABLE table.
     */
    public static final String COMPANY_NAME = "companyName";

    /**
     * Location Name for JOBS_TABLE table.
     */
    public static final String LOCATION = "location";

    /**
     * Cost of Living Index for JOBS_TABLE table.
     */
    public static final String COST_OF_LIVING_INDEX = "costOfLivingIndex";

    /**
     * Yearly Salary column for JOBS_TABLE & COMPARISON_SETTINGS_TABLE tables.
     */
    public static final String YEARLY_SALARY = "yearlySalary";

    /**
     * Yearly Bonus column for JOBS_TABLE & COMPARISON_SETTINGS_TABLE tables.
     */
    public static final String YEARLY_BONUS = "yearlyBonus";

    /**
     * Gym Membership Annual column for JOBS_TABLE & COMPARISON_SETTINGS_TABLE tables.
     */
    public static final String GYM_MEMBERSHIP_ANNUAL = "gymMembershipAnnual";

    /**
     * Leave Time in Days column for JOBS_TABLE & COMPARISON_SETTINGS_TABLE tables.
     */
    public static final String LEAVE_TIME_DAYS = "leaveTimeDays";

    /**
     * 401k Percentage Match column for JOBS_TABLE & COMPARISON_SETTINGS_TABLE tables.
     */
    public static final String MATCH_401_PERCENTAGE = "match401Percentage";

    /**
     * Pet Insurance Annual column for JOBS_TABLE & COMPARISON_SETTINGS_TABLE tables.
     */
    public static final String PET_INSURANCE_ANNUAL = "petInsuranceAnnual";

    /**
     * Whether current job column for JOBS_TABLE table.
     */
    public static final String CURRENT_JOB = "currentJob";

    /**
     * Job score for JOBS_TABLE table.
     */
    public static final String SCORE = "score";

    /**
     * Whether job is selected for comparison for JOBS_TABLE table.
     */
    public static final String IS_SELECTED = "is_selected";

    /**
     * Comparison Settings Table.
     */
    public static final String COMPARISON_SETTINGS_TABLE = "comparison_settings";

    /**
     * Comparison Setting name for COMPARISON_SETTINGS_TABLE table.
     */
    public static final String SETTING_NAME = "name";

    /**
     * Comparison Setting value for COMPARISON_SETTINGS_TABLE table.
     */
    public static final String SETTING_VALUE = "value";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Creates the two tables accordingly.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creates the jobs table.
        createJobsTable(db);

        // Create Comparison Settings Table.
        createComparisonSettingsTable(db);
        prepopulateComparisonSettingsTable(db);
    }

    /**
     * Creates the COMPARISON_SETTINGS_TABLE.
     *
     * @param db The database.
     */
    public void createComparisonSettingsTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + COMPARISON_SETTINGS_TABLE + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SETTING_NAME + " TEXT, " +
                SETTING_VALUE + " INTEGER" +
                ");");

        if (DbUtilities.doesTableExist(db, COMPARISON_SETTINGS_TABLE)) {
            Log.i("db", DbUtilities.getTableSchema(db, COMPARISON_SETTINGS_TABLE));
        }
    }

    /**
     * Populates the COMPARISON_SETTINGS_TABLE with the default values.
     *
     * @param db The database.
     */
    public void prepopulateComparisonSettingsTable(SQLiteDatabase db) {
        // Add default comparison settings.
        long id;

        // Add YEARLY_SALARY.
        id = db.insert(DbHelper.COMPARISON_SETTINGS_TABLE, null, getComparisonSettingsValues(YEARLY_SALARY, 1));
        Log.i("settings_table", YEARLY_SALARY + ": " + id);

        // Add YEARLY_BONUS.
        id = db.insert(DbHelper.COMPARISON_SETTINGS_TABLE, null, getComparisonSettingsValues(YEARLY_BONUS, 1));
        Log.i("settings_table", YEARLY_BONUS + ": " + id);

        // Add GYM_MEMBERSHIP_ANNUAL
        id = db.insert(DbHelper.COMPARISON_SETTINGS_TABLE, null, getComparisonSettingsValues(GYM_MEMBERSHIP_ANNUAL, 1));
        Log.i("settings_table", GYM_MEMBERSHIP_ANNUAL + ": " + id);

        // Add GYM_MEMBERSHIP_ANNUAL
        id = db.insert(DbHelper.COMPARISON_SETTINGS_TABLE, null, getComparisonSettingsValues(LEAVE_TIME_DAYS, 1));
        Log.i("settings_table", LEAVE_TIME_DAYS + ": " + id);

        // Add MATCH_401_PERCENTAGE
        id = db.insert(DbHelper.COMPARISON_SETTINGS_TABLE, null, getComparisonSettingsValues(MATCH_401_PERCENTAGE, 1));
        Log.i("settings_table", MATCH_401_PERCENTAGE + ": " + id);

        // Add PET_INSURANCE_ANNUAL
        id = db.insert(DbHelper.COMPARISON_SETTINGS_TABLE, null, getComparisonSettingsValues(PET_INSURANCE_ANNUAL, 1));
        Log.i("settings_table", PET_INSURANCE_ANNUAL + ": " + id);
    }

    /**
     * Creates the JOBS_TABLE.
     *
     * @param db The database.
     */
    public void createJobsTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + JOBS_TABLE + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TITLE + " TEXT, " +
                COMPANY_NAME + " TEXT, " +
                LOCATION + " TEXT, " +
                COST_OF_LIVING_INDEX + " INTEGER, " +
                YEARLY_SALARY + " INTEGER, " +
                YEARLY_BONUS + " INTEGER, " +
                GYM_MEMBERSHIP_ANNUAL + " INTEGER, " +
                LEAVE_TIME_DAYS + " INTEGER, " +
                MATCH_401_PERCENTAGE + " INTEGER, " +
                PET_INSURANCE_ANNUAL + " INTEGER, " +
                CURRENT_JOB + " BOOLEAN, " +
                SCORE + " REAL, " +
                IS_SELECTED + " BOOLEAN" +
                ");");

        if (DbUtilities.doesTableExist(db, JOBS_TABLE)) {
            Log.i("db", DbUtilities.getTableSchema(db, JOBS_TABLE));
        }
    }

    private ContentValues getComparisonSettingsValues(String name, int value) {
        ContentValues values = new ContentValues();
        values.put(SETTING_NAME, name);
        values.put(SETTING_VALUE, value);
        return values;
    }

    /**
     * Doing nothing on upgrade of DB.
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + COMPARISON_SETTINGS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + JOBS_TABLE);
        onCreate(db);
    }

}
