package com.example.grpcclient.client;

public interface Callback{
    void onSucess(String message, int responseCode);
    void onError(String errorMessage);
}
