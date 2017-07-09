package ru.overtired.yamblz2017;

import android.content.DialogInterface;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String SAVED_NAV_ACTION = "ru.overtired.yamblz2017.action";
    private static final String SAVED_NAV_ABOUT = "ru.overtired.yamblz2017.about";

    private static final int CONTENT_FRAGMENT = 0;
    private static final int SETTINGS_FRAGMENT = 1;

    private boolean isAboutShown;
    private int currentFragment; //0-Content, 1-Settings. Интересно, как это нормальные люди делают

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

        if(savedInstanceState!=null){
            isAboutShown = savedInstanceState.getBoolean(SAVED_NAV_ABOUT,false);
            currentFragment = savedInstanceState.getInt(SAVED_NAV_ACTION,CONTENT_FRAGMENT);
        }else {
            isAboutShown = false;
            currentFragment = CONTENT_FRAGMENT;
        }

        if(isAboutShown){
            showAbout();
        }

        switch (currentFragment){
            case CONTENT_FRAGMENT:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_activity_fragment_container, ContentFragment.newInstance())
                        .commit();
                break;

            case SETTINGS_FRAGMENT:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_activity_fragment_container, new PreferenceFragment())
                        .commit();
                break;
        }
        navigationView.getMenu().getItem(currentFragment).setChecked(true);
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
                        .replace(R.id.main_activity_fragment_container, ContentFragment.newInstance())
                        .commit();
                item.setChecked(true);
                currentFragment = CONTENT_FRAGMENT;
                break;

            case R.id.main_activity_nav_settings:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_activity_fragment_container, new PreferenceFragment())
                        .commit();
                item.setChecked(true);
                currentFragment = SETTINGS_FRAGMENT;
                break;
        }

        if(id==R.id.main_activity_nav_about){
            showAbout();
        }

        ((DrawerLayout) findViewById(R.id.main_activity_drawer_layout))
                .closeDrawer(GravityCompat.START);

        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(SAVED_NAV_ABOUT,isAboutShown);
        outState.putInt(SAVED_NAV_ACTION,currentFragment);
    }

    private void showAbout() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(R.string.app_about)
                .setTitle(R.string.app_about_title)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        isAboutShown = false;
                    }
                })
                .create();
        dialog.show();

        isAboutShown = true;
    }
}
