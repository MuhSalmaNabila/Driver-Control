package com.ditrol.tugasakhir.utils;

public class Utility {

    public final static boolean isEmailValid(String email) {
        CharSequence target = email;
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

}