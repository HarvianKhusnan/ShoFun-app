package com.example.shofun.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shofun.Models.CartModel;
import com.example.shofun.Models.ShirtCart;
import com.example.shofun.R;
import com.example.shofun.eventbus.UpdateCartEvent;
import com.example.shofun.listener.ICartLoadListener;
import com.example.shofun.listener.IRecyclerViewClickListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private Context context;
    private List<ShirtCart> shirtCartList;
    private ICartLoadListener iCartLoadListener;

    public MyAdapter(Context context, List<ShirtCart> shirtCartList, ICartLoadListener iCartLoadListener) {
        this.context = context;
        this.shirtCartList = shirtCartList;
        this.iCartLoadListener = iCartLoadListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.my_row,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context)
                .load(shirtCartList.get(position).getImage())
                .into(holder.imageView);
        holder.txtPrice.setText(new StringBuilder("IDR").append(shirtCartList.get(position).getPrice()));
        holder.txtName.setText(new StringBuilder("").append(shirtCartList.get(position).getName()));

        holder.setListener((view, adapterPosition) -> {
            addToCart(shirtCartList.get(position));
        });


    }

    private void addToCart(ShirtCart shirtcart) {
        DatabaseReference userCart = FirebaseDatabase
                .getInstance()
                .getReference("Cart")
                .child("UNIQUE_USER_ID");
        userCart.child(shirtcart.getKey())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            CartModel cartModels = snapshot.getValue(CartModel.class);
                            cartModels.setQuantity(cartModels.getQuantity()+1);
                            Map<String,Object> updateData = new HashMap<>();
                            updateData.put("quantity",cartModels.getQuantity());
                            updateData.put("total price",cartModels.getQuantity()*Float.parseFloat(cartModels.getPrice()));

                            userCart.child(shirtcart.getKey())
                                    .updateChildren(updateData)
                                    .addOnSuccessListener(unused -> {
                                        iCartLoadListener.onCartLoadFailed("Add To Cart Succes");
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            iCartLoadListener.onCartLoadFailed(e.getMessage());
                                        }
                                    });

                        }
                        else {
                            CartModel cartModel = new CartModel();
                            cartModel.setName(shirtcart.getName());
                            cartModel.setImage(shirtcart.getImage());
                            cartModel.setKey(shirtcart.getKey());
                            cartModel.setPrice(shirtcart.getPrice());
                            cartModel.setQuantity(1);
                            cartModel.setTotalPrice(Float.parseFloat(shirtcart.getPrice()));

                            userCart.child(shirtcart.getKey())
                                    .setValue(cartModel)
                                    .addOnSuccessListener(unused -> {
                                iCartLoadListener.onCartLoadFailed("Add To Cart Succes");
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            iCartLoadListener.onCartLoadFailed(e.getMessage());
                                        }
                                    });


                        }
                        EventBus.getDefault().postSticky(new UpdateCartEvent());


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iCartLoadListener.onCartLoadFailed(error.getMessage());
                    }
                });
    }

    @Override
    public int getItemCount() {
        return shirtCartList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.imageview)
        ImageView imageView;
        @BindView(R.id.txtName)
        TextView txtName;
        @BindView(R.id.txtPrice)
        TextView txtPrice;
        @BindView(R.id.crtbttn)
        ImageView cartbutton;


        IRecyclerViewClickListener listener;

        public void setListener(IRecyclerViewClickListener listener) {
            this.listener = listener;
        }

        private Unbinder unbinder;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
            cartbutton.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            listener.onRecylerClick(view, getAdapterPosition());
        }
    }
}
