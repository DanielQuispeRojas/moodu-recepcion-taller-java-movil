package com.idastasoft.moodu.recepcion.ui.recepcion;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.idastasoft.moodu.recepcion.R;
import com.idastasoft.moodu.recepcion.data.api.ApiClient;
import com.idastasoft.moodu.recepcion.data.model.ApiResponse;
import com.idastasoft.moodu.recepcion.data.model.ClienteOSDetail;
import com.idastasoft.moodu.recepcion.data.model.DetalleOSDetail;
import com.idastasoft.moodu.recepcion.data.model.InfoOS;
import com.idastasoft.moodu.recepcion.databinding.FragmentVerOsBinding;
import com.idastasoft.moodu.recepcion.ui.main.MainActivity;
import com.idastasoft.moodu.recepcion.util.Estados;
import com.idastasoft.moodu.recepcion.util.SessionUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class VerOSFragment extends Fragment {

    private static final String ARG_OS_ID = "os_id";

    private FragmentVerOsBinding binding;
    private long osId;
    private final Handler pollHandler = new Handler(Looper.getMainLooper());
    private Runnable pollRunnable;

    public static VerOSFragment newInstance(long osId) {
        VerOSFragment fragment = new VerOSFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_OS_ID, osId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        osId = getArguments() != null ? getArguments().getLong(ARG_OS_ID, 0) : 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentVerOsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.rvServicios.setLayoutManager(new LinearLayoutManager(requireContext()));
        cargarOS();
        iniciarPolling();

        binding.btnEjecutar.setOnClickListener(v -> confirmarAccion("ejecutar", this::ejecutar));
        binding.btnPausar.setOnClickListener(v -> confirmarAccion("pausar", this::pausar));
        binding.btnReanudar.setOnClickListener(v -> confirmarAccion("reanudar", this::reanudar));
        binding.btnFinalizar.setOnClickListener(v -> confirmarAccion("finalizar", this::finalizar));
        binding.btnAnular.setOnClickListener(v -> confirmarAccion("anular", this::anular));
        binding.btnCerrar.setOnClickListener(v -> confirmarAccion("cerrar", this::cerrar));
    }

    private void confirmarAccion(String accion, Runnable onConfirm) {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Confirmar")
                .setMessage("Desea " + accion + " esta orden de servicio?")
                .setPositiveButton("Si", (d, w) -> onConfirm.run())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void cargarOS() {
        requireActivity().runOnUiThread(() -> binding.progressBar.setVisibility(View.VISIBLE));
        new Thread(() -> {
            try {
                Response<ApiResponse<InfoOS>> res = ApiClient.getRecepcionApi().verOS(osId).execute();
                if (res.isSuccessful() && res.body() != null && res.body().isOk()) {
                    InfoOS os = res.body().getPayload();
                    if (os != null) {
                        requireActivity().runOnUiThread(() -> mostrarOS(os));
                    }
                } else if (res.code() == 401) {
                    requireActivity().runOnUiThread(() -> SessionUtil.cerrarSesion(requireContext()));
                } else {
                    requireActivity().runOnUiThread(() -> Snackbar.make(binding.getRoot(), "Error al cargar OS", Snackbar.LENGTH_SHORT).show());
                }
            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> Snackbar.make(binding.getRoot(),
                        "Error al cargar OS: " + e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show());
            } finally {
                requireActivity().runOnUiThread(() -> binding.progressBar.setVisibility(View.GONE));
            }
        }).start();
    }

    private void mostrarOS(InfoOS os) {
        binding.tvNumeroOs.setText("OS #" + (os.getNoOrdenServicio() != null ? os.getNoOrdenServicio() : os.getId()));
        binding.tvEstado.setText(Estados.estadoTexto(os.getEstado()));
        binding.tvEstado.setBackgroundResource(Estados.estadoColor(os.getEstado()));

        ClienteOSDetail cli = os.getCliente();
        String nombreCli = cli != null ? cli.nombreCompleto() : null;
        binding.tvCliente.setText((nombreCli == null || nombreCli.isEmpty()) ? "Sin cliente" : nombreCli);

        com.idastasoft.moodu.recepcion.data.model.VehiculoOSDetail v = os.getVehiculoCli();
        binding.tvPlaca.setText(v != null && v.getNoPlaca() != null ? v.getNoPlaca() : "N/A");
        binding.tvVehiculo.setText(((v != null && v.getMarca() != null ? v.getMarca() : "") + " "
                + (v != null && v.getModelo() != null ? v.getModelo() : "") + " "
                + (v != null && v.getColor() != null ? v.getColor() : "")).trim());
        binding.tvFecha.setText(os.getFechaEmision() != null ? os.getFechaEmision() : "-");

        List<DetalleOSDetail> servicios = os.getDetalleOS() != null ? os.getDetalleOS() : new ArrayList<>();
        double total = os.getTotalGeneral() != null ? os.getTotalGeneral() : sumTotal(servicios);
        binding.tvTotal.setText("S/. " + String.format("%.2f", total));

        if (servicios.isEmpty()) {
            binding.tvServiciosVacio.setVisibility(View.VISIBLE);
            binding.rvServicios.setVisibility(View.GONE);
        } else {
            binding.tvServiciosVacio.setVisibility(View.GONE);
            binding.rvServicios.setVisibility(View.VISIBLE);
            binding.rvServicios.setAdapter(new DetalleOSAdapter(servicios));
        }

        String estado = os.getEstado() != null ? os.getEstado() : "";
        binding.btnEjecutar.setVisibility("EN_ESPERA".equals(estado) ? View.VISIBLE : View.GONE);
        binding.btnPausar.setVisibility("EN_CURSO".equals(estado) ? View.VISIBLE : View.GONE);
        binding.btnReanudar.setVisibility("PAUSADO".equals(estado) ? View.VISIBLE : View.GONE);
        binding.btnFinalizar.setVisibility("EN_CURSO".equals(estado) ? View.VISIBLE : View.GONE);
        binding.btnAnular.setVisibility(estado.equals("EN_ESPERA") || estado.equals("EN_CURSO") || estado.equals("PAUSADO") ? View.VISIBLE : View.GONE);
        binding.btnCerrar.setVisibility("FINALIZADO".equals(estado) ? View.VISIBLE : View.GONE);
    }

    private double sumTotal(List<DetalleOSDetail> servicios) {
        double t = 0;
        for (DetalleOSDetail d : servicios) {
            t += d.getTotal() != null ? d.getTotal() : 0.0;
        }
        return t;
    }

    private void ejecutar() { accionEstado(() -> ApiClient.getRecepcionApi().ejecutarOS(osId).execute()); }
    private void pausar() { accionEstado(() -> ApiClient.getRecepcionApi().pausarOS(osId).execute()); }
    private void reanudar() { accionEstado(() -> ApiClient.getRecepcionApi().reanudarOS(osId).execute()); }
    private void finalizar() { accionEstado(() -> ApiClient.getRecepcionApi().finalizarOS(osId).execute()); }
    private void anular() { accionEstado(() -> ApiClient.getRecepcionApi().anularOS(osId).execute()); }
    private void cerrar() { accionEstado(() -> ApiClient.getRecepcionApi().cerrarOS(osId).execute()); }

    private void accionEstado(CallProvider call) {
        requireActivity().runOnUiThread(() -> binding.progressBar.setVisibility(View.VISIBLE));
        new Thread(() -> {
            try {
                Response<?> res = call.get();
                if (res.isSuccessful()) {
                    requireActivity().runOnUiThread(() -> Snackbar.make(binding.getRoot(), "Accion realizada", Snackbar.LENGTH_SHORT).show());
                    cargarOS();
                } else if (res.code() == 401) {
                    requireActivity().runOnUiThread(() -> SessionUtil.cerrarSesion(requireContext()));
                } else {
                    requireActivity().runOnUiThread(() -> Snackbar.make(binding.getRoot(), "Error al ejecutar accion", Snackbar.LENGTH_SHORT).show());
                }
            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> Snackbar.make(binding.getRoot(), "Error: " + e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show());
            } finally {
                requireActivity().runOnUiThread(() -> binding.progressBar.setVisibility(View.GONE));
            }
        }).start();
    }

    private void iniciarPolling() {
        pollRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isAdded() || !isResumed()) return;
                cargarOS();
                pollHandler.postDelayed(this, 8000);
            }
        };
        pollHandler.postDelayed(pollRunnable, 8000);
    }

    @Override
    public void onDestroyView() {
        if (pollRunnable != null) pollHandler.removeCallbacks(pollRunnable);
        super.onDestroyView();
        binding = null;
    }

    private interface CallProvider {
        Response<?> get() throws Exception;
    }
}
