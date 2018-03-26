package com.example.parita.pplview;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class AnonymousLogin extends AppCompatActivity {

    Context apContext;
    RelativeLayout mRelativeLayout;
     Context mContext;
    String adeviceid, aname, PasswordHolder;
    TextView tvdid, name, registerlink;
    EditText password;
    Button login;
    boolean CheckEditText;
    String finalResult ;
    String HttpURL="https://paritasampa95.000webhostapp.com/PeopleView/anonymousLogin.php";
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    public static final String UserName = "";
    ImageButton closePopup;
    private  PopupWindow popupWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anonymous_login);

        mRelativeLayout=(RelativeLayout)findViewById(R.id.mrelative);
        showPopupWindow();
        login=(Button)findViewById(R.id.login);
        String id = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        //displaying id in textview
        TextView deviceid=(TextView)findViewById(R.id.deviceid);
        deviceid.setText(id);
        adeviceid=deviceid.getText().toString().trim();
        deviceid.setVisibility(View.INVISIBLE);

        tvdid=(TextView)findViewById(R.id.textViewdeviceid);
        tvdid.setVisibility(View.INVISIBLE);

        name=(TextView)findViewById(R.id.name);
        aname=name.getText().toString().trim();

        password=(EditText)findViewById(R.id.password);
        PasswordHolder=password.getText().toString().trim();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckEditTextIsEmptyOrNot();

                if(CheckEditText){

                    UserLoginFunction(adeviceid, aname, PasswordHolder);

                }
                else {

                    Toast.makeText(AnonymousLogin.this, "Enter Password", Toast.LENGTH_LONG).show();

                }
            }
        });
        registerlink=(TextView)findViewById(R.id.registerlink);
        registerlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register=new Intent(AnonymousLogin.this, Annonymous.class);
                startActivity(register);
            }
        });
    }

    public void showPopupWindow(){
        try{

            LayoutInflater li = LayoutInflater.from(getApplicationContext());
            final View prompt = li.inflate(R.layout.custom_layout, null);
            final AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder
                    (AnonymousLogin.this);
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

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setCancelable(false);
        builder.setMessage("How do you continue? ");
        builder.setPositiveButton("Login as Member", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent member=new Intent(AnonymousLogin.this, Login.class);
                startActivity(member);
            }
        });
        builder.setNegativeButton("Login as Anonymous", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
               // Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        builder.setNeutralButton("Exit application", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                //This code will completely stop the application.
                System.runFinalizersOnExit(true);
                System.exit(0);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        builder.show();



    }

    public void UserLoginFunction(final String deviceid, final String name, final String password){

        class UserLoginClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                isConnectingToInternet(apContext);
                progressDialog = ProgressDialog.show(AnonymousLogin.this,"Authenticating...",
                        null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                if(httpResponseMsg.equalsIgnoreCase("Data Matched")){

                    finish();

                    Intent intent = new Intent(AnonymousLogin.this, HomeScreen.class);
                    intent.putExtra(UserName, name);
                    startActivity(intent);

                }
                else{
                    Toast.makeText(AnonymousLogin.this,"Error Occured",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("deviceid",params[0]);

                hashMap.put("name", params[1]);

                hashMap.put("password",params[2]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        UserLoginClass userLoginClass = new UserLoginClass();

        userLoginClass.execute(deviceid,name, password);
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
    public void CheckEditTextIsEmptyOrNot(){

        PasswordHolder = password.getText().toString();

        if(TextUtils.isEmpty(PasswordHolder))
        {
            CheckEditText = false;

        }
        else {

            CheckEditText = true ;
        }
    }
}
