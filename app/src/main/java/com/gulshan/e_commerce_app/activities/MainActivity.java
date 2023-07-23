package com.gulshan.e_commerce_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gulshan.e_commerce_app.adapters.CategoryAdapter;
import com.gulshan.e_commerce_app.adapters.ProductAdapter;
import com.gulshan.e_commerce_app.databinding.ActivityMainBinding;
import com.gulshan.e_commerce_app.model.Category;
import com.gulshan.e_commerce_app.model.Product;
import com.gulshan.e_commerce_app.utils.Constants;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    CategoryAdapter categoryAdapter;
    ArrayList<Category> categories;



    ProductAdapter productAdapter;
    ArrayList<Product> products;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("query", text.toString());
                startActivity(intent);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

        initCategories();
        initProduct();
        initSilder();
    }

    private void initSilder() {
        getRecentOffers();
//        binding.carousel.addData(new CarouselItem("https://tutorials.mianasad.com/ecommerce/uploads/news/1689617482037.jpg", "Some Caption here"));
//        binding.carousel.addData(new CarouselItem("https://w7.pngwing.com/pngs/934/355/png-transparent-graphy-best-offer-thumbnail.png", "Some Caption here"));
    }

    void initCategories() {
        categories = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(this, categories);

        getCategories();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        binding.categoryList.setLayoutManager(layoutManager);
        binding.categoryList.setAdapter(categoryAdapter);
    }

    void getCategories() {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, Constants.GET_CATEGORIES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.e("err",response);
                try {
                    JSONObject mainObj = new JSONObject(response);
                    if(mainObj.getString("status").equals("success")){
                        JSONArray categoriesArray = mainObj.getJSONArray("categories");
                        for(int i =0; i< categoriesArray.length(); i++){
                            JSONObject object =  categoriesArray.getJSONObject(i);
                            Category category = new Category(
                                    object.getString("name"),
                                    Constants.CATEGORIES_IMAGE_URL + object.getString("icon"),
                                    object.getString("color"),
                                    object.getString("brief"),
                                    object.getInt("id")
                            );
                            categories.add(category);
                        }
                        categoryAdapter.notifyDataSetChanged();
                    }else{
                        // Da not anything
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }

    void getRecentProducts() {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = Constants.GET_PRODUCTS_URL + "?count=8";
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if(object.getString("status").equals("success")){
                    JSONArray productsArray = object.getJSONArray("products");
                    for(int i =0; i< productsArray.length(); i++) {
                        JSONObject childObj = productsArray.getJSONObject(i);
                        Product product = new Product(
                                childObj.getString("name"),
                                Constants.PRODUCTS_IMAGE_URL + childObj.getString("image"),
                                childObj.getString("status"),
                                childObj.getDouble("price"),
                                childObj.getDouble("price_discount"),
                                childObj.getInt("stock"),
                                childObj.getInt("id")

                        );
                        products.add(product);
                    }
                    productAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> { });

        queue.add(request);
    }

    void getRecentOffers() {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET,Constants.GET_OFFERS_URL, response -> {
            try {
                JSONObject  object = new JSONObject(response);
                if(object.getString("status").equals("success")) {
                    JSONArray offerArray = object.getJSONArray("news_infos");
                    for(int i = 0; i < offerArray.length(); i++){
                        JSONObject childObj = offerArray.getJSONObject(i);
                        binding.carousel.addData(
                                new CarouselItem(
                                        Constants.NEWS_IMAGE_URL + childObj.getString("image"),
                                        childObj.getString("title")
                                )
                        );
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {});
        queue.add(request);
    }

    void initProduct(){
        products = new ArrayList<>();
        productAdapter = new ProductAdapter(this, products);

        getRecentProducts();

        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        binding.productList.setLayoutManager(layoutManager);
        binding.productList.setAdapter(productAdapter);
    }
}