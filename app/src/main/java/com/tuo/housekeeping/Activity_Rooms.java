package com.tuo.housekeeping;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tuo.housekeeping.adapters.RoomsAdapter;
import com.tuo.housekeeping.model.LoginResponse;
import com.tuo.housekeeping.model.LoginUserModel;
import com.tuo.housekeeping.model.Room_status_response;
import com.tuo.housekeeping.model.RoomsModel;
import com.tuo.housekeeping.model.RoomsResponse;
import com.tuo.housekeeping.model.Rooms_status;
import com.tuo.housekeeping.services.APIClient;
import com.tuo.housekeeping.storage.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Rooms extends AppCompatActivity implements RoomsAdapter.ItemClickListener, SwipeRefreshLayout.OnRefreshListener, Spinner.OnItemSelectedListener{
    RoomsAdapter adapter;
    public RecyclerView recyclerrooms;
    private List<RoomsModel> roomsModels;
    private List<Rooms_status> rooms_status;
    SwipeRefreshLayout refreshrooms;
    String housekeeperid ="",auth;
Spinner spinner;
    String refresh;
ArrayList<String> status;
    LoginUserModel loginUserModel= SharedPrefManager.getInstance(this).getUser();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);
        housekeeperid= getIntent().getExtras().getString("housekeeperid");
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("Rooms");
            actionBar.setDisplayHomeAsUpEnabled(true);
            // actionBar.setHomeAsUpIndicator(R.mipmap.ic_housekeepingicon);
        }
        status = new ArrayList<String>();
        spinner = (Spinner) findViewById(R.id.spnr_status);
        spinner.setOnItemSelectedListener(Activity_Rooms.this);
        refreshrooms=findViewById(R.id.refreshrooms);
         auth= loginUserModel.getAccess_token();
         refresh= loginUserModel.getRefresh_token();
        recyclerrooms=findViewById(R.id.roomslistrecycler);


        recyclerrooms.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        fetchdata();
        refreshrooms.setOnRefreshListener(this);
//        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
        refreshrooms.setColorSchemeResources(android.R.color.holo_blue_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_blue_dark);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        refreshrooms.post(new Runnable() {
            @Override
            public void run() {
                if(refreshrooms != null) {

                }else{

                  fetchdata();
                    refreshrooms.setRefreshing(false);
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
        inflater.inflate(R.menu.rooms_search,menu);
        MenuItem menuItem=menu.findItem(R.id.search_rooms_icon);
        SearchView searchView= (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Seach Rooms");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length()<=0){

                    recyclerrooms=findViewById(R.id.roomslistrecycler);
                    recyclerrooms.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    Call<RoomsResponse> call = APIClient.getInstance().getApi().getStations( "Bearer "+auth);
                    call.enqueue(new Callback<RoomsResponse>() {
                        @Override
                        public void onResponse(Call<RoomsResponse> call, Response<RoomsResponse> response) {
                            roomsModels=response.body().getData();
                            adapter=new RoomsAdapter(getApplicationContext(),roomsModels);
                            recyclerrooms.setAdapter(adapter);
                            refreshrooms.setRefreshing(false);


                        }

                        @Override
                        public void onFailure(Call<RoomsResponse> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }else{

                    recyclerrooms = findViewById(R.id.roomslistrecycler);
                    recyclerrooms.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    Call<RoomsResponse> call = APIClient.getInstance().getApi().getStationsFilter( "Bearer "+auth,newText.trim(),newText.trim(),newText.trim(),newText.trim(),newText.trim());
                    call.enqueue(new Callback<RoomsResponse>() {
                        @Override
                        public void onResponse(Call<RoomsResponse> call, Response<RoomsResponse> response) {
                            roomsModels=response.body().getData();
                            adapter=new RoomsAdapter(getApplicationContext(),roomsModels);
                            recyclerrooms.setAdapter(adapter);
                            refreshrooms.setRefreshing(false);


                        }

                        @Override
                        public void onFailure(Call<RoomsResponse> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }
public void fetchdata(){
    Call<RoomsResponse> call = APIClient.getInstance().getApi().getStations( "Bearer "+auth);
    call.enqueue(new Callback<RoomsResponse>() {
        @Override
        public void onResponse(Call<RoomsResponse> call, Response<RoomsResponse> response) {
            if(response.isSuccessful()){
            roomsModels=response.body().getData();
            adapter=new RoomsAdapter(getApplicationContext(),roomsModels);
            recyclerrooms.setAdapter(adapter);
            refreshrooms.setRefreshing(false);
            }else{refreshtoken();
            }

        }

        @Override
        public void onFailure(Call<RoomsResponse> call, Throwable t) {
            Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("roomsss",t.getMessage());
        }
    });
    Call<Room_status_response> call2 = APIClient.getInstance().getApi().getRoomsStatusOptions( "Bearer "+auth);
    call2.enqueue(new Callback<Room_status_response>() {
        @Override
        public void onResponse(Call<Room_status_response> call2, Response<Room_status_response> response) {
            rooms_status=response.body().getData();
            for(int i =0;i<rooms_status.size();i++){
                status.add(rooms_status.get(i).statdesc);
            }
            spinner.setAdapter(new ArrayAdapter<String>(Activity_Rooms.this, android.R.layout.simple_spinner_dropdown_item, status));

        }

        @Override
        public void onFailure(Call<Room_status_response> call2, Throwable t) {
            Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("rom_stats",t.getMessage());
        }
    });
}

    @Override
    public void onRefresh() {
        fetchdata();
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(status.get(position) =="") {
            Call<RoomsResponse> call = APIClient.getInstance().getApi().getStations( "Bearer "+auth);
            call.enqueue(new Callback<RoomsResponse>() {
                @Override
                public void onResponse(Call<RoomsResponse> call, Response<RoomsResponse> response) {
                    roomsModels=response.body().getData();
                    adapter=new RoomsAdapter(getApplicationContext(),roomsModels);
                    recyclerrooms.setAdapter(adapter);
                    refreshrooms.setRefreshing(false);


                }

                @Override
                public void onFailure(Call<RoomsResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("roomsss",t.getMessage());
                }
            });
        }else {
            Call<RoomsResponse> call = APIClient.getInstance().getApi().getStationsFilter("Bearer " + auth, status.get(position), status.get(position), status.get(position), status.get(position), status.get(position));
            call.enqueue(new Callback<RoomsResponse>() {
                @Override
                public void onResponse(Call<RoomsResponse> call, Response<RoomsResponse> response) {
                    roomsModels = response.body().getData();
                    adapter = new RoomsAdapter(getApplicationContext(), roomsModels);
                    recyclerrooms.setAdapter(adapter);
                    refreshrooms.setRefreshing(false);


                }

                @Override
                public void onFailure(Call<RoomsResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void refreshtoken(){
        Call<LoginResponse> call = APIClient.getInstance().getApi().refreshToken( "refresh_token",refresh);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()){
                    auth=response.body().getAccess_token();
                }else{
                    SharedPrefManager.getInstance(Activity_Rooms.this).clear();
                    Intent intent=new Intent(Activity_Rooms.this, Activity_Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }





            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                SharedPrefManager.getInstance(Activity_Rooms.this).clear();
                Intent intent=new Intent(Activity_Rooms.this, Activity_Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
}
