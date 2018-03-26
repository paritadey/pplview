package com.example.parita.pplview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Account_info extends AppCompatActivity {

    TextView etUsername;
    Button mydetails,mytimeline, mybookmark, mypost;
    String myemail;
    String EmailHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);

        init();
        etUsername=(TextView)findViewById(R.id.etUsername);
        Intent intent = getIntent();
        EmailHolder = intent.getStringExtra("email"); //Emailid of the user
        etUsername.setText(EmailHolder);
        myemail= etUsername.getText().toString().trim();


        mydetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent details=new Intent(Account_info.this, UserDetails.class);
                details.putExtra("email", myemail);
                startActivity(details);
            }
        });
        mytimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent log=new Intent(Account_info.this, ActivityLog.class);
                log.putExtra("email", myemail);
                startActivity(log);
            }
        });
        mybookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mark=new Intent(Account_info.this, Bookmark.class);
                mark.putExtra("email",myemail);
                startActivity(mark);
            }
        });
        mypost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent post=new Intent(Account_info.this, MyPostHistory.class);
                post.putExtra("email", myemail);
                startActivity(post);
            }
        });
    }
    public void init(){
        mydetails=(Button)findViewById(R.id.mydetails);
        mytimeline=(Button)findViewById(R.id.mytimeline);
        mybookmark=(Button)findViewById(R.id.mybookmark);
        mypost=(Button)findViewById(R.id.mypost);
    }


}
