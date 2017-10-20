package com.onthehouse.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.onthehouse.Utils.CheckConnection;
import com.onthehouse.connection.APIConnection;
import com.onthehouse.details.Member;
import com.onthehouse.details.UtilMethods;
import com.onthehouse.guest.GuestMain;
import com.onthehouse.onthehouse.MainMenu;
import com.onthehouse.onthehouse.R;
import com.onthehouse.onthehouse.ResetPassword;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.xm.weidongjian.progressbuttonlib.ProgressButton;

public class LoginActivity extends Fragment
{
    private SharedPreferences sharedPreferences;
    public EditText email;
    public EditText password;
    public TextView skip;
    public TextView forgotPassword;
    private ProgressButton loginButton;
    private SharedPreferences.Editor editor;
    private ArrayList<String> inputList = new ArrayList<>();
    private String emailStr;
    private String passStr;
    public ConstraintLayout layout;
    public CheckBox checkLogin;
    public boolean checkLognText;
    public CheckConnection checkConnection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_login, container, false);
        final Context mContext = container.getContext();

        email = (EditText) view.findViewById(R.id.loginEmail);
        password = (EditText) view.findViewById(R.id.loginPassword);
        loginButton= (ProgressButton) view.findViewById(R.id.loginButton);
        checkLogin = (CheckBox) view.findViewById(R.id.chkLogin);
        skip = (TextView) view.findViewById(R.id.skip);
        forgotPassword = (TextView) view.findViewById(R.id.forgot_Password);

        layout = (ConstraintLayout) view.findViewById(R.id.loginlayout);
        //Shared Preferences
        sharedPreferences = getContext().getSharedPreferences("memberInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        checkConnection = new CheckConnection(this.getActivity());
        checkConnection.check();
        checkLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                checkLognText = b;
            }
        });

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(email.getText().length() <= 0) {
                    email.setError(null);
                }

                else if(!isValidEmail(email.getText().toString())) {
                    email.setError("Invalid Email Address");
                }

                else {
                    email.setError(null);
                }

            }
        });

        layout = (ConstraintLayout) view.findViewById(R.id.loginlayout);
        //Shared Preference
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(password.getText().length() <= 0) {
                    password.setError(null);
                }

                else if(password.getText().length() < 4) {
                    password.setError("Min 4 chars");
                }

                else {
                    password.setError(null);
                }
            }
        });

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

                new inputAsyncData(mContext).execute(inputList);
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgotPass = new Intent(getActivity(), ResetPassword.class);
                startActivity(forgotPass);
            }
        });

        skip.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SharedPreferences guestPreference = getContext().getSharedPreferences("GuestMember", Context.MODE_PRIVATE);
                SharedPreferences.Editor guestEditor = guestPreference.edit();
                guestEditor.putBoolean("GuestCheck", true);
                guestEditor.apply();
                Intent resetIntent = new Intent(mContext, GuestMain.class);
                LoginActivity.this.startActivity(resetIntent);
            }
        });

        return view;
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
            member.setLanguage_id(jsonArray.getString("language_id"));
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
                    if (result.equals("success"))
                    {
                        Member member = Member.getInstance();
                        JSONObject jsonArray = obj.getJSONObject("member");
                        setData(member, jsonArray);
                        //1 = success;
                        status = 1;
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
                //Checkout as member session
                SharedPreferences guestPreference = getContext().getSharedPreferences("GuestMember", Context.MODE_PRIVATE);
                SharedPreferences.Editor guestEditor = guestPreference.edit();
                guestEditor.putBoolean("GuestCheck", false);
                guestEditor.apply();
                loginButton.animFinish();
                //Save auto login info
                editor.putString("memberEmail", Member.getInstance().getEmail());
                editor.putString("memberPass",Member.getInstance().getPassword());
                editor.putBoolean("RememberMe" , checkLognText);
                editor.apply();
                Toast.makeText(context, "Login Successful\nLogged in as: " +
                                Member.getInstance().getFirst_name() + " " + Member.getInstance().getLast_name()
                        ,Toast.LENGTH_LONG).show();
                Intent mainMenuIntent = new Intent(context, MainMenu.class);
                LoginActivity.this.startActivity(mainMenuIntent);
                getActivity().finish();
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
            }
            loginButton.setEnabled(true);
        }
    }

    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
