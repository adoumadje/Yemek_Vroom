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
import com.example.yemekvroom.models.Restaurant;

import java.util.List;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.MyViewHolder> {

    private List<Restaurant> restaurantList;
    private RestaurantListClickListener clickListener;


    public RestaurantListAdapter(List<Restaurant> restaurantList,
                                 RestaurantListClickListener clickListener) {
        this.restaurantList = restaurantList;
        this.clickListener = clickListener;
    }

    public void updateData(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
        notifyDataSetChanged();
    }

    public interface RestaurantListClickListener {
        public void onItemClick(Restaurant restaurant);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvRestaurantName.setText(restaurantList.get(position).getName());
        holder.tvRestaurantAddress.setText(restaurantList.get(position).getAddress());
        holder.tvRestaurantHours.setText(restaurantList.get(position).getHours().getTodayHour());
        Glide.with(holder.ivThumbImage)
                .load(restaurantList.get(position).getImage())
                .into(holder.ivThumbImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(restaurantList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvRestaurantName, tvRestaurantAddress, tvRestaurantHours;
        ImageView ivThumbImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRestaurantName = itemView.findViewById(R.id.tv_RestaurantName);
            tvRestaurantAddress = itemView.findViewById(R.id.tv_RestaurantAddress);
            tvRestaurantHours = itemView.findViewById(R.id.tv_RestaurantHours);
            ivThumbImage = itemView.findViewById(R.id.iv_ThumbImage);
        }
    }
}
