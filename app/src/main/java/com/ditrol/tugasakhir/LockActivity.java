package com.ditrol.tugasakhir;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ditrol.tugasakhir.unused.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LockActivity extends AppCompatActivity {

    private TextView statusMesinTV,pertanyaanTV;
    private Button yesBut, noBut;
    private String  pertanyaanStr;
    private LinearLayout kotakKonfirmasiLY;

    JSONParser jParser = new JSONParser();
    JSONArray lokasi = null;
    String url_status = "http://drivercontrol.info/read_status.php";
    public static final String TAG_SUCCESS = "success";
    public static final String TAG_STATUS = "status";

    private String statusMesin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);

        statusMesinTV = (TextView) findViewById(R.id.status_hidup_motor);
        pertanyaanTV = (TextView) findViewById(R.id.pertanyaan_status_motor);
        yesBut = (Button) findViewById(R.id.button_iya);
        noBut = (Button) findViewById(R.id.button_tidak);
        kotakKonfirmasiLY = (LinearLayout) findViewById(R.id.kotak_konfirmasi);

        new ReadStatusTask().execute();

        yesBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UpdateStatusTask().execute();
            }
        });

        noBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kotakKonfirmasiLY.setVisibility(View.GONE);
            }
        });

    }


    class ReadStatusTask extends AsyncTask<String, Void, String>
    {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = ProgressDialog.show(LockActivity.this,"",
                    "processing...", false);
        }

        @Override
        protected String doInBackground(String... sText) {
            String returnResult = getLocation();
            return returnResult;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if(result.equalsIgnoreCase("Exception Caught"))
            {
                Toast.makeText(LockActivity.this, "Unable to connect to server,please check your internet connection!", Toast.LENGTH_LONG).show();
            }

            if(result.equalsIgnoreCase("no results"))
            {
                Toast.makeText(LockActivity.this, "Data empty", Toast.LENGTH_LONG).show();
            }else{
                if(statusMesin.equalsIgnoreCase("relayhidup")){
                    statusMesinTV.setText("Mesin Motor Hidup");
                    pertanyaanTV.setText("Anda yakin akan mematikan mesin ?");
                }else{
                    statusMesinTV.setText("Mesin Motor Mati");
                    pertanyaanTV.setText("Anda yakin akan menghidupkan mesin ?");
                }
            }

        }


        public String getLocation()
        {

            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            try {
                JSONObject json = jParser.makeHttpRequest(url_status,"POST", parameter);

                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    lokasi = json.getJSONArray(TAG_STATUS);
                    for (int i = 0; i < lokasi.length() ; i++){
                        JSONObject c = lokasi.getJSONObject(i);
                        statusMesin = c.getString("status_relay");
                        Log.v("status_relay",statusMesin);
                    }
                    return "OK";
                }
                else {
                    return "no results";
                }

            } catch (Exception e) {
                e.printStackTrace();
                return "Exception Caught";
            }
        }

    }


    class UpdateStatusTask extends AsyncTask<String, String, String> {
        ProgressDialog dialog;
        JSONParser jParser = new JSONParser();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(LockActivity.this,"",
                    "processing...", false);
        }

        protected String doInBackground(String... args) {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            if(statusMesin.equalsIgnoreCase("relaymati")){
                params.add(new BasicNameValuePair("status", "relayhidup"));
            }else if(statusMesin.equalsIgnoreCase("relayhidup")){
                params.add(new BasicNameValuePair("status", "relaymati"));
            }

            // check for success tag
            try {
                String url_edit_status = "http://drivercontrol.info/update_status.php";

                JSONObject json = jParser.makeHttpRequest(url_edit_status,"POST", params);

                int success = json.getInt("success");
                if (success == 1) {
                    return "OK";
                }
                else {
                    return "fail";
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return "gagal_koneksi_or_exception";
            }
        }



        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if (result.equalsIgnoreCase("fail")){
                Toast.makeText(LockActivity.this, "Terjadi masalah! Silahkan cek koneksi Anda!", Toast.LENGTH_SHORT).show();
            }
            else if (result.equalsIgnoreCase("gagal_koneksi_or_exception")){
                Toast.makeText(LockActivity.this, "Terjadi masalah! Silahkan cek koneksi Anda!",  Toast.LENGTH_SHORT).show();
            }
            else if (result.equalsIgnoreCase("OK")){
                new ReadStatusTask().execute();
            }
        }
    }
}
