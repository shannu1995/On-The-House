package com.onthehouse.onthehouse;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.onthehouse.Utils.ViewPagerAdapter;
import com.onthehouse.fragments.LoginActivity;
import com.onthehouse.fragments.RegisterActivity;

/**
 * Created by anashanifm on 11/9/17.
 */

public class OnTheMain extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Locate the viewpager in activity_main.xml
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        // Set the ViewPagerAdapter into ViewPager
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new RegisterActivity(), "Sign Up");
        adapter.addFrag(new LoginActivity(), "Sign In");

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(2);


        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(viewPager);
        mTabLayout.setTabTextColors(Color.WHITE,Color.WHITE);
    }
}
