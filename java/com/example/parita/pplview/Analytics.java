package com.example.parita.pplview;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

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
import java.util.ArrayList;

public class Analytics extends AppCompatActivity {

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    String Like, Dislike, heading, val;
    TextView like, dislike;
    BarChart mChart;
    int FOR=0,AGAINST=0;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        Intent intent = getIntent();
        heading = intent.getStringExtra("heading");
        val=intent.getStringExtra("heading");

        like=(TextView)findViewById(R.id.like);
        dislike=(TextView)findViewById(R.id.dislike);
       // heading="#Jewellery_industry_on_tenterhooks";
        //val="#Jewellery_industry_on_tenterhooks";
        //like.setText(heading);

        new AsyncFetch(heading).execute();
        new AsyncFetch1(val).execute();

        //Graph
        //Graph Initialization
        mChart=findViewById(R.id.barChart);
        mChart.getDescription().setEnabled(false);
        mChart.setDrawBorders(true);
        mChart.setPinchZoom(false);
        mChart.setDrawBarShadow(false);
        mChart.setDrawGridBackground(false);

        //Dataset
        ArrayList<BarEntry> barEntries=new ArrayList<>();
        barEntries.add(new BarEntry(1,0));
        barEntries.add(new BarEntry(2,0));


        //Lebel and Color template
        BarDataSet barDataSet=new BarDataSet(barEntries,"data set");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);


        BarData data=new BarData(barDataSet);
        data.setBarWidth(0.5f);

        mChart.setData(data);
        progressDialog = ProgressDialog.show(Analytics.this,"Fetching Analytics Details...",null,true,true);

        //Handler for time delay
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                ArrayList<BarEntry> barEntries=new ArrayList<>();
                barEntries.add(new BarEntry(1,FOR));
                barEntries.add(new BarEntry(2,AGAINST));

                //Animation of the Bar Chart
                mChart.animateXY(3000, 3000);
                mChart.animateX(3000);
                mChart.animateY(3000);

                progressDialog.dismiss();
                BarDataSet barDataSet=new BarDataSet(barEntries,"#Green stands for LIKE\n#Yellow stands for DISLIKE");
                barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);


                BarData data=new BarData(barDataSet);
                data.setBarWidth(0.5f);

                mChart.setData(data);


            }
        }, 5000);



    }
    public class AsyncFetch extends AsyncTask<String, String, String> {

        HttpURLConnection conn;
        URL url = null;
        String searchQuery;

        public AsyncFetch(String searchQuery){
            this.searchQuery=searchQuery;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL("https://paritasampa95.000webhostapp.com/PeopleView/PLike.php");

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
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("heading", heading);
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
                Toast.makeText(Analytics.this, "No Results found", Toast.LENGTH_LONG).show();
            }else{
                try {

                    JSONArray jArray = new JSONArray(result);
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.getJSONObject(i);
                        Like = json_data.getString(String.valueOf(i));
                    }
                    //like.setText(Like);
                    FOR=Integer.parseInt(Like);
                } catch (JSONException e) {
                    Log.d(e.toString(),"error");
                    Toast.makeText(Analytics.this, "Error in fetching details", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


public class AsyncFetch1 extends AsyncTask<String, String, String> {

    HttpURLConnection conn;
    URL url = null;
    String searchQuery;

    public AsyncFetch1(String searchQuery){
        this.searchQuery=searchQuery;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            url = new URL("https://paritasampa95.000webhostapp.com/PeopleView/PDislike.php");

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
            Uri.Builder builder = new Uri.Builder().appendQueryParameter("heading", val);
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
            Toast.makeText(Analytics.this, "No Results found", Toast.LENGTH_LONG).show();
        }else{
            try {

                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    Dislike = json_data.getString(String.valueOf(i));
                }
                AGAINST=Integer.parseInt(Dislike);
               // dislike.setText(Dislike);

            } catch (JSONException e) {
                Log.d(e.toString(),"error");
                Toast.makeText(Analytics.this, "Error in fetching details", Toast.LENGTH_LONG).show();
            }
        }
    }
    //Graph Implementation



}

}