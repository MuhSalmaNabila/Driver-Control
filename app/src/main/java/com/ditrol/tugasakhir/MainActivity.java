package com.ditrol.tugasakhir;

import android.content.Intent;
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
                i = new Intent(MainActivity.this, SpeedActivity.class);
                startActivity(i);
            }
        });

        // Session class instance
        session = new SessionManagement(getApplicationContext());
        /**
         * Call this function whenever you want to check user login
         * This will redirect user to LoginActivity is he is not
         * logged in
         * */
        session.checkLogin();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_howtouse){
            Intent i = null;
            i = new Intent(MainActivity.this, HowtouseActivity.class);
            startActivity(i);
        }else if(id == R.id.action_record){
            Intent i = null;
            i = new Intent(MainActivity.this, RecordActivity.class);
            startActivity(i);
        }else if(id == R.id.action_about){
            Intent i = null;
            i = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(i);
        }else if(id == R.id.action_logout){
            session.logoutUser();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
