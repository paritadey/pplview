package com.example.parita.pplview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AnonymousSetting extends AppCompatActivity {
    TextView editpassword,editprofile, anoname, settings;
    RelativeLayout layout;
    String EmailHolder, emailsender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anonymous_setting);

        String id = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        //displaying id in textview
        anoname=(TextView)findViewById(R.id.anoname);
        anoname.setText(id);
        EmailHolder=anoname.getText().toString().trim();
        emailsender=anoname.getText().toString();

        editpassword=(TextView)findViewById(R.id.editpassword);
        editprofile=(TextView)findViewById(R.id.editprofile);
        settings=(TextView)findViewById(R.id.settings);

       editpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chnagepass=new Intent(AnonymousSetting.this, AnonymousEditPassword.class);
             //   chnagepass.putExtra("deviceno", emailsender);
                startActivity(chnagepass);
            }
        });

        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chnageprofile=new Intent(AnonymousSetting.this, AnonymousEditProfile.class);
              //  chnageprofile.putExtra("deviceno", emailsender);
                startActivity(chnageprofile);

            }
        });
    }
}
