package com.example.shofun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.shofun.Adapter.MyAdapter;
import com.example.shofun.Models.CartModel;
import com.example.shofun.Models.ShirtCart;
import com.example.shofun.eventbus.UpdateCartEvent;
import com.example.shofun.listener.ICartLoadListener;
import com.example.shofun.listener.IDshirtLoadListener;
import com.example.shofun.utils.SpaceItemDecoration;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Katalog extends AppCompatActivity implements IDshirtLoadListener, ICartLoadListener {

    @BindView(R.id.recylerView)
    RecyclerView recyclerView;
    @BindView(R.id.mainLayout)
    RelativeLayout mainlayout;
    @BindView(R.id.badge)
    NotificationBadge badge;
    @BindView(R.id.btnCart)
    FrameLayout btncart;

    IDshirtLoadListener shirtLoadListener;
    ICartLoadListener cartLoadListener;


    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        if(EventBus.getDefault().hasSubscriberForEvent(UpdateCartEvent.class))
            EventBus.getDefault().removeStickyEvent(UpdateCartEvent.class);
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)

    public void onUpdateCart(UpdateCartEvent event){
        councartItem();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_katalog);

        init();
        LoadShirtFromFirebase();
        councartItem();

    }

    private void LoadShirtFromFirebase() {
        List<ShirtCart> shirtCarts = new ArrayList<>();
        FirebaseDatabase.getInstance()
                .getReference("Shirt")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                                ShirtCart shirtcart = dataSnapshot.getValue(ShirtCart.class);
                                shirtcart.setKey(dataSnapshot.getKey());
                                shirtCarts.add(shirtcart);
                            }
                            shirtLoadListener.omShirtLoadSucces(shirtCarts);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
shirtLoadListener.onShirtLoadFailed(error.getMessage());
                    }
                });
    }

    private void init() {
        ButterKnife.bind(this);

        shirtLoadListener = this;
        cartLoadListener = this;

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new SpaceItemDecoration());

        btncart.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
    }

    @Override
    public void omShirtLoadSucces(List<ShirtCart> shirtCartList) {
        MyAdapter adapter = new MyAdapter(this,shirtCartList,cartLoadListener);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onShirtLoadFailed(String Message) {
        Snackbar.make(mainlayout,Message,Snackbar.LENGTH_LONG).show();

    }

    @Override
    public void onCartLoadSucces(List<CartModel> cartModelList) {

        int cartSum = 0;
        for (CartModel cartModel: cartModelList)
            cartSum += cartModel.getQuantity();
        badge.setNumber(cartSum);

    }

    @Override
    public void onCartLoadFailed(String Message) {
        Snackbar.make(mainlayout,Message,Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        councartItem();
    }

    private void councartItem() {
        List<CartModel> cartmodels = new ArrayList<>();
        FirebaseDatabase
                .getInstance().getReference("Cart")
                .child("UNIQUE_USER_ID")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot cartsnaphot:snapshot.getChildren()){
                            CartModel cartmodel = cartsnaphot.getValue(CartModel.class);
                            cartmodel.setKey(cartsnaphot.getKey());
                            cartmodels.add(cartmodel);
                        }
                        cartLoadListener.onCartLoadSucces(cartmodels);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        cartLoadListener.onCartLoadFailed(error.getMessage());
                    }
                });
    }


}