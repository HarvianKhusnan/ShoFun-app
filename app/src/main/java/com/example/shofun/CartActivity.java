package com.example.shofun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shofun.Adapter.MyCartAdapter;
import com.example.shofun.Models.CartModel;
import com.example.shofun.eventbus.UpdateCartEvent;
import com.example.shofun.listener.ICartLoadListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartActivity extends AppCompatActivity implements ICartLoadListener {

    @BindView(R.id.recylerCart)
    RecyclerView recylercart;
    @BindView(R.id.mainLayout)
    RelativeLayout panelcartlayout;
    @BindView(R.id.btnbckk)
    ImageView btnbck;
    @BindView(R.id.txtTotal)
    TextView totalprice;
    @BindView(R.id.btncnftm)
    ImageView btncnfrm;
    @BindView(R.id.btntryon)
    ImageView btntry;
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

        loadCartFromFirebase();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        init();
        loadCartFromFirebase();
        btntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        CartActivity.this, R.style.BottomSheetDialogTheme
                );
                View bottomSheetView = LayoutInflater.from(getApplicationContext())
                        .inflate(
                                R.layout.activity_information, (RelativeLayout) findViewById(R.id.bottomSheetContainer)

                        );
                bottomSheetView.findViewById(R.id.clickhere).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(CartActivity.this, "Mendirect Ke Link", Toast.LENGTH_SHORT).show();
                        goLink("https://drive.google.com/drive/folders/13ulA5lvu5MJkoJ0rBqn-rAp9-fz9EsEQ?usp=sharing");
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();

            }
        });
    }



    private void loadCartFromFirebase() {
        List<CartModel> cartModels = new ArrayList<>();
        FirebaseDatabase.getInstance()
                .getReference("Cart")
                .child("UNIQUE_USER_ID")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for(DataSnapshot cartsnapshot:snapshot.getChildren()){
                                CartModel cartmodel = cartsnapshot.getValue(CartModel.class);
                                cartmodel.setKey(cartsnapshot.getKey());
                                cartModels.add(cartmodel);

                            }
                            cartLoadListener.onCartLoadSucces(cartModels);

                        }else{
                            cartLoadListener.onCartLoadFailed("Cart Empty");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        cartLoadListener.onCartLoadFailed(error.getMessage());

                    }
                });
    }

    private void init() {
        ButterKnife.bind(this);

        cartLoadListener = this;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recylercart.setLayoutManager(layoutManager);
        recylercart.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));

        btnbck.setOnClickListener(v -> finish());

        btncnfrm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CartActivity.this,paymentsucces.class));
            }
        });


    }

    @Override
    public void onCartLoadSucces(List<CartModel> cartModelList) {
        double sum = 0;
        for(CartModel cartmodel:cartModelList){
            sum+=cartmodel.getTotalPrice();
        }
        totalprice.setText(new StringBuilder(("Rp")).append(sum));
        MyCartAdapter adapter = new MyCartAdapter(this, cartModelList);
        recylercart.setAdapter(adapter);

    }

    @Override
    public void onCartLoadFailed(String Message) {

        Snackbar.make(panelcartlayout,Message,Snackbar.LENGTH_LONG).show();

    }

    private void goLink(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }


}