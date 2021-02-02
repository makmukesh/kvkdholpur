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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.vpipl.kvkdholpur.Adapter.Faculty_List_Adapter;
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
public class FacultyListActivity extends Activity {

    private String TAG = "FacultyListActivity";

    Activity act ;
    public Faculty_List_Adapter adapter;
    RecyclerView recyclerView;
    LinearLayout ll_data_found ,ll_no_data_found ;
    public static ArrayList<HashMap<String, String>> FacultyList = new ArrayList<>();

    ImageView img_nav_back;


    TextView heading ;

    public void SetupToolbar() {

        img_nav_back = findViewById(R.id.img_nav_back);
        heading = findViewById(R.id.heading);
        heading.setText("Staff");

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
        setContentView(R.layout.activity_faculty_list);

        try {
            act = FacultyListActivity.this ;
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

            Toolbar toolbar = findViewById(R.id.toolbar);
            SetupToolbar();

            ll_data_found  =  findViewById(R.id.ll_data_found);
            ll_no_data_found  =  findViewById(R.id.ll_no_data_found);

            recyclerView = (RecyclerView) findViewById(R.id.listView);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);


            recyclerView.setItemAnimator(new DefaultItemAnimator());

            if (AppUtils.isNetworkAvailable(act)) {
                    executePhotoGalleryRequest();
            } else {
                AppUtils.alertDialogWithFinish(act, getResources().getString(R.string.txt_networkAlert));
            }
            /*FacultyList.clear();
            HashMap<String, String> map = new HashMap<>();
            map.put("id", "1");
            map.put("Name", "Dr. C. M. Balai");
            map.put("Designation", "Senior Scientist & Head");
            map.put("mobile_no", "(+91)9414518876");
            map.put("qualification", "");
            map.put("email_id", "cmpuat@gmail.com");
            map.put("Photo_Url", "http://versatileitsolution.com/appupload/D_Faculty1.png".replaceAll(" ", "%20"));
            FacultyList.add(map);

            HashMap<String, String> map1 = new HashMap<>();
            map1.put("id", "2");
            map1.put("Name", "Dr. B. L. Roat");
            map1.put("Designation", "Subject Matter Specialist (Plant Pathology)");
            map1.put("mobile_no", "(+91)9414723019");
            map1.put("qualification", "");
            map1.put("email_id", "blroat4a4@gmail.com");
            map1.put("Photo_Url", "http://versatileitsolution.com/appupload/D_Faculty2.png".replaceAll(" ", "%20"));
            FacultyList.add(map1);

            HashMap<String, String> map11 = new HashMap<>();
            map11.put("id", "11");
            map11.put("Name", "Dr. G. P. Narolia");
            map11.put("Designation", "SMS (Agrometeorology)");
            map11.put("mobile_no", "(+91)9785977392");
            map11.put("qualification", "Ph. D.");
            map11.put("email_id", "narolia.agro@gmail.com");
            map11.put("Photo_Url", "http://versatileitsolution.com/appupload/D_Faculty2_2.png".replaceAll(" ", "%20"));
            FacultyList.add(map11);

            HashMap<String, String> map2 = new HashMap<>();
            map2.put("id", "3");
            map2.put("Name", "Sh. N. L. Ahari");
            map2.put("Designation", "Technical Assistant");
            map2.put("mobile_no", "(+91)9414723120");
            map2.put("qualification", "");
            map2.put("email_id", "");
            map2.put("Photo_Url", "http://versatileitsolution.com/appupload/D_Faculty3.png".replaceAll(" ", "%20"));
            FacultyList.add(map2);

            HashMap<String, String> map3 = new HashMap<>();
            map3.put("id", "4");
            map3.put("Name", "Sh. K. C. Kharadi");
            map3.put("Designation", "Technical Assistant");
            map3.put("mobile_no", "(+91)9636383424");
            map3.put("qualification", "");
            map3.put("email_id", "kharadikc1987@gmail.com");
            map3.put("Photo_Url", "http://versatileitsolution.com/appupload/D_Faculty4.png".replaceAll(" ", "%20"));
            FacultyList.add(map3);

            HashMap<String, String> map4 = new HashMap<>();
            map4.put("id", "5");
            map4.put("Name", "Sh. Suraj Bamaniya");
            map4.put("Designation", "Division Clerk");
            map4.put("mobile_no", "(+91) 9660233098");
            map4.put("qualification", "");
            map4.put("email_id", "");
            map4.put("Photo_Url", "http://versatileitsolution.com/appupload/D_Faculty5.png".replaceAll(" ", "%20"));
            FacultyList.add(map4);

            HashMap<String, String> map5 = new HashMap<>();
            map5.put("id", "6");
            map5.put("Name", "Sh. Jeeva Ram");
            map5.put("Designation", "Class IVth");
            map5.put("mobile_no", "(+91) 8107485217");
            map5.put("qualification", "");
            map5.put("email_id", "");
            map5.put("Photo_Url", "http://versatileitsolution.com/appupload/D_Faculty6.png".replaceAll(" ", "%20"));
            FacultyList.add(map5);

            HashMap<String, String> map6 = new HashMap<>();
            map6.put("id", "7");
            map6.put("Name", "Sh. Jawara");
            map6.put("Designation", "Class IVth");
            map6.put("mobile_no", "7426920386");
            map6.put("qualification", "");
            map6.put("email_id", "");
            map6.put("Photo_Url", "http://versatileitsolution.com/appupload/D_Faculty7.png".replaceAll(" ", "%20"));
            FacultyList.add(map6);

            showListView();*/

        } catch (Exception e) {
            e.printStackTrace();
            AppUtils.showExceptionDialog(FacultyListActivity.this);
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
                            response = AppUtils.callWebServiceWithMultiParam(act, postParameters, QueryUtils.methodToSelect_Faculty, TAG);
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
                            FacultyList.clear();
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
            FacultyList.clear();

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("FID", jsonObject.getString("FID"));
                hashMap.put("Name", jsonObject.getString("Name"));
                hashMap.put("Designation", jsonObject.getString("Designation"));
                hashMap.put("mobile_no", jsonObject.getString("ContactNo"));
                hashMap.put("qualification", "");
                hashMap.put("email_id", jsonObject.getString("EmailID"));
                hashMap.put("Photo_Url", AppUtils.imageURL() + jsonObject.getString("Image"));
                hashMap.put("OnDeputation", jsonObject.getString("OnDeputation"));
                hashMap.put("WorkingArea", jsonObject.getString("WorkingArea"));
                FacultyList.add(hashMap);
            }

            showListView();

        } catch (Exception e) {
            e.printStackTrace();
            AppUtils.showExceptionDialog(act);
        }
    }

    private void showListView() {
        try {

            if (FacultyList.size() > 0) {
                adapter = new Faculty_List_Adapter(act, FacultyList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                recyclerView.setVisibility(View.VISIBLE);

                ll_data_found.setVisibility(View.VISIBLE);
                ll_no_data_found.setVisibility(View.GONE);
            }
            else{
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
            AppUtils.showExceptionDialog(FacultyListActivity.this);
        }
        System.gc();
    }
}
