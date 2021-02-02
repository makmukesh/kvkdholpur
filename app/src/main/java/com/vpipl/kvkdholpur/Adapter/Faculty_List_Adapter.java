package com.vpipl.kvkdholpur.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.vpipl.kvkdholpur.R;
import com.vpipl.kvkdholpur.Utils.AppUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class Faculty_List_Adapter extends RecyclerView.Adapter<Faculty_List_Adapter.MyViewHolder> {
    public ArrayList<HashMap<String, String>> faculty_list;
    LayoutInflater inflater = null;
    Context context;
    String str_filename;

    public Faculty_List_Adapter(Context con, ArrayList<HashMap<String, String>> list) {
        faculty_list = list;
        inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        context = con;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(inflater.inflate(R.layout.facultylist_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {
            /*String html1 = "<b>Name : </b>" +faculty_list.get(position).get("Name");
            String html2 = "<b>Designation : </b>" +faculty_list.get(position).get("Designation");
            String html3 = "<b>Mobile No : </b>" +faculty_list.get(position).get("mobile_no");
            String html4 = "<b>Qualification : </b>" +faculty_list.get(position).get("qualification");
            String html5 = "<b>Email Id : </b>" +faculty_list.get(position).get("email_id");*/

            holder.txt_name.setText(faculty_list.get(position).get("Name"));
            holder.txt_Designation.setText(faculty_list.get(position).get("Designation"));
            holder.txt_mobile_no.setText(faculty_list.get(position).get("mobile_no"));
            holder.txt_qualification.setText(faculty_list.get(position).get("qualification"));
            holder.txt_email_id.setText(faculty_list.get(position).get("email_id"));

           AppUtils.loadImage(context, faculty_list.get(position).get("Photo_Url"), holder.iv_photo);

            holder.txt_name.setSelected(true);
            holder.txt_Designation.setSelected(true);
            holder.txt_mobile_no.setSelected(true);
            holder.txt_qualification.setSelected(true);
            holder.txt_email_id.setSelected(true);

            holder.txt_name.setSingleLine(true);
            holder.txt_Designation.setSingleLine(true);
            holder.txt_mobile_no.setSingleLine(true);
            holder.txt_qualification.setSingleLine(true);
            holder.txt_email_id.setSingleLine(true);

           /* holder.ll_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context , PhotoGalleryDetailsActivity.class);
                   // intent.putExtra("id" , faculty_list.get(position).get("id"));
                    intent.putExtra("id" , "" + position);
                    intent.putExtra("List" , faculty_list);
                    context.startActivity(intent);
                }
            });*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return faculty_list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_name , txt_Designation, txt_mobile_no,txt_qualification,txt_email_id;
        ImageView iv_photo;
        LinearLayout ll_main;

        public MyViewHolder(View view) {
            super(view);
            iv_photo = (ImageView) view.findViewById(R.id.iv_photo);
            txt_name = (TextView) view.findViewById(R.id.txt_name);
            txt_Designation = (TextView) view.findViewById(R.id.txt_Designation);
            txt_mobile_no = (TextView) view.findViewById(R.id.txt_mobile_no);
            txt_qualification = (TextView) view.findViewById(R.id.txt_qualification);
            txt_email_id = (TextView) view.findViewById(R.id.txt_email_id);
            ll_main = (LinearLayout) view.findViewById(R.id.ll_main);
        }
    }
}