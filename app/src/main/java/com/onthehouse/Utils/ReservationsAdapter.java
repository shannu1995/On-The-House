package com.onthehouse.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.onthehouse.connection.APIConnection;
import com.onthehouse.details.Member;
import com.onthehouse.details.Reservation;
import com.onthehouse.onthehouse.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

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
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Reservation reservation = reservationList.get(position);
        holder.event_name.setText(reservation.getEvent_name());
        holder.event_date.setText(reservation.getDate());
        holder.event_tickets.setText(String.valueOf(reservation.getNum_tickets()));
        holder.event_venue.setText(reservation.getVenue_name());
        if (reservation.isCan_rate()) {
            holder.rate_event.setText(R.string.please_rate_event);
        } else {
            holder.rate_event.setVisibility(View.GONE);
        }
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

    public void reservationDetails(final Context context, final Reservation reservation) {
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
                            ratingDialog(context, reservation);
                        }
                    });
        }
        if (reservation.isCan_cancel()) {
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel Reservation",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            confirmCancelDialog(context, reservation);
                            dialog.dismiss();
                        }
                    });
        }
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "More Info",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        alertDialog.show();
    }

    public void ratingDialog(final Context context, final Reservation reservation) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setPositiveButton("Submit", null)
                .create();

        View ratingView = View.inflate(context, R.layout.dialog_rate, null);

        alertDialog.setView(ratingView);

        final RatingBar ratingBar = ratingView.findViewById(R.id.rb_rate_event);
        final TextView comments = ratingView.findViewById(R.id.et_provide_comments);

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ratingBar.getRating() == 0.0) {
                            Toast.makeText(context, "Please provide a rating", Toast.LENGTH_LONG).show();
                            alertDialog.show();
                        } else {
                            ArrayList<String> ratingInput = new ArrayList<>();
                            ratingInput.add("&event_id=" + reservation.getEvent_id());
                            ratingInput.add("&member_id=" + Member.getInstance().getId());
                            ratingInput.add("&rating=" + (int) ratingBar.getRating());
                            if (!comments.getText().toString().trim().equalsIgnoreCase("")) {
                                ratingInput.add("&comments=" + comments.getText().toString());
                            }
                            new rateEventAsyncData(context).execute(ratingInput);
                            dialog.dismiss();
                            reservation.setCan_rate(false);
                            reservation.setHas_rated(true);
                            notifyDataSetChanged();
                        }
                    }
                });
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    public void confirmCancelDialog(final Context context, final Reservation reservation) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Cancel Reservation");
        alertDialog.setMessage("Are you sure you want to cancel this reservation?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Cancel Reservation", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ArrayList<String> inputList = new ArrayList<>();
                inputList.add("&reservation_id=" + reservation.getId());
                inputList.add("&member_id=" + Member.getInstance().getId());
                new cancelReservationAsyncData(context).execute(inputList);
                reservationList.remove(reservation);
                notifyDataSetChanged();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Dismiss", new DialogInterface.OnClickListener() {
            @Override
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
        public TextView rate_event;
        public ConstraintLayout layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            event_name = itemView.findViewById(R.id.tv_event_name);
            event_date = itemView.findViewById(R.id.tv_event_date);
            event_tickets = itemView.findViewById(R.id.tv_event_tickets);
            event_venue = itemView.findViewById(R.id.tv_event_venue);
            rate_event = itemView.findViewById(R.id.tv_rate_event);
            layout = itemView.findViewById(R.id.cl_reservations);
        }
    }

    public class rateEventAsyncData extends AsyncTask<ArrayList<String>, Void, Integer> {
        Context context;
        String errorText;
        ProgressDialog progressDialog;

        public rateEventAsyncData(Context context) {
            this.context = context;
        }

        @Override
        protected Integer doInBackground(ArrayList<String>... params) {
            int status;
            APIConnection connection = new APIConnection();
            try {
                String output = connection.sendPost("/api/v1/event/rate", params[0]);
                if (output.length() > 0) {
                    JSONObject obj = new JSONObject(output);
                    String result = obj.getString("status");
                    if (result.equals("success")) {
                        status = 1;
                    } else {
                        status = 2;
                        JSONObject jsonArray = obj.getJSONObject("error");
                        JSONArray errorArr = jsonArray.getJSONArray("messages");
                        errorText = errorArr.getString(0);
                    }
                } else {
                    status = 3;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "doInBackground: " + e.getMessage());
                status = 3;
            }
            return status;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result == 1) {
                Toast.makeText(context, "Rating submitted, thank you for your feedback", Toast.LENGTH_LONG).show();
            } else if (result == 2) {
                Toast.makeText(context, errorText, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Submission failed, technical error.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class cancelReservationAsyncData extends AsyncTask<ArrayList<String>, Void, Integer> {
        Context context;
        JSONObject object = new JSONObject();
        String errorText;
        ProgressDialog progressDialog;

        public cancelReservationAsyncData(Context context) {
            this.context = context;
        }

        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Cancelling Reservation");
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        @Override
        protected Integer doInBackground(ArrayList<String>... params) {
            int status;
            String output;
            try {
                APIConnection connection = new APIConnection();
                output = connection.sendPost("/api/v1/reservation/cancel", params[0]);
                if (output.length() > 0) {
                    object = new JSONObject(output);
                    String result = object.getString("status");
                    if (result.equals("success")) {
                        status = 1;
                    } else {
                        status = 2;
                        JSONObject jsonArray = object.getJSONObject("error");
                        JSONArray errorArr = jsonArray.getJSONArray("messages");
                        errorText = errorArr.getString(0);
                        Log.w("Submission error", errorText);
                    }
                } else {
                    status = 3;
                }
            } catch (Exception e) {
                status = 3;
            }
            return status;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            if (result == 1) {
                Toast.makeText(context, "Reservation Cancelled", Toast.LENGTH_LONG).show();
            } else if (result == 2) {
                Toast.makeText(context, errorText, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Submission failed, technical error.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
