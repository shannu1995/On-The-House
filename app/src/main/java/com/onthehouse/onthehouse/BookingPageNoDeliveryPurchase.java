package com.onthehouse.onthehouse;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.onthehouse.connection.APIConnection;
import com.onthehouse.details.Member;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.xm.weidongjian.progressbuttonlib.ProgressButton;


public class BookingPageNoDeliveryPurchase extends AppCompatActivity {
    public PaymentPortal portal;
    public ProgressButton confirmButton;
    public TextView paymentView;
    private String show_id;
    private String tickets;
    public String errorText = "";
    public ConstraintLayout layout;
    public ArrayList<String> inputList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_page_no_delivery_purchase);


        layout = (ConstraintLayout) findViewById(R.id.pickUpLayout);
        final EditText editText = (EditText) findViewById(R.id.editText2);
        confirmButton = (ProgressButton) findViewById(R.id.confirm_delivery);

        Bundle extras = getIntent().getExtras();
        this.setShow_id(extras.getString("show_id"));
        this.setTickets(extras.getString("tickets"));

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.show_source_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        confirmButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                inputList.add("show_id="+getShow_id());
                inputList.add("&member_id="+ Member.getInstance().getId());
                inputList.add("&tickets="+getTickets());
                inputList.add("&shipping_first_name=null");
                inputList.add("&shipping_last_name=null");
                inputList.add("&shipping_address1=null");
                inputList.add("&shipping_city=null");
                inputList.add("&shipping_zone_id=null");
                inputList.add("&shipping_zip=null");
                inputList.add("&shipping_phone=null");
                inputList.add("&shipping_save_info=1");
                inputList.add("&question_id="+Integer.toString(spinner.getSelectedItemPosition()));
                inputList.add("&question_text="+((EditText) findViewById(R.id.editText2)).getText().toString());
                for (int i = 0; i < inputList.size(); i ++){
                    Log.w("Info: ", inputList.get(i));
                    Log.w("","\n");
                }
                new pickUpAsyncData(getApplicationContext()).execute(inputList);
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                if(parent.getItemAtPosition(pos).equals("Which website?") ||
                        parent.getItemAtPosition(pos).equals("If Google search, what did you search for?") ||
                        parent.getItemAtPosition(pos).equals("Please provide details of how you heard of this show?")){
                    findViewById(R.id.editText2).setVisibility(View.VISIBLE);
                }
                else{
                    findViewById(R.id.editText2).setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void setShow_id(String show_id){this.show_id = show_id;}
    public String getShow_id(){return this.show_id;}

    public void setTickets(String tickets){this.tickets = tickets;}
    public String getTickets(){return this.tickets;}

    public class pickUpAsyncData extends AsyncTask<ArrayList<String>, Void, Integer> {
        Context context;
        Intent paymentActivity;
        APIConnection connection = new APIConnection();
        JSONObject object = new JSONObject();

        public pickUpAsyncData(Context context){this.context = context;
         paymentActivity = new Intent(context, PaymentPortal.class);}

        protected void onPreExecute()
        {
            confirmButton.setEnabled(false);
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
                confirmButton.animFinish();
                Snackbar.make(layout, "Submitted Answer", Snackbar.LENGTH_LONG).show();
                try{
                    if(object.getString("paypal").equals("1")){
                        Bundle extras = new Bundle();
                        extras.putString("item_name",object.getString("item_name"));
                        extras.putString("item_sku",object.getString("item_sku"));
                        extras.putString("item_quantity",getTickets());
                        extras.putString("item_price", object.getString("item_price"));
                        extras.putString("reservation_id", object.getString("reservation_id"));
                        extras.putString("show_id", getShow_id());
                        extras.putString("tickets", getTickets());
                        paymentActivity.putExtras(extras);
                        paymentActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(paymentActivity);
                    }
                    else{
                    }
                }catch(JSONException e){
                    Log.w("Error: JSON Exception", e);
                }
            }

            else if(result == 2)
            {
                Snackbar.make(layout, errorText, Snackbar.LENGTH_LONG).show();
                confirmButton.animError();
            }
            else
            {
                Snackbar.make(layout, "Submission failed, technical error.", Snackbar.LENGTH_LONG).show();
                confirmButton.animError();
            }
            confirmButton.setEnabled(true);
        }
    }
}
