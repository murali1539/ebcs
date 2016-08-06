package com.example.myqrscanapplication.cart;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.myqrscanapplication.MyApplication;
import com.example.myqrscanapplication.R;
import com.example.myqrscanapplication.SharedPreference;
import com.example.myqrscanapplication.cart.db.CartDBHelper;
import com.example.myqrscanapplication.util.GridSpacingItemDecoration;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yatra on 22/1/16.
 */
public class ShoppingCartActivity extends AppCompatActivity {

    private TextView mEmptyTextView;
    private RecyclerView mCartItemListView;
    private TextView mTotalPrice;
    private RelativeLayout mCheckoutView;

    private LinearLayoutManager mListManager;
    private CartItemListAdapter mAdapter;
    private List<CartItem> mCartList;
    private ProgressBar mProgressBar;
//    private CartDBHelper mCartDBHelper;

    private static final String CART_URL="http://52.33.238.16:8002/grocshop/get/cart/?phone=%s";
    private String mPhoneNumber;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_cart);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext = this;
        mEmptyTextView = (TextView) findViewById(R.id.empty_cart);
        mCartItemListView = (RecyclerView)findViewById(R.id.cart_item_list);
        mCheckoutView = (RelativeLayout) findViewById(R.id.checkout_view);
        mTotalPrice = (TextView) findViewById(R.id.total_price);
        mProgressBar = (ProgressBar) findViewById(R.id.cart_progress);

//        readCartDB();
        mCartList = new ArrayList<>();
        mPhoneNumber = SharedPreference.getString(mContext,SharedPreference.USER_NUMBER,"");
        getCart();
        mListManager = new LinearLayoutManager(this);

        mCartItemListView.setHasFixedSize(true);
        mCartItemListView.setLayoutManager(mListManager);
        mCartItemListView.addItemDecoration(new GridSpacingItemDecoration(1, 10, false));

        mAdapter = new CartItemListAdapter(this, mCartList, new CartChangedListener() {
            @Override
            public void onListChanged() {
                calculateTotalPrice();
            }

            @Override
            public void itemRemoved(String id) {
//                mCartDBHelper.deleteItem(id);
            }
        });

        mCartItemListView.setAdapter(mAdapter);
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

    protected void onResume() {
        super.onResume();
        /*if(mCartList!=null && !mCartList.isEmpty()) {
            mEmptyTextView.setVisibility(View.GONE);
            mCartItemListView.setVisibility(View.VISIBLE);
            mCheckoutView.setVisibility(View.VISIBLE);
            calculateTotalPrice();
        } else {
            mEmptyTextView.setVisibility(View.VISIBLE);
            mCartItemListView.setVisibility(View.GONE);
            mCheckoutView.setVisibility(View.GONE);
        }*/
    }

    private void readCartDB() {
//        mCartDBHelper = CartDBHelper.getInstance(getApplicationContext());
//        mCartList = mCartDBHelper.getAllItems();
    }

    private float calculateTotalPrice() {
        float totalPrice = 0.0f;
        for(CartItem item : mCartList) {
            totalPrice = totalPrice + (item.getPrice()*item.getQuant());
        }
        mTotalPrice.setText(totalPrice+"");
        return totalPrice;
    }

    private void getCart() {
        StringRequest request = new StringRequest(Request.Method.GET, String.format(CART_URL,mPhoneNumber), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("cart","received response : " + response);
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                try {
                    JSONObject responseObj = new JSONObject(response);
                    JSONArray cartArr = responseObj.getJSONArray("cart");
                    for(int i=0;i<cartArr.length();i++) {
                        JSONObject cartObj = cartArr.getJSONObject(i);
                        CartItem item = gson.fromJson(cartObj.toString(),CartItem.class);
                        mCartList.add(item);
                    }
                    mProgressBar.setVisibility(View.INVISIBLE);
                    if(mCartList.size()==0) {
                        mCartItemListView.setVisibility(View.INVISIBLE);
                        mEmptyTextView.setVisibility(View.VISIBLE);
                        mCheckoutView.setVisibility(View.INVISIBLE);
                    } else {
                        mCartItemListView.setVisibility(View.VISIBLE);
                        mEmptyTextView.setVisibility(View.INVISIBLE);
                        mCheckoutView.setVisibility(View.VISIBLE);
                    }
                    calculateTotalPrice();
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("cart", "JsonException");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext, "Network Error !!! please try again some time later", Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.INVISIBLE);
                mCartItemListView.setVisibility(View.INVISIBLE);
                mEmptyTextView.setVisibility(View.VISIBLE);
                mCheckoutView.setVisibility(View.INVISIBLE);
//                Log.e(SubCategoryListActivity.class.getSimpleName(), error.getMessage());
            }
        });
        /*{
            @Override
            protected Map<String, String> getParams() {
            HashMap<String,String> params = new HashMap<>();
            params.put("phone",mPhoneNumber);
            return params;
        }
        }*/
        request.setShouldCache(false);
        request.setTag(this);
        MyApplication.getInstance().addToRequestQueue(request);
    }

    @Override
    protected void onDestroy() {
        if(mCartList!=null && !mCartList.isEmpty())
            mCartList.clear();
//        mCartDBHelper.close();
        super.onDestroy();
    }

    public interface CartChangedListener {
        public void onListChanged();
        public void itemRemoved(String id);
    }

}
