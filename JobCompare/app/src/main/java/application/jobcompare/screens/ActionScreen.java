package application.jobcompare.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import application.jobcompare.R;

public class ActionScreen extends AppCompatActivity implements Screen {

    protected Button saveButton;
    protected Button cancelButton;
    protected Button compareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Current Job Details Screen
        saveButton = findViewById(R.id.saveButton);
        if (saveButton != null) {
            saveButton.setOnClickListener(getSaveListener());
            saveButton.setOnFocusChangeListener(getOnFocusChangeListener());
        }

        compareButton = findViewById(R.id.compareButton);
        if (compareButton != null) {
            compareButton.setOnClickListener(getCompareListener());
            compareButton.setOnFocusChangeListener(getOnFocusChangeListener());
        }

        cancelButton = findViewById(R.id.cancelButton);
        if (cancelButton != null) {
            cancelButton.setOnClickListener(getCancelListener());
            cancelButton.setOnFocusChangeListener(getOnFocusChangeListener());
        }
    }

    public View.OnFocusChangeListener getOnFocusChangeListener() {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        };
    }

    protected View.OnClickListener getSaveListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActionScreen.this.save();
            }
        };
    }

    protected View.OnClickListener getCompareListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActionScreen.this.compare();
            }
        };
    }

    protected View.OnClickListener getCancelListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActionScreen.this.cancel();
            }
        };
    }

    @Override
    public void cancel() {
        startActivity(new Intent(ActionScreen.this, MainMenuScreen.class));
    }

    @Override
    public void save() {
        startActivity(new Intent(ActionScreen.this, MainMenuScreen.class));
    }

    public void compare() {
        this.save();
    }

    @Override
    public void onBackPressed() {
        Log.d("TEST_pressBack", "inside onBackPressed()");
        super.onBackPressed();
    }

    @Override
    public void finish() {
        Log.d("TEST_pressBack", "inside finish()");
        super.finish();
    }

    /**
     * Taken from https://stackoverflow.com/questions/4165414/how-to-hide-soft-keyboard-on-android-after-clicking-outside-edittext#answer-19828165
     * @param view
     */
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
