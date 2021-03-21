package com.tuo.housekeeping;

import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tuo.housekeeping.adapters.PatientListAdapter;
import com.tuo.housekeeping.model.LoginUserModel;
import com.tuo.housekeeping.model.PatientData;
import com.tuo.housekeeping.model.PatientDataResponse;
import com.tuo.housekeeping.services.APIClient;
import com.tuo.housekeeping.storage.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Orderly extends AppCompatActivity implements PatientListAdapter.ItemClickListener,SwipeRefreshLayout.OnRefreshListener{
    PatientListAdapter adapter;
    public RecyclerView recyclertask,recyclertaskfinished,recyclertaskworking,recyclertaskacknowledge;
    private List<PatientData> patientData;
    SwipeRefreshLayout refreshpending,refreshwoking,refreshfinished,refreshacknowledge;
    String housekeeperid ="",tabstatus="";
    LoginUserModel loginUserModel= SharedPrefManager.getInstance(this).getUser();
    TabHost th_jobs;
    String auth= loginUserModel.getAccess_token();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joborder);
        housekeeperid= getIntent().getExtras().getString("housekeeperid");
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("Orderly Task");
            actionBar.setDisplayHomeAsUpEnabled(true);
            // actionBar.setHomeAsUpIndicator(R.mipmap.ic_housekeepingicon);
        }
        refreshpending=findViewById(R.id.refreshpending_joborder);
        refreshwoking=findViewById(R.id.refreshpending_joborder_working);
        refreshfinished=findViewById(R.id.refreshpending_joborder_finished);
        refreshacknowledge=findViewById(R.id.refreshpending_joborder_acknowledge);
        tabstatus="Pending";

        recyclertask=findViewById(R.id.tasklistrecycler_joborder);
        recyclertaskfinished=findViewById(R.id.tasklistrecycler_joborder_finished);
        recyclertaskworking=findViewById(R.id.tasklistrecycler_joborder_working);
        recyclertaskacknowledge=findViewById(R.id.tasklistrecycler_joborder_acknowledge);
        fetchdata();
        fetchDataworking();
        fetchDataFinished();
        fetchDataAcknowledge();

        th_jobs=findViewById(R.id.th_jobs_joborder);
        th_jobs.setup();
        TabHost.TabSpec tabSpec = th_jobs.newTabSpec("Pending");
        tabSpec.setContent(R.id.Pending);
        tabSpec.setIndicator("Pending");
        th_jobs.addTab(tabSpec);
        tabSpec = th_jobs.newTabSpec("Accept");
        tabSpec.setContent(R.id.Working);
        tabSpec.setIndicator("Accept");
        th_jobs.addTab(tabSpec);
        tabSpec = th_jobs.newTabSpec("Finished");
        tabSpec.setContent(R.id.Finished);
        tabSpec.setIndicator("Finished");
        th_jobs.addTab(tabSpec);

        tabSpec = th_jobs.newTabSpec("Signed");
        tabSpec.setContent(R.id.Signed);
        tabSpec.setIndicator("Signed");
        th_jobs.addTab(tabSpec);



        th_jobs.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
            @Override
            public void onTabChanged(String tabId) {
                if(tabId.equals("Pending")) {
                    tabstatus=tabId;
                    // fetchdata();
                }
                else if(tabId.equals("Finished")) {
                    tabstatus=tabId;
                    // fetchDataFinished();
                }else if(tabId.equals("Accept")){
                    tabstatus=tabId;
                    //  fetchDataworking();

                }
                else if(tabId.equals("Signed")){
                    tabstatus=tabId;
                    //  fetchDataworking();

                }
            }});






        refresh(refreshfinished);
        refresh(refreshpending);
        refresh(refreshwoking);
        refresh(refreshacknowledge);
    }


    private void refresh(final SwipeRefreshLayout refreshloading){

        refreshloading.setOnRefreshListener(this);
//        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
        refreshloading.setColorSchemeResources(android.R.color.holo_blue_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_blue_dark);
        refreshloading.post(new Runnable() {
            @Override
            public void run() {
                if(refreshloading != null) {

                }else{

                    fetchdata();
                    fetchDataFinished();
                    fetchDataworking();
                    fetchDataAcknowledge();
                    refreshloading.setRefreshing(false);
                }
            }
        });
    }
    private void fetchDataAcknowledge(){
     String auth= loginUserModel.getAccess_token();
        recyclertaskacknowledge=findViewById(R.id.tasklistrecycler_joborder_acknowledge);
        recyclertaskacknowledge.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Call<PatientDataResponse> call2 = APIClient.getInstance().getApi().getJoborderDtlsbyORDERLYAcknowledge("Bearer "+auth,housekeeperid);
        call2.enqueue(new Callback<PatientDataResponse>() {
            @Override
            public void onResponse(Call<PatientDataResponse> call2, Response<PatientDataResponse> response) {
                patientData=response.body().getData();
                adapter=new PatientListAdapter(getApplicationContext(),patientData);
                recyclertaskacknowledge.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<PatientDataResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void fetchDataFinished(){
        String auth= loginUserModel.getAccess_token();
        recyclertaskfinished=findViewById(R.id.tasklistrecycler_joborder_finished);
        recyclertaskfinished.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Call<PatientDataResponse> call2 = APIClient.getInstance().getApi().getJoborderDtlsbyORDERLYFinished("Bearer "+auth,housekeeperid);
        call2.enqueue(new Callback<PatientDataResponse>() {
            @Override
            public void onResponse(Call<PatientDataResponse> call2, Response<PatientDataResponse> response) {
                patientData=response.body().getData();
                adapter=new PatientListAdapter(getApplicationContext(),patientData);
                recyclertaskfinished.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<PatientDataResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void fetchDataworking(){
        String auth= loginUserModel.getAccess_token();
        recyclertaskworking=findViewById(R.id.tasklistrecycler_joborder_working);
        recyclertaskworking.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Call<PatientDataResponse> call2 = APIClient.getInstance().getApi().getJoborderDtlsbyORDERLYWorking("Bearer "+auth,housekeeperid);
        call2.enqueue(new Callback<PatientDataResponse>() {
            @Override
            public void onResponse(Call<PatientDataResponse> call2, Response<PatientDataResponse> response) {
                patientData=response.body().getData();
                adapter=new PatientListAdapter(getApplicationContext(),patientData);
                recyclertaskworking.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<PatientDataResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void fetchdata(){
        String auth= loginUserModel.getAccess_token();
        recyclertask=findViewById(R.id.tasklistrecycler_joborder);
        recyclertask.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Call<PatientDataResponse> call2 = APIClient.getInstance().getApi().getJoborderDtlsbyORDERLY("Bearer "+auth,housekeeperid);
        call2.enqueue(new Callback<PatientDataResponse>() {
            @Override
            public void onResponse(Call<PatientDataResponse> call2, Response<PatientDataResponse> response) {
                patientData=response.body().getData();
                adapter=new PatientListAdapter(getApplicationContext(),patientData);
                recyclertask.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<PatientDataResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onRefresh() {
        if(tabstatus=="Finished"){
            fetchDataFinished();
            refreshfinished.setRefreshing(false);
        }else if (tabstatus=="Accept"){
            fetchDataworking();
            refreshwoking.setRefreshing(false);
        }
        else if (tabstatus=="Signed"){
            fetchDataAcknowledge();
            refreshacknowledge.setRefreshing(false);
        }else{
            fetchdata();
            refreshpending.setRefreshing(false);
        }
    }
    @Override
    public void onRestart()
    {
        super.onRestart();
        if(tabstatus=="Finished"){
            fetchDataFinished();
            refreshfinished.setRefreshing(false);
        }else if (tabstatus=="Accept"){
            fetchDataworking();
            refreshwoking.setRefreshing(false);
        }else if (tabstatus=="Signed"){
            fetchDataAcknowledge();
            refreshacknowledge.setRefreshing(false);
        }else{
            fetchdata();
            refreshpending.setRefreshing(false);
        }
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}