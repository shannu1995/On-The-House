package com.onthehouse.onthehouse;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.onthehouse.fragments.OffersList;
import com.onthehouse.fragments.PastOffersList;

public class MainMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.frame_container, new OffersList());
        tx.commit();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        int id = item.getItemId();

        if (id == R.id.offers) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
                fragmentManager.popBackStack();
            }
            fragment = new OffersList();

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_past_offers) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragment = new PastOffersList();

        } else if (id == R.id.nav_logout) {
            SharedPreferences sharedPreferences = getSharedPreferences("memberInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("memberEmail", null);
            editor.putString("memberPass", null);
            editor.apply();

            Toast.makeText(getApplicationContext(), "Logged Out Successfully"
                    , Toast.LENGTH_LONG).show();
            Intent loginIntent = new Intent(MainMenu.this, OnTheMain.class);
            MainMenu.this.startActivity(loginIntent);
            finish();
        } else {
            fragment = new OffersList();
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).addToBackStack("On the House").commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
