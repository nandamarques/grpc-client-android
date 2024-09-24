package com.example.grpcclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.grpcclient.client.Callback;
import com.example.grpcclient.client.GrpcClient;
import com.example.grpcclient.constants.ServerConnection;
import com.example.grpcclient.model.User;
import com.google.android.material.snackbar.Snackbar;
import com.redmadrobot.inputmask.MaskedTextChangedListener;

import io.grpc.Grpc;

public class RegisterActivity extends Activity {

    private GrpcClient grpcClient;

    private EditText usernameInput;

    private EditText passwordInput;

    private EditText cpfInput;

    private Button registerBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        grpcClient = new GrpcClient(ServerConnection.HOST, ServerConnection.PORT);

        registerBtn = findViewById(R.id.registerBtn);

        cpfInput = findViewById(R.id.cpf_txt);

        final MaskedTextChangedListener listener = MaskedTextChangedListener.Companion.installOn(
                cpfInput,
                "[000].[000].[000]-[00]",
                new MaskedTextChangedListener.ValueListener() {
                    @Override
                    public void onTextChanged(boolean b, @NonNull String s, @NonNull String s1, @NonNull String s2) {
                        Log.d("TAG", s1);
                        Log.d("TAG", String.valueOf(b));
                    }
                }

        );

        cpfInput.setHint(listener.placeholder());


        registerBtn.setOnClickListener(v -> {

            User user = new User();

            user.setUsername(this.getUsernameValue());

            user.setCpf(cpfInput.getText().toString());

            user.setPassword(this.getPasswordValue());

            grpcClient.registerCall(user, new Callback() {
                        @Override
                        public void onSucess(String message, int responseCode) {
                            if (responseCode != 200){

                                runOnUiThread(() -> Snackbar.make(v, "Erro ao cadastrar: " + message, Snackbar.LENGTH_LONG)
                                        .setDuration(5000)
                                        .show());

                                return;
                            }

                            runOnUiThread(() -> Snackbar.make(v, "Usuário Cadastrado com sucesso: " + message, Snackbar.LENGTH_LONG)
                                    .setDuration(5000)
                                    .show());

                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);

                        }

                        @Override
                        public void onError(String errorMessage) {
                            runOnUiThread(() -> Snackbar.make(v, "Cadastro falhou: " + errorMessage, Snackbar.LENGTH_LONG).show());
                        }
                    }

            );

        });

    }

    private String getPasswordValue() {
        passwordInput = findViewById(R.id.password_txt);

        return passwordInput.getText().toString();
    }


    private String getUsernameValue() {
        usernameInput = findViewById(R.id.username_txt);

        return usernameInput.getText().toString();
    }
}