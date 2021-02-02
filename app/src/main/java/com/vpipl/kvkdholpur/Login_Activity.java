package com.vpipl.kvkdholpur;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.textfield.TextInputEditText;

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

public class Login_Activity extends AppCompatActivity {

    private static final String TAG = "Login_Register_Activity";
    boolean accepet_conditions = false;
    Button button_login , button_sign_up;
    TextView txt_forgot_password;
    CheckBox cb_login_rememberMe;
    private TextView txt_terms_conditions, txt_show_password;
    private CheckBox cb_accept;
    LinearLayout ll_terms_and_condition;
    RelativeLayout root_layout;
    private TextInputEditText edtxt_mobileno, edtxt_password_member;

    LinearLayout ll_login, ll_terms_condition_show;
    String mobileno;
    String password;
    TextView txt_sign_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

            setContentView(R.layout.activity_login);
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            root_layout = findViewById(R.id.root_layout);

            edtxt_mobileno = findViewById(R.id.edtxt_mobileno);
            edtxt_password_member = findViewById(R.id.edtxt_password_member);

            button_login = findViewById(R.id.button_login);
            button_sign_up = findViewById(R.id.button_sign_up);

            cb_login_rememberMe = findViewById(R.id.cb_login_rememberMe);

            txt_forgot_password = findViewById(R.id.txt_forgot_password);

            ll_terms_and_condition = findViewById(R.id.ll_terms_and_condition);
            cb_accept = findViewById(R.id.cb_accept);
            txt_terms_conditions = findViewById(R.id.txt_terms_conditions);
            txt_show_password = findViewById(R.id.txt_show_password);

            AppController.getSpUserInfo().edit().putString(SPUtils.Company_ID, "1").commit();

            cb_accept.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    accepet_conditions = b;

                    if (cb_accept.isChecked()) {
                        button_login.setVisibility(View.VISIBLE);
                    } else {
                        button_login.setVisibility(View.GONE);
                    }
                }
            });
            txt_show_password.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (txt_show_password.getText().toString().equals("Show Password")) {
                        edtxt_password_member.setTransformationMethod(null);
                        txt_show_password.setText("Hide Password");
                    } else {
                        edtxt_password_member.setTransformationMethod(new PasswordTransformationMethod());
                        txt_show_password.setText("Show Password");
                    }
                }
            });
            button_login.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppUtils.hideKeyboardOnClick(Login_Activity.this, v);
                    ValidateData();
                }
            });

            button_sign_up.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppUtils.hideKeyboardOnClick(Login_Activity.this, v);
                    startActivity(new Intent(Login_Activity.this , Register_Activity.class));
                }
            });

            edtxt_password_member.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == 1234 || id == EditorInfo.IME_NULL) {
                        AppUtils.hideKeyboardOnClick(Login_Activity.this, textView);
                        ValidateData();
                        return true;
                    }
                    return false;
                }
            });

            txt_forgot_password.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppUtils.hideKeyboardOnClick(Login_Activity.this, v);
                    startActivity(new Intent(Login_Activity.this, Forget_Password_Activity.class));
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

    private void ValidateData() {
        edtxt_mobileno.setError(null);
        edtxt_password_member.setError(null);

        mobileno = edtxt_mobileno.getText().toString().trim();
        password = edtxt_password_member.getText().toString().trim();

        if (TextUtils.isEmpty(mobileno)) {
            AppUtils.alertDialog(Login_Activity.this, getResources().getString(R.string.error_required_user_id));
            edtxt_mobileno.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            AppUtils.alertDialog(Login_Activity.this, getResources().getString(R.string.error_required_password));
            edtxt_password_member.requestFocus();
        } else {
            if (AppUtils.isNetworkAvailable(Login_Activity.this)) {
                executeLoginRequest(mobileno, password);
            } else {
                AppUtils.alertDialog(Login_Activity.this, getResources().getString(R.string.txt_networkAlert));
            }
        }
    }

    private void executeLoginRequest(final String userId, final String passwd) {
        try {

            if (AppUtils.isNetworkAvailable(Login_Activity.this)) {
                new AsyncTask<Void, Void, String>() {
                    protected void onPreExecute() {
                        AppUtils.showProgressDialog(Login_Activity.this);
                    }

                    @Override
                    protected String doInBackground(Void... params) {
                        String response = "";
                        try {
                            List<NameValuePair> postParameters = new ArrayList<>();
                            postParameters.add(new BasicNameValuePair("MobileNo", userId));
                            postParameters.add(new BasicNameValuePair("Passowrd", passwd));
                            postParameters.add(new BasicNameValuePair("CompID", "" + AppController.getSpUserInfo().getString(SPUtils.Company_ID, "")));
                            postParameters.add(new BasicNameValuePair("DeviceID", "" + AppUtils.getDeviceID(Login_Activity.this)));
                            response = AppUtils.callWebServiceWithMultiParam(Login_Activity.this, postParameters, QueryUtils.methodToUser_Login, TAG);
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
                                    saveLoginUserInfo(jsonArrayData);
                                } else {
                                    AppUtils.alertDialog(Login_Activity.this, jsonObject.getString("Message"));
                                }
                            } else {
                                AppUtils.alertDialog(Login_Activity.this, jsonObject.getString("Message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            AppUtils.showExceptionDialog(Login_Activity.this);
                        }
                    }
                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            AppUtils.showExceptionDialog(Login_Activity.this);
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

            startSplash(new Intent(Login_Activity.this, MainActivity.class));

            AppController.getSpIsLogin().edit().putBoolean(SPUtils.IS_LOGIN, true).commit();

        } catch (Exception e) {
            e.printStackTrace();
            AppUtils.showExceptionDialog(Login_Activity.this);
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
            AppUtils.showExceptionDialog(Login_Activity.this);
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