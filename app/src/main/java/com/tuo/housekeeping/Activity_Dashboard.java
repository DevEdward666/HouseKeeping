package com.tuo.housekeeping;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.tuo.housekeeping.model.AppVersionModel;
import com.tuo.housekeeping.model.AppVersionResponse;
import com.tuo.housekeeping.model.LoginResponse;
import com.tuo.housekeeping.model.LoginUserModel;
import com.tuo.housekeeping.model.TokenModel;
import com.tuo.housekeeping.model.TokenResponse;
import com.tuo.housekeeping.model.TotalJobs;
import com.tuo.housekeeping.model.TotalJobsFinished;
import com.tuo.housekeeping.model.TotalJobsFinishedResponse;
import com.tuo.housekeeping.model.TotalJobsResponse;
import com.tuo.housekeeping.model.UserModel;
import com.tuo.housekeeping.model.UserModelResponse;
import com.tuo.housekeeping.services.APIClient;
import com.tuo.housekeeping.storage.SharedPrefManager;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Dashboard extends AppCompatActivity{
    CardView card_pending,card_Logout,card_completed,card_rooms;
    TextView txt_empname,txt_dashusername,txt_total_pending,txt_total_pending_JO;
    private ProgressDialog dialog;
    LoginUserModel loginUserModel= SharedPrefManager.getInstance(this).getUser();
    String housekeeperid;
     String token;
     String auth,auth2="";
     String refresh= loginUserModel.getRefresh_token();
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    DownloadManager downloadmanager;
    long enq;
    String version,appversion;
    private Timer timer = new Timer();
    private TimerTask timerTask;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Handler handler = new android.os.Handler();
       // isNetworkConnectionAvailable();
        dialog = new ProgressDialog(this);
        txt_empname=findViewById(R.id.txt_dashempname);
        txt_dashusername=findViewById(R.id.txt_dashusername);
        txt_total_pending=findViewById(R.id.txt_total_pending);
        txt_total_pending_JO=findViewById(R.id.txt_total_pending_JO);

        card_pending=findViewById(R.id.card_pending);
        card_Logout=findViewById(R.id.card_logout);
        card_completed=findViewById(R.id.card_completed);
        card_rooms=findViewById(R.id.card_rooms);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("Dashboard");

            getdefaults();
            getAppVersion();
            fetchData();
        }

        timerTask = new TimerTask() {
            @Override
            public void run() {
                getdefaults();
            }
        };
        timer.schedule(timerTask, 500, 1000);

        StrictMode.setThreadPolicy(policy);
        downloadmanager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    long downloadId = intent.getLongExtra(
                            DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                    openDownloadedAttachment(context, downloadId);
                }
            }

        };
        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));



            card_completed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent;
                        if(txt_empname.getText().equals("Orderly")){
                            intent = new Intent(Activity_Dashboard.this, Activity_Orderly.class);
                        }else{
                            intent = new Intent(Activity_Dashboard.this, Activity_completed.class);
                        }
                        Bundle bundle = new Bundle();
                        bundle.putString("housekeeperid",housekeeperid);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            card_pending.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Activity_Dashboard.this, Activity_pending.class);

                        Bundle bundle = new Bundle();
                        bundle.putString("housekeeperid",housekeeperid);
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }
                });
            card_rooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_Dashboard.this, Activity_Rooms.class);
                Bundle bundle = new Bundle();
                bundle.putString("housekeeperid",housekeeperid);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        card_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefManager.getInstance(Activity_Dashboard.this).clear();
                Intent intent=new Intent(Activity_Dashboard.this, Activity_Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }

    public void fetchData()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }

        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d("key_test_1", "Key: " + key + " Value: " + value);
            }
        }
        // [END handle_data_extras]


        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = token;
                        Log.d("fcm_token", msg);
                        //Toast.makeText(Activity_Dashboard.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
        // [END retrieve_current_token]


    }
    public void checkNetworkConnection(){
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("No internet Connection");
        builder.setMessage("Please turn on internet connection to continue");
        builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public boolean isNetworkConnectionAvailable(){
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if(isConnected) {
            Log.d("Network", "Connected");
            return true;
        }
        else{
            checkNetworkConnection();
            Log.d("Network","Not Connected");
            return false;
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        if(!SharedPrefManager.getInstance(this).isLoggedIn() && txt_dashusername.getText().length()<0 && housekeeperid.length()<0){
            Intent intent=new Intent(this,Activity_Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }



    public void gettotal_pending_JO(String empid) {
        Call<TotalJobsResponse> call2 = APIClient.getInstance().getApi().total_pending_JO("Bearer " + auth,empid);
        call2.enqueue(new Callback<TotalJobsResponse>() {
            @Override
            public void onResponse(Call<TotalJobsResponse> call2, Response<TotalJobsResponse> response) {

                List<TotalJobs> totalJobs = response.body().getData();
                for(int i=0;i<totalJobs.size();i++) {
                    txt_total_pending_JO.setText(totalJobs.get(i).getTotal_pending_JO());
                }


            }

            @Override
            public void onFailure(Call<TotalJobsResponse> call, Throwable t) {
                Toast.makeText(Activity_Dashboard.this, "Network Error", Toast.LENGTH_SHORT).show();


            }
        });
    }
    public void gettotal_pending_routine(String empid) {
        Call<TotalJobsFinishedResponse> call2 = APIClient.getInstance().getApi().getTotal_pending_Routine("Bearer " + auth,empid);
        call2.enqueue(new Callback<TotalJobsFinishedResponse>() {
            @Override
            public void onResponse(Call<TotalJobsFinishedResponse> call2, Response<TotalJobsFinishedResponse> response) {

                List<TotalJobsFinished> totalJobsFinished = response.body().getData();
                for(int i=0;i<totalJobsFinished.size();i++){
                    txt_total_pending.setText(totalJobsFinished.get(i).getTotal_pending_routine());

                }



            }

            @Override
            public void onFailure(Call<TotalJobsFinishedResponse> call, Throwable t) {
                Toast.makeText(Activity_Dashboard.this, "Network Error", Toast.LENGTH_SHORT).show();


            }
        });
    }
    public void gettotal_pending_orderly(String empid) {
        Call<TotalJobsResponse> callAsync = APIClient.getInstance().getApi().gettotal_pending_orderly("Bearer " + auth,empid);
        callAsync.enqueue(new Callback<TotalJobsResponse>() {
            @Override
            public void onResponse(Call<TotalJobsResponse> call2, Response<TotalJobsResponse> response) {
                if(response.isSuccessful()){
                    List<TotalJobs> totalJobs = response.body().getData();
                      for(int i=0;i<totalJobs.size();i++){
                          txt_total_pending_JO.setText(totalJobs.get(i).getTotal_orderly_pending());
                      }
                    }
                            else
                    {
                           System.out.println("Request Error :: " + response.errorBody());
                    }




            }

            @Override
            public void onFailure(Call<TotalJobsResponse> call, Throwable t) {
                Toast.makeText(Activity_Dashboard.this, "Network Error", Toast.LENGTH_SHORT).show();


            }
        });
    }
    void installNewVersion() {

        Uri uri = Uri.parse("http://192.168.1.40:45457/api/ns/getapkhousekeeping");
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setMimeType(getMimeType(uri.toString()));
        request.setTitle("Patient Medication Update");
        request.setDescription("Downloading");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,"patient_medication");
        enq=downloadmanager.enqueue(request);

    }
    private String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }
        return type;
    }
    private void openDownloadedAttachment(final Context context, final long downloadId) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);
        Cursor cursor = downloadManager.query(query);
        if (cursor.moveToFirst()) {
            int downloadStatus = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            String downloadLocalUri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
            String downloadMimeType = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE));
            if ((downloadStatus == DownloadManager.STATUS_SUCCESSFUL) && downloadLocalUri != null) {
                openDownloadedAttachment(context, Uri.parse(downloadLocalUri), downloadMimeType);
            }
        }
        cursor.close();
    }
    private void openDownloadedAttachment(final Context context, Uri attachmentUri, final String attachmentMimeType) {
        if(attachmentUri!=null) {
            // Get Content Uri.
            if (ContentResolver.SCHEME_FILE.equals(attachmentUri.getScheme())) {
                // FileUri - Convert it to contentUri.
                File file = new File(attachmentUri.getPath());
                attachmentUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID+".provider", file);;
            }

            Intent openAttachmentIntent = new Intent(Intent.ACTION_VIEW);
            openAttachmentIntent.setDataAndType(attachmentUri, attachmentMimeType);
            openAttachmentIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                context.startActivity(openAttachmentIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "error cant open file", Toast.LENGTH_LONG).show();
            }
        }
    }
    public void CheckAppVersion(){
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void getAppVersion() {
        CheckAppVersion();
        auth = loginUserModel.getAccess_token();
        Call<AppVersionResponse> call2 = APIClient.getInstance().getApi().AndroidVersion("Bearer " + auth,"HouseKeeping");
        call2.enqueue(new Callback<AppVersionResponse>() {
            @Override
            public void onResponse(Call<AppVersionResponse> call2, Response<AppVersionResponse> response) {

                List<AppVersionModel> appVersionModel = response.body().getData();
                if (appVersionModel != null) {
                    for(int i=0;i<appVersionModel.size();i++){
                        appversion = appVersionModel.get(i).getAppVersion();
                    }
                    if(!version.equals(appversion)){
                        installNewVersion();
                        dialog.setMessage("Installing New Update");
                        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        dialog.show();
                        dialog.setCancelable(false);
                    }
                } else {
                    Toast.makeText(Activity_Dashboard.this, "Network Error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<AppVersionResponse> call, Throwable t) {
                Toast.makeText(Activity_Dashboard.this, "Network Error", Toast.LENGTH_SHORT).show();


            }
        });

    }
    public void getdefaults() {

        if (auth2.equals("")) {
            auth = loginUserModel.getAccess_token();
            Call<UserModelResponse> call2 = APIClient.getInstance().getApi().db_get_active_user("Bearer " + auth);
            call2.enqueue(new Callback<UserModelResponse>() {
                @Override
                public void onResponse(Call<UserModelResponse> call2, Response<UserModelResponse> response) {
                    if (response.isSuccessful()){

                        UserModel userModels = response.body().getData();
                        if (userModels != null) {
                            txt_dashusername.setText(userModels.getEmpname());
                            txt_empname.setText(userModels.getUsertype());
                            housekeeperid = userModels.getUsername();
                            if(userModels.getUsertype().equals("Orderly")){
                                card_pending.setVisibility(View.GONE);
                                gettotal_pending_orderly(userModels.getUsername());
                            }else{

                                gettotal_pending_routine(userModels.getUsername());
                                gettotal_pending_JO(userModels.getUsername());
                                getFirebaseToken(userModels.getUsername());
                            }

                        } else {
                            auth = "";
                            Toast.makeText(Activity_Dashboard.this, "Network Error", Toast.LENGTH_SHORT).show();
                            refreshtoken();

                        }
                    }else {
                        auth = "";
                        Toast.makeText(Activity_Dashboard.this, "Network Error", Toast.LENGTH_SHORT).show();
                        refreshtoken();

                    }

                }

                @Override
                public void onFailure(Call<UserModelResponse> call, Throwable t) {
                    Toast.makeText(Activity_Dashboard.this, "Network Error", Toast.LENGTH_SHORT).show();


                }
            });
        }else{
            Call<UserModelResponse> call2 = APIClient.getInstance().getApi().db_get_active_user("Bearer " + auth2);
            call2.enqueue(new Callback<UserModelResponse>() {
                @Override
                public void onResponse(Call<UserModelResponse> call2, Response<UserModelResponse> response) {

                    UserModel userModels = response.body().getData();
                    if (userModels != null) {
                        txt_dashusername.setText(userModels.getEmpname());
                        txt_empname.setText(userModels.getUsertype());
                        housekeeperid = userModels.getUsername();

                    } else {
                        auth = "";
                        Toast.makeText(Activity_Dashboard.this, "Network Error", Toast.LENGTH_SHORT).show();
                        refreshtoken();

                    }

                }

                @Override
                public void onFailure(Call<UserModelResponse> call, Throwable t) {
                    Toast.makeText(Activity_Dashboard.this, "Network Error", Toast.LENGTH_SHORT).show();


                }
            });
        }
    }

    public void getFirebaseToken(String empid){
        Call<TokenResponse> call = APIClient.getInstance().getApi().gettoken( "Bearer " + auth,empid);
        call.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if (response.isSuccessful()){

                    List<TokenModel> tokenModel = response.body().getData();
                    if (tokenModel != null) {
                        for(int i=0;i<tokenModel.size();i++) {
                            token = tokenModel.get(i).getToken();
                        }
                    } else {
                        Toast.makeText(Activity_Dashboard.this, "Network Error", Toast.LENGTH_SHORT).show();


                    }
            }


        }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {

            }
        });
    }

    public void refreshtoken(){
        Call<LoginResponse> call = APIClient.getInstance().getApi().refreshToken( "refresh_token",refresh);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()){
                    auth2=response.body().getAccess_token();
                }else{
                    SharedPrefManager.getInstance(Activity_Dashboard.this).clear();
                    Intent intent=new Intent(Activity_Dashboard.this, Activity_Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }





            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                SharedPrefManager.getInstance(Activity_Dashboard.this).clear();
                Intent intent=new Intent(Activity_Dashboard.this, Activity_Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
    }

