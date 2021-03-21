package com.tuo.housekeeping;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tuo.housekeeping.model.LoginResponse;
import com.tuo.housekeeping.services.APIClient;
import com.tuo.housekeeping.services.PatientService;
import com.tuo.housekeeping.storage.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Login extends AppCompatActivity {
    Button btn_login;
    EditText username,password;
    private ProgressDialog dialog;
    PatientService patientService;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        dialog = new ProgressDialog(Activity_Login.this);

        btn_login= (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Username=username.getText().toString();
                String Password=password.getText().toString();
                String grant_type="password";
                if(validateLogin(Username,Password)){
                    dialog.setMessage("Signing In. . .");
                    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    dialog.show();
                    dialog.setCancelable(false);
                    doLogin(grant_type,Username,Password);
                }
            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            Intent intent=new Intent(this,Activity_Dashboard.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private boolean validateLogin(String Username, String Password){
        if(Username==null || Username.trim().length()==0){
            Toast.makeText(this,"Username is Required",Toast.LENGTH_LONG).show();
            return false;
        }
        if(Password==null || Password.trim().length()==0){
            Toast.makeText(this,"Password is Required",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    private void doLogin(final String grant_type,final String username, final String password){

        Call<LoginResponse> call= APIClient.getInstance().getApi().userLogin("password",username,password);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                try {
                    LoginResponse loginResponse=response.body();
                    dialog.dismiss();
                    SharedPrefManager.getInstance(Activity_Login.this).saveUser(loginResponse.getAccess_token(),loginResponse.getRefresh_token());
                    Intent intent=new Intent(Activity_Login.this,Activity_Dashboard.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(Activity_Login.this,"Your username/password is incorrect",Toast.LENGTH_LONG).show();
                }




            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(Activity_Login.this,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
