package com.example.myqrscanapplication.cart.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myqrscanapplication.cart.CartItem;

import java.util.ArrayList;

/**
 * Created by yatra on 21/1/16.
 */
public class CartDBHelper extends SQLiteOpenHelper {

    private static CartDBHelper mCartDBHelper;

    public static final String DATABASE_NAME = "MyCART.db";
    public static final int DB_VERSION = 1;
    public static final String CART_TABLE_NAME = "cart";
    public static final String CART_PRODUCT_ID = "id";
    public static final String CART_PRODUCT_NAME = "name";
    public static final String CART_PRODUCT_PRICE = "price";
    public static final String CART_PRODUCT_QUANTITY = "quantity";

    public static synchronized CartDBHelper getInstance(Context context) {
        if(mCartDBHelper == null)
            mCartDBHelper = new CartDBHelper(context.getApplicationContext());
        return mCartDBHelper;
    }

    private CartDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + CART_TABLE_NAME + " " +
                        "(id TEXT PRIMARY KEY, name TEXT,price REAL,quantity INTEGER)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+CART_TABLE_NAME);
        onCreate(db);
    }

    public boolean addItem  (String name, String prodID, float price, int quant)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("id", prodID);
        contentValues.put("price", price);
        contentValues.put("quantity", quant);
        db.insert(CART_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getItem(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+CART_TABLE_NAME+" where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CART_TABLE_NAME);
        return numRows;
    }

    public boolean updateItem (String id, String name, float price, int quant)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("id", id);
        contentValues.put("price", price);
        contentValues.put("quantity",quant);
        db.update("contacts", contentValues, "id = ? ", new String[] { id } );
        return true;
    }

    public Integer deleteItem (String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(CART_TABLE_NAME,
                "id = ? ",
                new String[] { id });
    }

    public ArrayList<CartItem> getAllItems()
    {
        ArrayList<CartItem> itemList = new ArrayList<CartItem>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + CART_TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            CartItem item = new CartItem(res.getString(res.getColumnIndex(CART_PRODUCT_ID))
                    ,res.getString(res.getColumnIndex(CART_PRODUCT_NAME))
                    ,res.getFloat(res.getColumnIndex(CART_PRODUCT_PRICE))
                    ,res.getInt(res.getColumnIndex(CART_PRODUCT_QUANTITY)));
            itemList.add(item);
            res.moveToNext();
        }
        return itemList;
    }

}
