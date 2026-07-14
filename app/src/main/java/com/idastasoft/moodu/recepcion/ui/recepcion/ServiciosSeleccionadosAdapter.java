package com.idastasoft.moodu.recepcion.ui.recepcion;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.idastasoft.moodu.recepcion.data.model.ServicioOSDTO;
import com.idastasoft.moodu.recepcion.databinding.ItemServicioSeleccionadoBinding;

import java.util.List;

public class ServiciosSeleccionadosAdapter extends RecyclerView.Adapter<ServiciosSeleccionadosAdapter.ViewHolder> {

    public interface OnQuitar {
        void onQuitar(int position);
    }

    private final List<ServicioOSDTO> items;
    private final OnQuitar onQuitar;

    public ServiciosSeleccionadosAdapter(List<ServicioOSDTO> items, OnQuitar onQuitar) {
        this.items = items;
        this.onQuitar = onQuitar;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemServicioSeleccionadoBinding binding;

        ViewHolder(ItemServicioSeleccionadoBinding b) {
            super(b.getRoot());
            binding = b;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemServicioSeleccionadoBinding b = ItemServicioSeleccionadoBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(b);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ServicioOSDTO s = items.get(position);
        holder.binding.tvServicio.setText(s.getServicio());
        holder.binding.tvDetalle.setText((s.getSku() != null ? s.getSku() : "") + "  x" + s.getCantidad()
                + "  S/. " + String.format("%.2f", s.getPrecioUnitario()));
        holder.binding.btnQuitar.setOnClickListener(v -> onQuitar.onQuitar(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
