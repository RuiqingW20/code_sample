package application.jobcompare.screens;

import android.os.Bundle;
import android.widget.EditText;

import java.util.ArrayList;

import application.jobcompare.db.DbHelper;
import application.jobcompare.db.DbManager;
import application.jobcompare.models.ComparisonSettings;
import application.jobcompare.models.Job;
import application.jobcompare.R;

public class ComparisonSettingsScreen extends ActionScreen {
    private static ComparisonSettingsScreen instance;

    EditText yearlySalarySettingInput;
    EditText yearlyBonusSettingInput;
    EditText gymMembershipAnnualSettingInput;
    EditText leaveTimeDaysSettingInput;
    EditText match401kPercentageSettingInput;
    EditText petInsuranceAnnualSettingInput;

    // Public method to get the singleton instance
    public static ComparisonSettingsScreen getInstance() {
        if (instance == null) {
            synchronized (ComparisonSettingsScreen.class) {
                if (instance == null) {
                    instance = new ComparisonSettingsScreen();
                }
            }
        }
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_comparison_settings_screen);
        super.onCreate(savedInstanceState);

        ComparisonSettings comparisonSettings = ComparisonSettings.getInstance();
        yearlySalarySettingInput = findViewById(R.id.yearlySalarySettingInput);
        yearlyBonusSettingInput = findViewById(R.id.yearlyBonusSettingInput);
        gymMembershipAnnualSettingInput = findViewById(R.id.gymMembershipAnnualSettingInput);
        leaveTimeDaysSettingInput = findViewById(R.id.leaveTimeDaysSettingInput);
        match401kPercentageSettingInput = findViewById(R.id.match401kPercentageSettingInput);
        petInsuranceAnnualSettingInput = findViewById(R.id.petInsuranceAnnualSettingInput);

        Utilities.setText(yearlySalarySettingInput, comparisonSettings.getYearlySalary());
        Utilities.setText(yearlyBonusSettingInput, comparisonSettings.getYearlyBonus());
        Utilities.setText(gymMembershipAnnualSettingInput, comparisonSettings.getGymMembershipAnnual());
        Utilities.setText(leaveTimeDaysSettingInput, comparisonSettings.getLeaveTimeDays());
        Utilities.setText(match401kPercentageSettingInput, comparisonSettings.getMatch401kPercentage());
        Utilities.setText(petInsuranceAnnualSettingInput, comparisonSettings.getPetInsuranceAnnual());
    }

    public void save() {
        boolean recalculate = false;
        // Get the current settings.
        ComparisonSettings settings = ComparisonSettings.getInstance();

        // Get the settings from the fields.
        if (maybe_save(DbHelper.YEARLY_SALARY, settings.getYearlySalary(), Utilities.getTextAsInt(yearlySalarySettingInput))) {
            recalculate = true;
            settings.setYearlySalary(Utilities.getTextAsInt(yearlySalarySettingInput));
        }

        if (maybe_save(DbHelper.YEARLY_BONUS, settings.getYearlyBonus(), Utilities.getTextAsInt(yearlyBonusSettingInput))) {
            recalculate = true;
            settings.setYearlyBonus(Utilities.getTextAsInt(yearlyBonusSettingInput));
        }

        if (maybe_save(DbHelper.GYM_MEMBERSHIP_ANNUAL, settings.getGymMembershipAnnual(), Utilities.getTextAsInt(gymMembershipAnnualSettingInput))) {
            recalculate = true;
            settings.setGymMembershipAnnual(Utilities.getTextAsInt(gymMembershipAnnualSettingInput));
        }

        if (maybe_save(DbHelper.LEAVE_TIME_DAYS, settings.getLeaveTimeDays(), Utilities.getTextAsInt(leaveTimeDaysSettingInput))) {
            recalculate = true;
            settings.setLeaveTimeDays(Utilities.getTextAsInt(leaveTimeDaysSettingInput));
        }

        if (maybe_save(DbHelper.MATCH_401_PERCENTAGE, settings.getMatch401kPercentage(), Utilities.getTextAsInt(match401kPercentageSettingInput))) {
            recalculate = true;
            settings.setMatch401kPercentage(Utilities.getTextAsInt(match401kPercentageSettingInput));
        }

        if (maybe_save(DbHelper.PET_INSURANCE_ANNUAL, settings.getPetInsuranceAnnual(), Utilities.getTextAsInt(petInsuranceAnnualSettingInput))) {
            recalculate = true;
            settings.setPetInsuranceAnnual(Utilities.getTextAsInt(petInsuranceAnnualSettingInput));
        }
        if (recalculate) {
            DbManager dbManager = DbManager.getInstance();

            // Recalculate all job scores.
            ArrayList<Job> jobs = dbManager.getJobs();
            for (Job job: jobs) {
                job.setScore(Job.calculateScore(job));
                dbManager.updateJob(job);
            }
        }

        super.save();
    }

    private boolean maybe_save(String settingName, int currentValue, int newValue) {
        if (newValue != currentValue) {
            return DbManager.getInstance().updateComparisonSetting(settingName, newValue);
        }
        return false;
    }
}
