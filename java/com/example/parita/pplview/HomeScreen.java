package com.example.parita.pplview;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

public class HomeScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView recyclerView;
    private static final String HTTP_SERVER_URL = "https://paritasampa95.000webhostapp.com/PeopleView/Notification.php";

    AlertDialog.Builder alertDialog, exitapp, alertDialog1;
    String EmailHolder, emailsender, AnoNameHolder;
    TextView etUsername, etAnonymous;
    String checkemail, checkanoname;
    public static final String UserEmail = "";
    public String Anonymousid = "";
    String chk_member, chk_anonymous="Anonymous";
    int uservaildity=0;
    List<DataAdapter> DataAdapterClassList;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    RecyclerView.Adapter recyclerViewadapter;
    JsonArrayRequest jsonArrayRequest ;
    ArrayList<String> SubjectNames;
    RequestQueue requestQueue ;
    View ChildView ;
    int RecyclerViewClickedItemPOS ;
    String post;
    String isendmyemail;
    String emailfrommain;


    TextView firstetUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

         etAnonymous=(TextView)findViewById(R.id.anoname); //anonymous_name
        etUsername = (TextView) findViewById(R.id.etUsername); //email of the logged user
        Intent intent = getIntent();
        EmailHolder = intent.getStringExtra("email"); //holding the email that is coming from Login Activity
        etUsername.setText(EmailHolder); //and set it to a textview

     //   Toast.makeText(getApplicationContext(), EmailHolder, Toast.LENGTH_LONG).show();
       /* firstetUsername=(TextView)findViewById(R.id.firstetUsername);
        firstetUsername.setText(EmailHolder);

        emailfrommain=intent.getStringExtra("emailKey"); //holding the email that is coming from MainActivity
        etUsername.setText(emailfrommain); //and set it to a textview*/

        emailsender=etUsername.getText().toString(); //send it to the Account_info Activity
        AnoNameHolder=intent.getStringExtra(AnonymousLogin.UserName); //for anonymous login Anonymous keyword is sent from AnonymousLogin to HomeScreen
        etAnonymous.setText(AnoNameHolder);

        checkemail=etUsername.getText().toString(); //when logged user logged into the app his email id is sent from Login Activity and convert into String
        checkanoname=etAnonymous.getText().toString(); //when anonymous user logged into the app his name saved as Anonymous sent from AnonymousLogin will be converted to String

        //SharedPreference is used to share the emailid of the logged in user and sent it to DetailedAboutPost activity for referring the
        //user who has logged into tha application

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("email", checkemail);
        editor.commit();


        //setting the dialog when backbutton is pressed
        alertDialog = new AlertDialog.Builder(HomeScreen.this);
        exitapp = new AlertDialog.Builder(this);
        showmsg();
        checkUser();

        //Setting the RecyclerView for displaying the data fetched from the server
        DataAdapterClassList = new ArrayList<>();
        SubjectNames = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewlayoutManager);

        JSON_WEB_CALL();

        recyclerView.addItemDecoration(new BackgroundRecyclerview(this));
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(HomeScreen.this,
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
                                Intent international=new Intent(HomeScreen.this, International.class);
                                international.putExtra("email", emailsender);
                                startActivity(international);
                                return true;
                            case R.id.national:
                                Intent nation=new Intent(HomeScreen.this, National.class);
                                nation.putExtra("email", emailsender);
                                startActivity(nation);
                                return true;
                            case R.id.profile:
                                Intent searc=new Intent(HomeScreen.this, Profile.class);
                                searc.putExtra("email", emailsender);
                                startActivity(searc);
                                return true;
                        }
                        return true;
                    }
                });
        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    /*    final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // TODO: Your application init goes here.

                JSON_WEB_CALL();
                Toast.makeText(getApplicationContext(), "New contents are availabele", Toast.LENGTH_SHORT).show();
            }
        }, 8000);*/

    }

    //when the user enter login id in Login Activity then the login email will be passed through intent and checked
    // whether the length of the string stores email id is greater than 1 or not then only shows toast message
    // otherwise alertmsg() will call
    public void showmsg(){
        if(checkemail.length()>1 || checkanoname.length()>1 || EmailHolder.length()>1){

          // Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_LONG).show();
        }
    }

    //if any person doesnt login then it shows the message to choose options
    public void alertmsg(){

        alertDialog.setTitle("Account Creation");
        alertDialog.setMessage("How do you conitue?");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Toast.makeText(getApplicationContext(), "Login to Account", Toast.LENGTH_SHORT).show();
                Intent regis=new Intent(HomeScreen.this, Login.class);
                startActivity(regis);

            }
        });
        alertDialog.setNegativeButton("Annonymous", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Continue as Annonymous", Toast.LENGTH_SHORT).show();
                Intent world=new Intent(HomeScreen.this, AnonymousLogin.class);
                startActivity(world);
            }
        });
        alertDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alertDialog.show();
    }

    //on back button press of the android device
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } /*else {
            super.onBackPressed();

        }*/
    }

    //check who has logged in and how logged in :as Member or as Anonymous
    public void checkUser(){
        if(checkemail.length()>1 && ! chk_anonymous.equals(checkanoname) )//||  EmailHolder.length()>1)
        {
            uservaildity=1; //integer value will increase to 1 if the condition satisfied i.e when the logged user is a Member
           // Toast.makeText(getApplicationContext(), "Logged in as memebr" +uservaildity,  Toast.LENGTH_LONG).show();
        }
        else{
            uservaildity=2; //integer value will increase to 1 if the condition satisfied i.e when the logged user is an Anonymous User
           // Toast.makeText(getApplicationContext(), "Logged in as Anonymous" +uservaildity, Toast.LENGTH_LONG).show();
        }
    }

 /*   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.search) {
            Intent post=new Intent(HomeScreen.this, Search.class);
            startActivity(post);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    //navigation drawer's items and its functionalities
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //Drawer options and its facility when User logged in as Member
        if(uservaildity==1) {
            if (id == R.id.account) {
                Intent acc = new Intent(HomeScreen.this, Account_info.class);
                acc.putExtra("email", EmailHolder);
                startActivity(acc);

            } else if (id == R.id.settings) {
                Intent settin = new Intent(HomeScreen.this, Settings.class);
                settin.putExtra("email", EmailHolder);
                startActivity(settin);
            } else if (id == R.id.about) {
                Intent ab = new Intent(HomeScreen.this, About.class);
                startActivity(ab);
            } else if (id == R.id.logout) {
                showexitmsg();
            } else if (id == R.id.ano_account) {
                showPopupWindowForLoggedInMember();
            }
            else if(id==R.id.switchacc){
                Intent toanoymous=new Intent(HomeScreen.this, AnonymousLogin.class);
                startActivity(toanoymous);
            }
        }
        //Drawer options and its facility when User logged in as Anonymous
        else{
            if (id == R.id.ano_account) {
                Intent ano = new Intent(HomeScreen.this, AnonymousAccountinfo.class);
                ano.putExtra("anonymous", checkanoname);
                startActivity(ano);

            } else if (id == R.id.settings) {
                Intent settin = new Intent(HomeScreen.this, AnonymousSetting.class);
                startActivity(settin);
            } else if (id == R.id.about) {
                Intent ab = new Intent(HomeScreen.this, About.class);
                startActivity(ab);
            } else if (id == R.id.logout) {
                showexitmsg();
            } else if(id==R.id.account){
                showPopupWindowForAnonymous();
            }
            else if(id==R.id.switchacc){
                Intent tomember=new Intent(HomeScreen.this, Login.class);
                startActivity(tomember);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //when logout is hit then it shows an alert messgae whether the user really want to close the app
    public void showexitmsg(){

        // Setting Alert Dialog Title
        exitapp.setTitle("Confirm Exit..!!!");
        exitapp.setMessage("Are you sure,You want to exit");
        exitapp.setCancelable(false); //setCancelable is made as false because without choosing any option the dialog message will not disapper

        exitapp.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
               // HomeScreen.this.finish();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME); //shows the homescreen
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                System.exit(0); //kill the app

                //This code will completely stop the application.
               /* System.runFinalizersOnExit(true);
                System.exit(0);
                android.os.Process.killProcess(android.os.Process.myPid());*/
            }
        });

        exitapp.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(HomeScreen.this,"You clicked over No",Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alertDialog = exitapp.create();
        alertDialog.show();
    }

    //This message will only show when a Member tries to hit AnonymousAccountinfo
    //It shows how the user logged in to the application
    //if logged in as Member then He can only open the Account info option of the drawer and while hitting the Anonymous Account
    //info it shows "You are a Member
    public void showPopupWindowForLoggedInMember(){
        try{

            LayoutInflater li = LayoutInflater.from(getApplicationContext());
            final View prompt = li.inflate(R.layout.loggedinas_member, null);
            final AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder
                    (HomeScreen.this);
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setView(prompt);
            // positive button  --right side
            alertDialogBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();

                }
            });

            alertDialogBuilder.show();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    //Similarly When user logged in as Anonymous and hit Account info option of the drawer then only shows the popup message
    //and says "You Logged into as Anonymous

    public void showPopupWindowForAnonymous(){
        try{

            LayoutInflater li = LayoutInflater.from(getApplicationContext());
            final View prompt = li.inflate(R.layout.loggedinas_anonymous, null);
            final AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder
                    (HomeScreen.this);
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setView(prompt);
            // positive button  --right side
            alertDialogBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();

                }
            });

            alertDialogBuilder.show();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    //Used for fetching all the news i.e : National, International all together in the HomeScreen
    //and show in a recyclerview one after the another.
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
