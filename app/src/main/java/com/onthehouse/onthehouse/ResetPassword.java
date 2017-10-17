package com.onthehouse.onthehouse;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.onthehouse.connection.APIConnection;

import org.json.JSONObject;

import java.util.ArrayList;

import cn.xm.weidongjian.progressbuttonlib.ProgressButton;

public class ResetPassword extends AppCompatActivity
{
    EditText resetEmail;
    ProgressButton resetButton;
    public ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        resetButton = (ProgressButton) findViewById(R.id.resetButton);
        resetEmail = (EditText) findViewById(R.id.resetEmail);
        layout = (ConstraintLayout) findViewById(R.id.resetLayout);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                resetButton.startRotate();
                String email = resetEmail.getText().toString();

                ArrayList<String> inputList = new ArrayList<String>();
                inputList.add("&email=" + email);

                new inputAsyncData(getApplicationContext()).execute(inputList);
            }
        });
    }

    public class inputAsyncData extends AsyncTask<ArrayList<String>, Void, Integer> {

        Context context;

        public inputAsyncData(Context context) {
            this.context = context;
        }

        protected void onPreExecute()
        {
            resetButton.setEnabled(false);
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

        protected void onPostExecute(Integer result)
        {
            Log.w("Reset result", result.toString());

            if(result == 1)
            {
                resetButton.animFinish();
                Toast.makeText(getApplicationContext(), "Reset Successful" +
                        "\nPlease, check your inbox", Toast.LENGTH_LONG).show();
                View view = getWindow().getDecorView().getRootView();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                finish();
            }
            else if(result == 2)
            {
                resetButton.animError();
                Toast.makeText(getApplicationContext(), "Reset failed, please check your email", Toast.LENGTH_LONG).show();
            }

            else
            {
                resetButton.animError();
                Toast.makeText(getApplicationContext(), "Reset failed, technical error", Toast.LENGTH_LONG).show();
            }
            resetButton.setEnabled(true);
        }
    }

}
