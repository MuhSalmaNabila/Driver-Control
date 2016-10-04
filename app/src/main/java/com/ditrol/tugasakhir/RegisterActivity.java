package com.ditrol.tugasakhir;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ditrol.tugasakhir.utils.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private RegisterAccount mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mUlangPasswordView;
    private EditText mNamaLengkapView;
    private EditText mPlatMotor;
    private EditText mKodeAlat;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();
        mPasswordView = (EditText) findViewById(R.id.password);
        mUlangPasswordView = (EditText) findViewById(R.id.ulang_password);
        mNamaLengkapView = (EditText) findViewById(R.id.nama_lengkap);
        mPlatMotor = (EditText) findViewById(R.id.no_plat_motor);
        mKodeAlat = (EditText) findViewById(R.id.kode_alat);



        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String nama_lengkap = mNamaLengkapView.getText().toString();
        String plat_motor = mPlatMotor.getText().toString();
        String ulang_password = mUlangPasswordView.getText().toString();
        String kode_alat = mKodeAlat.getText().toString();

        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);



        String password1 = mPasswordView.getText().toString();
        String password2 = mUlangPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;
        //username
        if (TextUtils.isEmpty(nama_lengkap)) {
            mNamaLengkapView.setError(getString(R.string.error_field_required));
            focusView = mNamaLengkapView;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (!password1.equals(password2)) {
            mUlangPasswordView.setError("Password tidak sama");
            focusView = mUlangPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        //plat motor
        if (TextUtils.isEmpty(plat_motor)) {
            mPlatMotor.setError(getString(R.string.error_field_required));
            focusView = mPlatMotor;
            cancel = true;
        }

    //kode alat
       if (TextUtils.isEmpty(kode_alat)) {
          mKodeAlat.setError(getString(R.string.error_field_required));
          focusView = mKodeAlat;
          cancel = true;
       }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new RegisterAccount(email, password, nama_lengkap, plat_motor, kode_alat);
            mAuthTask.execute("");
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(RegisterActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous registration task used to authenticate
     * the user.
     */
    class RegisterAccount extends AsyncTask<String, Void, String> {

        ProgressDialog pDialog;
        JSONParser jParser = new JSONParser();
        JSONArray posts = null;

        private final String LOG_TAG = RegisterAccount.class.getSimpleName();

        private final String mEmail;
        private final String mPassword;
        private final String mNamaLengkap;
        private final String mPlatMotor;
        private final String mUlangPassword;
        private final String mKodeAlat;

        String sUserName, sEmail, sPassword, sRePassword;

        RegisterAccount(String email, String password, String nama_lengkap, String plat_motor, String kode_alat) {
            mEmail = email;
            mPassword = password;
            mNamaLengkap = nama_lengkap;
            mPlatMotor = plat_motor;
            mUlangPassword = password;
            mKodeAlat = kode_alat;
        }

        @Override
        protected void onPreExecute() {
            showProgress(false);
            super.onPreExecute();
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage("Loading..");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... sText) {
            String returnResult = createAccount();
            return returnResult;
        }

        public String createAccount() {
            String m_email = mEmail;
            String m_password = mPassword;
            String m_nama_lengkap = mNamaLengkap;
            String m_plat_motor = mPlatMotor;
            String m_ulang_password = mUlangPassword;
            String m_kode_alat = mKodeAlat;
            Log.v(LOG_TAG, "Cek data: " + "email: " + mEmail + " password: " + mPassword + " nama :" + mNamaLengkap + " plat: " + mPlatMotor + " kode_alat: " + mKodeAlat);
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            parameter.add(new BasicNameValuePair("email", m_email));
            parameter.add(new BasicNameValuePair("password", m_password));
            parameter.add(new BasicNameValuePair("nama_lengkap", m_nama_lengkap));
            parameter.add(new BasicNameValuePair("plat_motor", m_plat_motor));
            parameter.add(new BasicNameValuePair("kode_alat", m_kode_alat));

            try {
                String url_all_posts = "http://drivercontrol.info/register_user.php";

                JSONObject json = jParser.makeHttpRequest(url_all_posts, "POST", parameter);

                int success = json.getInt("success");
                if (success == 1) {
                    return "OK";
                } else if (success == 2) {
                    return "email registered";
                } else {
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
            mAuthTask = null;

            if (result.equalsIgnoreCase("Exception Caught")) {
                Toast.makeText(RegisterActivity.this, "Erorr! Cek koneksi internet Anda", Toast.LENGTH_LONG).show();
            } else if (result.equalsIgnoreCase("fail")) {
                Toast.makeText(RegisterActivity.this, "Registrasi gagal, coba lagi!", Toast.LENGTH_LONG).show();
            } else if (result.equalsIgnoreCase("email registered")) {
                Toast.makeText(RegisterActivity.this, "Pendaftaran Gagal! Email Anda telah terdaftar!", Toast.LENGTH_LONG).show();
            } else {
                //SUKSES
                Toast.makeText(RegisterActivity.this, "Selamat pendaftaran berhasil dilakukan! Silahkan login menggunakan akun Anda", Toast.LENGTH_LONG).show();
                // Launch login activity
                Intent intent = new Intent(
                        RegisterActivity.this,
                        LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }

    }

}

