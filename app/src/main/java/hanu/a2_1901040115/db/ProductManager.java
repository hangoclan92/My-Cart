package hanu.a2_1901040115.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.List;

import hanu.a2_1901040115.models.Product;

public class ProductManager {
    private static ProductManager instance;
    private static DbHelper dbHelper;
    private static SQLiteDatabase db;

    private ProductManager(Context context) {
        dbHelper = new DbHelper(context);
        db = dbHelper.getReadableDatabase();
    }


    public static ProductManager getInstance(Context context) {
        if (instance == null) {
            instance = new ProductManager(context);
        }
        return instance;
    }

    public void add(Product product) {
        if (findById(product.getId()) != null) {
            Log.d("TAGAAXAXAXAX", "add: "+ update(product));
            return;
        }
        String sqlAdd = "INSERT INTO cart VALUES (" + product.getId() + "," +
                "'" + product.getThumbnail() + "'," +
                "'" + product.getName() + "'," + product.getPrice() + "," + product.getNumItems() + ")";
        db.execSQL(sqlAdd);
    }


    public List<Product> all() {
        Cursor cursor = db.rawQuery("SELECT * FROM cart", null);
        ProductCursorWrapper cursorWrapper = new ProductCursorWrapper(cursor);
        List<Product> list = cursorWrapper.getProducts();
        cursor.close();
        return list;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        db.close();
    }

    public Product findById(int id) {
        Cursor cursor = db.rawQuery("SELECT * FROM cart WHERE id=" + id, null);
        ProductCursorWrapper cursorWrapper = new ProductCursorWrapper(cursor);
        Product product = cursorWrapper.getProduct();
        cursor.close();
        return product;
    }

    public boolean update(Product product) {
        String sqlUpdate = "UPDATE cart SET numItems=? WHERE id=?";
        SQLiteStatement statement = db.compileStatement(sqlUpdate);
        statement.bindLong(1, product.getNumItems());
        statement.bindLong(2, product.getId());
        int numOfAffectedRow = statement.executeUpdateDelete();
        return numOfAffectedRow > 0;
    }

    public boolean remove(Product product) {
        String sqlUpdate = "DELETE FROM cart WHERE id=?";
        SQLiteStatement statement = db.compileStatement(sqlUpdate);
        statement.bindLong(1, product.getId());
        int numOfAffectedRow = statement.executeUpdateDelete();
        return numOfAffectedRow > 0;
    }}
