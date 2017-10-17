package com.onthehouse.onthehouse;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.onthehouse.connection.APIConnection;
import com.onthehouse.details.Country;
import com.onthehouse.details.Member;
import com.onthehouse.details.UtilMethods;
import com.onthehouse.details.Zone;
import com.onthehouse.fragments.RegisterActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.xm.weidongjian.progressbuttonlib.ProgressButton;

public class EditMemberActivity extends Fragment
{

    Spinner titleSpinner;
    Spinner ageSpinner;
    EditText editNick;
    EditText editFName;
    EditText editSurname;
    EditText editEmail;
    EditText editPhone;
    EditText editAddress;
    EditText editCity;
    ProgressButton updateBtn;
    ConstraintLayout layout;
    String errorText = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.activity_edit_member, container, false);
        final Context mContext = container.getContext();

        titleSpinner = (Spinner) view.findViewById(R.id.editMemberTitleSpinner);
        ageSpinner = (Spinner) view.findViewById(R.id.editMemberAgeSpinner);
        editNick = (EditText) view.findViewById(R.id.EditMemberNickName);
        editFName = (EditText) view.findViewById(R.id.editMemberFirstName);
        editSurname = (EditText) view.findViewById(R.id.editMemberLastName);
        editEmail = (EditText) view.findViewById(R.id.editMemberEmail);
        editPhone = (EditText) view.findViewById(R.id.editMemberPhone);
        editAddress = (EditText) view.findViewById(R.id.EditMemberStreet);
        editCity = (EditText) view.findViewById(R.id.editMemberCity);
        updateBtn = (ProgressButton) view.findViewById(R.id.editUpdateBtn);
        layout = (ConstraintLayout) view.findViewById(R.id.edit_Layout);

        editNick.setText(Member.getInstance().getNickname());
        editFName.setText(Member.getInstance().getFirst_name());
        editSurname.setText(Member.getInstance().getLast_name());
        editEmail.setText(Member.getInstance().getEmail());
        editPhone.setText(Member.getInstance().getPhone_number());
        editAddress.setText(Member.getInstance().getAddress1() + " "+Member.getInstance().getAddress2());
        editCity.setText(Member.getInstance().getCity());

        if(!Member.getInstance().getAge().isEmpty())
        {
            String age = Member.getInstance().getAge();
            int ageIndex = 0;
            String[] ageArray = getResources().getStringArray(R.array.age_array);
            for(int i=0; i<ageArray.length; i++)
            {
                if(ageArray[i].equals(age))
                {
                    ageIndex = i;
                    break;
                }
            }

            ageSpinner.setSelection(ageIndex);
        }

        if(!Member.getInstance().getTitle().isEmpty())
        {
            String title = Member.getInstance().getTitle();
            int titleIndex = 0;
            String[] titleArray = getResources().getStringArray(R.array.title_array);
            for(int i=0; i<titleArray.length; i++)
            {
                if(titleArray[i].equals(title))
                {
                    titleIndex = i;
                    break;
                }
            }

            titleSpinner.setSelection(titleIndex);
        }

        updateBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                updateBtn.startRotate();

                String email = editEmail.getText().toString();
                String phone = editPhone.getText().toString();
                String address = editAddress.getText().toString();
                String lastName = editSurname.getText().toString();
                String firstName = editFName.getText().toString();
                String nickName = editNick.getText().toString();
                String city = editCity.getText().toString();
                int countryId = Member.getInstance().getCountry_id();
                int zone_id = Member.getInstance().getZone_id();
                int zip = Member.getInstance().getZip_code();
                int timezone = Member.getInstance().getTimezone_id();
                int newsLetter = 1;
                int focusGroup = 0;
                int paidMarket = 0;

                String title = titleSpinner.getSelectedItem().toString();
                if(title.startsWith("Select"))
                {
                    title = "";
                }
                String age = ageSpinner.getSelectedItem().toString();
                if(age.startsWith("Select"))
                {
                    age = "";
                }

                APIConnection connection = new APIConnection();
                ArrayList<String> inputList = new ArrayList<String>();
                inputList.add("&nickname="+nickName);
                inputList.add("&title="+title);
                inputList.add("&first_name="+firstName);
                inputList.add("&last_name="+lastName);
                inputList.add("&age="+age);
                inputList.add("&email="+email);
                inputList.add("&phone="+phone);
                inputList.add("&language_id=");
                inputList.add("&address1="+address);
                inputList.add("&city="+city);
                inputList.add("&zone_id="+zone_id);
                inputList.add("&zip="+zip);
                inputList.add("&country_id="+countryId);
                inputList.add("&timezone_id="+timezone);
                inputList.add("&newsletters="+newsLetter);
                inputList.add("&focus_groups="+focusGroup);
                inputList.add("&paid_marketing="+paidMarket);
                inputList.add("&categories=");

                new registerAsyncData(mContext).execute(inputList);
            }
        });

        return view;
    }

    public class registerAsyncData extends AsyncTask<ArrayList<String>, Void, Integer> {

        Context context;

        public registerAsyncData(Context context) {
            this.context = context;
        }

        protected void onPreExecute()
        {
            updateBtn.setEnabled(false);
        }

        protected Integer doInBackground(ArrayList<String>... params)
        {
            int status = 0;

            APIConnection connection = new APIConnection();
            try
            {
                String output = connection.sendPost(("/api/v1/member/update-account/"+Member.getInstance().getId()), params[0]);
                if (output.length() > 0)
                {
                    JSONObject obj = new JSONObject(output);
                    String result = obj.getString("status");
                    // JSONArray arr = obj.getJSONArray("member");

                    Log.w("UPDATE RESULT", result);

                    if (result.equals("success"))
                    {
                        Member member = Member.getInstance();
                        JSONObject jsonArray = obj.getJSONObject("member");
                        setData(member, jsonArray);
                        //1 = success;
                        status = 1;
                        Log.w("status", String.valueOf(status));
                        Log.w("OBJECT TEST", member.getFirst_name());
                    }

                    else
                    {
                        //2 = wrong details;
                        status = 2;

                        JSONObject jsonArray = obj.getJSONObject("error");
                        JSONArray errorArr=  jsonArray.getJSONArray("messages");
                        errorText = errorArr.getString(0);
                        Log.w("Registration error", errorText);
                        //2 = wrong details;
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
            if(result == 1)
            {
                updateBtn.animFinish();
                //Snackbar.make(layout, "Registration Successful.", Snackbar.LENGTH_LONG).show();
                Toast.makeText(getActivity(), "Update Details Successful.", Toast.LENGTH_LONG).show();

                //Intent registerDoneIntent = new Intent(context, MainMenu.class);
                //getActivity().startActivity(registerDoneIntent);

            }

            else if(result == 2)
            {
                Snackbar.make(layout, errorText, Snackbar.LENGTH_LONG).show();
                updateBtn.animError();
            }
            else
            {
                Snackbar.make(layout, "Update failed, technical error.", Snackbar.LENGTH_LONG).show();
                updateBtn.animError();
            }
            updateBtn.setEnabled(true);
        }
    }


    public void setData(Member member, JSONObject jsonArray)
    {
        try
        {
            member.setId(UtilMethods.tryParseInt(jsonArray.getString("id")));
            member.setTitle(jsonArray.getString("title"));
            member.setFirst_name(jsonArray.getString("first_name"));
            member.setLast_name(jsonArray.getString("last_name"));
            member.setEmail(jsonArray.getString("email"));
            member.setPassword(jsonArray.getString("password"));
            member.setDate_logged_in(jsonArray.getInt("date_logged_in"));
            member.setStatus(UtilMethods.tryParseInt(jsonArray.getString("status")));
            member.setPhone_number(jsonArray.getString("phone"));
            member.setAddress1(jsonArray.getString("address1"));
            member.setAddress2(jsonArray.getString("address2"));
            member.setCity(jsonArray.getString("city"));
            member.setZone_id(UtilMethods.tryParseInt(jsonArray.getString("zone_id")));
            member.setZip_code(UtilMethods.tryParseInt(jsonArray.getString("zip")));
            member.setCountry_id(UtilMethods.tryParseInt(jsonArray.getString("country_id")));
            member.setAge(jsonArray.getString("age"));
            member.setLanguage_id(jsonArray.getString("language_id"));
            member.setTimezone_id(UtilMethods.tryParseInt(jsonArray.getString("timezone_id")));
            member.setMembership_level_id(UtilMethods.tryParseInt(jsonArray.getString("membership_level_id")));
            member.setMembership_expiry(UtilMethods.tryParseInt(jsonArray.getString("membership_expiry")));
            member.setMembership_id(UtilMethods.tryParseInt(jsonArray.getString("membership_id")));
            member.setMembership_expiry_email(UtilMethods.tryParseInt(jsonArray.getString("membership_expiry_email")));
            member.setNewsletters(UtilMethods.tryParseInt(jsonArray.getString("newsletters")));
            member.setFocus_groups(UtilMethods.tryParseInt(jsonArray.getString("focus_groups")));
            member.setPaid_marketing(UtilMethods.tryParseInt(jsonArray.getString("paid_marketing")));
            member.setGoogle_calender(UtilMethods.tryParseInt(jsonArray.getString("google_calendar")));
            member.setCategories(jsonArray.getString("categories"));
            member.setNickname(jsonArray.getString("nickname"));
            member.setImage(jsonArray.getString("image"));
            member.setGc_access_token(jsonArray.getString("gc_access_token"));
            member.setFoundation_member(UtilMethods.tryParseInt(jsonArray.getString("foundation_member")));
            member.setOauth_uid(UtilMethods.tryParseInt(jsonArray.getString("oauth_uid")));
            member.setOauth_provider(jsonArray.getString("oauth_provider"));
            member.setFoundation_member(UtilMethods.tryParseInt(jsonArray.getString("limit_num_reservations")));
        }
        catch (Exception e) {

        }
    }
}
