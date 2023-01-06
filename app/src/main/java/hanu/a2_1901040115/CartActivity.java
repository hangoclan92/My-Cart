package hanu.a2_1901040115;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hanu.a2_1901040115.db.ProductManager;
import hanu.a2_1901040115.models.Product;

public class CartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ProductManager productManager = ProductManager.getInstance(this);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        List<Product> list = new ArrayList<>(productManager.all());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        TextView sumList = findViewById(R.id.tv_sum_list);

        CartItemAdapter shoppingItemAdapter = new CartItemAdapter(list, sumList);

        recyclerView.setAdapter(shoppingItemAdapter);
    }

}