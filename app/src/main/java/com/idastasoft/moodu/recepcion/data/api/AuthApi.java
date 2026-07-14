package com.idastasoft.moodu.recepcion.data.api;

import com.idastasoft.moodu.recepcion.data.model.ApiResponse;
import com.idastasoft.moodu.recepcion.data.model.LoginPayload;
import com.idastasoft.moodu.recepcion.data.model.LoginRequest;
import com.idastasoft.moodu.recepcion.data.model.PerfilPayload;
import com.idastasoft.moodu.recepcion.data.model.RegisterRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AuthApi {
    @POST("auth/login")
    Call<ApiResponse<LoginPayload>> login(@Body LoginRequest request);

    @POST("auth/register")
    Call<ApiResponse<LoginPayload>> register(@Body RegisterRequest request);

    @GET("auth/has-users")
    Call<ApiResponse<Boolean>> hasUsers();

    @GET("auth/perfil")
    Call<ApiResponse<PerfilPayload>> perfil();
}
