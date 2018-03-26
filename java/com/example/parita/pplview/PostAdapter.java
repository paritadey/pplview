package com.example.parita.pplview;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by parita on 26-02-2018.
 */

public class PostAdapter extends ArrayAdapter<Posts> {

    Context mCtx;
    int listLayoutRes;
    List<Posts> postsList;
    SQLiteDatabase mDatabase;
    String desc;

    public PostAdapter(Context mCtx, int listLayoutRes, List<Posts> postsList, SQLiteDatabase mDatabase) {
        super(mCtx, listLayoutRes, postsList);

        this.mCtx = mCtx;
        this.listLayoutRes = listLayoutRes;
        this.postsList = postsList;
        this.mDatabase = mDatabase;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(listLayoutRes, null);

        //getting employee of the specified position
        final Posts posts = postsList.get(position);


        //getting views
        TextView textViewheading = view.findViewById(R.id.textViewheading);
        TextView textViewdescription=view.findViewById(R.id.textViewdescription);
        TextView textViewSource = view.findViewById(R.id.textViewSource);
        TextView textViewtime=view.findViewById(R.id.textViewtime);
        TextView textVewAuthorname = view.findViewById(R.id.textVewAuthorname);
        TextView textViewAuthoremail = view.findViewById(R.id.textViewAuthoremail);

        //adding data to views
        textViewheading.setText(posts.getHeading_post());
        textViewdescription.setText(posts.getDescription());
        textViewSource.setText(posts.getSource());
        textViewtime.setText(posts.getTimeofpost());
        textVewAuthorname.setText(String.valueOf(posts.getAuthorname()));
        textViewAuthoremail.setText(posts.getAuthoremail());

        desc=textViewdescription.getText().toString();

        Button delete = view.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String sql = "DELETE FROM bookmark WHERE heading = ?";
                        mDatabase.execSQL(sql, new String[]{posts.getHeading_post()});
                        reloadEmployeesFromDatabase();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
        return view;
    }

    private void reloadEmployeesFromDatabase() {
        Cursor cursorEmployees = mDatabase.rawQuery("SELECT * FROM bookmark", null);
        if (cursorEmployees.moveToFirst()) {
            postsList.clear();
            do {
                postsList.add(new Posts(
                        cursorEmployees.getString(0),
                        cursorEmployees.getString(1),
                        cursorEmployees.getString(2),
                        cursorEmployees.getString(3),
                        cursorEmployees.getString(4),
                        cursorEmployees.getString(5)
                ));
            } while (cursorEmployees.moveToNext());
        }
        cursorEmployees.close();
        notifyDataSetChanged();
    }
}
