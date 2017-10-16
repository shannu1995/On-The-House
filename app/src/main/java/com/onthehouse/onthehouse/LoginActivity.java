package com.onthehouse.onthehouse;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.onthehouse.connection.APIConnection;
import com.onthehouse.details.Member;
import com.onthehouse.details.UtilMethods;

import org.json.JSONObject;

import java.util.ArrayList;

import cn.xm.weidongjian.progressbuttonlib.ProgressButton;

public class LoginActivity extends AppCompatActivity
{
    public EditText email;
    public EditText password;
    //public Button loginButton;
    public TextView register;
    public TextView skip;
    public TextView reset;
    private ProgressButton loginButton;
    private SharedPreferences.Editor editor;
    private ArrayList<String> inputList = new ArrayList<>();
    private String emailStr;
    private String passStr;

    public ConstraintLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = (EditText) findViewById(R.id.loginEmail);
        password = (EditText) findViewById(R.id.loginPassword);
        loginButton= (ProgressButton) findViewById(R.id.loginButton);

        //register = (TextView) findViewById(R.id.signup);
        skip = (TextView) findViewById(R.id.skip);
        reset = (TextView) findViewById(R.id.resetPassword);

        layout = (ConstraintLayout) findViewById(R.id.loginlayout);
        //Shared Preference
        SharedPreferences sharedPreferences = getSharedPreferences("memberInfo",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                loginButton.startRotate();

                emailStr = email.getText().toString();
                passStr = password.getText().toString();

                inputList.add("&email="+emailStr);
                inputList.add("&password="+passStr);

                new inputAsyncData(getApplicationContext()).execute(inputList);


            }
        });

        /*
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });
        */
        skip.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent offerIntent = new Intent(LoginActivity.this, MainMenu.class);
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
            //    member.setLanguage_id(UtilMethods.tryParseInt(jsonArray.getString("language_id")));
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
        String output = new String("");

        public inputAsyncData(Context context) {
            this.context = context;
        }

        protected void onPreExecute()
        {
            loginButton.setEnabled(false);
        }

        protected Integer doInBackground(ArrayList<String>... params) {
            int status = 0;
            try {
                APIConnection connection = new APIConnection();
                output = connection.sendPost("/api/v1/member/login", params[0]);
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
                } else
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

        protected void onPostExecute(Integer result)
        {
            if(result == 1)
            {
                loginButton.animFinish();
                editor.putString("memberEmail", Member.getInstance().getEmail());
                editor.putString("memberPass",Member.getInstance().getPassword());
                editor.apply();
                Toast.makeText(getApplicationContext(),"Login Successful\nLoggded in as: " +
                        Member.getInstance().getFirst_name() + " " + Member.getInstance().getLast_name()
                        ,Toast.LENGTH_LONG).show();
                Intent mainMenuIntent = new Intent(LoginActivity.this, MainMenu.class);
                LoginActivity.this.startActivity(mainMenuIntent);
                finish();
            }

            else if(result == 2)
            {
                loginButton.animError();
                Snackbar.make(layout, "Login failed, please check your details", Snackbar.LENGTH_LONG).show();
            }

            else
            {
                loginButton.animError();
                Snackbar.make(layout, "Login failed, technical error.", Snackbar.LENGTH_LONG).show();
                Log.w("LOGINFUCKINGERROR!", output);
            }
            loginButton.setEnabled(true);
        }
    }

}
