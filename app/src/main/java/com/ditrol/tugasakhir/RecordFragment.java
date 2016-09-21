package com.ditrol.tugasakhir;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ditrol.tugasakhir.adapter.ListViewAdapter;
import com.ditrol.tugasakhir.utils.FormatDate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;


public class RecordFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private TextView pesan_kosong;
    private ListView listView;
    // Progress dialog
    private ProgressDialog pDialog;

    private ArrayList<String[]> records = new  ArrayList<String[]>();
    private ArrayList<String[]> records_filtered = new  ArrayList<String[]>();

    public RecordFragment() {
    }

    public static RecordFragment newInstance(int sectionNumber) {
        RecordFragment fragment = new RecordFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_record_new, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);

        pesan_kosong = (TextView) rootView.findViewById(R.id.pesan_kosong);
        // menampilkan hari dan tanggal suatu tab
        textView.setText(FormatDate.getDayDate(FormatDate.getDayWeek(getArguments().getInt(ARG_SECTION_NUMBER))));

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        getData();

        listView = (ListView) rootView.findViewById(R.id.gateListView);

        return rootView;
    }

    private void getData(){
        showpDialog();
        String url = "http://drivercontrol.info/read_record.php";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("respond", response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String status_sukses = response.getString("success");
                    JSONArray status_pintu = response.getJSONArray("produk");
                    for (int i=0;i<status_pintu.length();i++){
                        JSONObject data = (JSONObject) status_pintu.get(i);
                        String tanggal = data.getString("tanggal");
                        String jam = data.getString("jam");
                        String latitude = data.getString("latitude");
                        String longitude = data.getString("longitude");
                        String kecepatan = data.getString("kecepatan");
                        String[] data_record = new String[]{tanggal,jam,latitude,longitude,kecepatan};
                       // addToDatabase(id_shelter,waktu,status);
                        records.add(data_record);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
                hidepDialog();
                filterData();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                // hide the progress dialog
                hidepDialog();
            }
        });
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //Adding request to the queue
        requestQueue.add(jsonObjReq);

    }

    private void filterData(){
        records_filtered.clear();
        for(int i=0; i < records.size();i++ ){
            String[] item = records.get(i);
            // mengambil tanggal dari data
            String date1 = item[0];
            Log.v("tgl_1",date1);
            // mengambil tanggal berdasar judul tab
            String date2 = FormatDate.getDate(FormatDate.getDayWeek(getArguments().getInt(ARG_SECTION_NUMBER)));
            Log.v("tgl_2",date2);
            if(date1.equalsIgnoreCase(date2)){
                records_filtered.add(item);
            }
        }
        showData();
    }

    private void showData(){
        listView.setAdapter(new ListViewAdapter(getActivity(),records_filtered));
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
