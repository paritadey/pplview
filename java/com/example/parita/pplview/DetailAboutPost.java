package com.example.parita.pplview;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class DetailAboutPost extends AppCompatActivity {



    RelativeLayout layout3, layout2, layout1, layout5;
    TextView sharepost, etUsername, anoname, more;
    TextView description,source, time, authorname,authoremail;
    Button like,dislike, comment,share, bookmark, analytics;
    EditText commentpost;
    Button postcomment;
    TextView timeofpost;
    String anaheading;
    String post,deviceno;
    String heading, checkemail, anonymous, deviceid;
    int likevalue=0, dislikevalue=0;
    String likeitem, dislikeitem, useremail, postheading, postdescription, postsource, postpasttime, postauthor, postauthoremail, postmycomment;
    String systemposttime;
    public static final int CONNECTION_TIMEOUT = 20000;
    public static final int READ_TIMEOUT = 20000;
    String finalResult ;
    String HttpURL="https://paritasampa95.000webhostapp.com/PeopleView/Postlikedislike.php";
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    View v;

    //Creating Table we need to create a database in SQLITE
    public static final String DATABASE_NAME = "Bookmark";
    public static final String DATABASE_NAME2 ="Bookmark2";
    SQLiteDatabase mDatabase, mdatabase;//declaring the instance of SQLITEDATABASE for two bookmark tables one as Logged user
    // other for Anonyous User Bookmark


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_about_post);
        init();

        //creating a database and setting the MODE
        mDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        mdatabase =openOrCreateDatabase(DATABASE_NAME2, MODE_PRIVATE, null);

        createBookmarkTable(); //Create table in SQLITE for Bookmark When user logged in as Member
        createAnoBookmarkTable(); //Create table in SQLITE for bookmark when user logged in as Anonymous

        //Creating a custom ActionBar
        ActionBar mActionBar=getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);

//Initializes the custom action bar layout
        LayoutInflater mInflater = LayoutInflater.from(this);
        View view = mInflater.inflate(R.layout.custom_detailed_post, null);
        mActionBar.setCustomView(view);
        mActionBar.setDisplayShowCustomEnabled(true);

        //Buttons in custom ActionBar
        share=(Button)view.findViewById(R.id.share);
        bookmark=(Button)view.findViewById(R.id.bookmark);

        layout3.setVisibility(View.INVISIBLE);

        //getting the heading of the particular news whose details info is required from the HomeScreen
        final Intent intent = getIntent();
        post = intent.getStringExtra("heading");
        sharepost=(TextView)findViewById(R.id.sharepost);
        sharepost.setText(post);

        anoname=(TextView)findViewById(R.id.anoname);
        //Getting the device id
        anonymous = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        anoname.setText(anonymous);
        deviceid=anoname.getText().toString().trim();
        deviceno=anoname.getText().toString().trim();


        heading=sharepost.getText().toString().trim(); //for fetching we are sending heading data to the server to get matched values
        postheading=sharepost.getText().toString().trim(); //for user like/dislike sending the heading
        anaheading=sharepost.getText().toString().trim();

        etUsername=(TextView)findViewById(R.id.etUsername);
        //SharedPrefernce is used to get the email id of the logged in user from HomeScreen to get track the activity of the user
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String data = prefs.getString("email", "no id");
        etUsername.setText(data);
        checkemail=etUsername.getText().toString().trim();


        new AsyncFetch(heading).execute();

        //Used to show the whole story of the news against the heading hit in the HomeScreen Activity
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                description.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.VISIBLE);
                layout5.setVisibility(View.VISIBLE);
            }
        });

        //Getting the System date
       SimpleDateFormat timeStampFormat = new SimpleDateFormat("dd/MM/yyyy : HH:mm:ss");
        Date myDate = new Date();
        String filename = timeStampFormat.format(myDate);
        timeofpost.setText(filename);
        systemposttime=timeofpost.getText().toString().trim(); //for user like/dislike sending app user time of lik/dislike

       postcomment=(Button)findViewById(R.id.postcomment);

        //functionality when support button is pressed
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout3.setVisibility(View.VISIBLE);
                likevalue++; //when support button is clicked then likevalue will increase by 1
                dislikevalue=0; //dislikevalue will decrease andagainst button gets disabled because one can press one button at a time
                dislike.setEnabled(false);
                Toast.makeText(getApplicationContext(), "You support the Post", Toast.LENGTH_SHORT).show();
                likeitem=String.valueOf(likevalue); //for user like/dislike sending app user like
                dislikeitem=String.valueOf(dislikevalue); //for user like/dislike sending app user dislike
                WhoiAm(); //for posting comment first check how to post comment as Member or Anonymous
                postcomment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        postmycomment=commentpost.getText().toString().trim();
                        CheckUser();
                        PostLikeDislike(useremail,postheading, postdescription, postsource, postpasttime, postauthor, postauthoremail,
                                likeitem, dislikeitem,systemposttime,postmycomment);
                        commentpost.setText("");

                        Toast.makeText(getApplicationContext(), "Your Comment has been uploaded", Toast.LENGTH_LONG).show();
                       // Intent comment=new Intent(DetailAboutPost.this, ShowAllComments.class);
                       // startActivity(comment);
                    }
                });

            }
        });

        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout3.setVisibility(View.VISIBLE);
                dislikevalue++;
                likevalue=0;
                dislike.setEnabled(false);
                Toast.makeText(getApplicationContext(), "You are against of the Post", Toast.LENGTH_SHORT).show();
                likeitem=String.valueOf(likevalue); //for user like/dislike sending app user like
                dislikeitem=String.valueOf(dislikevalue); //for user like/dislike sending app user dislike
                WhoiAm();
                postcomment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        postmycomment=commentpost.getText().toString().trim();
                        CheckUser();
                        PostLikeDislike(useremail,postheading, postdescription, postsource, postpasttime, postauthor, postauthoremail,
                                likeitem, dislikeitem,systemposttime,postmycomment);
                        commentpost.setText("");
                        Toast.makeText(getApplicationContext(), "Your Comment has been uploaded", Toast.LENGTH_LONG).show();
                      //  Intent comment=new Intent(DetailAboutPost.this, ShowAllComments.class);
                     //   startActivity(comment);
                    }
                });



            }
        });

        analytics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(DetailAboutPost.this, Analytics.class);
                intent1.putExtra("heading", anaheading);
                startActivity(intent1);
            }
        });

        //When Star button is pressed functionalities are defined here
        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkemail.length()>1 && deviceid.length()>1){
                    addBookmark(); //if the user is a member then bookmark will be saved in DATABASE_NAME and in bookmark table
                    bookmark.setBackgroundResource(R.drawable.bookmarked); //after saving all the details in the table the icon of the bookmark will changed to filled white
                }
                else {
                    anoBookmark(); //if the user is an anonymous then bookmark will be saved in DATABASE_NAME2 and in anonymous_bookmark table
                    bookmark.setBackgroundResource(R.drawable.bookmarked);
                }
        }
        });

        //share only the source of the news to social media or share as message/email
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,postsource);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent comment=new Intent(DetailAboutPost.this, ShowAllComments.class);
                comment.putExtra("heading", anaheading);
                startActivity(comment);
            }
        });
    }
    //Create table in SQLITE for Bookmark When user logged in as Member
    private void createBookmarkTable() {
        mDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS bookmark(heading text, description text, source text," +
                        " time text, author_name text, author_email text)");
    }
