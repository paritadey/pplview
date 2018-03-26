package com.example.parita.pplview;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class National extends AppCompatActivity {

    TextView etUsername;
    String EmailHolder, frominternational, fromprofile, myemail;
    private RecyclerView recyclerView;
    private static final String HTTP_SERVER_URL = "https://paritasampa95.000webhostapp.com/PeopleView/NationalPost.php";
    List<DataAdapter> DataAdapterClassList;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    RecyclerView.Adapter recyclerViewadapter;
    JsonArrayRequest jsonArrayRequest ;
    ArrayList<String> SubjectNames;
    RequestQueue requestQueue ;
    View ChildView ;
    int RecyclerViewClickedItemPOS ;
    String post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_national);

        etUsername=(TextView)findViewById(R.id.etUsername);
        Intent intent = getIntent();
        EmailHolder = intent.getStringExtra("email");
        etUsername.setText(EmailHolder);
        myemail= etUsername.getText().toString().trim();

        frominternational=intent.getStringExtra("email");
        //Toast.makeText(getApplicationContext(), frominternational, Toast.LENGTH_LONG).show();

        fromprofile=intent.getStringExtra("email");

        DataAdapterClassList = new ArrayList<>();
        SubjectNames = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        JSON_WEB_CALL();
        recyclerView.addItemDecoration(new BackgroundRecyclerview(this));
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(National.this,
                    new GestureDetector.SimpleOnGestureListener() {

                        @Override
                        public boolean onSingleTapUp(MotionEvent motionEvent) {
                            return true;
                        }

                    });
            @Override
            public boolean onInterceptTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {
                ChildView = Recyclerview.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                if(ChildView != null && gestureDetector.onTouchEvent(motionEvent)) {
                    RecyclerViewClickedItemPOS = Recyclerview.getChildAdapterPosition(ChildView);
                }
                return false;
            }
            @Override
            public void onTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {
            }
            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });

        //Bottom Navigation Bar and its functionalities
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.international:
                                Intent international=new Intent(National.this, International.class);
                                international.putExtra("email", myemail);
                                startActivity(international);
                                return true;
                             case R.id.national:
                                item.setIcon(R.drawable.national);
                            case R.id.profile:
                                Intent searc=new Intent(National.this, Profile.class);
                                searc.putExtra("email", myemail);
                                startActivity(searc);
                                return true;
                        }
                        return true;
                    }
                });
    }
    public void JSON_WEB_CALL(){
        jsonArrayRequest = new JsonArrayRequest(HTTP_SERVER_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSON_PARSE_DATA_AFTER_WEBCALL(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
    public void JSON_PARSE_DATA_AFTER_WEBCALL(JSONArray array){
        for(int i = 0; i<array.length(); i++) {
            DataAdapter GetDataAdapter2 = new DataAdapter();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                GetDataAdapter2.setHeading(json.getString("heading"));
                post=json.getString("heading");
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            DataAdapterClassList.add(GetDataAdapter2);
        }
        recyclerViewadapter = new RecyclerViewAdapter(DataAdapterClassList, this);
        recyclerView.setAdapter(recyclerViewadapter);
    }
}
