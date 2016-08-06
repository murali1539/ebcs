package com.example.myqrscanapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.myqrscanapplication.cart.ShoppingCartActivity;
import com.example.myqrscanapplication.cart.db.CartDBHelper;
import com.example.myqrscanapplication.categories.CategoryListAdapter;
import com.example.myqrscanapplication.data.SubCategory;
import com.example.myqrscanapplication.util.GridSpacingItemDecoration;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ScanActivity extends AppCompatActivity {

    private GridLayoutManager layoutManager;
    private CartDBHelper mCartDBHelper;

    private List<SubCategory> subCategories;
    private Context mContext;
    private CategoryListAdapter categoryListAdapter;
//    http://demo8249346.mockable.io/category?id=0 10.0.2.2:8000
    public static final String CATEGORY_URL = "http://52.33.238.16:8002/grocshop/listOfItems/";
    private boolean apiOngoing = false;

    //TODO change the content in the QR code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContext = this;

        mCartDBHelper = CartDBHelper.getInstance(getApplicationContext());
        subCategories = new ArrayList<>();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ShoppingCartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        findViewById(R.id.scan_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, 0);
            }
        });

        /*List<SubCategory> rowListItem = new ArrayList<SubCategory>();
        rowListItem.add(new SubCategory("Grocery", GroceryListActivity.class,R.drawable.grocery));
        rowListItem.add(new SubCategory("Hygiene", GroceryListActivity.class, R.drawable.hygiene));
        rowListItem.add(new SubCategory("Cleaning", GroceryListActivity.class, R.drawable.cleaning));
        rowListItem.add(new SubCategory("food", GroceryListActivity.class, R.drawable.food));
        rowListItem.add(new SubCategory("beverages", GroceryListActivity.class, R.drawable.beverages));*/
//        rowListItem.add("Household");

        /*List<Integer> drawables = new ArrayList<>();
        drawables.add(R.drawable.grocery);
        drawables.add(R.drawable.hygiene);
        drawables.add(R.drawable.cleaning);
        drawables.add(R.drawable.food);
        drawables.add(R.drawable.beverages);*/
//        drawables.add(R.drawable.household);

        layoutManager = new GridLayoutManager(this, 2);
        RecyclerView rView = (RecyclerView)findViewById(R.id.category_container);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(layoutManager);
        rView.addItemDecoration(new GridSpacingItemDecoration(2, 50, false));

        categoryListAdapter = new CategoryListAdapter(this, subCategories);
        rView.setAdapter(categoryListAdapter);
//        getCategories(CATEGORY_URL);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(subCategories!=null && subCategories.isEmpty())
            getCategories(CATEGORY_URL);
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                apiOngoing = false;
                Toast.makeText(mContext,"Network Error !!! please try again",Toast.LENGTH_SHORT).show();
//                Log.e(ScanActivity.class.getSimpleName(), error.getMessage());
            }
        });
        request.setShouldCache(false);
        request.setTag(this);
        MyApplication.getInstance().addToRequestQueue(request);
        return items;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        if(requestCode == 0){
            if(resultCode == RESULT_OK){
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                String[] details = contents.split(";");
                mCartDBHelper.addItem(details[0],details[2],Float.parseFloat(details[1]),1);
                Toast.makeText(this,"contents: "+contents+" format: "+format,Toast.LENGTH_SHORT).show();
                Log.i("xZing", "contents: "+contents+" format: "+format); // Handle successful scan
            }
            else if(resultCode == RESULT_CANCELED){ // Handle cancel
                Log.i("xZing", "Cancelled");
                Toast.makeText(this,"Cancelled",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        mCartDBHelper.close();
        super.onDestroy();
    }
}
