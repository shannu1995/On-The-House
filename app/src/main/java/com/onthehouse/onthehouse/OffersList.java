package com.onthehouse.onthehouse;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.onthehouse.connection.APIConnection;
import com.onthehouse.details.Member;
import com.onthehouse.details.OfferDetail;
import com.onthehouse.details.UtilMethods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class OffersList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers_list);

        ArrayList<String> inputList = new ArrayList<String>();
        inputList.add("");

        new inputAsyncData(getApplicationContext()).execute(inputList);

    }

    public class inputAsyncData extends AsyncTask<ArrayList<String>, Void, Integer>
    {

        Context context;

        public inputAsyncData(Context context) {
            this.context = context;
        }

        protected void onPreExecute()
        {

        }

        protected Integer doInBackground(ArrayList<String>... params)
        {
            int status = 0;

            APIConnection connection = new APIConnection();
            try
            {
                String output = connection.sendPost("/api/v1/events/current", params[0]);
                if (output.length() > 0)
                {
                    JSONObject obj = new JSONObject(output);
                    String result = obj.getString("status");
                    // JSONArray arr = obj.getJSONArray("member");

                    Log.w("LOGIN RESULT", result);

                    if (result.equals("success"))
                    {
                        try
                        {
                            //Member member = Member.getInstance();
                            JSONArray jsonArray = obj.getJSONArray("events");
                            Log.w("events", jsonArray.toString());

                            OfferDetail.clearInstance();
                            ArrayList<OfferDetail> offerDetails = OfferDetail.getInstance();
                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                Log.w("for", String.valueOf(i));
                                OfferDetail detail = new OfferDetail();
                                JSONObject event = jsonArray.getJSONObject(i);
                                detail.setOfferId(UtilMethods.tryParseInt(event.getString("id")));
                                detail.setType(event.getString("type"));
                                detail.setName(event.getString("name"));
                                detail.setPageTitle(event.getString("page_title"));
                                detail.setRating(UtilMethods.tryParseInt(event.getString("rating")));
                                detail.setImageURL(event.getString("image_url"));
                                detail.setDescription(event.getString("description"));
                                detail.setPriceFrom(event.getDouble("price_from"));
                                detail.setPriceTo(event.getDouble("price_to"));
                                detail.setFullPrice(event.getString("full_price_string"));
                                detail.setOurPrice(event.getString("our_price_string"));
                                detail.setOurPriceHeading(event.getString("our_price_heading"));
                                detail.setMemberShipLevel(event.getString("membership_levels"));
                                detail.setSoldOut(event.getBoolean("sold_out"));
                                detail.setComingSoon(event.getBoolean("coming_soon"));
                                detail.setCompt(event.getBoolean("is_competition"));
                                offerDetails.add(detail);

                            }
                            //Log.w("eventtt", offerDetails.get(0).getDescription());
                            //Log.w("eventtt", offerDetails.get(1).getDescription());

                            status = 1;
                        }
                        catch(Exception e)
                        {
                            Log.w("Event error", e.getMessage());
                            status = 3;
                        }

                        //Log.w("status", String.valueOf(status));
                        //Log.w("OBJECT TEST", member.getFirst_name());
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

        protected void onPostExecute(Integer result)
        {
            if(result == 1)
            {
                //loginButton.animFinish();
                //Snackbar.make(layout, "Login successful.", Snackbar.LENGTH_LONG).show();

            }

            else if(result == 2)
            {
                //loginButton.animError();
                //Snackbar.make(layout, "Login failed, please check your details", Snackbar.LENGTH_LONG).show();
            }

            else
            {
                //loginButton.animError();
                //Snackbar.make(layout, "Login failed, technical error.", Snackbar.LENGTH_LONG).show();
            }
            //loginButton.setEnabled(true);
        }
    }
}
