package com.idastasoft.moodu.recepcion.ui.recepcion;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.idastasoft.moodu.recepcion.R;
import com.idastasoft.moodu.recepcion.data.api.ApiClient;
import com.idastasoft.moodu.recepcion.data.model.ApiResponse;
import com.idastasoft.moodu.recepcion.data.model.CampoConfig;
import com.idastasoft.moodu.recepcion.data.model.CamposRequeridosResponse;
import com.idastasoft.moodu.recepcion.data.model.ClienteOSDTO;
import com.idastasoft.moodu.recepcion.data.model.CrearOSRequest;
import com.idastasoft.moodu.recepcion.data.model.OrdenServicio;
import com.idastasoft.moodu.recepcion.data.model.RecepcionVehiculoDTO;
import com.idastasoft.moodu.recepcion.data.model.ServicioCatalogo;
import com.idastasoft.moodu.recepcion.data.model.ServicioOSDTO;
import com.idastasoft.moodu.recepcion.data.model.VehiculoOSDTO;
import com.idastasoft.moodu.recepcion.databinding.FragmentCrearOsBinding;
import com.idastasoft.moodu.recepcion.ui.main.MainActivity;
import com.idastasoft.moodu.recepcion.util.SessionUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Response;

public class CrearOSFragment extends Fragment {

    private FragmentCrearOsBinding binding;

    private List<ServicioCatalogo> serviciosCatalogo = new ArrayList<>();
    private final List<ServicioOSDTO> serviciosSeleccionados = new ArrayList<>();
    private ServiciosSeleccionadosAdapter serviciosAdapter;

    private CamposRequeridosResponse camposConfig;
    private final List<CampoDinamico> camposVisibles = new ArrayList<>();
    private final Map<String, String> valoresPorCampo = new HashMap<>();
    private String tipoCliente = "NATURAL";
    private List<String> tiposVehiculo = new ArrayList<>();
    private String tipoVehiculo = "AUTO";

    private static class CampoDinamico {
        final String configKey;
        final TextInputEditText editText;
        final boolean requerido;
        final String placeHolder;

        CampoDinamico(String configKey, TextInputEditText editText, boolean requerido, String placeHolder) {
            this.configKey = configKey;
            this.editText = editText;
            this.requerido = requerido;
            this.placeHolder = placeHolder;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCrearOsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) requireActivity()).setToolbarTitle("Nueva Orden de Servicio");

        serviciosAdapter = new ServiciosSeleccionadosAdapter(serviciosSeleccionados, position -> {
            if (position >= 0 && position < serviciosSeleccionados.size()) {
                serviciosSeleccionados.remove(position);
                actualizarListaServicios();
            }
        });
        binding.rvServiciosSeleccionados.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvServiciosSeleccionados.setAdapter(serviciosAdapter);

        binding.tabTipoCliente.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String nuevo = tab != null && tab.getPosition() == 1 ? "EMPRESA" : "NATURAL";
                if (!nuevo.equals(tipoCliente)) {
                    guardarValoresActuales();
                    tipoCliente = nuevo;
                    renderizarCamposCliente();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        cargarServicios();
        cargarCamposCliente();
        cargarTiposVehiculo();
        binding.btnAgregarServicio.setOnClickListener(v -> agregarServicioALista());
        binding.btnGuardar.setOnClickListener(v -> guardar());
    }

    private void cargarCamposCliente() {
        new Thread(() -> {
            try {
                Response<ApiResponse<CamposRequeridosResponse>> res =
                        ApiClient.getRecepcionApi().obtenerCamposRequeridos("clienteOS").execute();
                if (res.isSuccessful() && res.body() != null && res.body().isOk()) {
                    camposConfig = res.body().getPayload();
                }
            } catch (Exception ignore) {
            }
            requireActivity().runOnUiThread(this::renderizarCamposCliente);
        }).start();
    }

    private Map<String, CampoConfig> mapaCamposTipo() {
        if (camposConfig == null) return null;
        return "EMPRESA".equals(tipoCliente) ? camposConfig.getEmpresa() : camposConfig.getNatural();
    }

