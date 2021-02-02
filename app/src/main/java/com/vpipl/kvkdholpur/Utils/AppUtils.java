package com.vpipl.kvkdholpur.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vpipl.kvkdholpur.Login_Local_Activity;
import com.vpipl.kvkdholpur.MainActivity;
import com.vpipl.kvkdholpur.R;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mukesh on 25-05-2020.
 */
public class AppUtils {

    public static ProgressDialog progressDialog;
    public static boolean showLogs = true;
    public static Dialog dialog;

    public static String serviceAPIURL = "http://kvkdholpur.versatileitsolution.com/Webservice/Service.asmx/";
    public static String imageURL = "http://kvkdholpur.versatileitsolution.com/admin/";
    public static String newsimageURL = "http://kvkdholpur.versatileitsolution.com/admin/";

    public static String serviceAPIURL() {
        return (serviceAPIURL);
    }

    public static String imageURL() {
        return (imageURL);
    }

    public static String newsimageURL() {
        return (newsimageURL);
    }

    public static void hideKeyboardOnClick(Context con, View view) {
        try {
            InputMethodManager inputManager = (InputMethodManager) con.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
            AppUtils.showExceptionDialog(con);
        }
    }

    public static String getDeviceID(Context con) {
        String deviceid = "0000000000";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            deviceid = Settings.Secure.getString(con.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        }

        return (deviceid);
    }

