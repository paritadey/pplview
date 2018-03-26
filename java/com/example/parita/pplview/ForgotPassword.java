package com.example.parita.pplview;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class ForgotPassword extends AppCompatActivity {
    Button update;
    TextView login;
    EditText retypeemail, retypepass;
    String PasswordHolder, EmailHolder;
    String finalResult;
    String HttpURL = "https://paritasampa95.000webhostapp.com/PeopleView/ForgotPassword.php";
    Boolean CheckEditText;
    ProgressDialog progressDialog;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    Context apContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        init();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckEditTextIsEmptyOrNot();

                if (CheckEditText) {

                    Forgotpass(EmailHolder, PasswordHolder);

                } else {

                    Toast.makeText(ForgotPassword.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();

                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backlogin = new Intent(ForgotPassword.this, Login.class);
                startActivity(backlogin);
            }
        });
    }

    public void CheckEditTextIsEmptyOrNot() {

        EmailHolder = retypeemail.getText().toString();
        PasswordHolder = retypepass.getText().toString();

        if (TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(PasswordHolder)) {
            CheckEditText = false;
        } else {

            CheckEditText = true;
        }
    }

    public void Forgotpass(final String email, final String password) {

        class ForgotpassClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                isConnectingToInternet(apContext);
                progressDialog = ProgressDialog.show(ForgotPassword.this, "Updating...", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                if (httpResponseMsg.equalsIgnoreCase("Password Updated")) {

                    finish();
                } else {

                    Toast.makeText(ForgotPassword.this, "Error Occured", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("email", params[0]);

                hashMap.put("password", params[1]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        ForgotpassClass forgotpassClass = new ForgotpassClass();

        forgotpassClass.execute(email, password);
    }
    private boolean isConnectingToInternet(Context applicationContext){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            Toast.makeText(getApplicationContext(), "no internet", Toast.LENGTH_LONG).show();
            return false;
        } else
            return true;

    }
    public void init() {
        update = (Button) findViewById(R.id.update);
        login = (TextView) findViewById(R.id.backtologin);
        retypeemail = (EditText) findViewById(R.id.email);
        retypepass = (EditText) findViewById(R.id.retypepassword);
    }
}