package com.onthehouse.onthehouse;

import android.app.Activity;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.onthehouse.details.Member;
import com.paypal.android.sdk.payments.*;

import java.math.BigDecimal;

public class PaymentPortal extends AppCompatActivity {

    public PayPalConfiguration configuration;
    public ConstraintLayout layout;
    String clientId = "ASYJ58EM8uChSD_3fil3tG4cBtlhfAkCNBsqyqynS6NY0qqCfBLz7uE4yu1x3s4caImMq9JBESwQ8w1U";
    Intent service;
    int requestCode = 999;

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
                    if(state.equals("approved"))
                        Snackbar.make(layout, "Payment Succeeded", Snackbar.LENGTH_LONG).show();
                    else
                        Snackbar.make(layout, "Payment Failed, Technical Error", Snackbar.LENGTH_LONG).show();
                }
                else{
                    Snackbar.make(layout, "Payment Failed", Snackbar.LENGTH_LONG).show();
                }
            }
        }
    }
}
