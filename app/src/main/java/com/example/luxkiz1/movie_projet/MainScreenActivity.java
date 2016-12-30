package com.example.luxkiz1.movie_projet;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.luxkiz1.movie_projet.MainAlbum.Album;
import com.example.luxkiz1.movie_projet.MainAlbum.AlbumsAdapter;
import com.example.luxkiz1.movie_projet.NavigationDrawer.NavigationDrawerFragment;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.pkmmte.view.CircularImageView;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainScreenActivity extends ActionBarActivity {

    //private ContextMenuDialogFragment mMenuDialogFragment;
    //private FragmentManager fragmentManager;
    private Toolbar toolbar;

    private TextView name;
    private TextView email;
    private CircularImageView fbimg;
    private String url;
    private Bitmap bmp;


    private SearchView searchView;

    private RecyclerView recyclerView;
    private AlbumsAdapter adapter;
    private List<Album> albumList = new ArrayList<>();

    private ProgressBar pro;
    private SwipeRefreshLayout swiper;

    ArrayList<String> TenPhim = new ArrayList<String>();
    ArrayList<String> TacGia = new ArrayList<String>();
    ArrayList<ParseFile> Thumbnails = new ArrayList<ParseFile>();
    ArrayList<String> idObj = new ArrayList<String>();

    private String idMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initCollapsingToolbar();

        //fragmentManager = getSupportFragmentManager();
        //initMenuFragment();
        //mMenuDialogFragment.setItemClickListener(this);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swiper = (SwipeRefreshLayout) findViewById(R.id.swiper);
        pro = (ProgressBar) findViewById(R.id.progress_bar);

        adapter = new AlbumsAdapter(this, albumList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(0), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        LoadDataFromMovieInfor();

        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        try {
            Glide.with(this).load(R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }


        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);


        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        fbimg = (CircularImageView) findViewById(R.id.fbimg);

        // Lay du lieu cua nguoi dung ve
        GraphRequest req = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject jsonObject, final GraphResponse response) {

                        try {

                            name.setText(response.getJSONObject().getString("name"));
                            email.setText(response.getJSONObject().getString("email"));

                            JSONObject obURL = jsonObject.getJSONObject("picture");
                            JSONObject ob = obURL.getJSONObject("data");

                            url = ob.getString("url");


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        new LoadImageFacebook(url, response.getJSONObject().getString("email")).execute();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });


                        } catch (JSONException e1) {
                            Log.d("----------------->", "2");
                        }

                    }


                });

        Bundle bundle = new Bundle();
        bundle.putString("fields", "id, name,email,gender,birthday,picture.height(300).width(300)");
        req.setParameters(bundle);
        req.executeAsync();


    }


    private void refresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                LoadDataFromMovieInfor();
                swiper.setRefreshing(false);
            }
        }, 2000);
    }

    public void LoadDataFromMovieInfor() {
        Log.d("----------------->", "TAI DU LIEU");
        albumList.clear();
        TenPhim.clear();
        TacGia.clear();
        Thumbnails.clear();
        idObj.clear();
        recyclerView.setVisibility(View.GONE);
        pro.setVisibility(View.VISIBLE);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("MovieInfor");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> movieList, ParseException e) {
                if (e == null) {

                    for (ParseObject info : movieList) {
                        TenPhim.add(info.getString("TenPhim"));
                        Thumbnails.add(info.getParseFile("Thumbnails"));
                        idObj.add(info.getObjectId());
                    }


                    for (int i = TenPhim.size() - 1; i >= 0; i--) {
                        Album current = new Album();

                        current.setName(TenPhim.get(i));
                        current.setThumbnail(Thumbnails.get(i));
                        current.setId(idObj.get(i));


                        albumList.add(current);
                    }

                    adapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.VISIBLE);
                    pro.setVisibility(View.GONE);

                } else {

                    Log.d("----------------->", "day ne");
                }
            }
        });
    }


    public void PhanLoaiPhim(int position) {
        switch (position) {
            // Movie
            case 0:
                LoadDataFromMovieInfor();
                break;
            // TV SHOW
            case 1:
                LoaiPhim(1);
                break;
            //Phim kinh di
            case 2:
                LoaiPhim(2);
                break;
            //Phim hoat hinh
            case 3:
                LoaiPhim(3);
                break;
            //Phim lang mang
            case 4:
                LoaiPhim(4);
                break;
            //Phim vien tuong
            case 5:
                LoaiPhim(5);
                break;
            //Phim hanh dong
            case 6:
                LoaiPhim(6);
                break;
        }

    }

    public void LoaiPhim(int loai) {

        albumList.clear();
        TenPhim.clear();
        TacGia.clear();
        Thumbnails.clear();
        idObj.clear();
        recyclerView.setVisibility(View.GONE);
        pro.setVisibility(View.VISIBLE);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("MovieInfor");
        query.whereEqualTo("LoaiPhim", loai);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> movieList, ParseException e) {
                if (e == null) {
                    for (ParseObject info : movieList) {

                        TenPhim.add(info.getString("TenPhim"));
                        Thumbnails.add(info.getParseFile("Thumbnails"));
                        idObj.add(info.getObjectId());
                    }
                    for (int i = TenPhim.size() - 1; i >= 0; i--) {
                        Album current = new Album();

                        current.setName(TenPhim.get(i));
                        current.setThumbnail(Thumbnails.get(i));
                        current.setId(idObj.get(i));


                        albumList.add(current);
                    }

                    adapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.VISIBLE);
                    pro.setVisibility(View.GONE);

                } else {
                    Log.d("----------------->", "Khong thuc hien");
                }
            }
        });

    }


    public void ChuyenDenDetailActivity(String s) {
        idMovie = s;

        Intent intent = new Intent(this, DetailScreenActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("idMovie", idMovie);
        intent.putExtra("tt", bundle);

        this.startActivity(intent);

    }


    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }


    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


   /* private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(true);
        // set other settings to meet your needs
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
    }*/

   /* private List<MenuObject> getMenuObjects() {
        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject close = new MenuObject();
        close.setResource(R.drawable.exit);
        close.setBgColor(R.color.colorPrimaryDark);
        close.setDividerColor(R.color.colorPrimaryDark);

        MenuObject send = new MenuObject("Mới nhất");
        send.setResource(R.drawable.neww);
        send.setBgColor(R.color.colorPrimaryDark);
        send.setDividerColor(R.color.colorPrimaryDark);

        MenuObject block = new MenuObject("Xem nhiều nhất");
        block.setResource(R.drawable.icn_3);
        block.setBgColor(R.color.colorPrimaryDark);
        block.setDividerColor(R.color.colorPrimaryDark);

        MenuObject addFav = new MenuObject("Rating cao nhất");
        addFav.setResource(R.drawable.rating);
        addFav.setBgColor(R.color.colorPrimaryDark);
        addFav.setDividerColor(R.color.colorPrimaryDark);

        MenuObject like = new MenuObject("Tải nhiều nhất");
        like.setResource(R.drawable.icn_5);
        like.setBgColor(R.color.colorPrimaryDark);
        like.setDividerColor(R.color.colorPrimaryDark);

        MenuObject addFr = new MenuObject("Danh sách yêu thích");
        addFr.setResource(R.drawable.favo);
        addFr.setBgColor(R.color.colorPrimaryDark);
        addFr.setDividerColor(R.color.colorPrimaryDark);

        menuObjects.add(close);
        menuObjects.add(send);
        menuObjects.add(block);
        menuObjects.add(addFav);
        menuObjects.add(like);
        menuObjects.add(addFr);


        return menuObjects;
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String queryy) {

                albumList.clear();
                TenPhim.clear();
                TacGia.clear();
                Thumbnails.clear();
                idObj.clear();
                recyclerView.setVisibility(View.GONE);
                pro.setVisibility(View.VISIBLE);
                ParseQuery<ParseObject> query = ParseQuery.getQuery("MovieInfor");
                query.whereEqualTo("TenPhim", queryy);
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> movieList, ParseException e) {
                        if (e == null) {
                            for (ParseObject info : movieList) {

                                TenPhim.add(info.getString("TenPhim"));
                                Thumbnails.add(info.getParseFile("Thumbnails"));
                                idObj.add(info.getObjectId());
                            }
                            for (int i = TenPhim.size() - 1; i >= 0; i--) {
                                Album current = new Album();

                                current.setName(TenPhim.get(i));
                                current.setThumbnail(Thumbnails.get(i));
                                current.setId(idObj.get(i));


                                albumList.add(current);
                            }

                            if (albumList.isEmpty()) {
                                Toast.makeText(getApplicationContext(), "Không tìm thấy kết quả", Toast.LENGTH_SHORT).show();
                                LoadDataFromMovieInfor();
                            } else {
                                adapter.notifyDataSetChanged();
                                recyclerView.setVisibility(View.VISIBLE);
                                pro.setVisibility(View.GONE);
                            }

                        } else {
                            Log.d("----------------->", "Khong thuc hien");
                        }
                    }
                });

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.context_menu:
              /*  if (fragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
                    mMenuDialogFragment.show(fragmentManager, "ContextMenuDialogFragment");
                }
                break;*/
                Intent i = new Intent(this, FavoriteScreenActivity.class);
                this.startActivity(i);
                break;
            default: break;

        }
        return super.onOptionsItemSelected(item);
    }

    /*@Override
    public void onMenuItemClick(View clickedView, int position) {
      *//*  switch (position) {

            case 0:
                break;
            //Moi nhat
            case 1:
                LoadDataFromMovieInfor();
                break;
            //Xem nhieu nhat
            case 2:
                break;
            //Ratingcao nhat
            case 3:
                break;
            //Tai nhieu nhat
            case 4:
                break;
            case 5:
                Intent i = new Intent(this, FavoriteScreenActivity.class);
                this.startActivity(i);
                break;
            default:
                break;
        }*//*
    }*/


    //Load anh ?

    class LoadImageFacebook extends AsyncTask<String, Integer, String> {

        public String url;
        public String emailll;

        public LoadImageFacebook(String url, String emailll) {
            this.url = url;
            this.emailll = emailll;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL u = new URL(url);
                bmp = BitmapFactory.decodeStream(u.openConnection().getInputStream());

               /* int size = bmp.getRowBytes() * bmp.getHeight();
                ByteBuffer byteBuffer = ByteBuffer.allocate(size);
                bmp.copyPixelsToBuffer(byteBuffer);
                byte[] data = byteBuffer.array();

                final ParseFile file = new ParseFile("resume.png", data);

                ParseQuery<ParseObject> query = ParseQuery.getQuery("AccountInformation");
                query.whereEqualTo("email", emailll);
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> movieList, ParseException e) {
                        if (e == null) {
                            for (ParseObject info : movieList) {
                                info.put("photo", file);
                                info.saveInBackground();
                            }

                        } else {
                            Log.d("----------------->", "Khong thuc hien");
                        }
                    }
                });
*/

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            fbimg.setImageBitmap(bmp);
        }
    }

}
