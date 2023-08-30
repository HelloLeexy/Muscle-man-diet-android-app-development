package com.example.project.Activity;

import static com.example.project.bean.FoodUtils.getAllFoodList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.SPUtils;
import com.example.project.Adapter.CategoryAdapter;
import com.example.project.Adapter.PopularAdapter;
import com.example.project.Domain.CategoryDomain;
import com.example.project.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.project.bean.foodBean;
import com.google.android.material.imageview.ShapeableImageView;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapter, adapter2;
    private RecyclerView recyclerViewCategoryList, recyclerViewPopularList;
    private ShapeableImageView ivHead;
    private RequestOptions requestOptions = RequestOptions.circleCropTransform()
            .diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
            .skipMemoryCache(true);//不做内存缓存
    TextView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerViewCategory();
        recyclerViewPopular();
        bottomNavigation();
//        ivHead = findViewById(R.id.ri_portrait);
//
//        String imageUrl = SPUtils.getString("imageUrl",null,this);
//        System.out.printf(imageUrl);
//        if(imageUrl != null){
//            Glide.with(this).load(imageUrl).apply(requestOptions).into(ivHead);
//        }
    }

    private void bottomNavigation() {
        FloatingActionButton floatingActionButton = findViewById(R.id.card_btn);
        LinearLayout infoBtn = findViewById(R.id.infoBtn);
        view = findViewById(R.id.editTextTextPersonName);
        //LinearLayout homeBtn = findViewById(R.id.homeBtn);

        view.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,HomeMenuActivity.class);
                startActivity(intent);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CartListActivity.class));
            }
        });

        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MyinfoActivity.class));
            }
        });

//        homeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, MainActivity.class));
//            }
//        });
    }

    private void recyclerViewPopular() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewPopularList = findViewById(R.id.recyclerView2);
        recyclerViewPopularList.setLayoutManager(linearLayoutManager);

//        ArrayList<FoodDomain> foodlist = new ArrayList<>();
//        foodlist.add(new FoodDomain("披萨", "pizza1", "slices pepperoni ,mozzarella cheese, fresh oregano,  ground black pepper, pizza sauce", 235));
//        foodlist.add(new FoodDomain("芝士汉堡", "burger", "beef, Gouda Cheese, Special sauce, Lettuce, tomato ", 455));
//        foodlist.add(new FoodDomain("素食汉堡", "pizza2", " olive oil, Vegetable oil, pitted Kalamata, cherry tomatoes, fresh oregano, basil", 120));

        ArrayList<foodBean> foodlist1  = getAllFoodList();
        ArrayList<foodBean> foodlist = new ArrayList<>();


        foodlist.add(foodlist1.get(0));
        foodlist.add(foodlist1.get(7));
        foodlist.add(foodlist1.get(10));
        foodlist.add(foodlist1.get(16));
        foodlist.add(foodlist1.get(21));
        adapter2 = new PopularAdapter(foodlist);
        recyclerViewPopularList.setAdapter(adapter2);

    }

    private void recyclerViewCategory() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategoryList = findViewById(R.id.recyclerView);
        recyclerViewCategoryList.setLayoutManager(linearLayoutManager);

        ArrayList<CategoryDomain> categoryList = new ArrayList<>();
        categoryList.add(new CategoryDomain("Staple", ""));
        categoryList.add(new CategoryDomain("Nuts", ""));
        categoryList.add(new CategoryDomain("Meat&Egg", ""));
        categoryList.add(new CategoryDomain("Fruit&Veg", ""));
        categoryList.add(new CategoryDomain("Snacks", ""));
        categoryList.add(new CategoryDomain("Dairy", ""));
        categoryList.add(new CategoryDomain("Condiment", ""));

        adapter = new CategoryAdapter(categoryList);
        recyclerViewCategoryList.setAdapter(adapter);
    }
}