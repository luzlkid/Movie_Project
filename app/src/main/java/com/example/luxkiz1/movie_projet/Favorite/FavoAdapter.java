package com.example.luxkiz1.movie_projet.Favorite;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luxkiz1.movie_projet.FavoriteScreenActivity;
import com.example.luxkiz1.movie_projet.MainAlbum.ItemClickListener;
import com.example.luxkiz1.movie_projet.MainScreenActivity;
import com.example.luxkiz1.movie_projet.R;
import com.parse.GetDataCallback;
import com.parse.ParseException;

import java.util.List;


public class FavoAdapter extends RecyclerView.Adapter<FavoAdapter.MyViewHolder> {

    public Context c;
    public List<Favo> Favolist;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView titlefavo, tacgiafavo;
        public ImageView thumbfavo;
        private ItemClickListener itemClickListener;

        public MyViewHolder(View itemView) {
            super(itemView);

            titlefavo = (TextView) itemView.findViewById(R.id.titlefavo);
            tacgiafavo = (TextView) itemView.findViewById(R.id.tacgiafavo);
            thumbfavo = (ImageView) itemView.findViewById(R.id.thumbfavo);

            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(getLayoutPosition());
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemfavo, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder,final int position) {

        final Favo favo = Favolist.get(position);
        holder.titlefavo.setText(favo.getTitlefavo());
        holder.tacgiafavo.setText(favo.getTacgiafavo());


        favo.getThumbfavo().getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] bytes, ParseException e) {

                if (e == null) {

                    try {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(
                                favo.getThumbfavo().getData(),
                                0,
                                favo.getThumbfavo().getData().length
                        );
                        holder.thumbfavo.setImageBitmap(bitmap);
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                } else {

                }

            }
        });



        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int pos) {
            }
        });

    }

    @Override
    public int getItemCount() {
        return Favolist.size();
    }

    public FavoAdapter(Context mContext, List<Favo> favolist) {
        this.c = mContext;
        this.Favolist = favolist;
    }



}
