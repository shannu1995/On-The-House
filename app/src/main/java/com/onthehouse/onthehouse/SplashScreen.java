package com.onthehouse.onthehouse;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.onthehouse.connection.APIConnection;
import com.onthehouse.details.Member;
import com.onthehouse.details.UtilMethods;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

import org.json.JSONObject;

import java.util.ArrayList;

public class SplashScreen extends AwesomeSplash {
    @Override
    public void initSplash(ConfigSplash configSplash) {
        configSplash.setBackgroundColor(R.color.backgroundColorFirstHalf); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(2000); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP

        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

        //Customize Logo
        configSplash.setLogoSplash(R.drawable.oth_logo_lightbkgd_lowres); //or any other drawable
        configSplash.setAnimLogoSplashDuration(2000); //int ms

        configSplash.setAnimLogoSplashTechnique(Techniques.Bounce); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)


        //Customize Path
        //configSplash.setPathSplash(Constants.DROID_LOGO); //set path String
        configSplash.setOriginalHeight(400); //in relation to your svg (path) resource
        configSplash.setOriginalWidth(400); //in relation to your svg (path) resource
        configSplash.setAnimPathStrokeDrawingDuration(3000);
        configSplash.setPathSplashStrokeSize(3); //I advise value be <5
        configSplash.setPathSplashStrokeColor(R.color.colorPrimary); //any color you want form colors.xml
        configSplash.setAnimPathFillingDuration(3000);
        configSplash.setPathSplashFillColor(R.color.backgroundColorFirstHalf); //path object filling color

        configSplash.setTitleSplash(null);


