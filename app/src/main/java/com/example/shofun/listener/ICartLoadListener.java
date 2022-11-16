package com.example.shofun.listener;

import com.example.shofun.Models.CartModel;
import com.example.shofun.Models.ShirtCart;

import java.util.List;

public interface ICartLoadListener {
    void onCartLoadSucces(List<CartModel> cartModelList);
    void onCartLoadFailed(String Message);
}
