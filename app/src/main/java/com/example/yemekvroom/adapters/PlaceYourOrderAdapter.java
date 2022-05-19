package com.example.yemekvroom.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.yemekvroom.R;
import com.example.yemekvroom.models.Menu;

import java.util.List;

public class PlaceYourOrderAdapter extends RecyclerView.Adapter<PlaceYourOrderAdapter.MyViewHolder> {

    private List<Menu> menuList;

    public PlaceYourOrderAdapter(List<Menu> menuList) {
        this.menuList = menuList;
    }

    public void updateData(List<Menu> menuList) {
        this.menuList = menuList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.place_your_order_recycler_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(holder.ivCartImage)
                .load(menuList.get(position).getUrl())
                .into(holder.ivCartImage);
        holder.tvCartMenuName.setText(menuList.get(position).getName());
        float totalItemPrice = menuList.get(position).getPrice() * menuList.get(position).getTotalInCart();
        holder.tvCartMenuPrice.setText("Price: $"+String.format("%.2f", totalItemPrice));
        holder.tvCartMenuQty.setText("Qty: "+menuList.get(position).getTotalInCart());
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvCartMenuName, tvCartMenuPrice, tvCartMenuQty;
        ImageView ivCartImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCartMenuName = itemView.findViewById(R.id.tv_CartMenuName);
            tvCartMenuPrice = itemView.findViewById(R.id.tv_CartMenuPrice);
            tvCartMenuQty = itemView.findViewById(R.id.tv_CartMenuQty);
            ivCartImage = itemView.findViewById(R.id.iv_CartImage);
        }
    }
}
