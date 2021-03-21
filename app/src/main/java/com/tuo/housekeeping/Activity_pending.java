package com.tuo.housekeeping;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.SearchView;
import android.widget.TabHost;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.tuo.housekeeping.adapters.JobOrderAdapter;
import com.tuo.housekeeping.model.JobOrders;
import com.tuo.housekeeping.model.JobOrdersResponse;
import com.tuo.housekeeping.model.LoginUserModel;
import com.tuo.housekeeping.services.APIClient;
import com.tuo.housekeeping.storage.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.VISIBLE;

public class Activity_pending extends AppCompatActivity implements JobOrderAdapter.ItemClickListener,SwipeRefreshLayout.OnRefreshListener{
    JobOrderAdapter adapter;
    public RecyclerView recyclertask,recyclertaskfinished,recyclertaskworking,recyclertaskacknowledge;
    private List<JobOrders> jobOrders;
    SwipeRefreshLayout refreshpending,refreshwoking,refreshfinished,refreshacknowledge;
    String housekeeperid ="",tabstatus="";
    int holdyear=0,holdmonth=0,holdday=0;
    SwitchMaterial calendar_switch ;
    CalendarView pendingCalendar;
    LoginUserModel loginUserModel= SharedPrefManager.getInstance(this).getUser();
    TabHost th_jobs;
    String auth= loginUserModel.getAccess_token();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending);
        housekeeperid= getIntent().getExtras().getString("housekeeperid");
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("Daily Task");
            actionBar.setDisplayHomeAsUpEnabled(true);
            // actionBar.setHomeAsUpIndicator(R.mipmap.ic_housekeepingicon);
        }
        refreshpending=findViewById(R.id.refreshpending);
        refreshwoking=findViewById(R.id.refreshwoking);
        refreshfinished=findViewById(R.id.refreshfinished);
        refreshacknowledge=findViewById(R.id.refreshacknowledge);
        tabstatus="Pending";

        recyclertask=findViewById(R.id.tasklistrecycler);
        recyclertaskfinished=findViewById(R.id.tasklistrecyclerfinished);
        recyclertaskworking=findViewById(R.id.tasklistrecyclerworking);
        recyclertaskacknowledge=findViewById(R.id.tasklistrecycleracknowledge);
        calendar_switch = (SwitchMaterial) findViewById(R.id.switch_calendar);
        pendingCalendar=findViewById(R.id.pendingCalendar);
        fetchdata();
        fetchDataworking();
        fetchDataFinished();
        fetchDataAcknowledge();

        th_jobs=findViewById(R.id.th_jobs);
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



        pendingCalendar.setVisibility(View.GONE);
        pendingCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                //Log.d("calendar"," " + year+" " + month +1 +" "+ dayOfMonth);
                holdyear=year;
                holdmonth=month;
                holdday=dayOfMonth;
                fetchdatawithdate(holdyear,holdmonth,holdday,tabstatus.toLowerCase());
            }
        });
        calendar_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
              if (isChecked) {
                  pendingCalendar.setVisibility(VISIBLE);
              }else {
                  pendingCalendar.setVisibility(View.GONE);
              }


            }
        });


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
     inflater.inflate(R.menu.search_menu,menu);
     MenuItem menuItem=menu.findItem(R.id.search_icon);
     SearchView searchView= (SearchView) menuItem.getActionView();
     searchView.setQueryHint("Search Task");
     searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
         @Override
         public boolean onQueryTextSubmit(String query) {
             return false;
         }

         @Override
         public boolean onQueryTextChange(String newText) {
             if(newText.length()<=0){
                 String auth= loginUserModel.getAccess_token();
                 recyclertask=findViewById(R.id.tasklistrecycler);
                 recyclertask.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                 Call<JobOrdersResponse> call2 = APIClient.getInstance().getApi().dbGetHousekeepingJobOrders( "Bearer "+auth,housekeeperid);
                 call2.enqueue(new Callback<JobOrdersResponse>() {
                     @Override
                     public void onResponse(Call<JobOrdersResponse> call2, Response<JobOrdersResponse> response) {
                         jobOrders=response.body().getData();
                         adapter=new JobOrderAdapter(getApplicationContext(),jobOrders);
                         recyclertask.setAdapter(adapter);

                     }

                     @Override
                     public void onFailure(Call<JobOrdersResponse> call, Throwable t) {
                         Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                     }
                 });
             }else{
                 String auth = loginUserModel.getAccess_token();
                 recyclertask = findViewById(R.id.tasklistrecycler);
                 recyclertask.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                 Call<JobOrdersResponse> call = APIClient.getInstance().getApi().dbGetHousekeepingJobOrdersFILTERPENDING("Bearer "+auth,newText.trim(),housekeeperid.trim());
                 call.enqueue(new Callback<JobOrdersResponse>() {
                     @Override
                     public void onResponse(Call<JobOrdersResponse> call, Response<JobOrdersResponse> response) {

                         jobOrders = response.body().getData();
                         adapter = new JobOrderAdapter(getApplicationContext(), jobOrders);
                         recyclertask.setAdapter(adapter);

                     }

                     @Override
                     public void onFailure(Call<JobOrdersResponse> call, Throwable t) {
                         Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                     }
                 });
             }
             return false;
         }
     });
     return super.onCreateOptionsMenu(menu);

    }
    private void fetchDataFinished(){
        recyclertaskfinished.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Call<JobOrdersResponse> call2 = APIClient.getInstance().getApi().dbGetHousekeepingJobOrdersFinished( "Bearer "+auth,housekeeperid);
        call2.enqueue(new Callback<JobOrdersResponse>() {
            @Override
            public void onResponse(Call<JobOrdersResponse> call, Response<JobOrdersResponse> response) {
                jobOrders=response.body().getData();
                adapter=new JobOrderAdapter(getApplicationContext(),jobOrders);
                recyclertaskfinished.setAdapter(adapter);
                refreshfinished.setRefreshing(false);


            }

            @Override
            public void onFailure(Call<JobOrdersResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void fetchDataAcknowledge(){
        recyclertaskacknowledge.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Call<JobOrdersResponse> call2 = APIClient.getInstance().getApi().dbGetHousekeepingJobOrdersAcknowledge( "Bearer "+auth,housekeeperid);
        call2.enqueue(new Callback<JobOrdersResponse>() {
            @Override
            public void onResponse(Call<JobOrdersResponse> call, Response<JobOrdersResponse> response) {
                jobOrders=response.body().getData();
                adapter=new JobOrderAdapter(getApplicationContext(),jobOrders);
                recyclertaskacknowledge.setAdapter(adapter);
                refreshacknowledge.setRefreshing(false);


            }

            @Override
            public void onFailure(Call<JobOrdersResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void fetchDataworking(){
        recyclertaskworking.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Call<JobOrdersResponse> call2 = APIClient.getInstance().getApi().dbGetHousekeepingJobOrdersWorking( "Bearer "+auth,housekeeperid);
        call2.enqueue(new Callback<JobOrdersResponse>() {
            @Override
            public void onResponse(Call<JobOrdersResponse> call, Response<JobOrdersResponse> response) {
                jobOrders=response.body().getData();
                adapter=new JobOrderAdapter(getApplicationContext(),jobOrders);
                recyclertaskworking.setAdapter(adapter);
                refreshwoking.setRefreshing(false);


            }

            @Override
            public void onFailure(Call<JobOrdersResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void fetchdatawithdate(int year, int month, int dayOfMonth, String status){
        String auth= loginUserModel.getAccess_token();
        if(tabstatus=="Pending"){
            recyclertask.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            Call<JobOrdersResponse> call = APIClient.getInstance().getApi().dbGetHousekeepingJobOrdersByDate( "Bearer "+auth,housekeeperid,year,month+1,dayOfMonth,status);
            call.enqueue(new Callback<JobOrdersResponse>() {
                @Override
                public void onResponse(Call<JobOrdersResponse> call, Response<JobOrdersResponse> response) {

                    jobOrders=response.body().getData();
                    adapter=new JobOrderAdapter(getApplicationContext(),jobOrders);
                    recyclertask.setAdapter(adapter);
                    refreshpending.setRefreshing(false);


                }

                @Override
                public void onFailure(Call<JobOrdersResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else if (tabstatus=="Finished") {
            recyclertaskfinished.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            Call<JobOrdersResponse> call = APIClient.getInstance().getApi().dbGetHousekeepingJobOrdersByDate( "Bearer "+auth,housekeeperid,year,month+1,dayOfMonth,status);
            call.enqueue(new Callback<JobOrdersResponse>() {
                @Override
                public void onResponse(Call<JobOrdersResponse> call, Response<JobOrdersResponse> response) {

                    jobOrders=response.body().getData();
                    adapter=new JobOrderAdapter(getApplicationContext(),jobOrders);
                    recyclertaskfinished.setAdapter(adapter);
                    refreshfinished.setRefreshing(false);


                }

                @Override
                public void onFailure(Call<JobOrdersResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            }
        else if (tabstatus=="Working") {
            recyclertaskworking.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            Call<JobOrdersResponse> call = APIClient.getInstance().getApi().dbGetHousekeepingJobOrdersByDate( "Bearer "+auth,housekeeperid,year,month+1,dayOfMonth,status);
            call.enqueue(new Callback<JobOrdersResponse>() {
                @Override
                public void onResponse(Call<JobOrdersResponse> call, Response<JobOrdersResponse> response) {

                    jobOrders=response.body().getData();
                    adapter=new JobOrderAdapter(getApplicationContext(),jobOrders);
                    recyclertaskworking.setAdapter(adapter);
                    refreshwoking.setRefreshing(false);


                }

                @Override
                public void onFailure(Call<JobOrdersResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }


        refreshpending.setOnRefreshListener(Activity_pending.this);
//        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
        refreshpending.setColorSchemeResources(android.R.color.holo_blue_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_blue_dark);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        refreshpending.post(new Runnable() {
            @Override
            public void run() {
                if(refreshpending != null) {

                }else{

                    fetchdata();
                    refreshpending.setRefreshing(false);
                }
            }
        });
    }
    private void fetchdata(){
        String auth= loginUserModel.getAccess_token();
            recyclertask=findViewById(R.id.tasklistrecycler);
            recyclertask.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            Call<JobOrdersResponse> call2 = APIClient.getInstance().getApi().dbGetHousekeepingJobOrders( "Bearer "+auth,housekeeperid);
            call2.enqueue(new Callback<JobOrdersResponse>() {
                @Override
                public void onResponse(Call<JobOrdersResponse> call2, Response<JobOrdersResponse> response) {
                    jobOrders=response.body().getData();
                    adapter=new JobOrderAdapter(getApplicationContext(),jobOrders);
                    recyclertask.setAdapter(adapter);

                }

                @Override
                public void onFailure(Call<JobOrdersResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


    }
    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        if(tabstatus=="Finished"){
            fetchDataFinished();
            refreshfinished.setRefreshing(false);
        }else if (tabstatus=="Working"){
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
        }else if (tabstatus=="Working"){
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
}
