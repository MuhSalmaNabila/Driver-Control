package com.ditrol.tugasakhir.unused;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.ditrol.tugasakhir.R;

public class Register extends Activity {
    Button bDaftar;
    EditText etNamalengkap,etPasswordreg, etPlatmotor, etEmail, etNohp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        /*
        etNamalengkap = (EditText) findViewById(R.id.etNamalengkap);
        etPasswordreg = (EditText) findViewById(R.id.etPasswordreg);
        etPlatmotor = (EditText) findViewById(R.id.etPlatmotor);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etNohp = (EditText) findViewById(R.id.etNohp);
        bDaftar = (Button) findViewById (R.id.bDaftar);

        bDaftar.setOnClickListener(this);
    }

            @Override
            public void onClick(View v) {
               switch (v.getId()){
                   case R.id.bDaftar:

                       break;
        }
        */
    }

}
