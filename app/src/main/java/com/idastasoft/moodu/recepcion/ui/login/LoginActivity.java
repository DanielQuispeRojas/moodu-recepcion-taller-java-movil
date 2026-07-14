package com.idastasoft.moodu.recepcion.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.idastasoft.moodu.recepcion.R;
import com.idastasoft.moodu.recepcion.RecepcionApp;
import com.idastasoft.moodu.recepcion.data.api.ApiClient;
import com.idastasoft.moodu.recepcion.data.local.PreferencesManager;
import com.idastasoft.moodu.recepcion.data.model.ApiResponse;
import com.idastasoft.moodu.recepcion.data.model.LoginPayload;
import com.idastasoft.moodu.recepcion.data.model.LoginRequest;
import com.idastasoft.moodu.recepcion.databinding.ActivityLoginBinding;
import com.idastasoft.moodu.recepcion.ui.main.MainActivity;

import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private PreferencesManager prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        prefs = ((RecepcionApp) getApplication()).getPrefs();

        if (prefs.isLoggedIn()) {
            startMain();
            return;
        }

        binding.tvServerUrl.setText(prefs.getCentralUrl() != null ? prefs.getCentralUrl() : prefs.getBaseUrl());

        String extraUser = getIntent().getStringExtra("extra_username");
        String extraPass = getIntent().getStringExtra("extra_password");
        if (extraUser != null && !extraUser.isEmpty()) binding.etUsername.setText(extraUser);
        if (extraPass != null && !extraPass.isEmpty()) binding.etPassword.setText(extraPass);

        binding.btnLogin.setOnClickListener(v -> login());
    }

    private void login() {
        String username = binding.etUsername.getText() != null ? binding.etUsername.getText().toString().trim() : "";
        String password = binding.etPassword.getText() != null ? binding.etPassword.getText().toString() : "";
        if (username.isEmpty() || password.isEmpty()) {
            Snackbar.make(binding.getRoot(), "Ingrese usuario y contraseña", Snackbar.LENGTH_SHORT).show();
            return;
        }

        binding.btnLogin.setEnabled(false);
        binding.progressBar.setVisibility(View.VISIBLE);

        new Thread(() -> {
            try {
                Response<ApiResponse<LoginPayload>> res =
                        ApiClient.getAuthApi().login(new LoginRequest(username, password)).execute();
                if (res.isSuccessful() && res.body() != null && res.body().isOk()) {
                    LoginPayload payload = res.body().getPayload();
                    if (payload != null) {
                        prefs.saveLogin(payload.getToken(), payload.getUserId(), payload.getUsername(),
                                payload.getRol(), payload.getNombreCompleto());
                        runOnUiThread(this::startMain);
                    }
                } else {
                    final String msg = res.body() != null ? res.body().getMensaje() : "Credenciales incorrectas";
                    runOnUiThread(() -> Snackbar.make(binding.getRoot(), msg, Snackbar.LENGTH_SHORT).show());
                }
            } catch (Exception e) {
                runOnUiThread(() -> Snackbar.make(binding.getRoot(),
                        "Error de conexion: " + e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show());
            } finally {
                runOnUiThread(() -> {
                    binding.btnLogin.setEnabled(true);
                    binding.progressBar.setVisibility(View.GONE);
                });
            }
        }).start();
    }

    private void startMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
