package com.example.parita.pplview;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class Registration extends AppCompatActivity {
    EditText fullname, email, phone, address, password, retypepass;
    TextView setdate, loginlink;
    Spinner sex, edude;
    Button dob, reg;
    DatePickerDialog.OnDateSetListener datePickerListener;
    private Calendar myCalendar = Calendar.getInstance();
    Boolean CheckEditText,passwordmatch ;
    ProgressDialog progressDialog;
    String uname, uemail, uphone, uadd, usetdate, ugen, uedu, upass, urepass;
    String finalResult ;
    String HttpURL="https://paritasampa95.000webhostapp.com/PeopleView/registration.php";
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    String chk_gender="Choose Gender", chk_details="Choose Educational Details";
    Context apContext, ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        init();
        loginlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent log=new Intent(Registration.this, Login.class);
                log.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //FLAG_ACTIVITY_NEW_TASK, which makes that activity the start of a new task on the history stack.
                // when FLAG_ACTIVITY_CLEAR_TOP goes to find the new activity in the stack,
                // it'll be there and be pulled up before everything else is cleared.
                startActivity(log);
                finish();
            }
        });

       datePickerListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                setdate.setText(sdf.format(myCalendar.getTime()));
            }

        };
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Registration.this, datePickerListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckEditTextIsEmptyOrNot();
                Checkpassword();

                if (CheckEditText && passwordmatch) {

                    // If EditText is not empty and CheckEditText = True then this block will execute.

                    UserRegisterFunction(uemail,uname, uphone, upass, uadd, usetdate,ugen,uedu);


                } else {
                    // If EditText is empty then this block will execute .
                    Toast.makeText(Registration.this, "Please fill all form fields correctly.",
                            Toast.LENGTH_LONG).show();

                }
            }
        });
    }
    public void UserRegisterFunction(final String email,  final String name, final String phone,final String password,
                                     final String dob, final String gender, final String address, final String edu_details){

        class UserRegisterFunctionClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                isConnectingToInternet(apContext);
                progressDialog = ProgressDialog.show(Registration.this,"Registering...",null,true,true);
            }



            @Override
            protected String doInBackground(String... params) {

                hashMap.put("email",params[0]);

                hashMap.put("name",params[1]);

                hashMap.put("phone",params[2]);

                hashMap.put("password", params[3]);

                hashMap.put("address", params[4]);

                hashMap.put("dob",params[5]);

                hashMap.put("gender", params[6]);

                hashMap.put("edu_details", params[7]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);
                isInternet(ctx);
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Server Result: "+httpResponseMsg, Toast.LENGTH_LONG).show();
                clearTextFields();
            }
        }

        UserRegisterFunctionClass userRegisterFunctionClass = new UserRegisterFunctionClass();

        userRegisterFunctionClass.execute(email,name, phone, password,dob, gender, address, edu_details);
    }

    //check whether the application is connected to the internet or not
    private boolean isConnectingToInternet(Context applicationContext){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            Toast.makeText(getApplicationContext(), "no internet", Toast.LENGTH_SHORT).show();
            Toast.makeText(Registration.this,"Connect to the Wifi/Mobile Data",Toast.LENGTH_LONG).show();
            return false;
        } else
            return true;

    }

    private boolean isInternet(Context applicationContext){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            Toast.makeText(getApplicationContext(), "Registration cannot be possible without Internet", Toast.LENGTH_SHORT).show();
            return false;
        } else{
            return true;
        }


    }
    public void CheckEditTextIsEmptyOrNot(){
        uname=fullname.getText().toString().trim();
        uemail=email.getText().toString().trim();
        uphone=phone.getText().toString().trim();
        uadd=address.getText().toString().trim();
        usetdate=setdate.getText().toString().trim();
        ugen=sex.getSelectedItem().toString();
        uedu=edude.getSelectedItem().toString();
        upass=password.getText().toString().trim();
        urepass=retypepass.getText().toString();

        if(TextUtils.isEmpty(uemail) || TextUtils.isEmpty(uname) || TextUtils.isEmpty(uphone)
                || TextUtils.isEmpty(uadd) || TextUtils.isEmpty(usetdate) || TextUtils.isEmpty(ugen) || TextUtils.isEmpty(uedu)
                || TextUtils.isEmpty(upass))
        {
            CheckEditText = false;
        }
        else {
            if(ugen.equals(chk_gender) || uedu.equals(chk_details)){
                CheckEditText = false ;
            }
            else {
                CheckEditText = true;
            }
        }
    }

    public void Checkpassword(){
        if(password.getText().toString().equals(retypepass.getText().toString()) && password.length()>0 && retypepass.length()>0){
            passwordmatch=true;
            //Toast is the pop up message
            Toast.makeText(getApplicationContext(), "Password match",
                    Toast.LENGTH_LONG).show();
        }
        else{
                passwordmatch = false;
                //Toast is the pop up message
                Toast.makeText(getApplicationContext(), "Password does not match!",
                        Toast.LENGTH_LONG).show();

        }
    }
    public void clearTextFields(){
        fullname.setText("");
        email.setText("");
        phone.setText("");
        address.setText("");
        setdate.setText("");
        edude.setSelection(0);
        sex.setSelection(0);
        password.setText("");
        retypepass.setText("");
    }

    public void init(){
        fullname=(EditText)findViewById(R.id.name);
        email=(EditText)findViewById(R.id.mailid);
        phone=(EditText)findViewById(R.id.userphoneno);
        address=(EditText)findViewById(R.id.place);
        setdate=(TextView) findViewById(R.id.dateset);
        password=(EditText)findViewById(R.id.password);
        retypepass=(EditText)findViewById(R.id.retypepassword);

       edude=(Spinner)findViewById(R.id.education_details);
        ArrayAdapter<CharSequence> detailsadaptar = ArrayAdapter.createFromResource(this, R.array.edu_details,
                android.R.layout.simple_spinner_item);
        detailsadaptar.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edude.setAdapter(detailsadaptar);

        sex=(Spinner)findViewById(R.id.gender);
        ArrayAdapter<CharSequence> genadaptar = ArrayAdapter.createFromResource(this, R.array.gen,
                android.R.layout.simple_spinner_item);
        genadaptar.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sex.setAdapter(genadaptar);

        dob=(Button)findViewById(R.id.date);
        reg=(Button)findViewById(R.id.register);
        loginlink=(TextView)findViewById(R.id.loginlink);
    }
}
