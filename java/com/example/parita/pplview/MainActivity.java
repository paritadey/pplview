package com.example.parita.pplview;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    ActionBar actionBar;
    //public static final String MyPREFERENCES = "MyPrefs" ;
   // SharedPreferences prefs;
   // int havevalue=0;
    String name, pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actionBar=getSupportActionBar();
        actionBar.hide();

       // checkCredentials();


        //actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // TODO: Your application init goes here.
            //    if(havevalue>0){
                    Intent mInHome = new Intent(MainActivity.this, Login.class);
                   // mInHome.putExtra("emailKey", name);
                    MainActivity.this.startActivity(mInHome);
                    MainActivity.this.finish();
               // }
               /* else{
                    Intent mInHome = new Intent(MainActivity.this, Login.class);
                    MainActivity.this.startActivity(mInHome);
                    MainActivity.this.finish();
                }*/
            }
        }, 5000);

    }
  /*  public void checkCredentials(){
        prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String restoredEmail = prefs.getString("emailKey", null);
        String restoredPass =prefs.getString("passwordkey", null);
        if (restoredEmail != null && restoredPass !=null) {
            name = prefs.getString("emailKey", "No name defined");//"No name defined" is the default value.
            pass=prefs.getString("passwordkey", "No password defined");
            havevalue++;
           // Toast.makeText(getApplicationContext(), name+pass, Toast.LENGTH_LONG).show();

        }
    }*/
}
