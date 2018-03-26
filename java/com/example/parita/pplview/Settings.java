package com.example.parita.pplview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Settings extends AppCompatActivity{
    TextView editpassword,editprofile, etUsername, settings;
    RelativeLayout layout;
    String EmailHolder, emailsender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        editpassword=(TextView)findViewById(R.id.editpassword);
        editprofile=(TextView)findViewById(R.id.editprofile);
        settings=(TextView)findViewById(R.id.settings);

        etUsername=(TextView)findViewById(R.id.etUsername);
        Intent intent = getIntent();
        EmailHolder = intent.getStringExtra("email");
        etUsername.setText(EmailHolder);
        emailsender=etUsername.getText().toString();

        editpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chnagepass=new Intent(Settings.this, EditPassword.class);
                chnagepass.putExtra("email", emailsender);
                startActivity(chnagepass);
            }
        });

        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chnageprofile=new Intent(Settings.this, EditProfile.class);
                chnageprofile.putExtra("email", emailsender);
                startActivity(chnageprofile);

            }
        });
    }
}