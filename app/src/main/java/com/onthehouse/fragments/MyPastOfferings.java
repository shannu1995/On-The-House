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

import com.onthehouse.connection.APIConnection;
import com.onthehouse.details.Member;
import com.onthehouse.onthehouse.R;

import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyPastOfferings extends Fragment {


    public MyPastOfferings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_past_offerings, container, false);
        ArrayList<String> inputList = new ArrayList<>();
        inputList.add("&member_id=" + Member.getInstance().getId());
        new getPastReservationAsyncData().execute(inputList);
        return view;
    }

    private class getPastReservationAsyncData extends AsyncTask<ArrayList<String>, Void, Integer> {

        Context context;
        ProgressDialog progressDialog;

        @Override
        protected Integer doInBackground(ArrayList<String>... params) {
            int status = 0;
            APIConnection connection = new APIConnection();
            try {
                String output = connection.sendPost("/api/v1/member/reservations/past", params[0]);
                if (output.length() > 0) {
                    JSONObject obj = new JSONObject(output);
                    String result = obj.getString("status");

//                    if (result.equals("success")) {
//                        try {
//                            JSONArray jsonArray = obj.getJSONArray("events");
//
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                PastOffers pastOffer = new PastOffers();
//                                JSONObject event = jsonArray.getJSONObject(i);
//                                pastOffer.setId(UtilMethods.tryParseInt(event.getString("id")));
//
//                                pastOffer.setName(event.getString("name"));
//                                pastOffer.setDescription(event.getString("description"));
//                                pastOffer.setRating(event.getInt("rating"));
//
//                            }
//                            status = 1;
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            Log.w("PAST Event error", e.getMessage());
//                            status = 3;
//                        }
//                    } else {
//                        status = 2;
//                        //Wrong Details
//                        Log.d(TAG, "doInBackground: Wrong details, Status = " + status);
//                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "doInBackground: " + e.getMessage());
                status = 3;
            }
            return status;
        }
    }

}
