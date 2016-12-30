package com.example.luxkiz1.movie_projet.MainAlbum;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.luxkiz1.movie_projet.DetailScreenActivity;
import com.example.luxkiz1.movie_projet.MainScreenActivity;
import com.example.luxkiz1.movie_projet.R;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder> {

    public Context mContext;
    public List<Album> albumList;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title;
        public ImageView thumbnail, overflow;
        private ItemClickListener itemClickListener;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);

            view.setOnClickListener(this);
        }


        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(getLayoutPosition());
        }
    }


    public AlbumsAdapter(Context mContext, List<Album> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Album album = albumList.get(position);
        holder.title.setText(album.getName());

        holder.overflow.setImageResource(R.drawable.nofavo);
        // Kiem tra favo
        GraphRequest req = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject jsonObject, GraphResponse response) {

                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Favorite");

                        try {
                            query.whereEqualTo("email", response.getJSONObject().getString("email"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("----------------->", "khong lay duoc email");
                        }
                        query.findInBackground(new FindCallback<ParseObject>() {
                            public void done(List<ParseObject> movieList, ParseException e) {
                                if (e == null) {


                                    for (ParseObject info : movieList) {
                                        if (album.getId().equals(info.getString("idMoive"))) {

                                            holder.overflow.setImageResource(R.drawable.icon_rating);
                                        }else {

                                        }

                                    }

                                } else {
                                    Log.d("----------------->", "Khong thuc hien");
                                }
                            }
                        });

                    }

                });
        final Bundle bundle = new Bundle();
        bundle.putString("fields", "id, name,email,gender,birthday,picture.height(300).width(300)");
        req.setParameters(bundle);
        req.executeAsync();


        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkImageResource(view.getContext(), holder.overflow, R.drawable.nofavo)) {


                    holder.overflow.setImageResource(R.drawable.icon_rating);
                    // Add vao database
                    GraphRequest req = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject jsonObject, GraphResponse response) {

                                    try {

                                        ParseObject gameScore = new ParseObject("Favorite");
                                        gameScore.put("Username", response.getJSONObject().getString("name"));
                                        gameScore.put("email", response.getJSONObject().getString("email"));
                                        gameScore.put("idMoive", album.getId());
                                        gameScore.put("TenPhim", album.getName());
                                        gameScore.saveInBackground();


                                    } catch (JSONException e1) {
                                        Log.d("----------------->", "2");
                                    }

                                }


                            });
                    Bundle bundle = new Bundle();
                    bundle.putString("fields", "id, name,email,gender,birthday,picture.height(300).width(300)");
                    req.setParameters(bundle);
                    req.executeAsync();

                } else {


                    // Xoa khoi database
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Favorite");
                    query.whereEqualTo("idMoive", album.getId());
                    query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> scoreList, ParseException e) {
                            if (e == null) {
                                scoreList.get(0).deleteInBackground();


                            } else {
                                Log.d("----------------->", "Khong xoa duoc");
                            }
                        }
                    });
                    holder.overflow.setImageResource(R.drawable.nofavo);

                }
            }
        });

        album.getThumbnail().getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] bytes, ParseException e) {

                if (e == null) {

                    try {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(
                                album.getThumbnail().getData(),
                                0,
                                album.getThumbnail().getData().length
                        );
                        holder.thumbnail.setImageBitmap(bitmap);
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                } else {

                }

            }
        });


        // Bat su kien click
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //((MainScreenActivity) v.getContext()).ChuyenDenDetailActivity(album.getId());
                ((MainScreenActivity) ((ContextWrapper) v.getContext()).getBaseContext()).ChuyenDenDetailActivity(album.getId());
            }
        });


    }


    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public static boolean checkImageResource(Context ctx, ImageView imageView,
                                             int imageResource) {
        boolean result = false;

        if (ctx != null && imageView != null && imageView.getDrawable() != null) {
            Drawable.ConstantState constantState;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                constantState = ctx.getResources()
                        .getDrawable(imageResource, ctx.getTheme())
                        .getConstantState();
            } else {
                constantState = ctx.getResources().getDrawable(imageResource)
                        .getConstantState();
            }

            if (imageView.getDrawable().getConstantState() == constantState) {
                result = true;
            }
        }

        return result;
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }


}
