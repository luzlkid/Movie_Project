package com.example.luxkiz1.movie_projet.NavigationDrawer;


import android.app.Fragment;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.luxkiz1.movie_projet.LoginScreenActivity;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.example.luxkiz1.movie_projet.MainScreenActivity;
import com.example.luxkiz1.movie_projet.R;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;


import java.util.ArrayList;
import java.util.List;


public class NavigationDrawerFragment extends Fragment implements RecycleNaviAdapter.ClickListener {


    private RecycleNaviAdapter adapter;
    private RecyclerView recyclerView;


    public static final String PREF_FILE_NAME = "testpref";
    public static final String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    private View containerView;
    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;

    private TextView name;
    private TextView email;


    public NavigationDrawerFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = Boolean.valueOf(readFromPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, "false"));
        if (savedInstanceState != null) {
            mFromSavedInstanceState = true;

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
        name = (TextView) layout.findViewById(R.id.name);
        email = (TextView) layout.findViewById(R.id.email);


        adapter = new RecycleNaviAdapter(getActivity(), getDate());
        adapter.setClickListener(this);


        // recyclerView.addItemDecoration(new SimpleDividerItemDecoration(layout.getContext()));  Dung de tao divider cho recyclerview
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        return layout;
    }

    public static List<Information> getDate() {
        List<Information> data = new ArrayList<>();
        int[] icon = {R.drawable.iconphim, R.drawable.icontvs, R.drawable.icon_muc,
                R.drawable.icon_muc, R.drawable.icon_muc, R.drawable.icon_muc, R.drawable.icon_muc
                , R.drawable.iconloggout};
        String[] title = {"Movie", "TV Show", "Phim kinh dị", "Phim hoạt hình", "Tình cảm lãng mạn"
                , "Khoa học viễn tưởng", "Hành động phiêu lưu", "Đăng xuất"};
        for (int i = 0; i < title.length && i < icon.length; i++) {
            Information current = new Information();
            current.iconId = icon[i];
            current.title = title[i];
            data.add(current);
        }

        return data;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    saveToPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, mUserLearnedDrawer + "");
                }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                getActivity().invalidateOptionsMenu();
            }
        };

        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(containerView);
        }

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public static String readFromPreferences(Context context, String preferenceName, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, defaultValue);
    }

    @Override
    public void itemClicked(View view, int position) {
        //int pos = recyclerView.getChildPosition(view);
        switch (position) {
            // Movie
            case 0:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                ((MainScreenActivity) getActivity()).PhanLoaiPhim(position);
                break;
            // TV SHOW
            case 1:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                ((MainScreenActivity) getActivity()).PhanLoaiPhim(position);
                break;
            //Phim kinh di
            case 2:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                ((MainScreenActivity) getActivity()).PhanLoaiPhim(position);
                break;
            //Phim hoat hinh
            case 3:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                ((MainScreenActivity) getActivity()).PhanLoaiPhim(position);
                break;
            //Phim lang mang
            case 4:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                ((MainScreenActivity) getActivity()).PhanLoaiPhim(position);
                break;
            //Phim vien tuong
            case 5:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                ((MainScreenActivity) getActivity()).PhanLoaiPhim(position);
                break;
            //Phim hanh dong
            case 6:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                ((MainScreenActivity) getActivity()).PhanLoaiPhim(position);
                break;

            case 7:
                //ParseFacebookUtils.getSession().closeAndClearTokenInformation();

                ParseUser.logOut();


                Intent i = new Intent(view.getContext(), LoginScreenActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                break;
        }

    }


}
