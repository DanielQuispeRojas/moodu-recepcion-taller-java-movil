package com.idastasoft.moodu.recepcion.data.api;

import com.idastasoft.moodu.recepcion.data.model.ApiResponse;
import com.idastasoft.moodu.recepcion.data.model.Plugin;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;
import java.util.Map;

public interface CoreApi {
    @GET("core/plugins")
    Call<ApiResponse<List<Plugin>>> listarPlugins();

    @GET("core/health")
    Call<ApiResponse<Map<String, Object>>> health();
}
