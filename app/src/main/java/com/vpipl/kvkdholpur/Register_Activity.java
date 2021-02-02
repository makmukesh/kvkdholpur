package com.vpipl.kvkdholpur;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.vpipl.kvkdholpur.Utils.AppController;
import com.vpipl.kvkdholpur.Utils.AppUtils;
import com.vpipl.kvkdholpur.Utils.QueryUtils;
import com.vpipl.kvkdholpur.Utils.SPUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Register_Activity extends AppCompatActivity {

    private static final String TAG = "Register_Activity";
    Button button_register, button_otp_register;
    private EditText edtxt_otp, edtxt_name, edtxt_mobileno, edtxt_password_member;
    String userotp, OTP, name, mobileno, password;
    TextView txt_back_to_loginfirst,txt_back_to_loginsecond;
    LinearLayout layoutInput , ll_register_otp ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

            setContentView(R.layout.activity_register);
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            edtxt_otp = findViewById(R.id.edtxt_otp);
            edtxt_name = findViewById(R.id.edtxt_name);
            edtxt_mobileno = findViewById(R.id.edtxt_mobileno);
            edtxt_password_member = findViewById(R.id.edtxt_password_member);
            button_register = findViewById(R.id.button_register);
            button_otp_register = findViewById(R.id.button_otp_register);
            txt_back_to_loginfirst = findViewById(R.id.txt_back_to_loginfirst);
            txt_back_to_loginsecond = findViewById(R.id.txt_back_to_loginsecond);
            layoutInput = findViewById(R.id.layoutInput);
            ll_register_otp = findViewById(R.id.ll_register_otp);

            button_register.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppUtils.hideKeyboardOnClick(Register_Activity.this, v);
                        ValidateDataOTP();
                }
            });
            button_otp_register.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppUtils.hideKeyboardOnClick(Register_Activity.this, v);
                        ValidateData();
                }
            });

            edtxt_password_member.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == 1234 || id == EditorInfo.IME_NULL) {
                        AppUtils.hideKeyboardOnClick(Register_Activity.this, textView);
                        ValidateData();
                        return true;
                    }
                    return false;
                }
            });

            txt_back_to_loginfirst.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppUtils.hideKeyboardOnClick(Register_Activity.this, v);
                    startActivity(new Intent(Register_Activity.this, Login_Activity.class));
                    finish();
                }
            });

            txt_back_to_loginsecond.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppUtils.hideKeyboardOnClick(Register_Activity.this, v);
                    startActivity(new Intent(Register_Activity.this, Login_Activity.class));
                    finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    private void ValidateDataOTP() {
        edtxt_mobileno.setError(null);

        mobileno = edtxt_mobileno.getText().toString().trim();

        if (TextUtils.isEmpty(mobileno)) {
            AppUtils.alertDialog(Register_Activity.this, getResources().getString(R.string.error_required_user_id));
            edtxt_mobileno.requestFocus();
        } else if (!AppUtils.isValidMobileno(mobileno)) {
            AppUtils.alertDialog(Register_Activity.this, "Please Enter Valid Mobile No");
            edtxt_mobileno.requestFocus();
        } else {
            if (AppUtils.isNetworkAvailable(Register_Activity.this)) {
                executeRegisterOTPRequest(mobileno);
            } else {
                AppUtils.alertDialog(Register_Activity.this, getResources().getString(R.string.txt_networkAlert));
            }
        }
    }

    private void executeRegisterOTPRequest(final String mobileno) {
        try {

            if (AppUtils.isNetworkAvailable(Register_Activity.this)) {
                new AsyncTask<Void, Void, String>() {
                    protected void onPreExecute() {
                        AppUtils.showProgressDialog(Register_Activity.this);
                    }

                    @Override
                    protected String doInBackground(Void... params) {
                        String response = "";
                        try {
                            List<NameValuePair> postParameters = new ArrayList<>();
                            postParameters.add(new BasicNameValuePair("MobileNo", mobileno));
                            postParameters.add(new BasicNameValuePair("CompID", "" + AppController.getSpUserInfo().getString(SPUtils.Company_ID, "")));
                            response = AppUtils.callWebServiceWithMultiParam(Register_Activity.this, postParameters, QueryUtils.methodToSendRegstrationOTP, TAG);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return response;

                    }

                    @Override
                    protected void onPostExecute(String resultData) {
                        try {
                            AppUtils.dismissProgressDialog();
                            JSONObject jsonObject = new JSONObject(resultData);
                            if (jsonObject.getString("Status").equalsIgnoreCase("True")) {
                                JSONArray jsonArrayData = jsonObject.getJSONArray("Data");
                                if (jsonArrayData.length() != 0) {
                                    OTP = jsonArrayData.getJSONObject(0).getString("OTP");
                                    layoutInput.setVisibility(View.GONE);
                                    ll_register_otp.setVisibility(View.VISIBLE);
                                } else {
                                    AppUtils.alertDialog(Register_Activity.this, jsonObject.getString("Message"));
                                }
                            } else {
                                AppUtils.alertDialog(Register_Activity.this, jsonObject.getString("Message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            AppUtils.showExceptionDialog(Register_Activity.this);
                        }
                    }
                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            AppUtils.showExceptionDialog(Register_Activity.this);
        }
    }

    private void ValidateData() {
        OTP = "12345" ;
        edtxt_otp.setError(null);
        edtxt_name.setError(null);
        edtxt_password_member.setError(null);

        userotp = edtxt_otp.getText().toString().trim();
        name = edtxt_name.getText().toString().trim();
        password = edtxt_password_member.getText().toString().trim();

        if (TextUtils.isEmpty(userotp)) {
            AppUtils.alertDialog(Register_Activity.this, "Please Enter OTP");
            edtxt_otp.requestFocus();
        } else if (!userotp.equalsIgnoreCase(OTP)) {
            AppUtils.alertDialog(Register_Activity.this, "Wrong OTP Entered");
            edtxt_otp.requestFocus();
        } else if (TextUtils.isEmpty(name)) {
            AppUtils.alertDialog(Register_Activity.this, "Please Enter Your Name");
            edtxt_name.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            AppUtils.alertDialog(Register_Activity.this, getResources().getString(R.string.error_required_password));
            edtxt_password_member.requestFocus();
        } else {
            if (AppUtils.isNetworkAvailable(Register_Activity.this)) {
                executeRegisterSubmitRequest(name, mobileno, password);
            } else {
                AppUtils.alertDialog(Register_Activity.this, getResources().getString(R.string.txt_networkAlert));
            }
        }
    }

    private void executeRegisterSubmitRequest(final String name, final String userId, final String passwd) {
        try {

            if (AppUtils.isNetworkAvailable(Register_Activity.this)) {
                new AsyncTask<Void, Void, String>() {
                    protected void onPreExecute() {
                        AppUtils.showProgressDialog(Register_Activity.this);
                    }

                    @Override
                    protected String doInBackground(Void... params) {
                        String response = "";
                        try {
                            List<NameValuePair> postParameters = new ArrayList<>();
                            postParameters.add(new BasicNameValuePair("CustomerName", name));
                            postParameters.add(new BasicNameValuePair("MobileNo", userId));
                            postParameters.add(new BasicNameValuePair("Passowrd", passwd));
                            postParameters.add(new BasicNameValuePair("CompID", "" + AppController.getSpUserInfo().getString(SPUtils.Company_ID, "")));
                            postParameters.add(new BasicNameValuePair("DeviceID", "" + AppUtils.getDeviceID(Register_Activity.this)));
                            response = AppUtils.callWebServiceWithMultiParam(Register_Activity.this, postParameters, QueryUtils.methodToUser_Registration, TAG);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return response;
                    }

                    @Override
                    protected void onPostExecute(String resultData) {
                        try {
                            AppUtils.dismissProgressDialog();
                            JSONObject jsonObject = new JSONObject(resultData);
                            if (jsonObject.getString("Status").equalsIgnoreCase("True")) {
                                JSONArray jsonArrayData = jsonObject.getJSONArray("Data");
                                if (jsonArrayData.length() != 0) {
                                    //saveLoginUserInfo(jsonArrayData);
                                    AppUtils.alertDialogWithFinish(Register_Activity.this, jsonObject.getString("Message"));
                                } else {
                                    AppUtils.alertDialog(Register_Activity.this, jsonObject.getString("Message"));
                                }
                            } else {
                                AppUtils.alertDialog(Register_Activity.this, jsonObject.getString("Message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            AppUtils.showExceptionDialog(Register_Activity.this);
                        }
                    }
                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            AppUtils.showExceptionDialog(Register_Activity.this);
        }
    }

    private void saveLoginUserInfo(final JSONArray jsonArray) {
        try {

            AppController.getSpUserInfo().edit().clear().commit();

            AppController.getSpUserInfo().edit()
                    .putString(SPUtils.USER_TYPE, "Farmer")
                    .putString(SPUtils.Company_ID, "1")
                    .putString(SPUtils.Member_ID, jsonArray.getJSONObject(0).getString("LID"))
                    .putString(SPUtils.MemberMobileNo, jsonArray.getJSONObject(0).getString("MobileNo"))
                    .putString(SPUtils.USER_FORM_NUMBER, jsonArray.getJSONObject(0).getString("Password"))
                    .putString(SPUtils.DeviceToken, jsonArray.getJSONObject(0).getString("DeviceID"))
                    .putString(SPUtils.MemberActiveStatus, jsonArray.getJSONObject(0).getString("ActiveStatus"))
                    .putString(SPUtils.MemberDOJ, AppUtils.getDateFromAPIDate(jsonArray.getJSONObject(0).getString("RTS")))
                    .commit();

            startSplash(new Intent(Register_Activity.this, MainActivity.class));

            AppController.getSpIsLogin().edit().putBoolean(SPUtils.IS_LOGIN, true).commit();

        } catch (Exception e) {
            e.printStackTrace();
            AppUtils.showExceptionDialog(Register_Activity.this);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case android.R.id.home:
                    finish();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            AppUtils.showExceptionDialog(Register_Activity.this);
        }
        return super.onOptionsItemSelected(item);
    }

    private void startSplash(final Intent intent) {
        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}