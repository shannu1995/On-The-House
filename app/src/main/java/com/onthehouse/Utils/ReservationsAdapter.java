package com.onthehouse.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.onthehouse.details.Reservation;
import com.onthehouse.onthehouse.R;

import java.util.ArrayList;

public class ReservationsAdapter extends RecyclerView.Adapter<ReservationsAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Reservation> reservationList;

    public ReservationsAdapter(Context context, ArrayList<Reservation> reservationList) {
        this.context = context;
        this.reservationList = reservationList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.current_reservations_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Reservation reservation = reservationList.get(position);
        holder.event_name.setText(reservation.getEvent_name());
        holder.event_date.setText(reservation.getDate());
        holder.event_tickets.setText(String.valueOf(reservation.getNum_tickets()));
        holder.event_venue.setText(reservation.getVenue_name());

        holder.layout.setHapticFeedbackEnabled(true);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reservationDetails(context, reservation);
            }
        });


    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    public void reservationDetails(Context context, Reservation reservation) {
        String message = "- Venue: " + reservation.getVenue_name() +
                "\n- Tickets/Qty: " + reservation.getNum_tickets() +
                "\n- Date: " + reservation.getDate();
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(reservation.getEvent_name());
        alertDialog.setMessage(message);
        if (reservation.isCan_rate()) {
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Rate",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        }
        if (reservation.isCan_cancel()) {
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel Reservation",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        }
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "More Info",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView event_name;
        public TextView event_date;
        public TextView event_tickets;
        public TextView event_venue;
        public ConstraintLayout layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            event_name = itemView.findViewById(R.id.tv_event_name);
            event_date = itemView.findViewById(R.id.tv_event_date);
            event_tickets = itemView.findViewById(R.id.tv_event_tickets);
            event_venue = itemView.findViewById(R.id.tv_event_venue);
            layout = itemView.findViewById(R.id.cl_reservations);
        }
    }
}
