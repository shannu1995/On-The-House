package com.onthehouse.onthehouse;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.onthehouse.connection.APIConnection;
import com.onthehouse.details.Member;
import com.onthehouse.details.UtilMethods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity
{
    public EditText email;
    public EditText password;
    public Button loginButton;
    public TextView register;
    public TextView skip;
    public TextView reset;
    public ConstraintLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }*/

        email = (EditText) findViewById(R.id.loginEmail);
        password = (EditText) findViewById(R.id.loginPassword);
        loginButton= (Button)findViewById(R.id.loginButton);

        register = (TextView) findViewById(R.id.signup);
        skip = (TextView) findViewById(R.id.skip);
        reset = (TextView) findViewById(R.id.resetPassword);

        layout = (ConstraintLayout) findViewById(R.id.loginlayout);


        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                String emailStr = email.getText().toString();
                String passStr = password.getText().toString();

                ArrayList<String> inputList = new ArrayList<String>();
                inputList.add("email="+emailStr);
                inputList.add("password="+passStr);

                new inputAsyncData(getApplicationContext()).execute(inputList);


            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });
        skip.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent offerIntent = new Intent(LoginActivity.this, OffersList.class);
                LoginActivity.this.startActivity(offerIntent);
            }
        });

        reset.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent resetIntent = new Intent(LoginActivity.this, ResetPassword.class);
                LoginActivity.this.startActivity(resetIntent);
            }
        });
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
            member.setAge(UtilMethods.tryParseInt(jsonArray.getString("age")));
            member.setLanguage_id(UtilMethods.tryParseInt(jsonArray.getString("language_id")));
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



    public class inputAsyncData extends AsyncTask<ArrayList<String>, Void, Integer> {

        Context context;

        public inputAsyncData(Context context) {
            this.context = context;
        }

        protected Integer doInBackground(ArrayList<String>... params)
        {
            int status = 0;

            APIConnection connection = new APIConnection();
            try
            {
                String output = connection.sendPost("/api/v1/member/login", params[0]);
                if (output.length() > 0)
                {
                    JSONObject obj = new JSONObject(output);
                    String result = obj.getString("status");
                    // JSONArray arr = obj.getJSONArray("member");

                    Log.w("LOGIN RESULT", result);

                    if (result.equals("success"))
                    {
                        Member member = Member.getInstance();
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
                status = 3;
            }

            return status;
        }


        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Integer result)
        {
            Log.w("Login result", result.toString());

            if(result == 1)
            {
                Snackbar.make(layout, "Login successful.", Snackbar.LENGTH_LONG).show();
                //Toast.makeText(LoginActivity.this, "Login successful.", Toast.LENGTH_LONG).show();

                //Intent loginDoneIntent = new Intent(LoginActivity.this, OffersList.class);
                //LoginActivity.this.startActivity(loginDoneIntent);
            }
            else if(result == 2)
            {
                Snackbar.make(layout, "Login failed, please check your details", Snackbar.LENGTH_LONG).show();

                //Toast.makeText(LoginActivity.this, "Login failed, please check your details", Toast.LENGTH_LONG).show();
            }
            else
            {
                Snackbar.make(layout, "Login failed, technical error.", Snackbar.LENGTH_LONG).show();

                //Toast.makeText(LoginActivity.this, "Login failed, technical error.", Toast.LENGTH_LONG).show();
            }
        }
    }

}
