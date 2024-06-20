package com.example.wine_shop.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.wine_shop.R;

public class OrderViewHolder extends RecyclerView.ViewHolder {
    public TextView txtOrderDate, txtOrderStatus, txtOrderTotalAmount, txtOrderAddress, txtOrderTime;

    public OrderViewHolder(View itemView) {
        super(itemView);

        txtOrderDate = itemView.findViewById(R.id.order_date);
        txtOrderTime = itemView.findViewById(R.id.order_time); // Добавлено поле для времени
        txtOrderStatus = itemView.findViewById(R.id.order_status);
        txtOrderTotalAmount = itemView.findViewById(R.id.order_total_amount);
        txtOrderAddress = itemView.findViewById(R.id.order_address);
    }
}

