package com.onthehouse.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

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
    TextView shows;
    //Spinner spinner;
    //Button book_now;
    TextView showMore;
    ProgressButton book;
    LinearLayout showDetailsLayout;
    boolean admin_fee;
    boolean is_shipping;


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
        //book_now.setVisibility(View.GONE);
        //spinner.setVisibility(View.GONE);
    }


    public void infoDetail(View view, final Context context) {
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
        showDetailsLayout = view.findViewById(R.id.show_details);

        //showsLayout = view.findViewById(R.id.show_details);
        //offerVenue = view.findViewById(R.id.venue_details);
        //offerShowHeading = view.findViewById(R.id.shows);
        //offerShowTimes = view.findViewById(R.id.show_details);
        //spinner = view.findViewById(R.id.quantity_list);
        //book_now = view.findViewById(R.id.book_button);

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

        offerTitle.setText(offerDet.getName());
        offerFullPrice.setText("FULL PRICE: "+offerDet.getFullPrice());
        offerAdminFee.setText(offerDet.getOurPriceHeading()+": "+offerDet.getOurPrice());
        offerMemberType.setText(offerDet.getMemberShipLevel());
        offerAbout.setText(offerDet.getDescription());
        ratingBar.setRating(offerDet.getRating());

            try {
                JSONArray wrongShowsArray = offerDet.getWrongShowsArray();
                JSONArray showsArray = offerDet.getShowsArray();
                for(int i = 0; i < showsArray.length(); i++){
                    ArrayAdapter adapter;
                    JSONObject eachShow = showsArray.getJSONObject(i);
                    JSONObject venue = eachShow.getJSONObject("venue");
                    String venueName = venue.getString("name");
                    String streetName = venue.getString("address1");
                    String cityName = venue.getString("city");
                    String stateName = venue.getString("zone_name");
                    String zipName = venue.getString("zip");
                    String countryName = venue.getString("country_name");

                    String venueAddress = venueName + "\n" + streetName + "\n"
                            + cityName + ", " + stateName + "\n" + zipName + "\n"
                            + countryName;
                    JSONArray show_details_array = new JSONArray();
                    show_details_array = eachShow.getJSONArray("shows");

                    final JSONObject show_details_object = show_details_array.getJSONObject(0);
                    String is_admin_fee = show_details_object.getString("is_admin_fee");
                    if(is_admin_fee.equals("1")){
                        admin_fee = true;
                    }else {
                        admin_fee = false;
                    }
                    long startDateL = UtilMethods.tryParseInt(show_details_object.getString("show_date"));
                    Date startDate = new Date(startDateL * 1000);
                    long endDateL = UtilMethods.tryParseInt(show_details_object.getString("show_date2"));
                    Date endDate = new Date(endDateL * 1000);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    dateFormat.setTimeZone(TimeZone.getTimeZone("AEST"));
                    String startDateStr = dateFormat.format(startDate);
                    String endDateStr = dateFormat.format(endDate);

                    TextView showsHeading = new TextView(context);
                    showsHeading.setText("Shows");
                    showsHeading.setTextColor(Color.BLACK);
                    showsHeading.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
                    showDetailsLayout.addView(showsHeading);

                    TextView offerShowTimes = new TextView(context);
                    offerShowTimes.setText("From " +startDateStr + " to " + endDateStr);
                    offerShowTimes.setTextColor(Color.BLACK);
                    showDetailsLayout.addView(offerShowTimes);

                    TextView venueHeading = new TextView(context);
                    venueHeading.setText("Venue");
                    venueHeading.setTextColor(Color.BLACK);
                    venueHeading.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
                    showDetailsLayout.addView(venueHeading);

                    TextView venueDetails = new TextView(context);
                    venueDetails.setText(venueAddress);
                    venueDetails.setTextColor(Color.BLACK);
                    showDetailsLayout.addView(venueDetails);

                    LinearLayout booking_layout = new LinearLayout(context);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
                    booking_layout.setOrientation(LinearLayout.HORIZONTAL);;
                    final Button book_now = new Button(context);
                    book_now.setText("Book Now");
                    book_now.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    book_now.setTextColor(Color.WHITE);
                    book_now.setBackground(getResources().getDrawable(R.drawable.rectangle_button));
                    book_now.setId(UtilMethods.tryParseInt(show_details_object.getString("id")));
                    book_now.setLayoutParams(layoutParams);
                    booking_layout.addView(book_now);
                    booking_layout.setLayoutParams(layoutParams);


                    final Spinner spinner = new Spinner(context);
                    spinner.setBackground(getResources().getDrawable(R.drawable.gradient_spinner2));
                    spinner.setId(book_now.getId());
                    spinner.setTag(book_now.getId());
                    int spinnerHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 49, getResources().getDisplayMetrics());
                    LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            spinnerHeight, 1.0f);
                    final ArrayList<Integer> quantities = new ArrayList<Integer>();
                    JSONArray quantitiesArray = show_details_object.getJSONArray("quantities");
                    if(quantitiesArray != null) {
                        for (int j = 0; j < quantitiesArray.length(); j++) {
                            quantities.add(quantitiesArray.getInt(j));
                            Log.w("qty: ", Integer.toString(quantitiesArray.getInt(j)));
                        }
                    }
                    adapter = new ArrayAdapter<Integer>(view.getContext(),
                            R.layout.spinner_item,
                            quantities);
                    spinner.setAdapter(adapter);
                    spinner.setSelection(0);
                    spinner.setPrompt("Tickets / Qty");
                    booking_layout.addView(spinner);
                    showDetailsLayout.addView(booking_layout, layoutParams);
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
                                Spinner selected_spinner = (Spinner)((LinearLayout)book_now.getParent()).getChildAt(1);
                                int tickets = (int)selected_spinner.getSelectedItem();
                                Intent buyIntent;
                                Bundle purchase_details = new Bundle();
                                purchase_details.putString("tickets", Integer.toString(tickets));
                                purchase_details.putString("show_id", Integer.toString(book_now.getId()));
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
    }

}
