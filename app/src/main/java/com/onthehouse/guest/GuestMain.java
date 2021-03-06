package com.onthehouse.guest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.onthehouse.fragments.OffersList;
import com.onthehouse.fragments.PastOffersList;
import com.onthehouse.onthehouse.OnTheMain;
import com.onthehouse.onthehouse.R;
// Guest screen operations starts from here
// Everything related to guest is being managed here
public class GuestMain extends AppCompatActivity {
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment fragment = null;

            if (item.getItemId() == R.id.navigation_offers) {
                fragment = new OffersList();
                getSupportActionBar().setSubtitle("Current Offers");
            } else if (item.getItemId() == R.id.navigation_offerings) {
                fragment = new PastOffersList();
                getSupportActionBar().setSubtitle("Past Offers");

            } else {
                fragment = new OffersList();
            }

            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.guest_content, fragment).commit();
            }

            return true;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_main);

        getSupportActionBar().setTitle("On The House - Guest User");
        getSupportActionBar().setSubtitle("Current Offers");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPreferences = getSharedPreferences("GuestMember", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("GuestCheck", true);
        editor.apply();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.guest_content, new OffersList());
        tx.commit();
    }


    @Override
    public void onBackPressed() {
        //Back to login
        Intent intent = new Intent(this, OnTheMain.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                Intent intent = new Intent(this, OnTheMain.class);
                startActivity(intent);
                this.finish();
            } else {
                getSupportFragmentManager().popBackStack();
            }
        }


        return super.onOptionsItemSelected(item);
    }
}
