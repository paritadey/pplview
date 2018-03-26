package com.example.parita.pplview;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class AnonymousUserDetails extends AppCompatActivity {
    TextView deviceid;
    String adeviceid, id;
    RelativeLayout RelativeLayout02;
    TextView useraddress,userbirth, usergender,userdetails;
    String auseraddress, auserbirth, ausergender, auserdetials;
    public static final int CONNECTION_TIMEOUT = 20000;
    public static final int READ_TIMEOUT = 20000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anonymous_user_details);
        //String id = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        Intent intent = getIntent();
        id=intent.getStringExtra("anonymous");
        deviceid=(TextView)findViewById(R.id.deviceid);
        deviceid.setText(id);
        adeviceid=deviceid.getText().toString().trim();

        RelativeLayout02=(RelativeLayout)findViewById(R.id.RelativeLayout02);
        RelativeLayout02.setVisibility(View.INVISIBLE);
        init();
        new AsyncFetch(adeviceid).execute();
    }
    public void init(){
        useraddress=(TextView)findViewById(R.id.useraddress);
        userbirth=(TextView)findViewById(R.id.userbirth);
        usergender=(TextView)findViewById(R.id.usergender);
        userdetails=(TextView)findViewById(R.id.userdetails);
    }
    public class AsyncFetch extends AsyncTask<String, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(AnonymousUserDetails.this);
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
                url = new URL("https://paritasampa95.000webhostapp.com/PeopleView/GetAnonymousProfile.php");

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
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("deviceid", adeviceid);
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
                Toast.makeText(AnonymousUserDetails.this, "No Results found", Toast.LENGTH_LONG).show();
            }else{
                try {
                    RelativeLayout02.setVisibility(View.VISIBLE);
                    JSONArray jArray = new JSONArray(result);
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.getJSONObject(i);

                        auserbirth=json_data.getString("dob");
                        ausergender=json_data.getString("gender");
                        auseraddress=json_data.getString("address");
                        auserdetials=json_data.getString("details");

                    }

                    userbirth.setText(auserbirth);
                    usergender.setText(ausergender);
                    useraddress.setText(auseraddress);
                    userdetails.setText(auserdetials);
                } catch (JSONException e) {
                    Log.d(e.toString(),"error");
                    Toast.makeText(AnonymousUserDetails.this, "Error in fetching details", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
