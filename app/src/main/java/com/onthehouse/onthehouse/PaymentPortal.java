package com.onthehouse.onthehouse;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.onthehouse.connection.APIConnection;
import com.onthehouse.details.Member;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

public class PaymentPortal extends AppCompatActivity {

    public PayPalConfiguration configuration;
    public ConstraintLayout layout;
    String clientId = "ASYJ58EM8uChSD_3fil3tG4cBtlhfAkCNBsqyqynS6NY0qqCfBLz7uE4yu1x3s4caImMq9JBESwQ8w1U";
    Intent service;
    int requestCode = 999;
    String errorText = "";
    ArrayList<String> inputList = new ArrayList<String>();
    Bundle extras;
    Button button;
    TextView heading;
    TextView details;
    TextView result;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_payment_portal);
        configuration = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(clientId);
        heading = (TextView) findViewById(R.id.paymentSummary);
        details = (TextView) findViewById(R.id.details);
        button = (Button) findViewById(R.id.button);
        result = (TextView) findViewById(R.id.result);

        extras = getIntent().getExtras();
        details.setText("Number of Tickets: "+extras.getString("tickets"));
        if(extras.getString("payment").equals("affiliate")){
            button.setVisibility(View.GONE);
            details.append("\nRedirected to the affiliated URL provided by the supplier.\n Please complete your transaction there");
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(extras.getString("affiliate_url")));
            startActivity(browserIntent);
        }
        else if(extras.getString("payment").equals("free")){
            button.setVisibility(View.GONE);
            details.append("\nAs a gold member, you do not need to pay the admin fee for this event");
            inputList.add("nonce="+(UUID.randomUUID().toString()).replace("-",""));
            inputList.add("&reservation_id="+extras.getString("reservation_id"));
            inputList.add("&show_id="+extras.getString("show_id"));
            inputList.add("&member_id="+Member.getInstance().getId());
            inputList.add("&price=0");
            inputList.add("&tickets="+extras.getString("tickets"));
            new reserveAsyncData(getApplicationContext(), result).execute(inputList);
        } else{

        }
        service = new Intent(this, PayPalService.class);
        service.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        startService(service);
    }
    @Override
    public void onBackPressed(){
        Intent mainMenuIntent = new Intent(this, MainMenu.class);
        PaymentPortal.this.startActivity(mainMenuIntent);
    }
    public void pay(View view){
        if(extras.getString("payment").equals("paypal")){
            PayPalPayment payment =
                    new PayPalPayment(new BigDecimal(10), "AUD", "Paypal testing", PayPalPayment.PAYMENT_INTENT_SALE);
            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
            startActivityForResult(intent, requestCode);
        }
        else{
            inputList.add("nonce="+(UUID.randomUUID().toString()).replace("-",""));
            inputList.add("&reservation_id="+extras.getString("reservation_id"));
            inputList.add("&show_id="+extras.getString("show_id"));
            inputList.add("&member_id="+Member.getInstance().getId());
            inputList.add("&price="+extras.getString("item_price"));
            inputList.add("&tickets="+extras.getString("tickets"));
            new reserveAsyncData(getApplicationContext(), result).execute(inputList);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == this.requestCode){
            if(resultCode == Activity.RESULT_OK)
            {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if(confirmation != null){
                    String state = confirmation.getProofOfPayment().getState();
                    if(state.equals("approved")) {
                        inputList.add("nonce="+(UUID.randomUUID().toString()).replace("-",""));
                        inputList.add("&reservation_id="+extras.getString("reservation_id"));
                        inputList.add("&show_id="+extras.getString("show_id"));
                        inputList.add("&member_id="+Member.getInstance().getId());
                        inputList.add("&price="+extras.getString("item_price"));
                        inputList.add("&tickets="+extras.getString("tickets"));
                        new reserveAsyncData(getApplicationContext(), result).execute(inputList);
                    }
                    else
                        Snackbar.make(layout, "Payment Failed, Technical Error", Snackbar.LENGTH_LONG).show();
                }
                else{
                    Snackbar.make(layout, "Payment Failed", Snackbar.LENGTH_LONG).show();
                }
            }
        }
    }
    public class reserveAsyncData extends AsyncTask<ArrayList<String>, Void, Integer> {
        Context context;
        APIConnection connection = new APIConnection();
        JSONObject object;

        public reserveAsyncData(Context context, TextView result){this.context = context;}

        protected void onPreExecute()
        {
        }

        protected Integer doInBackground(ArrayList<String>... params){
            int status = 0;
            try{
                String output = connection.sendPost("/api/v1/reserve/complete", params[0]);
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
            TextView textView = (TextView) findViewById(R.id.result);
            textView.setVisibility(View.VISIBLE);
            if(result == 1)
            {
                Log.w("COMPLETED!", "SUCCESS!!");
                textView.setText("Reservation Successful!\n Please check \'My Account\' or your email for the reservation details");
                finish();
            }

            else if(result == 2)
            {
                Log.w("ERROR!!!", errorText);
                textView.setText("Reservation Failed because:\n"+errorText);
            }
            else
            {
                Log.w("SUBMISSION FAILED!", errorText);
                if(extras.getString("payment").equals("free")){
                    textView.setText("Reservation Successful!\n Please check \'My Account\' or your email for the reservation details");
                }else{
                    textView.setText("Reservation Failed because:\n"+errorText);
                }
            }
        }
    }
}
