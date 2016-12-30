package com.example.luxkiz1.movie_projet.Comment;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.luxkiz1.movie_projet.R;
import com.pkmmte.view.CircularImageView;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    public Context c;
    public List<CommentList> commentLists;


    public CommentAdapter(Context mContext, List<CommentList> commentLists) {
        this.c = mContext;
        this.commentLists = commentLists;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView username, comment;
        public CircularImageView photo;

        public MyViewHolder(View itemView) {
            super(itemView);

            username = (TextView) itemView.findViewById(R.id.username);
            comment = (TextView) itemView.findViewById(R.id.comment);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

       final CommentList comment = commentLists.get(position);
        holder.username.setText(comment.getUsername());
        holder.comment.setText(comment.getComment());
    }

    @Override
    public int getItemCount() {
        return commentLists.size();
    }


}
