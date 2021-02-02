package com.vpipl.kvkdholpur;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.vpipl.kvkdholpur.Utils.AppController;
import com.vpipl.kvkdholpur.Utils.AppUtils;
import com.vpipl.kvkdholpur.Utils.QueryUtils;
import com.vpipl.kvkdholpur.Utils.SPUtils;
import com.vpipl.kvkdholpur.Utils.Utility;

public class QueryActivity extends AppCompatActivity {
    private static final String TAG = "QueryActivity";
    private static final int READ_EXTERNAL_STORAGE = 786;

    ImageView imageCancelInActFeedback, iv_query_image, iv_voice, iv_voice_name;
    EditText edtxt_query, edtxt_query_name, edtxt_query_mobileno;
    Button btnSubmitInActFeedback;
    Activity act;
    private static final int REQUEST_CODE = 1234;
    String str_sts_voice = "N";
    Bitmap bitmap;
    String userChoosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String FeedbackInActFeedback , name , mobileno ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        try {
            getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
            );

            act = QueryActivity.this;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.white));
            }
            iv_voice = findViewById(R.id.iv_voice);
            iv_voice_name = findViewById(R.id.iv_voice_name);

            imageCancelInActFeedback = findViewById(R.id.imageCancelInActFeedback);
            edtxt_query = findViewById(R.id.edtxt_query);
            edtxt_query_name = findViewById(R.id.edtxt_query_name);
            edtxt_query_mobileno = findViewById(R.id.edtxt_query_mobileno);
            iv_query_image = findViewById(R.id.iv_query_image);
            btnSubmitInActFeedback = findViewById(R.id.btnSubmitInActFeedback);

            imageCancelInActFeedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            requestPermission();

            btnSubmitInActFeedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    edtxt_query.setError(null);
                    edtxt_query_name.setError(null);
                    FeedbackInActFeedback = edtxt_query.getText().toString();
                    name = edtxt_query_name.getText().toString();
                    mobileno = edtxt_query_name.getText().toString();

                    if (TextUtils.isEmpty(name)) {
                        AppUtils.alertDialog(act, "Name Required");
                        edtxt_query_name.requestFocus();
                    } else if (TextUtils.isEmpty(mobileno)) {
                        AppUtils.alertDialog(act, "Mobile no Required");
                        edtxt_query_mobileno.requestFocus();
                    } else if (TextUtils.isEmpty(FeedbackInActFeedback)) {
                        AppUtils.alertDialog(act, "Query Required");
                        edtxt_query.requestFocus();
                    } else {
                        if (AppUtils.isNetworkAvailable(act)) {
                            executeFeedBackRequest();
                        } else {
                            AppUtils.alertDialog(act, getResources().getString(R.string.txt_networkAlert));
                        }
                    }
                }
            });
            iv_query_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectImage();
                }
            });

            /*Searching by voice code*/

            // final Animation myAnim = AnimationUtils.loadAnimation(act, R.anim.bounce);
            // iv_voice.startAnimation(myAnim);

            PackageManager pm = getPackageManager();
            final List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);

            iv_voice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (activities.size() == 0) {
                        //   speak.setEnabled(false);
                        // speak.setText("Recognizer not present");
                        Toast.makeText(act, "Voice Recognizer not present", Toast.LENGTH_SHORT).show();
                    } else {
                        str_sts_voice = "Q";
                        startVoiceRecognitionActivity();
                    }
                }
            });
            iv_voice_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (activities.size() == 0) {
                        //   speak.setEnabled(false);
                        // speak.setText("Recognizer not present");
                        Toast.makeText(act, "Voice Recognizer not present", Toast.LENGTH_SHORT).show();
                    } else {
                        str_sts_voice = "N";
                        startVoiceRecognitionActivity();
                    }
                }
            });

            if (activities.size() == 0) {
                //  speak.setEnabled(false);
                // speak.setText("Recognizer not present");
                // Toast.makeText(this, "Recognizer not present", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE);
        } else {

        }
    }
    /**
     * Fire an intent to start the voice recognition activity.
     */
    private void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice searching...");
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void executeFeedBackRequest() {
        new AsyncTask<Void, Void, String>() {
            protected void onPreExecute() {
                AppUtils.showProgressDialog(act);
            }

            @Override
            protected String doInBackground(Void... params) {
                String response = "";
                try { //                     String FeedbackInActFeedback , name , mobileno ;
                    List<NameValuePair> postParameters = new ArrayList<>();
                    byte[] data = name.getBytes("UTF-8");
                    String nameconvert = Base64.encodeToString(data, Base64.DEFAULT);

                    byte[] data1 = FeedbackInActFeedback.getBytes("UTF-8");
                    String queryconvert = Base64.encodeToString(data1, Base64.DEFAULT);

                  //  postParameters.add(new BasicNameValuePair("CompID", "" + AppController.getSpUserInfo().getString(SPUtils.Company_ID, "")));
                  //  postParameters.add(new BasicNameValuePair("LID", "" + AppController.getSpUserInfo().getString(SPUtils.Member_ID, "")));
                    postParameters.add(new BasicNameValuePair("Name", "" + nameconvert ));
                    postParameters.add(new BasicNameValuePair("ContactNo", "" + AppController.getSpUserInfo().getString(SPUtils.MemberMobileNo, "")));
                    postParameters.add(new BasicNameValuePair("EmailID", "" ));
                    postParameters.add(new BasicNameValuePair("QueryText", "" + queryconvert));
                    postParameters.add(new BasicNameValuePair("FileURL", "" + AppUtils.getBase64StringFromBitmap(bitmap)));
                    postParameters.add(new BasicNameValuePair("Extension", "png"));
                    response = AppUtils.callWebServiceWithMultiParam(act, postParameters, QueryUtils.methodToInser_QueryNew, TAG);
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
                    JSONArray jsonArrayData = jsonObject.getJSONArray("Data");

                    if (jsonObject.getString("Status").equalsIgnoreCase("True")) {
                        //  getProductListResult(jsonArrayData);
                        AppUtils.alertDialogWithFinishHome(act, jsonObject.getString("Message"));
                    } else {
                        AppUtils.alertDialog(act, jsonObject.getString("Message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    /*Query image capture*/
    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(act);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
            else if (requestCode == REQUEST_CODE) {
                // Populate the wordsList with the String values the recognition engine thought it heard
                final ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (!matches.isEmpty()) {
                    if (str_sts_voice.equalsIgnoreCase("N")) {
                        edtxt_query_name.setText("");
                        String Query = matches.get(0);
                        edtxt_query_name.setText(Query);
                    } else if (str_sts_voice.equalsIgnoreCase("Q")) {
                        edtxt_query.setText("");
                        String Query = matches.get(0);
                        edtxt_query.setText(Query);
                    }
                    // speak.setEnabled(false);
                }
            }
        }
    }

    private void onCaptureImageResult(Intent data) {
        bitmap = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        String imageStoragePath = destination.getAbsolutePath();
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        iv_query_image.setImageBitmap(bitmap);

        Log.e("from camera data", imageStoragePath);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        bitmap = bm;
        iv_query_image.setImageBitmap(bm);
        String imagepath = bm.toString();
        Log.e("from gallery data", imagepath);
    }
}