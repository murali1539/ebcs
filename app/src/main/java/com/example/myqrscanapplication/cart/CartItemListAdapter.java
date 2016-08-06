package com.example.myqrscanapplication.cart;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myqrscanapplication.R;

import java.util.List;

/**
 * Created by yatra on 22/1/16.
 */
public class CartItemListAdapter extends RecyclerView.Adapter<CartItemHolder> {

    private Context mContext;
    private List<CartItem> mItemList;
    private ShoppingCartActivity.CartChangedListener listener;

    public CartItemListAdapter(Context context, List<CartItem> itemList
            , ShoppingCartActivity.CartChangedListener listener) {
        mContext = context;
        mItemList = itemList;
        this.listener = listener;
    }

    @Override
    public CartItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_cart_item, null);
        CartItemHolder holder = new CartItemHolder(layoutView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final CartItemHolder holder, final int position) {
        try{
            final CartItem item = mItemList.get(position);
            holder.mQuantity.setText(item.getQuant()+"");
            holder.mProductName.setText(item.getName());
            holder.mPrice.setText(item.getPrice()*item.getQuant() + "");
            holder.mQuantUnit.setText(item.getQuantUnit());

            holder.mReduce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.getQuant() <= 0)
                        return;
                    item.setQuant(item.getQuant() - 1);
                    holder.mQuantity.setText(item.getQuant()+"");
                    holder.mPrice.setText(item.getPrice() * item.getQuant() + "");
                    listener.onListChanged();
                }
            });

            holder.mIncrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    item.setQuant(item.getQuant()+1);
                    holder.mQuantity.setText(item.getQuant()+"");
                    holder.mPrice.setText(item.getPrice() * item.getQuant() + "");
                    listener.onListChanged();
                }
            });

            holder.mRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = mItemList.get(position).getId();
                    mItemList.remove(position);
                    listener.onListChanged();
                    listener.itemRemoved(id);
                }
            });

        } catch (IndexOutOfBoundsException ex) {
            Log.e("CartItemList","index out of bound exception while loading cart");
            ex.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        if(mItemList!=null)
        return mItemList.size();
        else return 0;
    }
}
