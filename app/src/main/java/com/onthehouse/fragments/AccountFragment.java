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
import android.widget.TextView;

import com.onthehouse.Utils.DrawerLocker;
import com.onthehouse.Utils.ReservationsAdapter;
import com.onthehouse.connection.APIConnection;
import com.onthehouse.details.Member;
import com.onthehouse.details.Reservation;
import com.onthehouse.onthehouse.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    private ArrayList<Reservation> reservationArrayList = new ArrayList<>();
    private static final String TAG = "AccountFragment";
    private ReservationsAdapter adapter;
    private TextView reservations_label;

    public AccountFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        //Views
        reservations_label = view.findViewById(R.id.tv_my_reservations_label);
        //Text View Membership details
        Member member = Member.getInstance();
        String detailsStr;
        Date expireDate = new Date(member.getMembership_expiry() * 1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateStr = dateFormat.format(expireDate);

        TextView details = view.findViewById(R.id.tv_membership_details);
        if (member.getMembership_level_id() == 9) {
            detailsStr = "Gold - Expires on " + dateStr;
            details.setText(detailsStr);
        } else if (member.getMembership_level_id() == 6) {
            detailsStr = "Silver - Expires on " + dateStr;
            details.setText(detailsStr);
        } else {
            detailsStr = "Bronze - Expires on " + dateStr;
            details.setText(detailsStr);
        }
        //Get Reservation Data
        ArrayList<String> inputList = new ArrayList<>();
        inputList.add("&member_id=" + Member.getInstance().getId());
        new getReservationsAsyncData(container.getContext()).execute(inputList);
        //Recycle View
        RecyclerView recyclerView = view.findViewById(R.id.rv_current_reservations);
        LinearLayoutManager layoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter = new ReservationsAdapter(container.getContext(), reservationArrayList);
        recyclerView.setAdapter(adapter);
        return view;
    }


    public class getReservationsAsyncData extends AsyncTask<ArrayList<String>, Void, Integer> {

        ProgressDialog progressDialog;
        Context context;

        private getReservationsAsyncData(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Fetching Current Reservations");
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            ((DrawerLocker) getActivity()).setDrawerEnabled(false);
        }

        @Override
        protected Integer doInBackground(ArrayList<String>... params) {
            int status = 0;
            APIConnection connection = new APIConnection();
            try {
                String output = connection.sendPost("/api/v1/member/reservations", params[0]);
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

                                reservationArrayList.add(reservation);
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
            if (reservationArrayList.isEmpty()) {
                reservations_label.setText(R.string.no_reservations_available);
            } else {
                reservations_label.setText(R.string.reservations);
            }
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            ((DrawerLocker) getActivity()).setDrawerEnabled(true);
        }
    }
}
