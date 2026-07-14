package com.idastasoft.moodu.recepcion.data.api;

import com.idastasoft.moodu.recepcion.data.model.ApiResponse;
import com.idastasoft.moodu.recepcion.data.model.ServicioCatalogo;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface ServicioApi {
    @GET("servicio/ver-servicios")
    Call<ApiResponse<List<ServicioCatalogo>>> verServicios();
}
