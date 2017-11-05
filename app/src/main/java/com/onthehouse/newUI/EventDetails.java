package com.onthehouse.newUI;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.onthehouse.connection.APIConnection;
import com.onthehouse.details.UtilMethods;
import com.onthehouse.onthehouse.R;

import org.json.JSONObject;

public class EventDetails extends AppCompatActivity {

    private ImageView imageView;
    private Events currentEvent;
    private String eventID;
    private TextView title;
    private TextView fullPrice;
    private TextView adminFee;
    private TextView memberType;
    private TextView description;
    private RatingBar ratingBar;
    private Button btn_more_less;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        imageView = (ImageView) findViewById(R.id.htab_header);
        title = (TextView) findViewById(R.id.tv_ed_title_new);
        fullPrice = (TextView) findViewById(R.id.tv_ed_full_price);
        adminFee = (TextView) findViewById(R.id.tv_ed_admin_fee);
        memberType = (TextView) findViewById(R.id.tv_ed_mem_type);
        description = (TextView) findViewById(R.id.tv_ed_about_event);
        ratingBar = (RatingBar) findViewById(R.id.rb_ed_new);
        btn_more_less = (Button) findViewById(R.id.btn_ed_more_less);

        setBtn_more_less();


        //Initialize event
        currentEvent = new Events();
        //Collapsing toolbar
        setupToolbar("Event Details");
        //Get Event ID
        eventID = String.valueOf(getIntent().getIntExtra("event_id", 0));
        System.out.println(eventID);
        //noinspection unchecked
        new eventData(this).execute(eventID);


    }

    public void setBtn_more_less() {
        btn_more_less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_more_less.getText().toString().equalsIgnoreCase("VIEW MORE")) {
                    description.setMaxLines(Integer.MAX_VALUE);
                    description.setEllipsize(null);

                    btn_more_less.setText("VIEW LESS");
                    System.out.println("CALLED");
                } else {
                    description.setMaxLines(5);
                    description.setEllipsize(TextUtils.TruncateAt.END);

                    btn_more_less.setText("VIEW MORE");
                }

            }
        });
    }

    public void setupToolbar(final String title) {
        //Theme and color
        final CollapsingToolbarLayout mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mCollapsingToolbar.setTitle(" ");//careful there should a space between double quote otherwise it wont work

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_event_details);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    mCollapsingToolbar.setTitle(title);
                    isShow = true;
                } else if (isShow) {
                    mCollapsingToolbar.setTitle(" ");//careful there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public class eventData extends AsyncTask<String, Void, Integer> {

        private Context context;

        public eventData(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... strings) {
            Integer status = 0;
            APIConnection connection = new APIConnection();
            try {
                String output = connection.sendGet("/api/v1/event/" + strings[0]);
                if (output.length() > 0) {
                    JSONObject jsonObject = new JSONObject(output);
                    String result = jsonObject.getString("status");
                    if (result.equals("success")) {
                        JSONObject event = jsonObject.getJSONObject("event");

                        currentEvent.setId(UtilMethods.tryParseInt(event.getString("id")));
                        currentEvent.setType(event.getString("type"));
                        currentEvent.setName(event.getString("name"));
                        currentEvent.setRating(UtilMethods.tryParseInt(event.getString("rating")));
                        currentEvent.setImage_url(event.getString("image_url"));
                        currentEvent.setDescription(event.getString("description"));
                        currentEvent.setPrice_from(event.getDouble("price_from"));
                        currentEvent.setPrice_to(event.getDouble("price_to"));
                        currentEvent.setFull_price_string(event.getString("full_price_string"));
                        currentEvent.setOur_price_string(event.getString("our_price_string"));
                        currentEvent.setOur_price_heading(event.getString("our_price_heading"));
                        currentEvent.setMembership_levels(event.getString("membership_levels"));
                        currentEvent.setComing_soon(event.getBoolean("coming_soon"));
                        currentEvent.setIs_competition(event.getBoolean("is_competition"));

                        System.out.println(currentEvent.toString());
                        status = 1;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return status;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if (integer == 1) {
                title.setText(currentEvent.getName());
                fullPrice.setText("Full price: " + currentEvent.getFull_price_string());
                adminFee.setText(currentEvent.getOur_price_heading() + ": " + currentEvent.getOur_price_string());
                memberType.setText(currentEvent.getMembership_levels());
                description.setText(currentEvent.getDescription());
                ratingBar.setRating(currentEvent.getRating());

                if (currentEvent.getImage_url().isEmpty()) {
                    currentEvent.setImage_url("http://vollrath.com/ClientCss/images/VollrathImages/No_Image_Available.jpg");
                }
                Glide.with(context).load(currentEvent.getImage_url()).into(imageView);
            }
        }
    }

}
