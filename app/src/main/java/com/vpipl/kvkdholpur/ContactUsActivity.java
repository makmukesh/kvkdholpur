package com.vpipl.kvkdholpur;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.vpipl.kvkdholpur.Utils.AppUtils;

public class ContactUsActivity extends AppCompatActivity {
    ImageView img_nav_back;
    String vision = "<p>Dr. Sushil Kumar Sharma</p>\n" +
            "<h4>Sr. Scientist and Head&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</h4>\n" +
            "<p>&nbsp;</p>\n" +
            "<p><strong>Krishi Vigyan Kendra</strong></p>\n" +
            "<p><strong>(</strong>भा.कृ़.अनु.प.-सरसों अनुसंधान निदेषालय<strong>)</strong></p>\n" +
            "<p>&nbsp;</p>\n" +
            "<p>Gunta, Tahsil- Bansur 301 402, Alwar (Rajasthan)</p>\n" +
            "<p><strong> Mobile No. :</strong> 07976966603</p>\n" +
            "<p><strong>Fax :</strong> 05644&nbsp;&nbsp;&nbsp; 260565/260575&nbsp;&nbsp;</p>\n" +
            "<p><strong>Email :</strong> kvkbansur<u>@gmail.com</u></p>\n" +
            "<p><strong>Website : <span style=\"color: #3366ff;\"><a style=\"color: #3366ff;\" href=\"http://alwar2.kvk2.in/\">http://alwar2.kvk2.in/</a></span></strong></p>";


    TextView heading;
    Activity act ;
    public void SetupToolbar() {

        img_nav_back = findViewById(R.id.img_nav_back);
        heading = findViewById(R.id.heading);
        heading.setText("Contact Us");

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

    WebView wv_contact_us;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        Toolbar toolbar = findViewById(R.id.toolbar);
        SetupToolbar();
        act = ContactUsActivity.this ;
        wv_contact_us = findViewById(R.id.wv_contact_us);
        TextView txt_vision = findViewById(R.id.txt_vision);

        txt_vision.setText(Html.fromHtml(vision));

        String str_song_url = "<iframe src=https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d466899.2835531091!2d73.6253900105352!3d23.90200356598528!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x396771de9e77130b%3A0x1cfcd90a215fdfdb!2sKrishi+Vigyan+Kendra!5e0!3m2!1sen!2sin!4v1555086966289!5m2!1sen!2sin width=100% height=350 frameborder=0 style=border:0 > </iframe>";


    //    String iframe = "<iframe src=https://www.google.com/maps/embed?pb=!1m16!1m12!1m3!1d434549.40374533384!2d74.24349628287739!3d31.690830957117996!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!2m1!1sPakistan+Lahore!5e0!3m2!1sen!2s!4v1395138949280 width=600 height=450 frameborder=0 style=border:0</iframe>";
        wv_contact_us.getSettings().setJavaScriptEnabled(true);
        wv_contact_us.loadData(str_song_url, "text/html", "utf-8");

    }
}
