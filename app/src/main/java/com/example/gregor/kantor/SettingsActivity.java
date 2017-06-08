package com.example.gregor.kantor;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import java.lang.String;
import android.app.PendingIntent;
import android.support.v4.app.TaskStackBuilder;



public class SettingsActivity extends AppCompatActivity {

    private Spinner spinnerRefreshCurrency;
    private Spinner spinnerNotifications;
    private Spinner spinnerMotive;
    private Button buttonSaveSettings;

    String motives[] = {"Business", "Adriana"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(MainActivity.theme ? R.style.AppTheme_Adriana : R.style.AppTheme);
        System.out.println("Adrianatheme= " + MainActivity.theme);
        setContentView(R.layout.activity_settings);

        buttonSaveSettings = (Button)findViewById(R.id.buttonSaveSettings);
        buttonSaveSettings.setOnClickListener(buttonSaveSettingsListener);

        spinnerRefreshCurrency = (Spinner) findViewById(R.id.spinnerRefreshCurrency);
        spinnerNotifications = (Spinner) findViewById(R.id.spinnerNotifications);
        spinnerMotive = (Spinner) findViewById(R.id.spinnerMotive);
        setSpinners();



    }


    private void setSpinners(){

        String[] refreshTime = new String[3];
        String[] notifications = new String[2];
        String[] motive = new String[2];

        refreshTime[0] = "5 s";
        refreshTime[1] = "10 s";
        refreshTime[2] = "15 s";

        notifications[0] = "Nie";
        notifications[1] = "Tak";

        motive[0] = motives[0];
        motive[1] = motives[1];


        ArrayAdapter adapterNotifications = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, notifications);

        ArrayAdapter adapterRefresh = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, refreshTime);

        ArrayAdapter adapterMotive = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, motive);

        spinnerRefreshCurrency.setAdapter(adapterRefresh);
        spinnerNotifications.setAdapter(adapterNotifications);
        spinnerMotive.setAdapter(adapterMotive);


    }

    private View.OnClickListener buttonSaveSettingsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String refreshTime = spinnerRefreshCurrency.getSelectedItem().toString();
            String notifications = spinnerNotifications.getSelectedItem().toString();
            String motive = spinnerMotive.getSelectedItem().toString();

            if( motive == motives[0]) MainActivity.theme = false;
            else if (motive == motives[1]) MainActivity.theme = true;

            SharedPreferences.Editor editor = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE).edit();
            editor.putBoolean("changeTheme", MainActivity.theme);
            editor.apply();

            Intent intent = getIntent();


            // Intent for the activity to open when user selects the notification
            /*
            Intent upIntent = NavUtils.getParentActivityIntent(this);
            // Use TaskStackBuilder to build the back stack and get the PendingIntent
            PendingIntent pendingIntent =
                    TaskStackBuilder.create(this)
                            // add all of DetailsActivity's parents to the stack,
                            // followed by DetailsActivity itself
                            .addNextIntentWithParentStack(upIntent)
                            .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setContentIntent(pendingIntent);
            */
            finish();
            startActivity(intent);
        }
    };

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }

}
