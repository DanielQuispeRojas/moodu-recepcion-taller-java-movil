package com.idastasoft.moodu.recepcion.ui.recepcion;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.idastasoft.moodu.recepcion.data.model.DetalleOSDetail;
import com.idastasoft.moodu.recepcion.databinding.ItemDetalleOsBinding;

import java.util.List;

public class DetalleOSAdapter extends RecyclerView.Adapter<DetalleOSAdapter.ViewHolder> {

    private final List<DetalleOSDetail> items;

    public DetalleOSAdapter(List<DetalleOSDetail> items) {
        this.items = items;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemDetalleOsBinding binding;

        ViewHolder(ItemDetalleOsBinding b) {
            super(b.getRoot());
            binding = b;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemDetalleOsBinding b = ItemDetalleOsBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(b);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DetalleOSDetail d = items.get(position);
        holder.binding.tvServicio.setText(d.getServicio() != null ? d.getServicio() : "Servicio");
        holder.binding.tvSku.setText(d.getSku() != null ? d.getSku() : "");
        holder.binding.tvCantidad.setText("x" + (d.getCantidad() != null ? d.getCantidad() : 1));
        holder.binding.tvTotal.setText("S/. " + String.format("%.2f", d.getTotal() != null ? d.getTotal() : 0.0));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
