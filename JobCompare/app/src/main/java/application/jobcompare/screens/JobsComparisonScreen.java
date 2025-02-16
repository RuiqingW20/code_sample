package application.jobcompare.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

import application.jobcompare.db.DbManager;
import application.jobcompare.models.Job;
import application.jobcompare.R;

public class JobsComparisonScreen extends ActionScreen {
    private static JobsComparisonScreen instance;

    TextView titleJob1, titleJob2;
    TextView companyNameJob1, companyNameJob2;
    TextView locationJob1, locationJob2;
    TextView yearlySalaryJob1, yearlySalaryJob2;
    TextView yearlyBonusJob1, yearlyBonusJob2;
    TextView gymMembershipAnnualJob1, gymMembershipAnnualJob2;
    TextView leaveTimeDaysJob1, leaveTimeDaysJob2;
    TextView match401kPercentageJob1, match401kPercentageJob2;
    TextView petInsuranceAnnualJob1, petInsuranceAnnualJob2;

    // Public method to get the singleton instance
    public static JobsComparisonScreen getInstance() {
        if (instance == null) {
            synchronized (JobsComparisonScreen.class) {
                if (instance == null) {
                    instance = new JobsComparisonScreen();
                }
            }
        }
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_jobs_comparison_screen);

        // Buttons done by super.
        super.onCreate(savedInstanceState);

        ArrayList<Job> jobs = DbManager.getInstance().getSelectedJobs();

        // Make sure we have 2!
        if (jobs.size() != 2) {
            // @todo Maybe toast a Snackbar for a better UI.
            cancel();
        }

        Job job1 = jobs.get(0);

        // Set Job 1.
        titleJob1 = findViewById(R.id.titleJob1);
        titleJob1.setText(job1.getTitle());
        companyNameJob1 = findViewById(R.id.companyNameJob1);
        companyNameJob1.setText(job1.getCompanyName());
        locationJob1 = findViewById(R.id.locationJob1);
        locationJob1.setText(job1.getLocation());
        yearlySalaryJob1 = findViewById(R.id.yearlySalaryJob1);
        yearlySalaryJob1.setText(Job.format(job1.getYearlySalary()));
        yearlyBonusJob1 = findViewById(R.id.yearlyBonusJob1);
        yearlyBonusJob1.setText(Job.format(job1.getYearlyBonus()));
        gymMembershipAnnualJob1 = findViewById(R.id.gymMembershipAnnualJob1);
        gymMembershipAnnualJob1.setText(Job.format(job1.getGymMembershipAnnual()));
        leaveTimeDaysJob1 = findViewById(R.id.leaveTimeDaysJob1);
        leaveTimeDaysJob1.setText(Job.format(job1.getLeaveTimeDays()));
        match401kPercentageJob1 = findViewById(R.id.match401kPercentageJob1);
        match401kPercentageJob1.setText(Job.format(job1.getMatch401kPercentage()));
        petInsuranceAnnualJob1 = findViewById(R.id.petInsuranceAnnualJob1);
        petInsuranceAnnualJob1.setText(Job.format(job1.getPetInsuranceAnnual()));

        // Set Job 2.
        Job job2 = jobs.get(1);
        titleJob2 = findViewById(R.id.titleJob2);
        titleJob2.setText(job2.getTitle());
        companyNameJob2 = findViewById(R.id.companyNameJob2);
        companyNameJob2.setText(job2.getCompanyName());
        locationJob2 = findViewById(R.id.locationJob2);
        locationJob2.setText(job2.getLocation());
        yearlySalaryJob2 = findViewById(R.id.yearlySalaryJob2);
        yearlySalaryJob2.setText(Job.format(job2.getYearlySalary()));
        yearlyBonusJob2 = findViewById(R.id.yearlyBonusJob2);
        yearlyBonusJob2.setText(Job.format(job2.getYearlyBonus()));
        gymMembershipAnnualJob2 = findViewById(R.id.gymMembershipAnnualJob2);
        gymMembershipAnnualJob2.setText(Job.format(job2.getGymMembershipAnnual()));
        leaveTimeDaysJob2 = findViewById(R.id.leaveTimeDaysJob2);
        leaveTimeDaysJob2.setText(Job.format(job2.getLeaveTimeDays()));
        match401kPercentageJob2 = findViewById(R.id.match401kPercentageJob2);
        match401kPercentageJob2.setText(Job.format(job2.getMatch401kPercentage()));
        petInsuranceAnnualJob2 = findViewById(R.id.petInsuranceAnnualJob2);
        petInsuranceAnnualJob2.setText(Job.format(job2.getPetInsuranceAnnual()));
    }

    public static boolean isDisabled() {
        ArrayList<Job> jobs = DbManager.getInstance().getSelectedJobs();
        return jobs.size() != 2;
    }

    @Override
    public void save() {
        deselectJobs();
        startActivity(new Intent(JobsComparisonScreen.this, JobsListScreen.class));
    }

    @Override
    public void cancel() {
        deselectJobs();
        super.cancel();
    }

    private void deselectJobs() {
        DbManager dbManager = DbManager.getInstance();
        dbManager.resetSelected();
    }

}
