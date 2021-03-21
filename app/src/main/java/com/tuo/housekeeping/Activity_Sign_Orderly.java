package com.tuo.housekeeping;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.device.ScanDevice;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.loader.content.CursorLoader;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.tuo.housekeeping.model.LoginUserModel;
import com.tuo.housekeeping.services.APIClient;
import com.tuo.housekeeping.storage.SharedPrefManager;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class Activity_Sign_Orderly  extends AppCompatActivity {
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    private GestureOverlayView gestureOverlayView = null;
    String imagePath, filepath, pathToFile, room, spot_name, bed, area_name, routine_id, empid, housekeeperid = "", status, notes, date_requested_info,joborderid,patname,patno;
    TextView txt_room, txt_status, txt_bed, txt_requesition_sign, txt_notes, txt_sign_ns,txt_patname,txt_patno;

    private FloatingActionButton btn_approved_scan, btn_approved, btn, btn_sign, btn_accept;
    private ProgressDialog dialog;
    private ImageView img;
    private CardView card_status_sign;
    ScanDevice sm;
   public View v;
    private MaterialAlertDialogBuilder materialAlertDialogBuilder;
    private final static String SCAN_ACTION = "scan.rcv.message";
    private String barcodeStr;
    LoginUserModel loginUserModel = SharedPrefManager.getInstance(this).getUser();
    public BroadcastReceiver mScanReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            byte[] barocode = intent.getByteArrayExtra("barocode");
            int barocodelen = intent.getIntExtra("length", 0);
            byte temp = intent.getByteExtra("barcodeType", (byte) 0);
            byte[] aimid = intent.getByteArrayExtra("aimid");
            barcodeStr = new String(barocode, 0, barocodelen);

            // Toast.makeText(Activity_Signature.this,"Task Completed",Toast.LENGTH_LONG);

            if (txt_status.getText().equals("pending") && txt_sign_ns.getText().equals(barcodeStr.toLowerCase())) {

                Toast.makeText(Activity_Sign_Orderly.this, "Task Completed", Toast.LENGTH_LONG);
                //updatestatusongoing(v);
                sm.closeScan();
            } else if (txt_status.getText().equals("cleaning") && txt_sign_ns.getText().equals(barcodeStr.toLowerCase())) {
                checkPermissionAndSaveSignature();
                sm.closeScan();

            } else {

                updatejoborder(empid,"AK","acknowledge");
                // updatestatusacknowledge(barcodeStr);
                //wrong(context);
            }
            System.out.println("openScanner = " + txt_status.getText());
            sm.stopScan();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderly_joborder);
        ActionBar actionBar = getSupportActionBar();
        sm = new ScanDevice();
        sm.setOutScanMode(0);
        sm.openScan();
        Bundle extras = getIntent().getExtras();
        housekeeperid = getIntent().getExtras().getString("housekeeperid");
        status = extras.getString("status");
        notes = extras.getString("notes");
        date_requested_info = extras.getString("date_requested");
        empid = extras.getString("empid");
        room = extras.getString("room");
        bed = extras.getString("bed");
        patname = extras.getString("patname");
        patno = extras.getString("patno");
        joborderid=extras.getString("orderly_jo_id");

        materialAlertDialogBuilder=new MaterialAlertDialogBuilder(this,R.style.AlertDialogTheme);

        dialog = new ProgressDialog(Activity_Sign_Orderly.this);

        btn_approved = findViewById(R.id.btn_approve_joborder_orderly);
        btn_approved_scan = findViewById(R.id.btn_approve_scan_joborder_orderly);
        btn_accept = findViewById(R.id.btn_accept_joborder_orderly);
        btn = findViewById(R.id.btn_cancel_joborder_orderly);
        txt_notes = findViewById(R.id.txtnotes_joborder_orderly);
        img = findViewById(R.id.img_signage_joborder_orderly);
        txt_status = findViewById(R.id.txtstatus_sign_joborder_orderly);
        txt_room = findViewById(R.id.txt_Room_joborder_orderly);
        txt_bed = findViewById(R.id.txt_Bed_joborder_orderly);
        txt_requesition_sign = findViewById(R.id.txt_requesition_sign_orderly);
        card_status_sign = findViewById(R.id.card_status_sign_joborder_orderly);
        txt_patno=findViewById(R.id.txt_patno_joborder_orderly);
        txt_patname=findViewById(R.id.txt_patname_joborder_orderly);

        txt_status.setText(status);
        txt_room.setText(room);
        txt_requesition_sign.setText(date_requested_info);
        txt_bed.setText(bed);
        txt_notes.setText(notes);
        txt_patno.setText(patno);
        txt_patname.setText(patname);


        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = df.format(c);

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                materialAlertDialogBuilder
                        .setTitle("Accept Task")
                        .setMessage("Are you sure?")
                        .setNeutralButton(getResources().getString(R.string.cancel),new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();

                            }
                        })
                        .setPositiveButton(getResources().getString(R.string.accept),new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                updatejoborder(empid,"A","accept");
                                ongoingtask(v);
                            }
                        })
                        .show();

            }
        });
        if (actionBar != null) {
            actionBar.setTitle("Task Information");
            actionBar.setDisplayHomeAsUpEnabled(true);
            // actionBar.setHomeAsUpIndicator(R.mipmap.ic_housekeepingicon);
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Activity_Sign_Orderly.this, "test", Toast.LENGTH_LONG);
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
        });

        setTitle("");
        init();
        gestureOverlayView.addOnGesturePerformedListener(new CustomGestureListener());

        btn_approved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Toast.makeText(Activity_Sign_Orderly.this, "test", Toast.LENGTH_LONG);
                // checkPermissionAndSaveSignature(); PAG NAAY SIGNATURE UNCOMMENT
