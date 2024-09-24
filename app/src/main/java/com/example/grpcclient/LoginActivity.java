package com.example.grpcclient;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.example.grpcclient.client.Callback;
import com.example.grpcclient.client.GrpcClient;
import com.example.grpcclient.constants.ServerConnection;
import com.example.grpcclient.model.User;
import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends Activity {

    private GrpcClient grpcClient;

    private EditText usernameInput;

    private EditText passwordInput;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        grpcClient = new GrpcClient(ServerConnection.HOST, ServerConnection.PORT);

        findViewById(R.id.login_button).setOnClickListener(v -> {

            User user = new User();

            user.setUsername(this.getUsernameValue());

            user.setPassword(this.getPasswordValue());

            grpcClient.loginCall(user, new Callback() {
                @Override
                public void onSucess(String message, int responseCode) {

                    if (responseCode != 200){

                        runOnUiThread(() -> Snackbar.make(v, "Erro ao logar: " + message, Snackbar.LENGTH_LONG)
                                .setDuration(5000)
                                .show());

                        return;
                    }


                    runOnUiThread(() -> Snackbar.make(v, "Login bem-sucedido: " + message, Snackbar.LENGTH_LONG)
                            .setDuration(5000)
                            .show());

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=dQw4w9WgXcQ"));
                    startActivity(intent);

                }

                @Override
                public void onError(String errorMessage) {
                    runOnUiThread(() -> Snackbar.make(v, "Login falhou: " + errorMessage, Snackbar.LENGTH_LONG).show());
                }
            });
        });

    }

    private String getUsernameValue() {
        usernameInput = findViewById(R.id.username_txt);

        return usernameInput.getText().toString();
    }

    private String getPasswordValue() {
        passwordInput = findViewById(R.id.password_txt);

        return passwordInput.getText().toString();
    }
}
