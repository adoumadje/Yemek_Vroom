package com.example.yemekvroom.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.yemekvroom.R;
import com.example.yemekvroom.models.Menu;

import java.util.List;

public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.MyViewHolder> {

    private List<Menu> menuList;
    private MenuListClickListener clickListener;

    public MenuListAdapter(List<Menu> menuList,
                           MenuListClickListener clickListener) {
        this.menuList = menuList;
        this.clickListener = clickListener;
    }

    public void updateData(List<Menu> menuList) {
        this.menuList = menuList;
        notifyDataSetChanged();
    }

    public interface MenuListClickListener {
        public void onAddToCartClick(Menu menu);
        public void onUpdateCartClick(Menu menu);
        public void onRemoveFromCartClick(Menu menu);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.menu_recycler_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(holder.ivMenuImage)
                .load(menuList.get(position).getUrl())
                .into(holder.ivMenuImage);
        holder.tvMenuName.setText(menuList.get(position).getName());
        holder.tvMenuPrice.setText("Price: $"+menuList.get(position).getPrice());
        holder.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Menu menu = menuList.get(holder.getAdapterPosition());
                menu.setTotalInCart(1);
                clickListener.onAddToCartClick(menu);
                holder.addMoreLayout.setVisibility(View.VISIBLE);
                holder.btnAddToCart.setVisibility(View.GONE);
                holder.tvCount.setText(""+menu.getTotalInCart());
            }
        });
        holder.ivMinusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Menu menu = menuList.get(holder.getAdapterPosition());
                int total = menu.getTotalInCart();
                --total;
                menu.setTotalInCart(total);
                if(total > 0) {
                    clickListener.onUpdateCartClick(menu);
                    holder.tvCount.setText(""+menu.getTotalInCart());
                } else {
                    clickListener.onRemoveFromCartClick(menu);
                    holder.addMoreLayout.setVisibility(View.GONE);
                    holder.btnAddToCart.setVisibility(View.VISIBLE);
                }
            }
        });
        holder.ivPlusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Menu menu = menuList.get(holder.getAdapterPosition());
                int total = menu.getTotalInCart();
                ++total;
                if (total > 10) return;
                menu.setTotalInCart(total);
                clickListener.onUpdateCartClick(menu);
                holder.tvCount.setText(""+menu.getTotalInCart());
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvMenuName, tvMenuPrice, tvCount;
        ImageView ivMenuImage, ivMinusBtn, ivPlusBtn;
        Button btnAddToCart;
        LinearLayout addMoreLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMenuName = itemView.findViewById(R.id.tv_MenuName);
            tvMenuPrice = itemView.findViewById(R.id.tv_MenuPrice);
            tvCount = itemView.findViewById(R.id.tv_Count);
            ivMenuImage = itemView.findViewById(R.id.iv_MenuImage);
            ivMinusBtn = itemView.findViewById(R.id.iv_MinusBtn);
            ivPlusBtn = itemView.findViewById(R.id.iv_PlusBtn);
            btnAddToCart = itemView.findViewById(R.id.btn_AddToCart);
            addMoreLayout = itemView.findViewById(R.id.addMoreLayout);
        }
    }
}
