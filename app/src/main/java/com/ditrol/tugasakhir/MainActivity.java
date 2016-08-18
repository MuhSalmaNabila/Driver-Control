package com.ditrol.tugasakhir;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    // Session Management Class
    SessionManagement session;

    // Disable back button
    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton buttonProfile = (ImageButton)findViewById(R.id.ibprofil);
        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = null;
                i = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });

        ImageButton buttonMaps = (ImageButton)findViewById(R.id.ibMaps);
        buttonMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = null;
                i = new Intent(MainActivity.this, PetaActivity.class);
                startActivity(i);
            }
        });

        ImageButton buttonLock = (ImageButton)findViewById(R.id.ibLock);
        buttonLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = null;
                i = new Intent(MainActivity.this, LockActivity.class);
                startActivity(i);
            }
        });

        ImageButton buttonSpeed = (ImageButton)findViewById(R.id.ibSpeed);
        buttonSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = null;
                i = new Intent(MainActivity.this, Speed2Activity.class);
                startActivity(i);
            }
        });

        // Session class instance
        session = new SessionManagement(getApplicationContext());
        session.checkLogin();

    }
    public void showPopupLogout(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(R.layout.popup_logout);
        //alertDialogBuilder.setView(textView);

        // set dialog message
        alertDialogBuilder.setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                session.logoutUser();
            }
        });

        alertDialogBuilder.setCancelable(true).setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it

        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_howtouse){
            Intent i = null;
            i = new Intent(MainActivity.this, HowtouseActivity.class);
            startActivity(i);
        }else if(id == R.id.action_record){
            Intent i = null;
            i = new Intent(MainActivity.this, RecordNewActivity.class);
            startActivity(i);
        }else if(id == R.id.action_about){
            Intent i = null;
            i = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(i);
        }else if(id == R.id.action_logout){
            showPopupLogout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
