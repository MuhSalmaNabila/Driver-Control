package com.ditrol.tugasakhir;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ditrol.tugasakhir.utils.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    SessionManagement session;
    TextView kodeAlat, statusAlat;
    String sKodeAlat, sStatusAlat, sEditStatus;
    Button editStatus, refreshData;

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
        setContentView(R.layout.activity_admin);

        // Session class instance
        session = new SessionManagement(getApplicationContext());

        new GetStatusAlat().execute();

        kodeAlat = (TextView)findViewById(R.id.kode_alat_tv);
        statusAlat = (TextView)findViewById(R.id.status_alat_tv);
        editStatus = (Button)findViewById(R.id.edit_alat_button);
        refreshData = (Button)findViewById(R.id.refresh_alat_button);

        editStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupConfirmation();
            }
        });

        refreshData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetStatusAlat().execute();
            }
        });

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
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_about){
            Intent i = null;
            i = new Intent(AdminActivity.this, AboutActivity.class);
            startActivity(i);
        }else if(id == R.id.action_logout){
            showPopupLogout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showPopupConfirmation(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(R.layout.popup_confirmation);
        //alertDialogBuilder.setView(textView);

        // set dialog message
        alertDialogBuilder.setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                new UpdateStatusAlat().execute();
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

    class GetStatusAlat extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;
        JSONParser jParser = new JSONParser();
        JSONArray posts = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AdminActivity.this);
            pDialog.setMessage("Displaying data...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... sText) {
            String returnResult = getStatusAlat();
            return returnResult;
        }

        public String getStatusAlat()
        {
            String kode_alat = "b46c93ky";

            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            parameter.add(new BasicNameValuePair("kode_alat", kode_alat));

            try {
                String url_all_posts = "http://drivercontrol.info/read_alat.php" ;

                JSONObject json = jParser.makeHttpRequest(url_all_posts,"POST", parameter);

                int success = json.getInt("success");

                if (success == 1){
                    return "1";
                }else if(success == 0){
                    return "0";
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
                kodeAlat.setText("");
                statusAlat.setText("");
                editStatus.setVisibility(View.GONE);
                Toast.makeText(AdminActivity.this, "Terjadi kesalahan! Silahkan ulangi kembali", Toast.LENGTH_LONG).show();
                refreshData.setVisibility(View.VISIBLE);
            }else if(result.equalsIgnoreCase("fail")) {
                kodeAlat.setText("");
                statusAlat.setText("");
                editStatus.setVisibility(View.GONE);
                Toast.makeText(AdminActivity.this, "Tidak dapat mendapatkan data alat, silahkan ulangi kembali!", Toast.LENGTH_LONG).show();
                refreshData.setVisibility(View.VISIBLE);
            }else if(result.equalsIgnoreCase("1")){
                //SUKSES
                sKodeAlat = "b46c93ky";
                sStatusAlat = "Aktif";
                sEditStatus = "Nonaktifkan";

                kodeAlat.setText(sKodeAlat);
                statusAlat.setText(sStatusAlat);
                editStatus.setText(sEditStatus);
                Toast.makeText(AdminActivity.this, "Data alat berhasil ditampilkan", Toast.LENGTH_LONG).show();
                refreshData.setVisibility(View.GONE);
                editStatus.setVisibility(View.VISIBLE);

            }else if(result.equalsIgnoreCase("0")){
                sKodeAlat = "b46c93ky";
                sStatusAlat = "Tidak Aktif";
                sEditStatus = "Aktifkan";

                kodeAlat.setText(sKodeAlat);
                statusAlat.setText(sStatusAlat);
                editStatus.setText(sEditStatus);
                Toast.makeText(AdminActivity.this, "Data alat berhasil dinonaktifkan", Toast.LENGTH_LONG).show();
                refreshData.setVisibility(View.GONE);
                editStatus.setVisibility(View.VISIBLE);

            }
        }

    }

    class UpdateStatusAlat extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;
        JSONParser jParser = new JSONParser();
        JSONArray posts = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AdminActivity.this);
            pDialog.setMessage("Updating status...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... sText) {
            String returnResult = updateStatusAlat();
            return returnResult;
        }

        public String updateStatusAlat()
        {
            String kode_alat = "b46c93ky";
            String status_alat = "";

            if(statusAlat.getText().toString().equalsIgnoreCase("Aktif")){
                status_alat = "2";
            }else if(statusAlat.getText().toString().equalsIgnoreCase("Tidak Aktif")){
                status_alat = "1";
            }

            Log.v("ST",status_alat);

            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            parameter.add(new BasicNameValuePair("kode_alat", kode_alat));
            parameter.add(new BasicNameValuePair("status", status_alat));

            try {
                String url_all_posts = "http://drivercontrol.info/update_alat.php" ;

                JSONObject json = jParser.makeHttpRequest(url_all_posts,"POST", parameter);

                String success = json.getString("success");

                if (success.equalsIgnoreCase("1")){
                    return "1";
                }else if(success.equalsIgnoreCase("2")){
                    return "2";
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
                Toast.makeText(AdminActivity.this, "Terjadi kesalahan! Silahkan ulangi kembali", Toast.LENGTH_LONG).show();
            }else if(result.equalsIgnoreCase("fail")) {
                Toast.makeText(AdminActivity.this, "Perubahan gagal disimpan, silahkan ulangi kembali!", Toast.LENGTH_LONG).show();
            }else{
                new GetStatusAlat().execute();
            }
        }

    }

}
