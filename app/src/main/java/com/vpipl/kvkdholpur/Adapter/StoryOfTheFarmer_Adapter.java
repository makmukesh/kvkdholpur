package com.vpipl.kvkdholpur.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.squareup.picasso.Picasso;
import com.vpipl.kvkdholpur.PhotoGalleryDetailsActivity;
import com.vpipl.kvkdholpur.R;
import com.vpipl.kvkdholpur.Utils.AppUtils;
import com.vpipl.kvkdholpur.YoutubePlayerActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StoryOfTheFarmer_Adapter extends RecyclerView.Adapter<StoryOfTheFarmer_Adapter.MyViewHolder> {
    public ArrayList<HashMap<String, String>> photo_list;
    LayoutInflater inflater = null;
    Context context;
    String str_filename;
    final String youTubeUrlRegEx = "^(https?)?(://)?(www.)?(m.)?((youtube.com)|(youtu.be))/";
    final String[] videoIdRegex = {"\\?vi?=([^&]*)", "watch\\?.*v=([^&]*)", "(?:embed|vi?)/([^/?]*)", "^([A-Za-z0-9\\-]*)"};

    public StoryOfTheFarmer_Adapter(Context con, ArrayList<HashMap<String, String>> list) {
        photo_list = list;
        inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        context = con;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(inflater.inflate(R.layout.storyofthefarmer_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {

            if (!photo_list.get(position).get("Name").equalsIgnoreCase("")) {
                holder.txt_name.setVisibility(View.VISIBLE);
            } else {
                holder.txt_name.setVisibility(View.GONE);
            }
            holder.txt_name.setSelected(true);
            holder.txt_name.setSingleLine(true);

            String html = photo_list.get(position).get("Name");
            holder.txt_name.setText(Html.fromHtml(html));

            //  AppUtils.loadImage(context, photo_list.get(position).get("Photo_Url"), holder.iv_photo);
            if (photo_list.get(position).get("Type").equalsIgnoreCase("I")) {
                Picasso.with(context)
                        .load(photo_list.get(position).get("Photo_Url"))
                        .placeholder(R.drawable.logo)
                        .into(holder.iv_photo);
                holder.iv_video.setVisibility(View.GONE);
                holder.iv_photo.setAlpha(1.0f);
            } else if (photo_list.get(position).get("Type").equalsIgnoreCase("V")) {
                String videoId = "" + extractVideoIdFromUrl(photo_list.get(position).get("Photo_Url"));
                String img_url = "http://img.youtube.com/vi/" + videoId + "/0.jpg"; // this is link which will give u thumnail image of that video

                Picasso.with(context)
                        .load(img_url)
                        .placeholder(R.drawable.logo)
                        .into(holder.iv_photo);
                holder.iv_video.setVisibility(View.VISIBLE);
                holder.iv_photo.setAlpha(0.5f);
            }

            /*Picasso.with(context)
                    .load(img_url)
                    .placeholder(R.drawable.logo)
                    .into(holder.iv_photo);*/

            holder.ll_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (photo_list.get(position).get("Type").equalsIgnoreCase("I")) {
                        Intent intent = new Intent(context, PhotoGalleryDetailsActivity.class);
                        // intent.putExtra("id" , photo_list.get(position).get("id"));
                        intent.putExtra("id", "" + position);
                        intent.putExtra("List", photo_list);
                        context.startActivity(intent);
                    } else if (photo_list.get(position).get("Type").equalsIgnoreCase("V")) {
                        Intent intent = new Intent(context, YoutubePlayerActivity.class);
                        // intent.putExtra("id" , photo_list.get(position).get("id"));
                        intent.putExtra("id", "" + position);
                        // intent.putExtra("videoUrl", photo_list.get(position).get("Photo_Url"));
                        intent.putExtra("videoUrl", "" + photo_list.get(position).get("Photo_Url"));
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
        return photo_list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_name;
        ImageView iv_photo;
        LinearLayout ll_main;
        ImageView iv_video;

        public MyViewHolder(View view) {
            super(view);
            iv_photo = (ImageView) view.findViewById(R.id.iv_photo);
            txt_name = (TextView) view.findViewById(R.id.txt_name);
            ll_main = (LinearLayout) view.findViewById(R.id.ll_main);
            iv_video = (ImageView) view.findViewById(R.id.iv_video);
        }
    }

    public String extractVideoIdFromUrl(String url) {
        String youTubeLinkWithoutProtocolAndDomain = youTubeLinkWithoutProtocolAndDomain(url);

        for (String regex : videoIdRegex) {
            Pattern compiledPattern = Pattern.compile(regex);
            Matcher matcher = compiledPattern.matcher(youTubeLinkWithoutProtocolAndDomain);

            if (matcher.find()) {
                return matcher.group(1);
            }
        }

        return null;
    }

    private String youTubeLinkWithoutProtocolAndDomain(String url) {
        Pattern compiledPattern = Pattern.compile(youTubeUrlRegEx);
        Matcher matcher = compiledPattern.matcher(url);

        if (matcher.find()) {
            return url.replace(matcher.group(), "");
        }
        return url;
    }
}