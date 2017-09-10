package com.onthehouse.onthehouse;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.MaterialEditText;
import android.widget.TextView;
import android.widget.Toast;

import com.juanpabloprado.countrypicker.CountryPicker;
import com.juanpabloprado.countrypicker.CountryPickerListener;
import com.onthehouse.connection.APIConnection;
import com.onthehouse.details.Country;
import com.onthehouse.details.Member;
import com.onthehouse.details.UtilMethods;
import com.onthehouse.details.Zone;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.xm.weidongjian.progressbuttonlib.ProgressButton;

public class RegisterActivity extends AppCompatActivity
{
    MaterialEditText regEmail;
    MaterialEditText regPass;
    MaterialEditText regCPass;
    MaterialEditText regLName;
    MaterialEditText regFName;
    MaterialEditText regNickName;
    TextView regCountry;
    MaterialBetterSpinner regState;
    ProgressButton registerBtn;
    Country selectedCountry = new Country();
    String errorText = "";
    public ConstraintLayout layout;
    ArrayList<Country> countryList = new ArrayList<>();
    ArrayList<Zone> zoneList = new ArrayList<>();
    ArrayList<String> zoneNames = new ArrayList<>();
    private static final String TAG = "RegisterActivity";

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

        regEmail = (MaterialEditText) findViewById(R.id.regEmail);
        regPass = (MaterialEditText) findViewById(R.id.regPassword);
        regCPass = (MaterialEditText) findViewById(R.id.regConfirmPassword);
        regLName = (MaterialEditText) findViewById(R.id.regLastName);
        regFName = (MaterialEditText) findViewById(R.id.regFirstName);
        regNickName = (MaterialEditText) findViewById(R.id.regNickName);
        regCountry = (TextView) findViewById(R.id.regCountry);
        regState = (MaterialBetterSpinner) findViewById(R.id.regSpinnerState);
        registerBtn = (ProgressButton) findViewById(R.id.registerBtn);
        layout = (ConstraintLayout) findViewById(R.id.registerLayout);
       // regCountry.set
        new countryAsyncData(getApplicationContext()).execute();
        regState.setVisibility(View.GONE);




        regCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                CountryPicker picker = CountryPicker.getInstance("Select Country", new CountryPickerListener() {
                    @Override public void onSelectCountry(String name, String code) {
                        int country_id = 0;
                        zoneList = new ArrayList<Zone>();
                        Toast.makeText(RegisterActivity.this, "Name: " + name, Toast.LENGTH_SHORT).show();
                        for (Country counter: countryList) {
                            if (counter.getName().equals(name))
                            {
                                country_id = counter.getId();
                                System.out.println("Country name matched "+country_id);
                                //zoneList = setDataStates(country_id);
                                System.out.println("ZoneList extracted "+zoneList);
                                regCountry.setText(name);
                                selectedCountry = counter;
                                new Thread(new StateThread()).start();
                               // BaseAdapter stateAdapter = new ArrayAdapter(getApplicationContext(), R.layout.state_item, R.id.regStateItem, zoneNames);
                                ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, zoneNames);


                                zoneNames.clear();
                                regState.setId(0);
                                regState.setAdapter(stateAdapter);
                                regState.setVisibility(View.VISIBLE);
                                break;
                            }
                        }
                        DialogFragment dialogFragment =
                                (DialogFragment) getSupportFragmentManager().findFragmentByTag("CountryPicker");
                        dialogFragment.dismiss();

                    }
                });
                picker.show(getSupportFragmentManager(), "CountryPicker");
            }
        });


        regPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(regPass.getText().length() < 4) {
                    regPass.setError("Min 4 chars");
                }

                else if(regPass.getText().length() >= 4) {
                    regPass.setError("");
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(regPass.getText().length() < 4) {
                    regPass.setError("Min 4 chars");
                }

                else if(regPass.getText().length() >= 4) {
                    regPass.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(regPass.getText().length() < 4) {
                    regPass.setError("Min 4 chars");
                }

                else if(regPass.getText().length() >= 4) {
                    regPass.setError("");
                }
            }
        });


        regCPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!regPass.getText().toString().equals(regCPass.getText().toString())) {
                    regCPass.setError("Password Not matched");
                }

                else {
                    regCPass.setError("");
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                regCPass.setError("");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!regPass.getText().toString().equals(regCPass.getText().toString())) {
                    regCPass.setError("Password Not matched");
                }

                else {
                    regCPass.setError("");
                }

            }
        });

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
                String countryId = null;
                String zone_id = null;
                for (Zone zoneCounter: zoneList) {
                    if (zoneCounter.getName().equals(regState.getText().toString())) {
                        zone_id = Integer.toString(zoneCounter.getId());
                    }

                }

                for (Country countryCounter: countryList) {
                    if (countryCounter.getName().equals(regCountry.getText()))

                    countryId = Integer.toString(countryCounter.getId());
                }

                APIConnection connection = new APIConnection();
                ArrayList<String> inputList = new ArrayList<String>();
                inputList.add("nickname="+nickName);
                inputList.add("first_name="+firstName);
                inputList.add("last_name="+lastName);
                inputList.add("zip=3000");
                inputList.add("zone_id="+zone_id);
                inputList.add("country_id="+countryId);
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

    class StateThread implements Runnable
    {
        @Override
        public void run()
        {

            int status = 0;
            ArrayList<Zone> zoneArrayList = new ArrayList<>();
            APIConnection connection = new APIConnection();
            try
            {
                int country_id = selectedCountry.getId();
                String url = "/api/v1/zones/"+country_id;
                String output = connection.sendGet(url);
                Log.w( "setDataStates", output );
                status = 1;
                if (output.length() > 0)
                {
                    JSONObject obj = new JSONObject(output);
                    String result = obj.getString("status");
                    System.out.println("result of the states is "+result);
                    Log.w("RESET RESULT", result);

                    if (result.equals("success"))
                    {
                        JSONArray zones = obj.getJSONArray("zones");
                        for (int i = 0; i <zones.length(); i++) {
                            Zone zone = new Zone();
                            System.out.println("json Array length is: "+ zones.length());
                            JSONObject jObj = zones.getJSONObject(i);
                            System.out.println("json Object is: "+ jObj);
                            zone.setId(UtilMethods.tryParseInt(jObj.getString("id")));
                            System.out.println("json Object ID is: "+ zone.getId());
                            zone.setCountry_id(UtilMethods.tryParseInt(jObj.getString("country_id")));
                            System.out.println("json Object is: "+ zone.getCountry_id());
                            zone.setCode(jObj.getString("code"));
                            System.out.println("json Object is: "+ zone.getCode());
                            zone.setName(jObj.getString("name"));
                            System.out.println("json Object is: "+ zone.getName());
                            System.out.println(zone);
                            zoneList.add(zone);
                            zoneNames.add(zone.getName());
                        }
                        //1 = success;
                        status = 1;
                        for (Zone zoneCounter: zoneArrayList) {
                            Log.d(TAG, "doInBackground: country list "+ zoneCounter);
                        }
                        Log.w("status", String.valueOf(status));
                    }
                    else
                    {
                        status = 2;
                        JSONObject jsonArray = obj.getJSONObject("error");
                        JSONArray errorArr=  jsonArray.getJSONArray("messages");
                        errorText = errorArr.getString(0);
                        Log.w("Registration error", errorText);
                    }
                }
                else
                {
                    // 3 = json parse error
                    status = 3;
                    Log.d(TAG, "setDataStates: error status 3");
                }
            } catch (final Exception e) {
                status = 2;
                String err = (e.getMessage()==null)?"Exception occured in main setDataStates block ":e.getMessage();
                Log.d(TAG, "setDataStates: "+err);
            }

        }
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

        protected void onPreExecute()
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
            }
            else
            {
                Snackbar.make(layout, "Registration failed, technical error.", Snackbar.LENGTH_LONG).show();
                registerBtn.animError();
            }
            registerBtn.setEnabled(true);
        }
    }


    public ArrayList<Zone>  setDataStates(int country_id) {

        int status = 0;
        ArrayList<Zone> zoneArrayList = new ArrayList<>();
        APIConnection connection = new APIConnection();
        try {
            String url = "/api/v1/zones/"+country_id;
            String output = connection.sendGet(url);
            Log.w( "setDataStates", output );
            status = 1;
            if (output.length() > 0)
            {
                JSONObject obj = new JSONObject(output);
                String result = obj.getString("status");
                System.out.println("result of the states is "+result);
                Log.w("RESET RESULT", result);

                if (result.equals("success")) {
                    JSONArray zones = obj.getJSONArray("zones");
                    for (int i = 0; i <zones.length(); i++) {
                            Zone zone = new Zone();
                            System.out.println("json Array length is: "+ zones.length());
                            JSONObject jObj = zones.getJSONObject(i);
                            System.out.println("json Object is: "+ jObj);
                            zone.setId(UtilMethods.tryParseInt(jObj.getString("id")));
                            System.out.println("json Object ID is: "+ zone.getId());
                            zone.setCountry_id(UtilMethods.tryParseInt(jObj.getString("country_id")));
                            System.out.println("json Object is: "+ zone.getCountry_id());
                            zone.setCode(jObj.getString("code"));
                            System.out.println("json Object is: "+ zone.getCode());
                            zone.setName(jObj.getString("name"));
                            System.out.println("json Object is: "+ zone.getName());
                            System.out.println(zone);
                            zoneArrayList.add(zone);
                        }
                    //1 = success;
                    status = 1;
                        for (Zone zoneCounter: zoneArrayList) {
                            Log.d(TAG, "doInBackground: country list "+ zoneCounter);
                        }
                    Log.w("status", String.valueOf(status));
                }
                else
                {
                    status = 2;
                    JSONObject jsonArray = obj.getJSONObject("error");
                    JSONArray errorArr=  jsonArray.getJSONArray("messages");
                    errorText = errorArr.getString(0);
                    Log.w("Registration error", errorText);
                }
            }
            else
            {
                // 3 = json parse error
                status = 3;
                Log.d(TAG, "setDataStates: error status 3");
            }
        } catch (final Exception e) {
            status = 2;
            String err = (e.getMessage()==null)?"Exception occured in main setDataStates block ":e.getMessage();
            Log.d(TAG, "setDataStates: "+err);
        }
        return zoneArrayList;
    }


    //setting data for countries
    public void setDataCountry( JSONArray jsonCountriesArray) {

        ArrayList<Country> countryArrayList = new ArrayList<>();

        try {   //country = new Country();
                for (int i = 0; i <jsonCountriesArray.length(); i++) {
                    Country country = new Country();
                    JSONObject jObj = jsonCountriesArray.getJSONObject(i);
                    System.out.println("json Object is: "+ jObj);
                    country.setId(UtilMethods.tryParseInt(jObj.getString("id")));
                    System.out.println("json Object ID is: "+ country.getId());
                    country.setName(jObj.getString("name"));
                    System.out.println("json Object is: "+ country.getName());
                    country.setIso_code_2(jObj.getString("iso_code_2"));
                    System.out.println("json Object is: "+ country.getIso_code_2());
                    country.setIso_code_3(jObj.getString("iso_code_3"));
                    countryList.add(country);
                }
            for (int i = 0; i < countryArrayList.size(); i++){
            System.out.println("Country id and name is "+countryArrayList.get(i).getId()+" "+countryArrayList.get(i).getName()+ " ");

            }
        }
        catch (final Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.w("Country array", e.getMessage());
                    Toast.makeText(getApplicationContext(), "Country Json Parsing Error :" +e.getMessage(), Toast.LENGTH_LONG ).show();
                }
            });

        }


    }



    public class countryAsyncData extends AsyncTask<ArrayList<String>, Void, Integer> {
        private static final String TAG = "inputAsyncData";
        Context context;

        public countryAsyncData(Context context) {
            this.context = context;
        }


        protected void onPreExecute() {

        }


        @Override
        protected Integer doInBackground(ArrayList<String>... params) {
            int status = 0;

            APIConnection connection = new APIConnection();
            try
            {
                String url = "/api/v1/countries";
                String output = connection.sendGet(url);
                status = 1;

                if (output.length() > 0)
                {

                    JSONObject obj = new JSONObject(output);

                    String result = obj.getString("status");
                    if (result.equals("success")) {
                        JSONArray countries = obj.getJSONArray("countries");
                        setDataCountry(countries);
                        //1 = success;
                        status = 1;
//                        for (Country countryCounter: countryList) {
//                            Log.d(TAG, "doInBackground: country list "+ countryCounter);
//                        }

                        Log.w("status", String.valueOf(status));
                    } else {
                        //2 = wrong details;
                        status = 2;}
                } else {
                    // 3 = json parse error
                    status = 3;
                }
            } catch (Exception e) {
                Log.d(TAG, "doInBackground: there is a problem with connection");
                status = 2;
            }
            return status;
        }


        protected void onPostExecute(Integer result) {
//            Log.w("Reset result", result.toString());
//
//            if(result == 1)
//            {
//                Log.d(TAG, "onPostExecute: ");
////                resetButton.animFinish();
////                Snackbar.make(layout, "Reset Password Successful.", Snackbar.LENGTH_LONG).show();
////                Intent resetDoneIntent = new Intent(ResetPassword.this, LoginActivity.class);
////                ResetPassword.this.startActivity(resetDoneIntent);
//            }
//            else if(result == 2)
//            {
////                resetButton.animError();
////                Snackbar.make(layout, "Reset failed, please check your email.", Snackbar.LENGTH_LONG).show();
//            }
//
//            else
//            {
////                resetButton.animError();
////                Snackbar.make(layout, "Reset failed, technical error.", Snackbar.LENGTH_LONG).show();
//            }
////            resetButton.setEnabled(true);
//        }
        }
    }
}
