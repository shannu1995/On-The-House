package com.onthehouse.onthehouse;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.onthehouse.connection.APIConnection;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 5/09/2017.
 */

public class Countries {


    public Countries() {

    }

    public class countryAsyncData extends AsyncTask<ArrayList<String>, Void, Integer> {
        private static final String TAG = "inputAsyncData";
        Context context;

        public countryAsyncData(Context context) {
            this.context = context;
        }



        protected void onPreExecute()
        {

        }



        @Override
        protected Integer doInBackground(ArrayList<String>... params)
        {
            int status = 0;

            APIConnection connection = new APIConnection();

            try
            {
                String output = connection.sendPost("/api/v1/countries", null);

                if (output.length() > 0)
                {
                    JSONObject obj = new JSONObject(output);
                    String result = obj.getString("status");

                    Log.w("RESET RESULT", result);

                    if (result.equals("success"))
                    {
                        //1 = success;
                        status = 1;
                        Log.w("status", String.valueOf(status));

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

        protected void onPostExecute(Integer result)
        {
            Log.w("Reset result", result.toString());

            if(result == 1)
            {
                Log.d(TAG, "onPostExecute: ");
//                resetButton.animFinish();
//                Snackbar.make(layout, "Reset Password Successful.", Snackbar.LENGTH_LONG).show();
//                Intent resetDoneIntent = new Intent(ResetPassword.this, LoginActivity.class);
//                ResetPassword.this.startActivity(resetDoneIntent);
            }
            else if(result == 2)
            {
//                resetButton.animError();
//                Snackbar.make(layout, "Reset failed, please check your email.", Snackbar.LENGTH_LONG).show();
            }

            else
            {
//                resetButton.animError();
//                Snackbar.make(layout, "Reset failed, technical error.", Snackbar.LENGTH_LONG).show();
            }
//            resetButton.setEnabled(true);
        }
    }

}
