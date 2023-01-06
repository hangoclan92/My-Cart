package hanu.a2_1901040115.db;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.ArrayList;
import java.util.List;

import hanu.a2_1901040115.models.Product;

public class ProductCursorWrapper extends CursorWrapper {
    private Cursor cursor;

    public ProductCursorWrapper(Cursor cursor) {
        super(cursor);
        this.cursor = cursor;
    }

    public Product getProduct() {
        int idIndex = cursor.getColumnIndex("id");
        int thumbnailIndex = cursor.getColumnIndex("thumbnail");
        int nameIndex = cursor.getColumnIndex("name");
        int priceIndex = cursor.getColumnIndex("price");
        int numItemsIndex = cursor.getColumnIndex("numItems");
        if (cursor.moveToNext())
            return (
                    new Product(cursor.getInt(idIndex),
                            cursor.getString(thumbnailIndex),
                            cursor.getString(nameIndex),
                            cursor.getDouble(priceIndex),
                            cursor.getInt(numItemsIndex))
            );
        return null;
    }

    public List<Product> getProducts() {
        List<Product> list = new ArrayList<>();

        int idIndex = cursor.getColumnIndex("id");
        int thumbnailIndex = cursor.getColumnIndex("thumbnail");
        int nameIndex = cursor.getColumnIndex("name");
        int priceIndex = cursor.getColumnIndex("price");
        int numItemsIndex = cursor.getColumnIndex("numItems");

        while (cursor.moveToNext()) {
            Product product = new Product(cursor.getInt(idIndex),
                    cursor.getString(thumbnailIndex),
                    cursor.getString(nameIndex),
                    cursor.getDouble(priceIndex),
                    cursor.getInt(numItemsIndex));
            list.add(product);
        }

        return list;
    }
}
