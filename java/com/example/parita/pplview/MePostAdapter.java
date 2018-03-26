package com.example.parita.pplview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

/**
 * Created by parita on 02-03-2018.
 */

public class MePostAdapter extends RecyclerView.Adapter<MePostAdapter.ViewHolder> {
    private Context mCtx;
    List<Mepost> productList = Collections.emptyList();
    Mepost current;

    public MePostAdapter(Context mCtx, List<Mepost> productList) {
        this.mCtx = mCtx;
        this.productList = productList;

    }

    @Override
    public MePostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.mepost, null);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ViewHolder myHolder = (ViewHolder) holder;
        Mepost current = productList.get(position);

        myHolder.tvheading.setText(current.heading);
        myHolder.tvdescription.setText(current.description);

        myHolder.tvview.setText(current.support_against);
        myHolder.tvdate.setText(current.date);

    }


    // return total item from List
    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvheading, tvdescription;
        TextView tvview, tvdate;

        // create constructor to get widget reference
        public ViewHolder(View itemView) {
            super(itemView);
            tvheading = itemView.findViewById(R.id.tvheading);
            tvdescription = itemView.findViewById(R.id.tvdescription);
            tvview = itemView.findViewById(R.id.tvview);
            tvdate=itemView.findViewById(R.id.tvdate);
            itemView.setOnClickListener(this);
        }

        // Click event for all items
        @Override
        public void onClick(View v) {

            Toast.makeText(mCtx, "My Post details", Toast.LENGTH_SHORT).show();

        }

    }
}
