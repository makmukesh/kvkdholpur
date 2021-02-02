package com.vpipl.kvkdholpur.Adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.vpipl.kvkdholpur.RemotePDFActivity;
import com.vpipl.kvkdholpur.R;
import com.vpipl.kvkdholpur.Utils.AppUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class Package_List_Adapter extends RecyclerView.Adapter<Package_List_Adapter.MyViewHolder> {
    public ArrayList<HashMap<String, String>> array_list;
    LayoutInflater inflater = null;
    Context context;
    String str_filename;

    public Package_List_Adapter(Context con, ArrayList<HashMap<String, String>> list) {
        array_list = list;
        inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        context = con;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(inflater.inflate(R.layout.packagelist_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {
            holder.txt_name.setText(array_list.get(position).get("Name"));

            AppUtils.loadImage(context, array_list.get(position).get("Photo_Url"), holder.iv_photo);

            holder.txt_name.setSelected(true);
            holder.txt_name.setSingleLine(true);

            holder.ll_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  /* context.startActivity(new Intent(context, pdfViewerActivity.class).putExtra("URL",
                            "https://docs.google.com/gview?embedded=true&url=" +
                                    AppUtils.imageURL() + array_list.get(position).get("FileURL")));*/
                    context.startActivity(new Intent(context, RemotePDFActivity.class).putExtra("URL",
                            AppUtils.imageURL() + array_list.get(position).get("FileURL")));
                }
            });

            holder.ll_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  //  str_filename = "http://versatileitsolution.com/appupload/Package1%20Kharif%20krishi%20karyamala.pdf";
                    str_filename = AppUtils.imageURL() + array_list.get(position).get("FileURL");

                   // String message = "Package1%20Kharif%20krishi%20karyamala.pdf";
                    String message = array_list.get(position).get("FileURL").replaceAll("UploadImage/" , "") ;
                    message = Environment.getExternalStorageDirectory() + File.separator + "KVKALWAR/" + message;
                    Log.e("PDFURL1", message);
                    File file = new File(message);

                    PackageManager packageManager = context.getPackageManager();
                    Intent testIntent = new Intent(Intent.ACTION_VIEW);
                    testIntent.setType("application/pdf");
                    List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
                    if (list.size() > 0 && file.isFile()) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        Uri uri = Uri.fromFile(file);
                        intent.setDataAndType(uri, "application/pdf");
                        context.startActivity(intent);
                    } else {
                        new DownloadFile().execute(str_filename, "maven.pdf");
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
        ImageView iv_photo;
        LinearLayout ll_download , ll_view;

        public MyViewHolder(View view) {
            super(view);
            iv_photo = (ImageView) view.findViewById(R.id.iv_photo);
            txt_name = (TextView) view.findViewById(R.id.txt_name);
            ll_download = (LinearLayout) view.findViewById(R.id.ll_download);
            ll_view = (LinearLayout) view.findViewById(R.id.ll_view);
        }
    }

    public class DownloadFile extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;
        private boolean isDownloaded;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(context);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // getting file length
                int lengthOfFile = connection.getContentLength();


                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                //  String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

                //Extract file name from URL
                fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length());

                //Append timestamp to file name

                //   fileName = timestamp + "_" + fileName;

                //External directory path to save file
                folder = Environment.getExternalStorageDirectory() + File.separator + "KVKALWAR/";

                str_filename = "KVKALWAR/" + fileName;

                //Create androiddeft folder if it does not exist
                File directory = new File(folder);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Output stream to write file
                OutputStream output = new FileOutputStream(folder + fileName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    Log.d(TAG, "Progress: " + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
                // return "Downloaded at: " + folder + fileName;
                return "" + folder + fileName;

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return "Something went wrong";
        }


        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String message) {
            // dismiss the dialog after the file was downloaded
            this.progressDialog.dismiss();

            // Display File path after downloading

            alertDialog(context, message, str_filename);
            Toast.makeText(context.getApplicationContext(),
                    message, Toast.LENGTH_LONG).show();
        }
    }

    public static void alertDialog(final Context context, final String message, final String str_filename) {
        try {
            final Dialog dialog = AppUtils.createDialog(context, false);
            TextView dialog4all_txt = (TextView) dialog.findViewById(R.id.txt_DialogTitle);
            //   dialog4all_txt.setText(message);
            //  dialog4all_txt.setText(str_filename);
            dialog4all_txt.setText("Your file completed download and view from file manager!!");

            TextView txt_submit = (TextView) dialog.findViewById(R.id.txt_submit);
            txt_submit.setText("Open");
            txt_submit.setVisibility(View.GONE);
            txt_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("PDFURL2", message);
                    File file = new File(message);

                    PackageManager packageManager = context.getPackageManager();
                    Intent testIntent = new Intent(Intent.ACTION_VIEW);
                    testIntent.setType("application/pdf");
                    List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
                    if (list.size() > 0 && file.isFile()) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        Uri uri = Uri.fromFile(file);
                        intent.setDataAndType(uri, "application/pdf");
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "PDF Reader not available !!", Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }
            });
            TextView txt_cancel = (TextView) dialog.findViewById(R.id.txt_cancel);
            txt_cancel.setText("Ok");
            txt_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}