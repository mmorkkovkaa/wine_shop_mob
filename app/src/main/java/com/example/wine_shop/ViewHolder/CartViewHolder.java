package com.example.wine_shop.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.wine_shop.R;

public class CartViewHolder extends RecyclerView.ViewHolder {
    public TextView txtProductName, txtProductQuantity, txtProductPrice;

    public CartViewHolder(View itemView) {
        super(itemView);

        txtProductName = itemView.findViewById(R.id.cart_product_name);
        txtProductQuantity = itemView.findViewById(R.id.cart_product_quantity);
        txtProductPrice = itemView.findViewById(R.id.cart_product_price);
    }
}
