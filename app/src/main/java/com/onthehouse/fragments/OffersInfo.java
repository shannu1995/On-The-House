package com.onthehouse.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.onthehouse.details.OfferDetail;
import com.onthehouse.onthehouse.BookingPageCompetition;
import com.onthehouse.onthehouse.BookingPageDeliveryPurchase;
import com.onthehouse.onthehouse.BookingPageNoDeliveryPurchase;
import com.onthehouse.onthehouse.R;

/**
 * Created by anashanifm on 9/9/17.
 */

public class OffersInfo extends Fragment {
    public OffersInfo() {

    }

    TextView offerTitle;
    TextView offerFullPrice;
    TextView offerAdminFee;
    TextView offerMemberType;
    TextView offerAbout;
    ImageView offerImage;
    TextView offerVenue;
    TextView offerShowHeading;
    TextView offerShowTimes;
    ConstraintLayout layout;
    ArrayAdapter adapter;
    Spinner spinner;
    Button button;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.activity_offer_detail, container, false);

        int pos = getArguments().getInt("position");
        Context mContext = container.getContext();


        offerTitle = view.findViewById(R.id.offer_title);
        offerFullPrice = view.findViewById(R.id.offer_fullPriceText);
        offerAdminFee = view.findViewById(R.id.offer_adminFee);
        offerMemberType = view.findViewById(R.id.offer_memberType);
        offerAbout = view.findViewById(R.id.offer_about);
        offerImage = view.findViewById(R.id.offer_image);
        offerVenue = view.findViewById(R.id.venue_details);
        offerShowHeading = view.findViewById(R.id.shows);
        offerShowTimes = view.findViewById(R.id.show_details);
        spinner = view.findViewById(R.id.quantity_list);
        button = view.findViewById(R.id.book_button);



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
        offerVenue.setText(offerDet.getVenueDetails());
        offerShowHeading.setText(offerDet.getShowsHeading());
        if(offerDet.isCompt()){
            offerShowTimes.setText("Enter Competition");
        }else{
            for(int i = 0; i < offerDet.getDates().size(); i++){
                offerShowTimes.setText(offerDet.getDates().get(i) + "\n");
                //offerShowTimes.append(offerDet.getOurPrice().toString());
            }
        }


        String imageUrl = offerDet.getImageURL();
        if(imageUrl.isEmpty())
        {
            imageUrl = "http://vollrath.com/ClientCss/images/VollrathImages/No_Image_Available.jpg";
        }

        Glide.with(mContext).load(imageUrl).into(offerImage);


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


        return view;
    }


}
