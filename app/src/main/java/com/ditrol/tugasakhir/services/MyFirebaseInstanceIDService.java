package com.ditrol.tugasakhir.services;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Eko on 8/23/2016.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    String sIdDevice;
    public static final String FIREBASE_PREFS_NAME = "FirebaseToken";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        sIdDevice = refreshedToken;
        Log.d(TAG, "Refreshed token:" + refreshedToken);
        final Intent intent = new Intent("tokenReceiver");
        final LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        intent.putExtra("token",refreshedToken);
        broadcastManager.sendBroadcast(intent);
        //sendRegistrationToServer();
    }
}
