package com.tuo.housekeeping.services;

import com.tuo.housekeeping.model.AppVersionResponse;
import com.tuo.housekeeping.model.JOResponse;
import com.tuo.housekeeping.model.JobOrdersResponse;
import com.tuo.housekeeping.model.LoginResponse;
import com.tuo.housekeeping.model.MedsResponse;
import com.tuo.housekeeping.model.PatientDataResponse;
import com.tuo.housekeeping.model.Room_status_response;
import com.tuo.housekeeping.model.RoomsResponse;
import com.tuo.housekeeping.model.TokenResponse;
import com.tuo.housekeeping.model.TotalJobsFinishedResponse;
import com.tuo.housekeeping.model.TotalJobsResponse;
import com.tuo.housekeeping.model.UserModelResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface PatientService {
    @FormUrlEncoded
    @POST("/api/user/gettoken")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<TokenResponse> gettoken(
            @Header("Authorization") String auth,
            @Field("username") String username
    );
    @FormUrlEncoded
    @POST("api/android/getdetails")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<AppVersionResponse> AndroidVersion(
            @Header("Authorization") String auth,
            @Field("AppName") String AppName
    );
    @FormUrlEncoded
    @POST("api/joborder/getJoborderDtlsbyORDERLYFinished")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<PatientDataResponse>getJoborderDtlsbyORDERLYFinished(
            @Header("Authorization") String auth,
            @Field("empid") String empid
    );
    @FormUrlEncoded
    @POST("api/joborder/getJoborderDtlsbyORDERLYWorking")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<PatientDataResponse>getJoborderDtlsbyORDERLYWorking(
            @Header("Authorization") String auth,
            @Field("empid") String empid
    );
    @FormUrlEncoded
    @POST("api/joborder/getJoborderDtlsbyORDERLYAcknowledge")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<PatientDataResponse>getJoborderDtlsbyORDERLYAcknowledge(
            @Header("Authorization") String auth,
            @Field("empid") String empid
    );
    @FormUrlEncoded
    @POST("api/joborder/getJoborderDtlsbyORDERLY")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<PatientDataResponse>getJoborderDtlsbyORDERLY(
            @Header("Authorization") String auth,
            @Field("empid") String empid
    );
    @FormUrlEncoded
    @POST("api/joborder/total_pending_orderly")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<TotalJobsResponse> gettotal_pending_orderly(
            @Header("Authorization") String auth,
            @Field("empid") String empid
    );
    @FormUrlEncoded
    @POST("api/routine/getTotal_pending_Routine")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<TotalJobsFinishedResponse> getTotal_pending_Routine(
            @Header("Authorization") String auth,
            @Field("empid") String empid
    );
    @FormUrlEncoded
    @POST("api/joborder/total_pending_JO")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<TotalJobsResponse> total_pending_JO(
            @Header("Authorization") String auth,
            @Field("empid") String empid
    );
    @FormUrlEncoded
    @POST("api/joborder/getJoborderDtlsbyHK")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<JOResponse> getJoborderDtlsbyHK(
            @Header("Authorization") String auth,
            @Field("empid") String empid
    );

    @FormUrlEncoded
    @POST("api/joborder/getJoborderDtlsbyHK_working")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<JOResponse> getJoborderDtlsbyHK_working(
            @Header("Authorization") String auth,
            @Field("empid") String empid
    );
    @FormUrlEncoded
    @POST("api/joborder/updatetojoborder_orderly")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<ResponseBody> updatetojoborder_orderly(
            @Header("Authorization") String auth,
            @Field("empid") String empid,
            @Field("orderly_jo_id") String orderly_jo_id,
            @Field("sts_id") String sts_id,
            @Field("sts_desc") String sts_desc
    );
    @FormUrlEncoded
    @POST("api/joborder/updatetojoborder")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<ResponseBody> updatetojoborder(
            @Header("Authorization") String auth,
            @Field("empid") String empid,
            @Field("hk_jo_id") String hk_jo_id,
            @Field("sts_id") String sts_id,
            @Field("sts_desc") String sts_desc
    );

    @FormUrlEncoded
    @POST("api/joborder/getJoborderDtlsbyHK_finished")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<JOResponse> getJoborderDtlsbyHK_finished(
            @Header("Authorization") String auth,
            @Field("empid") String empid
    );

    @FormUrlEncoded
    @POST("api/joborder/getJoborderDtlsbyHK_acknowledge")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<JOResponse> getJoborderDtlsbyHK_acknowledge(
            @Header("Authorization") String auth,
            @Field("empid") String empid
    );
    @FormUrlEncoded
    @POST("api/routine/updatetoacknowledgejob")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<ResponseBody> updatetoacknowledgejob(
            @Header("Authorization") String auth,
            @Field("username") String username,
            @Field("routine_job_id") String routine_job_id
    );
    @FormUrlEncoded
    @POST("api/routine/getRoutineTodayFinished")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<JobOrdersResponse> dbGetHousekeepingJobOrdersFinished(
            @Header("Authorization") String auth,
            @Field("emp_id") String emp_id
    );
    @FormUrlEncoded
    @POST("api/routine/getRoutineTodayAcknowledge")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<JobOrdersResponse> dbGetHousekeepingJobOrdersAcknowledge(
            @Header("Authorization") String auth,
            @Field("emp_id") String emp_id
    );
    @FormUrlEncoded
    @POST("api/routine/getRoutineTodayWorking")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<JobOrdersResponse> dbGetHousekeepingJobOrdersWorking(
            @Header("Authorization") String auth,
            @Field("emp_id") String emp_id
    );
    @FormUrlEncoded
    @POST("api/routine/getRoutineToday")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<JobOrdersResponse> dbGetHousekeepingJobOrders(
            @Header("Authorization") String auth,
            @Field("emp_id") String emp_id
    );
    @FormUrlEncoded
    @POST("api/routine/getRoutinebyDate")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<JobOrdersResponse> dbGetHousekeepingJobOrdersByDate(
            @Header("Authorization") String auth,
            @Field("emp_id") String emp_id,
            @Field("year") Integer year,
            @Field("month") Integer month,
            @Field("day") Integer dayOfMonth,
               @Field("status") String status
    );
    @FormUrlEncoded
    @POST("api/token")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<LoginResponse> userLogin(
            @Field("grant_type") String grant_type,
            @Field("username") String username,
            @Field("password") String password
    );







    @FormUrlEncoded
    @POST("api/ns/dbGetHousekeepingJobOrdersByIdFinished")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<JobOrdersResponse> dbGetHousekeepingJobOrdersCompleted(
            @Header("Authorization") String auth,
            @Field("housekeeperid") String housekeeperid
    );
    @FormUrlEncoded
    @POST("api/ns/dbGetHousekeepingJobOrdersByIdFilterFinished")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<JobOrdersResponse> dbGetHousekeepingJobOrdersFILTER(
            @Header("Authorization") String auth,
            @Field("joid") String id,
            @Field("housekeeperid") String housekeeperid

    );
    @FormUrlEncoded
    @POST("api/ns/dbGetHousekeepingJobOrdersByIdFilterPending")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<JobOrdersResponse> dbGetHousekeepingJobOrdersFILTERPENDING(
            @Header("Authorization") String auth,
            @Field("joid") String id,
            @Field("housekeeperid") String housekeeperid


    );
    @POST("api/ns/download")
    Call<ResponseBody> fetchSignature(
            @Header("Authorization") String auth,
            @Query("joborderid") String joborderid
    );
    @POST("api/user/getUserInfo")
    Call<UserModelResponse> db_get_active_user(
            @Header("Authorization") String auth
    );
    @POST("api/station/getStations")
    Call<RoomsResponse> getStations(
            @Header("Authorization") String auth
    );
    @POST("api/station/getRoomsStatusOptions")
    Call<Room_status_response> getRoomsStatusOptions(
            @Header("Authorization") String auth
    );
    @FormUrlEncoded
    @POST("api/station/getStationsFilter")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<RoomsResponse> getStationsFilter(
            @Header("Authorization") String auth,
            @Field("patno") String patno,
            @Field("patientname") String patientname,
            @Field("roomdesc") String roomdesc,
            @Field("roomcode") String roomcode,
            @Field("statdesc") String statdesc
    );
    @FormUrlEncoded
    @POST("api/routine/InserNewJob")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<ResponseBody> dbUpdateHousekeepingJobOrder(
            @Header("Authorization") String auth,
            @Field("routine_id") String routine_id,
            @Field("date_int") String date_int,
            @Field("emp_id") String emp_id,
            @Field("emp_name") String emp_name,
            @Field("sts_id") String sts_id,
            @Field("sts_desc") String sts_desc,
            @Field("area_id") String area_id,
            @Field("area_name") String area_name,
            @Field("spot_id") String spot_id ,
            @Field("spot_name") String spot_name,
            @Field("start_time") String start_time,
            @Field("start_date") String start_date 

    );
    @FormUrlEncoded
    @POST("api/routine/updatetofinishedjob")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<ResponseBody> updatetofinishedjob(
            @Header("Authorization") String auth,
            @Field("routine_job_id") String routine_job_id

    );
    @FormUrlEncoded
    @POST("api/ns/dbUpdateHousekeepertoken")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<ResponseBody> dbUpdateHousekeepertoken(
            @Header("Authorization") String auth,
            @Field("token") String token,
            @Field("housekeeperid") String housekeeperid

    );
    @FormUrlEncoded
    @POST("api/ns/dbGetTotalPending")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<TotalJobsResponse> dbGetTotalPending(
            @Header("Authorization") String auth,
            @Field("joborderid") String joborderid

    );
    @FormUrlEncoded
    @POST("api/ns/dbGetTotalFinished")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<TotalJobsFinishedResponse> dbGetTotalFinished(
            @Header("Authorization") String auth,
            @Field("joborderid") String joborderid

    );
    @GET("getallmedications")
    Call<MedsResponse> getallmedications(
            @Query("patno") String patno
    );


    @FormUrlEncoded
    @POST("api/token")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<LoginResponse> refreshToken(
            @Field("grant_type") String grant_type,
            @Field("refresh_token") String refresh_token
    );
    @FormUrlEncoded
    @POST("patientinformationwithoutmeds")
    Call<PatientDataResponse> PatientList(
            @Field("patno") String patno
    );
    @Multipart
    @POST("api/ns/upload")
    Call<RequestBody> upload(@Header("Authorization") String auth,@Part MultipartBody.Part photo, @Part("description") RequestBody requestBody);
}