    private void renderizarCamposCliente() {
        camposVisibles.clear();
        binding.clienteCamposContainer.removeAllViews();
        Map<String, CampoConfig> campos = mapaCamposTipo();

        if (campos == null || campos.isEmpty()) {
            agregarCampo("nombres", new CampoConfig("Nombres", "text", true, true),
                    InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            return;
        }

        for (Map.Entry<String, CampoConfig> entry : campos.entrySet()) {
            CampoConfig def = entry.getValue();
            if (def.isVisibilidad() || def.isRequerido()) {
                int inputType;
                if ("email".equals(def.getTipo())) {
                    inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;
                } else if ("number".equals(def.getTipo())) {
                    inputType = InputType.TYPE_CLASS_NUMBER;
                } else {
                    inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME;
                }
                agregarCampo(entry.getKey(), def, inputType);
            }
        }
    }

    private void agregarCampo(String configKey, CampoConfig def, int inputType) {
        TextInputLayout til = (TextInputLayout) LayoutInflater.from(requireContext())
                .inflate(R.layout.campo_dinamico, binding.clienteCamposContainer, false);
        StringBuilder hint = new StringBuilder();
        hint.append(def.getPlaceHolder() != null ? def.getPlaceHolder() : configKey);
        if (def.isRequerido()) hint.append(" *");
        til.setHint(hint.toString());

        TextInputEditText et = (TextInputEditText) til.getEditText();
        if (et != null) {
            et.setInputType(inputType);
            String prev = valoresPorCampo.get(configKey);
            et.setText(prev != null ? prev : "");
        }
        binding.clienteCamposContainer.addView(til);
        camposVisibles.add(new CampoDinamico(configKey, et, def.isRequerido(),
                def.getPlaceHolder() != null ? def.getPlaceHolder() : configKey));
    }

    private void guardarValoresActuales() {
        for (CampoDinamico c : camposVisibles) {
            String v = c.editText.getText() != null ? c.editText.getText().toString().trim() : "";
            valoresPorCampo.put(c.configKey, v);
        }
    }

    private void cargarServicios() {
        new Thread(() -> {
            try {
                Response<ApiResponse<List<ServicioCatalogo>>> res = ApiClient.getServicioApi().verServicios().execute();
                if (res.isSuccessful() && res.body() != null && res.body().isOk()) {
                    serviciosCatalogo = res.body().getPayload() != null ? res.body().getPayload() : new ArrayList<>();
                    List<String> nombres = new ArrayList<>();
                    for (ServicioCatalogo s : serviciosCatalogo) {
                        nombres.add((s.getSku() != null ? s.getSku() : "") + " - " + (s.getNombre() != null ? s.getNombre() : ""));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                            android.R.layout.simple_dropdown_item_1line, nombres);
                    requireActivity().runOnUiThread(() -> ((AutoCompleteTextView) binding.autoCompleteServicio).setAdapter(adapter));
                }
            } catch (Exception ignore) {
            }
        }).start();
    }

    private void cargarTiposVehiculo() {
        new Thread(() -> {
            try {
                Response<List<String>> res = ApiClient.getRecepcionApi().listarTipoVeh().execute();
                if (res.isSuccessful() && res.body() != null) {
                    tiposVehiculo = res.body();
                    requireActivity().runOnUiThread(() -> {
                        if (!tiposVehiculo.isEmpty()) {
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                                    android.R.layout.simple_dropdown_item_1line, tiposVehiculo);
                            AutoCompleteTextView act = (AutoCompleteTextView) binding.autoCompleteTipoVehiculo;
                            act.setAdapter(adapter);
                            act.setText(tipoVehiculo, false);
                        }
                    });
                }
            } catch (Exception ignore) {
            }
        }).start();
        ((AutoCompleteTextView) binding.autoCompleteTipoVehiculo)
                .setOnItemClickListener((parent, view, position, id) ->
                        tipoVehiculo = position < tiposVehiculo.size() ? tiposVehiculo.get(position) : tipoVehiculo);
    }

    private void agregarServicioALista() {
        AutoCompleteTextView act = (AutoCompleteTextView) binding.autoCompleteServicio;
        String texto = act.getText() != null ? act.getText().toString() : "";
        ServicioCatalogo seleccionado = null;
        for (ServicioCatalogo s : serviciosCatalogo) {
            String label = (s.getSku() != null ? s.getSku() : "") + " - " + (s.getNombre() != null ? s.getNombre() : "");
            if (label.equals(texto)) {
                seleccionado = s;
                break;
            }
        }
        if (seleccionado == null) {
            Snackbar.make(binding.getRoot(), "Seleccione un servicio del catálogo", Snackbar.LENGTH_SHORT).show();
            return;
        }

        int cantidad = 1;
        String cantStr = binding.etCantidad.getText() != null ? binding.etCantidad.getText().toString().trim() : "";
        try {
            int c = Integer.parseInt(cantStr);
            if (c > 0) cantidad = c;
        } catch (Exception ignore) {
        }

        ServicioOSDTO existente = null;
        for (ServicioOSDTO s : serviciosSeleccionados) {
            if (seleccionado.getSku() != null && seleccionado.getSku().equals(s.getSku())) {
                existente = s;
                break;
            }
        }
        if (existente != null) {
            existente.setCantidad(existente.getCantidad() + cantidad);
        } else {
            serviciosSeleccionados.add(new ServicioOSDTO(
                    seleccionado.getSku() != null ? seleccionado.getSku() : "",
                    seleccionado.getNombre() != null ? seleccionado.getNombre() : "",
                    cantidad,
                    seleccionado.getPrecio() != null ? seleccionado.getPrecio() : 0.0));
        }
        act.setText("");
        actualizarListaServicios();
    }

    private void actualizarListaServicios() {
        serviciosAdapter.notifyDataSetChanged();
        binding.tvServiciosVacio.setVisibility(serviciosSeleccionados.isEmpty() ? View.VISIBLE : View.GONE);
        binding.rvServiciosSeleccionados.setVisibility(serviciosSeleccionados.isEmpty() ? View.GONE : View.VISIBLE);
    }

