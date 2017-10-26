package com.onthehouse.onthehouse;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.onthehouse.connection.APIConnection;
import com.onthehouse.details.Member;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.xm.weidongjian.progressbuttonlib.ProgressButton;

public class BookingPageCompetition extends AppCompatActivity {
    public TextView question;
    public EditText answer;
    public ConstraintLayout layout;
    String errorText = "";

    public ProgressButton submit_competition;
    private String eventId;
    private String memberId;
    private ArrayList<String> inputList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_page_competition);
        question = (TextView) findViewById(R.id.competition_question);
        answer = (EditText) findViewById(R.id.answer);
        submit_competition = (ProgressButton) findViewById(R.id.confirm_delivery);
        layout = (ConstraintLayout) findViewById(R.id.competeLayout);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        question.setText(extras.getString("question"));
        this.setEventId(extras.getString("eventID"));
        this.setMemberId(Integer.toString(Member.getInstance().getId()));

        submit_competition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //APIConnection connection = new APIConnection();
                //inputList.add("event_id="+getEventId());
                inputList.add("member_id="+getMemberId());
                inputList.add("&event_id="+getEventId());
                inputList.add("&competition_answer="+answer.getText().toString());
                new competeAsyncData(getApplicationContext()).execute(inputList);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setEventId(String eventId){this.eventId = eventId;}
    public String getEventId(){return this.eventId;}

    public void setMemberId(String memberId){this.memberId = memberId;}
    public String getMemberId(){return  this.memberId;}

    public class competeAsyncData extends AsyncTask<ArrayList<String>, Void, Integer>{
        Context context;

        public competeAsyncData(Context context){this.context = context;}

        protected void onPreExecute()
        {
            submit_competition.setEnabled(false);
        }

        protected Integer doInBackground(ArrayList<String>... params){
            int status = 0;

            APIConnection connection = new APIConnection();

            try{
                String output = connection.sendPost("/api/v1/competition/enter", params[0]);
                //String output = connection.sendPost("/api/v1/member/change-password", params[0]);
                if (output.length() > 0){
                    JSONObject object = new JSONObject(output);
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
                submit_competition.animFinish();
                Snackbar.make(layout, "Submitted Answer", Snackbar.LENGTH_LONG).show();
            }

            else if(result == 2)
            {
                Snackbar.make(layout, errorText, Snackbar.LENGTH_LONG).show();
                submit_competition.animError();
            }
            else
            {
                Snackbar.make(layout, "Submission failed, technical error.", Snackbar.LENGTH_LONG).show();
                submit_competition.animError();
            }
            submit_competition.setEnabled(true);
        }
    }
}