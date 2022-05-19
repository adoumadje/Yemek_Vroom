package com.example.yemekvroom;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.yemekvroom.adapters.MenuListAdapter;
import com.example.yemekvroom.models.Menu;
import com.example.yemekvroom.models.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class RestaurantMenuActivity extends AppCompatActivity implements MenuListAdapter.MenuListClickListener {

    private Button btnCheckout;
    private List<Menu> menuList;
    private List<Menu> itemsInCartList;
    private int totalItemsInCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu);

        Restaurant restaurant = getIntent().getParcelableExtra("Restaurant");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(restaurant.getName());
        actionBar.setSubtitle(restaurant.getAddress());
        actionBar.setDisplayHomeAsUpEnabled(true);

        menuList = restaurant.getMenus();
        itemsInCartList = new ArrayList<>();
        totalItemsInCart = 0;
        initRecyclerView();

        btnCheckout = findViewById(R.id.btn_Checkout);
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (totalItemsInCart == 0) {
                    Toast.makeText(RestaurantMenuActivity.this,
                            "Add item(s) to cart before checkout", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(RestaurantMenuActivity.this, PlaceYourOrderActivity.class);
                restaurant.setMenus(itemsInCartList);
                intent.putExtra("Restaurant", restaurant);
                startActivityForResult(intent, 3000);
            }
        });
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.rv_RestaurantMenu);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        MenuListAdapter adapter = new MenuListAdapter(menuList, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onAddToCartClick(Menu menu) {
        itemsInCartList.add(menu);
        totalItemsInCart = 0;
        for (Menu item: itemsInCartList) {
            totalItemsInCart += item.getTotalInCart();
        }
        btnCheckout.setText("Checkout ("+totalItemsInCart+") items");
    }

    @Override
    public void onUpdateCartClick(Menu menu) {
        if(itemsInCartList.contains(menu)) {
            int index = itemsInCartList.indexOf(menu);
            itemsInCartList.remove(index);
            itemsInCartList.add(index, menu);
            totalItemsInCart = 0;
            for (Menu item: itemsInCartList) {
                totalItemsInCart += item.getTotalInCart();
            }
            btnCheckout.setText("Checkout ("+totalItemsInCart+") items");
        }
    }

    @Override
    public void onRemoveFromCartClick(Menu menu) {
        if(itemsInCartList.contains(menu)) {
            itemsInCartList.remove(menu);
            totalItemsInCart = 0;
            for(Menu item: itemsInCartList) {
                totalItemsInCart += item.getTotalInCart();
            }
            btnCheckout.setText("Checkout ("+totalItemsInCart+") items");
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: finish();
            default: // Do nothing
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 3000 && resultCode == Activity.RESULT_OK) {
            finish();
        }
    }
}