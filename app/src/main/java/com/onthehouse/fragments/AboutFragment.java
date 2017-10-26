package com.onthehouse.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.onthehouse.onthehouse.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {


    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        final Context context = container.getContext();

        TextView faq = view.findViewById(R.id.tv_faq);
        TextView privacyPolicy = view.findViewById(R.id.tv_privacy_policy);
        TextView terms = view.findViewById(R.id.tv_terms);

        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://ma2.on-the-house.org/faq";
                openLinkDialog(context, url);

            }
        });
        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://ma2.on-the-house.org/privacy-policy";
                openLinkDialog(context, url);
            }
        });
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://ma2.on-the-house.org/terms-and-conditions";
                openLinkDialog(context, url);
            }
        });
        return view;
    }

    public void openLinkDialog(Context context, final String url) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Open Link in Browser");
        alertDialog.setIcon(R.drawable.icon_browser);
        alertDialog.setMessage("Continue to open link in browser?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Proceed", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
                dialog.dismiss();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

}
