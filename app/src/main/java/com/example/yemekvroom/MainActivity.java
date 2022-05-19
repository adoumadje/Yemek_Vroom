package com.example.yemekvroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.yemekvroom.adapters.RestaurantListAdapter;
import com.example.yemekvroom.models.Restaurant;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RestaurantListAdapter.RestaurantListClickListener {

    int COLLECTION_SIZE = 4;
    Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Restaurants List");

        getAndDisplayRestaurantList();
    }

    private void initRecylerView(List<Restaurant> restaurantList) {
        RecyclerView recyclerView = findViewById(R.id.rv_RestaurantList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RestaurantListAdapter adapter = new RestaurantListAdapter(restaurantList, this);
        recyclerView.setAdapter(adapter);
    }

    private void getAndDisplayRestaurantList() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference restaurantRef;
        List<Restaurant> restaurantList = new ArrayList<>();
        for (int i = 0; i < COLLECTION_SIZE; ++i) {
            restaurantRef = db.document("Restaurants/Restuarant"+i);
            restaurantRef.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            restaurant = documentSnapshot.toObject(Restaurant.class);
                            restaurantList.add(restaurant);
                            if(restaurantList.size() == COLLECTION_SIZE){
                                initRecylerView(restaurantList);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this,
                                    "Failed reading data", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    public void onItemClick(Restaurant restaurant) {
        Intent intent = new Intent(MainActivity.this, RestaurantMenuActivity.class);
        intent.putExtra("Restaurant", restaurant);
        startActivity(intent);
    }
}