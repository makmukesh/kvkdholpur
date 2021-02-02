package com.vpipl.kvkdholpur.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.vpipl.kvkdholpur.Query_Detail_Activity;
import com.vpipl.kvkdholpur.R;
import com.vpipl.kvkdholpur.Utils.AppUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class Query_List_Adapter extends RecyclerView.Adapter<Query_List_Adapter.MyViewHolder> {
    public ArrayList<HashMap<String, String>> faculty_list;
    LayoutInflater inflater = null;
    Context context;
    String str_filename;

    public Query_List_Adapter(Context con, ArrayList<HashMap<String, String>> list) {
        faculty_list = list;
        inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        context = con;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(inflater.inflate(R.layout.querylist_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {
            holder.txt_name.setText(faculty_list.get(position).get("Name"));
            holder.txt_Query.setText(faculty_list.get(position).get("QueryText"));
            holder.txt_Querydatetime.setText(faculty_list.get(position).get("QDate"));

            AppUtils.loadImage(context, faculty_list.get(position).get("FileURL"), holder.iv_photo);

            holder.txt_name.setSelected(true);
            holder.txt_Query.setSelected(true);

            holder.txt_name.setSingleLine(true);
            holder.txt_Query.setSingleLine(true);

            if(!faculty_list.get(position).get("QuerySolution").equalsIgnoreCase("")){
                holder.ll_view_solution.setVisibility(View.VISIBLE);
            }
            else{
                holder.ll_view_solution.setVisibility(View.GONE);
            }

            holder.ll_view_solution.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context , Query_Detail_Activity.class);
                    intent.putExtra("Query" , "" + faculty_list.get(position).get("QueryText"));
                    intent.putExtra("QuerySolution" , "" + faculty_list.get(position).get("QuerySolution"));
                    context.startActivity(intent);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return faculty_list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_name , txt_Query, txt_Querydatetime ;
        ImageView iv_photo;
        LinearLayout ll_view_solution;

        public MyViewHolder(View view) {
            super(view);
            iv_photo = (ImageView) view.findViewById(R.id.iv_photo);
            txt_name = (TextView) view.findViewById(R.id.txt_name);
            txt_Query = (TextView) view.findViewById(R.id.txt_Query);
            txt_Querydatetime = (TextView) view.findViewById(R.id.txt_Querydatetime);
            ll_view_solution = (LinearLayout) view.findViewById(R.id.ll_view_solution);
        }
    }
}