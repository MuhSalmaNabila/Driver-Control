package com.ditrol.tugasakhir;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ditrol.tugasakhir.utils.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {


    private final String LOG_TAG = ProfileActivity.class.getSimpleName();
    // Session Management Class
    SessionManagement session;
    String sId, sEmail, sNama, sPwd1, sPlatMotor, sNope;
    EditText etUsername;
    EditText etPlatMotor;
    EditText etEmail;
    EditText etNoHp;
    Button bUbahPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Session class instance
        session = new SessionManagement(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // name
        String userId = user.get(SessionManagement.KEY_ID);
        sId = userId;
        String userName = user.get(SessionManagement.KEY_USERNAME);
        String userPassword = user.get(SessionManagement.KEY_PASSWORD);
        String userPlatMotor = user.get(SessionManagement.KEY_PLAT_MOTOR);
        String userEmail = user.get(SessionManagement.KEY_EMAIL);
        String userNoHp = user.get(SessionManagement.KEY_NO_HP);

        etUsername = (EditText)findViewById(R.id.etusername);
        etUsername.setText(userName);

        etPlatMotor = (EditText)findViewById(R.id.etplat);
        etPlatMotor.setText(userPlatMotor);

        etEmail = (EditText)findViewById(R.id.etmail);
        etEmail.setText(userEmail);

        etNoHp = (EditText)findViewById(R.id.etnohp);
        etNoHp.setText(userNoHp);

        bUbahPassword = (Button)findViewById(R.id.bganti);
        bUbahPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = null;
                i = new Intent(getBaseContext(), UbahPasswordActivity.class);
                startActivity(i);
            }
        });

    }

    public void ubahProfil(View v){
        View focusView=null;
        Boolean cancel = false;
        etUsername.setError(null);
        etEmail.setError(null);
        etPlatMotor.setError(null);
        etNoHp.setError(null);

        sNama = etUsername.getText().toString();
        sEmail = etEmail.getText().toString();
        sPlatMotor = etPlatMotor.getText().toString();
        sNope = etNoHp.getText().toString();

        if (TextUtils.isEmpty(sNama)) {
            etUsername.setError(getString(R.string.error_field_required));
            focusView = etUsername;
            cancel = true;
        }
        if (TextUtils.isEmpty(sEmail)) {
            etEmail.setError(getString(R.string.error_field_required));
            focusView = etEmail;
            cancel = true;
        }else if(!isEmailValid(sEmail)){
            etEmail.setError(getString(R.string.error_invalid_email));
            focusView = etEmail;
            cancel = true;
        }
        if (TextUtils.isEmpty(sPlatMotor)) {
            etPlatMotor.setError(getString(R.string.error_field_required));
            focusView = etPlatMotor;
            cancel = true;
        }
        if (TextUtils.isEmpty(sNope)) {
            etNoHp.setError(getString(R.string.error_field_required));
            focusView = etNoHp;
            cancel = true;
        }
        if(cancel){
            //jika ada error (data kosong)
            focusView.requestFocus();
        }else{
            showPopupProfil();
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    public void showPopupProfil(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        TextView textView = new TextView(getApplicationContext());
        textView.setText("Apakah Anda yakin ingin merubah profil?");


        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(R.layout.popup_profile);
        //alertDialogBuilder.setView(textView);

        // set dialog message
        alertDialogBuilder.setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                UpdateProfil up = (UpdateProfil) new UpdateProfil().execute();
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

    class UpdateProfil extends AsyncTask<String, Void, String>
    {
        ProgressDialog pDialog;
        JSONParser jParser = new JSONParser();
        JSONArray posts = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ProfileActivity.this);
            pDialog.setMessage("Proses...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... sText) {
            String returnResult = createAccount();
            return returnResult;

        }

        public String createAccount()
        {
            String id_user = sId;
            String email_user = etEmail.getText().toString();
            sEmail = email_user;
            String nama_user = etUsername.getText().toString();
            sNama = nama_user;
            String plat_motor = etPlatMotor.getText().toString();
            sPlatMotor = plat_motor;
            String no_hp = etNoHp.getText().toString();
            sNope = no_hp;

            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            parameter.add(new BasicNameValuePair("id", id_user));
            parameter.add(new BasicNameValuePair("email", email_user));
            parameter.add(new BasicNameValuePair("username", nama_user));
            parameter.add(new BasicNameValuePair("plat_motor", plat_motor));
            parameter.add(new BasicNameValuePair("no_hp", no_hp));

            try {
                // String url_all_posts = "http://api.vhiefa.net76.net/whatson/create_account.php" ;
                String url_all_posts = "http://drivercontrol.info/update_profile.php" ;

                JSONObject json = jParser.makeHttpRequest(url_all_posts,"POST", parameter);

                //JSONArray jsonArray = json.getJSONArray("user");
                //int length = jsonArray.length();

                //Log.v(LOG_TAG, "Panjang Array: " + length);

                int success = json.getInt("success");

                if (success == 1){
                    session.createLoginSession(sId, sNama, sPwd1, sPlatMotor, sEmail, sNope);
                    return "OK";
                }else if (success == 2){
                    return "email registered";
                }else{
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
            pDialog.dismiss();
            if(result.equalsIgnoreCase("Exception Caught")) {
                Toast.makeText(ProfileActivity.this, "Terjadi kesalahan! Silahkan ulangi kembali", Toast.LENGTH_LONG).show();
            } else if(result.equalsIgnoreCase("fail")) {
                Toast.makeText(ProfileActivity.this, "Perubahan gagal disimpan, silahkan ulangi kembali!", Toast.LENGTH_LONG).show();
            }else {
                //SUKSES
                etUsername.setText(sNama);
                etPlatMotor.setText(sPlatMotor);
                etEmail.setText(sEmail);
                etNoHp.setText(sNope);
                Toast.makeText(ProfileActivity.this, "Perubahan berhasil disimpan!", Toast.LENGTH_LONG).show();

            }
        }

    }


}
