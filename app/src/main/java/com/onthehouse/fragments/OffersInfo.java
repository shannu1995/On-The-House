package com.onthehouse.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    TextView offerVenue;
    //TextView offerShowHeading;
    TextView offerShowTimes;
    ConstraintLayout layout;
    //ConstraintLayout showsLayout;
    ArrayAdapter adapter;
    TextView shows;
    Spinner spinner;
    Button book_now;
    TextView showMore;
    ProgressButton book;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_offer_detail, container, false);
        final Context context = container.getContext();

        //book = new ProgressButton(context);

        boolean guest = getArguments().getBoolean("Guest");
        infoDetail(view, context);

        if (guest) {
            guestDetails(view, context);
        }

        return view;
    }

    public void guestDetails(View view, Context context) {
        book_now.setVisibility(View.GONE);
        spinner.setVisibility(View.GONE);
    }


    public void infoDetail(View view, Context context) {
        int pos = getArguments().getInt("position");

        offerTitle = view.findViewById(R.id.offer_title);
        offerFullPrice = view.findViewById(R.id.offer_fullPriceText);
        offerAdminFee = view.findViewById(R.id.offer_adminFee);
        offerMemberType = view.findViewById(R.id.offer_memberType);
        offerAbout = view.findViewById(R.id.offer_about);
        offerImage = view.findViewById(R.id.offer_image);
        shows = view.findViewById(R.id.shows);
        ratingBar = view.findViewById(R.id.rb_offer_details_rating);
        showMore = view.findViewById(R.id.show_more);
        //showsLayout = view.findViewById(R.id.show_details);
        offerVenue = view.findViewById(R.id.venue_details);
        //offerShowHeading = view.findViewById(R.id.shows);
        offerShowTimes = view.findViewById(R.id.show_details);
        spinner = view.findViewById(R.id.quantity_list);
        book_now = view.findViewById(R.id.book_button);

        showMore.setMaxLines(3);

        showMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showMore.getText().toString().equals(getString(R.string.show_more))) {
                    offerAbout.setMaxLines(Integer.MAX_VALUE);
                    showMore.setText(R.string.show_less);
                } else {
                    offerAbout.setMaxLines(3);
                    showMore.setText(R.string.show_more);
                }
            }
        });

        layout = (ConstraintLayout) view.findViewById(R.id.offer_info);

        final OfferDetail offerDet = OfferDetail.getInstance().get(pos);


        adapter = new ArrayAdapter<Integer>(view.getContext(),
                R.layout.spinner_item,
                offerDet.getQuantities());


        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        spinner.setPrompt("Tickets / Qty");

        offerTitle.setText(offerDet.getName());
        offerFullPrice.setText("FULL PRICE: "+offerDet.getFullPrice());
        offerAdminFee.setText(offerDet.getOurPriceHeading()+": "+offerDet.getOurPrice());
        offerMemberType.setText(offerDet.getMemberShipLevel());
        offerAbout.setText(offerDet.getDescription());
        ratingBar.setRating(offerDet.getRating());

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

                    offerShowTimes.setText(startDateStr + " to " + endDateStr);

                    ArrayList<Integer> quantities = new ArrayList<>();
                    JSONArray quantitiesJson = eachShow.getJSONArray("quantities");
                    spinner.setId(1000 + i);

                    for (int j = 0; j < quantitiesJson.length(); j++) {
                        quantities.add(UtilMethods.tryParseInt(quantitiesJson.getString(j)));
                    }

                    offerVenue.setText(offerDet.getVenueDetails());
                    if(offerDet.isCompt())
                        book_now.setText("Enter Competition");
                    book_now.setOnClickListener(new View.OnClickListener() {
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
                        }
                    });
                }
            }catch(Exception e){
                Log.w("Error JSONArray:",e);
            }



        String imageUrl = offerDet.getImageURL();
        if(imageUrl.isEmpty())
        {
            imageUrl = "http://vollrath.com/ClientCss/images/VollrathImages/No_Image_Available.jpg";
        }

        Glide.with(context).load(imageUrl).into(offerImage);

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
        });*/

    }

}
