package com.vpipl.kvkdholpur.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.vpipl.kvkdholpur.PackageListActivity;
import com.vpipl.kvkdholpur.R;

import java.util.ArrayList;
import java.util.HashMap;

public class Category_List_Adapter extends RecyclerView.Adapter<Category_List_Adapter.MyViewHolder> {
    public ArrayList<HashMap<String, String>> array_list;
    LayoutInflater inflater = null;
    Context context;
    String str_filename;

    public Category_List_Adapter(Context con, ArrayList<HashMap<String, String>> list) {
        array_list = list;
        inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        context = con;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(inflater.inflate(R.layout.categorylist_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(25, 0, 0, 0);

            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params2.setMargins(45, 0, 0, 0);

            if (array_list.get(position).get("Type").equalsIgnoreCase("C")) {
                holder.txt_name.setText(array_list.get(position).get("CategoryName"));
                holder.txt_name.setTextColor(Color.BLACK);
                holder.txt_name.setTextSize(18);

                holder.iv_bullet.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_radio_button_checked_black_24dp));
            } else if (array_list.get(position).get("Type").equalsIgnoreCase("SC1")) {
                holder.txt_name.setText("" + array_list.get(position).get("CategoryName"));
                holder.txt_name.setTextColor(Color.DKGRAY);
                holder.txt_name.setTextSize(16);
                holder.iv_bullet.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_adjust_black_24dp));

                holder.ll_main.setLayoutParams(params);
            } else if (array_list.get(position).get("Type").equalsIgnoreCase("SC2")) {
                holder.txt_name.setText("" + array_list.get(position).get("CategoryName"));
                holder.txt_name.setTextColor(Color.DKGRAY);
                holder.txt_name.setTextSize(14);
                holder.iv_bullet.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_radio_button_unchecked_black_24dp));

                holder.ll_main.setLayoutParams(params2);
            }

            holder.txt_name.setSelected(true);
            holder.txt_name.setSingleLine(true);

            holder.ll_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (array_list.get(position).get("Type").equalsIgnoreCase("C")) {
                        Intent intent = new Intent(context, PackageListActivity.class);
                        intent.putExtra("CID", "" + array_list.get(position).get("CID"));
                        intent.putExtra("L1CategoryID", "");
                        intent.putExtra("L2CategoryID", "");
                        context.startActivity(intent);
                    } else if (array_list.get(position).get("Type").equalsIgnoreCase("SC1")) {
                        Intent intent = new Intent(context, PackageListActivity.class);
                        intent.putExtra("CID", "" + array_list.get(position).get("Parent"));
                        intent.putExtra("L1CategoryID", "" + array_list.get(position).get("CID"));
                        intent.putExtra("L2CategoryID", "");
                        context.startActivity(intent);
                    } else if (array_list.get(position).get("Type").equalsIgnoreCase("SC2")) {
                        Intent intent = new Intent(context, PackageListActivity.class);
                        intent.putExtra("CID", "" + array_list.get(position).get("Parent"));
                        intent.putExtra("L1CategoryID", "" + array_list.get(position).get("Parent2"));
                        intent.putExtra("L2CategoryID", "" + array_list.get(position).get("CID"));
                        context.startActivity(intent);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return array_list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_name;
        LinearLayout ll_main;
        ImageView iv_bullet;

        public MyViewHolder(View view) {
            super(view);
            txt_name = (TextView) view.findViewById(R.id.txt_name);
            ll_main = (LinearLayout) view.findViewById(R.id.ll_main);
            iv_bullet = (ImageView) view.findViewById(R.id.iv_bullet);
        }
    }
}