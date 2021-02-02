package com.vpipl.kvkdholpur;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vpipl.kvkdholpur.Adapter.Tranining_List_Adapter;

import com.vpipl.kvkdholpur.Utils.AppController;
import com.vpipl.kvkdholpur.Utils.AppUtils;
import com.vpipl.kvkdholpur.Utils.QueryUtils;
import com.vpipl.kvkdholpur.Utils.SPUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class Tranning_ListActivity extends AppCompatActivity {

    Activity act;

    ImageView img_nav_back;

    TextView heading;

    public void SetupToolbar() {

        img_nav_back = findViewById(R.id.img_nav_back);
        heading = findViewById(R.id.heading);
        heading.setText("Training Scheduling ");

        img_nav_back = findViewById(R.id.img_nav_back);

        img_nav_back.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
        img_nav_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImageView img_nav_logout = findViewById(R.id.img_nav_logout);

        img_nav_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.showDialogSignOut(act);
            }
        });
    }

    RecyclerView recyclerView;
    LinearLayout ll_data_found, ll_no_data_found;
    Tranining_List_Adapter adapter;
    public static ArrayList<HashMap<String, String>> arraylist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_list);

        act = Tranning_ListActivity.this;

        Toolbar toolbar = findViewById(R.id.toolbar);
        SetupToolbar();

        recyclerView = (RecyclerView) findViewById(R.id.listView);
        ll_data_found  =  findViewById(R.id.ll_data_found);
        ll_no_data_found  =  findViewById(R.id.ll_no_data_found);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (AppUtils.isNetworkAvailable(act)) {
            executeTrainingListRequest();
        } else {
            AppUtils.alertDialogWithFinish(act, getResources().getString(R.string.txt_networkAlert));
        }

    }

    private void executeTrainingListRequest() {
        try {
            if (AppUtils.isNetworkAvailable(act)) {
                new AsyncTask<Void, Void, String>() {
                    protected void onPreExecute() {
                        AppUtils.showProgressDialog(act);
                    }

                    @Override
                    protected String doInBackground(Void... params) {
                        String response = "";
                        try {
                            List<NameValuePair> postParameters = new ArrayList<>();
                            postParameters.add(new BasicNameValuePair("CompID", "" + AppController.getSpUserInfo().getString(SPUtils.Company_ID, "")));
                            response = AppUtils.callWebServiceWithMultiParam(act, postParameters, QueryUtils.methodToSelect_TrainingSchedul, TAG);

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
                            arraylist.clear();
                            if (jsonObject.getString("Status").equalsIgnoreCase("True")) {
                                ll_data_found.setVisibility(View.VISIBLE);
                                ll_no_data_found.setVisibility(View.GONE);
                                if (jsonObject.getJSONArray("Data").length() > 0) {
                                    getAllActivityListResult(jsonObject.getJSONArray("Data"));
                                }
                            } else {
                                showListView();
                                //   AppUtils.alertDialog(act, jsonObject.getString("Message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            AppUtils.showExceptionDialog(act);
                        }
                    }
                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            AppUtils.showExceptionDialog(act);
        }
    }

    private void getAllActivityListResult(JSONArray jsonArray) {
        try {
            arraylist.clear();

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id", jsonObject.getString("TID"));
                hashMap.put("Name", jsonObject.getString("Heading"));
                hashMap.put("Descri", jsonObject.getString("Descri"));
                hashMap.put("FileURL",  jsonObject.getString("FileURL"));
                hashMap.put("Photo_Url", AppUtils.imageURL() + jsonObject.getString("Image"));
                hashMap.put("RTS", jsonObject.getString("RTS"));
                arraylist.add(hashMap);
            }

            showListView();

        } catch (Exception e) {
            e.printStackTrace();
            AppUtils.showExceptionDialog(act);
        }
    }

    private void showListView() {
        try {
            if (arraylist.size() > 0) {
                adapter = new Tranining_List_Adapter(act, arraylist);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                recyclerView.setVisibility(View.VISIBLE);

                ll_data_found.setVisibility(View.VISIBLE);
                ll_no_data_found.setVisibility(View.GONE);
            } else {
                ll_data_found.setVisibility(View.GONE);
                ll_no_data_found.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            AppUtils.showExceptionDialog(act);
        }
    }
}
