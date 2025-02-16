package application.jobcompare.screens;

import android.os.Bundle;
import android.widget.TextView;

import application.jobcompare.db.DbManager;
import application.jobcompare.models.Job;
import application.jobcompare.R;

public class CurrentJobDetailsScreen extends JobDetailsScreen {
    private static CurrentJobDetailsScreen instance;

    // Public method to get the singleton instance
    public static CurrentJobDetailsScreen getInstance() {
        if (instance == null) {
            synchronized (CurrentJobDetailsScreen.class) {
                if (instance == null) {
                    instance = new CurrentJobDetailsScreen();
                }
            }
        }
        return instance;
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        setContentView(R.layout.activity_job_details_screen);
//        super.onCreate(savedInstanceState);
//
//        TextView screenTitle = (TextView)findViewById(R.id.screenTitle);
//        screenTitle.setText(getString(R.string.screen_title_job_details_current_job_details));
//
//        Job currentJob = DbManager.getInstance().getCurrentJob();
//        TextView helperText = (TextView)findViewById(R.id.helperText);
//
//        if (currentJob == null) {
//            helperText.setText(getString(R.string.screen_helper_job_details_enter_current_job_details_below));
//        } else {
//            helperText.setText(getString(R.string.screen_helper_job_details_edit_current_job_details_below));
//
//            setValues(currentJob);
//        }
//    }

    public void save() {
        if (hasValidInputs()) {
            return;
        }

        Job currentJob = DbManager.getInstance().getCurrentJob();

        // Check to see if we are updating.
        if (currentJob != null) {
            // Updating current job.
            currentJob.setTitle(Utilities.getText(titleInput));
            currentJob.setCompanyName(Utilities.getText(companyNameInput));
            currentJob.setLocation(Utilities.getText(locationInput));
            currentJob.setCostOfLivingIndex(Utilities.getTextAsInt(costOfLivingIndexInput));
            currentJob.setYearlySalary(Utilities.getTextAsInt(yearlySalaryInput));
            currentJob.setYearlyBonus(Utilities.getTextAsInt(yearlyBonusInput));
            currentJob.setGymMembershipAnnual(Utilities.getTextAsInt(gymMembershipAnnualInput));
            currentJob.setLeaveTimeDays(Utilities.getTextAsInt(leaveTimeDaysInput));
            currentJob.setMatch401kPercentage(Utilities.getTextAsInt(match401kPercentageInput));
            currentJob.setPetInsuranceAnnual(Utilities.getTextAsInt(petInsuranceAnnualInput));
            currentJob.setAsCurrentJob();

            DbManager.getInstance().updateJob(currentJob);
        } else {
            // Adding new current job.
            Job job = this.getJobDetails();
            job.setAsCurrentJob();
            DbManager.getInstance().addJob(job);
        }

        super.save();
    }
}
