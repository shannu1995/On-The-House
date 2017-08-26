package com.onthehouse.onthehouse;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    @Override
        protected void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            if (android.os.Build.VERSION.SDK_INT > 9)
            {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            final EditText email = (EditText) findViewById(R.id.loginEmail);
            final EditText password = (EditText) findViewById(R.id.loginPassword);
            final Button loginButton= (Button)findViewById(R.id.loginButton);

            final TextView signUp = (TextView) findViewById(R.id.signup);
            final TextView skip = (TextView) findViewById(R.id.skip);
            final TextView reset = (TextView) findViewById(R.id.resetPassword);


            loginButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    String emailStr = email.getText().toString();
                    String passStr = password.getText().toString();

                    APIConnection connection = new APIConnection();
                    ArrayList<String> inputList = new ArrayList<String>();
                    inputList.add("email="+emailStr);
                    inputList.add("password="+passStr);
                    try
                    {
                        StringBuffer output = connection.sendPost("/api/v1/member/login", inputList);
                        Log.w("LOGIN OUTPUT",output.toString());
                        if(output.length()>0)
                            Toast.makeText(getApplicationContext(), output.toString(), Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getApplicationContext(), "NULL", Toast.LENGTH_LONG).show();

                    }
                    catch(Exception e)
                    {
                        Log.e("OTH Error", "I got an error", e);
                        Toast.makeText(getApplicationContext(), "Technical Error, contact support", Toast.LENGTH_LONG).show();
                    }
                }
            });

            signUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                    LoginActivity.this.startActivity(registerIntent);
                }
            });
            skip.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent offerIntent = new Intent(LoginActivity.this, OffersList.class);
                    LoginActivity.this.startActivity(offerIntent);
                }
            });
        }
}
