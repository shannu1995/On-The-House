package com.onthehouse.onthehouse;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.onthehouse.connection.APIConnection;
import com.onthehouse.details.Member;
import com.onthehouse.details.UtilMethods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.xm.weidongjian.progressbuttonlib.ProgressButton;

public class RegisterActivity extends AppCompatActivity
{

    EditText regEmail;
    EditText regPass;
    EditText regCPass;
    EditText regLName;
    EditText regFName;
    EditText regNickName;
    ProgressButton registerBtn;
    String errorText = "";
    public ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
       /* if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }*/

        regEmail = (EditText) findViewById(R.id.regEmail);
        regPass = (EditText) findViewById(R.id.regPassword);
        regCPass = (EditText) findViewById(R.id.regConfirmPassword);
        regLName = (EditText) findViewById(R.id.regLastName);
        regFName = (EditText) findViewById(R.id.regFirstName);
        regNickName = (EditText) findViewById(R.id.regNickName);
        registerBtn = (ProgressButton) findViewById(R.id.registerBtn);
        layout = (ConstraintLayout) findViewById(R.id.registerLayout);

        registerBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                registerBtn.startRotate();

                String email = regEmail.getText().toString();
                String password = regPass.getText().toString();
                String cPassword = regCPass.getText().toString();
                String lastName = regLName.getText().toString();
                String firstName = regFName.getText().toString();
                String nickName = regNickName.getText().toString();

                APIConnection connection = new APIConnection();
                ArrayList<String> inputList = new ArrayList<String>();
                inputList.add("nickname="+nickName);
                inputList.add("first_name="+firstName);
                inputList.add("last_name="+lastName);
                inputList.add("zip=3000");
                inputList.add("zone_id=216");
                inputList.add("country_id=13");
                inputList.add("timezone_id=106");
                inputList.add("question_id=");
                inputList.add("question_text=");
                inputList.add("email="+email);
                inputList.add("password="+password);
                inputList.add("password_confirm="+cPassword);
                inputList.add("terms=1");

                new registerAsyncData(getApplicationContext()).execute(inputList);

            }
        });



        /*ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.states_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/

    }

    public void setData(Member member, JSONObject jsonArray) {
        try {
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
        catch (Exception e) {

        }
    }

    public class registerAsyncData extends AsyncTask<ArrayList<String>, Void, Integer> {

        Context context;

        public registerAsyncData(Context context) {
            this.context = context;
        }

        protected void preExecute(Integer input)
        {
            registerBtn.setEnabled(false);
        }

        protected Integer doInBackground(ArrayList<String>... params)
        {
            int status = 0;

            APIConnection connection = new APIConnection();
            try
            {
                String output = connection.sendPost("/api/v1/member/create", params[0]);
                if (output.length() > 0)
                {
                    JSONObject obj = new JSONObject(output);
                    String result = obj.getString("status");
                    // JSONArray arr = obj.getJSONArray("member");

                    Log.w("REGISTRATION RESULT", result);

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

                        JSONObject jsonArray = obj.getJSONObject("error");
                        JSONArray errorArr=  jsonArray.getJSONArray("messages");
                        errorText = errorArr.getString(0);
                        Log.w("Registration error", errorText);
                        //2 = wrong details;
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
            if(result == 1)
            {
                registerBtn.animFinish();
                Snackbar.make(layout, "Registration Successful.", Snackbar.LENGTH_LONG).show();
                //Toast.makeText(RegisterActivity.this, "Registration Successful.", Toast.LENGTH_LONG).show();

                Intent registerDoneIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                RegisterActivity.this.startActivity(registerDoneIntent);

            }

            else if(result == 2)
            {
                Snackbar.make(layout, errorText, Snackbar.LENGTH_LONG).show();
                registerBtn.animError();

                //Toast.makeText(RegisterActivity.this, errorText, Toast.LENGTH_LONG).show();
            }
            else
            {
                Snackbar.make(layout, "Registration failed, technical error.", Snackbar.LENGTH_LONG).show();
                registerBtn.animError();

                //Toast.makeText(RegisterActivity.this, "Registration failed, technical error.", Toast.LENGTH_LONG).show();
            }
        }
    }

}
