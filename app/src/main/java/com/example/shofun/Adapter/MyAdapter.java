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
import com.example.shofun.Models.ShirtCart;
import com.example.shofun.R;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private Context context;
    private List<ShirtCart> shirtCartList;

    public MyAdapter(Context context, List<ShirtCart> shirtCartList) {
        this.context = context;
        this.shirtCartList = shirtCartList;
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
        holder.txtPrice.setText(new StringBuilder("RP").append(shirtCartList.get(position).getPrice()));
        holder.txtName.setText(new StringBuilder("").append(shirtCartList.get(position).getName()));


    }

    @Override
    public int getItemCount() {
        return shirtCartList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageview)
        ImageView imageView;
        @BindView(R.id.txtName)
        TextView txtName;
        @BindView(R.id.txtPrice)
        TextView txtPrice;

        private Unbinder unbinder;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);

        }
    }
}
