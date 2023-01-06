package hanu.a2_1901040115;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import hanu.a2_1901040115.constant.Constant;
import hanu.a2_1901040115.db.ProductManager;
import hanu.a2_1901040115.models.Product;

public class ShoppingItemAdapter extends RecyclerView.Adapter<ShoppingItemAdapter.ShoppingItemHolder> {
    private List<Product> list;

    public ShoppingItemAdapter(List<Product> list) {
        this.list = list;
    }

    protected class ShoppingItemHolder extends RecyclerView.ViewHolder {
        private Context context;

        public ShoppingItemHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
        }

        public void bind(Product product) {

            TextView name = itemView.findViewById(R.id.textView);
            name.setText(product.getName());
            TextView price = itemView.findViewById(R.id.textView2);
            price.setText((int) product.getPrice() + "");
            Handler handler = new Handler(Looper.getMainLooper());

            ImageView imageView = itemView.findViewById(R.id.imageView);

            ImageView addCart = itemView.findViewById(R.id.iv_add_cart);
            addCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "added to cart", Toast.LENGTH_SHORT).show();
                    if (ProductManager.getInstance(context).findById(product.getId()) != null) {
                        int currentNum = ProductManager.getInstance(context).findById(product.getId()).getNumItems();
                        ProductManager.getInstance(context).add(
                                new Product(product.getId(),
                                        product.getThumbnail(),
                                        product.getName(), product.getPrice(),
                                        currentNum + 1)
                        );
                    }
                    else {
                        ProductManager.getInstance(context).add(
                                new Product(product.getId(),
                                        product.getThumbnail(),
                                        product.getName(), product.getPrice(),
                                        product.getNumItems() + 1)
                        );
                    }
                }
            });

            Constant.executor.execute(() -> {
                Bitmap bitmap = downloadImage(product.getThumbnail());
                handler.post(() -> {
                    imageView.setImageBitmap(bitmap);
                });
            });

        }

        private Bitmap downloadImage(String link) {
            try {
                URL url = new URL(link);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream is = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @NonNull
    @Override
    public ShoppingItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View itemView = layoutInflater.inflate(R.layout.shopping_item, parent, false);

        return new ShoppingItemHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingItemHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
