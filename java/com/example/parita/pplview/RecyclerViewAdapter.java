package com.example.parita.pplview;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by parita on 18-02-2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;

    List<DataAdapter> dataAdapters;


    public RecyclerViewAdapter(List<DataAdapter> getDataAdapter, Context context) {

        super();

        this.dataAdapters = getDataAdapter;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_details, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {

        DataAdapter dataAdapter = dataAdapters.get(position);

        viewHolder.textViewHeading.setText(dataAdapter.getHeading());

        final String post = viewHolder.textViewHeading.getText().toString().trim();
        //final String attempt="1";
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), DetailAboutPost.class);
                i.putExtra("heading", post);
             //   i.putExtra("attempt", attempt);
                v.getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {

        return dataAdapters.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewHeading;

        public ViewHolder(View itemView) {

            super(itemView);

            textViewHeading = itemView.findViewById(R.id.textViewHeading);

        }


    }


}
