package com.onthehouse.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.onthehouse.Utils.PastOffersAdapter;
import com.onthehouse.connection.APIConnection;
import com.onthehouse.details.PastOffers;
import com.onthehouse.details.UtilMethods;
import com.onthehouse.onthehouse.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PastOffersList extends Fragment {

    private PastOffersAdapter adapter;
    private ArrayList<PastOffers> pastOffersArrayList;

    public PastOffersList() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_past_offers, container, false);
        Context context = container.getContext();

        ListView offerViewList = view.findViewById(R.id.past_offers_list_view);
        pastOffersArrayList = new ArrayList<>();
        adapter = new PastOffersAdapter(context, pastOffersArrayList);
        offerViewList.setAdapter(adapter);

        ArrayList<String> inputList = new ArrayList<>();
        inputList.clear();
        new inputAsyncData(context).execute(inputList);

        return view;
    }

    private class inputAsyncData extends AsyncTask<ArrayList<String>, Void, Integer> {

        Context context;
        ProgressDialog progressDialog;
        private static final String TAG = "inputAsyncData";

        private inputAsyncData(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Fetching Past Offers");
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected Integer doInBackground(ArrayList<String>... params) {
            int status = 0;
            APIConnection connection = new APIConnection();
            try {
                String output = connection.sendPost("/api/v1/events/past", params[0]);
                if (output.length() > 0) {
                    JSONObject obj = new JSONObject(output);
                    String result = obj.getString("status");

                    if (result.equals("success")) {
                        try {
                            JSONArray jsonArray = obj.getJSONArray("events");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                PastOffers pastOffer = new PastOffers();
                                JSONObject event = jsonArray.getJSONObject(i);
                                pastOffer.setId(UtilMethods.tryParseInt(event.getString("id")));

                                pastOffer.setName(event.getString("name"));
                                pastOffer.setDescription(event.getString("description"));
                                pastOffer.setRating(event.getInt("rating"));

                                pastOffersArrayList.add(pastOffer);
                            }
                            status = 1;

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.w("PAST Event error", e.getMessage());
                            status = 3;
                        }
                    } else {
                        status = 2;
                        //Wrong Details
                        Log.d(TAG, "doInBackground: Wrong details, Status = " + status);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "doInBackground: " + e.getMessage());
                status = 3;
            }

            return status;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            adapter.notifyDataSetChanged();
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }
    }
}
