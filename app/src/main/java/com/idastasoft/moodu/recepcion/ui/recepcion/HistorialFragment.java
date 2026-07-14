package com.idastasoft.moodu.recepcion.ui.recepcion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.idastasoft.moodu.recepcion.R;
import com.idastasoft.moodu.recepcion.data.api.ApiClient;
import com.idastasoft.moodu.recepcion.data.model.ApiResponse;
import com.idastasoft.moodu.recepcion.data.model.OrdenServicio;
import com.idastasoft.moodu.recepcion.databinding.FragmentHistorialBinding;
import com.idastasoft.moodu.recepcion.ui.main.MainActivity;
import com.idastasoft.moodu.recepcion.util.SessionUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Response;

public class HistorialFragment extends Fragment {

    private FragmentHistorialBinding binding;
    private OSAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHistorialBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) requireActivity()).setToolbarTitle("Historial");

        adapter = new OSAdapter(os -> {
            VerOSFragment fragment = VerOSFragment.newInstance(os.getId() != null ? os.getId() : 0L);
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
            ((MainActivity) requireActivity()).setToolbarTitle("OS #" + (os.getNoOrdenServicio() != null ? os.getNoOrdenServicio() : os.getId()));
        });

        binding.rvHistorial.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvHistorial.setAdapter(adapter);

        binding.swipeRefresh.setOnRefreshListener(this::cargarHistorial);
        binding.btnBuscar.setOnClickListener(v -> buscar());

        cargarHistorial();
    }

    private void cargarHistorial() {
        binding.swipeRefresh.setRefreshing(true);
        binding.tvEmpty.setVisibility(View.GONE);

        new Thread(() -> {
            try {
                Map<String, String> filtro = new HashMap<>();
                String texto = binding.etBuscar.getText() != null ? binding.etBuscar.getText().toString().trim() : "";
                if (!texto.isEmpty()) filtro.put("buscar", texto);

                Response<ApiResponse<List<OrdenServicio>>> res = ApiClient.getRecepcionApi().filtrarOS(filtro).execute();
                if (res.isSuccessful() && res.body() != null && res.body().isOk()) {
                    List<OrdenServicio> ordenes = res.body().getPayload() != null ? res.body().getPayload() : new ArrayList<>();
                    requireActivity().runOnUiThread(() -> {
                        adapter.submitList(ordenes);
                        binding.tvEmpty.setVisibility(ordenes.isEmpty() ? View.VISIBLE : View.GONE);
                    });
                } else if (res.code() == 401) {
                    requireActivity().runOnUiThread(() -> SessionUtil.cerrarSesion(requireContext()));
                } else {
                    requireActivity().runOnUiThread(() -> {
                        binding.tvEmpty.setText("Error: " + res.message());
                        binding.tvEmpty.setVisibility(View.VISIBLE);
                    });
                }
            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> {
                    binding.tvEmpty.setText("Error de conexion: " + e.getLocalizedMessage());
                    binding.tvEmpty.setVisibility(View.VISIBLE);
                });
            }

            requireActivity().runOnUiThread(() -> binding.swipeRefresh.setRefreshing(false));
        }).start();
    }

    private void buscar() {
        cargarHistorial();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
