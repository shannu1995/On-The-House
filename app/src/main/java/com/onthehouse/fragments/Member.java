package com.onthehouse.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onthehouse.onthehouse.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Member extends Fragment {

    public Member() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_member, container, false);
        final Context mContext = container.getContext();
        return view;
    }
}
