package com.onthehouse.onthehouse;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.onthehouse.Utils.ViewPagerAdapter;
import com.onthehouse.fragments.LoginActivity;
import com.onthehouse.fragments.RegisterActivity;

/**
 * Created by anashanifm on 11/9/17.
 */

public class OnTheMain extends AppCompatActivity{
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setVisibility(View.INVISIBLE);
        imageView = (ImageView)findViewById(R.id.logo);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.home_animation);
        imageView.startAnimation(animation);
        // Locate the viewpager in activity_main.xml


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
                viewPager.setVisibility(View.VISIBLE);
                // Set the ViewPagerAdapter into ViewPager
                ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
                adapter.addFrag(new RegisterActivity(), "Sign Up");
                adapter.addFrag(new LoginActivity(), "Sign In");

                viewPager.setAdapter(adapter);
                viewPager.setCurrentItem(2);


                TabLayout mTabLayout = (TabLayout) findViewById(R.id.tabs);
                mTabLayout.setupWithViewPager(viewPager);
                mTabLayout.setTabTextColors(Color.BLACK,Color.BLACK);
            }
        }, 1000);

    }
}
