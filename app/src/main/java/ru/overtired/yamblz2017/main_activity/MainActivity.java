package ru.overtired.yamblz2017.main_activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import ru.overtired.yamblz2017.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String SAVED_NAV_ACTION = "ru.overtired.yamblz2017.action";

    private static final int WEATHER_FRAGMENT = 0;
    private static final int PREFERENCE_FRAGMENT = 1;

    private static final String WEATHER_TAG = "content_fragment";
    private static final String PREFERENCE_TAG = "preference_fragment";
    private static final String ABOUT_TAG = "about_fragment";

    private int currentFragment; //0-Content, 1-Preferences, 2-About.

    private PreferenceFragment preferenceFragment;
    private WeatherFragment weatherFragment;
    private AboutFragment aboutFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_activity_toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_activity_drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.toggle_open_drawer, R.string.toggle_close_drawer);

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.main_activity_navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        weatherFragment = (WeatherFragment) getSupportFragmentManager().findFragmentByTag(WEATHER_TAG);
        if(weatherFragment == null){
            weatherFragment = WeatherFragment.newInstance();
        }

        preferenceFragment = (PreferenceFragment) getSupportFragmentManager().findFragmentByTag(PREFERENCE_TAG);
        if(preferenceFragment == null){
            preferenceFragment = new PreferenceFragment();
        }

        aboutFragment = (AboutFragment) getSupportFragmentManager().findFragmentByTag(ABOUT_TAG);
        if(aboutFragment ==null){
            aboutFragment = AboutFragment.newInstance();
        }

        if(savedInstanceState!=null){
            currentFragment = savedInstanceState.getInt(SAVED_NAV_ACTION, WEATHER_FRAGMENT);

        }else {
            currentFragment = WEATHER_FRAGMENT;

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_activity_fragment_container, weatherFragment, WEATHER_TAG)
                    .commit();

            navigationView.getMenu().getItem(currentFragment).setChecked(true);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_activity_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.main_activity_nav_content:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_activity_fragment_container, weatherFragment, WEATHER_TAG)
                        .commit();
                //item.setChecked(true);
                currentFragment = WEATHER_FRAGMENT;
                break;

            case R.id.main_activity_nav_settings:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_activity_fragment_container, preferenceFragment,PREFERENCE_TAG)
                        .commit();
                //item.setChecked(true);
                currentFragment = PREFERENCE_FRAGMENT;
                break;
        }

        if(id==R.id.main_activity_nav_about){
            aboutFragment.show(getSupportFragmentManager(),ABOUT_TAG);
        }

        ((DrawerLayout) findViewById(R.id.main_activity_drawer_layout))
                .closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(SAVED_NAV_ACTION,currentFragment);
    }
}
