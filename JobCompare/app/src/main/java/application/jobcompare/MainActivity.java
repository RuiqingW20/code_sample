package application.jobcompare;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import application.jobcompare.db.DbManager;
import application.jobcompare.screens.MainMenuScreen;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DbManager dbManager;

        // Setup a global DBManager.
        // This isn't the best method per se but will work for the class.
        dbManager = DbManager.getInstance();
        dbManager.setContext(getApplicationContext());
        dbManager.resetSelected();

        // Start MainMenu Screen.
        startActivity(new Intent(MainActivity.this, MainMenuScreen.class));
    }
}
