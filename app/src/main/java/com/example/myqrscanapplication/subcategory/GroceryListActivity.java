package com.example.myqrscanapplication.subcategory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.myqrscanapplication.R;
import com.example.myqrscanapplication.cart.ShoppingCartActivity;
import com.example.myqrscanapplication.categories.CategoryListAdapter;
import com.example.myqrscanapplication.data.SubCategory;
import com.example.myqrscanapplication.util.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yatra on 24/1/16.
 */
public class GroceryListActivity extends AppCompatActivity {

    public GridLayoutManager layoutManager;

    public List<SubCategory> groceryList;

    protected void onCreate(Bundle savedInstancestate) {
        super.onCreate(savedInstancestate);
        setContentView(R.layout.category_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Groceries");

        groceryList = new ArrayList<>();
        /*groceryList.add(new SubCategory("rice", RiceListActivity.class, R.drawable.rice));
        groceryList.add(new SubCategory("dal", DalListActivity.class, R.drawable.dal));
        groceryList.add(new SubCategory("Flour & Atta", RiceListActivity.class, R.drawable.flour_atta));
        groceryList.add(new SubCategory("Spices", RiceListActivity.class, R.drawable.spice));*/

        /*List<Integer> drawables = new ArrayList<>();
        drawables.add(R.drawable.rice);
        drawables.add(R.drawable.dal);
        drawables.add(R.drawable.flour_atta);
        drawables.add(R.drawable.spice);*/

        layoutManager = new GridLayoutManager(this,2);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.sub_category_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 50, false));

        CategoryListAdapter rcAdapter = new CategoryListAdapter(this, groceryList);
        recyclerView.setAdapter(rcAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.shopping_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.shop_menu:
                Intent intent = new Intent(getApplicationContext(),ShoppingCartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
