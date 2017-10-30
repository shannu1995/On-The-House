package com.onthehouse.onthehouse;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.onthehouse.Utils.ViewPagerAdapter;
import com.onthehouse.fragments.LoginActivity;
import com.onthehouse.fragments.RegisterActivity;

/**
 * Created by anashanifm on 11/9/17.
 */

public class OnTheMain extends AppCompatActivity{
    ImageView imageView;
    Boolean anim = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setVisibility(View.INVISIBLE);
        imageView = (ImageView)findViewById(R.id.logo);

        Bundle b = getIntent().getExtras();

        if (b != null)
            anim = b.getBoolean("Animation");

        if (anim) {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.home_animation);
            imageView.startAnimation(animation);
            anim = false;
        }
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
                LinearLayout linearLayout = (LinearLayout)mTabLayout.getChildAt(0);
                linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
                GradientDrawable drawable = new GradientDrawable();
                drawable.setColor(Color.BLACK);
                drawable.setSize(6, 6);
                linearLayout.setDividerPadding(0);
                linearLayout.setDividerDrawable(drawable);
                mTabLayout.setupWithViewPager(viewPager);
                mTabLayout.setTabTextColors(Color.BLACK,Color.BLACK);
            }
        }, 1000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
