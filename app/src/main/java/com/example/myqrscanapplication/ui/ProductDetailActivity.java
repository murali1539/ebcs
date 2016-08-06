package com.example.myqrscanapplication.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.example.myqrscanapplication.MyApplication;
import com.example.myqrscanapplication.R;
import com.example.myqrscanapplication.ScanActivity;
import com.example.myqrscanapplication.SharedPreference;
import com.example.myqrscanapplication.cart.ShoppingCartActivity;
import com.example.myqrscanapplication.cart.db.CartDBHelper;
import com.example.myqrscanapplication.data.Product;
import com.example.myqrscanapplication.data.SubCategory;
import com.example.myqrscanapplication.util.StringUtils;
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
 * Created by yatra on 24/1/16.
 */
public class ProductDetailActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String prodID;
    private String prodName;
    private Product mProduct;
    private String mPhoneNumber;
    private int mPosition = 0;
//    private CartDBHelper mCartDBHelper;
    private String name;
    private Context mContext;
    private List<String> mQuantValues;
    private ArrayAdapter<String> quantAdapter;
    private boolean apiOngoing = false;

    private ImageView mProductImage;
    private Button addButton;
    private TextView mProductDescription;
    private TextView mProductPriceValue;
    private ProgressBar mProgressBar;

    private static final String TAG = ProductDetailActivity.class.getSimpleName();
//    http://demo8249346.mockable.io/product?id=%s
//    52.33.238.16:8002
    public static final String PRODUCT_DETAIL_URL = "http://52.33.238.16:8002/grocshop/productInfo/?productId=%s";
    public static final String ADD_TO_CART_URL = "http://52.33.238.16:8002/grocshop/add/cart/";
    public static final String CATEGORY_NAME_EXTRA_KEY = "com.ebcs.category.name";
    public static final String CATEGORY_ID_EXTRA_KEY = "com.ebcs.category.id";


    /*{"productInfo": [{"item_type": 1, "stock_status": [true],
        "image_url": "http://www.bigbasket.com/media/uploads/p/l/10000454_3-bb-royal-sona-masoori-boiled-rice.jpg",
                "name": "Boiled", "price_value": [50], "quantity_type": ["kg"], "price_type": ["Rs"],
        "quantity_value": [1], "id": 1, "description": "Boiled_desc"}]}*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContext = this;
//        mCartDBHelper = CartDBHelper.getInstance(getApplicationContext());

        Intent intent = getIntent();
        if(intent == null) {
            finish();
        } else {
            prodName = getIntent().getStringExtra(CATEGORY_NAME_EXTRA_KEY);
            prodID = getIntent().getStringExtra(CATEGORY_ID_EXTRA_KEY);
            getSupportActionBar().setTitle(prodName);
        }

        if(StringUtils.isEmptyOrNull(prodID))
            finish();

        mProductImage = (ImageView) findViewById(R.id.prod_image);
        mProductDescription = (TextView) findViewById(R.id.product_description);
        mProductPriceValue = (TextView) findViewById(R.id.product_price_value);
        Spinner spinner = (Spinner) findViewById(R.id.product_qty_spinner);
        spinner.setOnItemSelectedListener(this);

        mQuantValues = new ArrayList<String>();


        // Creating adapter for spinner
        quantAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,mQuantValues);

        // Drop down layout style - list view with radio button
        quantAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(quantAdapter);

        addButton = (Button) findViewById(R.id.prod_add_cart);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_add_cart);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mCartDBHelper.addItem(name, prodID, 50.0f, qty);
//                Toast.makeText(mContext,"Added to cart", Toast.LENGTH_SHORT).show();
//                addButton.setText("ADDED TO CART");
                mProgressBar.setVisibility(View.VISIBLE);
                addToCart();
                v.setEnabled(false);
            }
        });

        mPhoneNumber = SharedPreference.getString(mContext,SharedPreference.USER_NUMBER,"");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mProduct == null)
            getProductDetails(String.format(PRODUCT_DETAIL_URL, prodID));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(apiOngoing) {
            MyApplication.getInstance().cancelPendingRequests(this);
            apiOngoing = false;
        }
    }

    public void getProductDetails(String url) {
        apiOngoing = true;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                try {
                    JSONObject responseObj = new JSONObject(response);
                    JSONObject detailObj = responseObj.getJSONArray("productInfo").getJSONObject(0);
                    mProduct = gson.fromJson(detailObj.toString(),Product.class);
                    apiOngoing = false;
                    updateUI();
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
        request.setShouldCache(true);
        request.setTag(this);
        MyApplication.getInstance().addToRequestQueue(request);
    }

    private void addToCart() {
        if(mProduct == null)
            return;
        StringRequest postRequest = new StringRequest(Request.Method.POST, ADD_TO_CART_URL,
                /*null,*/ new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                {'success': True, 'reason': 'saved the data'}
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.getBoolean("success")) {
                        Toast.makeText(mContext,"Added to cart", Toast.LENGTH_SHORT).show();
                        addButton.setText("ADDED TO CART");
                    } else {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        addButton.setEnabled(true);
                        Toast.makeText(mContext, obj.getString("reason") + "", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                mProgressBar.setVisibility(View.INVISIBLE);
                addButton.setEnabled(true);
                Toast.makeText(mContext, "Connection Error", Toast.LENGTH_SHORT).show();
            }
        }) {

//            phone = request.POST.get('phone','')
//            productId = request.POST.get('prodId','')
//            quantType = request.POST.get('quantType','')
//            quantValue = request.POST.get('quantValue','')
//            priceUnit = request.POST.get('priceUnit','')
//            priceValue = request.POST.get('priceValue','')

            @Override
            public Map<String,String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("phone",mPhoneNumber);
                params.put("prodId",prodID);
                params.put("quantType",mProduct.getQuantTypes()[mPosition]);
                params.put("quantValue",mProduct.getQuantityValues()[mPosition]+"");
                params.put("priceUnit",mProduct.getCurrency()[mPosition]);
                params.put("priceValue",mProduct.getPriceValues()[mPosition]+"");
                return params;
            }
        };
        postRequest.setTag(this);
        postRequest.setRetryPolicy(new DefaultRetryPolicy(30000,2,0));
        MyApplication.getInstance().addToRequestQueue(postRequest);
    }

    private void updateUI() {
        if(mProduct == null)
            return;
        //Update Product Description
        mProductDescription.setText(mProduct.getDescription());
        // Update Spinner
        int[] quants = mProduct.getQuantityValues();
        String[] quantTypes = mProduct.getQuantTypes();
        for(int i=0; i<quants.length; i++) {
            mQuantValues.add(quants[i]+" "+quantTypes[i]);
        }
        //Set Price Value
        mProductPriceValue.setText(mProduct.getPriceValues()[0]+"");
        quantAdapter.notifyDataSetChanged();

        ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();
        imageLoader.get(mProduct.getImageUrl(), new ImageLoader.ImageListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Image Load Error: " + error.getMessage());
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {
                    // load image into imageview
                    mProductImage.setImageBitmap(response.getBitmap());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.shopping_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
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
                Intent intent = new Intent(mContext,ShoppingCartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mPosition = position;
        mProductPriceValue.setText(mProduct.getPriceValues()[position]+"");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
