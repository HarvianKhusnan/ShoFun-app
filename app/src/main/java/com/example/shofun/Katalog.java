package com.example.shofun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.shofun.Adapter.MyAdapter;
import com.example.shofun.Models.CartModel;
import com.example.shofun.Models.ShirtCart;
import com.example.shofun.listener.ICartLoadListener;
import com.example.shofun.listener.IDshirtLoadListener;
import com.example.shofun.utils.SpaceItemDecoration;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_katalog);

        init();
        LoadShirtFromFirebase();

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
    }

    @Override
    public void omShirtLoadSucces(List<ShirtCart> shirtCartList) {
        MyAdapter adapter = new MyAdapter(this,shirtCartList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onShirtLoadFailed(String Message) {
        Snackbar.make(mainlayout,Message,Snackbar.LENGTH_LONG).show();

    }

    @Override
    public void onCartLoadSucces(List<CartModel> cartModelList) {

    }

    @Override
    public void onCartLoadFailed(String Message) {

    }
}