package com.onthehouse.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.onthehouse.Utils.DrawerLocker;
import com.onthehouse.Utils.MyMembershipsAdapter;
import com.onthehouse.connection.APIConnection;
import com.onthehouse.details.Member;
import com.onthehouse.details.Membership;
import com.onthehouse.onthehouse.R;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MembershipFragment extends Fragment {

    private static final String TAG = "MembershipFragment";
    private RadioGroup membershipLevel;
    private RadioButton gold_radio, bronze_radio;
    private Membership currentMembership;
    private ArrayList<Membership> membershipArrayList;
    private MyMembershipsAdapter adapter;
    private TextView myMembership_tv;
    //Pay pal
    private PayPalConfiguration configuration;
    int requestCode = 999;

    public MembershipFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_membership, container, false);
        final Context mContext = container.getContext();
        //Member Object
        final Member member = Member.getInstance();

        //Pay pal
        String client_id = getString(R.string.paypal_client_id);
        configuration = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(client_id);
        Intent service = new Intent(mContext, PayPalService.class);
        service.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        mContext.startService(service);

        //Initialize Lists
        membershipArrayList = new ArrayList<>();
        currentMembership = new Membership();

        //Views
        Button btn_change_membership = view.findViewById(R.id.btn_change_membership);
        ImageView info_gold = view.findViewById(R.id.iv_info_gold);
        ImageView info_bronze = view.findViewById(R.id.iv_info_bronze);
        myMembership_tv = view.findViewById(R.id.tv_my_memberships_label);
        membershipLevel = view.findViewById(R.id.rg_memberships);
        gold_radio = view.findViewById(R.id.radioButton_gold);
        bronze_radio = view.findViewById(R.id.radioButton_bronze);
        ListView listView = view.findViewById(R.id.lv_my_memberships);
        adapter = new MyMembershipsAdapter(container.getContext(), membershipArrayList);

        listView.setAdapter(adapter);

        ArrayList<String> inputList = new ArrayList<>();
        inputList.add("&member_id=" + Member.getInstance().getId());
        new currentMembershipAsyncData().execute(inputList);
        new pastMembershipAsyncData().execute(inputList);

        //listeners
        info_gold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = getString(R.string.gold_membership_details);
                infoDialog(container.getContext(), "Gold Membership: 6 Months - $55.90",
                        message, R.drawable.icon_gold_member);
            }
        });
        info_bronze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = getString(R.string.bronze_membership_details);
                infoDialog(container.getContext(), "Bronze Membership: 2 Years - Free",
                        message, R.drawable.icon_bronze_member);
            }
        });
        btn_change_membership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check if member is already a gold member
                if (member.getMembership_level_id() == 9
                        && member.getMembership_expiry() * 1000 > System.currentTimeMillis()
                        && gold_radio.isChecked()) {
                    Toast.makeText(getContext(), "Already a gold member", Toast.LENGTH_LONG).show();
                }
                //Upgrade to gold from bronze
                else if (member.getMembership_level_id() == 3 && gold_radio.isChecked()) {
                    upgradeDialog(mContext);

                }
                //Already a bronze member
                else if (member.getMembership_level_id() == 3 && bronze_radio.isChecked()) {
                    Toast.makeText(getContext(), "Already a bronze member", Toast.LENGTH_LONG).show();
                }
                //Downgrade to bronze
                else if (member.getMembership_level_id() == 9 && bronze_radio.isChecked()) {
                    downgradeDialog(mContext);
                }
                //Renew gold membership
                else if (member.getMembership_level_id() == 9
                        && member.getMembership_expiry() * 1000 < System.currentTimeMillis()
                        && gold_radio.isChecked()) {

                }


            }
        });

        return view;
    }

    public void upgradeDialog(final Context context) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Upgrade to Gold");
        alertDialog.setIcon(R.drawable.icon_gold_member);
        alertDialog.setMessage("You would be taken to Paypal.");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Proceed to Paypal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PayPalPayment payment =
                        new PayPalPayment(new BigDecimal(55.90), "AUD", "On The House - Gold Membership Fee", PayPalPayment.PAYMENT_INTENT_SALE);
                Intent intent = new Intent(context, PaymentActivity.class);
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
                startActivityForResult(intent, requestCode);
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void downgradeDialog(Context context) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Downgrade to Bronze");
        alertDialog.setIcon(R.drawable.icon_bronze_member);
        alertDialog.setMessage(getString(R.string.downgrade_message));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Change Membership", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == this.requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    String state = confirmation.getProofOfPayment().getState();
                    if (state.equals("approved")) {
                        Toast.makeText(getContext(), "Thank you for your payment. It may take a few minutes before your new membership is activated.", Toast.LENGTH_LONG).show();
                    } else
                        Toast.makeText(getContext(), "Payment Failed, Technical Error", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Payment Failed", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void infoDialog(Context context, String title, String message, int icon) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setIcon(icon);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public class pastMembershipAsyncData extends AsyncTask<ArrayList<String>, Void, Integer> {

        @Override
        protected void onPreExecute() {
            //Lock Drawer While Loading
            ((DrawerLocker) getActivity()).setDrawerEnabled(false);
        }

        @Override
        protected Integer doInBackground(ArrayList<String>... params) {
            int status = 0;
            APIConnection connection = new APIConnection();
            try {
                String output = connection.sendPost("/api/v1/member/membership/history", params[0]);
                if (output.length() > 0) {
                    JSONObject obj = new JSONObject(output);
                    String result = obj.getString("status");

                    if (result.equals("success")) {
                        try {
                            JSONArray memArray = obj.getJSONArray("memberships");
                            for (int i = 0; i < memArray.length(); i++) {
                                Membership membership = new Membership();
                                JSONObject memObject = memArray.getJSONObject(i);

                                membership.setId(memObject.getInt("id"));
                                membership.setMembership_level_id(memObject.getInt("membership_level_id"));
                                membership.setMember_id(memObject.getInt("member_id"));
                                membership.setDate_expires(memObject.getLong("date_expires"));
                                membership.setPrice(memObject.getDouble("price"));
                                membership.setPaypayl_data(memObject.getString("paypal_data"));
                                membership.setMembership_level_name(memObject.getString("membership_level_name"));
                                membership.setDate_created(memObject.getLong("date_created"));

                                membershipArrayList.add(membership);
                            }
                            status = 1;

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.w("Error", e.getMessage());
                            status = 3;
                        }
                    } else {
                        status = 2;
                        //Wrong Details
                        Log.d(TAG, "doInBackground: Wrong details, Status = " + status);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "doInBackground: " + e.getMessage());
                status = 3;
            }
            return status;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            adapter.notifyDataSetChanged();
            if (membershipArrayList.isEmpty()) {
                myMembership_tv.setText(R.string.no_membership_history);
            } else {
                myMembership_tv.setText(R.string.my_memberships);
            }
            //Unlock Drawer While Loading
            ((DrawerLocker) getActivity()).setDrawerEnabled(true);
        }
    }

    public class currentMembershipAsyncData extends AsyncTask<ArrayList<String>, Void, Integer> {

        @Override
        protected void onPreExecute() {
            //Lock Drawer While Loading
            ((DrawerLocker) getActivity()).setDrawerEnabled(false);
        }

        @Override
        protected Integer doInBackground(ArrayList<String>... params) {

            int status = 0;
            APIConnection connection = new APIConnection();
            try {
                String output = connection.sendPost("/api/v1/member/membership", params[0]);
                if (output.length() > 0) {
                    JSONObject obj = new JSONObject(output);
                    String result = obj.getString("status");

                    if (result.equals("success")) {
                        try {
                            JSONObject jsonArray = obj.getJSONObject("membership");
                            currentMembership.setId(jsonArray.getInt("id"));
                            currentMembership.setMembership_level_id(jsonArray.getInt("membership_level_id"));
                            currentMembership.setMember_id(jsonArray.getInt("member_id"));
                            currentMembership.setDate_expires(jsonArray.getLong("date_expires"));
                            currentMembership.setPrice(jsonArray.getDouble("price"));
                            currentMembership.setPaypayl_data(jsonArray.getString("paypal_data"));
                            currentMembership.setMembership_level_name(jsonArray.getString("membership_level_name"));
                            currentMembership.setDate_created(jsonArray.getLong("date_created"));

                            status = 1;

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.w("Error", e.getMessage());
                            status = 3;
                        }
                    } else {
                        status = 2;
                        //Wrong Details
                        Log.d(TAG, "doInBackground: Wrong details, Status = " + status);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "doInBackground: " + e.getMessage());
                status = 3;
            }
            return status;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            System.out.println(currentMembership.toString());
            try {
                if (currentMembership.getMembership_level_name().equalsIgnoreCase("gold")) {
                    gold_radio.setChecked(true);
                } else {
                    bronze_radio.setChecked(true);
                }
            } catch (NullPointerException e) {
                //API returns null if no membership changes have been made ever
                bronze_radio.setChecked(true);
                //Set Manually for comparison
                currentMembership.setMembership_level_name("Bronze");
                currentMembership.setMembership_level_id(3);
            }
            //Unlock Drawer While Loading
            ((DrawerLocker) getActivity()).setDrawerEnabled(true);
        }
    }
}
