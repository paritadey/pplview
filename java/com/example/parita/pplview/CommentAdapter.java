package com.example.parita.pplview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
/**
 * Created by parita on 21-02-2018.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private Context mCtx;
    List<Comment> productList ;


    public CommentAdapter(Context mCtx, List<Comment> productList) {
        this.mCtx = mCtx;
        this.productList = productList;

    }
    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.comment_details, null);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ViewHolder myHolder = (ViewHolder) holder;
        Comment current = productList.get(position);

        myHolder.textViewComment.setText(current.comment);
        myHolder.textViewUser.setText(current.useremail);
        myHolder.textViewtimeofpost.setText(current.userposttime);

    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewComment, textViewUser, textViewtimeofpost;

        // create constructor to get widget reference
        public ViewHolder(View itemView) {
            super(itemView);
            textViewComment = itemView.findViewById(R.id.textViewComment);
            textViewUser = itemView.findViewById(R.id.textViewUser);
            textViewtimeofpost=itemView.findViewById(R.id.textViewtimeofpost);
            itemView.setOnClickListener(this);
        }

        // Click event for all items
        @Override
        public void onClick(View v) {

            //Toast.makeText(mCtx, "Orderd item details", Toast.LENGTH_SHORT).show();

        }

    }
}
