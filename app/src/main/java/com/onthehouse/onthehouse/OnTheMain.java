package com.onthehouse.onthehouse;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
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

public class OnTheMain extends AppCompatActivity{

    ImageView imageView;
    Boolean anim = false;
    AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout layout = (LinearLayout) findViewById(R.id.onthemain);
        animationDrawable = (AnimationDrawable) layout.getBackground();
        animationDrawable.setEnterFadeDuration(4000);
        animationDrawable.setExitFadeDuration(1000);


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
                mTabLayout.setupWithViewPager(viewPager);
                mTabLayout.setTabTextColors(Color.BLACK,Color.BLACK);
            }
        }, 1000);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (animationDrawable != null && !animationDrawable.isRunning())
            animationDrawable.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (animationDrawable != null && animationDrawable.isRunning())
            animationDrawable.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
