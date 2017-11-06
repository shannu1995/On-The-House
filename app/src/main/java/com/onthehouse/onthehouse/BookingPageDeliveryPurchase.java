package com.onthehouse.onthehouse;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.onthehouse.connection.APIConnection;
import com.onthehouse.details.Member;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.xm.weidongjian.progressbuttonlib.ProgressButton;

public class BookingPageDeliveryPurchase extends AppCompatActivity {
    public MaterialEditText firstName;
    public MaterialEditText lastName;
    public MaterialEditText street;
    public MaterialEditText city;
    public MaterialEditText state;
    public MaterialEditText postcode;
    public MaterialEditText phone;
    public MaterialEditText mail;
    public ProgressButton submit_reservation;
    public ConstraintLayout layout;
    public ArrayList<String> inputList = new ArrayList<String>();

    private String showId;
    private String tickets;
    private String price;
    private String is_admin_fee;
    private String shipping_price;
    public String errorText = "";
    private MaterialEditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_page_delivery_purchase);

        editText = (MaterialEditText) findViewById(R.id.elaborate);
        layout = (ConstraintLayout) findViewById(R.id.deliveryLayout);
        firstName = (MaterialEditText) findViewById(R.id.first_name);
        lastName = (MaterialEditText) findViewById(R.id.last_name);
        street = (MaterialEditText) findViewById(R.id.street);
        city = (MaterialEditText) findViewById(R.id.city);
        state = (MaterialEditText) findViewById(R.id.state);
        mail = (MaterialEditText) findViewById(R.id.email);
        phone = (MaterialEditText) findViewById(R.id.phone);
        postcode = (MaterialEditText) findViewById(R.id.postcode);
        submit_reservation = (ProgressButton) findViewById(R.id.confirm_delivery);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        this.setIs_admin_fee(extras.getString("admin_fee"));
        this.setShipping_price(extras.getString("shipping_fee"));
        this.setShowId(extras.getString("show_id"));
        this.setTickets(extras.getString("tickets"));
        this.setPrice(extras.getString("price"));

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.show_source_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        firstName.setText(Member.getInstance().getFirst_name());
        lastName.setText(Member.getInstance().getLast_name());
        city.setText(Member.getInstance().getCity());
        state.setText("Victoria");
        postcode.setText(Integer.toString(Member.getInstance().getZip_code()));
        mail.setText(Member.getInstance().getEmail());
        street.setText(Member.getInstance().getAddress1());
        phone.setText(Member.getInstance().getPhone_number());


        submit_reservation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                inputList.add("show_id="+showId);
                inputList.add("&member_id="+Member.getInstance().getId());
                inputList.add("&tickets="+tickets);
                inputList.add("&shipping_first_name="+firstName.getText().toString());
                inputList.add("&shipping_last_name="+lastName.getText().toString());
                inputList.add("&shipping_address1="+street.getText().toString());
                inputList.add("&shipping_city="+city.getText().toString());
                inputList.add("&shipping_zone_id="+Member.getInstance().getZone_id());
                inputList.add("&shipping_zip="+postcode.getText().toString());
                inputList.add("&shipping_phone="+phone.getText().toString());
                inputList.add("&shipping_save_info=1");
                inputList.add("&question_id="+Integer.toString(spinner.getSelectedItemPosition()));
                inputList.add("&question_text="+(editText.getText().toString()));
                for (int i = 0; i < inputList.size(); i ++){
                    Log.w("Info: ", inputList.get(i));
                    Log.w("","\n");
                }
                int size = editText.getText().toString().length();
                Log.w("SHOULDBEEMPTY!", Integer.toString(size) + "why?");
                if(editText.getVisibility() != EditText.GONE && size == 0){
                    Snackbar.make(layout, "Please Elaborate.", Snackbar.LENGTH_LONG).show();
                }else{
                    if(spinner.getSelectedItemPosition() == 0){
                        Snackbar.make(layout, "Please choose how you've discovered this event.", Snackbar.LENGTH_LONG).show();
                    }else{
                        new deliverAsyncData(getApplicationContext()).execute(inputList);
                    }
                }
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                if(parent.getItemAtPosition(pos).equals("Which website?") ||
                        parent.getItemAtPosition(pos).equals("If Google search, what did you search for?") ||
                        parent.getItemAtPosition(pos).equals("Please provide details of how you heard of this show?")){
                    findViewById(R.id.elaborate).setVisibility(View.VISIBLE);
                }
                else{
                    findViewById(R.id.elaborate).setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setShowId(String showId){this.showId = showId;}
    public String getShowId(){return this.showId;}
    public void setIs_admin_fee(String is_admin_fee){this.is_admin_fee = is_admin_fee;}
    public String getIs_admin_fee(){return  this.is_admin_fee;}
    public void setShipping_price(String shipping_price){this.shipping_price = shipping_price;}
    public String getShipping_price(){return  this.shipping_price;}
    public void setTickets(String tickets){this.tickets = tickets;}
    public String getTickets(){return this.tickets;}
    public void setPrice(String price){this.price = price;}
    public String getPrice(){return this.price;}

    public class deliverAsyncData extends AsyncTask<ArrayList<String>, Void, Integer>{
        Context context;
        Intent paymentActivity;
        APIConnection connection = new APIConnection();
        JSONObject object = new JSONObject();

        public deliverAsyncData(Context context){this.context = context;
            paymentActivity = new Intent(context, PaymentPortal.class);}

        protected void onPreExecute()
        {
            submit_reservation.setEnabled(false);
        }

        protected Integer doInBackground(ArrayList<String>... params){
            int status = 0;
            try{
                String output = connection.sendPost("/api/v1/reserve", params[0]);
                if (output.length() > 0){
                    object = new JSONObject(output);
                    String result = object.getString("status");
                    Log.w("Full details: ",output.toString());

                    if(result.equals("success")){
                        status = 1;
                    }else{
                        status = 2;
                        JSONObject jsonArray = object.getJSONObject("error");
                        JSONArray errorArr=  jsonArray.getJSONArray("messages");
                        errorText = errorArr.getString(0);
                        Log.w("Submission error", errorText);
                    }
                }
                else{
                    status = 3;
                }
            }catch(Exception e) {
                status = 3;
            }
            return status;
        }
        protected void onPostExecute(Integer result)
        {
            if(result == 1)
            {
                submit_reservation.animFinish();
                Snackbar.make(layout, "Submitted Answer", Snackbar.LENGTH_LONG).show();
                Bundle extras = new Bundle();
                if(object.has("paypal")){
                    try{
                        if(object.getString("paypal").equals("1")){
                            extras.putString("payment", "paypal");
                            extras.putString("item_name",object.getString("item_name"));
                            extras.putString("item_sku",object.getString("item_sku"));
                            extras.putString("item_quantity",getTickets());
                            extras.putString("item_price", object.getString("item_price"));
                            extras.putString("reservation_id", object.getString("reservation_id"));
                            extras.putString("show_id", getShowId());
                            extras.putString("tickets", getTickets());
                        }
                        else{
                            extras.putString("payment", "free");
                            extras.putString("reservation_id", object.getString("reservation_id"));
                            extras.putString("show_id", getShowId());
                            extras.putString("tickets", getTickets());
                        }
                    } catch (Exception e){
                    }
                } else{
                    try{
                        extras.putString("payment", "affiliate");
                        extras.putString("affiliate_url", object.getString("affiliate_url"));
                    }catch (Exception e){

                    }
                }
                extras.putString("is_admin_fee", getIs_admin_fee());
                extras.putString("price", getPrice());
                extras.putString("shipping_fee", getShipping_price());
                paymentActivity.putExtras(extras);
                paymentActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(paymentActivity);
            }

            else if(result == 2)
            {
                Snackbar.make(layout, errorText, Snackbar.LENGTH_LONG).show();
                submit_reservation.animError();
            }
            else
            {
                Snackbar.make(layout, "Submission failed, technical error.", Snackbar.LENGTH_LONG).show();
                submit_reservation.animError();
            }
            submit_reservation.setEnabled(true);
        }
    }
}
