package com.example.gregor.kantor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Kantor.java";
    public static final String DATA_PATH = Environment
            .getExternalStorageDirectory().toString() + "/Kantor/";
    protected Button buttonCompareCantors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int writePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int internetPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET);
        Log.v(TAG, "WRITE_EXTERNAL_STORAGE_Permission: " + writePermission);
        Log.v(TAG, "INTERNET_Permission: " + internetPermission);
        askForPermission();

        buttonCompareCantors = (Button)findViewById(R.id.buttonCompareCantors);
        buttonCompareCantors.setOnClickListener(buttonCompareCantorsListener);
    }

    private View.OnClickListener buttonCompareCantorsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent cantorsViewIntent = new Intent(v.getContext(), CantorsViewActivity.class);
            startActivity(cantorsViewIntent);
        }
    };

    public void askForPermission(){
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET}, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission was granted.");
                    createDirectories();
                } else {
                    Log.v(TAG, "Permission dnied.");
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private void createDirectories() {
        File dir = new File(DATA_PATH);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.v(TAG, "ERROR: Creation of directory " + DATA_PATH + " failed.");
                return;
            } else {
                Log.v(TAG, "Created directory " + DATA_PATH + " on sdcard");
            }
        } else {
            Log.v(TAG, "Directory" + DATA_PATH + " already exists.");
        }
        File newFile = new File(DATA_PATH + "result.txt");
        if (newFile.exists()){
            Log.v(TAG, "Result file already exists.");
            return;
        }
        try {
            newFile.createNewFile();
            Log.v(TAG, "Created result file.");
        } catch(IOException e){
            Log.v(TAG, e.getMessage());
        }
    }
}
