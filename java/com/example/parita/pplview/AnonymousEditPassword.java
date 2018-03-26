package com.example.parita.pplview;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class AnonymousEditPassword extends AppCompatActivity {
    TextView anoname;
    String deviceid, emailsender;
    Button editpass;
    EditText oldpass, newpass;
    String PasswordHolder, OldPassword;
    String finalResult;
    Boolean CheckEditText;
    ProgressDialog progressDialog;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    Context apContext;
    String HttpURL = "https://paritasampa95.000webhostapp.com/PeopleView/AnonymousEditPassword.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anonymous_edit_password);

        anoname=(TextView)findViewById(R.id.anoname);
        String id = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        // Intent intent = getIntent();
        //EmailHolder = intent.getStringExtra("deviceno");
        anoname.setText(id);
        emailsender=anoname.getText().toString();
        deviceid=anoname.getText().toString().trim();

        init();
        editpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckEditTextIsEmptyOrNot();

                if (CheckEditText) {

                    EditPassword(deviceid, PasswordHolder);

                } else {

                    Toast.makeText(AnonymousEditPassword.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();

                }


            }
        });
    }
    public void EditPassword(final String email, final String password) {

        class EditpassClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                isConnectingToInternet(apContext);
                progressDialog = ProgressDialog.show(AnonymousEditPassword.this, "Updating...", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                if (httpResponseMsg.equalsIgnoreCase("Password Edited")) {

                    finish();
                } else {

                    Toast.makeText(AnonymousEditPassword.this, "Error Occured", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("deviceid", params[0]);

                hashMap.put("password", params[1]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        EditpassClass editpassClass = new EditpassClass();

        editpassClass.execute(email, password);
    }

    public void CheckEditTextIsEmptyOrNot() {

        OldPassword=oldpass.getText().toString();
        deviceid = anoname.getText().toString();
        PasswordHolder = newpass.getText().toString();

        if (TextUtils.isEmpty(OldPassword) || TextUtils.isEmpty(deviceid) || TextUtils.isEmpty(PasswordHolder)) {
            CheckEditText = false;
        } else {

            CheckEditText = true;
        }
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
    public void init(){
        editpass=(Button)findViewById(R.id.editpass);
        oldpass=(EditText)findViewById(R.id.oldpassword);
        newpass=(EditText)findViewById(R.id.newpassword);
    }
}
