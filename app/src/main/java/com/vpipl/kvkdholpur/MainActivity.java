package com.vpipl.kvkdholpur;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.vpipl.kvkdholpur.Adapter.SliderAdapterExample;
import com.vpipl.kvkdholpur.Model.SliderItem;

import com.vpipl.kvkdholpur.Utils.AppController;
import com.vpipl.kvkdholpur.Utils.AppUtils;
import com.vpipl.kvkdholpur.Utils.QueryUtils;
import com.vpipl.kvkdholpur.Utils.SPUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {
    private String TAG = "MainActivity";

    SliderView sliderView;
    private SliderAdapterExample adapter;
    private List<SliderItem> mSliderItems = new ArrayList<>();

    public static ArrayList<HashMap<String, String>> imageSlider = new ArrayList<>();
    Activity act;
    LinearLayout ll_home_aboutus, ll_home_training, ll_home_packages, ll_home_photo_gallery,
            ll_home_query, ll_home_query_solution, ll_home_contact_us, ll_home_Faculty, ll_logout, ll_home_Ekai,
            ll_home_Agricultural_news, ll_home_Weather_today, ll_home_Monthly_agricultural_work, ll_home_Story_of_farmer, ll_home_Available_for_sale;
    TextView txt_home_mobile_no , txt_ttl_visitors;
    TextToSpeech t1;
    Button btn_go_for_website, btn_logout;
    ImageView img_nav_back, img_nav_seond;
    TextView heading;

    public void SetupToolbar() {

        img_nav_back = findViewById(R.id.img_nav_back);
        img_nav_seond = findViewById(R.id.img_nav_seond);
        img_nav_seond.setVisibility(View.VISIBLE);
        heading = findViewById(R.id.heading);
        heading.setText("कृषि विज्ञान केंद्र, धौलपुर");
        img_nav_back.setImageDrawable(getResources().getDrawable(R.drawable.logo));

        ImageView img_nav_logout = findViewById(R.id.img_nav_logout);

        img_nav_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.showDialogSignOut(MainActivity.this);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            act = MainActivity.this;

            Toolbar toolbar = findViewById(R.id.toolbar);
            SetupToolbar();

            btn_go_for_website = findViewById(R.id.btn_go_for_website);
            btn_logout = findViewById(R.id.btn_logout);
            txt_ttl_visitors = findViewById(R.id.txt_ttl_visitors);
            ll_home_aboutus = findViewById(R.id.ll_home_aboutus);
            ll_home_training = findViewById(R.id.ll_home_training);
            ll_home_packages = findViewById(R.id.ll_home_packages);
            ll_home_photo_gallery = findViewById(R.id.ll_home_photo_gallery);
            ll_home_query = findViewById(R.id.ll_home_query);
            ll_home_query_solution = findViewById(R.id.ll_home_query_solution);
            ll_home_contact_us = findViewById(R.id.ll_home_contact_us);
            ll_home_Faculty = findViewById(R.id.ll_home_Faculty);
            ll_logout = findViewById(R.id.ll_logout);
            ll_home_Ekai = findViewById(R.id.ll_home_Ekai);
            ll_home_Agricultural_news = findViewById(R.id.ll_home_Agricultural_news);
            ll_home_Weather_today = findViewById(R.id.ll_home_Weather_today);
            ll_home_Monthly_agricultural_work = findViewById(R.id.ll_home_Monthly_agricultural_work);
            ll_home_Story_of_farmer = findViewById(R.id.ll_home_Story_of_farmer);
            ll_home_Available_for_sale = findViewById(R.id.ll_home_Available_for_sale);

            txt_home_mobile_no = findViewById(R.id.txt_home_mobile_no);
            TextView txt_Monthly_Agricultural_Work = findViewById(R.id.txt_Monthly_Agricultural_Work);
            txt_Monthly_Agricultural_Work.setSingleLine(true);
            txt_Monthly_Agricultural_Work.setSelected(true);

            sliderView = findViewById(R.id.imageSlider);

            adapter = new SliderAdapterExample(this);
            sliderView.setSliderAdapter(adapter);

            sliderView.setIndicatorAnimation(IndicatorAnimations.DROP); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
            sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
            sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
            sliderView.setIndicatorSelectedColor(Color.WHITE);
            sliderView.setIndicatorUnselectedColor(Color.GRAY);
            sliderView.setScrollTimeInSec(3);
            sliderView.setAutoCycle(true);
            sliderView.startAutoCycle();

            sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
                @Override
                public void onIndicatorClicked(int position) {
                    sliderView.setCurrentPagePosition(position);
                }
            });

           /* addNewItem("http://Alwar.kvk2.in/images/slide1.jpg");
            addNewItem("http://Alwar.kvk2.in/images/slide2.jpg");
            addNewItem("http://Alwar.kvk2.in/images/slide3.jpg");
            addNewItem("http://Alwar.kvk2.in/images/slide4.jpg");
            addNewItem("http://Alwar.kvk2.in/images/slide5.jpg");*/

            String m_no = AppController.getSpUserInfo().getString(SPUtils.MemberMobileNo, "");
            if (!m_no.equalsIgnoreCase("")) {
                // txt_home_mobile_no.setText("किसान का मोबाइल नं : " + m_no);
                txt_home_mobile_no.setText("मोबाइल नं : " + m_no);
            } else {
                txt_home_mobile_no.setVisibility(View.GONE);
            }
            txt_home_mobile_no.setVisibility(View.GONE);

            t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status != TextToSpeech.ERROR) {
                        t1.setLanguage(Locale.UK);
                    }
                }
            });
            txt_home_mobile_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String toSpeak = "" + getResources().getString(R.string.app_name);
                    // Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
                    // t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                }
            });

            /*ll_home_aboutus ,ll_home_training , ll_home_packages , ll_home_photo_gallery ,
                    ll_home_query , ll_home_contact_us , ll_home_Faculty*/

            if (AppController.getSpUserInfo().getString(SPUtils.MemberMobileNo, "").equalsIgnoreCase("")) {
                btn_logout.setVisibility(View.GONE);
            } else {
                btn_logout.setVisibility(View.VISIBLE);
            }

            ll_home_aboutus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(act, AboutUsActivity.class));
                    //    startActivity(new Intent(act, RemotePDFActivity.class).putExtra("URL" , "https://file-examples.com/wp-content/uploads/2017/02/file-sample_1MB.docx"));
                }
            });

            ll_home_training.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(act, Tranning_ListActivity.class));
                }
            });
            ll_home_packages.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(act, CategoryListActivity.class));
                }
            });
            ll_home_photo_gallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(act, PhotoGalleryActivity.class));
                }
            });
            ll_home_query.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (AppController.getSpUserInfo().getString(SPUtils.MemberMobileNo, "").equalsIgnoreCase("")) {
                        startActivity(new Intent(act, Login_Local_Activity.class).putExtra("Entry_type", "Q"));
                    } else {
                        startActivity(new Intent(act, QueryActivity.class));
                    }
                }
            });
            ll_home_query_solution.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (AppController.getSpUserInfo().getString(SPUtils.MemberMobileNo, "").equalsIgnoreCase("")) {
                        startActivity(new Intent(act, Login_Local_Activity.class).putExtra("Entry_type", "QS"));
                    } else {
                        startActivity(new Intent(act, QuerySolutionListActivity.class));
                    }
                }
            });
            ll_home_contact_us.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(act, ContactUsActivity.class));
                }
            });
            ll_home_Faculty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(act, FacultyListActivity.class));
                }
            });
            ll_home_Ekai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(act, Ekai_ListActivity.class));
                }
            });

            ll_home_Agricultural_news.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(act, AgriculturalNewsActivity.class));
                }
            });
            ll_home_Weather_today.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(act, WeatherTodayActivity.class));
                }
            });
            ll_home_Monthly_agricultural_work.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(act, MonthlyAgriculturalWorkActivity.class));
                }
            });
            ll_home_Story_of_farmer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(act, StoryOfTheFarmerActivity.class));
                }
            });
            ll_home_Available_for_sale.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(act, AvailableForSaleActivity.class));
                }
            });

            btn_go_for_website.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent httpIntent = new Intent(Intent.ACTION_VIEW);
                    httpIntent.setData(Uri.parse("http://dholpur.kvk2.in/index.html"));
                    startActivity(httpIntent);
                }
            });
            btn_logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppUtils.showDialogSignOut(act);
                }
            });

            if (AppUtils.isNetworkAvailable(act)) {
                executeSliderPhotosRequest();
                executeVisitingAppRequest();
            } else {
                addNewItem("http://kvkdholpur.versatileitsolution.com/admin/assets/img/logo-white.jpg");
                // AppUtils.alertDialogWithFinish(act, getResources().getString(R.string.txt_networkAlert));
            }
            AppController.getSpUserInfo().edit().putString(SPUtils.Company_ID, "1").commit();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("onCreate", e.getMessage());
        }
    }

    public void addNewItem(String URL) {
        SliderItem sliderItem = new SliderItem();
        sliderItem.setDescription("");
        sliderItem.setImageUrl("" + URL);
        adapter.addItem(sliderItem);
    }

    private void executeSliderPhotosRequest() {
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
                            response = AppUtils.callWebServiceWithMultiParam(act, postParameters, QueryUtils.methodToSelectSliderImages, TAG);
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
                                for (int i = 0; i < jsonObject.getJSONArray("Data").length(); i++) {
                                    JSONObject jsonObject1 = jsonObject.getJSONArray("Data").getJSONObject(i);
                                    addNewItem(AppUtils.imageURL() + jsonObject1.getString("ImageUrl"));
                                }
                            } else {
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

    private void executeVisitingAppRequest() {
        try {
            if (AppUtils.isNetworkAvailable(act)) {
                new AsyncTask<Void, Void, String>() {
                    protected void onPreExecute() {
                       // AppUtils.showProgressDialog(act);
                    }

                    @Override
                    protected String doInBackground(Void... params) {
                        String response = "";
                        try {
                            List<NameValuePair> postParameters = new ArrayList<>();
                            postParameters.add(new BasicNameValuePair("IP", "" + AppUtils.getDeviceID(MainActivity.this)));
                            response = AppUtils.callWebServiceWithMultiParam(act, postParameters, QueryUtils.methodToSelect_VisitorsCount, TAG);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return response;
                    }

                    @Override
                    protected void onPostExecute(String resultData) {
                        try {
                          //  AppUtils.dismissProgressDialog();
                            JSONObject jsonObject = new JSONObject(resultData);
                            if (jsonObject.getString("Status").equalsIgnoreCase("True")) {
                                JSONObject jsonObject1 = jsonObject.getJSONArray("Data").getJSONObject(0);
                                txt_ttl_visitors.setText("Total Visitors : " + jsonObject1.getString("Visitor") + "");
                              //  txt_ttl_visitors.setText("Total Visitors : " + "1001" + "");
                                txt_ttl_visitors.setVisibility(View.VISIBLE);
                            } else {
                                //   AppUtils.alertDialog(act, jsonObject.getString("Message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                           // AppUtils.showExceptionDialog(act);
                        }
                    }
                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        } catch (Exception e) {
            e.printStackTrace();
           // AppUtils.showExceptionDialog(act);
        }
    }

    @Override
    protected void onRestart() {
        if (AppController.getSpUserInfo().getString(SPUtils.MemberMobileNo, "").equalsIgnoreCase("")) {
            btn_logout.setVisibility(View.GONE);
        } else {
            btn_logout.setVisibility(View.VISIBLE);
        }
        super.onRestart();
    }
}
