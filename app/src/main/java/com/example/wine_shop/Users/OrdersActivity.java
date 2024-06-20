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

import com.example.wine_shop.Model.Order;
import com.example.wine_shop.Prevalent.Prevalent;
import com.example.wine_shop.R;
import com.example.wine_shop.SettingsActivity;
import com.example.wine_shop.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class OrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private TextView closeBut;

    DatabaseReference ordersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        closeBut = (TextView)findViewById(R.id.close_bt);
        ordersRef= FirebaseDatabase.getInstance().getReference().child("Orders");
        recyclerView = findViewById(R.id.orders_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        loadOrders();

        closeBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent closeIntent = new Intent(OrdersActivity.this, HomeActivity.class);
                startActivity(closeIntent);
            }
        });
    }

    private void loadOrders() {
        FirebaseRecyclerOptions<Order> options = new FirebaseRecyclerOptions.Builder<Order>()
                .setQuery(ordersRef, Order.class)
                .build();

        FirebaseRecyclerAdapter<Order, OrderViewHolder> adapter = new FirebaseRecyclerAdapter<Order, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull Order model) {
                if (model.getDate() != null && model.getStatus() != null && model.getTotalAmount() != null && model.getAddress() != null) {
                    holder.txtOrderDate.setText("Дата: " + model.getDate());
                    holder.txtOrderTime.setText("Время: " + model.getTime());
                    holder.txtOrderStatus.setText("Статус: " + model.getStatus());
                    holder.txtOrderTotalAmount.setText("Сумма: " + model.getTotalAmount() + "$");
                    holder.txtOrderAddress.setText("Адрес: " + model.getAddress());
                }
            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_layout, parent, false);
                return new OrderViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

}
