package com.example.parita.pplview;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class Login extends AppCompatActivity {
    EditText useremail, userpassword;
    Button login;
    TextView register, forgotpass, anonymous;
    String PasswordHolder, EmailHolder;
    boolean CheckEditText;
    String finalResult ;
    CheckBox checkBox;
    String HttpURL="https://paritasampa95.000webhostapp.com/PeopleView/login.php";
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    public static final String UserEmail = "";
    Context apContext;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Email = "emailKey";
    public static final String Password = "passwordkey";

    AlertDialog.Builder alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


       // showPopupWindow();
        init();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckEditTextIsEmptyOrNot();

                if(CheckEditText){

                    //saveMyData(EmailHolder, PasswordHolder);
                    UserLoginFunction(EmailHolder, PasswordHolder);

                }
                else {

                    Toast.makeText(Login.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();

                }
            }
        });

        anonymous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ano=new Intent(Login.this, AnonymousLogin.class);
                startActivity(ano);
            }
        });
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pass=new Intent(Login.this, ForgotPassword.class);
                startActivity(pass);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regis=new Intent(Login.this, Registration.class);
                startActivity(regis);
            }
        });
       // alertmsg();

    }

    public void saveMyData(final String EmailHolder, final String PasswordHolder){
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString("emailKey", EmailHolder);
        editor.putString("passwordkey", PasswordHolder);
        editor.commit();
        Toast.makeText(Login.this,"Your credentials are saved",Toast.LENGTH_LONG).show();
}

    public void UserLoginFunction(final String email, final String password){

        class UserLoginClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                isConnectingToInternet(apContext);
                progressDialog = ProgressDialog.show(Login.this,"Authenticating...",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                if(httpResponseMsg.equalsIgnoreCase("Data Matched")){

                    finish();

                    Intent intent = new Intent(Login.this, HomeScreen.class);
                    intent.putExtra("email", email);
                    startActivity(intent);

                }
                else{
                    Toast.makeText(Login.this,"Error Occured",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("email",params[0]);

                hashMap.put("password",params[1]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        UserLoginClass userLoginClass = new UserLoginClass();

        userLoginClass.execute(email,password);
    }
    @Override
    public void onBackPressed() {
       //super.onBackPressed();
       // System.exit(0);
       // super.finish();//kill the app

        //   Intent log=new Intent(Login.this, Login.class);
      //  log.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //FLAG_ACTIVITY_NEW_TASK, which makes that activity the start of a new task on the history stack.
        // when FLAG_ACTIVITY_CLEAR_TOP goes to find the new activity in the stack,
        // it'll be there and be pulled up before everything else is cleared.
     //   startActivity(log);
     //   finish();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Do you want to Exit? ");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
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
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent log=new Intent(Login.this, Login.class);
                startActivity(log);
            }
        });
        builder.show();



    }
    public void showPopupWindow(){
        try{

            LayoutInflater li = LayoutInflater.from(getApplicationContext());
            final View prompt = li.inflate(R.layout.custome_login_dialog, null);
            final AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder
                    (Login.this);
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setView(prompt);
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
    //check whether the application is connected to the internet or not
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

        EmailHolder = useremail.getText().toString();
        PasswordHolder = userpassword.getText().toString();

        if(TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(PasswordHolder))
        {
            CheckEditText = false;

        }
        else {

            CheckEditText = true ;
        }
    }


    public void init(){
        useremail=(EditText)findViewById(R.id.mailid);
        userpassword=(EditText)findViewById(R.id.password);
        login=(Button)findViewById(R.id.login);
        register=(TextView)findViewById(R.id.regislink);
        forgotpass=(TextView)findViewById(R.id.forgotpass);
        anonymous=(TextView)findViewById(R.id.anonymous);
    }


}
