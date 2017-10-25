package com.onthehouse.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.onthehouse.details.OfferDetail;
import com.onthehouse.details.UtilMethods;
import com.onthehouse.onthehouse.BookingPageCompetition;
import com.onthehouse.onthehouse.BookingPageDeliveryPurchase;
import com.onthehouse.onthehouse.BookingPageNoDeliveryPurchase;
import com.onthehouse.onthehouse.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.xm.weidongjian.progressbuttonlib.ProgressButton;

public class OffersInfo extends Fragment {
    public OffersInfo() {

    }

    TextView offerTitle;
    TextView offerFullPrice;
    TextView offerAdminFee;
    TextView offerMemberType;
    TextView offerAbout;
    ImageView offerImage;
    RatingBar ratingBar;
    //TextView offerVenue;
    //TextView offerShowHeading;
    //TextView offerShowTimes;
    ConstraintLayout layout;
    //ConstraintLayout showsLayout;
    ArrayAdapter adapter;
    TextView shows;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_offer_detail, container, false);
        final Context context = container.getContext();

        int pos = getArguments().getInt("position");
        Context mContext = container.getContext();


        offerTitle = view.findViewById(R.id.offer_title);
        offerFullPrice = view.findViewById(R.id.offer_fullPriceText);
        offerAdminFee = view.findViewById(R.id.offer_adminFee);
        offerMemberType = view.findViewById(R.id.offer_memberType);
        offerAbout = view.findViewById(R.id.offer_about);
        offerImage = view.findViewById(R.id.offer_image);
        shows = view.findViewById(R.id.shows);
        ratingBar = view.findViewById(R.id.rb_offer_details_rating);
        //showsLayout = view.findViewById(R.id.show_details);
        //offerVenue = view.findViewById(R.id.venue_details);
        //offerShowHeading = view.findViewById(R.id.shows);
        //offerShowTimes = view.findViewById(R.id.show_details);
        //spinner = view.findViewById(R.id.quantity_list);
        //button = view.findViewById(R.id.book_button);


        layout = (ConstraintLayout) view.findViewById(R.id.offer_info);

        final OfferDetail offerDet = OfferDetail.getInstance().get(pos);
        System.out.println(offerDet.toString());
        /*
        adapter = new ArrayAdapter<Integer>(view.getContext(),
                android.R.layout.simple_expandable_list_item_1,
                offerDet.getQuantities());
                */

        //spinner.setAdapter(adapter);
        //spinner.setSelection(0);

        offerTitle.setText(offerDet.getName());
        offerFullPrice.setText("FULL PRICE: "+offerDet.getFullPrice());
        offerAdminFee.setText(offerDet.getOurPriceHeading()+": "+offerDet.getOurPrice());
        offerMemberType.setText(offerDet.getMemberShipLevel());
        offerAbout.setText(offerDet.getDescription());
        ratingBar.setRating(offerDet.getRating());
        //offerVenue.setText(offerDet.getVenueDetails());
        //offerShowHeading.setText(offerDet.getShowsHeading());

        ConstraintSet constraintSet = new ConstraintSet();
        if(offerDet.isCompt()){
            ProgressButton book = new ProgressButton(context);
            book.setBackground(getResources().getDrawable(R.drawable.selector_button));
            book.setText("Book Now");
            book.setTextColor (getResources().getColor(R.color.white));
            book.setTypeface(null, Typeface.BOLD);
            book.setId(offerDet.getOfferId());
            book.setText("Enter Competition");
            book.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT));
            layout.addView(book);
            constraintSet.clone(layout);
            constraintSet.connect(book.getId(), ConstraintSet.TOP, shows.getId(), ConstraintSet.BOTTOM, 16);
            constraintSet.connect(book.getId(), ConstraintSet.LEFT, shows.getId(), ConstraintSet.LEFT, 0);
            constraintSet.applyTo(layout);
            book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent competeIntent = new Intent(getActivity(), BookingPageCompetition.class);
                    Bundle competeDetails = new Bundle();
                    competeDetails.putString("eventID", Integer.toString(offerDet.getOfferId()));
                    competeDetails.putString("question", offerDet.getQuestion());
                    competeIntent.putExtras(competeDetails);
                    startActivity(competeIntent);
                }
            });
        }else{
            try {
                JSONArray showsArray = offerDet.getShowsArray();
                //JSONArray showsArray = new JSONArray(offerDet.getShowsArray());
                for(int i = 0; i < showsArray.length(); i++){
                        JSONObject eachShow = showsArray.getJSONObject(i);
                        long startDateL = UtilMethods.tryParseInt(eachShow.getString("show_date"));
                        Date startDate = new Date(startDateL * 1000);
                        long endDateL = UtilMethods.tryParseInt(eachShow.getString("show_date2"));
                        Date endDate = new Date(endDateL * 1000);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        String startDateStr = dateFormat.format(startDate);
                        String endDateStr = dateFormat.format(endDate);


                        TextView dates = new TextView(context);
                        dates.setText(startDateStr+" to "+endDateStr);
                        Log.w("Date is", dates.getText().toString());
                        dates.setId(100 + i);
                        layout.addView(dates);
                        constraintSet.clone(layout);
                        if(i == 0){
                            constraintSet.connect(dates.getId(), ConstraintSet.TOP, shows.getId(), ConstraintSet.BOTTOM, 16);
                        }else{
                            constraintSet.connect(dates.getId(), ConstraintSet.TOP, 500 + (i - 1), ConstraintSet.BOTTOM, 16);
                        }
                        constraintSet.connect(dates.getId(), ConstraintSet.TOP, shows.getId(), ConstraintSet.BOTTOM, 16);
                        constraintSet.connect(dates.getId(), ConstraintSet.LEFT, shows.getId(), ConstraintSet.LEFT, 0);
                        constraintSet.applyTo(layout);

                        ProgressButton book = new ProgressButton(context);
                        book.setBackground(getResources().getDrawable(R.drawable.selector_button));
                        book.setText("Book Now");
                        book.setTextColor (getResources().getColor(R.color.white));
                        book.setTypeface(null, Typeface.BOLD);
                        book.setId(UtilMethods.tryParseInt(eachShow.getString("id")));


                        final float scale = getResources().getDisplayMetrics().density;
                        int dpWidthInPx  = (int) (120 * scale);
                        int dpHeightInPx = (int) (40 * scale);
                        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(dpWidthInPx, dpHeightInPx);
                        book.setLayoutParams(layoutParams);
                        layout.addView(book);
                        constraintSet.clone(layout);
                        constraintSet.connect(book.getId(), ConstraintSet.TOP, dates.getId(), ConstraintSet.BOTTOM, 16);
                        constraintSet.connect(book.getId(), ConstraintSet.LEFT, dates.getId(), ConstraintSet.LEFT, 0);
                        constraintSet.applyTo(layout);

                        TextView tickets = new TextView(context);
                        tickets.setText("No. of tickets: ");
                        tickets.setTypeface(null, Typeface.ITALIC);
                        tickets.setId(500 + i);
                        layout.addView(tickets);
                        constraintSet.clone(layout);
                        constraintSet.connect(tickets.getId(), ConstraintSet.TOP, dates.getId(), ConstraintSet.BOTTOM, 16);
                        constraintSet.connect(tickets.getId(), ConstraintSet.LEFT, book.getId(), ConstraintSet.RIGHT, 16);
                        constraintSet.applyTo(layout);

                        final Spinner spinner = new Spinner(context);
                    spinner.setBackground(getResources().getDrawable(R.drawable.spinner_edit_profile));

                    spinner.setPopupBackgroundDrawable(getResources().getDrawable(R.drawable.spinner_edit_profile));
                        ArrayList<Integer> quantities = new ArrayList<>();
                        JSONArray quantitiesJson = eachShow.getJSONArray("quantities");
                        spinner.setId(1000 + i);
                        spinner.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT));
                        for(int j = 0; j < quantitiesJson.length(); j++){
                            quantities.add(UtilMethods.tryParseInt(quantitiesJson.getString(j)));
                        }
                        /*
                        adapter = new ArrayAdapter<Integer>(view.getContext(),
                            android.R.layout.simple_expandable_list_item_1,
                            quantities);
                            */
                        adapter = new ArrayAdapter<Integer>(view.getContext(),
                            android.R.layout.simple_spinner_item,
                            quantities);
                        spinner.setPadding(0, 0, 0, 0);
                        spinner.setAdapter(adapter);
                        spinner.setSelection(0);
                        spinner.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT));
                        layout.addView(spinner);
                        constraintSet.clone(layout);
                        constraintSet.connect(spinner.getId(), ConstraintSet.TOP, dates.getId(), ConstraintSet.BOTTOM, 16);
                        constraintSet.connect(spinner.getId(), ConstraintSet.LEFT, tickets.getId(), ConstraintSet.RIGHT, 16);
                        constraintSet.applyTo(layout);

                        TextView venueHeading = new TextView(context);
                        venueHeading.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                        venueHeading.setText("Venue");
                        venueHeading.setId(1500 + i);
                        venueHeading.setTextColor(getResources().getColor(R.color.textColor));
                        layout.addView(venueHeading);
                        constraintSet.clone(layout);
                        constraintSet.connect(venueHeading.getId(), ConstraintSet.TOP, book.getId(), ConstraintSet.BOTTOM, 16);
                        constraintSet.connect(venueHeading.getId(), ConstraintSet.LEFT, book.getId(), ConstraintSet.LEFT, 0);
                        constraintSet.applyTo(layout);

                        TextView venueDetails = new TextView(context);

                        venueDetails.setId(i);
                        venueDetails.setText(offerDet.getVenueDetails());

                        layout.addView(venueDetails);
                        constraintSet.clone(layout);
                        constraintSet.connect(venueDetails.getId(), ConstraintSet.TOP, venueHeading.getId(), ConstraintSet.BOTTOM, 16);
                        constraintSet.connect(venueDetails.getId(), ConstraintSet.LEFT, book.getId(), ConstraintSet.LEFT, 0);
                        constraintSet.applyTo(layout);
                        book.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            int tickets = (int)spinner.getSelectedItem();
                            Intent buyIntent;
                            Bundle purchase_details = new Bundle();
                            purchase_details.putString("tickets", Integer.toString(tickets));
                            purchase_details.putString("show_id", offerDet.getShow_id());
                            if(offerDet.isDelivery()){
                                buyIntent = new Intent(getActivity(), BookingPageDeliveryPurchase.class);
                            }
                            else {
                                buyIntent = new Intent(getActivity(), BookingPageNoDeliveryPurchase.class);
                            }
                            buyIntent.putExtras(purchase_details);
                            startActivity(buyIntent);
                        }
                    });
                }
            }catch(Exception e){
                Log.w("Error JSONArray:",e);
            }
            /*
            for(int i = 0; i < offerDet.getDates().size(); i++){
                offerShowTimes.setText(offerDet.getDates().get(i) + "\n");
                //offerShowTimes.append(offerDet.getOurPrice().toString());
            }
            */
        }

        String imageUrl = offerDet.getImageURL();
        if(imageUrl.isEmpty())
        {
            imageUrl = "http://vollrath.com/ClientCss/images/VollrathImages/No_Image_Available.jpg";
        }

        Glide.with(mContext).load(imageUrl).into(offerImage);

        /*
        offerShowTimes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(offerDet.isCompt()){
                    Intent competeIntent = new Intent(getActivity(), BookingPageCompetition.class);

                    Bundle competeDetails = new Bundle();
                    competeDetails.putString("eventID", Integer.toString(offerDet.getOfferId()));
                    competeDetails.putString("question", offerDet.getQuestion());
                    competeIntent.putExtras(competeDetails);
                    startActivity(competeIntent);
                }else{
                    int tickets = (int)spinner.getSelectedItem();
                    Bundle purchase_details = new Bundle();
                    purchase_details.putString("tickets", Integer.toString(tickets));
                    purchase_details.putString("show_id", offerDet.getShow_id());
                    Intent buyIntent;
                    Log.w("Printing Show ID:", Integer.toString(tickets));
                    if(offerDet.isDelivery()){
                        buyIntent = new Intent(getActivity(), BookingPageDeliveryPurchase.class);
                    }
                    else {
                        buyIntent = new Intent(getActivity(), BookingPageNoDeliveryPurchase.class);
                    }
                    buyIntent.putExtras(purchase_details);
                    startActivity(buyIntent);
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int tickets = (int)spinner.getSelectedItem();
                Intent buyIntent;
                Bundle purchase_details = new Bundle();
                purchase_details.putString("tickets", Integer.toString(tickets));
                purchase_details.putString("show_id", offerDet.getShow_id());
                    if(offerDet.isDelivery()){
                        buyIntent = new Intent(getActivity(), BookingPageDeliveryPurchase.class);
                    }
                    else {
                        buyIntent = new Intent(getActivity(), BookingPageNoDeliveryPurchase.class);
                    }
                buyIntent.putExtras(purchase_details);
                startActivity(buyIntent);
            }
        });
        */
        return view;
    }


}
