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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.onthehouse.Utils.DrawerLocker;
import com.onthehouse.details.Member;
import com.onthehouse.fragments.AboutFragment;
import com.onthehouse.fragments.AccountFragment;
import com.onthehouse.fragments.ChangePasswordFragment;
import com.onthehouse.fragments.EditMemberFragment;
import com.onthehouse.fragments.MembershipFragment;
import com.onthehouse.fragments.MyPastOfferings;
import com.onthehouse.fragments.OffersList;
import com.onthehouse.fragments.PastOffersList;

public class MainMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DrawerLocker {

    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    boolean offerListFragment;
    Toolbar toolbar;
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("On The House");
        toolbar.setSubtitle("Current Offers");
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.offers);

        View header = navigationView.getHeaderView(0);
        TextView navName = header.findViewById(R.id.navName);
        TextView navEmail = header.findViewById(R.id.navEmail);

        navName.setText(Member.getInstance().getFirst_name() + " " + Member.getInstance().getLast_name());
        navEmail.setText(Member.getInstance().getEmail());

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.frame_container, new OffersList());
        tx.commit();
        offerListFragment = true;

    }

    @Override
    public void setDrawerEnabled(boolean enabled) {

        int lockMode = enabled ? DrawerLayout.LOCK_MODE_UNLOCKED :
                DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
        drawer.setDrawerLockMode(lockMode);
        toggle.setDrawerIndicatorEnabled(enabled);
    }

    public void setChecked(int id, boolean offerList) {
        navigationView.setCheckedItem(id);
        offerListFragment = offerList;
    }

    @Override
    public void onBackPressed() {
        if (offerListFragment) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        } else {
            navigationView.setCheckedItem(R.id.offers);
            toolbar.setSubtitle("Current Offers");
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.frame_container, new OffersList());
            tx.commit();
            offerListFragment = true;
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
        Class fragmentClass = null;

        switch (item.getItemId()) {
            case R.id.offers:
                fragmentClass = OffersList.class;
                offerListFragment = true;
                break;
            case R.id.nav_past_offers:
                fragmentClass = PastOffersList.class;
                offerListFragment = false;
                break;
            case R.id.edit_details:
                fragmentClass = EditMemberFragment.class;
                offerListFragment = false;
                break;
            case R.id.past_offerings:
                fragmentClass = MyPastOfferings.class;
                offerListFragment = false;
                break;
            case R.id.change_password:
                fragmentClass = ChangePasswordFragment.class;
                offerListFragment = false;
                break;
            case R.id.membership:
                fragmentClass = MembershipFragment.class;
                offerListFragment = false;
                break;
            case R.id.account:
                fragmentClass = AccountFragment.class;
                offerListFragment = false;
                break;
            case R.id.nav_about:
                fragmentClass = AboutFragment.class;
                offerListFragment = false;
                break;
            case R.id.nav_logout:
                SharedPreferences sharedPreferences = getSharedPreferences("memberInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("memberEmail", null);
                editor.putString("memberPass", null);
                editor.apply();

                Toast.makeText(getApplicationContext(), "Logged Out Successfully", Toast.LENGTH_LONG).show();
                Intent loginIntent = new Intent(MainMenu.this, OnTheMain.class);
                startActivity(loginIntent);
                finish();
                break;


            default:
                fragmentClass = OffersList.class;
                offerListFragment = true;
                break;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Insert the fragment by replacing any existing fragment
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
        }
        toolbar.setSubtitle(item.getTitle());
        //Close nav drawer
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
