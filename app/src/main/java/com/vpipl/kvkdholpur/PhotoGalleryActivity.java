package com.vpipl.kvkdholpur;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vpipl.kvkdholpur.Adapter.Photo_Gallery_Adapter;

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

/**
 * Created by Mukesh on 26/05/2020.
 */
public class PhotoGalleryActivity extends Activity {

    private String TAG = "PhotoGalleryActivity";

    Activity act;
    public Photo_Gallery_Adapter adapter;
    RecyclerView recyclerView;
    LinearLayout ll_data_found, ll_no_data_found;
    public static ArrayList<HashMap<String, String>> PhotoList = new ArrayList<>();

    ImageView img_nav_back;


    TextView heading;

    public void SetupToolbar() {

        img_nav_back = findViewById(R.id.img_nav_back);
        heading = findViewById(R.id.heading);
        heading.setText("Photo Gallery");
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_gallery);

        try {
            act = PhotoGalleryActivity.this;
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

            Toolbar toolbar = findViewById(R.id.toolbar);
            SetupToolbar();

            ll_data_found = findViewById(R.id.ll_data_found);
            ll_no_data_found = findViewById(R.id.ll_no_data_found);

            /*News Section Start*/
            recyclerView = (RecyclerView) findViewById(R.id.listView);

            // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            // recyclerView.setLayoutManager(mLayoutManager);

            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());

        /*  GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),3);
            gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // set Horizontal Orientation
            recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView*/

            if (AppUtils.isNetworkAvailable(act)) {
                executePhotoGalleryRequest();
            } else {
                AppUtils.alertDialogWithFinish(act, getResources().getString(R.string.txt_networkAlert));
            }

        } catch (Exception e) {
            e.printStackTrace();
            AppUtils.showExceptionDialog(PhotoGalleryActivity.this);
        }
    }

    private void executePhotoGalleryRequest() {
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
                            response = AppUtils.callWebServiceWithMultiParam(act, postParameters, QueryUtils.methodToSelect_Gallary, TAG);
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
                            AppController.PhotoList.clear();
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
            AppController.PhotoList.clear();

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id", jsonObject.getString("GID"));
                hashMap.put("Name", jsonObject.getString("Name"));
                hashMap.put("Photo_Url", AppUtils.imageURL() + jsonObject.getString("Image"));
                AppController.PhotoList.add(hashMap);
            }

            showListView();

        } catch (Exception e) {
            e.printStackTrace();
            AppUtils.showExceptionDialog(act);
        }
    }

    private void showListView() {
        try {
            if (AppController.PhotoList.size() > 0) {
                adapter = new Photo_Gallery_Adapter(act, AppController.PhotoList);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            AppUtils.dismissProgressDialog();
            ////overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } catch (Exception e) {
            e.printStackTrace();
            AppUtils.showExceptionDialog(PhotoGalleryActivity.this);
        }
        System.gc();
    }
}
