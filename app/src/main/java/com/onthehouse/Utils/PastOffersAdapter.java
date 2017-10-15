package com.onthehouse.Utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.onthehouse.details.PastOffers;
import com.onthehouse.onthehouse.R;

import java.util.ArrayList;

public class PastOffersAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<PastOffers> pastOffersArrayList;

    public PastOffersAdapter(Context context, ArrayList<PastOffers> pastOffersArrayList) {
        this.context = context;
        this.pastOffersArrayList = pastOffersArrayList;
    }

    @Override
    public int getCount() {
        return pastOffersArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return pastOffersArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.past_offers_row, null);
        TextView offerTitle = (TextView) v.findViewById(R.id.tv_past_offer_title);
        TextView offerDescription = (TextView) v.findViewById(R.id.tv_past_offer_desc);
        RatingBar ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);
        ratingBar.setMax(5);

        offerTitle.setText(pastOffersArrayList.get(i).getName());
        offerDescription.setText(pastOffersArrayList.get(i).getDescription());
        ratingBar.setRating(pastOffersArrayList.get(i).getRating());

        return v;
    }
}
