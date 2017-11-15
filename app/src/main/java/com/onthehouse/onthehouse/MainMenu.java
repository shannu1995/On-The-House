package com.onthehouse.onthehouse;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.onthehouse.Utils.DrawerLocker;
import com.onthehouse.connection.APIConnection;
import com.onthehouse.details.Category;
import com.onthehouse.details.Member;
import com.onthehouse.details.UtilMethods;
import com.onthehouse.details.Zone;
import com.onthehouse.fragments.AboutFragment;
import com.onthehouse.fragments.AccountFragment;
import com.onthehouse.fragments.ChangePasswordFragment;
import com.onthehouse.fragments.EditMemberFragment;
import com.onthehouse.fragments.MembershipFragment;
import com.onthehouse.fragments.MyPastOfferings;
import com.onthehouse.fragments.OffersList;
import com.onthehouse.fragments.PastOffersList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DrawerLocker {

    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    boolean offerListFragment;
    Toolbar toolbar;
    NavigationView navigationView;
    private Menu options_menu;
    private ArrayList<Category> categoriesArrayList;
    private ArrayList<Zone> zoneArrayList;

    private HashMap<Category, Boolean> category_selected;
    private HashMap<Zone, Boolean> state_selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        categoriesArrayList = new ArrayList<Category>();
        zoneArrayList = new ArrayList<Zone>();
        if(category_selected == null){
            category_selected = new HashMap<Category, Boolean>();
            Log.w("Starting over", "Empty array of selected categories");
        }
        else {
            Log.w("Categories Selected:","");
            for (Map.Entry<Category, Boolean> entry : category_selected.entrySet()){
                Log.w("",entry.getKey().getName());
            }
        }
        state_selected = new HashMap<Zone, Boolean>();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("On The House");
        toolbar.setSubtitle("Current Offers");
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        Drawable filter = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_filter_white);
        toolbar.setOverflowIcon(filter);

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

        new getCategoriesAsyncData().execute();
        new getStatesAsyncData().execute();
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
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter, menu);
        options_menu = menu;
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        menu.clear();
        getMenuInflater().inflate(R.menu.filter, menu);

        MenuItem category_item = menu.findItem(R.id.category_item);
        SubMenu category_submenu = category_item.getSubMenu();

        for(int i = 0; i < categoriesArrayList.size(); i ++){
            category_submenu.add(R.id.category_group, UtilMethods.tryParseInt(categoriesArrayList.get(i).getId()), i, categoriesArrayList.get(i).getName());
            final MenuItem tempItem = category_submenu.getItem(i);
            tempItem.setCheckable(true);
            if(!category_selected.isEmpty()){
                for (Map.Entry<Category, Boolean> entry : category_selected.entrySet()){
                    if(tempItem.getItemId() == UtilMethods.tryParseInt(entry.getKey().getId())){
                        tempItem.setChecked(true);
                    }
                }
            }
            tempItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    tempItem.setChecked(!tempItem.isChecked());
                    if(tempItem.isChecked()){
                        int j = tempItem.getItemId();
                        for (int k = 0; k < categoriesArrayList.size(); k++){
                            if(categoriesArrayList.get(k).getId().equals(Integer.toString(j))){
                                category_selected.put(categoriesArrayList.get(k), true);
                            }
                        }
                        for (Map.Entry<Category, Boolean> entry : category_selected.entrySet()){
                            Log.w("Category Selected:",entry.getKey().getName());
                        }
                    }else{
                        int j = tempItem.getItemId();
                        for (int k = 0; k < categoriesArrayList.size(); k++){
                            if(categoriesArrayList.get(k).getId().equals(Integer.toString(j))){
                                category_selected.remove(categoriesArrayList.get(k));
                            }
                        }
                        for (Map.Entry<Category, Boolean> entry : category_selected.entrySet()){
                            Log.w("Category Selected:",entry.getKey().getName());
                        }
                    }
                    Class fragmentClass = OffersList.class;
                    Fragment fragment = null;
                    try {
                        fragment = (Fragment) fragmentClass.newInstance();
                        Bundle category_offers_bundle = new Bundle();
                        category_offers_bundle.putSerializable("categories",category_selected);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (fragment != null) {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
                    }
                    return false;
                }
            });
            category_submenu.setGroupCheckable(R.id.category_group, true, false);
        }

        MenuItem state_item = menu.findItem(R.id.state_item);
        SubMenu state_itemSubMenu = state_item.getSubMenu();
        for(int i = 0; i < zoneArrayList.size(); i ++){
            state_itemSubMenu.add(R.id.state_group, i, i, zoneArrayList.get(i).getName());
            final MenuItem tempItem = state_itemSubMenu.getItem(i);
            tempItem.setCheckable(true);
            tempItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    tempItem.setChecked(!tempItem.isChecked());
                    return false;
                }
            });
            state_itemSubMenu.setGroupCheckable(R.id.state_group, true, false);
        }
        options_menu = menu;
        return super.onPrepareOptionsMenu(menu);
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

    private class getCategoriesAsyncData extends AsyncTask<Void, Void, Integer> {

        @Override
        protected void onPreExecute() {
            //Lock Drawer While Loading
        }
        @Override
        protected Integer doInBackground(Void... params) {
            int status = 0;
            APIConnection connection = new APIConnection();
            try {
                String output = connection.sendGet("/api/v1/categories");
                if(output.length() > 0){
                    JSONObject obj = new JSONObject(output);
                    String result = obj.getString("status");
                    if (result.equals("success")) {
                        JSONArray category_array = new JSONArray();
                        category_array = obj.getJSONArray("categories");
                        for(int i = 0; i < category_array.length(); i++){
                            JSONObject ind_category_json = new JSONObject();
                            ind_category_json = category_array.getJSONObject(i);
                            Category ind_category = new Category();
                            ind_category.setId(ind_category_json.getString("id"));
                            ind_category.setParent_id(ind_category_json.getString("parent_id"));
                            ind_category.setName(ind_category_json.getString("name"));
                            ind_category.setDescription(ind_category_json.getString("description"));
                            ind_category.setImage(ind_category_json.getString("image"));
                            ind_category.setType(ind_category_json.getString("type"));
                            categoriesArrayList.add(ind_category);
                            invalidateOptionsMenu();
                        }
                        status = 1;
                    }else{
                        status = 2;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                status = 3;
            }
            return status;
        }
        @Override
        protected void onPostExecute(Integer integer) {

        }
    }

    private class getStatesAsyncData extends AsyncTask<Void, Void, Integer>{
        @Override
        protected void onPreExecute() {
            //Lock Drawer While Loading
        }
        @Override
        protected Integer doInBackground(Void... params) {
            int status = 0;
            APIConnection connection = new APIConnection();
            try {
                String output = connection.sendGet("/api/v1/zones/"+Integer.toString(Member.getInstance().getCountry_id()));
                if (output.length() > 0) {
                    JSONObject obj = new JSONObject(output);
                    String result = obj.getString("status");
                    if (result.equals("success")) {
                        try{
                            JSONArray zones = obj.getJSONArray("zones");
                            for (int i = 0; i <zones.length(); i++)
                            {
                                Zone zone = new Zone();
                                JSONObject jObj = zones.getJSONObject(i);
                                zone.setId(UtilMethods.tryParseInt(jObj.getString("id")));
                                zone.setCountry_id(UtilMethods.tryParseInt(jObj.getString("country_id")));
                                zone.setCode(jObj.getString("code"));
                                zone.setName(jObj.getString("name"));
                                System.out.println(zone);
                                zoneArrayList.add(zone);
                                invalidateOptionsMenu();
                            }
                            status = 1;
                        }catch (Exception e){
                            status = 3;
                        }
                    }else{
                        status = 2;
                    }
                }
            }catch (Exception e){
                status = 3;
            }
            return status;
        }
        @Override
        protected void onPostExecute(Integer integer) {
        }
    }
}
