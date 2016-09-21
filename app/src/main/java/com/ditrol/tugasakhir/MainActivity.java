package com.ditrol.tugasakhir;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ditrol.tugasakhir.utils.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Session Management Class
    SessionManagement session;
    String sEmailUser, sIdDevice, sKodeUser;
    public static final String FIREBASE_PREFS_NAME = "FirebaseToken";

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

        LocalBroadcastManager.getInstance(this).registerReceiver(tokenReceiver, new IntentFilter("tokenReceiver"));

        // Session class instance
        session = new SessionManagement(getApplicationContext());

        HashMap<String, String> user = session.getUserDetails();
        String emailUser = user.get(SessionManagement.KEY_EMAIL);
        sEmailUser = emailUser;
        String kodeUser = user.get(SessionManagement.KEY_KODE_USER);
        sKodeUser = kodeUser;

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
                if(sKodeUser.equals("10234")){
                    Intent i = null;
                    i = new Intent(MainActivity.this, PetaActivity.class);
                    startActivity(i);
                }else{
                    Toast.makeText(MainActivity.this, "Silahkan isi kode user pada menu profil sebelum menggunakan layanan aplikasi", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageButton buttonLock = (ImageButton)findViewById(R.id.ibLock);
        buttonLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sKodeUser.equals("10234")){
                    Intent i = null;
                    i = new Intent(MainActivity.this, LockActivity.class);
                    startActivity(i);
                }else{
                    Toast.makeText(MainActivity.this, "Silahkan isi kode user pada menu profil sebelum menggunakan layanan aplikasi", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageButton buttonSpeed = (ImageButton)findViewById(R.id.ibSpeed);
        buttonSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sKodeUser.equals("10234")){
                    Intent i = null;
                    i = new Intent(MainActivity.this, SpeedActivity.class);
                    startActivity(i);
                }else{
                    Toast.makeText(MainActivity.this, "Silahkan isi kode user pada menu profil sebelum menggunakan layanan aplikasi", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Session class instance
        session = new SessionManagement(getApplicationContext());
        session.checkLogin();

    }

    BroadcastReceiver tokenReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String token = intent.getStringExtra("token");
            if(token != null){
                sIdDevice = token;
                sendRegistrationToServer();
                Log.v("Token fcm", token + "");
                SharedPreferences.Editor editor = getSharedPreferences(FIREBASE_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("token", sIdDevice);
                editor.commit();

            }
        }
    };

    private void sendRegistrationToServer() {
        // send the token to server
        updateTokenTask ut = new updateTokenTask();
        ut.execute();
    }

    class updateTokenTask extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;
        JSONParser jParser = new JSONParser();
        JSONArray posts = null;

        @Override
        protected String doInBackground(String... sText) {
            String returnResult = getSpeed();
            return returnResult;

        }

        public String getSpeed() {
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            parameter.add(new BasicNameValuePair("id_device", sIdDevice));
            parameter.add(new BasicNameValuePair("email", sEmailUser));

            try {
                String url_all_posts = "http://drivercontrol.info/register_device.php";

                JSONObject json = jParser.makeHttpRequest(url_all_posts, "POST", parameter);

                int success = json.getInt("success");

                if (success == 1) {
                    return "OK";
                } else {
                    return "fail";
                }

            } catch (Exception e) {
                e.printStackTrace();
                return "Exception Caught";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

        }

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
            if(sKodeUser.equals("10234")){
                Intent i = null;
                i = new Intent(MainActivity.this, RecordNewActivity.class);
                startActivity(i);
            }else {
                Toast.makeText(MainActivity.this, "Silahkan isi kode user pada menu profil sebelum menggunakan layanan aplikasi", Toast.LENGTH_SHORT).show();
            }

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
