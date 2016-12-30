package com.example.luxkiz1.movie_projet;

import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luxkiz1.movie_projet.Comment.CommentAdapter;
import com.example.luxkiz1.movie_projet.Comment.CommentList;
import com.example.luxkiz1.movie_projet.MainAlbum.Album;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.pkmmte.view.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class DetailScreenActivity extends AppCompatActivity {
    private RecyclerView recCom;
    private List<CommentList> commentLists;
    private CommentAdapter adapter;
    private ScrollView scrollView;
    private TextView TenPhim, TacGia, DaoDien, QuocGia, Nam, NgayRaRap, ThoiLuong,
            DoPhanGiai, NgonNgu, TheLoai, LuotXem, NoiDung, Diem, TrangThai;
    private ImageView Thumbnail;
    private String idMovie;
    private EditText txtComment;
    private Button btnComment, btnTrailer, btnXemNgay;


    private String comment;

    private ArrayList<String> idMoive = new ArrayList<>();
    private ArrayList<String> Username = new ArrayList<>();
    private ArrayList<String> emailss = new ArrayList<>();
    private ArrayList<String> commenttt = new ArrayList<>();

    private ProgressBar progressBar;
    private LinearLayout linearLayout;

    private String urlTrailer, urlMovie;
    private String localtion = "rtsp://192.168.43.33:1935/vod/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_screen);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        linearLayout = (LinearLayout) findViewById(R.id.tongview);

        recCom = (RecyclerView) findViewById(R.id.recComment);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollView.setFocusableInTouchMode(true);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);

        Thumbnail = (ImageView) findViewById(R.id.thumbs);
        TenPhim = (TextView) findViewById(R.id.TenPhim);
        TacGia = (TextView) findViewById(R.id.TacGia);
        DaoDien = (TextView) findViewById(R.id.txtDaoDien);
        QuocGia = (TextView) findViewById(R.id.txtQuocGia);
        Nam = (TextView) findViewById(R.id.txtNam);
        NgayRaRap = (TextView) findViewById(R.id.txtNgayRaRap);
        ThoiLuong = (TextView) findViewById(R.id.txtThoiLuong);
        DoPhanGiai = (TextView) findViewById(R.id.txtDoPhanGiai);
        NgonNgu = (TextView) findViewById(R.id.txtNgonNgu);
        TheLoai = (TextView) findViewById(R.id.txtTheLoai);
        LuotXem = (TextView) findViewById(R.id.txtLuotXem);
        NoiDung = (TextView) findViewById(R.id.txtNoiDung);
        Diem = (TextView) findViewById(R.id.txtDiem);
        TrangThai = (TextView) findViewById(R.id.txtTrangthai);
        txtComment = (EditText) findViewById(R.id.txtComment);
        btnComment = (Button) findViewById(R.id.btnComment);
        btnTrailer = (Button) findViewById(R.id.btnTrailer);
        btnXemNgay = (Button) findViewById(R.id.btnXemNgay);


        commentLists = new ArrayList<>();
        adapter = new CommentAdapter(this, commentLists);

        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        recCom.setLayoutManager(llm);
        recCom.setAdapter(adapter);


        Intent i = getIntent();
        Bundle bundle = i.getBundleExtra("tt");
        idMovie = bundle.getString("idMovie");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("MovieInfor");
        query.whereEqualTo("objectId", idMovie);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> movieList, ParseException e) {
                if (e == null) {

                    for (ParseObject info : movieList) {

                        final ParseFile image;
                        image = info.getParseFile("Thumbnails");
                        image.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] bytes, ParseException e) {

                                if (e == null) {

                                    try {
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(
                                                image.getData(),
                                                0,
                                                image.getData().length
                                        );
                                        Thumbnail.setImageBitmap(bitmap);

                                    } catch (ParseException e1) {
                                        e1.printStackTrace();
                                    }
                                } else {

                                }

                            }
                        });


                        TenPhim.setText(info.getString("TenPhim"));
                        TacGia.setText(info.getString("TacGia"));
                        DaoDien.setText(info.getString("DaoDien"));
                        QuocGia.setText(info.getString("QuocGia"));
                        Nam.setText(info.getString("Nam"));
                        NgayRaRap.setText(info.getString("NgayRaRap"));
                        ThoiLuong.setText(info.getString("ThoiLuong"));
                        DoPhanGiai.setText(info.getString("DoPhanGiai"));
                        NgonNgu.setText(info.getString("NgonNgu"));
                        TheLoai.setText(info.getString("TheLoai"));
                        LuotXem.setText(info.getString("LuotXem"));
                        NoiDung.setText(info.getString("NoiDungPhim"));
                        Diem.setText(info.getString("DiemIMDb"));
                        TrangThai.setText(info.getString("TrangThai"));
                        progressBar.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.VISIBLE);

                    }

                } else {

                    Log.d("----------------->", "day ne");
                }
            }
        });


        ParseQuery<ParseObject> queryy = ParseQuery.getQuery("MovieInfor");
        queryy.whereEqualTo("objectId", idMovie);
        queryy.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> movieList, ParseException e) {
                if (e == null) {
                    for (ParseObject info : movieList) {


                        urlTrailer = localtion + info.getString("LocationTrailer");
                        urlMovie = localtion + info.getString("Location");

                        btnTrailer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(v.getContext(), MovieActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("url", urlTrailer);
                                intent.putExtra("tt", bundle);

                                v.getContext().startActivity(intent);
                            }
                        });


                        btnXemNgay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(v.getContext(), MovieActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("url", urlMovie);
                                intent.putExtra("tt", bundle);

                                v.getContext().startActivity(intent);
                            }
                        });
                    }

                } else {
                    Log.d("----------------->", "Khong Load duoc trailer");
                }
            }
        });



        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                GraphRequest req = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject jsonObject, final GraphResponse response) {
                                comment = txtComment.getText().toString();
                                if (!comment.equals("")) {
                                    txtComment.setText("");
                                    try {


                                        ParseObject gameScore = new ParseObject("Comments");
                                        gameScore.put("Username", response.getJSONObject().getString("name"));
                                        gameScore.put("email", response.getJSONObject().getString("email"));
                                        gameScore.put("idMoive", idMovie);
                                        gameScore.put("Comment", comment);
                                        gameScore.saveInBackground();


                                        LoadComment();


                                    } catch (JSONException e1) {

                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Mời bình luận", Toast.LENGTH_SHORT).show();
                                }

                            }


                        });
                Bundle bundlee = new Bundle();
                bundlee.putString("fields", "id, name,email,gender,birthday,picture.height(300).width(300)");
                req.setParameters(bundlee);
                req.executeAsync();


            }
        });

        LoadComment();
    }


    public void LoadComment() {
        commentLists.clear();
        idMoive.clear();
        Username.clear();
        emailss.clear();
        commenttt.clear();


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Comments");
        query.whereEqualTo("idMoive", idMovie);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> movieList, ParseException e) {
                if (e == null) {

                    for (ParseObject info : movieList) {
                        Username.add(info.getString("Username"));
                        emailss.add(info.getString("email"));
                        commenttt.add(info.getString("Comment"));

                    }

                    for (int i = Username.size() - 1; i >= 0; i--) {
                        CommentList comm = new CommentList();
                        comm.setUsername(Username.get(i));
                        comm.setComment(commenttt.get(i));


                        commentLists.add(comm);
                    }


                    adapter.notifyDataSetChanged();
                } else {
                    Log.d("----------------->", "Khong Load duoc comment");
                }
            }
        });

    }



}
