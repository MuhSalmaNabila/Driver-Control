package com.ditrol.tugasakhir;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.ditrol.tugasakhir.unused.JSONParser;
import com.ditrol.tugasakhir.utils.MyLocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PetaActivity extends AppCompatActivity implements GoogleMap.OnMarkerClickListener,GoogleMap.OnInfoWindowClickListener,GoogleMap.OnMyLocationButtonClickListener {

    private GoogleMap mMap;

    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;

    JSONParser jParser = new JSONParser();
    JSONArray lokasi = null;
    String url_lokasi = "http://drivercontrol.info/read_location.php";
    public static final String TAG_SUCCESS = "success";
    public static final String TAG_LOKASI = "produk";
    public static final String TAG_LATITUDE = "latitude";
    public static final String TAG_LONGITUDE = "longitude";

    String latitudeStr, longitudeStr;

    protected LocationManager locationManager;

    boolean isNetworkEnabled;

    Location location;

    MyLocationListener mylistener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peta);

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSION_ACCESS_COARSE_LOCATION);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        try{
            initilizeMap();
        }catch (Exception e){
            e.printStackTrace();
        }
        new ReadLocationTask().execute();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_ACCESS_COARSE_LOCATION: {
                if (grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setUpLocation();
                } else {
                }
                return;
            }
        }
    }


    class ReadLocationTask extends AsyncTask<String, Void, String>
    {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... sText) {
            String returnResult = getLocation(); //memanggil method getTokoList()
            return returnResult;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //pDialog.dismiss();
            //progressBar.setVisibility(View.GONE);
            if(result.equalsIgnoreCase("Exception Caught"))
            {
                Toast.makeText(PetaActivity.this, "Unable to connect to server,please check your internet connection!", Toast.LENGTH_LONG).show();
            }

            if(result.equalsIgnoreCase("no results"))
            {
                Toast.makeText(PetaActivity.this, "Data empty", Toast.LENGTH_LONG).show();
            }else{
                showLocation (Double.valueOf(latitudeStr), Double.valueOf(longitudeStr));
            }

        }


        public String getLocation()
        {

            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            try {
                JSONObject json = jParser.makeHttpRequest(url_lokasi,"POST", parameter);

                int success = json.getInt(TAG_SUCCESS);
                Log.v("succes",""+TAG_SUCCESS);
                if (success == 1) {
                    lokasi = json.getJSONArray(TAG_LOKASI);
                    for (int i = 0; i < lokasi.length() ; i++){
                        JSONObject c = lokasi.getJSONObject(i);
                        latitudeStr = c.getString(TAG_LATITUDE);
                        longitudeStr = c.getString(TAG_LONGITUDE);
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

    private void setUpLocation(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSION_ACCESS_COARSE_LOCATION);
        }
        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        mylistener = new MyLocationListener(PetaActivity.this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, mylistener);
        mylistener.onLocationChanged(location);

    }

    private void cariLokasiKita(){
        if(isNetworkEnabled){
            if((String.valueOf(mylistener.getLatitude())!=null) || (String.valueOf(mylistener.getLongitude())!=null)){
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mylistener.getLatitude(),mylistener.getLongitude()), 10));
                createMarker("Lokasi Anda","",mylistener.getLatitude(),mylistener.getLongitude());
            }else{
                Toast.makeText(PetaActivity.this, "Tidak dapat mendapatkan lokasi", Toast.LENGTH_SHORT).show();
            }
        }else{
            showSettingsAlert();
        }
    }

    private void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                PetaActivity.this);
        alertDialog.setTitle("Location setting");

        alertDialog
                .setMessage("Lokasi belum aktif, silahkan aktifkan terlebih dulu.");

        alertDialog.setPositiveButton("Setting",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        PetaActivity.this.startActivity(intent);
                    }
                });

        alertDialog.setNegativeButton("Batal",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }


    private void createMarker(String name,String alamat, double lat, double log){
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat,log))
                .snippet(alamat).title(name)).showInfoWindow();
    }


    @Override
    public boolean onMarkerClick(final Marker marker) {
        marker.showInfoWindow();
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(PetaActivity.this, "Clicked", Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onMyLocationButtonClick() {
        //cariLokasiKita();
        return false;
    }



    private void showLocation (double latitude, double longitude){
        LatLng lokasi = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(lokasi).title("Lokasi Motor")).showInfoWindow();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude), 13));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(lokasi));
    }

    private void initilizeMap() {
        if (mMap == null) {
            mMap =((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            mMap.getUiSettings().setMapToolbarEnabled(true);
            mMap.setOnMyLocationButtonClickListener(this);
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSION_ACCESS_COARSE_LOCATION);
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            if (mMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
}
