package com.example.myqrscanapplication.categories;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.example.myqrscanapplication.MyApplication;
import com.example.myqrscanapplication.R;
import com.example.myqrscanapplication.ui.ProductDetailActivity;
import com.example.myqrscanapplication.data.SubCategory;
import com.example.myqrscanapplication.ui.SubCategoryListActivity;

import java.util.List;

/**
 * Created by yatra on 21/1/16.
 */
public class CategoryListAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    private static final String TAG = CategoryListAdapter.class.getSimpleName();
    public static final String CATEGORY_NAME_EXTRA_KEY = "com.ebcs.category.name";
    public static final String CATEGORY_ID_EXTRA_KEY = "com.ebcs.category.id";
    private List<SubCategory> itemList;
    private Context context;
    private ImageLoader mImageLoader;

    public CategoryListAdapter(Context context, List<SubCategory> itemList) {
        this.itemList = itemList;
        this.context = context;
        mImageLoader = MyApplication.getInstance().getImageLoader();
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, null);
        CategoryViewHolder rcv = new CategoryViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final CategoryViewHolder holder, final int position) {
        SubCategory item = itemList.get(position);
        holder.categoryName.setText(item.getName());
        mImageLoader.get(item.getImageUrl(), new ImageLoader.ImageListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Image Load Error: " + error.getMessage());
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {
                    // load image into imageview
                    holder.categoryImage.setImageBitmap(response.getBitmap());
                }
            }
        });

        holder.category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemList.get(position).getType() == 0) {
                    Intent intent = new Intent(context, SubCategoryListActivity.class);
                    intent.putExtra(CATEGORY_NAME_EXTRA_KEY, itemList.get(position).getName());
                    intent.putExtra(CATEGORY_ID_EXTRA_KEY, itemList.get(position).getProdID());
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.putExtra(CATEGORY_NAME_EXTRA_KEY, itemList.get(position).getName());
                    intent.putExtra(CATEGORY_ID_EXTRA_KEY, itemList.get(position).getProdID());
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}
