package com.ditrol.tugasakhir;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    // Session Management Class
    SessionManagement session;

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
        String userName = user.get(SessionManagement.KEY_USERNAME);
        String userPassword = user.get(SessionManagement.KEY_PASSWORD);
        String userPlatMotor = user.get(SessionManagement.KEY_PLAT_MOTOR);
        String userEmail = user.get(SessionManagement.KEY_EMAIL);
        String userNoHp = user.get(SessionManagement.KEY_NO_HP);

        EditText etUsername = (EditText)findViewById(R.id.etusername);
        etUsername.setText(userName);

        EditText etPlatMotor = (EditText)findViewById(R.id.etplat);
        etPlatMotor.setText(userPlatMotor);

        EditText etEmail = (EditText)findViewById(R.id.etmail);
        etEmail.setText(userEmail);

        EditText etNoHp = (EditText)findViewById(R.id.etnohp);
        etNoHp.setText(userNoHp);
    }

    public void showPopup(View v){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        TextView textView = new TextView(getApplicationContext());
        textView.setText("Apakah Anda yakin ingin merubah profil?");


        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(R.layout.popup_profile);
        //alertDialogBuilder.setView(textView);

        // set dialog message
        alertDialogBuilder.setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
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

}
