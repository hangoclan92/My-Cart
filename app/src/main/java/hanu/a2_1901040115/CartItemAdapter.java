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

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemHolder> {
    private List<Product> list;
    private TextView sumList;

    public CartItemAdapter(List<Product> list, TextView sumList) {
        this.list = list;
        this.sumList = sumList;
    }

    protected class CartItemHolder extends RecyclerView.ViewHolder {
        private Context context;

        public CartItemHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
        }

        public void bind(Product product) {

            ImageView imageView = itemView.findViewById(R.id.iv_image);
            TextView numItems = itemView.findViewById(R.id.item_nums);
            TextView name = itemView.findViewById(R.id.tv_name);
            TextView price = itemView.findViewById(R.id.tv_price);
            TextView sum = itemView.findViewById(R.id.tv_sum_price);
            ImageView up = itemView.findViewById(R.id.iv_up);
            ImageView down = itemView.findViewById(R.id.iv_down);

            Handler handler = new Handler(Looper.getMainLooper());

            Constant.executor.execute(() -> {
                Bitmap bitmap = downloadImage(product.getThumbnail());
                handler.post(() -> {
                    imageView.setImageBitmap(bitmap);
                });
            });

            name.setText(product.getName());
            price.setText((int) product.getPrice() + "");
            numItems.setText(product.getNumItems() + "");
            sum.setText((int) (product.getPrice() * product.getNumItems()) + "");
            setSum();
            up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    product.setNumItems(product.getNumItems() + 1);
                    ProductManager.getInstance(context).update(product);
                    notifyItemChanged(list.indexOf(product));
                    setSum();
                }
            });
            down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (product.getNumItems() == 1) {
                        ProductManager.getInstance(context).remove(product);
                        int index = list.indexOf(product);
                        list.remove(product);
                        notifyItemRemoved(index);
                        setSum();
                        return;
                    }
                    product.setNumItems(product.getNumItems() - 1);
                    ProductManager.getInstance(context).update(product);
                    notifyItemChanged(list.indexOf(product));
                    setSum();
                }
            });
        }

        private void setSum() {
            int sumListInt = 0;
            for (int i = 0; i < list.size(); i++) {
                sumListInt = (int) (sumListInt + (list.get(i).getPrice() * list.get(i).getNumItems()));
            }
            sumList.setText("đ " + sumListInt );
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
    public CartItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View itemView = layoutInflater.inflate(R.layout.cart_item, parent, false);

        return new CartItemHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
