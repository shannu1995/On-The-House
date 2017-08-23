package com.onthehouse.onthehouse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText email = (EditText) findViewById(R.id.email);
        final EditText password = (EditText) findViewById(R.id.password);
        final Button loginButton= (Button)findViewById(R.id.login_button);

        final TextView register = (TextView) findViewById(R.id.signup);
        final TextView skip = (TextView) findViewById(R.id.skip);
        final TextView reset = (TextView) findViewById(R.id.resetPassword);

        register.setOnClickListener(new View.OnClickListener() {
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
