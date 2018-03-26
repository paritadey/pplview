package com.example.parita.pplview;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Bookmark extends AppCompatActivity {
    List<Posts> postsList;
    SQLiteDatabase mDatabase;
    ListView listViewBookmark;
    PostAdapter adapter;
    TextView etUsername;
    String EmailHolder, myemail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        etUsername=(TextView)findViewById(R.id.etUsername);
        Intent intent = getIntent();
        EmailHolder = intent.getStringExtra("email");
        etUsername.setText(EmailHolder);
        myemail= etUsername.getText().toString().trim();

        listViewBookmark = (ListView) findViewById(R.id.listViewBookmark);
        postsList = new ArrayList<>();

        //opening the database
        mDatabase = openOrCreateDatabase(DetailAboutPost.DATABASE_NAME, MODE_PRIVATE, null);

        //this method will display the employees in the list
        showEmployeesFromDatabase();
    }
    private void showEmployeesFromDatabase() {
        //we used rawQuery(sql, selectionargs) for fetching all the employees
        Cursor cursorPost = mDatabase.rawQuery("SELECT * FROM bookmark", null);

        //if the cursor has some data
        if (cursorPost.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                postsList.add(new Posts(
                        cursorPost.getString(0),
                        cursorPost.getString(1),
                        cursorPost.getString(2),
                        cursorPost.getString(3),
                        cursorPost.getString(4),
                        cursorPost.getString(5)
                ));
            } while (cursorPost.moveToNext());
        }
        //closing the cursor
        cursorPost.close();

        //creating the adapter object
        adapter = new PostAdapter(this, R.layout.list_layout_bookmark, postsList, mDatabase);

        //adding the adapter to listview
        listViewBookmark.setAdapter(adapter);
    }
}
