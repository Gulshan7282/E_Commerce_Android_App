package com.gulshan.e_commerce_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.icu.util.ULocale;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gulshan.e_commerce_app.R;
import com.gulshan.e_commerce_app.activities.CategoryActivity;
import com.gulshan.e_commerce_app.databinding.ItemCategoriesBinding;
import com.gulshan.e_commerce_app.model.Category;

import java.util.ArrayList;

public class CategoryAdapter extends  RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    Context context;
   ArrayList<Category> categories;

   public CategoryAdapter(Context context, ArrayList<Category> categories){
       this.context = context;
       this.categories = categories;
   }


    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.item_categories,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.binding.lable.setText(Html.fromHtml(category.getName()));
        Glide.with(context)   //Glide supports fetching, decoding, and displaying video stills, images, and animated GIFs. Glide includes a flexible API that allows developers to plug in to almost any network stack. By default Glide uses a custom HttpUrlConnection based stack, but also includes utility libraries plug in to Google's Volley project or Square's OkHttp library instead.
                .load(category.getIcon())
                .into(holder.binding.image);


        holder.binding.image.setBackgroundColor(Color.parseColor(category.getColor()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,CategoryActivity.class);
                intent.putExtra("catId", category.getId());
                intent.putExtra("categoryName", category.getName());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {

       return categories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        ItemCategoriesBinding binding;


        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCategoriesBinding.bind(itemView);
        }
    }
}
