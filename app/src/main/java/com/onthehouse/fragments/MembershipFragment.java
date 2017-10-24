package com.onthehouse.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.onthehouse.Utils.DrawerLocker;
import com.onthehouse.connection.APIConnection;
import com.onthehouse.details.Member;
import com.onthehouse.details.Membership;
import com.onthehouse.onthehouse.R;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MembershipFragment extends Fragment {

    private static final String TAG = "MembershipFragment";
    private RadioGroup membershipLevel;
    private RadioButton gold_radio, bronze_radio;
    private Membership currentMembership;
    private ArrayList<Membership> membershipArrayList;

    public MembershipFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_membership, container, false);
        final Context mContext = container.getContext();

        membershipArrayList = new ArrayList<>();
        membershipLevel = view.findViewById(R.id.rg_memberships);
        gold_radio = view.findViewById(R.id.radioButton_gold);
        bronze_radio = view.findViewById(R.id.radioButton_bronze);
        currentMembership = new Membership();

        ArrayList<String> inputList = new ArrayList<>();
        inputList.add("&member_id=" + Member.getInstance().getId());
        new currentMembershipAsyncData().execute(inputList);

        return view;
    }

    public class pastMembershipAsynData extends AsyncTask<ArrayList<String>, Void, Integer> {

        @Override
        protected void onPreExecute() {
            //Lock Drawer While Loading
            ((DrawerLocker) getActivity()).setDrawerEnabled(false);
        }

        @Override
        protected Integer doInBackground(ArrayList<String>... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            //Unlock Drawer While Loading
            ((DrawerLocker) getActivity()).setDrawerEnabled(true);
        }
    }

    public class currentMembershipAsyncData extends AsyncTask<ArrayList<String>, Void, Integer> {

        @Override
        protected void onPreExecute() {
            //Lock Drawer While Loading
            ((DrawerLocker) getActivity()).setDrawerEnabled(false);
        }

        @Override
        protected Integer doInBackground(ArrayList<String>... params) {

            int status = 0;
            APIConnection connection = new APIConnection();
            try {
                String output = connection.sendPost("/api/v1/member/membership", params[0]);
                if (output.length() > 0) {
                    JSONObject obj = new JSONObject(output);
                    String result = obj.getString("status");

                    if (result.equals("success")) {
                        try {
                            JSONObject jsonArray = obj.getJSONObject("membership");
                            currentMembership.setId(jsonArray.getInt("id"));
                            currentMembership.setMembership_level_id(jsonArray.getInt("membership_level_id"));
                            currentMembership.setMember_id(jsonArray.getInt("member_id"));
                            currentMembership.setDate_expires(jsonArray.getLong("date_expires"));
                            currentMembership.setPrice(jsonArray.getDouble("price"));
                            currentMembership.setPaypayl_data(jsonArray.getString("paypal_data"));
                            currentMembership.setMembership_level_name(jsonArray.getString("membership_level_name"));
                            currentMembership.setDate_created(jsonArray.getLong("date_created"));

                            status = 1;

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.w("Error", e.getMessage());
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
            System.out.println(currentMembership.toString());
            try {
                if (currentMembership.getMembership_level_name().equalsIgnoreCase("gold")) {
                    gold_radio.setChecked(true);
                } else {
                    bronze_radio.setChecked(true);
                }
            } catch (NullPointerException e) {
                //API returns null if no membership changes have been made ever
                bronze_radio.setChecked(true);
                //Set Manually for comparison
                currentMembership.setMembership_level_name("Bronze");
                currentMembership.setMembership_level_id(3);
            }
            //Unlock Drawer While Loading
            ((DrawerLocker) getActivity()).setDrawerEnabled(true);
        }
    }
}
