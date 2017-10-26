package com.onthehouse.Utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.onthehouse.details.Membership;
import com.onthehouse.onthehouse.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyMembershipsAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Membership> membershipArrayList;

    public MyMembershipsAdapter(Context context, ArrayList<Membership> membershipArrayList) {
        this.context = context;
        this.membershipArrayList = membershipArrayList;
    }

    @Override
    public int getCount() {
        return membershipArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return membershipArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.my_memberships_row, null);

        Membership membership = membershipArrayList.get(position);

        TextView level = v.findViewById(R.id.tv_level);
        TextView period = v.findViewById(R.id.tv_period);
        TextView price = v.findViewById(R.id.tv_mem_price);

        level.setText(membership.getMembership_level_name());

        Date start = new Date(membership.getDate_created() * 1000);
        Date end = new Date(membership.getDate_expires() * 1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        period.setText(dateFormat.format(start) + " - " + dateFormat.format(end));

        price.setText("$" + membership.getPrice());


        return v;
    }
}
