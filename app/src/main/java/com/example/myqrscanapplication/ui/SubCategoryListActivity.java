package com.example.myqrscanapplication.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.myqrscanapplication.MyApplication;
import com.example.myqrscanapplication.R;
import com.example.myqrscanapplication.cart.ShoppingCartActivity;
import com.example.myqrscanapplication.categories.CategoryListAdapter;
import com.example.myqrscanapplication.data.SubCategory;
import com.example.myqrscanapplication.util.GridSpacingItemDecoration;
import com.example.myqrscanapplication.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yatra on 7/3/16.
 */
public class SubCategoryListActivity extends AppCompatActivity {

    public static final String CATEGORY_NAME_EXTRA_KEY = "com.ebcs.category.name";
    public static final String CATEGORY_ID_EXTRA_KEY = "com.ebcs.category.id";
//    http://demo8249346.mockable.io/category?id=%s
    public static final String CATEGORY_URL = "http://52.33.238.16:8002/grocshop/listOfItems/?ques=%s";
    public GridLayoutManager layoutManager;
    private Context mContext;
    public List<SubCategory> subCategories;
    private String categoryName;
    private String categoryId;
    private boolean apiOngoing = false;
    private CategoryListAdapter categoryListAdapter;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;

    protected void onCreate(Bundle savedInstancestate) {
        super.onCreate(savedInstancestate);
        setContentView(R.layout.category_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if(intent == null) {
            finish();
        } else {
            categoryName = getIntent().getStringExtra(CATEGORY_NAME_EXTRA_KEY);
            categoryId = getIntent().getStringExtra(CATEGORY_ID_EXTRA_KEY);
            getSupportActionBar().setTitle(categoryName);
        }

        if(StringUtils.isEmptyOrNull(categoryId))
            finish();

        mContext = this;
        subCategories = new ArrayList<>();

        layoutManager = new GridLayoutManager(this,2);

        mRecyclerView = (RecyclerView) findViewById(R.id.sub_category_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 50, false));

        mProgressBar = (ProgressBar) findViewById(R.id.category_progress_bar);

        categoryListAdapter = new CategoryListAdapter(this, subCategories);
        mRecyclerView.setAdapter(categoryListAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(subCategories!=null && subCategories.isEmpty())
            getCategories(String.format(CATEGORY_URL,categoryId));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(apiOngoing) {
            MyApplication.getInstance().cancelPendingRequests(this);
            apiOngoing = false;
        }
    }

    public List<SubCategory> getCategories(String url) {
        apiOngoing = true;
        final List<SubCategory> items = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                try {
                    JSONArray arr = new JSONObject(response).getJSONArray("categories");
                    for(int i=0;i<arr.length();i++) {
                        JSONObject ele = arr.getJSONObject(i);
                        SubCategory category = gson.fromJson(ele.toString(),SubCategory.class);
                        items.add(category);
                    }
                    subCategories.addAll(items);
                    apiOngoing = false;
                    categoryListAdapter.notifyDataSetChanged();
                    mProgressBar.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                apiOngoing = false;
                Toast.makeText(mContext, "Network Error !!! please try again some time later", Toast.LENGTH_SHORT).show();
//                Log.e(SubCategoryListActivity.class.getSimpleName(), error.getMessage());
            }
        });
        request.setShouldCache(false);
        request.setTag(this);
        MyApplication.getInstance().addToRequestQueue(request);
        return items;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.shopping_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
//        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.shop_menu).setVisible(false);
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
