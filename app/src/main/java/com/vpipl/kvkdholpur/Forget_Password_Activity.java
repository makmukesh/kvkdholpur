package com.vpipl.kvkdholpur;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.textfield.TextInputEditText;

import com.vpipl.kvkdholpur.Utils.AppController;
import com.vpipl.kvkdholpur.Utils.AppUtils;
import com.vpipl.kvkdholpur.Utils.QueryUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Forget_Password_Activity extends AppCompatActivity {

    private static final String TAG = "Forget_Password_Activity";
    private TextInputEditText edtxt_mobileno, edtxt_otp;
    private String mobileno, Otp = "", user_otp = "", Password = "";

    private Button button_submit, btn_submit_forgot;
    TextView txt_back_to_login, txt_back_to_login2;
    LinearLayout forgot_request, forgot_otp;

    private String LoginType = "User";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget__password);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        forgot_request = findViewById(R.id.forgot_request);
        forgot_request = findViewById(R.id.forgot_request);
        forgot_otp = findViewById(R.id.forgot_otp);
        edtxt_mobileno = findViewById(R.id.edtxt_mobileno);
        txt_back_to_login = findViewById(R.id.txt_back_to_login);
        button_submit = findViewById(R.id.btn_forgot_password);


        edtxt_otp = findViewById(R.id.edtxt_otp);
        btn_submit_forgot = findViewById(R.id.btn_submit_forgot);
        txt_back_to_login2 = findViewById(R.id.txt_back_to_login2);

        forgot_request.setVisibility(View.VISIBLE);
        forgot_otp.setVisibility(View.GONE);

        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.hideKeyboardOnClick(Forget_Password_Activity.this, v);

                    ValidateData();
            }
        });

        txt_back_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Forget_Password_Activity.this, Login_Activity.class);
                intent.putExtra("SendToHome", true);
                startActivity(intent);
                finish();
            }
        });
        btn_submit_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateDataOTP();
            }
        });
        txt_back_to_login2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Forget_Password_Activity.this, Login_Activity.class);
                intent.putExtra("SendToHome", true);
                startActivity(intent);
                finish();
            }
        });
    }

    private void ValidateData() {
        mobileno = edtxt_mobileno.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(mobileno)) {
            AppUtils.alertDialog(Forget_Password_Activity.this, getResources().getString(R.string.error_required_user_id));
            focusView = edtxt_mobileno;
            cancel = true;
        } else if (!AppUtils.isValidMobileno(mobileno)) {
            AppUtils.alertDialog(Forget_Password_Activity.this, "Mobile Number Invalid");
            focusView = edtxt_mobileno;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            if (AppUtils.isNetworkAvailable(Forget_Password_Activity.this)) {

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        executeForgetRequest();
                    }
                };
                new Handler().postDelayed(runnable, 500);
            } else {
                AppUtils.alertDialog(Forget_Password_Activity.this, getResources().getString(R.string.txt_networkAlert));
            }
        }
    }

    private void ValidateDataOTP() {
        user_otp = edtxt_otp.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(user_otp)) {
            AppUtils.alertDialog(Forget_Password_Activity.this, "Enter OTP");
            focusView = edtxt_otp;
            cancel = true;
        } else if (!user_otp.equalsIgnoreCase(Otp)) {
            AppUtils.alertDialog(Forget_Password_Activity.this, "You enter wrong OTP");
            focusView = edtxt_otp;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            if (AppUtils.isNetworkAvailable(Forget_Password_Activity.this)) {

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        executeSendPasswordRequest();
                    }
                };
                new Handler().postDelayed(runnable, 500);
            } else {
                AppUtils.alertDialog(Forget_Password_Activity.this, getResources().getString(R.string.txt_networkAlert));
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Forget_Password_Activity.this, Login_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("SendToHome", true);
        startActivity(intent);
        finish();
    }

    private void executeForgetRequest() {
        try {
            if (AppUtils.isNetworkAvailable(Forget_Password_Activity.this)) {
                new AsyncTask<Void, Void, String>() {
                    protected void onPreExecute() {
                        AppUtils.showProgressDialog(Forget_Password_Activity.this);
                    }

                    @Override
                    protected String doInBackground(Void... params) {
                        String response = null;

                        List<NameValuePair> postParameters = new ArrayList<>();
                        try {
                            postParameters.add(new BasicNameValuePair("DeviceID", AppUtils.getDeviceID(Forget_Password_Activity.this)));
                            postParameters.add(new BasicNameValuePair("MobileNo", mobileno));
                            response = AppUtils.callWebServiceWithMultiParam(Forget_Password_Activity.this, postParameters, QueryUtils.methodToForgotPassword, TAG);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return response;
                    }

                    @Override
                    protected void onPostExecute(String resultData) {
                        try {
                            AppUtils.dismissProgressDialog();
                           JSONObject jobject = new JSONObject(resultData);
                            if (jobject.length() > 0) {
                                if (jobject.getString("Status").equalsIgnoreCase("True")) {
                                    JSONArray jsonArrayData = jobject.getJSONArray("Data");
                                    if (jsonArrayData.length() != 0) {
                                        forgot_request.setVisibility(View.GONE);
                                        forgot_otp.setVisibility(View.VISIBLE);
                                        Otp = jsonArrayData.getJSONObject(0).getString("OTP");
                                        Password = jsonArrayData.getJSONObject(0).getString("Password");
                                    } else {
                                        AppUtils.alertDialog(Forget_Password_Activity.this, jobject.getString("Message"));
                                    }
                                } else {
                                    AppUtils.alertDialog(Forget_Password_Activity.this, jobject.getString("Message"));
                                }
                            } else {
                                AppUtils.showExceptionDialog(Forget_Password_Activity.this);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            AppUtils.showExceptionDialog(Forget_Password_Activity.this);
                        }
                    }
                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            AppUtils.showExceptionDialog(Forget_Password_Activity.this);
        }
    }

    private void executeSendPasswordRequest() {
        try {
            if (AppUtils.isNetworkAvailable(Forget_Password_Activity.this)) {
                new AsyncTask<Void, Void, String>() {
                    protected void onPreExecute() {
                        AppUtils.showProgressDialog(Forget_Password_Activity.this);
                    }

                    @Override
                    protected String doInBackground(Void... params) {
                        String response = null;

                        List<NameValuePair> postParameters = new ArrayList<>();
                        try {
                            postParameters.add(new BasicNameValuePair("Mobile", mobileno));
                            postParameters.add(new BasicNameValuePair("Password", Password));
                            response = AppUtils.callWebServiceWithMultiParam(Forget_Password_Activity.this, postParameters, QueryUtils.methodToSendPassword, TAG);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return response;
                    }

                    @Override
                    protected void onPostExecute(String resultData) {
                        try {
                            AppUtils.dismissProgressDialog();
                            JSONObject jobject = new JSONObject(resultData);
                            if (jobject.length() > 0) {
                                if (jobject.getString("Status").equalsIgnoreCase("True")) {
                                    JSONArray jsonArrayData = jobject.getJSONArray("Data");
                                    if (jsonArrayData.length() != 0) {
                                        AppUtils.alertDialogWithFinish(Forget_Password_Activity.this, jobject.getString("Message"));
                                    } else {
                                        AppUtils.alertDialogWithFinish(Forget_Password_Activity.this, jobject.getString("Message"));
                                    }
                                } else {
                                    AppUtils.alertDialog(Forget_Password_Activity.this, jobject.getString("Message"));
                                }
                            } else {
                                AppUtils.showExceptionDialog(Forget_Password_Activity.this);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            AppUtils.showExceptionDialog(Forget_Password_Activity.this);
                        }
                    }
                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            AppUtils.showExceptionDialog(Forget_Password_Activity.this);
        }
    }

    private void ShowDialog(String message) {
        final Dialog dialog = AppUtils.createDialog(Forget_Password_Activity.this, true);
        TextView dialog4all_txt = dialog.findViewById(R.id.txt_DialogTitle);
        dialog4all_txt.setText(message);

        TextView textView = dialog.findViewById(R.id.txt_submit);
        textView.setText("Login");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                AppController.getSpUserInfo().edit().clear().commit();
                AppController.getSpIsLogin().edit().clear().commit();

                Intent intent = new Intent(Forget_Password_Activity.this, Login_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("SendToHome", true);
                startActivity(intent);
                finish();
            }
        });
        dialog.show();
    }
}
