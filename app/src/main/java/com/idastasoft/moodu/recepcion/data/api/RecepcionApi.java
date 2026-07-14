package com.idastasoft.moodu.recepcion.data.api;

import com.idastasoft.moodu.recepcion.data.model.ApiResponse;
import com.idastasoft.moodu.recepcion.data.model.CamposRequeridosResponse;
import com.idastasoft.moodu.recepcion.data.model.CrearOSRequest;
import com.idastasoft.moodu.recepcion.data.model.InfoOS;
import com.idastasoft.moodu.recepcion.data.model.OrdenServicio;
import com.idastasoft.moodu.recepcion.data.model.StatsOS;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;
import java.util.Map;

public interface RecepcionApi {
    @GET("recepcion-taller-mec/inicio")
    Call<List<OrdenServicio>> listarOSEnCurso();

    @GET("recepcion-taller-mec/stats")
    Call<ApiResponse<StatsOS>> obtenerStats();

    @POST("recepcion-taller-mec/crear-os")
    Call<ApiResponse<OrdenServicio>> crearOS(@Body CrearOSRequest request);

    @GET("recepcion-taller-mec/ver-os")
    Call<ApiResponse<InfoOS>> verOS(@Query("id") long id);

    @GET("recepcion-taller-mec/{formulario}/campos-requeridos")
    Call<ApiResponse<CamposRequeridosResponse>> obtenerCamposRequeridos(@Path("formulario") String formulario);

    @GET("vehiculo/listar-tipo-veh")
    Call<List<String>> listarTipoVeh();

    @POST("recepcion-taller-mec/filtrar-os")
    Call<ApiResponse<List<OrdenServicio>>> filtrarOS(@Body Map<String, String> filtro);

    @POST("recepcion-taller-mec/{id}/ejecutar")
    Call<ApiResponse<Void>> ejecutarOS(@Path("id") long id);

    @POST("recepcion-taller-mec/{id}/pausar")
    Call<ApiResponse<Void>> pausarOS(@Path("id") long id);

    @POST("recepcion-taller-mec/{id}/reanudar")
    Call<ApiResponse<Void>> reanudarOS(@Path("id") long id);

    @POST("recepcion-taller-mec/{id}/anular")
    Call<ApiResponse<Void>> anularOS(@Path("id") long id);

    @POST("recepcion-taller-mec/{id}/finalizar")
    Call<ApiResponse<Void>> finalizarOS(@Path("id") long id);

    @POST("recepcion-taller-mec/{id}/cerrar")
    Call<ApiResponse<Void>> cerrarOS(@Path("id") long id);

    @POST("recepcion-taller-mec/{id}/agregar-servicio")
    Call<ApiResponse<Void>> agregarServicio(@Path("id") long id, @Body Map<String, Object> body);

    @DELETE("recepcion-taller-mec/detalle/{detalleId}")
    Call<ApiResponse<Void>> quitarServicio(@Path("detalleId") long detalleId);
}
