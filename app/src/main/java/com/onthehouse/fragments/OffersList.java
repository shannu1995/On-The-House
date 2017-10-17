package com.onthehouse.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onthehouse.Utils.OffersAdapter;
import com.onthehouse.connection.APIConnection;
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
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);

        offersList = new ArrayList<>();
        adapter = new OffersAdapter(mContext, offersList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                // Refresh items
                refreshItems();
            }
        });

        ArrayList<String> inputList = new ArrayList<String>();
        inputList.clear();

        new inputAsyncData(mContext).execute(inputList);

        return view;
    }


    void refreshItems()
    {

        offersList = new ArrayList<>();

        ArrayList<String> inputList = new ArrayList<String>();
        inputList.clear();

        new inputAsyncData(getActivity()).execute(inputList);
        //Intent offerIntent = new Intent(getActivity(), MainMenu.class);
        //startActivity(offerIntent);

        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
    }

    /**
     * Adding few offers for testing
     */

/*   private void prepareOffers() {
       int[] covers = new int[]{
               R.drawable.album1,
               R.drawable.album2};

       Offers a = new Offers("True Romance", covers[0]);
       offersList.add(a);

       a = new Offers("Xscpae", covers[1]);
       offersList.add(a);


   }*/

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public class inputAsyncData extends AsyncTask<ArrayList<String>, Void, Integer>
    {

        Context context;

        public inputAsyncData(Context context) {
            this.context = context;
        }

        protected void onPreExecute()
        {
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(getActivity());
            // Set progressdialog title
            mProgressDialog.setTitle("Getting offers....");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
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
                            //Member member = Member.getInstance();
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
                                detail.setDates(showDates);
                                offerDetails.add(detail);
                            }
                            //Log.w("eventtt", offerDetails.get(0).getDescription());
                            //Log.w("eventtt", offerDetails.get(1).getDescription());

                            status = 1;
                        }
                        catch(Exception e)
                        {
                            Log.w("Event error", e.getMessage());
                            status = 3;
                        }

                        //Log.w("status", String.valueOf(status));
                        //Log.w("OBJECT TEST", member.getFirst_name());
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
                    offersList.add(a);
                }

                //loginButton.animFinish();
                //Snackbar.make(layout, "Login successful.", Snackbar.LENGTH_LONG).show();

            }

            else if(result == 2)
            {
                //loginButton.animError();
                //Snackbar.make(layout, "Login failed, please check your details", Snackbar.LENGTH_LONG).show();
            }

            else
            {
                //loginButton.animError();
                //Snackbar.make(layout, "Login failed, technical error.", Snackbar.LENGTH_LONG).show();
            }
            adapter.notifyDataSetChanged();
            mProgressDialog.dismiss();

            //loginButton.setEnabled(true);
        }
    }
}
