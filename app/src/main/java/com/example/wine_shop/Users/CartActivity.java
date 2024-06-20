package com.example.wine_shop.Users;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wine_shop.Model.Cart;
import com.example.wine_shop.Prevalent.Prevalent;
import com.example.wine_shop.R;
import com.example.wine_shop.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button checkoutButton;

    private TextView closeButn;
    private TextView totalAmountTextView;
    private int totalAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        closeButn = (TextView)findViewById(R.id.close_btn);

        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        checkoutButton = findViewById(R.id.checkout_button);
        totalAmountTextView = findViewById(R.id.total_amount);

        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Переход на страницу оплаты
                Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
                intent.putExtra("Total Amount", totalAmount);
                startActivity(intent);
                finish();
            }
        });

        loadCart();

        closeButn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent closeIntent = new Intent(CartActivity.this, HomeActivity.class);
                startActivity(closeIntent);
            }
        });
    }

    private void loadCart() {
        DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart");

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child(Prevalent.currentOnlineUser.getPhone()), Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {
                holder.txtProductName.setText(model.getPname());
                holder.txtProductQuantity.setText("Количество: " + model.getQuantity());
                holder.txtProductPrice.setText("Цена: " + model.getPrice() + "$");

                int productTotalPrice = Integer.parseInt(model.getPrice()) * Integer.parseInt(model.getQuantity());
                totalAmount += productTotalPrice;
                totalAmountTextView.setText("Итого: " + totalAmount + "$");
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                return new CartViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
