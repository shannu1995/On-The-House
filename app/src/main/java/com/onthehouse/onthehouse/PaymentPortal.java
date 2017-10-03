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

import com.onthehouse.connection.APIConnection;
import com.onthehouse.details.Member;
import com.paypal.android.sdk.payments.*;

import org.json.JSONArray;
import org.json.JSONException;
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_portal);
        configuration = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(clientId);
        service = new Intent(this, PayPalService.class);
        service.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        startService(service);
    }
    public void pay(View view){
        PayPalPayment payment =
                new PayPalPayment(new BigDecimal(10), "AUD", "Paypal testing", PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, requestCode);
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
                        Bundle extras = getIntent().getExtras();
                        inputList.add("nonce="+(UUID.randomUUID().toString()).replace("-",""));
                        inputList.add("&reservation_id="+extras.getString("reservation_id"));
                        inputList.add("&show_id="+extras.getString("show_id"));
                        inputList.add("&member_id="+Member.getInstance().getId());
                        inputList.add("&price="+extras.getString("item_price"));
                        inputList.add("&tickets="+extras.getString("tickets"));
                        new reserveAsyncData(getApplicationContext()).execute(inputList);
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

        public reserveAsyncData(Context context){this.context = context;}

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
            if(result == 1)
            {
                Log.w("COMPLETED!", "SUCCESS!!");
            }

            else if(result == 2)
            {
                Log.w("ERROR!!!", errorText);
            }
            else
            {
                Log.w("SUBMISSION FAILED!", "Technical Error");
            }
        }
    }
}
