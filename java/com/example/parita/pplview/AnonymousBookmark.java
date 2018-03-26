package com.example.parita.pplview;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AnonymousBookmark extends AppCompatActivity {

    TextView anoname;
    List<Posts> postsList;
    SQLiteDatabase mdatabase;
    ListView listViewBookmark;
    BookmarkAdapter adapter;
    String EmailHolder, myemail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anonymous_bookmark);

        String id = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        anoname=(TextView)findViewById(R.id.anoname);
        anoname.setText(id);
        EmailHolder=anoname.getText().toString().trim();
        myemail=anoname.getText().toString().trim();
        listViewBookmark = (ListView) findViewById(R.id.listViewBookmark);
        postsList = new ArrayList<>();

        //opening the database
        mdatabase = openOrCreateDatabase(DetailAboutPost.DATABASE_NAME2, MODE_PRIVATE, null);

        //this method will display the employees in the list
        showEmployeesFromDatabase();
    }
    private void showEmployeesFromDatabase() {
        //we used rawQuery(sql, selectionargs) for fetching all the employees
        Cursor cursorPost = mdatabase.rawQuery("SELECT * FROM anonymous_bookmark", null);

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
        adapter = new BookmarkAdapter(this, R.layout.list_layout_bookmark, postsList, mdatabase);

        //adding the adapter to listview
        listViewBookmark.setAdapter(adapter);
    }
}
