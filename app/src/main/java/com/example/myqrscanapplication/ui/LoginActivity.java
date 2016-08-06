package com.example.myqrscanapplication.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.myqrscanapplication.MyApplication;
import com.example.myqrscanapplication.R;
import com.example.myqrscanapplication.ScanActivity;
import com.example.myqrscanapplication.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yatra on 16/4/16.
 */
public class LoginActivity extends AppCompatActivity {


    private static final String LOGIN_URL = "";
    private Context mContext;

    private EditText mNameEditText;
    private EditText mEmailEditText;
    private EditText mNumberEditText;
    private TextInputLayout mNameLayout;
    private TextInputLayout mEmailLayout;
    private TextInputLayout mNumberLayout;
    private ProgressBar mProgressBar;
    private Button mSubmitButton;

    @Override
    protected void onCreate(Bundle savedInstancestate) {
        super.onCreate(savedInstancestate);
        setContentView(R.layout.login_ui);
        mContext = this;
        boolean isLoggedIn = SharedPreference.getBoolean(mContext,SharedPreference.LOGIN_CHECK_KEY,false);
        if(isLoggedIn) {
            Intent intent = new Intent(this, ScanActivity.class);
            startActivity(intent);
            finish();
        }
        initUI();
    }

    private void initUI() {
        mNameEditText = (EditText) findViewById(R.id.name_edit_text);
        mEmailEditText = (EditText) findViewById(R.id.email_edit_text);
        mNumberEditText = (EditText) findViewById(R.id.number_edit_text);
        mProgressBar = (ProgressBar) findViewById(R.id.register_progress_bar);
        mNameLayout = (TextInputLayout) findViewById(R.id.name_edit_layout);
        mEmailLayout = (TextInputLayout) findViewById(R.id.email_edit_layout);
        mNumberLayout = (TextInputLayout) findViewById(R.id.number_edit_layout);

        mSubmitButton = (Button) findViewById(R.id.submit_button);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mNameEditText.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    mNameLayout.setError("Compulsory field");
                    return;
                }
                String email = mEmailEditText.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    mEmailLayout.setError("Compulsory field");
                    return;
                }
                String number = mNumberEditText.getText().toString().trim();
                if (TextUtils.isEmpty(number)) {
                    mNumberLayout.setError("Compulsory field");
                    return;
                }
                mProgressBar.setVisibility(View.VISIBLE);
                mSubmitButton.setEnabled(false);
                register(name, number, email);
            }
        });
    }

    private void saveUserDetails(String name,String number,String email) {
        SharedPreference.setString(mContext,SharedPreference.USER_NAME,name);
        SharedPreference.setString(mContext,SharedPreference.USER_NUMBER,number);
        SharedPreference.setString(mContext,SharedPreference.USER_EMAIL,email);
    }

    private void register(final String name,final String number,final String email) {

        saveUserDetails(name,number,email);

        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://52.33.238.16:8002/grocshop/login/",
                /*null,*/ new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                {'success': True, 'reason': 'saved the data'}
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.getBoolean("success")) {
                        SharedPreference.setBoolean(mContext,SharedPreference.LOGIN_CHECK_KEY,true);
                        Intent intent = new Intent(mContext, ScanActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        mSubmitButton.setEnabled(true);
                        Toast.makeText(mContext, obj.getString("reason") + "", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                mProgressBar.setVisibility(View.INVISIBLE);
                mSubmitButton.setEnabled(true);
                Toast.makeText(mContext, "Connection Error", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String,String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("name",name);
                params.put("email",email);
                params.put("phone",number);
                return params;
            }
        };
        postRequest.setTag(this);
        postRequest.setRetryPolicy(new DefaultRetryPolicy(30000,2,0));
        MyApplication.getInstance().addToRequestQueue(postRequest);
    }

}
