package com.vpipl.kvkdholpur;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.textfield.TextInputEditText;
import com.vpipl.kvkdholpur.Utils.AppController;
import com.vpipl.kvkdholpur.Utils.AppUtils;
import com.vpipl.kvkdholpur.Utils.SPUtils;

public class Login_Local_Activity extends AppCompatActivity {

    private static final String TAG = "Login_Register_Activity";
    Button button_login_local;
    private TextInputEditText edtxt_mobileno, edtxt_name;
    TextView txt_go_home;

    String mobileno;
    String name;
    String entry_type = "Q";
    Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            act = Login_Local_Activity.this;
            setContentView(R.layout.activity_login_local);
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            edtxt_mobileno = findViewById(R.id.edtxt_mobileno);
            edtxt_name = findViewById(R.id.edtxt_name);

            button_login_local = findViewById(R.id.button_login_local);
            txt_go_home = findViewById(R.id.txt_go_home);

            AppController.getSpUserInfo().edit().putString(SPUtils.Company_ID, "1").commit();

            entry_type = getIntent().getStringExtra("Entry_type");

            button_login_local.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ValidateData();
                }
            });
            txt_go_home.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(act, MainActivity.class));
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
        edtxt_name.setError(null);

        mobileno = edtxt_mobileno.getText().toString().trim();
        name = edtxt_name.getText().toString().trim();

        if (TextUtils.isEmpty(mobileno)) {
            AppUtils.alertDialog(act, "Please Enter Mobile No. / कृपया मोबाइल नंबर दर्ज करें");
            edtxt_mobileno.requestFocus();
        } else if (TextUtils.isEmpty(name)) {
            AppUtils.alertDialog(act, "Please Enter Name / कृपया नाम दर्ज करें");
            edtxt_name.requestFocus();
        } else {
            saveLoginUserInfo();
        }
    }

    private void saveLoginUserInfo() {
        try {
            AppController.getSpUserInfo().edit().clear().commit();

            edtxt_mobileno.setText("");
            edtxt_name.setText("");

            AppController.getSpUserInfo().edit()
                    .putString(SPUtils.USER_TYPE, "Farmer")
                    .putString(SPUtils.Company_ID, "1")
                    .putString(SPUtils.MemberMobileNo, mobileno)
                    .putString(SPUtils.MemberName, name)
                    .commit();

            if (entry_type.equalsIgnoreCase("Q")) {
                startSplash(new Intent(act, QueryActivity.class));
                finish();
            } else if (entry_type.equalsIgnoreCase("QS")) {
                startSplash(new Intent(act, QuerySolutionListActivity.class));
                finish();
            } else {
                startSplash(new Intent(act, MainActivity.class));
            }

            AppController.getSpIsLogin().edit().putBoolean(SPUtils.IS_LOGIN, true).commit();
        } catch (Exception e) {
            e.printStackTrace();
            AppUtils.showExceptionDialog(act);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(act, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

        super.onBackPressed();
    }

    private void startSplash(final Intent intent) {
        try {
            //  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}