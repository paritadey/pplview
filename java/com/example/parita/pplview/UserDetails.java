package com.example.parita.pplview;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
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

public class UserDetails extends AppCompatActivity {
    TextView etUsername;
    String myemail;
    String EmailHolder;
    RelativeLayout layout;
    TextView useraddress, usergender, userbirth, userphone, username, userdetails;
    String suseraddress, susergender, suserbirth, suserphone, susername, suserdetails;
    public static final int CONNECTION_TIMEOUT = 20000;
    public static final int READ_TIMEOUT = 20000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        layout=(RelativeLayout)findViewById(R.id.sublayout);
        layout.setVisibility(View.INVISIBLE);
        init();
        etUsername=(TextView)findViewById(R.id.etUsername);
        Intent intent = getIntent();
        EmailHolder = intent.getStringExtra("email");
        etUsername.setText(EmailHolder);
        myemail= etUsername.getText().toString().trim();

        new AsyncFetch(myemail).execute();
    }
    public void init(){

        useraddress=(TextView)findViewById(R.id.useraddress);
        usergender=(TextView)findViewById(R.id.usergender);
        userbirth=(TextView)findViewById(R.id.userbirth);
        userphone=(TextView)findViewById(R.id.userphone);
        username=(TextView)findViewById(R.id.username);
        userdetails=(TextView)findViewById(R.id.userdetails);

    }
    public class AsyncFetch extends AsyncTask<String, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(UserDetails.this);
        HttpURLConnection conn;
        URL url = null;
        String searchQuery;

        public AsyncFetch(String searchQuery){
            this.searchQuery=searchQuery;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();
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

            pdLoading.dismiss();
            pdLoading.dismiss();
            if(result.equals("no rows")) {
                Toast.makeText(UserDetails.this, "No Results found", Toast.LENGTH_LONG).show();
            }else{
                try {
                    layout.setVisibility(View.VISIBLE);
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
                    userbirth.setText(suserbirth);
                    usergender.setText(susergender);
                    useraddress.setText(suseraddress);
                    userdetails.setText(suserdetails);
                } catch (JSONException e) {
                    Log.d(e.toString(),"error");
                    Toast.makeText(UserDetails.this, "Error in fetching details", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
