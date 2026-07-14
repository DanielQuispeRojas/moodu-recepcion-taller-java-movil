package com.idastasoft.moodu.recepcion.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.idastasoft.moodu.recepcion.R;
import com.idastasoft.moodu.recepcion.RecepcionApp;
import com.idastasoft.moodu.recepcion.databinding.ActivityMainBinding;
import com.idastasoft.moodu.recepcion.ui.connect.ConnectActivity;
import com.idastasoft.moodu.recepcion.ui.recepcion.CrearOSFragment;
import com.idastasoft.moodu.recepcion.ui.recepcion.HistorialFragment;
import com.idastasoft.moodu.recepcion.ui.recepcion.RecepcionInicioFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private RecepcionApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        app = (RecepcionApp) getApplication();

        if (!app.getPrefs().isConectado()) {
            binding.fabCrearOs.setVisibility(View.GONE);
            binding.bottomNav.setVisibility(View.GONE);
            new AlertDialog.Builder(this)
                    .setTitle("Sin canal conectado")
                    .setMessage("Debes unirte a un canal Moodu para poder operar. Conectate a un canal para continuar.")
                    .setCancelable(false)
                    .setPositiveButton("Unirse a un canal", (d, w) -> {
                        startActivity(new Intent(this, ConnectActivity.class));
                        finish();
                    })
                    .show();
            return;
        }

        if (!app.getPrefs().isLoggedIn()) {
            startActivity(new Intent(this, ConnectActivity.class));
            finish();
            return;
        }

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Recepcion Taller");

        String central = app.getPrefs().getNombreCentral() != null
                ? app.getPrefs().getNombreCentral()
                : (app.getPrefs().getCodigoCanal() != null ? app.getPrefs().getCodigoCanal() : "");
        binding.tvConexion.setText("Conectado a " + central);

        binding.fabCrearOs.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new CrearOSFragment())
                    .addToBackStack(null)
                    .commit();
            if (getSupportActionBar() != null) getSupportActionBar().setTitle("Nueva Orden de Servicio");
        });

        binding.bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_inicio) {
                loadFragment(new RecepcionInicioFragment());
                if (getSupportActionBar() != null) getSupportActionBar().setTitle("Recepcion Taller");
                return true;
            } else if (id == R.id.nav_historial) {
                loadFragment(new HistorialFragment());
                if (getSupportActionBar() != null) getSupportActionBar().setTitle("Historial");
                return true;
            } else if (id == R.id.nav_config) {
                showConfigDialog();
                return true;
            }
            return false;
        });

        binding.bottomNav.setSelectedItemId(R.id.nav_inicio);
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void showConfigDialog() {
        String central = app.getPrefs().getNombreCentral() != null ? app.getPrefs().getNombreCentral() : "Desconocido";
        String codigo = app.getPrefs().getCodigoCanal() != null ? app.getPrefs().getCodigoCanal() : "N/A";
        String url = app.getPrefs().getCentralUrl() != null ? app.getPrefs().getCentralUrl() : "N/A";

        new AlertDialog.Builder(this)
                .setTitle("Conexion")
                .setMessage("Central: " + central + "\nCodigo: " + codigo + "\nURL: " + url)
                .setPositiveButton("Cerrar", (d, w) -> d.dismiss())
                .setNegativeButton("Desconectar", (d, w) -> {
                    app.getPrefs().clearConexion();
                    app.getPrefs().logout();
                    startActivity(new Intent(this, ConnectActivity.class));
                    finish();
                })
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            if (getSupportActionBar() != null) getSupportActionBar().setTitle("Recepcion Taller");
            return true;
        }
        return super.onSupportNavigateUp();
    }

    public void setToolbarTitle(String title) {
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(title);
    }
}
