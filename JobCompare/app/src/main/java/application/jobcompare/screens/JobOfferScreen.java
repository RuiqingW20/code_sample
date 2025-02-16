package application.jobcompare.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import application.jobcompare.db.DbManager;
import application.jobcompare.models.Job;
import application.jobcompare.R;

public class JobOfferScreen extends JobDetailsScreen {
    private static JobOfferScreen instance;

    public static Job jobOffer;

    // Public method to get the singleton instance
    public static JobOfferScreen getInstance() {
        if (instance == null) {
            synchronized (JobOfferScreen.class) {
                if (instance == null) {
                    instance = new JobOfferScreen();
                }
            }
        }
        return instance;
    }

    public static void resetJobOffer() {
        jobOffer = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_job_details_screen);
        super.onCreate(savedInstanceState);

        TextView screenTitle = (TextView)findViewById(R.id.screenTitle);
        screenTitle.setText(getString(R.string.screen_title_job_details_job_offer));

        TextView helperText = (TextView)findViewById(R.id.helperText);
        helperText.setText(getString(R.string.screen_helper_job_details_job_offer_enter_offer_below));

        int numberOfJobs = DbManager.getInstance().getJobs().size();

        // Are we editing?
        if (jobOffer != null) {
            setValues(jobOffer);

            if (numberOfJobs > 1) {
                compareButton.setEnabled(true);
            }
        } else {
            // This is a new job offer, so if there are other jobs
            if (numberOfJobs >= 1) {
                compareButton.setEnabled(true);
            }
        }

        if (enterAnother != null) {
            enterAnother.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addAnother();
                }
            });
        }

        // @todo Remove keyboard on click of button.

    }

    public void save() {
        if (hasValidInputs()) {
            return;
        }

        Job job = this.getJobDetails();
        if (jobOffer == null) {
            job = DbManager.getInstance().addJob(job);
            if (job.getID() == 0) {
                Snackbar.make(findViewById(R.id.jobDetailsLayout), job + " " + getApplicationContext().getResources().getString(R.string.did_not_save), Snackbar.LENGTH_LONG).show();
                return;
            }
        } else {
            job.setID(jobOffer.getID());
            if (!DbManager.getInstance().updateJob(job)) {
                Snackbar.make(findViewById(R.id.jobDetailsLayout), job + " " + getApplicationContext().getResources().getString(R.string.did_not_save), Snackbar.LENGTH_LONG).show();
                return;
            }
        }
        jobOffer = job;

        // Notify user that it was saved.
        Snackbar.make(findViewById(R.id.jobDetailsLayout), job + " " + getApplicationContext().getResources().getString(R.string.saved), Snackbar.LENGTH_LONG).show();

        // Make action buttons appear.
        enterAnother.setVisibility(View.VISIBLE);
        returnToMenu.setVisibility(View.VISIBLE);

        // Conditionally enable the Compare button.
        if (DbManager.getInstance().getCurrentJob() != null) {
            compareButton.setVisibility(View.VISIBLE);
            compareButton.setEnabled(true);
        }
    }

    @Override
    public void compare() {
        if (hasValidInputs()) {
            return;
        }

        jobOffer.setSelected(true);
        DbManager.getInstance().updateJob(jobOffer);

        Job currentJob = DbManager.getInstance().getCurrentJob();
        currentJob.setSelected(true);
        DbManager.getInstance().updateJob(currentJob);
        startActivity(new Intent(JobOfferScreen.this, JobsComparisonScreen.class));
        resetJobOffer();
    }

    public void addAnother() {
        resetJobOffer();

        // Restart the activity.
        startActivity(getIntent());
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void cancel() {
        resetJobOffer();
        super.cancel();
    }

}
