package com.ditrol.tugasakhir.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by ADIK on 09/03/2016.
 */
public class MyLocationListener implements LocationListener {

    private Context context;

    private double latitude;
    private double longitude;
    private ProgressDialog dialog;

    public MyLocationListener(Context context) {
        this.context = context;
    }

    @Override
    public void onLocationChanged(Location location) {
        // Initialize the location fields
        if(location != null){
            setLatitude(location.getLatitude());
            setLongitude(location.getLongitude());
        }


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Toast.makeText(context, provider + "'s status changed to " + status + "!", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onProviderEnabled(String provider) {
        // Toast.makeText(context, "Provider " + provider + " enabled!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        // Toast.makeText(context, "Provider " + provider + " disabled!", Toast.LENGTH_SHORT).show();
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

}
