package com.onthehouse.onthehouse;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.onthehouse.connection.APIConnection;
import com.onthehouse.details.Member;

import org.json.JSONObject;

import java.util.ArrayList;

public class ResetPassword extends AppCompatActivity
{
    EditText resetEmail;
    Button resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        resetButton = (Button) findViewById(R.id.resetButton);
        resetEmail = (EditText) findViewById(R.id.resetEmail);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String email = resetEmail.getText().toString();

                ArrayList<String> inputList = new ArrayList<String>();
                inputList.add("email="+email);

                new inputAsyncData(getApplicationContext()).execute(inputList);
            }
        });


    }

    public class inputAsyncData extends AsyncTask<ArrayList<String>, Void, Integer> {

        Context context;

        public inputAsyncData(Context context) {
            this.context = context;
        }

        protected Integer doInBackground(ArrayList<String>... params)
        {
            int status = 0;

            APIConnection connection = new APIConnection();

            try
            {
                String output = connection.sendPost("/api/v1/member/forgot-password", params[0]);

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


        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Integer result)
        {
            Log.w("Reset result", result.toString());

            if(result == 1)
            {
                Toast.makeText(ResetPassword.this, "Reset Password Successful.", Toast.LENGTH_LONG).show();

                Intent resetDoneIntent = new Intent(ResetPassword.this, LoginActivity.class);
                ResetPassword.this.startActivity(resetDoneIntent);
            }
            else if(result == 2)
            {
                Toast.makeText(ResetPassword.this, "Reset failed, please check your email.", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(ResetPassword.this, "Result failed, technical error.", Toast.LENGTH_LONG).show();
            }
        }
    }

}
