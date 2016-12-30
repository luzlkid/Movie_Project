package com.example.luxkiz1.movie_projet;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.luxkiz1.movie_projet.Favorite.Favo;
import com.example.luxkiz1.movie_projet.Favorite.FavoAdapter;
import com.example.luxkiz1.movie_projet.MainAlbum.Album;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FavoriteScreenActivity extends AppCompatActivity {
    private SwipeRefreshLayout swiperfavo;
    private RecyclerView recFavo;
    private FavoAdapter favoAdapter;
    private List<Favo> favoList;
    private Toolbar toolbar;

    ArrayList<String> TenPhim = new ArrayList<String>();
    ArrayList<String> TacGia = new ArrayList<String>();
    ArrayList<ParseFile> Thumbnails = new ArrayList<ParseFile>();
    ArrayList<String> idObj = new ArrayList<String>();

    ArrayList<String> idMoive = new ArrayList<>();

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_screen);

        swiperfavo = (SwipeRefreshLayout) findViewById(R.id.swiperFavo);
        recFavo = (RecyclerView) findViewById(R.id.recycleFavo);
        toolbar = (Toolbar) findViewById(R.id.toolbarfavo);
        setSupportActionBar(toolbar);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        favoList = new ArrayList<>();
        favoAdapter = new FavoAdapter(this, favoList);

        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        recFavo.setLayoutManager(llm);
        recFavo.setAdapter(favoAdapter);
        LoadDataFromMovieInfor();


        swiperfavo.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    public void LoadDataFromMovieInfor() {

        favoList.clear();
        TenPhim.clear();
        TacGia.clear();
        Thumbnails.clear();
        idObj.clear();
        idMoive.clear();
        recFavo.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        GraphRequest req = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject jsonObject, GraphResponse response) {

                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Favorite");

                        try {
                            query.whereEqualTo("email", response.getJSONObject().getString("email"));

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                        query.findInBackground(new FindCallback<ParseObject>() {
                            public void done(List<ParseObject> movieList, ParseException e) {
                                if (e == null) {

                                    for (ParseObject info : movieList) {
                                        idMoive.add(info.getString("idMoive"));

                                    }

                                    ParseQuery<ParseObject> query = ParseQuery.getQuery("MovieInfor");
                                    query.findInBackground(new FindCallback<ParseObject>() {
                                        public void done(List<ParseObject> movieList, ParseException e) {
                                            if (e == null) {

                                                for (ParseObject info : movieList) {
                                                    for (int i = 0; i < idMoive.size(); i++) {
                                                        if (info.getObjectId().equals(idMoive.get(i))) {
                                                            TenPhim.add(info.getString("TenPhim"));
                                                            TacGia.add(info.getString("TacGia"));
                                                            Thumbnails.add(info.getParseFile("Thumbnails"));
                                                            idObj.add(info.getObjectId());
                                                        }
                                                    }
                                                }


                                                for (int i = TenPhim.size() - 1; i >= 0; i--) {
                                                    Favo current = new Favo();

                                                    current.setTitlefavo(TenPhim.get(i));
                                                    current.setTacgiafavo(TacGia.get(i));
                                                    current.setThumbfavo(Thumbnails.get(i));
                                                    current.setId(idObj.get(i));

                                                    favoList.add(current);
                                                }

                                                favoAdapter.notifyDataSetChanged();

                                                recFavo.setVisibility(View.VISIBLE);
                                                progressBar.setVisibility(View.GONE);

                                            } else {

                                                Log.d("----------------->", "day ne");
                                            }
                                        }
                                    });



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





    }


    private void refresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                LoadDataFromMovieInfor();
                swiperfavo.setRefreshing(false);
            }
        }, 3000);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_favo_toolbar, menu);


        return super.onCreateOptionsMenu(menu);
    }
}
