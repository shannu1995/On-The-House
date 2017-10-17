package com.onthehouse.onthehouse;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.onthehouse.connection.APIConnection;
import com.onthehouse.details.Country;
import com.onthehouse.details.Member;
import com.onthehouse.details.UtilMethods;
import com.onthehouse.details.Zone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.xm.weidongjian.progressbuttonlib.ProgressButton;

public class EditMemberActivity extends AppCompatActivity
{

    Spinner titleSpinner;
    Spinner ageSpinner;
    TextView editNick;
    TextView editFName;
    TextView editSurname;
    TextView editEmail;
    TextView editPhone;
    TextView editAddress;
    TextView editCity;
    ProgressButton updateBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_member);

        titleSpinner = (Spinner) findViewById(R.id.editMemberTitleSpinner);
        ageSpinner = (Spinner) findViewById(R.id.editMemberAgeSpinner);
        editNick = (TextView) findViewById(R.id.EditMemberNickName);
        editFName = (TextView) findViewById(R.id.editMemberFirstName);
        editSurname = (TextView) findViewById(R.id.editMemberLastName);
        editEmail = (TextView) findViewById(R.id.editMemberEmail);
        editPhone = (TextView) findViewById(R.id.editMemberPhone);
        editAddress = (TextView) findViewById(R.id.EditMemberStreet);
        editCity = (TextView) findViewById(R.id.editMemberCity);
        updateBtn = (ProgressButton) findViewById(R.id.editUpdateBtn);

        editNick.setText(Member.getInstance().getNickname());
        editFName.setText(Member.getInstance().getFirst_name());
        editSurname.setText(Member.getInstance().getLast_name());
        editEmail.setText(Member.getInstance().getEmail());
        editPhone.setText(Member.getInstance().getPhone_number());
        editAddress.setText(Member.getInstance().getAddress1() + " "+Member.getInstance().getAddress2());
        editCity.setText(Member.getInstance().getCity());

    }


}
