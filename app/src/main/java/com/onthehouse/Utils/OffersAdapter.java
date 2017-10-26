package com.onthehouse.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.onthehouse.details.Member;
import com.onthehouse.details.Offers;
import com.onthehouse.fragments.MembershipFragment;
import com.onthehouse.fragments.OffersInfo;
import com.onthehouse.guest.GuestMain;
import com.onthehouse.onthehouse.MainMenu;
import com.onthehouse.onthehouse.OnTheMain;
import com.onthehouse.onthehouse.R;

import java.util.List;

import static android.content.ContentValues.TAG;

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.MyViewHolder> {
    private Context mContext;
    private List<Offers> offersList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;
        public ImageView offersInfo;
        public ImageView offersShare;
        public ImageView offersUpgrade;
        public TextView offersInfoText;
        public TextView offersShareText;
        public TextView offersUpgradeText;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            offersInfo = (ImageView) view.findViewById(R.id.offers_info);
            offersShare = (ImageView) view.findViewById(R.id.offers_share);
            offersUpgrade = (ImageView) view.findViewById(R.id.offers_upgrade);
            offersInfoText = (TextView) view.findViewById(R.id.offers_info_text);
            offersShareText = (TextView) view.findViewById(R.id.offers_share_text);
            offersUpgradeText = (TextView) view.findViewById(R.id.offers_upgrade_text);
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
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Offers offers = offersList.get(position);
        holder.title.setText(offers.getName());

        SharedPreferences sharedPreferences = mContext.getSharedPreferences("GuestMember", Context.MODE_PRIVATE);
        boolean guestMember = sharedPreferences.getBoolean("GuestCheck", false);

        if (!guestMember) {
            if (Member.getInstance().getMembership_level_id() == 3) {
                //Update text and button icon for Upgrade
                holder.offersUpgradeText.setText("Upgrade to Gold");
                holder.offersUpgrade.setImageResource(R.drawable.icon_upgrade_to_gold);
                //Update Listener
                holder.offersUpgrade.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.frame_container, new MembershipFragment())
                                .commit();
                        ((AppCompatActivity) mContext).getSupportActionBar().setSubtitle("My Membership");
                        ((MainMenu) mContext).setChecked(R.id.membership, false);
                    }
                });

                holder.offersUpgradeText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.frame_container, new MembershipFragment())
                                .commit();
                        ((AppCompatActivity) mContext).getSupportActionBar().setSubtitle("My Membership");
                        ((MainMenu) mContext).setChecked(R.id.membership, false);
                    }
                });
            } else {
                //Update text and button icon for Book Now
                holder.offersUpgradeText.setText("Book Now");
                holder.offersUpgrade.setImageResource(R.drawable.icon_book_now);
                //Update Listener
                holder.offersUpgrade.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Fragment fragment = null;
                        Bundle bundle = new Bundle();
                        bundle.putInt("position", position);

                        fragment = new OffersInfo();
                        fragment.setArguments(bundle);

                        if (fragment != null) {
                            FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.frame_container, fragment).addToBackStack("On the House").commit();

                        }
                        ((MainMenu) mContext).getSupportActionBar().setSubtitle("");
                    }
                });

                holder.offersUpgradeText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Fragment fragment = null;
                        Bundle bundle = new Bundle();
                        bundle.putInt("position", position);

                        fragment = new OffersInfo();
                        fragment.setArguments(bundle);

                        if (fragment != null) {
                            FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.frame_container, fragment).addToBackStack("On the House").commit();

                        }
                        ((MainMenu) mContext).getSupportActionBar().setSubtitle("");
                    }
                });

            }
        } else {
            Log.d(TAG, "onBindViewHolder: -----------------" + guestMember);
            holder.offersUpgradeText.setText("Login - Register");
            holder.offersUpgrade.setImageResource(R.drawable.icon_register);

            holder.offersUpgrade.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, OnTheMain.class);
                    mContext.startActivity(intent);
                    ((GuestMain) mContext).finish();

                }
            });

            holder.offersUpgradeText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, OnTheMain.class);
                    mContext.startActivity(intent);
                    ((GuestMain) mContext).finish();
                }
            });
        }

        // loading album cover using Glide library
        Glide.with(mContext).load(offers.getThumbnail()).into(holder.thumbnail);
        holder.offersInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchOfferDetailFragment(position);
            }
        });

        holder.offersInfoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchOfferDetailFragment(position);
            }
        });

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchOfferDetailFragment(position);
            }
        });

        holder.offersShareText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = "ma2.on-the-house.org/events/" + offers.getId();
                Intent share = new Intent(Intent.ACTION_SEND);
                share.putExtra(Intent.EXTRA_TEXT, message);
                share.setType("text/*");

                mContext.startActivity(Intent.createChooser(share, "Share this event"));
            }
        });

        holder.offersShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = "ma2.on-the-house.org/events/" + offers.getId();
                Intent share = new Intent(Intent.ACTION_SEND);
                share.putExtra(Intent.EXTRA_TEXT, message);
                share.setType("text/*");

                mContext.startActivity(Intent.createChooser(share, "Share this event"));
            }
        });

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchOfferDetailFragment(position);
            }
        });
    }

    public void launchOfferDetailFragment(int position) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("GuestMember", Context.MODE_PRIVATE);
        boolean guestMember = sharedPreferences.getBoolean("GuestCheck", false);

        Fragment fragment = null;
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);

        if (guestMember) {
            bundle.putBoolean("Guest", true);
        } else {
            bundle.putBoolean("Guest", false);
        }

        fragment = new OffersInfo();
        fragment.setArguments(bundle);


        if (fragment != null) {

            if (!guestMember) {
                FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container, fragment).addToBackStack("On the House").commit();
            } else {
                FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.guest_content, fragment).addToBackStack("On the House").commit();

            }

        }
    }


    @Override
    public int getItemCount() {
        return offersList.size();
    }
}
