package com.idastasoft.moodu.recepcion.ui.connect;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.idastasoft.moodu.recepcion.R;
import com.idastasoft.moodu.recepcion.RecepcionApp;
import com.idastasoft.moodu.recepcion.data.api.ApiClient;
import com.idastasoft.moodu.recepcion.data.api.AuthApi;
import com.idastasoft.moodu.recepcion.data.api.LanDiscovery;
import com.idastasoft.moodu.recepcion.data.api.SistemaApi;
import com.idastasoft.moodu.recepcion.data.local.PreferencesManager;
import com.idastasoft.moodu.recepcion.data.model.ApiResponse;
import com.idastasoft.moodu.recepcion.data.model.LoginPayload;
import com.idastasoft.moodu.recepcion.data.model.LoginRequest;
import com.idastasoft.moodu.recepcion.data.model.UnirseCanalPayload;
import com.idastasoft.moodu.recepcion.data.model.UnirseCanalRequest;
import com.idastasoft.moodu.recepcion.data.model.ValidarCanalPayload;
import com.idastasoft.moodu.recepcion.data.model.ValidarCanalRequest;
import com.idastasoft.moodu.recepcion.databinding.ActivityConnectBinding;
import com.idastasoft.moodu.recepcion.databinding.ItemCanalLanBinding;
import com.idastasoft.moodu.recepcion.ui.login.LoginActivity;
import com.idastasoft.moodu.recepcion.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class ConnectActivity extends AppCompatActivity {

    private ActivityConnectBinding binding;
    private PreferencesManager prefs;

    private final List<LanDiscovery.CanalEncontrado> canalesEncontrados = new ArrayList<>();
    private CanalesAdapter adapter;
    private boolean modoNube = false;

    private String centralUrlValidado;
    private String codigoValidado;
    private String claveValidada;
    private String nombreCentralValidado;
    private String modoAcceso = "login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConnectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefs = ((RecepcionApp) getApplication()).getPrefs();

        if (prefs.isConectado() && prefs.isLoggedIn()) {
            startMain();
            return;
        }
        if (prefs.isConectado() && !prefs.isLoggedIn()) {
            startLogin();
            return;
        }

        adapter = new CanalesAdapter(this::seleccionarCanal);
        binding.rvCanales.setLayoutManager(new LinearLayoutManager(this));
        binding.rvCanales.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        binding.rvCanales.setAdapter(adapter);

        binding.btnBuscarLan.setOnClickListener(v -> buscarEnLan());
        binding.btnProbarConexion.setOnClickListener(v -> probarCanal());
        binding.btnConectar.setOnClickListener(v -> unirseAlCanal());
        binding.btnEntrarSinCanal.setOnClickListener(v -> startMain());

        binding.btnReintentar.setOnClickListener(v -> {
            binding.toggleAcceso.check(R.id.btn_acceso_login);
            modoAcceso = "login";
            unirseAlCanal();
        });

        binding.toggleModo.check(R.id.btn_modo_local);
        binding.toggleModo.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (!isChecked) return;
                modoNube = checkedId == R.id.btn_modo_nube;
                aplicarModo();
            }
        });

        binding.toggleAcceso.check(R.id.btn_acceso_login);
        binding.toggleAcceso.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (!isChecked) return;
                modoAcceso = checkedId == R.id.btn_acceso_registro ? "registro" : "login";
            }
        });

        aplicarModo();
    }

    private String calcularBaseUrl() {
        if (modoNube) {
            String url = binding.etUrlNube.getText() != null ? binding.etUrlNube.getText().toString().trim() : "";
            if (url.isEmpty()) {
                show_error("Ingrese la URL del servidor en la nube");
                return "";
            }
            return normalizarUrl(url);
        } else {
            String ip = binding.etIp.getText() != null ? binding.etIp.getText().toString().trim() : "";
            String puerto = binding.etPuerto.getText() != null ? binding.etPuerto.getText().toString().trim() : "8080";
            if (ip.isEmpty()) {
                show_error("Ingrese la IP del canal");
                return "";
            }
            return "http://" + ip + ":" + puerto;
        }
    }

    private void buscarEnLan() {
        String puerto = binding.etPuerto.getText() != null ? binding.etPuerto.getText().toString().trim() : "8080";
        binding.progressLan.setVisibility(View.VISIBLE);
        binding.btnBuscarLan.setEnabled(false);
        canalesEncontrados.clear();
        adapter.notifyDataSetChanged();
        binding.tvLanVacio.setVisibility(View.GONE);

        new Thread(() -> {
            try {
                List<LanDiscovery.CanalEncontrado> encontrados = LanDiscovery.escanear(ConnectActivity.this, puerto);
                runOnUiThread(() -> {
                    canalesEncontrados.clear();
                    canalesEncontrados.addAll(encontrados);
                    adapter.setItems(canalesEncontrados);
                    binding.tvLanVacio.setVisibility(encontrados.isEmpty() ? View.VISIBLE : View.GONE);
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    binding.tvLanVacio.setText("No se pudieron escanear canales: " + e.getLocalizedMessage());
                    binding.tvLanVacio.setVisibility(View.VISIBLE);
                });
            } finally {
                runOnUiThread(() -> {
                    binding.progressLan.setVisibility(View.GONE);
                    binding.btnBuscarLan.setEnabled(true);
                });
            }
        }).start();
    }

    private void seleccionarCanal(LanDiscovery.CanalEncontrado canal) {
        binding.etIp.setText(canal.ip);
        binding.etPuerto.setText(canal.puerto);
        binding.etCodigoCanal.setText(canal.codigoCanal);
        binding.layoutClave.setVisibility(canal.requiereClave ? View.VISIBLE : View.GONE);
        if (!canal.requiereClave) binding.etClave.setText("");

        binding.cardConexionInfo.setVisibility(View.GONE);
        binding.layoutCredenciales.setVisibility(View.GONE);
        binding.btnConectar.setEnabled(false);
        centralUrlValidado = null;
        codigoValidado = null;
        claveValidada = null;
        clearStatus();
    }

    private void probarCanal() {
        String codigo = binding.etCodigoCanal.getText() != null ? binding.etCodigoCanal.getText().toString().trim() : "";
        String clave = binding.etClave.getText() != null ? binding.etClave.getText().toString() : "";
        String baseUrl = calcularBaseUrl();
        if (baseUrl.isEmpty()) return;
        if (codigo.isEmpty()) {
            show_error("Ingrese el codigo del canal");
            return;
        }

        setLoading(true);
        clearStatus();

        new Thread(() -> {
            try {
                SistemaApi sistemaApi = ApiClient.getSistemaApi(baseUrl);
                Response<ApiResponse<ValidarCanalPayload>> res =
                        sistemaApi.validarCanal(new ValidarCanalRequest(codigo, clave.isEmpty() ? null : clave)).execute();

                if (res.isSuccessful() && res.body() != null && res.body().isOk()
                        && res.body().getPayload() != null && res.body().getPayload().isOk()) {
                    ValidarCanalPayload p = res.body().getPayload();
                    centralUrlValidado = baseUrl;
                    codigoValidado = codigo;
                    claveValidada = clave.isEmpty() ? null : clave;
                    nombreCentralValidado = p.getNombreCentral() != null ? p.getNombreCentral() : "Central";

                    runOnUiThread(() -> {
                        binding.tvEstado.setText("Canal valido: " + p.getNombreCentral());
                        binding.tvEstado.setVisibility(View.VISIBLE);
                        binding.tagCentral.setText(p.getNombreCentral());
                        binding.tagCodigo.setText(codigo);
                        binding.tagUrl.setText(baseUrl);
                        binding.cardConexionInfo.setVisibility(View.VISIBLE);
                        binding.layoutCredenciales.setVisibility(View.VISIBLE);
                        binding.btnConectar.setEnabled(true);
                    });
                } else {
                    final String msg = res.body() != null ? res.body().getMensaje() : "Canal no valido";
                    runOnUiThread(() -> show_error(msg));
                }
            } catch (Exception e) {
                runOnUiThread(() -> show_error("No se pudo conectar: " + e.getLocalizedMessage()));
            } finally {
                runOnUiThread(() -> setLoading(false));
            }
        }).start();
    }

    private void unirseAlCanal() {
        String usuario = binding.etUsuario.getText() != null ? binding.etUsuario.getText().toString().trim() : "";
        String password = binding.etPassword.getText() != null ? binding.etPassword.getText().toString() : "";
        final String centralUrl = centralUrlValidado;
        final String codigo = codigoValidado;

        if (centralUrl == null || codigo == null) {
            show_error("Primero pruebe el canal");
            return;
        }
        if (usuario.isEmpty() || password.isEmpty()) {
            show_error("Ingrese su usuario y contrasena del canal");
            return;
        }

        setLoading(true);
        clearStatus();
        binding.cardPendiente.setVisibility(View.GONE);

        new Thread(() -> {
            try {
                SistemaApi sistemaApi = ApiClient.getSistemaApi(centralUrl);
                Response<ApiResponse<UnirseCanalPayload>> res =
                        sistemaApi.unirseACanal(new UnirseCanalRequest(codigo, claveValidada, usuario, password)).execute();

                if (res.isSuccessful() && res.body() != null && res.body().isOk()) {
                    UnirseCanalPayload p = res.body().getPayload();
                    if (p != null && Boolean.TRUE.equals(p.getPendienteAprobacion())) {
                        prefs.saveConexion(centralUrl, codigo, nombreCentralValidado != null ? nombreCentralValidado : "Central");
                        runOnUiThread(() -> {
                            binding.cardPendiente.setVisibility(View.VISIBLE);
                            binding.btnConectar.setEnabled(false);
                        });
                    } else {
                        AuthApi authApi = ApiClient.getAuthApi(centralUrl);
                        Response<ApiResponse<LoginPayload>> log = authApi.login(new LoginRequest(usuario, password)).execute();
                        if (log.isSuccessful() && log.body() != null && log.body().isOk()) {
                            LoginPayload payload = log.body().getPayload();
                            prefs.saveConexion(centralUrl, codigo, nombreCentralValidado != null ? nombreCentralValidado : "Central");
                            if (payload != null) {
                                prefs.saveLogin(payload.getToken(), payload.getUserId(), payload.getUsername(),
                                        payload.getRol(), payload.getNombreCompleto());
                            }
                            runOnUiThread(this::startMain);
                        } else {
                            final String msg = log.body() != null ? log.body().getMensaje() : "Credenciales incorrectas";
                            runOnUiThread(() -> show_error(msg));
                        }
                    }
                } else {
                    final String msg = res.body() != null ? res.body().getMensaje() : "No se pudo unir al canal";
                    runOnUiThread(() -> show_error(msg));
                }
            } catch (Exception e) {
                runOnUiThread(() -> show_error("Error al unirse al canal: " + e.getLocalizedMessage()));
            } finally {
                runOnUiThread(() -> setLoading(false));
            }
        }).start();
    }

    private String normalizarUrl(String url) {
        String trimmed = url.trim();
        if (trimmed.startsWith("http://") || trimmed.startsWith("https://")) {
            return trimmed;
        }
        return "https://" + trimmed;
    }

    private void aplicarModo() {
        if (modoNube) {
            binding.cardBuscarLan.setVisibility(View.GONE);
            binding.layoutIpPuerto.setVisibility(View.GONE);
            binding.layoutUrlNube.setVisibility(View.VISIBLE);
        } else {
            binding.cardBuscarLan.setVisibility(View.VISIBLE);
            binding.layoutIpPuerto.setVisibility(View.VISIBLE);
            binding.layoutUrlNube.setVisibility(View.GONE);
        }
    }

    private void show_error(String msg) {
        binding.tvError.setText(msg);
        binding.tvError.setVisibility(View.VISIBLE);
        binding.tvEstado.setVisibility(View.GONE);
    }

    private void clearStatus() {
        binding.tvError.setVisibility(View.GONE);
        binding.tvEstado.setVisibility(View.GONE);
        binding.cardPendiente.setVisibility(View.GONE);
    }

    private void setLoading(boolean loading) {
        binding.progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        binding.btnProbarConexion.setEnabled(!loading);
        binding.btnBuscarLan.setEnabled(!loading);
        binding.btnConectar.setEnabled(!loading || binding.btnConectar.isEnabled());
    }

    private void startMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void startLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private interface OnCanalSeleccionado {
        void onSeleccionar(LanDiscovery.CanalEncontrado canal);
    }

    private static class CanalesAdapter extends RecyclerView.Adapter<CanalesAdapter.ViewHolder> {

        private final OnCanalSeleccionado onSeleccionar;
        private final List<LanDiscovery.CanalEncontrado> items = new ArrayList<>();

        CanalesAdapter(OnCanalSeleccionado onSeleccionar) {
            this.onSeleccionar = onSeleccionar;
        }

        void setItems(List<LanDiscovery.CanalEncontrado> list) {
            items.clear();
            items.addAll(list);
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemCanalLanBinding b = ItemCanalLanBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(b);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bind(items.get(position), onSeleccionar);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            private final ItemCanalLanBinding binding;

            ViewHolder(ItemCanalLanBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }

            void bind(LanDiscovery.CanalEncontrado canal, OnCanalSeleccionado onSeleccionar) {
                binding.tvNombreCentral.setText(canal.nombreCentral);
                binding.tvIp.setText(canal.ip);
                binding.tvCodigo.setText(canal.codigoCanal);
                binding.tvClave.setVisibility(canal.requiereClave ? View.VISIBLE : View.GONE);
                binding.getRoot().setOnClickListener(v -> onSeleccionar.onSeleccionar(canal));
            }
        }
    }
}
