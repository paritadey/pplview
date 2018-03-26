package com.example.parita.pplview;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class EditProfile extends AppCompatActivity {

    Button correct;
    TextView etUsername;
    String EmailHolder, emailsender, myemail;
    EditText username,userphone,useraddress, userdateofbirth;
    TextView textViewgender,textViewedudetails;
    Spinner usergender, education_details;
    public static final int CONNECTION_TIMEOUT = 20000;
    public static final int READ_TIMEOUT = 20000;
    String suseraddress, susergender, suserbirth, suserphone, susername, suserdetails;
    String altergender, alteredudetails, altername, alteraddress, alterbirth, alterphone;
    String oldgender, olddetails, actualgender, actualdetails;
    String chk_gender="Choose Gender", chk_details="Choose Educational Details";
    boolean CheckEditText;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    ProgressDialog progressDialog;
    String finalResult ;
    String HttpURL="https://paritasampa95.000webhostapp.com/PeopleView/EditProfile.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        init();
        etUsername=(TextView)findViewById(R.id.etUsername);
        Intent intent = getIntent();
        EmailHolder = intent.getStringExtra("email");
        etUsername.setText(EmailHolder);
        emailsender=etUsername.getText().toString();
        myemail=etUsername.getText().toString().trim();

        ActionBar mActionBar=getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);

//Initializes the custom action bar layout
        LayoutInflater mInflater = LayoutInflater.from(this);
        View view = mInflater.inflate(R.layout.custom_action_bar_layout, null);
        mActionBar.setCustomView(view);
        mActionBar.setDisplayShowCustomEnabled(true);

        new AsyncFetch(myemail).execute();

        correct=(Button)view.findViewById(R.id.action_bar_right);

        correct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Value();
                if(CheckEditText){
                    UserUpdateFunction(myemail, altername,alterphone,alteraddress,alterbirth,actualgender,actualdetails);
                }

            }
        });


    }

    public void Value(){
        altername=username.getText().toString().trim();
        alterphone=userphone.getText().toString().trim();
        alteraddress=useraddress.getText().toString().trim();
        alterbirth=userdateofbirth.getText().toString().trim();
        altergender=usergender.getSelectedItem().toString();
        alteredudetails=education_details.getSelectedItem().toString();

        oldgender=textViewgender.getText().toString().trim();
        olddetails=textViewedudetails.getText().toString().trim();

        if(altergender==oldgender || alteredudetails==olddetails){
            actualgender=oldgender;
            actualdetails=olddetails;
        }
        else{
            actualgender=altergender;
            actualdetails=alteredudetails;
        }
        CheckSpinner();
    }

    public void  CheckSpinner(){
        if(actualgender.equals(chk_gender) || actualdetails.equals(chk_details)){
            CheckEditText= false ;
        }
        else {
            CheckEditText = true;
        }
    }

    public void UserUpdateFunction(final String email, final String name, final String phone, final String address, final String dob,
                                   final String gender, final String edu_details){

        class UserUpdateFunctionClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(EditProfile.this,"Updating your profile...",null,true,true);
            }



            @Override
            protected String doInBackground(String... params) {

                hashMap.put("email", params[0]);

                hashMap.put("name",params[1]);

                hashMap.put("phone",params[2]);

                hashMap.put("address", params[3]);

                hashMap.put("dob",params[4]);

                hashMap.put("gender", params[5]);

                hashMap.put("edu_details", params[6]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Profile is updated", Toast.LENGTH_SHORT).show();
               // Toast.makeText(getApplicationContext(), "Server Result: "+httpResponseMsg, Toast.LENGTH_LONG).show();
            }
        }

        UserUpdateFunctionClass userUpdateFunctionClass = new UserUpdateFunctionClass();

        userUpdateFunctionClass.execute(email,name, phone,address, dob, gender,edu_details);
    }
    public void init(){
        username=(EditText)findViewById(R.id.username);
        userphone=(EditText)findViewById(R.id.userphone);
        useraddress=(EditText)findViewById(R.id.useraddress);
        userdateofbirth=(EditText)findViewById(R.id.userdateofbirth);

        textViewgender=(TextView)findViewById(R.id.textViewgender);
        textViewedudetails=(TextView)findViewById(R.id.textViewedudetails);

        usergender=(Spinner)findViewById(R.id.usergender);
        ArrayAdapter<CharSequence> genadaptar = ArrayAdapter.createFromResource(this, R.array.gen,
                android.R.layout.simple_spinner_item);
        genadaptar.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        usergender.setAdapter(genadaptar);

        education_details=(Spinner)findViewById(R.id.education_details);
        ArrayAdapter<CharSequence> detailsadaptar = ArrayAdapter.createFromResource(this, R.array.edu_details,
                android.R.layout.simple_spinner_item);
        detailsadaptar.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        education_details.setAdapter(detailsadaptar);
    }

    public class AsyncFetch extends AsyncTask<String, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(EditProfile.this);
        HttpURLConnection conn;
        URL url = null;
        String searchQuery;

        public AsyncFetch(String searchQuery){
            this.searchQuery=searchQuery;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL("https://paritasampa95.000webhostapp.com/PeopleView/UserProfile.php");

            } catch (MalformedURLException e) {

                e.printStackTrace();
                return e.toString();
            }
            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("myemail", myemail);
                String query = builder.build().getEncodedQuery();
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                e1.printStackTrace();
                return e1.toString();
            }
            try {
                int response_code = conn.getResponseCode();
                if (response_code == HttpURLConnection.HTTP_OK) {
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    return (result.toString());
                } else {
                    return("Connection error");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }
        }
        @Override
        protected void onPostExecute(String result) {

            if(result.equals("no rows")) {
                Toast.makeText(EditProfile.this, "No Results found", Toast.LENGTH_LONG).show();
            }else{
                try {

                    JSONArray jArray = new JSONArray(result);
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.getJSONObject(i);
                        susername = json_data.getString("name");
                        suserphone = json_data.getString("phone");
                        suseraddress=json_data.getString("address");
                        suserbirth=json_data.getString("dob");
                        susergender=json_data.getString("gender");
                        suserdetails=json_data.getString("edu_details");

                    }
                    username.setText(susername);
                    userphone.setText(suserphone);
                    userdateofbirth.setText(suserbirth);
                    textViewgender.setText(susergender);
                    useraddress.setText(suseraddress);
                    textViewedudetails.setText(suserdetails);
                } catch (JSONException e) {
                    Log.d(e.toString(),"error");
                    Toast.makeText(EditProfile.this, "Error in fetching details", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