//                Log.d("routine_job_id",routine_job_id);
                //updatestatus(v); //CHANGE STATUS TO FINISHED FOR NOW
                materialAlertDialogBuilder
                        .setTitle("Finished Task")
                        .setMessage("Are you sure?")
                        .setNeutralButton(getResources().getString(R.string.cancel),new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();

                            }
                        })
                        .setPositiveButton(getResources().getString(R.string.accept),new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                updatejoborder(empid,"F","finished");
                                finishedTask(v);
                            }
                        })
                        .show();

            }
        });
        btn_approved_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sm.startScan();
                //ACKNOWLEDGE TASK FOR THIS SECTION
            }
        });

        if (txt_status.getText().toString().equals("finished")) {
//            getsignature(joborder.trim());
            btn.setVisibility(View.GONE);
            btn_approved.setVisibility(View.GONE);
            btn_accept.setVisibility(View.GONE);
            card_status_sign.setCardBackgroundColor(Color.rgb(161, 247, 177));
            txt_status.setTextColor(Color.rgb(24, 173, 52));
        } else if (txt_status.getText().toString().equals("accept")) {
            btn.setVisibility(View.VISIBLE);
            btn_accept.setVisibility(View.GONE);
            btn_approved.setVisibility(View.VISIBLE);
            card_status_sign.setCardBackgroundColor(Color.rgb(227, 242, 253));
            txt_status.setTextColor(Color.rgb(13, 71, 161));
        } else if (txt_status.getText().toString().equals("pending")) {
            btn.setVisibility(View.VISIBLE);
            btn_accept.setVisibility(View.VISIBLE);
            btn_approved.setVisibility(View.GONE);
            card_status_sign.setCardBackgroundColor(Color.rgb(255, 243, 224));
            txt_status.setTextColor(Color.rgb(230, 81, 0));
        }  else{
            btn.setVisibility(View.GONE);
            btn_accept.setVisibility(View.GONE);
            btn_approved.setVisibility(View.GONE);
            btn_approved_scan.setVisibility(View.GONE);
            card_status_sign.setCardBackgroundColor(Color.rgb(227,242,253));
            txt_status.setTextColor(Color.rgb(13,71,161));
        }

    }
    public void updatejoborder(String username,String sts_id,String sts_desc)
    {
        String auth= loginUserModel.getAccess_token();
        Call<ResponseBody> call= APIClient.getInstance().getApi().updatetojoborder_orderly("Bearer "+auth,username,joborderid,sts_id,sts_desc);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                txt_status.setText("finished");
                // acknowledgeTask(v);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.setMessage(t.getMessage());
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.show();
                dialog.setCancelable(true);


            }
        });

    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mScanReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(SCAN_ACTION);
        registerReceiver(mScanReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sm != null) {
            sm.stopScan();
            sm.setScanLaserMode(8);
            sm.closeScan();
        }
    }
    private void dispatchpics(){
        Intent takepic=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takepic.resolveActivity(getPackageManager())!=null){
            File photofile=null;
            photofile=createPhotoFile();
            if(photofile!=null){
                pathToFile=photofile.getAbsolutePath();
                Uri photoURI= FileProvider.getUriForFile(Activity_Sign_Orderly.this,"com.tuo.housekeeping.fileprovider",photofile);
                takepic.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
                startActivityForResult(takepic,1);
            }
        }
    }
    private File createPhotoFile(){
        String name=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir=getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image=null;
        try{
            image=File.createTempFile(name,".jpg",storageDir);
        }catch (Exception e){
            Log.d("myLog","Excep"+e.toString());
        }
        return image;

    }
    private String getRealPathFromUri(Uri uri){
        String[] projection={MediaStore.Images.Media.DATA};
        CursorLoader loader=new CursorLoader(getApplicationContext(),uri,projection,null,null,null);
        Cursor cursor=loader.loadInBackground();
        int column_idx=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result=cursor.getString(column_idx);
        cursor.close();
        return result;
    }
    private void init()
    {
        if(gestureOverlayView==null)
        {
            gestureOverlayView = (GestureOverlayView)findViewById(R.id.sign_pad_orderly);
        }
        if(btn_approved==null)
        {
            btn_approved = findViewById(R.id.btn_approve);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                onRestart();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void finishedTask(View v){
        Snackbar.make(v, "Task Successfully Finished", Snackbar.LENGTH_SHORT).show();
    }
    public void acknowledgeTask(View v){
        Snackbar.make(v, "Task Acknowledge", Snackbar.LENGTH_SHORT).show();
    }
    public void wrong(View v){
        Snackbar.make(v, "Task Not Finished", Snackbar.LENGTH_SHORT).show();
    }
    public void ongoingtask(View v){
        Snackbar.make(v, "Task has been accept", Snackbar.LENGTH_SHORT).show();
    }
    private void checkPermissionAndSaveSignature()
    {
        try {

            // Check whether this app has write external storage permission or not.
            int writeExternalStoragePermission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

            // If do not grant write external storage permission.
            if(writeExternalStoragePermission!= PackageManager.PERMISSION_GRANTED)
            {
                // Request user to grant write external storage permission.
                ActivityCompat.requestPermissions(Activity_Sign_Orderly.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
            }else
            {
                saveSignature();
            }

        } catch (Exception e) {
            Log.v("Signature Gestures", e.getMessage());
            e.printStackTrace();
        }
    }
    private void saveSignature()
    {
        String name=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        try {
            String auth= loginUserModel.getAccess_token();
            // First destroy cached image.
            gestureOverlayView.destroyDrawingCache();
            // Enable drawing cache function.
            gestureOverlayView.setDrawingCacheEnabled(true);
            // Get drawing cache bitmap.
            Bitmap drawingCacheBitmap = gestureOverlayView.getDrawingCache();
            // Create a new bitmap
            Bitmap bitmap = Bitmap.createBitmap(drawingCacheBitmap);
            String filePath = Environment.getExternalStorageDirectory().toString();
            File file = new File(filePath+"/"+"Joborderid-"+empid.trim()+".png");
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            // Compress bitmap to png image.
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            // Flush bitmap to image file.
            fileOutputStream.flush();
            // Close the output stream.
            fileOutputStream.close();

            File Newfile = new File(file+"/test.png");
            RequestBody photoContent=RequestBody.create(MediaType.parse("image/*"),file);
            MultipartBody.Part photo=MultipartBody.Part.createFormData("photo",file.getName(),photoContent);
            RequestBody description=RequestBody.create(MediaType.parse("text/plain"),"test");
            Call<RequestBody> call= APIClient.getInstance().getApi().upload("Bearer "+auth,photo,description);
            call.enqueue(new Callback<RequestBody>() {
                @Override
                public void onResponse(Call<RequestBody> call, Response<RequestBody> response) {
                    Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                    if(response.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Not Success"+response.body(), Toast.LENGTH_LONG).show();
                    }
                    Toast.makeText(Activity_Sign_Orderly.this,"Task Completed",Toast.LENGTH_LONG);
                    //  updatestatus(v);
                    fetchdata();

                }

                @Override
                public void onFailure(Call<RequestBody> call, Throwable t) {
                    Toast.makeText(Activity_Sign_Orderly.this,"Task Completed",Toast.LENGTH_LONG);
                    //  updatestatus(v);
                    fetchdata();
                }
            });

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Errors" + empid, Toast.LENGTH_LONG).show();
            Log.v("Signature Gestures", e.getMessage());
            e.printStackTrace();

        }
    }
    private void fetchdata(){
        Bundle extras = getIntent().getExtras();


        empid=empid;
        txt_status.setText(status);
        txt_notes.setText(notes);

    }
}
