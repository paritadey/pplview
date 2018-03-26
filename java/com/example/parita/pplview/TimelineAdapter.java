package com.example.parita.pplview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by parita on 22-02-2018.
 */

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ViewHolder> {
    private Context mCtx;
    List<Timeline> productList = Collections.emptyList();
    Timeline current;

    public TimelineAdapter(Context mCtx, List<Timeline> productList) {
        this.mCtx = mCtx;
        this.productList = productList;

    }


    @Override
    public TimelineAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.timeline_activity_log, null);
        TimelineAdapter.ViewHolder holder=new TimelineAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(TimelineAdapter.ViewHolder holder, int position) {
        TimelineAdapter.ViewHolder myHolder = (TimelineAdapter.ViewHolder) holder;
        Timeline current = productList.get(position);

        myHolder.textViewheading.setText(current.heading);
        myHolder.textViewdescription.setText(current.description);
        myHolder.textViewcomment.setText(current.comment);
        myHolder.textViewtime.setText(current.userposttime);

    }


    // return total item from List
    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewheading, textViewdescription, textViewcomment, textViewtime;

        // create constructor to get widget reference
        public ViewHolder(View itemView) {
            super(itemView);
            textViewheading = itemView.findViewById(R.id.textViewheading);
            textViewdescription = itemView.findViewById(R.id.textViewdescription);
            textViewcomment=itemView.findViewById(R.id.textViewcomment);
            textViewtime=itemView.findViewById(R.id.textViewtime);

            itemView.setOnClickListener(this);
        }

        // Click event for all items
        @Override
        public void onClick(View v) {

            //Toast.makeText(mCtx, "Orderd item details", Toast.LENGTH_SHORT).show();

        }

    }
}
