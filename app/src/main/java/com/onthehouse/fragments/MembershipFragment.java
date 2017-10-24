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

import com.onthehouse.connection.APIConnection;
import com.onthehouse.details.Member;
import com.onthehouse.onthehouse.R;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class MembershipFragment extends Fragment {

    private RadioGroup membershipLevel;
    private RadioButton gold_radio, bronze_radio;

    public MembershipFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_member, container, false);
        final Context mContext = container.getContext();

        membershipLevel = view.findViewById(R.id.rg_memberships);
        gold_radio = view.findViewById(R.id.radioButton_gold);
        bronze_radio = view.findViewById(R.id.radioButton_bronze);

        ArrayList<String> inputList = new ArrayList<>();
        inputList.add("&member_id=" + Member.getInstance().getId());
        new currentMembershipAsyncData().execute(inputList);

        return view;
    }

    public class currentMembershipAsyncData extends AsyncTask<ArrayList<String>, Void, Integer> {

        @Override
        protected Integer doInBackground(ArrayList<String>... params) {

            int status = 0;
            APIConnection connection = new APIConnection();
            try {
                String output = connection.sendPost("/api/v1/member/membership/history", params[0]);
                if (output.length() > 0) {
                    System.out.println(output);
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
