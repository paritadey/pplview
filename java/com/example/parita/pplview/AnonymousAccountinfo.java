package com.example.parita.pplview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AnonymousAccountinfo extends AppCompatActivity {
    Button mydetails,mytimeline, mybookmark, mypost;

    TextView anoname,deviceid;
    String anonymous, adeviceid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anonymous_accountinfo);
        init();
        Intent intent = getIntent();
        anonymous=intent.getStringExtra("anonymous");
        anoname.setText(anonymous);

        String id = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        //displaying id in textview
        deviceid=(TextView)findViewById(R.id.deviceid);
        deviceid.setText(id);
        adeviceid=deviceid.getText().toString().trim();

        mydetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent details=new Intent(AnonymousAccountinfo.this, AnonymousUserDetails.class);
                details.putExtra("anonymous", adeviceid);
                startActivity(details);
            }
        });
        mytimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent log=new Intent(AnonymousAccountinfo.this, AnonymousActivityLog.class);
                startActivity(log);
            }
        });
        mypost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent post=new Intent(AnonymousAccountinfo.this, AnonymousPost.class);
                startActivity(post);
            }
        });
        mybookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mark=new Intent(AnonymousAccountinfo.this, AnonymousBookmark.class);
                startActivity(mark);
            }
        });
    }
    public void init(){
        anoname=(TextView)findViewById(R.id.anoname);

        mydetails=(Button)findViewById(R.id.mydetails);
        mytimeline=(Button)findViewById(R.id.mytimeline);
        mybookmark=(Button)findViewById(R.id.mybookmark);
        mypost=(Button)findViewById(R.id.mypost);
    }
}
