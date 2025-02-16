package application.jobcompare.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import application.jobcompare.db.DbManager;
import application.jobcompare.models.ComparisonSettings;
import application.jobcompare.R;

public class MainMenuScreen extends AppCompatActivity implements Screen {
    private static MainMenuScreen instance;
    private DbManager dbManager;
    private Button jobDetailsMenuButton;
    private Button enterJobOfferMenuButton;
    private Button comparisonSettingsMenuButton;
    private Button jobComparisonMenuButton;


    // Public method to get the singleton instance
    public static MainMenuScreen getInstance() {
        if (instance == null) {
            synchronized (MainMenuScreen.class) {
                if (instance == null) {
                    instance = new MainMenuScreen();
                }
            }
        }
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup DbManager.
        dbManager = DbManager.getInstance();

        // Setup Comparison Settings.
        ComparisonSettings.getInstance().setFromDB(dbManager.getSettings());

        // Setup Menu.
        createCurrentJobButton();
        createJobOfferButton();
        createComparisonSettingButton();
        createJobsComparisonButton();
    }

    protected void createCurrentJobButton() {
        // Current Job Details Screen
        jobDetailsMenuButton = findViewById(R.id.jobDetailsMenuButton);
        if (DbManager.getInstance().getCurrentJob() != null) {
            jobDetailsMenuButton.setText(getString(R.string.edit_current_job_details));
        } else {
            jobDetailsMenuButton.setText(getString(R.string.enter_current_job_details));
        }
        jobDetailsMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenuScreen.this, CurrentJobDetailsScreen.class));
            }
        });
    }

    protected void createJobOfferButton() {
        // Job Offer Screen
        enterJobOfferMenuButton = findViewById(R.id.enterJobOfferMenuButton);
        enterJobOfferMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenuScreen.this, JobOfferScreen.class));
            }
        });
    }

    protected void createComparisonSettingButton() {
        // Comparison Settings Screen
        comparisonSettingsMenuButton = findViewById(R.id.comparisonSettingsMenuButton);
        comparisonSettingsMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenuScreen.this, ComparisonSettingsScreen.class));
            }
        });
    }

    protected void createJobsComparisonButton() {
        // Jobs Comparison Screen
        jobComparisonMenuButton = findViewById(R.id.jobComparisonMenuButton);

        if (DbManager.getInstance().getJobs().size() < 2) {
            jobComparisonMenuButton.setEnabled(false);
        } else {
            jobComparisonMenuButton.setEnabled(true);
        }
        jobComparisonMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenuScreen.this, JobsListScreen.class));
            }
        });
    }

    @Override
    public void cancel() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void save() {
        setContentView(R.layout.activity_main);
    }
}
