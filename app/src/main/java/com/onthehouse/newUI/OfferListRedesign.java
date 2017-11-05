package com.onthehouse.newUI;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.onthehouse.Utils.DrawerLocker;
import com.onthehouse.connection.APIConnection;
import com.onthehouse.details.OfferDetail;
import com.onthehouse.details.UtilMethods;
import com.onthehouse.onthehouse.MainMenu;
import com.onthehouse.onthehouse.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class OfferListRedesign extends Fragment {

    private ArrayList<Events> eventsArrayList = new ArrayList<>();
    private OfferListAdapter adapter;


    public OfferListRedesign() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_offer_list, container, false);
        //Async Data
        ArrayList<String> inputList = new ArrayList<>();
        inputList.clear();
        //noinspection unchecked
        new inputAsyncData(container.getContext()).execute(inputList);
        //Recycler VIew
        //Recycler View
        RecyclerView recyclerView = view.findViewById(R.id.rv_events_new);
        LinearLayoutManager layoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new OfferListAdapter(container.getContext(), eventsArrayList);
        recyclerView.setAdapter(adapter);


        return view;
    }

    public class inputAsyncData extends AsyncTask<ArrayList<String>, Void, Integer> {

        Context context;
        ProgressDialog mProgressDialog;

        public inputAsyncData(Context context) {
            this.context = context;
        }

        protected void onPreExecute() {
            // Create a progress dialog
            mProgressDialog = new ProgressDialog(getActivity());
            // Set progress dialog title
            mProgressDialog.setTitle("Getting offers....");
            // Set progress dialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progress dialog
            mProgressDialog.show();
            //Lock Drawer While Loading
            if (getActivity() instanceof MainMenu) {
                ((DrawerLocker) getActivity()).setDrawerEnabled(false);
            }
        }

        @Override
        protected Integer doInBackground(ArrayList<String>... params) {
            int status = 0;
            APIConnection connection = new APIConnection();
            try {
                String output = connection.sendPost("/api/v1/events/current", params[0]);
                if (output.length() > 0) {
                    JSONObject obj = new JSONObject(output);
                    String result = obj.getString("status");

                    if (result.equals("success")) {
                        try {
                            //MembershipFragment member = MembershipFragment.getInstance();
                            JSONArray jsonArray = obj.getJSONArray("events");
                            Log.w("events", jsonArray.toString());

                            OfferDetail.clearInstance();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                Events events = new Events();
                                JSONObject event = jsonArray.getJSONObject(i);
                                events.setId(UtilMethods.tryParseInt(event.getString("id")));
                                events.setType(event.getString("type"));
                                events.setName(event.getString("name"));
                                events.setRating(UtilMethods.tryParseInt(event.getString("rating")));
                                events.setImage_url(event.getString("image_url"));
                                events.setDescription(event.getString("description"));
                                events.setPrice_from(event.getDouble("price_from"));
                                events.setPrice_to(event.getDouble("price_to"));
                                events.setFull_price_string(event.getString("full_price_string"));
                                events.setOur_price_string(event.getString("our_price_string"));
                                events.setOur_price_heading(event.getString("our_price_heading"));
                                events.setMembership_levels(event.getString("membership_levels"));
                                events.setSold_out(event.getBoolean("sold_out"));
                                events.setComing_soon(event.getBoolean("coming_soon"));
                                events.setIs_competition(event.getBoolean("is_competition"));

                                eventsArrayList.add(events);
                            }
                            status = 1;
                        } catch (Exception e) {
                            Log.w("Event error", e.getMessage());
                            status = 3;
                        }
                    } else {
                        //2 = wrong details;
                        status = 2;
                    }
                } else {
                    // 3 = json parse error
                    status = 3;
                }
            } catch (Exception e) {
                status = 3;
            }
            return status;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result == 1) {
                for (Events events : eventsArrayList) {
                    if (events.getImage_url().isEmpty()) {
                        events.setImage_url("http://vollrath.com/ClientCss/images/VollrathImages/No_Image_Available.jpg");
                    }
                }
            } else if (result == 2) {
                Toast.makeText(context, "Technical error", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Technical error", Toast.LENGTH_LONG).show();
            }

            adapter.notifyDataSetChanged();
            mProgressDialog.dismiss();
            if (getActivity() instanceof MainMenu) {
                //Unlock Drawer
                ((DrawerLocker) getActivity()).setDrawerEnabled(true);
            }
        }
    }
}
