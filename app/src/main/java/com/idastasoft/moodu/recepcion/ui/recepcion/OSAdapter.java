package com.idastasoft.moodu.recepcion.ui.recepcion;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.idastasoft.moodu.recepcion.R;
import com.idastasoft.moodu.recepcion.data.model.OrdenServicio;
import com.idastasoft.moodu.recepcion.databinding.ItemOsBinding;

public class OSAdapter extends ListAdapter<OrdenServicio, OSAdapter.OSViewHolder> {

    public interface OnOsClick {
        void onClick(OrdenServicio os);
    }

    private final OnOsClick onClick;

    public OSAdapter(OnOsClick onClick) {
        super(new OSDiffCallback());
        this.onClick = onClick;
    }

    @Override
    public OSViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemOsBinding b = ItemOsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new OSViewHolder(b);
    }

    @Override
    public void onBindViewHolder(OSViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class OSViewHolder extends RecyclerView.ViewHolder {
        private final ItemOsBinding binding;

        OSViewHolder(ItemOsBinding b) {
            super(b.getRoot());
            this.binding = b;
        }

        void bind(OrdenServicio os) {
            binding.tvNumeroOs.setText("OS #" + (os.getNoOrdenServicio() != null ? os.getNoOrdenServicio() : os.getId()));
            String cli = ((os.getClienteNombres() != null ? os.getClienteNombres() : "") + " "
                    + (os.getClienteApellidos() != null ? os.getClienteApellidos() : "")).trim();
            binding.tvCliente.setText(cli.isEmpty() ? "Sin cliente" : cli);
            binding.tvPlaca.setText(os.getVehiculoPlaca() != null ? os.getVehiculoPlaca() : "N/A");
            binding.tvVehiculo.setText(((os.getVehiculoMarca() != null ? os.getVehiculoMarca() : "") + " "
                    + (os.getVehiculoModelo() != null ? os.getVehiculoModelo() : "")).trim());
            binding.tvFecha.setText(os.getFechaEmision() != null ? os.getFechaEmision() : "");
            binding.tvTotal.setText("S/. " + (os.getTotalGeneral() != null ? os.getTotalGeneral() : 0.0));

            String estado = os.getEstadoOS() != null ? os.getEstadoOS() : "";
            binding.tvEstado.setText(estado);
            int color;
            switch (estado) {
                case "EN_ESPERA":
                    color = ContextCompat.getColor(binding.getRoot().getContext(), R.color.estado_espera);
                    break;
                case "EN_CURSO":
                    color = ContextCompat.getColor(binding.getRoot().getContext(), R.color.estado_curso);
                    break;
                case "FINALIZADO":
                    color = ContextCompat.getColor(binding.getRoot().getContext(), R.color.estado_finalizado);
                    break;
                case "CERRADO":
                    color = ContextCompat.getColor(binding.getRoot().getContext(), R.color.estado_cerrado);
                    break;
                case "PAUSADO":
                    color = ContextCompat.getColor(binding.getRoot().getContext(), R.color.estado_pausado);
                    break;
                case "ANULADO":
                    color = ContextCompat.getColor(binding.getRoot().getContext(), R.color.estado_anulado);
                    break;
                default:
                    color = ContextCompat.getColor(binding.getRoot().getContext(), R.color.estado_espera);
                    break;
            }
            binding.tvEstado.setBackgroundColor(color);

            binding.getRoot().setOnClickListener(v -> onClick.onClick(os));
        }
    }

    static class OSDiffCallback extends DiffUtil.ItemCallback<OrdenServicio> {
        @Override
        public boolean areItemsTheSame(OrdenServicio oldItem, OrdenServicio newItem) {
            return oldItem.getId() != null && oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(OrdenServicio oldItem, OrdenServicio newItem) {
            return oldItem.equals(newItem);
        }
    }
}
