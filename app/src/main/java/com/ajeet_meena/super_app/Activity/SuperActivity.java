package com.ajeet_meena.super_app.Activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ajeet_meena.super_app.Adapter.ViewPagerAdapter;
import com.ajeet_meena.super_app.R;
import com.ajeet_meena.super_app.UiHelpers.CircularImageView;
import com.ajeet_meena.super_app.UiHelpers.ViewPageTransformer;

public class SuperActivity extends AppCompatActivity {

    CircularImageView circularImageView;
    TextView userName;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    String[] fragmentTitles = {"Explore", "Spotlights", "Picks"};
    int noOfFragment = fragmentTitles.length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super);
        initViews();
        initToolbar();
        initViewPagerAndFragments();
        initNavigationView();
        initDrawerLayout();
        initProfile();
    }

    private void initProfile() {
    }

    private void initDrawerLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.openDrawer(Gravity.LEFT);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {

                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {

                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }



    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.app_name));
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        if( getSupportActionBar() != null )
            getSupportActionBar().show();
    }

    private void initNavigationView() {
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                drawerLayout.closeDrawers();
                switch ( menuItem.getItemId() ) {
                    case R.id.explore:
                        return true;
                    case R.id.spotlights:
                        return true;
                    case R.id.picks:
                        return true;
                    case R.id.logout:
                        return true;
                    default:
                        Toast.makeText(SuperActivity.this, "An error has occurred", Toast.LENGTH_SHORT).show();
                        return true;
                }
            }
        });
    }

    private void initViewPagerAndFragments() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),fragmentTitles,noOfFragment);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setPageTransformer(true, new ViewPageTransformer(ViewPageTransformer.TransformType.DEPTH));
    }

    private void initViews() {
        circularImageView = (CircularImageView) findViewById(R.id.profile_pic);
        userName = (TextView) findViewById(R.id.user_name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
