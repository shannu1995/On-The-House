package com.onthehouse.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.onthehouse.Utils.DrawerLocker;
import com.onthehouse.connection.APIConnection;
import com.onthehouse.details.Member;
import com.onthehouse.onthehouse.MainMenu;
import com.onthehouse.onthehouse.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.xm.weidongjian.progressbuttonlib.ProgressButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePasswordFragment extends Fragment {

    public ProgressButton confirmBtn;
    public EditText newPassword;
    public EditText confirmPassword;
    public String errorText = "";
    public ConstraintLayout layout;

    private ArrayList<String> inputList = new ArrayList<String>();
    public ChangePasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        final Context mContext = container.getContext();

        confirmBtn = view.findViewById(R.id.btn_changePassword);

        newPassword = view.findViewById(R.id.editText);
        confirmPassword = view.findViewById(R.id.editText3);
        layout = view.findViewById(R.id.passwordChangeFragment);


        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            inputList.add("&member_id="+ Member.getInstance().getId());
                inputList.add("&password=" + newPassword.getText().toString().trim());
                inputList.add("&password_confirm=" + confirmPassword.getText().toString().trim());
            new changePasswordAsyncData(mContext).execute(inputList);
            }
        });

        return view;
    }
    public class changePasswordAsyncData extends AsyncTask<ArrayList<String>, Void, Integer> {
        Context context;
        APIConnection connection = new APIConnection();
        JSONObject object = new JSONObject();

        public changePasswordAsyncData(Context context){this.context = context;}

        protected void onPreExecute()
        {
            confirmBtn.setEnabled(false);
            ((DrawerLocker) getActivity()).setDrawerEnabled(false);
        }

        protected Integer doInBackground(ArrayList<String>... params){
            int status = 0;

            try{
                String output = connection.sendPost("/api/v1/member/change-password", params[0]);
                if (output.length() > 0){
                    object = new JSONObject(output);
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
                //Update preferences for auto login
                SharedPreferences sharedPreferences = context.getSharedPreferences("memberInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("memberPass", confirmPassword.getText().toString());
                Member.getInstance().setPassword(confirmPassword.getText().toString());
                editor.apply();
                confirmBtn.animFinish();
                //Show Message
                Toast.makeText(context, "Password Updated", Toast.LENGTH_LONG).show();
                //Return to offers page
                ((MainMenu) context).onBackPressed();
            }
            else if(result == 2)
            {
                Toast.makeText(context, errorText, Toast.LENGTH_LONG).show();
                confirmBtn.animError();
            }
            else
            {
                Toast.makeText(context, "Submission failed, technical error", Toast.LENGTH_LONG).show();
                confirmBtn.animError();
            }
            confirmBtn.setEnabled(true);
            ((DrawerLocker) getActivity()).setDrawerEnabled(true);
        }
    }
}
