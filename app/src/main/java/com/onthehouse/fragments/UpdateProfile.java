package com.onthehouse.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onthehouse.onthehouse.R;

import java.util.ArrayList;

/**
 * Created by ammar on 15/10/2017.
 */

public class UpdateProfile extends Fragment{

    public UpdateProfile(){};


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_register, container, false);
        Context context = container.getContext();


    class getMemberData extends AsyncTask<ArrayList<String>, Void, Integer> {

        @Override
        protected Integer doInBackground(ArrayList<String>... arrayLists) {
            return null;
        }
    }
}
