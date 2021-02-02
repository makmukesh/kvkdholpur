package com.vpipl.kvkdholpur;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.vpipl.kvkdholpur.Utils.AppUtils;

public class AboutUsActivity extends AppCompatActivity {

    String vision = "KrishiVigyan Kendra, Gunta- Bansur, is a newly established ( 28th March 2012 ) by Indian Council of Agricultural Research, New Delhi under the administrative control of ICAR - Directorate of Rapeseed-Mustard Research, Sewar,Bharatpur. Alwar district has 12 tehsils and two KVKs one is KVK, Navgaon and other is KVK, Gunta-Bansur. Among 12 tehsils in the district, 6 are comes under the jurisdiction of KVK, Navgaon and 6 (Bansur, Behror, Mundawar, Kishangarhbash, Tizara and Kotkasim) under the KVK, Gunta-Bansur.KVK, Gunta-Bansur has been allotted 19.63 hectare land near Gunta village of Bansur Tehsil, of which 10 hectare area is currently under cultivation and remaining is still to reform. Presently kvk having the office building and farmer’s hostel and demonstration units like mother orchard, vermi compost, ajola, animal fodder, mushroom and nutri-garden etc on beginning stage. \n";
    String mission = "The mandate of the KVK is to imparting vocational training to the practicing farmers including, farm women, youth and extension functionaries in improved technologies in the field of agriculture, horticulture, animal husbandry and other allied enterprises. It has additional responsibilities of testing and refining the developed technologies by conducting On Farm testing and popularization of new technologies through front line demonstrations at the farmer’s field.";
    String maindate = "";
    String activity = " ";
    TextView txt_vision, txt_mission, txt_maindat, txt_activity;
    ImageView img_nav_back;
    Activity act ;
    TextView heading ;

    public void SetupToolbar() {
        img_nav_back = findViewById(R.id.img_nav_back);
        heading = findViewById(R.id.heading);
        heading.setText("About Us");

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
        setContentView(R.layout.activity_about_us);
        try {
            act = AboutUsActivity.this ;
            Toolbar toolbar = findViewById(R.id.toolbar);
            SetupToolbar();

            /*txt_vision = findViewById(R.id.txt_vision);
            txt_mission = findViewById(R.id.txt_mission);
            txt_maindat = findViewById(R.id.txt_maindat);
            txt_activity = findViewById(R.id.txt_activity);

            txt_vision.setText(Html.fromHtml(vision));
            txt_mission.setText(Html.fromHtml(mission));
            txt_maindat.setText(Html.fromHtml(maindate));
            txt_activity.setText(Html.fromHtml(activity));*/
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("oncreateerror", e.getMessage());
        }
    }
}
