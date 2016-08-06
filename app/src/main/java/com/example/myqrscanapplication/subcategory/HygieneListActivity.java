package com.example.myqrscanapplication.subcategory;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.example.myqrscanapplication.R;
import com.example.myqrscanapplication.categories.CategoryListAdapter;
import com.example.myqrscanapplication.data.SubCategory;
import com.example.myqrscanapplication.util.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yatra on 24/1/16.
 */
public class HygieneListActivity extends AppCompatActivity {

    public GridLayoutManager layoutManager;

    public List<SubCategory> groceryList;

    protected void onCreate(Bundle savedInstancestate) {
        super.onCreate(savedInstancestate);
        setContentView(R.layout.category_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Hygiene");

        groceryList = new ArrayList<>();
        /*groceryList.add(new SubCategory("Soap", GroceryListActivity.class, R.drawable.rice));
        groceryList.add(new SubCategory("Hair Care", GroceryListActivity.class, R.drawable.dal));
        groceryList.add(new SubCategory("Deo & Talc", GroceryListActivity.class, R.drawable.flour_atta));
        groceryList.add(new SubCategory("Sanitary Care", GroceryListActivity.class, R.drawable.spice));*/

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
//                NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
