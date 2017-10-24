package com.onthehouse.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onthehouse.Utils.DrawerLocker;
import com.onthehouse.Utils.ReservationsAdapter;
import com.onthehouse.connection.APIConnection;
import com.onthehouse.details.Member;
import com.onthehouse.details.Reservation;
import com.onthehouse.onthehouse.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyPastOfferings extends Fragment {

    private static final String TAG = "MyPastOfferings";
    private ArrayList<Reservation> pastReservations = new ArrayList<>();
    private ReservationsAdapter adapter;

    public MyPastOfferings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_past_offerings, container, false);
        //Get Reservation Data
        ArrayList<String> inputList = new ArrayList<>();
        inputList.add("&member_id=" + Member.getInstance().getId());
        new getPastReservationAsyncData(container.getContext()).execute(inputList);
        //Recycler View
        RecyclerView recyclerView = view.findViewById(R.id.rv_my_past_reservations);
        LinearLayoutManager layoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter = new ReservationsAdapter(container.getContext(), pastReservations);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private class getPastReservationAsyncData extends AsyncTask<ArrayList<String>, Void, Integer> {

        Context context;
        ProgressDialog progressDialog;

        public getPastReservationAsyncData(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Fetching Past Offerings");
            progressDialog.setMessage("Please wait...");
            progressDialog.show();

            ((DrawerLocker) getActivity()).setDrawerEnabled(false);
        }

        @Override
        protected Integer doInBackground(ArrayList<String>... params) {
            int status = 0;
            APIConnection connection = new APIConnection();
            try {
                String output = connection.sendPost("/api/v1/member/reservations/past", params[0]);
                if (output.length() > 0) {
                    JSONObject obj = new JSONObject(output);
                    String result = obj.getString("status");
                    if (result.equals("success")) {
                        try {
                            JSONArray jsonArray = obj.getJSONArray("reservations");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                Reservation reservation = new Reservation();

                                JSONObject reservationJsonObject = jsonArray.getJSONObject(i);

                                reservation.setId(reservationJsonObject.getInt("reservation_id"));
                                reservation.setEvent_id(reservationJsonObject.getInt("event_id"));
                                reservation.setShow_id(reservationJsonObject.getInt("show_id"));
                                reservation.setEvent_name(reservationJsonObject.getString("event_name"));
                                reservation.setDate(reservationJsonObject.getString("date"));
                                reservation.setNum_tickets(reservationJsonObject.getInt("num_tickets"));
                                reservation.setVenue_id(reservationJsonObject.getInt("venue_id"));
                                reservation.setVenue_name(reservationJsonObject.getString("venue_name"));
                                reservation.setType(reservationJsonObject.getString("type"));
                                reservation.setCan_cancel(reservationJsonObject.getBoolean("can_cancel"));
                                reservation.setCan_rate(reservationJsonObject.getBoolean("can_rate"));
                                reservation.setHas_rated(reservationJsonObject.getBoolean("has_rated"));

                                pastReservations.add(reservation);
                            }
                            status = 1;

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.w("Reservation error", e.getMessage());
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
            ((DrawerLocker) getActivity()).setDrawerEnabled(true);
        }
    }

}
