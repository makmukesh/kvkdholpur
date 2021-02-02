package com.vpipl.kvkdholpur;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.vpipl.kvkdholpur.Adapter.Package_List_Adapter;
import com.vpipl.kvkdholpur.Model.StackHelperCategory;
import com.vpipl.kvkdholpur.Model.StackHelperCategory1;

import com.vpipl.kvkdholpur.Utils.AppController;
import com.vpipl.kvkdholpur.Utils.AppUtils;
import com.vpipl.kvkdholpur.Utils.QueryUtils;
import com.vpipl.kvkdholpur.Utils.SPUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PackageListActivity extends Activity {

    private String TAG = "PackageListActivity";
    private static final int REQUEST_WRITE_PERMISSION = 786;

    Activity act;
    public Package_List_Adapter adapter;
    RecyclerView recyclerView;
    LinearLayout ll_data_found, ll_no_data_found;
    public static ArrayList<HashMap<String, String>> array_list = new ArrayList<>();
    Spinner spinner_selectcategory;
    String Categorycode = "0", Categoryname = "";
    List<StackHelperCategory> stackHelperCategoryList = new ArrayList<>();
    JSONArray jsonarray_categorylist;

    Spinner spinner_selectcategory1;
    String Categorycode1 = "0", Categoryname1 = "";
    List<StackHelperCategory1> stackHelperCategoryList1 = new ArrayList<>();
    JSONArray jsonarray_categorylist1;
    String CategoryID ,L1CategoryID , L2CategoryID ;

    RelativeLayout rl_cate, rl_subcate;

    ImageView img_nav_back;

    TextView heading;

    public void SetupToolbar() {

        img_nav_back = findViewById(R.id.img_nav_back);
        heading = findViewById(R.id.heading);
        heading.setText("Package List");

        img_nav_back = findViewById(R.id.img_nav_back);

        img_nav_back.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
        img_nav_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packagelist);

        try {
            act = PackageListActivity.this;
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

            Toolbar toolbar = findViewById(R.id.toolbar);
            SetupToolbar();
            requestPermission();
            /*rl_cate = findViewById(R.id.rl_cate);
            rl_subcate = findViewById(R.id.rl_subcate);*/
            ll_data_found = findViewById(R.id.ll_data_found);
            ll_no_data_found = findViewById(R.id.ll_no_data_found);

            recyclerView = (RecyclerView) findViewById(R.id.listView);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            CategoryID = getIntent().getStringExtra("CID");
            L1CategoryID = getIntent().getStringExtra("L1CategoryID");
            L2CategoryID = getIntent().getStringExtra("L2CategoryID");
            executePackageListRequest();

           // rl_subcate.setVisibility(View.GONE);

            /*category list start*/

         /*   jsonarray_categorylist = new JSONArray();
            jsonarray_categorylist1 = new JSONArray();
            spinner_selectcategory = findViewById(R.id.spinner_selectcategory);
            spinner_selectcategory1 = findViewById(R.id.spinner_selectcategory1);

            spinner_selectcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                public void onItemSelected(AdapterView<?> arg0, View view, int position, long id) {
                    if (jsonarray_categorylist.length() > 0) {
                        Categoryname = ((StackHelperCategory) spinner_selectcategory.getSelectedItem()).getStateName();
                        Categorycode = ((StackHelperCategory) spinner_selectcategory.getSelectedItem()).getCode();

                        if (Categorycode.equalsIgnoreCase("0")) {
                            Categoryname = "";
                        }

                        executePackageListRequest(Categorycode);
                       // executeCategoryListRequest1(Categorycode);
                    }
                }

                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
            spinner_selectcategory1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                public void onItemSelected(AdapterView<?> arg0, View view, int position, long id) {
                    if (jsonarray_categorylist1.length() > 0) {
                        Categoryname1 = ((StackHelperCategory1) spinner_selectcategory1.getSelectedItem()).getStateName();
                        Categorycode1 = ((StackHelperCategory1) spinner_selectcategory1.getSelectedItem()).getCode();

                        if (Categorycode1.equalsIgnoreCase("0")) {
                            Categoryname1 = "";
                        }

                        executePackageListRequest(Categorycode);

                    }
                }

                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });*/
            /*category list end*/
            if (AppUtils.isNetworkAvailable(act)) {
                // executeCategoryListRequest("0");
            } else {
                AppUtils.alertDialogWithFinish(act, getResources().getString(R.string.txt_networkAlert));
            }

        } catch (Exception e) {
            e.printStackTrace();
            AppUtils.showExceptionDialog(PackageListActivity.this);
        }
    }

    private void spinState() {
        try {
            stackHelperCategoryList.clear();
            for (int i = 0; i < jsonarray_categorylist.length(); i++) {
                JSONObject jsonobject = jsonarray_categorylist.getJSONObject(i);
                Categorycode = jsonobject.getString("CID");
                Categoryname = jsonobject.getString("CategoryName");

                StackHelperCategory stackHelper = new StackHelperCategory();
                stackHelper.setStateName(Categoryname);
                stackHelper.setCode(Categorycode);
                stackHelperCategoryList.add(stackHelper);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter<StackHelperCategory> dataAdapter = new ArrayAdapter<StackHelperCategory>(this, R.layout.sppiner_item, stackHelperCategoryList);
        dataAdapter.setDropDownViewResource(R.layout.sppiner_item);
        spinner_selectcategory.setAdapter(dataAdapter);
    }

    private void spinState1() {
        try {
            stackHelperCategoryList1.clear();
            for (int i = 0; i < jsonarray_categorylist1.length(); i++) {
                JSONObject jsonobject = jsonarray_categorylist1.getJSONObject(i);
                Categorycode1 = jsonobject.getString("CID");
                Categoryname1 = jsonobject.getString("CategoryName");

                StackHelperCategory1 stackHelper1 = new StackHelperCategory1();
                stackHelper1.setStateName(Categoryname1);
                stackHelper1.setCode(Categorycode1);
                stackHelperCategoryList1.add(stackHelper1);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter<StackHelperCategory1> dataAdapter = new ArrayAdapter<StackHelperCategory1>(this, R.layout.sppiner_item, stackHelperCategoryList1);
        dataAdapter.setDropDownViewResource(R.layout.sppiner_item);
        spinner_selectcategory1.setAdapter(dataAdapter);
    }

    private void executeCategoryListRequest(final String id) {
        new AsyncTask<Void, Void, String>() {
            protected void onPreExecute() {
                AppUtils.showProgressDialog(PackageListActivity.this);
            }

            @Override
            protected String doInBackground(Void... params) {
                String response = "";
                try {
                    List<NameValuePair> postParameters = new ArrayList<>();
                    postParameters.add(new BasicNameValuePair("CompID", "" + AppController.getSpUserInfo().getString(SPUtils.Company_ID, "")));
                    postParameters.add(new BasicNameValuePair("ParentID", "" + id));
                    response = AppUtils.callWebServiceWithMultiParam(PackageListActivity.this, postParameters, QueryUtils.methodToSelect_Category, TAG);
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
                    JSONArray jsonArrayData = jsonObject.getJSONArray("Data");
                    jsonarray_categorylist = null;
                    if (jsonObject.getString("Status").equalsIgnoreCase("True")) {
                        if (jsonArrayData.length() != 0) {
                            jsonarray_categorylist = jsonArrayData;
                            spinState();
                            rl_cate.setVisibility(View.VISIBLE);
                        } else {
                            jsonarray_categorylist = new JSONArray("[{\"CID\":0,\"CategoryName\":\"-- No State Found --\"}]");
                            spinState();
                            rl_cate.setVisibility(View.GONE);
                        }
                    } else {
                        // AppUtils.alertDialog(PackageListActivity.this, jsonObject.getString("Message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void executeCategoryListRequest1(final String id) {
        new AsyncTask<Void, Void, String>() {
            protected void onPreExecute() {
                AppUtils.showProgressDialog(PackageListActivity.this);
            }

            @Override
            protected String doInBackground(Void... params) {
                String response = "";
                try {
                    List<NameValuePair> postParameters = new ArrayList<>();
                    postParameters.add(new BasicNameValuePair("CompID", "" + AppController.getSpUserInfo().getString(SPUtils.Company_ID, "")));
                    postParameters.add(new BasicNameValuePair("ParentID", "" + id));
                    response = AppUtils.callWebServiceWithMultiParam(PackageListActivity.this, postParameters, QueryUtils.methodToSelect_Category, TAG);
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
                    JSONArray jsonArrayData = jsonObject.getJSONArray("Data");
                    jsonarray_categorylist1 = null;
                    if (jsonObject.getString("Status").equalsIgnoreCase("True")) {
                        if (jsonArrayData.length() != 0) {
                            jsonarray_categorylist1 = jsonArrayData;
                            spinState1();
                            rl_subcate.setVisibility(View.VISIBLE);
                        } else {
                            jsonarray_categorylist1 = new JSONArray("[{\"CID\":0,\"CategoryName\":\"-- No State Found --\"}]");
                            spinState1();
                            rl_subcate.setVisibility(View.GONE);
                        }
                    } else {
                        //  AppUtils.alertDialog(PackageListActivity.this, jsonObject.getString("Message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void executePackageListRequest() {
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
                            postParameters.add(new BasicNameValuePair("CategoryID", "" + CategoryID));
                            postParameters.add(new BasicNameValuePair("L1CategoryID", "" + L1CategoryID));
                            postParameters.add(new BasicNameValuePair("L2CategoryID", "" + L2CategoryID));
                            response = AppUtils.callWebServiceWithMultiParam(act, postParameters, QueryUtils.methodToSelect_Package, TAG);
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
                            array_list.clear();
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
            array_list.clear();

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("PID", jsonObject.getString("PID"));
                hashMap.put("CategoryID", jsonObject.getString("CategoryID"));
                hashMap.put("Level1CategoryID", jsonObject.getString("Level1CategoryID"));
                hashMap.put("Level2CategoryID", jsonObject.getString("Level2CategoryID"));
                hashMap.put("Name", jsonObject.getString("PackageName"));
                hashMap.put("Descri", jsonObject.getString("Descri"));
                hashMap.put("Photo_Url", AppUtils.imageURL() + jsonObject.getString("Image"));
                hashMap.put("FileURL", jsonObject.getString("FileURL"));

                array_list.add(hashMap);
            }

            showListView();

        } catch (Exception e) {
            e.printStackTrace();
            AppUtils.showExceptionDialog(act);
        }
    }

    private void showListView() {
        try {
            if (array_list.size() > 0) {
                adapter = new Package_List_Adapter(act, array_list);
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
            AppUtils.showExceptionDialog(PackageListActivity.this);
        }
        System.gc();
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {

        }
    }
}
