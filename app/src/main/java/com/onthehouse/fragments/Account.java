package com.onthehouse.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.onthehouse.connection.APIConnection;
import com.onthehouse.details.Member;
import com.onthehouse.onthehouse.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class Account extends Fragment {
    public TextView membership;
    public TextView welcome;
    public TextView reservations;
    public TextView reservationHeading;
    public String errorText = "";
    public ConstraintLayout layout;
    public ArrayList<String> id = new ArrayList<>();
    public HashMap<String, String> reservationData = new HashMap<String, String>();

    public Account() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_account, container, false);
        final Context mContext = container.getContext();

        layout = (ConstraintLayout) view.findViewById(R.id.account_layout);
        membership = view.findViewById(R.id.membership_status);
        welcome = view.findViewById(R.id.welcome);
        reservationHeading = view.findViewById(R.id.reservation_heading);
        //reservations = view.findViewById(R.id.current_reservations);

        membership.setText("Membership: ");
        if(Member.getInstance().getMembership_level_id() == 3){
            membership.append("Bronze - ");
        }
        else{
            membership.append("Gold - ");
        }
        Date expiry = new Date(Member.getInstance().getMembership_expiry() * 1000);
        membership.append("Expires on "+expiry.toString());
        reservationHeading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id.add("member_id="+Integer.toString(Member.getInstance().getId()));
                new getReservationsAsyncData(mContext).execute(id);
            }
        });
        /*
        int i = reservationString.indexOf("[");
        int j = reservationString.indexOf("]");
        reservations.setMovementMethod(LinkMovementMethod.getInstance());
        reservations.setText(reservationString, TextView.BufferType.SPANNABLE);
        final Spannable mySpannable = (Spannable)reservations.getText();
        final Pattern pattern = Pattern.compile("0-9+");
        ClickableSpan myClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Matcher matcher = pattern.matcher(mySpannable);
                while(matcher.find()){
                    Log.w("Chose to cancel",matcher.group());
                }
            }
        };
        mySpannable.setSpan(myClickableSpan, i, j + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        */
        return view;
    }
    /*
    * */
    public class getReservationsAsyncData extends AsyncTask<ArrayList<String>, Void, Integer> {
        Context context;
        JSONObject object = new JSONObject();
        JSONArray reservationsArray = new JSONArray();
        ArrayList<String> inputList = new ArrayList<>();

        public getReservationsAsyncData(Context context){
            this.context = context;
        }

        protected void onPreExecute()
        {

        }
        @Override
        protected Integer doInBackground(ArrayList<String>... params){
            int status = 0;
            String output = "";
            try{
                APIConnection connection = new APIConnection();
                output = connection.sendPost("/api/v1/member/reservations", params[0]);
                Log.w("reservations: ", output);

                if (output.length() > 0){
                    object = new JSONObject(output);
                    String result = object.getString("status");
                    reservationsArray = object.getJSONArray("reservations");
                    if(result.equals("success")){
                        status = 1;
                    }else{
                        status = 2;
                        JSONObject jsonArray = object.getJSONObject("error");
                        JSONArray errorArr=  jsonArray.getJSONArray("messages");
                        errorText = errorArr.getString(0);
                        Log.w("Submission error", errorText);
                    }
                }
                else{
                    status = 3;
                }
            }catch(Exception e) {
                status = 3;
            }
            return status;
        }
        @Override
        protected void onPostExecute(Integer result)
        {
            if(result == 1)
            {
                JSONArray reservationArray = this.reservationsArray;
                TableLayout table = layout.findViewById(R.id.tableLayout);
                for(int i = 0; i < reservationArray.length(); i++){
                    try{
                        final JSONObject singleReservation = reservationArray.getJSONObject(i);

                        TableRow row = new TableRow(context);

                        TextView name = new TextView(context);
                        name.setText(singleReservation.getString("event_name"));
                        row.addView(name);

                        TextView date = new TextView(context);
                        date.setText(singleReservation.getString("date"));
                        row.addView(date);

                        TextView number = new TextView(context);
                        number.setText(singleReservation.getString("num_tickets"));
                        row.addView(number);

                        TextView venue = new TextView(context);
                        venue.setText(singleReservation.getString("venue_name"));
                        row.addView(venue);

                        TextView cancel = new TextView(context);
                        cancel.setText("Cancel Reservation");
                        int tempId = Integer.parseInt(singleReservation.getString("reservation_id"));
                        cancel.setId(tempId);
                        cancel.setTextColor(Color.BLUE);
                        row.addView(cancel);
                        table.addView(row);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                inputList.add("&reservation_id="+Integer.toString(view.getId()));
                                inputList.add("&member_id="+Integer.toString(Member.getInstance().getId()));
                                new cancelReservationAsyncData(context).execute(inputList);
                            }
                        });

                        String reservation_display = singleReservation.getString("event_name") + "\t" +
                                singleReservation.getString("date") + "\t" +
                                singleReservation.getString("num_tickets") + "\t" +
                                singleReservation.getString("venue_name") + "\t" +
                                "[Cancel Reservation]";
                        reservationData.put(singleReservation.getString("reservation_id"), reservation_display);
                    }catch(Exception e){

                    }
                }
                Snackbar.make(layout, "Got Reservations", Snackbar.LENGTH_LONG).show();
            }
            else if(result == 2)
            {
                Snackbar.make(layout, errorText, Snackbar.LENGTH_LONG).show();
            }
            else
            {
                Snackbar.make(layout, "Submission failed, technical error.", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    public class cancelReservationAsyncData extends AsyncTask<ArrayList<String>, Void, Integer> {
        Context context;
        JSONObject object = new JSONObject();

        public cancelReservationAsyncData(Context context){
            this.context = context;
        }

        protected void onPreExecute()
        {

        }
        @Override
        protected Integer doInBackground(ArrayList<String>... params){
            int status = 0;
            String output = "";
            try{
                APIConnection connection = new APIConnection();
                output = connection.sendPost("/api/v1/reservation/cancel", params[0]);
                if (output.length() > 0){
                    object = new JSONObject(output);
                    String result = object.getString("status");
                    if(result.equals("success")){
                        status = 1;
                    }else{
                        status = 2;
                        JSONObject jsonArray = object.getJSONObject("error");
                        JSONArray errorArr=  jsonArray.getJSONArray("messages");
                        errorText = errorArr.getString(0);
                        Log.w("Submission error", errorText);
                    }
                }
                else{
                    status = 3;
                }
            }catch(Exception e) {
                status = 3;
            }
            return status;
        }
        @Override
        protected void onPostExecute(Integer result)
        {
            if(result == 1)
            {

                Snackbar.make(layout, "Cancellation Accepted!", Snackbar.LENGTH_LONG).show();
            }
            else if(result == 2)
            {
                Snackbar.make(layout, errorText, Snackbar.LENGTH_LONG).show();
            }
            else
            {
                Snackbar.make(layout, "Submission failed, technical error.", Snackbar.LENGTH_LONG).show();
            }
        }
    }
}
