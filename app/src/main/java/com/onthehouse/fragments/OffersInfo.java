package com.onthehouse.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.onthehouse.details.Offers;
import com.onthehouse.onthehouse.R;

/**
 * Created by anashanifm on 9/9/17.
 */

public class OffersInfo extends Fragment {
    public OffersInfo() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_offers_list, container, false);
        Context mContext = container.getContext();

        int pos = getArguments().getInt("position");
        Toast.makeText(mContext, Integer.toString(pos), Toast.LENGTH_LONG).show();

        return view;
    }
}
