package com.example.project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;
import com.example.project.bean.foodBean;

public class FoodDetailActivity extends AppCompatActivity {
    TextView food_title1, food_title2, food_power, food_comment;
    ImageView backIv, food_pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        initView();
//        接收上一级页面传来的数据
        Intent intent = getIntent();
        foodBean foodBean = (foodBean) intent.getSerializableExtra("food");
//        设置显示控件
        food_title1.setText(foodBean.getTitle());
        food_title2.setText(foodBean.getTitle());
        food_power.setText(foodBean.getDetail() + "kcal/100克");
        food_comment.setText(foodBean.getDesc());
        food_pic.setImageResource(foodBean.getPicId());

    }

    private void initView() {
        food_title1 = findViewById(R.id.food_title1);
        food_title2 = findViewById(R.id.food_title2);
        food_power = findViewById(R.id.food_power);
        food_comment = findViewById(R.id.food_comment);
        backIv = findViewById(R.id.foodDesc_back);
        food_pic = findViewById(R.id.foodDesc_pic);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//销毁当前的activity
            }
        });
    }
}