package com.example.myqrscanapplication.cart;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myqrscanapplication.R;

import java.util.List;

/**
 * Created by yatra on 22/1/16.
 */
public class CartItemHolder extends RecyclerView.ViewHolder {

    public TextView mProductName;
    public TextView mQuantity;
    public TextView mPrice;
    public ImageView mReduce;
    public ImageView mIncrease;
    public ImageView mRemove;
    public TextView mQuantUnit;

    public CartItemHolder(View itemView) {
        super(itemView);

        mProductName = (TextView) itemView.findViewById(R.id.product_name);
        mQuantity = (TextView) itemView.findViewById(R.id.cart_quant);
        mPrice = (TextView) itemView.findViewById(R.id.cart_price);
        mRemove = (ImageView) itemView.findViewById(R.id.remove_item);
        mIncrease = (ImageView) itemView.findViewById(R.id.cart_plus);
        mReduce = (ImageView) itemView.findViewById(R.id.cart_minus);
        mQuantUnit = (TextView) itemView.findViewById(R.id.product_quant_value);
    }
}
