package com.onthehouse.Utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.onthehouse.details.Offers;
import com.onthehouse.onthehouse.R;

import java.util.List;

/**
 * Created by anashanifm on 7/9/17.
 */

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.MyViewHolder> {
    private Context mContext;
    private List<Offers> offersList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }

    public OffersAdapter(Context mContext, List<Offers> offersList) {
        this.mContext = mContext;
        this.offersList = offersList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.offers_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Offers offers = offersList.get(position);
        holder.title.setText(offers.getName());

        // loading album cover using Glide library
        Glide.with(mContext).load(offers.getThumbnail()).into(holder.thumbnail);
    }


    @Override
    public int getItemCount() {
        return offersList.size();
    }
}