    private String dtoKey(String configKey) {
        if ("representante".equals(configKey)) return "representanteLegal";
        return configKey;
    }

    private void guardar() {
        guardarValoresActuales();

        for (CampoDinamico campo : camposVisibles) {
            String value = campo.editText.getText() != null ? campo.editText.getText().toString().trim() : "";
            if (campo.requerido && value.isEmpty()) {
                Snackbar.make(binding.getRoot(), "El campo '" + campo.placeHolder + "' es requerido", Snackbar.LENGTH_LONG).show();
                campo.editText.requestFocus();
                return;
            }
        }

        String placa = binding.etPlaca.getText() != null ? binding.etPlaca.getText().toString().trim() : "";
        String marca = binding.etMarca.getText() != null ? binding.etMarca.getText().toString().trim() : "";
        String modelo = binding.etModelo.getText() != null ? binding.etModelo.getText().toString().trim() : "";
        String color = binding.etColor.getText() != null ? binding.etColor.getText().toString().trim() : "";
        final Integer anio = parseIntOrNull(binding.etAnio);
        final Integer kilometraje = parseIntOrNull(binding.etKilometraje);

        if (tipoVehiculo.isEmpty()) {
            Snackbar.make(binding.getRoot(), "Seleccione el tipo de vehiculo", Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (placa.isEmpty()) {
            Snackbar.make(binding.getRoot(), "Ingrese la placa del vehiculo", Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (serviciosSeleccionados.isEmpty()) {
            Snackbar.make(binding.getRoot(), "Agregue al menos un servicio", Snackbar.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> dtoValues = new HashMap<>();
        for (CampoDinamico c : camposVisibles) {
            String value = c.editText.getText() != null ? c.editText.getText().toString().trim() : "";
            dtoValues.put(dtoKey(c.configKey), value);
        }

        ClienteOSDTO cliente = new ClienteOSDTO(
                tipoCliente,
                dtoValues.getOrDefault("nombres", ""),
                dtoValues.getOrDefault("apellidos", ""),
                dtoValues.getOrDefault("razonSocial", ""),
                dtoValues.getOrDefault("representanteLegal", ""),
                dtoValues.getOrDefault("dni", ""),
                dtoValues.getOrDefault("ruc", ""),
                dtoValues.getOrDefault("correo", ""),
                dtoValues.getOrDefault("direccion", ""),
                dtoValues.getOrDefault("pais", ""),
                dtoValues.getOrDefault("ciudad", ""),
                dtoValues.getOrDefault("codigoPostal", "")
        );

        binding.btnGuardar.setEnabled(false);
        binding.progressBar.setVisibility(View.VISIBLE);

        new Thread(() -> {
            try {
                CrearOSRequest request = new CrearOSRequest(
                        cliente,
                        new VehiculoOSDTO(tipoVehiculo, marca, modelo, color, anio, placa),
                        new ArrayList<>(),
                        new ArrayList<>(serviciosSeleccionados),
                        new RecepcionVehiculoDTO(kilometraje)
                );

                Response<ApiResponse<OrdenServicio>> res = ApiClient.getRecepcionApi().crearOS(request).execute();
                if (res.isSuccessful() && res.body() != null && res.body().isOk()) {
                    requireActivity().runOnUiThread(() -> {
                        Snackbar.make(binding.getRoot(), "Orden creada exitosamente", Snackbar.LENGTH_LONG).show();
                        requireActivity().getSupportFragmentManager().popBackStack();
                    });
                } else if (res.code() == 401) {
                    requireActivity().runOnUiThread(() -> SessionUtil.cerrarSesion(requireContext()));
                } else {
                    String raw = null;
                    try {
                        raw = res.errorBody() != null ? res.errorBody().string() : null;
                    } catch (Exception ignore) {
                    }
                    final String finalRaw = raw;
                    String mensaje;
                    if (res.body() != null && res.body().getMensaje() != null) mensaje = res.body().getMensaje();
                    else if (raw != null) mensaje = raw;
                    else mensaje = "Error al crear OS (codigo " + res.code() + ")";
                    final String finalMsg = mensaje != null ? mensaje : "Error";
                    requireActivity().runOnUiThread(() -> {
                        String visible = finalMsg.length() > 300 ? finalMsg.substring(0, 300) : finalMsg;
                        Snackbar.make(binding.getRoot(), visible, Snackbar.LENGTH_LONG).show();
                        android.util.Log.e("CrearOS", "Error crear OS codigo=" + res.code() + " body=" + finalRaw);
                    });
                }
            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> Snackbar.make(binding.getRoot(),
                        "Error: " + e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show());
                android.util.Log.e("CrearOS", "Excepcion crear OS", e);
            } finally {
                requireActivity().runOnUiThread(() -> {
                    binding.btnGuardar.setEnabled(true);
                    binding.progressBar.setVisibility(View.GONE);
                });
            }
        }).start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private static Integer parseIntOrNull(TextInputEditText et) {
        if (et == null) return null;
        String s = et.getText() != null ? et.getText().toString().trim() : "";
        if (s.isEmpty()) return null;
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return null;
        }
    }
}
