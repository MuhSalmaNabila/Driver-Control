package com.ditrol.tugasakhir;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ditrol.tugasakhir.utils.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UbahPasswordActivity extends AppCompatActivity {

    String sId, sPassLama1 , sPassLama2, sPassBaru, sUlangiPassBaru;
    EditText etPassLama;
    EditText etPassBaru;
    EditText etUlangiPassBaru;
    SessionManagement session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_password);

        // Session class instance
        session = new SessionManagement(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // name
        String userId = user.get(SessionManagement.KEY_ID);
        sId = userId;
        String userPassword = user.get(SessionManagement.KEY_PASSWORD);
        sPassLama1 = userPassword;

        etPassLama = (EditText)findViewById(R.id.et_pass_lama);
        etPassBaru = (EditText)findViewById(R.id.et_pass_baru);
        etUlangiPassBaru = (EditText)findViewById(R.id.et_ulangi_pass_baru);

    }

    public void showPopupUbahPassword(View v){

        String pass_lama = etPassLama.getText().toString();
        sPassLama2 = pass_lama;
        String pass_baru = etPassBaru.getText().toString();
        sPassBaru = pass_baru;
        String ulangi_pass_baru = etUlangiPassBaru.getText().toString();
        sUlangiPassBaru = ulangi_pass_baru;

        if(sPassLama1.equals(sPassLama2)){

            if(sPassBaru.equals(sUlangiPassBaru)) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(R.layout.popup_profile);
                //alertDialogBuilder.setView(textView);

                // set dialog message
                alertDialogBuilder.setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        UbahPassword up = (UbahPassword) new UbahPassword().execute();
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
            }else{
                etUlangiPassBaru.setError("Password baru tidak sama!");
            }
        }else{
            etPassLama.setError("Password lama salah");

        }
    }


    class UbahPassword extends AsyncTask<String, Void, String>
    {
        ProgressDialog pDialog;
        JSONParser jParser = new JSONParser();
        JSONArray posts = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UbahPasswordActivity.this);
            pDialog.setMessage("Loading..");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... sText) {
            String returnResult = ubahPassword();
            return returnResult;

        }

        public String ubahPassword()
        {
            String id_user = sId;
            String password = sPassBaru;

            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            parameter.add(new BasicNameValuePair("id", id_user));
            parameter.add(new BasicNameValuePair("password", password));


            try {

                // String url_all_posts = "http://api.vhiefa.net76.net/whatson/create_account.php" ;
                String url_all_posts = "http://drivercontrol.info/update_password.php" ;

                JSONObject json = jParser.makeHttpRequest(url_all_posts,"POST", parameter);

                int success = json.getInt("success");

                if (success == 1) {
                    session.updatePasswordSession(sPassBaru);
                    return "OK";
                }
                else if (success == 2){
                    return "email registered";
                }
                else {
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
            if(result.equalsIgnoreCase("Exception Caught"))
            {
                Toast.makeText(UbahPasswordActivity.this, "Erorr! Cek koneksi internet Anda", Toast.LENGTH_LONG).show();
            }
            else if(result.equalsIgnoreCase("fail"))
            {
                Toast.makeText(UbahPasswordActivity.this, "Perubahan gagal disimpan, silahkan ulangi kembali!", Toast.LENGTH_LONG).show();
            } else {
                //SUKSES
                Toast.makeText(UbahPasswordActivity.this, "Password berhasil diubah!", Toast.LENGTH_LONG).show();

            }
        }

    }


}