//Create table in SQLITE for Bookmark When user logged in as Anonymous
    private void createAnoBookmarkTable(){
        mdatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS anonymous_bookmark(heading text, description text, source text," +
                " time text, author_name text, author_email text)");
    }

    //Check how the user logged into the application : as Member or Anonymous then according functionalities are defined
    public void WhoiAm(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        if(checkemail.length()==0 && deviceid.length()>1) //if the user is Anonymous
        {
            //Toast.makeText(getApplicationContext(), "I am Anonymous", Toast.LENGTH_SHORT).show();
            builder.setMessage("You are an Anonymous User");
            builder.setPositiveButton("comment as Anonymous",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            builder.setNegativeButton("comment as Logged User", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent log=new Intent(DetailAboutPost.this,Login.class);
                    startActivity(log);
                }
            });
            builder.show();
        }
        else if(checkemail.length()>1 && deviceid.length()>1){ //if the user is a Member (as i kept the deviceid so if
            // the user logged in as
            // Member the device id will be there (deviceid is used for recognizing the Anonymous user))

          //  Toast.makeText(getApplicationContext(), "I am an User", Toast.LENGTH_SHORT).show();
            builder.setMessage("You are a Logged User");
            builder.setPositiveButton("comment as Logged User",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            builder.setNegativeButton("comment as Anonymous", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent log=new Intent(DetailAboutPost.this,AnonymousLogin.class);
                    startActivity(log);
                }
            });
            builder.show();
        }
    }


    public void CheckUser(){
        if(checkemail.length()!=1 && deviceid.length()>1){
            useremail=deviceid;
           // Toast.makeText(getApplicationContext(), useremail,Toast.LENGTH_LONG).show();
        }
        else
            useremail=etUsername.getText().toString().trim(); //for user like/dislike sending app user email

    }
    //for adding bookmark all the details of the particular news will be stored in the SQLITE DATABSE
    private void addBookmark() {

        String usersharepost=sharepost.getText().toString().trim();
        String userpostdescription=description.getText().toString().trim();
        String userpostsource=source.getText().toString().trim();
        String userposttime=time.getText().toString().trim();
        String postauthorname=authorname.getText().toString().trim();
        String postauthoremail=authoremail.getText().toString().trim();


            String insertSQL = "INSERT INTO bookmark" +
                    "(heading, description, source, time, author_name, author_email)" +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, ?);";

        mDatabase.execSQL(insertSQL, new String[]{usersharepost, userpostdescription, userpostsource, userposttime,
                    postauthorname, postauthoremail});

            Toast.makeText(this, "Bookmark Added Successfully", Toast.LENGTH_SHORT).show();
        }


        private void anoBookmark(){
            String usersharepost=sharepost.getText().toString().trim();
            String userpostdescription=description.getText().toString().trim();
            String userpostsource=source.getText().toString().trim();
            String userposttime=time.getText().toString().trim();
            String postauthorname=authorname.getText().toString().trim();
            String postauthoremail=authoremail.getText().toString().trim();


            String insertSQL = "INSERT INTO anonymous_bookmark" +
                    "(heading, description, source, time, author_name, author_email)" +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, ?);";

            mdatabase.execSQL(insertSQL, new String[]{usersharepost, userpostdescription, userpostsource, userposttime,
                    postauthorname, postauthoremail});

            Toast.makeText(this, "Bookmark Added Successfully !!!", Toast.LENGTH_SHORT).show();
        }

        //When User/Anonymous hit Post button the all the details of the post with support/against value are sent to the server for storing
    public void PostLikeDislike(final String useremail, final String heading, final String description, final String source,
                                final String timeofpost, final String author_name, final String author_email, final String liked_post,
                                final String disliked_post, final String userposttime, final String usercomment){

        class PostLikeDislikeClass extends AsyncTask<String,Void,String> {

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("useremail",params[0]);

                hashMap.put("heading",params[1]);

                hashMap.put("description",params[2]);

                hashMap.put("source", params[3]);

                hashMap.put("timeofpost", params[4]);

                hashMap.put("author_name",params[5]);

                hashMap.put("author_email", params[6]);

                hashMap.put("liked_post", params[7]);

                hashMap.put("disliked_post", params[8]);

                hashMap.put("userposttime", params[9]);

                hashMap.put("usercomment", params[10]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }

        }

        PostLikeDislikeClass postLikeDislikeClass = new PostLikeDislikeClass();

        postLikeDislikeClass.execute(useremail,heading, description, source,timeofpost, author_name,
                author_email, liked_post, disliked_post ,userposttime, usercomment);
    }

    public void init(){
        description=(TextView)findViewById(R.id.description);
        description.setVisibility(View.INVISIBLE);
        source=(TextView)findViewById(R.id.source);
        time=(TextView)findViewById(R.id.time);
        authorname=(TextView)findViewById(R.id.authorname);
        authoremail=(TextView)findViewById(R.id.authoremail);
        timeofpost=(TextView)findViewById(R.id.timeofpost); //time of like/dislike
        like=(Button)findViewById(R.id.like);
        dislike=(Button)findViewById(R.id.dislike);
        comment=(Button)findViewById(R.id.comment);

        layout1=(RelativeLayout)findViewById(R.id.layout1);
        layout2=(RelativeLayout)findViewById(R.id.layout2);
        layout2.setVisibility(View.INVISIBLE);
        layout3=(RelativeLayout)findViewById(R.id.layout3);
        layout5=(RelativeLayout)findViewById(R.id.layout5);
        layout5.setVisibility(View.INVISIBLE);

        commentpost=(EditText)findViewById(R.id.commentpost);

        more=(TextView)findViewById(R.id.more);

        analytics=(Button)findViewById(R.id.analytics);

    }

    //fetching the details of a particular news against issuing its heading
    public class AsyncFetch extends AsyncTask<String, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(DetailAboutPost.this);
        HttpURLConnection conn;
        URL url = null;
        String searchQuery;

        public AsyncFetch(String searchQuery){
            this.searchQuery=searchQuery;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("https://paritasampa95.000webhostapp.com/PeopleView/PostDetails.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput to true as we send and recieve data
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // add parameter to our above url
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
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
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

            //this method will be running on UI thread
            pdLoading.dismiss();


            pdLoading.dismiss();
            if(result.equals("no rows")) {
                Toast.makeText(DetailAboutPost.this, "No Results found", Toast.LENGTH_LONG).show();
            }else{

                try {

                    JSONArray jArray = new JSONArray(result);

                    // Extract data from json and store into ArrayList as class objects
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.getJSONObject(i);

                        description.setText(json_data.getString("description"));
                        source.setText(json_data.getString("source"));
                        time.setText(json_data.getString("time_of_post"));
                        authorname.setText(json_data.getString("name"));
                        authoremail.setText(json_data.getString("email"));

                        postdescription=description.getText().toString().trim();
                        postsource=source.getText().toString().trim();
                        postpasttime=time.getText().toString().trim();
                        postauthor=authorname.getText().toString().trim();
                        postauthoremail=authoremail.getText().toString().trim();

                       /* Toast.makeText(getApplicationContext(), postdescription, Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), postsource,Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), postpasttime, Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), postauthor, Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), postauthoremail,Toast.LENGTH_LONG).show();*/
                    }


                } catch (JSONException e) {
                    // You to understand what actually error is and handle it appropriately
                    Log.d(e.toString(),"error");
                    Toast.makeText(DetailAboutPost.this, "No Post history is present", Toast.LENGTH_LONG).show();
                }

            }

        }

    }


}
