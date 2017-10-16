package com.onthehouse.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.onthehouse.connection.APIConnection;
import com.onthehouse.details.Country;
import com.onthehouse.details.Member;
import com.onthehouse.details.Zone;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.xm.weidongjian.progressbuttonlib.ProgressButton;

/**
 * Created by ammar on 15/10/2017.
 */

public class UpdateProfile {


    EditText regEmail;
    EditText regPass;
    EditText regCPass;
    EditText regLName;
    EditText regFName;
    EditText regNickName;
    EditText regCountry;
    ArrayList<Zone> zoneList;
    ProgressButton registerBtn;
    Spinner regState;
    Country selectedCountry = new Country();
    String errorText = "";
    public ConstraintLayout layout;
    ArrayList<Country> countryList = new ArrayList<Country>();
    ArrayList<String> zoneNames = new ArrayList<String>();
    ArrayAdapter<String> stateAdapter = null;


//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
////
////        regEmail = (EditText) view.findViewById(R.id.regEmail);
////        regPass = (EditText) view.findViewById(R.id.regPassword);
////        regCPass = (EditText) view.findViewById(R.id.regConfirmPassword);
////        regLName = (EditText) view.findViewById(R.id.regLastName);
////        regFName = (EditText) view.findViewById(R.id.regFirstName);
////        regNickName = (EditText) view.findViewById(R.id.regNickName);
////        regCountry = (EditText) view.findViewById(R.id.regCountry);
////        registerBtn = (ProgressButton) view.findViewById(R.id.registerBtn);
////        regState = (Spinner) view.findViewById(R.id.stateSpinner);
////        layout = (ConstraintLayout) view.findViewById(R.id.registerLayout);
//
//        //SETTING DATA
//        setData();
//
//        //SETTING INPUT LIST
//
//        APIConnection connection = new APIConnection();
//        ArrayList<String> inputList = new ArrayList<String>();
//        inputList.add("&nickname="+"");
//        inputList.add("&first_name="+"");
//        inputList.add("&last_name="+"");
//        inputList.add("&zip=3000");
//        inputList.add("&zone_id="+"");
//        inputList.add("&country_id="+"");
//        inputList.add("&timezone_id=106");
//        inputList.add("&question_id=");
//        inputList.add("&question_text=");
//        inputList.add("&email="+"");
//        inputList.add("&password="+"");
//        inputList.add("&password_confirm="+"");
//        inputList.add("&terms=1");
//
//      //  new editAsyncData(mContext).execute(inputList);
//
//
//
//       // return view;
//    }


    private void setData() {
        regFName.setText(Member.getInstance().getFirst_name());
        regLName.setText(Member.getInstance().getLast_name());
        regNickName.setText(Member.getInstance().getNickname());
        regEmail.setText(Member.getInstance().getEmail());
        regPass.setText(Member.getInstance().getPassword());
        regCPass.setText(Member.getInstance().getPassword());
        regCountry.setVisibility(View.INVISIBLE);
        regState.setVisibility(View.INVISIBLE);

    }




    public class editAsyncData extends AsyncTask<ArrayList<String>, Void, Integer> {

        Context context;

        public editAsyncData(Context context) {
            this.context = context;
        }

        protected void onPreExecute()
        {
            registerBtn.setEnabled(false);
        }

        protected Integer doInBackground(ArrayList<String>... params)
        {
            int status = 0;

            APIConnection connection = new APIConnection();
            try
            {
                String output = connection.sendPost("/api/v1/member/create", params[0]);
                if (output.length() > 0)
                {
                    JSONObject obj = new JSONObject(output);
                    String result = obj.getString("status");
                    // JSONArray arr = obj.getJSONArray("member");

                    Log.w("REGISTRATION RESULT", result);

                    if (result.equals("success"))
                    {

//                        Member member = Member.getInstance();
//                        JSONObject jsonArray = obj.getJSONObject("member");
//                        setData(member, jsonArray);
//                        //1 = success;
//                        status = 1;
//                        Log.w("status", String.valueOf(status));
//                        Log.w("OBJECT TEST", member.getFirst_name());
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
                registerBtn.animFinish();
//                //Snackbar.make(layout, "Registration Successful.", Snackbar.LENGTH_LONG).show();
//                Toast.makeText(getActivity(), "Registration Successful.", Toast.LENGTH_LONG).show();
//
//                Intent registerDoneIntent = new Intent(context, MainMenu.class);
//                getActivity().startActivity(registerDoneIntent);

            }

            else if(result == 2)
            {
                Snackbar.make(layout, errorText, Snackbar.LENGTH_LONG).show();
                registerBtn.animError();
            }
            else
            {
                Snackbar.make(layout, "Registration failed, technical error.", Snackbar.LENGTH_LONG).show();
                registerBtn.animError();
            }
            registerBtn.setEnabled(true);
        }
    }


}
