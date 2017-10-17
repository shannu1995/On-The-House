package com.onthehouse.onthehouse;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.onthehouse.connection.APIConnection;
import com.onthehouse.details.Country;
import com.onthehouse.details.UtilMethods;
import com.onthehouse.details.Zone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class EditMemberActivity extends AppCompatActivity {

    private static final String TAG = "EditMemberActivity";
    ArrayList<Country> countryList = new ArrayList<>();
    Country selectedCountry = new Country();
    String errorText = "";
    ArrayList<String> zoneNames = new ArrayList<String>();
    ArrayAdapter<String> stateAdapter = null;
    ArrayList<Zone> zoneList;
    ArrayList<String> timeZoneList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_member);
    }


    //setting data for countries
    public HashMap<String, Country> setDataCountry(final Context mContext, JSONArray jsonCountriesArray) {
        HashMap<String, Country> countries = new HashMap<String, Country>();
        Country country = null;

        try {   //country = new Country();
            for (int i = 0; i <jsonCountriesArray.length(); i++)
            {
                country = new Country();
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
        }
        catch (final JSONException e) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, "Json Parsing Error :" +e.getMessage(), Toast.LENGTH_LONG ).show();
                }
            });

        }

        return countries;
    }

    public class countryAsyncData extends AsyncTask<ArrayList<Country>, Void, Integer> {
        private static final String TAG = "inputAsyncData";
        Context context;

        public countryAsyncData(Context context) {
            this.context = context;
        }


        protected void onPreExecute() {

        }


        @Override
        protected Integer doInBackground(ArrayList<Country>... params) {
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
                        setDataCountry(context, countries);
                        Log.d(TAG, "doInBackground: setting json_countries_array "+countries );
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

        }
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
                        for (int i = 0; i <zones.length(); i++)
                        {
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
                }
            } catch (final Exception e) {
                status = 2;
                String err = (e.getMessage()==null)?"Exception occured in main setDataStates block ":e.getMessage();
            }

        }
    }

    class TimeZoneThread implements Runnable {

        int status = 0;
        ArrayList<String> TimezoneArrayList = new ArrayList<>();

        public void run()
        {

            int status = 0;
            ArrayList<Zone> TimezoneArrayList = new ArrayList<>();
            APIConnection connection = new APIConnection();
            try
            {
                int country_id = selectedCountry.getId();
                String url = "/api/v1/timezones";
                String output = connection.sendGet(url);
                Log.w( "setDataTimeZones", output );
                status = 1;
                if (output.length() > 0)
                {
                    JSONObject obj = new JSONObject(output);
                    String result = obj.getString("status");
                    Log.d(TAG, "run: result of time zones is "+ result);


                    if (result.equals("success"))
                    {
                        JSONArray timeZones = obj.getJSONArray("zones");
                        for (int i = 0; i < timeZones.length(); i++)
                        {
                            Zone zone = new Zone();
//                            System.out.println("json Array length is: "+ zones.length());
//                            JSONObject jObj = zones.getJSONObject(i);
//                            System.out.println("json Object is: "+ jObj);
//                            zone.setId(UtilMethods.tryParseInt(jObj.getString("id")));
//                            System.out.println("json Object ID is: "+ zone.getId());
//                            zone.setCountry_id(UtilMethods.tryParseInt(jObj.getString("country_id")));
//                            System.out.println("json Object is: "+ zone.getCountry_id());
//                            zone.setCode(jObj.getString("code"));
//                            System.out.println("json Object is: "+ zone.getCode());
//                            zone.setName(jObj.getString("name"));
//                            System.out.println("json Object is: "+ zone.getName());
//                            System.out.println(zone);
//
//                            zoneList.add(zone);
//
//                            zoneNames.add(zone.getName());
                        }
                        //1 = success;
                        status = 1;
                        for (Zone zoneCounter: TimezoneArrayList) {
                        }
                        Log.w("status", String.valueOf(status));
                    }
                    else
                    {
                        status = 2;
                        JSONObject jsonArray = obj.getJSONObject("error");
                        JSONArray errorArr=  jsonArray.getJSONArray("messages");
                        errorText = errorArr.getString(0);
                        Log.w("Update error", errorText);
                    }
                }
                else
                {
                    // 3 = json parse error
                    status = 3;
                }
            } catch (final Exception e) {
                status = 2;
                String err = (e.getMessage()==null)?"Exception occured in main setTimeZones block ":e.getMessage();
            }

        }

    }
}
