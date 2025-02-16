package application.jobcompare.screens;

import static application.jobcompare.screens.Utilities.addTextWatcher;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import application.jobcompare.models.Errors;
import application.jobcompare.models.Job;
import application.jobcompare.R;

public class JobDetailsScreen extends ActionScreen {
    private static JobDetailsScreen instance;

    protected Job job;

    LinearLayout mainLayout;

    EditText titleInput;
    EditText companyNameInput;
    EditText locationInput;
    EditText costOfLivingIndexInput;
    EditText yearlySalaryInput;
    EditText yearlyBonusInput;
    EditText gymMembershipAnnualInput;
    EditText leaveTimeDaysInput;
    EditText match401kPercentageInput;
    EditText petInsuranceAnnualInput;

    Button enterAnother, returnToMenu;

    // Public method to get the singleton instance
    public static JobDetailsScreen getInstance() {
        if (instance == null) {
            synchronized (JobDetailsScreen.class) {
                if (instance == null) {
                    instance = new JobDetailsScreen();
                }
            }
        }
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_job_details_screen);
        super.onCreate(savedInstanceState);

        // Layout
        mainLayout = findViewById(R.id.jobDetailsLayout);

        // Buttons
        enterAnother = findViewById(R.id.enterAnother);
        returnToMenu = findViewById(R.id.returnToMenu);

        // Hide Action Buttons.
        compareButton.setVisibility(View.INVISIBLE);
        enterAnother.setVisibility(View.INVISIBLE);
        returnToMenu.setVisibility(View.INVISIBLE);

        // Wire Buttons.
        returnToMenu.setOnClickListener(getCancelListener());

        // Modify the text.
        TextView screenTitle = (TextView) findViewById(R.id.screenTitle);
        screenTitle.setText("Job Details Screen");

        TextView helperText = (TextView) findViewById(R.id.helperText);
        helperText.setText("Enter Job Details Below");

        titleInput = findViewById(R.id.titleInput);
        companyNameInput = findViewById(R.id.companyNameInput);
        locationInput = findViewById(R.id.locationInput);
        costOfLivingIndexInput = findViewById(R.id.costOfLivingIndexInput);
        yearlySalaryInput = findViewById(R.id.yearlySalaryInput);
        yearlyBonusInput = findViewById(R.id.yearlyBonusInput);
        gymMembershipAnnualInput = findViewById(R.id.gymMembershipAnnualInput);
        leaveTimeDaysInput = findViewById(R.id.leaveTimeDaysInput);
        match401kPercentageInput = findViewById(R.id.match401kPercentageInput);
        petInsuranceAnnualInput = findViewById(R.id.petInsuranceAnnualInput);

        addTextWatcher(yearlySalaryInput);
        addTextWatcher(yearlyBonusInput);
        addTextWatcher(petInsuranceAnnualInput);
    }

    /**
     * Validates all the inputs.
     *
     * @return
     */
    public boolean hasValidInputs() {
        boolean hasErrors = false;

        if (!isValidInput(titleInput)) {
            hasErrors = true;
        }

        if (!isValidInput(companyNameInput)) {
            hasErrors = true;
        }

        if (!isValidInput(locationInput)) {
            hasErrors = true;
        }

        if (!isValidInput(costOfLivingIndexInput, -1)) {
            hasErrors = true;
        }

        if (!isValidInput(yearlySalaryInput, -1)) {
            hasErrors = true;
        }

        if (!isValidInput(yearlyBonusInput, -1)) {
            hasErrors = true;
        }

        if (!isValidInput(gymMembershipAnnualInput, 500)) {
            hasErrors = true;
        }

        if (!isValidInput(leaveTimeDaysInput, -1)) {
            hasErrors = true;
        }

        if (!isValidInput(match401kPercentageInput, 20)) {
            hasErrors = true;
        }

        if (!isValidInput(petInsuranceAnnualInput, 5000)
        ) {
            hasErrors = true;
        }

        return hasErrors;
    }

    public void setValues(Job job) {
        Utilities.setText(titleInput, job.getTitle());
        Utilities.setText(companyNameInput, job.getCompanyName());
        Utilities.setText(locationInput, job.getLocation());
        Utilities.setText(costOfLivingIndexInput, job.getCostOfLivingIndex());
        Utilities.setText(yearlySalaryInput, job.getYearlySalary());
        Utilities.setText(yearlyBonusInput, job.getYearlyBonus());
        Utilities.setText(gymMembershipAnnualInput, job.getGymMembershipAnnual());
        Utilities.setText(leaveTimeDaysInput, job.getLeaveTimeDays());
        Utilities.setText(match401kPercentageInput, job.getMatch401kPercentage());
        Utilities.setText(petInsuranceAnnualInput, job.getPetInsuranceAnnual());
    }

    public boolean isValidInput(EditText editText) {
        String value = Utilities.getText(editText);
        if (value == null || value.isEmpty()) {
            editText.setError(Errors.CANNOT_BE_EMPTY);
            return false;
        }

        return true;
    }

    public boolean isValidInput(EditText editText, int maxValue) {
        if (Utilities.getText(editText) == null) {
            editText.setError(Errors.CANNOT_BE_EMPTY);
            return false;
        }

        int value = Utilities.getTextAsInt(editText);
        if (value <= 0) {
            editText.setError(Errors.CANNOT_BE_ZERO_OR_NEGATIVE);
            return false;
        }

        if (maxValue > 0 && value > maxValue) {
            editText.setError(Errors.EXCEEDS_MAX_VALUE + " " + maxValue);
            return false;
        }

        return true;
    }

    public Job getJobDetails() {
        return new Job(
                Utilities.getText(titleInput),
                Utilities.getText(companyNameInput),
                Utilities.getText(locationInput),
                Utilities.getTextAsInt(costOfLivingIndexInput),
                Utilities.getTextAsInt(yearlySalaryInput),
                Utilities.getTextAsInt(yearlyBonusInput),
                Utilities.getTextAsInt(gymMembershipAnnualInput),
                Utilities.getTextAsInt(leaveTimeDaysInput),
                Utilities.getTextAsInt(match401kPercentageInput),
                Utilities.getTextAsInt(petInsuranceAnnualInput),
                false
        );

    }
}
