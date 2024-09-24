package com.example.grpcclient.client;

import android.util.Log;

import com.example.grpcclient.APIResponse;
import com.example.grpcclient.LoginRequest;
import com.example.grpcclient.RegisterRequest;
import com.example.grpcclient.UserGrpc;
import com.example.grpcclient.constants.ServerConnection;
import com.example.grpcclient.model.User;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class GrpcClient {

    private final UserGrpc.UserStub stub;


    public GrpcClient(String host, int port) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(ServerConnection.HOST, ServerConnection.PORT)
                .usePlaintext()
                .build();

        stub = UserGrpc.newStub(channel);

    }

    public void loginCall(User user, Callback callback) {
        LoginRequest request = LoginRequest.newBuilder()
                .setUsername(user.getUsername())
                .setPassword(user.getPassword())
                .build();

        Log.d("GrpcClient", "Username: " + user.getUsername() + ", Password: " + user.getPassword());

        stub.login(request, new StreamObserver<APIResponse>() {
            @Override
            public void onNext(APIResponse response) {
                callback.onSucess(response.getResponseMessage(), response.getResponseCode());
            }

            @Override
            public void onError(Throwable t) {
                callback.onError(t.getMessage());
            }

            @Override
            public void onCompleted() {}
        });
    }

    public void registerCall(User user, Callback callback){
        RegisterRequest request = RegisterRequest.newBuilder()
                .setUsername(user.getUsername())
                .setCpf(user.getCpf())
                .setPassword(user.getPassword())
                .build();

        stub.register(request, new StreamObserver<APIResponse>() {
            @Override
            public void onNext(APIResponse response) {
                callback.onSucess(response.getResponseMessage(), response.getResponseCode());
            }

            @Override
            public void onError(Throwable t) {
                callback.onError(t.getMessage());
            }

            @Override
            public void onCompleted() {}
        });

    }
}


