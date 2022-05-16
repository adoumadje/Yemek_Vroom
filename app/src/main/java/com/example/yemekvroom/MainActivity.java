package com.example.yemekvroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.yemekvroom.models.Restaurant;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class MainActivity extends AppCompatActivity {

    Button btnFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnFirebase = findViewById(R.id.btn_Firebase);
        btnFirebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToFirebase();
            }
        });
    }

    private List<Restaurant> getRestaurantsListFromJSON() {
        InputStream inputStream = getResources().openRawResource(R.raw.restaurants);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
            int n;
            while ((n=reader.read(buffer)) != -1) {
                writer.write(buffer,0,n);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String jsonStr = writer.toString();
        Gson gson = new Gson();
        Restaurant[] restaurants = gson.fromJson(jsonStr,Restaurant[].class);
        List<Restaurant> restaurantList = Arrays.asList(restaurants);
        return restaurantList;
    }

    private void sendToFirebase() {
        List<Restaurant> restaurantList = getRestaurantsListFromJSON();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String DOCUMENT_ID;
        Restaurant restaurant;
        for (int i = 0; i < restaurantList.size(); ++i) {
            restaurant = restaurantList.get(i);
            DOCUMENT_ID = "Restuarant"+i;
            db.collection("Restaurants").document(DOCUMENT_ID).set(restaurant)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(MainActivity.this,
                                    "Successfully Saved", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this,
                                    "Failed to save", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        btnFirebase.setEnabled(false);
    }
}