        SharedPreferences sharedPreferences = getSharedPreferences("memberInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        ArrayList<String> inputList = new ArrayList<>();
        String savedEmailStr = sharedPreferences.getString("memberEmail",null);
        String savedPassStr = sharedPreferences.getString("memberPass", null);
        boolean savedLogin = sharedPreferences.getBoolean("RememberMe", false);

        if (savedEmailStr == null || savedPassStr == null || !savedLogin){

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent offerIntent = new Intent(SplashScreen.this, OnTheMain.class);
                    SplashScreen.this.startActivity(offerIntent);
                    finish();
                }
            }, 15000);

        }else {
            inputList.add("&email="+savedEmailStr);
            inputList.add("&password="+savedPassStr);

            new splashScreenAsyncTask(getApplicationContext()).execute(inputList);
        }
    }

    @Override
    public void animationsFinished() {

    }

    public void setData(Member member, JSONObject jsonArray) {
        try
        {

            member.setId(UtilMethods.tryParseInt(jsonArray.getString("id")));
            member.setTitle(jsonArray.getString("title"));
            member.setFirst_name(jsonArray.getString("first_name"));
            member.setLast_name(jsonArray.getString("last_name"));
            member.setEmail(jsonArray.getString("email"));
            member.setPassword(jsonArray.getString("password"));
            member.setDate_logged_in(jsonArray.getInt("date_logged_in"));
            member.setStatus(UtilMethods.tryParseInt(jsonArray.getString("status")));
            member.setPhone_number(jsonArray.getString("phone"));
            member.setAddress1(jsonArray.getString("address1"));
            member.setAddress2(jsonArray.getString("address2"));
            member.setCity(jsonArray.getString("city"));
            member.setZone_id(UtilMethods.tryParseInt(jsonArray.getString("zone_id")));
            member.setZip_code(UtilMethods.tryParseInt(jsonArray.getString("zip")));
            member.setCountry_id(UtilMethods.tryParseInt(jsonArray.getString("country_id")));
            member.setAge(jsonArray.getString("age"));
            // member.setLanguage_id(UtilMethods.tryParseInt(jsonArray.getString("language_id")));
            member.setTimezone_id(UtilMethods.tryParseInt(jsonArray.getString("timezone_id")));
            member.setMembership_level_id(UtilMethods.tryParseInt(jsonArray.getString("membership_level_id")));
            member.setMembership_expiry(UtilMethods.tryParseInt(jsonArray.getString("membership_expiry")));
            member.setMembership_id(UtilMethods.tryParseInt(jsonArray.getString("membership_id")));
            member.setMembership_expiry_email(UtilMethods.tryParseInt(jsonArray.getString("membership_expiry_email")));
            member.setNewsletters(UtilMethods.tryParseInt(jsonArray.getString("newsletters")));
            member.setFocus_groups(UtilMethods.tryParseInt(jsonArray.getString("focus_groups")));
            member.setPaid_marketing(UtilMethods.tryParseInt(jsonArray.getString("paid_marketing")));
            member.setGoogle_calender(UtilMethods.tryParseInt(jsonArray.getString("google_calendar")));
            member.setCategories(jsonArray.getString("categories"));
            member.setNickname(jsonArray.getString("nickname"));
            member.setImage(jsonArray.getString("image"));
            member.setGc_access_token(jsonArray.getString("gc_access_token"));
            member.setFoundation_member(UtilMethods.tryParseInt(jsonArray.getString("foundation_member")));
            member.setOauth_uid(UtilMethods.tryParseInt(jsonArray.getString("oauth_uid")));
            member.setOauth_provider(jsonArray.getString("oauth_provider"));
            member.setFoundation_member(UtilMethods.tryParseInt(jsonArray.getString("limit_num_reservations")));
        }
        catch(Exception e) {

        }
    }

    public class splashScreenAsyncTask extends AsyncTask<ArrayList<String>, Void, Integer> {

        Context context;

        public splashScreenAsyncTask(Context applicationContext) {
            this.context = applicationContext;
        }

        @Override
        protected Integer doInBackground(ArrayList<String>... params) {
            APIConnection connection = new APIConnection();
            Log.w("EXIST", "INSIDE THREAD ");
            Member member = Member.getInstance();
            int status = 0;
            try
            {
                System.out.println("/api/v1/member/login" + params[0]);
                String output = connection.sendPost("/api/v1/member/login", params[0]);
                if (output.length() > 0)
                {
                    JSONObject obj = new JSONObject(output);
                    String result = obj.getString("status");
                    // JSONArray arr = obj.getJSONArray("member");
                    Log.w("LOGIN RESULT", result);
                    if (result.equals("success"))
                    {
                        JSONObject jsonArray = obj.getJSONObject("member");
                        setData(member, jsonArray);
                        //1 = success;
                        status = 1;
                        Log.w("status", String.valueOf(status));
                        Log.w("OBJECT TEST", member.getFirst_name());
                    }
                    else
                    {
                        //2 = wrong details;
                        status = 2;
                    }
                }
                else
                {
                    // 3 = json parse error
                    status = 3;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            System.out.println("******************" + status);
            return status;
        }

        protected void onPostExecute(Integer result) {
            String toastMessage;
            switch (result){
                case 1:
                    toastMessage = "Logged in as " + Member.getInstance().getFirst_name() + " " +
                            Member.getInstance().getLast_name();
                    Toast.makeText(getApplicationContext(),toastMessage,Toast.LENGTH_LONG).show();
                    Intent mainMenuIntent = new Intent(SplashScreen.this, MainMenu.class);
                    SplashScreen.this.startActivity(mainMenuIntent);
                    finish();

                    break;
                case 2:
                    toastMessage = "Auto login failed, please check your details and sign in again";
                    Toast.makeText(getApplicationContext(),toastMessage,Toast.LENGTH_LONG).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent loginIntent = new Intent(SplashScreen.this, OnTheMain.class);
                            SplashScreen.this.startActivity(loginIntent);
                            finish();

                        }
                    }, 15000);
                    break;
                default:
                    toastMessage = "Auto login failed, Technical Error";
                    Toast.makeText(getApplicationContext(),toastMessage,Toast.LENGTH_LONG).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent loginIntent = new Intent(SplashScreen.this, OnTheMain.class);
                            SplashScreen.this.startActivity(loginIntent);
                            finish();

                        }
                    }, 15000);


                    break;

            }
        }
    }
}
