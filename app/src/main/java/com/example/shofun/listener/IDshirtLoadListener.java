package com.example.shofun.listener;

import com.example.shofun.Models.ShirtCart;

import java.util.List;

public interface IDshirtLoadListener {

    void omShirtLoadSucces(List<ShirtCart> shirtCartList);
    void onShirtLoadFailed(String Message);
}
