package com.vpipl.kvkdholpur;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;


import com.vpipl.kvkdholpur.Utils.AppUtils;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class SplashActivity extends Activity {

    String currentVersion, latestVersion;
    private static final String TAG = "Splash_Activity";

    private static int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        if (AppUtils.isNetworkAvailable(SplashActivity.this)) {
            getCurrentVersionnew();
        } else {
            AppUtils.alertDialogWithFinish(SplashActivity.this, getResources().getString(R.string.txt_networkAlert));
        }
    }
    private void getCurrentVersionnew() {
        PackageManager pm = this.getPackageManager();
        PackageInfo pInfo = null;

        try {
            pInfo = pm.getPackageInfo(this.getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        currentVersion = pInfo.versionName;

        new GetLatestVersionnew().execute();

    }

    private class GetLatestVersionnew extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {

                Document doc = Jsoup.connect("https://play.google.com/store/apps/details?id=" + getPackageName()).get();
                latestVersion = doc.getElementsByClass("htlgb").get(6).text();
            } catch (Exception e) {
                e.printStackTrace();
                latestVersion = currentVersion ;
            }

            return new JSONObject();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (latestVersion != null) {
                if (!currentVersion.equalsIgnoreCase(latestVersion)) {
                    if (!isFinishing()) {
                        showUpdateDialog();
                    }
                } else {
                    try {
                       /* if (AppController.getSpIsLogin().getBoolean(SPUtils.IS_LOGIN, false)) {
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        } else {
                            startActivity(new Intent(SplashActivity.this, Login_Activity.class));
                        }*/
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                        AppUtils.showExceptionDialog(SplashActivity.this);
                    }
                }
            } else
                //   background.start();
                super.onPostExecute(jsonObject);
        }
    }

    private void showUpdateDialog() {
        final Dialog dialog = new Dialog(SplashActivity.this, R.style.ThemeDialogCustom);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog_update);

        TextView dialog4all_txt = dialog.findViewById(R.id.tvDescription);
        Button btnNone = dialog.findViewById(R.id.btnNone);
        ImageView iv_update_image = dialog.findViewById(R.id.iv_update_image);
        dialog4all_txt.setText("An Update is available,Please Update App from Play Store.");


        btnNone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                dialog.dismiss();
                finish();
            }
        });
        dialog.show();
    }

    private void showDialogOK(DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage("These Permissions are required for use this Application")
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }
}
