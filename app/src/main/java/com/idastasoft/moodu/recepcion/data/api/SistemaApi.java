package com.idastasoft.moodu.recepcion.data.api;

import com.idastasoft.moodu.recepcion.data.model.ApiResponse;
import com.idastasoft.moodu.recepcion.data.model.InfoPublicaCanal;
import com.idastasoft.moodu.recepcion.data.model.RegistrarDispositivoPayload;
import com.idastasoft.moodu.recepcion.data.model.RegistrarDispositivoRequest;
import com.idastasoft.moodu.recepcion.data.model.UnirseCanalPayload;
import com.idastasoft.moodu.recepcion.data.model.UnirseCanalRequest;
import com.idastasoft.moodu.recepcion.data.model.ValidarCanalPayload;
import com.idastasoft.moodu.recepcion.data.model.ValidarCanalRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SistemaApi {
    @POST("sistema/canal/info-publica")
    Call<ApiResponse<InfoPublicaCanal>> infoPublica();

    @POST("sistema/canal/unirse")
    Call<ApiResponse<UnirseCanalPayload>> unirseACanal(@Body UnirseCanalRequest request);

    @POST("sistema/canal/validar")
    Call<ApiResponse<ValidarCanalPayload>> validarCanal(@Body ValidarCanalRequest request);

    @POST("sistema/dispositivo/registrar")
    Call<ApiResponse<RegistrarDispositivoPayload>> registrarDispositivo(@Body RegistrarDispositivoRequest request);
}
