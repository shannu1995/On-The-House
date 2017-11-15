package com.onthehouse.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.onthehouse.Utils.DrawerLocker;
import com.onthehouse.Utils.OffersAdapter;
import com.onthehouse.connection.APIConnection;
import com.onthehouse.details.Category;
import com.onthehouse.details.OfferDetail;
import com.onthehouse.details.Offers;
import com.onthehouse.details.UtilMethods;
import com.onthehouse.onthehouse.MainMenu;
import com.onthehouse.onthehouse.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class OffersList extends Fragment
{
    private RecyclerView recyclerView;
    private OffersAdapter adapter;
    private List<Offers> offersList;
    ArrayList<OfferDetail> offerDetails;
    ProgressDialog mProgressDialog;
    SwipeRefreshLayout mSwipeRefreshLayout;
    public OffersList() {

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_offers_list, container, false);
        Context mContext = container.getContext();

        offerDetails = OfferDetail.getInstance();
        recyclerView = view.findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_container);

        offersList = new ArrayList<>();
        adapter = new OffersAdapter(mContext, offersList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        //if not guest
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("GuestMember", Context.MODE_PRIVATE);
        boolean guestMember = sharedPreferences.getBoolean("GuestCheck", false);
        if (!guestMember) {
            ((MainMenu) container.getContext()).setChecked(R.id.offers, true);
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                // Refresh items
                refreshItems();
            }
        });

        ArrayList<String> inputList = new ArrayList<>();
        inputList.clear();
        Bundle bundle = this.getArguments();
        if(bundle == null){
            new inputAsyncData(mContext).execute(inputList);
        }else{
            if(bundle.getSerializable("categories") != null){
                HashMap<Category, Boolean> map = new HashMap<Category, Boolean>();
                map = (HashMap<Category, Boolean>)bundle.getSerializable("categories");
                String[] categories = {"","2"};
                inputList.add("category_id=");
            }
        }
        return view;
    }

    void refreshItems()
    {
        offersList = new ArrayList<>();

        ArrayList<String> inputList = new ArrayList<>();
        inputList.clear();

        new inputAsyncData(getActivity()).execute(inputList);
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public class inputAsyncData extends AsyncTask<ArrayList<String>, Void, Integer>
    {

        Context context;

        public inputAsyncData(Context context) {
            this.context = context;
        }

        protected void onPreExecute()
        {
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
        protected Integer doInBackground(ArrayList<String>... params)
        {
            int status = 0;

            APIConnection connection = new APIConnection();
            try
            {
                String output = connection.sendPost("/api/v1/events/current", params[0]);
                if (output.length() > 0)
                {
                    JSONObject obj = new JSONObject(output);
                    String result = obj.getString("status");
                    // JSONArray arr = obj.getJSONArray("member");

                    Log.w("LOGIN RESULT", result);

                    if (result.equals("success"))
                    {
                        try
                        {
                            //MembershipFragment member = MembershipFragment.getInstance();
                            JSONArray jsonArray = obj.getJSONArray("events");
                            Log.w("events", jsonArray.toString());

                            OfferDetail.clearInstance();

                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                Log.w("for", String.valueOf(i));
                                OfferDetail detail = new OfferDetail();
                                JSONObject event = jsonArray.getJSONObject(i);
                                detail.setOfferId(UtilMethods.tryParseInt(event.getString("id")));
                                Log.w("Offer Id is: ", event.getString("id"));
                                //String indEvent = connection.sendPost("/api/v1/event/13232", params[0]);
                                String indEvent = connection.sendPost("/api/v1/event/"+detail.getOfferId(), params[0]);
                                JSONObject indEventObj = new JSONObject(indEvent).getJSONObject("event");

                                detail.setType(event.getString("type"));
                                detail.setName(event.getString("name"));
                                detail.setPageTitle(event.getString("page_title"));
                                detail.setRating(UtilMethods.tryParseInt(event.getString("rating")));
                                detail.setImageURL(event.getString("image_url"));
                                detail.setDescription(event.getString("description"));
                                detail.setPriceFrom(event.getDouble("price_from"));
                                detail.setPriceTo(event.getDouble("price_to"));
                                detail.setFullPrice(event.getString("full_price_string"));
                                detail.setOurPrice(event.getString("our_price_string"));
                                detail.setOurPriceHeading(event.getString("our_price_heading"));
                                detail.setMemberShipLevel(event.getString("membership_levels"));
                                detail.setSoldOut(event.getBoolean("sold_out"));
                                detail.setComingSoon(event.getBoolean("coming_soon"));
                                detail.setCompt(event.getBoolean("is_competition"));
                                if(detail.isCompt()){
                                    JSONObject competitionDetails = indEventObj.getJSONObject("competition");
                                    detail.setQuestion(competitionDetails.getString("question"));
                                }
                                JSONArray showsArray = indEventObj.getJSONArray("show_data");

                                Log.w("Show Details: ",showsArray.toString());

                                JSONObject showsObject = showsArray.getJSONObject(0);
                                JSONObject venueObject = showsObject.getJSONObject("venue");
                                String showsHeadingObject = showsObject.getString("shows_heading");
                                JSONArray shows = showsObject.getJSONArray("shows");
                                detail.setWrongShowsArray(shows);
                                detail.setShowsArray(showsArray);

                                JSONObject showsDetailsObject = shows.getJSONObject(0);

                                String venueName = venueObject.getString("name");
                                String streetName = venueObject.getString("address1");
                                String cityName = venueObject.getString("city");
                                String stateName = venueObject.getString("zone_name");
                                String zipName = venueObject.getString("zip");
                                String countryName = venueObject.getString("country_name");

                                String venueAddress = venueName + "\n" + streetName + "\n"
                                        + cityName + ", " + stateName + "\n" + zipName + "\n"
                                        + countryName;
                                if(showsDetailsObject.getString("shipping").equals("0")){
                                    detail.setDelivery(false);
                                }
                                else
                                    detail.setDelivery(true);
                                ArrayList<Integer> quantities = new ArrayList<Integer>();
                                JSONArray quantitiesArray = showsDetailsObject.getJSONArray("quantities");
                                if(quantitiesArray != null){
                                    for(int j = 0; j < quantitiesArray.length(); j++){
                                        quantities.add(quantitiesArray.getInt(j));
                                        Log.w("qty: ",Integer.toString(quantitiesArray.getInt(j)));
                                    }
                                    detail.setQuantities(quantities);
                                }
                                Log.w("Venue Details: ",venueAddress);
                                detail.setVenueDetails(venueAddress);
                                detail.setShowsHeading(showsHeadingObject);
                                Date currentTime = Calendar.getInstance().getTime();

                                DateFormat dates = new SimpleDateFormat("dd/mm/yyyy hh:mm");
                                //Date date1 = new Date(Long.parseLong(showsDetailsObject.getString("show_date")) * 1000);
                                //String date2 = dates.format(showsDetailsObject.getString("show_date2"));
                                ArrayList<String> showDates = new ArrayList<>();
                                String date1 = dates.format(new Date(Long.parseLong(showsDetailsObject.getString("show_date"))* 1000));
                                String date2 = dates.format(new Date(Long.parseLong(showsDetailsObject.getString("show_date2"))* 1000));

                                String show_id = showsDetailsObject.getString("id");
                                detail.setShow_id(show_id);
                                showDates.add(date1);
                                showDates.add(date2);
                                Log.w("From: ",showsDetailsObject.getString("show_date") + "\n");
                                Log.w("To: ",date1 + "\n");
                                Log.w("From: ",showsDetailsObject.getString("show_date") + "\n");
                                Log.w("To: ",date2 + "\n");
                                //Log.w("to: ",date1);
                                //detail.setDates(showDates);
                                offerDetails.add(detail);
                            }

                            status = 1;
                        }
                        catch(Exception e)
                        {
                            Log.w("Event error", e.getMessage());
                            status = 3;
                        }
                    }

                    else
                    {
                        //2 = wrong details;
                        status = 2;
                    }
                }
                else
                {
                    // 3 = json parse error
                    status = 3;
                }
            }
            catch (Exception e)
            {
                status = 3;
            }

            return status;
        }

        @Override
        protected void onPostExecute(Integer result)
        {
            if(result == 1)
            {
                Offers a;
                for(int i=0; i<offerDetails.size(); i++) {

                    String imageUrl = offerDetails.get(i).getImageURL();
                    if(imageUrl.isEmpty())
                    {
                        imageUrl = "http://vollrath.com/ClientCss/images/VollrathImages/No_Image_Available.jpg";
                    }
                    a = new Offers(offerDetails.get(i).getName(), imageUrl, Integer.toString(offerDetails.get(i).getOfferId()));
                    a.setMembership_levels(offerDetails.get(i).getMemberShipLevel());
                    offersList.add(a);
                }
            }

            else if(result == 2)
            {
                Toast.makeText(context, "Technical error", Toast.LENGTH_LONG).show();
            }

            else
            {
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
