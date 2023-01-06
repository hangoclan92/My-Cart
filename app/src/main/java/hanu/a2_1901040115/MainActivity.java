package hanu.a2_1901040115;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import hanu.a2_1901040115.constant.Constant;
import hanu.a2_1901040115.models.Product;

public class MainActivity extends AppCompatActivity {
    private List<Product> list;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);

        list = new ArrayList<>();

        EditText editText = findViewById(R.id.edt_search);
        ImageView search = findViewById(R.id.iv_search);

        Handler handler = new Handler(Looper.getMainLooper());
        List<Product> savedList = new ArrayList<>();
        Constant.executor.execute(() -> {
            String json = loadJSON("https://mpr-cart-api.herokuapp.com/products");
            handler.post(() -> {
                if (json != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(json);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);
                            int id = data.getInt("id");
                            String thumbnail = data.getString("thumbnail");
                            String name = data.getString("name");
                            double unitPrice = data.getDouble("unitPrice");
                            list.add(new Product(id, thumbnail, name, unitPrice));
                        }
                        savedList.addAll(list);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    search.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (editText.getText().toString().isEmpty()) {
                                updateList(savedList);
                            } else {
                                editText.getText().toString();
                                List<Product> newList = new ArrayList<>();
                                for (int i = 0; i < savedList.size(); i++) {
                                    if (savedList.get(i).getName().toLowerCase().contains(editText.getText().toString().toLowerCase())) {
                                        newList.add(savedList.get(i));
                                    }
                                }
                                updateList(newList);
                            }

                            GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);

                            recyclerView.setLayoutManager(gridLayoutManager);

                            ShoppingItemAdapter shoppingItemAdapter = new ShoppingItemAdapter(list);

                            recyclerView.setAdapter(shoppingItemAdapter);
                        }
                    });

                    setAdapter();

                }
            });
        });

    }

    private void setAdapter() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(gridLayoutManager);

        ShoppingItemAdapter shoppingItemAdapter = new ShoppingItemAdapter(list);

        recyclerView.setAdapter(shoppingItemAdapter);
    }

    private void updateList(List<Product> newList) {
        list.clear();
        list.addAll(newList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mybutton) {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public static String loadJSON(String link) {
        URL url;
        HttpURLConnection urlConnection;
        try {
            url = new URL(link);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            InputStream is = urlConnection.getInputStream();
            Scanner sc = new Scanner(is);
            StringBuilder result = new StringBuilder();
            String line;
            while (sc.hasNextLine()) {
                line = sc.nextLine();
                result.append(line);
            }
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}