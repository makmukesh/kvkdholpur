package com.vpipl.kvkdholpur;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class Query_Detail_Activity extends AppCompatActivity {
    ImageView img_nav_back;
    TextView heading ;
    Activity act ;

    public void SetupToolbar() {

        img_nav_back = findViewById(R.id.img_nav_back);
        heading = findViewById(R.id.heading);
        heading.setText("Query Solution Detail");

        img_nav_back = findViewById(R.id.img_nav_back);

        img_nav_back.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
        img_nav_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    WebView wv_query_solution ;
    TextView txt_query_title ;
    String str_query , str_query_solution ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_details
        );

        try {
            act = Query_Detail_Activity.this ;
            Toolbar toolbar = findViewById(R.id.toolbar);
            SetupToolbar();

            wv_query_solution = findViewById(R.id.wv_query_solution);
            txt_query_title = findViewById(R.id.txt_query_title);

            if(!getIntent().equals(null)){
                str_query = getIntent().getStringExtra("Query");
                str_query_solution = getIntent().getStringExtra("QuerySolution");
            }
            txt_query_title.setText(str_query);

            wv_query_solution.loadData(str_query_solution , "text/html", "UTF-8");

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
