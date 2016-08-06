package com.example.myqrscanapplication.categories;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myqrscanapplication.R;

/**
 * Created by yatra on 21/1/16.
 */
public class CategoryViewHolder extends RecyclerView.ViewHolder{

    public TextView categoryName;
    public ImageView categoryImage;
    public RelativeLayout category;

    public CategoryViewHolder(View itemView) {
        super(itemView);
        categoryName = (TextView) itemView.findViewById(R.id.category_name);
        categoryImage = (ImageView) itemView.findViewById(R.id.category_image);
        category = (RelativeLayout) itemView.findViewById(R.id.category_item);
    }

}
