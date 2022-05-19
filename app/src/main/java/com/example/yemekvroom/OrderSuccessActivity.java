package com.example.yemekvroom;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.yemekvroom.API.JavaMailAPI;
import com.example.yemekvroom.models.Restaurant;

public class OrderSuccessActivity extends AppCompatActivity {

    Button btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);

        Restaurant restaurant = getIntent().getParcelableExtra("Restaurant");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(restaurant.getName());
        actionBar.setSubtitle(restaurant.getAddress());
        String emailAddress = getIntent().getStringExtra("Email");
        String totalAmount = getIntent().getStringExtra("Total");
        sendEmail(emailAddress, totalAmount);

        btnDone = findViewById(R.id.btn_Done);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void sendEmail(String emailAddress, String totalAmount) {
        String subject = "Order Confirmed";
        String message = "Your order has been sent to the restaurant."
                        + "\nTotal bill: "+totalAmount;
        JavaMailAPI javaMailAPI = new JavaMailAPI(this, emailAddress, subject, message);
        javaMailAPI.execute();
    }
}