package com.tuo.housekeeping;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TabHost;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tuo.housekeeping.adapters.JobOrderAdapterCompleted;
import com.tuo.housekeeping.model.JO;
import com.tuo.housekeeping.model.JOResponse;
import com.tuo.housekeeping.model.LoginUserModel;
import com.tuo.housekeeping.services.APIClient;
import com.tuo.housekeeping.storage.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_completed extends AppCompatActivity implements JobOrderAdapterCompleted.ItemClickListener, SwipeRefreshLayout.OnRefreshListener{
    JobOrderAdapterCompleted adapter;
    public RecyclerView recyclertask,recyclertask_working,recyclertask_finished,recyclertask_acknowledge;
    EditText txt_search;
    Button btn_completed;
    private List<JO> jobOrders;
    SwipeRefreshLayout refreshcompleted,refreshcompleted_working,refreshcompleted_acknowledge,refreshcompleted_finished;
    String housekeeperid ="",tabstatus="";;
    TabHost th_jobs;
    LoginUserModel loginUserModel= SharedPrefManager.getInstance(this).getUser();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joborder);
        housekeeperid= getIntent().getExtras().getString("housekeeperid");
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("Job Orders Task");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        tabstatus="Pending";
        th_jobs=findViewById(R.id.th_jobs_joborder);
        th_jobs.setup();
        TabHost.TabSpec tabSpec = th_jobs.newTabSpec("Pending");
        tabSpec.setContent(R.id.Pending);
        tabSpec.setIndicator("Pending");
        th_jobs.addTab(tabSpec);
        tabSpec = th_jobs.newTabSpec("Working");
        tabSpec.setContent(R.id.Working);
        tabSpec.setIndicator("Working");
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
                }else if(tabId.equals("Working")){
                    tabstatus=tabId;
                    //  fetchDataworking();

                }
                else if(tabId.equals("Signed")){
                    tabstatus=tabId;
                    //  fetchDataworking();

                }
            }});


        refreshcompleted=findViewById(R.id.refreshpending_joborder);
        refreshcompleted_working=findViewById(R.id.refreshpending_joborder_working);
        refreshcompleted_finished=findViewById(R.id.refreshpending_joborder_finished);
        refreshcompleted_acknowledge=findViewById(R.id.refreshpending_joborder_acknowledge);

        String auth= loginUserModel.getAccess_token();



        recyclertask=findViewById(R.id.tasklistrecycler_joborder);
        recyclertask_working=findViewById(R.id.tasklistrecycler_joborder_working);
        recyclertask_finished=findViewById(R.id.tasklistrecycler_joborder_finished);
        recyclertask_acknowledge=findViewById(R.id.tasklistrecycler_joborder_acknowledge);

        fetchcompleteddata();
        fetchcompleteddata_acknowledge();
        fetchcompleteddata_finished();
        fetchcompleteddata_working();
        recyclertask.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Call<JOResponse> call = APIClient.getInstance().getApi().getJoborderDtlsbyHK( "Bearer "+auth,housekeeperid);
        call.enqueue(new Callback<JOResponse>() {
            @Override
            public void onResponse(Call<JOResponse> call, Response<JOResponse> response) {
                jobOrders=response.body().getData();
                adapter=new JobOrderAdapterCompleted(getApplicationContext(),jobOrders);
                recyclertask.setAdapter(adapter);
                refreshcompleted.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<JOResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        refreshcompleted.setOnRefreshListener(this);
//        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
        refreshcompleted.setColorSchemeResources(android.R.color.holo_blue_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_blue_dark);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        refreshcompleted.post(new Runnable() {
            @Override
            public void run() {
                if(refreshcompleted != null) {

                }else{
                    fetchcompleteddata();
                    fetchcompleteddata_acknowledge();
                    fetchcompleteddata_finished();
                    fetchcompleteddata_working();
                    refreshcompleted.setRefreshing(false);
                    refreshcompleted_working.setRefreshing(false);
                    refreshcompleted_finished.setRefreshing(false);
                    refreshcompleted_acknowledge.setRefreshing(false);

                }
            }
        });
        refresh(refreshcompleted);
        refresh(refreshcompleted_acknowledge);
        refresh(refreshcompleted_finished);
        refresh(refreshcompleted_working);
//        txt_search.addTextChangedListener(new TextWatcher() {
//            String id=txt_search.getText().toString();
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(final CharSequence s, int start, int before, int count) {
//                if(txt_search.length()<=0){
//                    String auth= loginUserModel.getAccess_token();
//                    recyclertask=findViewById(R.id.taskcompletedlistrecycler);
//                    recyclertask.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//                    Call<JobOrdersResponse> call2 = APIClient.getInstance().getApi().dbGetHousekeepingJobOrdersCompleted( "Bearer "+auth);
//                    call2.enqueue(new Callback<JobOrdersResponse>() {
//                        @Override
//                        public void onResponse(Call<JobOrdersResponse> call2, Response<JobOrdersResponse> response) {
//                            jobOrders=response.body().getData();
//                            adapter=new JobOrderAdapterCompleted(getApplicationContext(),jobOrders);
//                            recyclertask.setAdapter(adapter);
//                            refreshcompleted.setRefreshing(false);
//                        }
//
//                        @Override
//                        public void onFailure(Call<JobOrdersResponse> call, Throwable t) {
//                            Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }else{
//                    String auth= loginUserModel.getAccess_token();
//                    recyclertask=findViewById(R.id.taskcompletedlistrecycler);
//                    recyclertask.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//                    Call<JobOrdersResponse> call = APIClient.getInstance().getApi().dbGetHousekeepingJobOrdersFILTER("Bearer "+auth,txt_search.getText().toString().trim());
//                    call.enqueue(new Callback<JobOrdersResponse>() {
//                        @Override
//                        public void onResponse(Call<JobOrdersResponse> call, Response<JobOrdersResponse> response) {
//
//                            jobOrders=response.body().getData();
//                            adapter=new JobOrderAdapterCompleted(getApplicationContext(),jobOrders);
//                            recyclertask.setAdapter(adapter);
//                            refreshcompleted.setRefreshing(false);
//                        }
//
//                        @Override
//                        public void onFailure(Call<JobOrdersResponse> call, Throwable t) {
//                            Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//            }
//
//
//            @Override
//            public void afterTextChanged(final Editable s) {
//                if(txt_search.length()<=0){
//                    String auth= loginUserModel.getAccess_token();
//                    recyclertask=findViewById(R.id.taskcompletedlistrecycler);
//                    recyclertask.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//                    Call<JobOrdersResponse> call2 = APIClient.getInstance().getApi().dbGetHousekeepingJobOrdersCompleted( "Bearer "+auth);
//                    call2.enqueue(new Callback<JobOrdersResponse>() {
//                        @Override
//                        public void onResponse(Call<JobOrdersResponse> call2, Response<JobOrdersResponse> response) {
//                            jobOrders=response.body().getData();
//                            adapter=new JobOrderAdapterCompleted(getApplicationContext(),jobOrders);
//                            recyclertask.setAdapter(adapter);
//                            refreshcompleted.setRefreshing(false);
//                        }
//
//                        @Override
//                        public void onFailure(Call<JobOrdersResponse> call, Throwable t) {
//                            Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }else{
//                    String auth = loginUserModel.getAccess_token();
//                    recyclertask = findViewById(R.id.taskcompletedlistrecycler);
//                    recyclertask.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//                    Call<JobOrdersResponse> call = APIClient.getInstance().getApi().dbGetHousekeepingJobOrdersFILTER("Bearer "+auth,txt_search.getText().toString().trim());
//                    call.enqueue(new Callback<JobOrdersResponse>() {
//                        @Override
//                        public void onResponse(Call<JobOrdersResponse> call, Response<JobOrdersResponse> response) {
//
//                            jobOrders = response.body().getData();
//                            adapter = new JobOrderAdapterCompleted(getApplicationContext(), jobOrders);
//                            recyclertask.setAdapter(adapter);
//                            refreshcompleted.setRefreshing(false);
//
//                        }
//
//                        @Override
//                        public void onFailure(Call<JobOrdersResponse> call, Throwable t) {
//                            Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//
//            }
//
//        });
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.completed_search,menu);
        MenuItem menuItem=menu.findItem(R.id.search_completed_icon);
        SearchView searchView= (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Seach Task");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                    String auth= loginUserModel.getAccess_token();
                    recyclertask=findViewById(R.id.taskcompletedlistrecycler);
                    recyclertask.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    Call<JOResponse> call2 = APIClient.getInstance().getApi().getJoborderDtlsbyHK( "Bearer "+auth,housekeeperid.trim());
                    call2.enqueue(new Callback<JOResponse>() {
                        @Override
                        public void onResponse(Call<JOResponse> call2, Response<JOResponse> response) {
                            jobOrders=response.body().getData();
                            adapter=new JobOrderAdapterCompleted(getApplicationContext(),jobOrders);
                            recyclertask.setAdapter(adapter);
                            refreshcompleted.setRefreshing(false);
                        }

                        @Override
                        public void onFailure(Call<JOResponse> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

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

                    fetchcompleteddata();
                    fetchcompleteddata_acknowledge();
                    fetchcompleteddata_finished();
                    fetchcompleteddata_working();
                    refreshloading.setRefreshing(false);
                }
            }
        });
    }
    private void fetchcompleteddata(){
        String auth= loginUserModel.getAccess_token();
        recyclertask=findViewById(R.id.tasklistrecycler_joborder);
        recyclertask.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Call<JOResponse> call2 = APIClient.getInstance().getApi().getJoborderDtlsbyHK( "Bearer "+auth,housekeeperid);
        call2.enqueue(new Callback<JOResponse>() {
            @Override
            public void onResponse(Call<JOResponse> call2, Response<JOResponse> response) {
                jobOrders=response.body().getData();
                adapter=new JobOrderAdapterCompleted(getApplicationContext(),jobOrders);
                recyclertask.setAdapter(adapter);
                refreshcompleted.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<JOResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void fetchcompleteddata_working(){
        String auth= loginUserModel.getAccess_token();
        recyclertask_working=findViewById(R.id.tasklistrecycler_joborder_working);
        recyclertask_working.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Call<JOResponse> call2 = APIClient.getInstance().getApi().getJoborderDtlsbyHK_working( "Bearer "+auth,housekeeperid);
        call2.enqueue(new Callback<JOResponse>() {
            @Override
            public void onResponse(Call<JOResponse> call2, Response<JOResponse> response) {
                jobOrders=response.body().getData();
                adapter=new JobOrderAdapterCompleted(getApplicationContext(),jobOrders);
                recyclertask_working.setAdapter(adapter);
                refreshcompleted_working.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<JOResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void fetchcompleteddata_finished(){
        String auth= loginUserModel.getAccess_token();
        recyclertask_finished=findViewById(R.id.tasklistrecycler_joborder_finished);
        recyclertask_finished.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Call<JOResponse> call2 = APIClient.getInstance().getApi().getJoborderDtlsbyHK_finished( "Bearer "+auth,housekeeperid);
        call2.enqueue(new Callback<JOResponse>() {
            @Override
            public void onResponse(Call<JOResponse> call2, Response<JOResponse> response) {
                jobOrders=response.body().getData();
                adapter=new JobOrderAdapterCompleted(getApplicationContext(),jobOrders);
                recyclertask_finished.setAdapter(adapter);
                refreshcompleted_finished.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<JOResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void fetchcompleteddata_acknowledge(){
        String auth= loginUserModel.getAccess_token();
        recyclertask_acknowledge=findViewById(R.id.tasklistrecycler_joborder_acknowledge);
        recyclertask_acknowledge.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Call<JOResponse> call2 = APIClient.getInstance().getApi().getJoborderDtlsbyHK_acknowledge( "Bearer "+auth,housekeeperid);
        call2.enqueue(new Callback<JOResponse>() {
            @Override
            public void onResponse(Call<JOResponse> call2, Response<JOResponse> response) {
                jobOrders=response.body().getData();
                adapter=new JobOrderAdapterCompleted(getApplicationContext(),jobOrders);
                recyclertask_acknowledge.setAdapter(adapter);
                refreshcompleted_acknowledge.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<JOResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

    public void onRefresh() {
        if(tabstatus=="Finished"){
            fetchcompleteddata_finished();
            refreshcompleted_finished.setRefreshing(false);
        }else if (tabstatus=="Working"){
            fetchcompleteddata_working();
            refreshcompleted_working.setRefreshing(false);
        }
        else if (tabstatus=="Signed"){
            fetchcompleteddata_acknowledge();
            refreshcompleted_acknowledge.setRefreshing(false);
        }else{
            fetchcompleteddata();
            refreshcompleted.setRefreshing(false);
        }
    }
    @Override
    public void onRestart()
    {
        super.onRestart();
        if(tabstatus=="Finished"){
            fetchcompleteddata_finished();
            refreshcompleted_finished.setRefreshing(false);
        }else if (tabstatus=="Working"){
            fetchcompleteddata_working();
            refreshcompleted_working.setRefreshing(false);
        }else if (tabstatus=="Signed"){
            fetchcompleteddata_acknowledge();
            refreshcompleted_acknowledge.setRefreshing(false);
        }else{
            fetchcompleteddata();
            refreshcompleted.setRefreshing(false);
        }
    }
}