    public static String getDateFromAPIDate(String date) {
        try {
            if (AppUtils.showLogs) Log.v("getFormatDate", "before date.." + date);
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);

            if (date.contains("/Date("))
                cal.setTimeInMillis(Long.parseLong(date.replace("/Date(", "").replace(")/", "")));
            else
                cal.setTimeInMillis(Long.parseLong(date.replace("/date(", "").replace(")/", "")));

            date = DateFormat.format("dd-MMM-yyyy", cal).toString().trim();

            if (AppUtils.showLogs) Log.v("getFormatDate", "after date.." + date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return date;
    }

    public static void alertDialogWithFinish(final Context context, String message) {
        try {
            final Dialog dialog = createDialog(context, true);
            TextView dialog4all_txt = (TextView) dialog.findViewById(R.id.txt_DialogTitle);
            dialog4all_txt.setText(message);
            dialog.findViewById(R.id.txt_submit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    //((Activity)context).//overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    ((Activity) context).finish();
                }
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
            AppUtils.showExceptionDialog(context);
        }
    }


    public static void alertDialog(Context context, String message) {
        try {
            final Dialog dialog = createDialog(context, true);
            TextView dialog4all_txt = (TextView) dialog.findViewById(R.id.txt_DialogTitle);
            dialog4all_txt.setText(message);
            dialog.findViewById(R.id.txt_submit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                }
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void alertDialogWithFinishHome(final Context context, String message) {
        try {
            final Dialog dialog = createDialog(context, true);
            TextView dialog4all_txt = dialog.findViewById(R.id.txt_DialogTitle);
            dialog4all_txt.setText(message);
            dialog.findViewById(R.id.txt_submit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(context, MainActivity.class);
                    //  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
            AppUtils.showExceptionDialog(context);
        }
    }

    public static Dialog createDialog(Context context, boolean single) {
        final Dialog dialog = new Dialog(context, R.style.ThemeDialogCustom);
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        if (single)
            dialog.setContentView(R.layout.custom_dialog_one);
        else
            dialog.setContentView(R.layout.custom_dialog_two);

        return dialog;
    }

    public static Bitmap getBitmapFromString(String imageString) {
        Bitmap bitmap = null;
        try {
            byte[] byteArray = Base64.decode(imageString, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static String getBase64StringFromBitmap(Bitmap bitmap) {
        String imageString = "";
        try {
            if (bitmap != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, stream);
                byte[] image = stream.toByteArray();
                if (AppUtils.showLogs)
                    Log.e("AppUtills", "Image Size after comress : " + image.length);
                imageString = Base64.encodeToString(image, Base64.DEFAULT);
            } else {
                imageString = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageString;
    }


    public static boolean isNetworkAvailable(Context context) {
        boolean connected = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();

            return connected;
        } catch (Exception e) {
            e.printStackTrace();
            AppUtils.showExceptionDialog(context);
        }
        return connected;
    }

    public static String callWebServiceWithMultiParam(Context con, List<NameValuePair> postParameters, String methodName, String pageName) {

        BufferedReader in = null;

        try {
            HttpClient client = new DefaultHttpClient();

            printQuery(pageName + " :: " + methodName, postParameters);

            String result = null;

            if (AppUtils.isNetworkAvailable(con)) {
                if (AppUtils.showLogs)
                    //Log.e(pageName, "Executing URL..." + AppUtils.serviceAPIURL() + methodName);
                    Log.e(pageName, "Executing URL..." + serviceAPIURL + methodName);
                //  HttpPost request = new HttpPost(AppUtils.serviceAPIURL() + methodName);
                HttpPost request = new HttpPost(serviceAPIURL + methodName);
                UrlEncodedFormEntity formEntity = null;
                try {
                    formEntity = new UrlEncodedFormEntity(postParameters);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                request.setEntity(formEntity);

                HttpResponse response = null;
                try {

                    response = client.execute(request);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                StringBuilder sb = new StringBuilder();
                String line;
                String NL = System.getProperty("line.separator");

                try {
                    while ((line = in.readLine()) != null) {
                        sb.append(line).append(NL);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    if (AppUtils.showLogs)
                        Log.e(pageName + "", "Response..." + methodName + "..... " + sb.toString().trim());

                    result = sb.toString().trim();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                AppUtils.alertDialog(con, con.getResources().getString(R.string.txt_networkAlert));
            }

            return result;

        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void printQuery(String pageName, List<NameValuePair> postParam) {
        try {
            String query = "";
            for (int i = 0; i < postParam.size(); i++) {
                query = query + " " + postParam.get(i).getName() + " : " + postParam.get(i).getValue();
            }

            if (AppUtils.showLogs) Log.e(pageName, "Executing Parameters..." + query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void alertDialogWithHomeFinish(final Context context, String message) {
        try {
            final Dialog dialog = createDialog(context, true);
            TextView dialog4all_txt = dialog.findViewById(R.id.txt_DialogTitle);
            dialog4all_txt.setText(message);
            dialog.findViewById(R.id.txt_submit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    //((Activity)context).//overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    context.startActivity(new Intent(context, MainActivity.class));
                    ((Activity) context).finish();
                }
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
            AppUtils.showExceptionDialog(context);
        }
    }

    public static boolean isValidMobileno(String s) {
        Pattern p = Pattern.compile("(0/91)?[6-9][0-9]{9}");
        Matcher m = p.matcher(s);
        return (m.find() && m.group().equals(s));
    }

    public static boolean isValidGSTNo(String s) {
        Pattern p = Pattern.compile("\\d{2}[A-Z]{5}\\d{4}[A-Z]{1}[A-Z\\d]{1}[Z]{1}[A-Z\\d]{1}");
        Matcher m = p.matcher(s);
        return (m.find() && m.group().equals(s));
    }

    public static String getPath(Uri uri, Context context) {
        if (uri == null)
            return null;

        if (AppUtils.showLogs) Log.d("URI", uri + "");
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String temp = cursor.getString(column_index);
            cursor.close();
            if (AppUtils.showLogs) Log.v("temp", "" + temp);
            return temp;
        } else
            return null;
    }

    public static void showExceptionDialog(Context con) {
        try {
            AppUtils.dismissProgressDialog();
            AppUtils.alertDialog(con, "Sorry, There seems to be some problem. Try again later");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showProgressDialog(Context conn) {
        try {

            //   displayLoader(conn);
            if (progressDialog != null) {
                if (!progressDialog.isShowing()) {
                    if (!((Activity) conn).isFinishing()) {
                        progressDialog.show();
                    }
                }
            } else {
//                progressDialog = ProgressDialogCustom_Layout.getCustomProgressDialog(conn);

                progressDialog = new ProgressDialog(conn);
                progressDialog.setMessage("Please Wait...");
                progressDialog.setTitle("Loading...");
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.setInverseBackgroundForced(false);
                progressDialog.show();

                if (!progressDialog.isShowing()) {
                    //   progressDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    progressDialog.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void finishLoader() {
        dialog.dismiss();
    }

    public static void dismissProgressDialog() {
        try {
            //  finishLoader();
            if (progressDialog != null) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getBitmapFromURL(String src) {
        Bitmap image = null;

        try {
            URL url = new URL(src);
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            System.out.println(e);
        }
        return image;
    }

    public static Bitmap imageView2Bitmap(ImageView view) {
        Bitmap bitmap = null;
        bitmap = ((BitmapDrawable) view.getDrawable()).getBitmap();
        return bitmap;
    }
    public static void loadImage(Context conn, String imageURL, ImageView imageView) {
        try {
            if (imageURL.equals("") && imageURL.isEmpty()) {
                imageURL = "https://www.kvkAlwar.com/design_img/kvkAlwar.png";
            }

            Glide.with(conn)
                    .load(imageURL)
                    .placeholder(R.drawable.ic_no_image)
                    .error(R.drawable.ic_no_image)
                    .fallback(R.drawable.ic_no_image)
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
  /*  public static ViewPropertyAnimation.Animator getAnimatorImageLoading() {
        ViewPropertyAnimation.Animator animationObject = null;
        try {
            animationObject = new ViewPropertyAnimation.Animator() {
                @Override
                public void animate(View view) {
                    view.setAlpha(0f);
                    ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
                    fadeAnim.setDuration(2500);
                    fadeAnim.start();
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
        return animationObject;
    }*/
  public static void showDialogSignOut(final Context con) {
      try {
          final Dialog dialog = AppUtils.createDialog(con, false);
          dialog.setCancelable(false);

          TextView txt_DialogTitle = (TextView) dialog.findViewById(R.id.txt_DialogTitle);
          txt_DialogTitle.setText(Html.fromHtml(con.getResources().getString(R.string.txt_signout_message)));

          TextView txt_submit = (TextView) dialog.findViewById(R.id.txt_submit);
          txt_submit.setText(con.getResources().getString(R.string.txt_signout_yes));
          txt_submit.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  try {
                      dialog.dismiss();

                      AppController.getSpUserInfo().edit().clear().commit();
                      AppController.getSpIsLogin().edit().clear().commit();

                      Intent intent = new Intent(con, Login_Local_Activity.class).putExtra("Entry_type" , "H");
                      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.putExtra("SendToHome",true);
                      con.startActivity(intent);
                      ((Activity) con).finish();

                  } catch (Exception e) {
                      e.printStackTrace();
                  }
              }
          });

          TextView txt_cancel = (TextView) dialog.findViewById(R.id.txt_cancel);
          txt_cancel.setTextColor(con.getResources().getColor(R.color.color_faculty));
          txt_cancel.setText(con.getResources().getString(R.string.txt_signout_no));
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
          AppUtils.showExceptionDialog(con);
      }
  }

}