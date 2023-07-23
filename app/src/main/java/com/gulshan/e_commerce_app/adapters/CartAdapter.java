package com.gulshan.e_commerce_app.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gulshan.e_commerce_app.R;
import com.gulshan.e_commerce_app.databinding.ItemCartBinding;
import com.gulshan.e_commerce_app.databinding.QuantityDilaogBinding;
import com.gulshan.e_commerce_app.model.Product;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.util.TinyCartHelper;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    Context context;
    ArrayList<Product> products;
    CartListener cartListener;
    Cart cart;

    public interface CartListener {
        public void onQuantityChanged();
    }

    public CartAdapter(Context context, ArrayList<Product> products, CartListener cartListener){
        this.context = context;
        this.products = products;
        this.cartListener = cartListener;
        cart = TinyCartHelper.getCart();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cart, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product product = products.get(position);
        Glide.with(context)
                .load(product.getImage())
                .into(holder.binding.image);

        holder.binding.name.setText(product.getName());
        holder.binding.price.setText(" INR" + product.getPrice());
        holder.binding.quantity.setText(product.getQuantity() +" item(s)");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                QuantityDilaogBinding quantityDilaogBinding = QuantityDilaogBinding.inflate(LayoutInflater.from(context));
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setView(quantityDilaogBinding.getRoot())
                        .create();

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));

                quantityDilaogBinding.productName.setText(product.getName());
                quantityDilaogBinding.productStock.setText("Stock" + product.getStock());
                quantityDilaogBinding.quantity.setText(String.valueOf(product.getQuantity()));
                int stock = product.getStock();

                quantityDilaogBinding.plusBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int quantity = product.getQuantity();
                        quantity++;

                        if(quantity > product.getStock()) {
                            Toast.makeText(context, "Max Stock available"+ product.getStock(), Toast.LENGTH_SHORT).show();
                            return;
                        }else{
                            product.setQuantity(quantity);
                            quantityDilaogBinding.quantity.setText(String.valueOf(quantity));

                        }
                        notifyDataSetChanged();
                        cart.updateItem(product, product.getQuantity());
                        cartListener.onQuantityChanged();
                    }
                });

                quantityDilaogBinding.minusBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int quantity = product.getQuantity();
                        if(quantity > 1)
                            quantity--;
                        product.setQuantity(quantity);
                        quantityDilaogBinding.quantity.setText(String.valueOf(quantity));

                        notifyDataSetChanged();
                        cart.updateItem(product, product.getQuantity());
                        cartListener.onQuantityChanged();

                    }
                });

                quantityDilaogBinding.saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
//                        notifyDataSetChanged();
//                        cart.updateItem(product, product.getQuantity());
//                        cartListener.onQuantityChanged();
                    }
                });

                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class CartViewHolder  extends RecyclerView.ViewHolder {
        ItemCartBinding binding;
        public CartViewHolder(@NonNull View itemview ) {
            super(itemview);
            binding = ItemCartBinding.bind(itemview);
        }
    }
}
