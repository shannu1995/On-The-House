package com.onthehouse.newUI;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.onthehouse.onthehouse.R;

import java.util.ArrayList;

public class OfferListAdapter extends RecyclerView.Adapter<OfferListAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Events> events;

    public OfferListAdapter(Context context, ArrayList<Events> events) {
        this.context = context;
        this.events = events;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.new_offer_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.title.setText(events.get(position).getName());

        holder.ratingBar.setRating(events.get(position).getRating());
        holder.desc.setText(events.get(position).getDescription());

        int colorRes = 0;
        switch (position % 6) {
            case 0:
                colorRes = R.color.tile_1;
                break;
            case 1:
                colorRes = R.color.tile_2;
                break;
            case 2:
                colorRes = R.color.tile_3;
                break;
            case 3:
                colorRes = R.color.tile_4;
                break;
            case 4:
                colorRes = R.color.tile_5;
                break;
            case 5:
                colorRes = R.color.tile_6;
                break;
            default:
                colorRes = R.color.tile_1;
                break;
        }

        holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, colorRes));

        Glide.with(context).load(events.get(position).getImage_url()).into(holder.thumbnail);

        holder.desc.setVisibility(View.GONE);

        holder.viewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.desc.getVisibility() == View.GONE) {
                    holder.desc.setVisibility(View.VISIBLE);
                    holder.viewMore.setImageResource(R.drawable.icon_shrink);
                } else {
                    holder.desc.setVisibility(View.GONE);
                    holder.viewMore.setImageResource(R.drawable.icon_expand);
                }
            }
        });

        holder.moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EventDetails.class);
                intent.putExtra("event_id", events.get(position).getId());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private ImageView thumbnail;
        private TextView title;
        private TextView desc;
        private RatingBar ratingBar;
        private Button moreInfo;
        private Button action;
        private ImageButton share;
        private ImageButton viewMore;

        public MyViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cv_offer_new);
            thumbnail = itemView.findViewById(R.id.iv_offer_new);
            title = itemView.findViewById(R.id.tv_offer_title_new);
            desc = itemView.findViewById(R.id.tv_offer_desc_new);
            ratingBar = itemView.findViewById(R.id.rb_offer_new);
            moreInfo = itemView.findViewById(R.id.btn_more_new);
            action = itemView.findViewById(R.id.btn_updrage_new);
            share = itemView.findViewById(R.id.ib_share_new);
            viewMore = itemView.findViewById(R.id.ib_more);
        }
    }
}
