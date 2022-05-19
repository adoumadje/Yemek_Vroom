package com.example.yemekvroom;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.example.yemekvroom.adapters.PlaceYourOrderAdapter;
import com.example.yemekvroom.models.Menu;
import com.example.yemekvroom.models.Restaurant;

public class PlaceYourOrderActivity extends AppCompatActivity {

    TextView tvSubTotalAmount, tvDeliveryCharge, tvDeliveryChargeAmount, tvTotalAmount;
    EditText etInputName, etInputEmail, etInputAddress;
    EditText etInputCardNumber, etInputCardExpiry, etInputCardCVC;
    Button btnPlaceYourOrder;
    Switch switchDelivery;
    boolean isDeliveryOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_your_order);

        Restaurant restaurant = getIntent().getParcelableExtra("Restaurant");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(restaurant.getName());
        actionBar.setSubtitle(restaurant.getAddress());
        actionBar.setDisplayHomeAsUpEnabled(true);
        isDeliveryOn = false;

        tvSubTotalAmount = findViewById(R.id.tv_SubTotalAmount);
        tvDeliveryCharge = findViewById(R.id.tv_DeliveryCharge);
        tvDeliveryChargeAmount = findViewById(R.id.tv_DeliveryChargeAmount);
        tvTotalAmount = findViewById(R.id.tv_TotalAmount);

        etInputName = findViewById(R.id.et_InputName);
        etInputEmail = findViewById(R.id.et_InputEmail);
        etInputAddress = findViewById(R.id.et_InputAddress);
        etInputCardNumber = findViewById(R.id.et_InputCardNumber);
        etInputCardExpiry = findViewById(R.id.et_InputCardExpiry);
        etInputCardCVC = findViewById(R.id.et_InputCardCVC);

        switchDelivery = findViewById(R.id.switchDelivery);
        switchDelivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) {
                    isDeliveryOn = true;
                    etInputAddress.setVisibility(View.VISIBLE);
                    calculateTotalAmount(restaurant);
                } else {
                    isDeliveryOn = false;
                    etInputAddress.setVisibility(View.GONE);
                    calculateTotalAmount(restaurant);
                }
            }
        });

        btnPlaceYourOrder = findViewById(R.id.btn_PlaceYourOrder);
        btnPlaceYourOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPlaceOrderButtonClick(restaurant);
            }
        });

        initRecyclerView(restaurant);
        calculateTotalAmount(restaurant);
    }

    private void initRecyclerView(Restaurant restaurant) {
        RecyclerView recyclerView = findViewById(R.id.rv_CartItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        PlaceYourOrderAdapter adapter = new PlaceYourOrderAdapter(restaurant.getMenus());
        recyclerView.setAdapter(adapter);
    }

    private void calculateTotalAmount(Restaurant restaurant) {
        float subTotalAmount = 0f;
        for (Menu menu: restaurant.getMenus()) {
            subTotalAmount += menu.getTotalInCart() * menu.getPrice();
        }
        tvSubTotalAmount.setText("$"+String.format("%.2f", subTotalAmount));
        if(isDeliveryOn) {
            tvDeliveryChargeAmount.setText("$"+String.format("%.2f", restaurant.getDelivery_charge()));
            subTotalAmount += restaurant.getDelivery_charge();
        }
        tvTotalAmount.setText("$"+String.format("%.2f", subTotalAmount));
    }

    private void onPlaceOrderButtonClick(Restaurant restaurant) {
        String errorStr = "This area can't be empty";
        if(TextUtils.isEmpty(etInputName.getText().toString())) {
            etInputName.setError(errorStr);
        } else if(TextUtils.isEmpty(etInputEmail.getText().toString())) {
            etInputEmail.setError(errorStr);
        } else if(isDeliveryOn && TextUtils.isEmpty(etInputAddress.getText().toString())) {
            etInputAddress.setError(errorStr);
        } else if(TextUtils.isEmpty(etInputCardNumber.getText().toString())) {
            etInputCardNumber.setError(errorStr);
        } else if(TextUtils.isEmpty(etInputCardExpiry.getText().toString())) {
            etInputCardExpiry.setError(errorStr);
        } else if(TextUtils.isEmpty(etInputCardCVC.getText().toString())) {
            etInputCardCVC.setError(errorStr);
        } else {
            Intent intent = new Intent(PlaceYourOrderActivity.this, OrderSuccessActivity.class);
            String email = etInputEmail.getText().toString();
            String totalAmount = tvTotalAmount.getText().toString();
            intent.putExtra("Restaurant", restaurant);
            intent.putExtra("Email", email);
            intent.putExtra("Total", totalAmount);
            startActivityForResult(intent, 100);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100 && resultCode == Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: finish();
            default: // do nothing
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}