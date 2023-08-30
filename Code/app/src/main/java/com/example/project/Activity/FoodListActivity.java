package com.example.project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.Adapter.FoodListAdapter;
import com.example.project.R;
import com.example.project.bean.FoodUtils;
import com.example.project.bean.foodBean;

import java.util.ArrayList;
import java.util.List;

public class FoodListActivity extends AppCompatActivity implements View.OnClickListener{
    EditText searchEt;
    TextView searchCancel;
    TextView searchIv;
    ListView showLv;
    List<foodBean> mDatas;
    List<foodBean> allFoodList;
    private FoodListAdapter foodListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        initView();
        Intent intentRecieve = getIntent();
        String msg = intentRecieve.getStringExtra("foodname");
        searchEt.setText(msg);



        mDatas = new ArrayList<>();
        allFoodList = FoodUtils.getAllFoodList();
        mDatas.addAll(allFoodList);
//        add an adpter
        foodListAdapter = new FoodListAdapter(this, mDatas);
        showLv.setAdapter(foodListAdapter); //设置适配器
        //设置单向监听
        setListener();

        List<foodBean> list = new ArrayList<>();
        for (int i = 0; i < allFoodList.size(); i++) {
            String title = allFoodList.get(i).getTitle();
            if (title.contains(msg)){
                list.add(allFoodList.get(i));
            }

        }
        mDatas.clear(); //清空listview的适配器数据内容
        mDatas.addAll(list); //添加新的数据源
    }

    private void setListener() {
        showLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                foodBean foodBean = mDatas.get(i);
                Intent intent = new Intent(FoodListActivity.this, FoodDetailActivity.class);
                intent.putExtra("food",foodBean);
                startActivity(intent);


            }
        });
    }


    private void initView() {
        searchEt = (EditText) findViewById(R.id.info_et_search);
        searchIv = findViewById(R.id.goSearch);
        searchCancel = findViewById(R.id.cancel);
        showLv = findViewById(R.id.infolist_lv);
//        添加点击事件
        searchIv.setOnClickListener(this);//


        searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String msg = searchEt.getText().toString().trim(); //获取输入信息
                    if (TextUtils.isEmpty(msg)){
                        Toast.makeText(FoodListActivity.this, "输入内容不能为空",Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    //判断食物列表的标题是否包含输入内容，如果包含添加到小集合中
                    List<foodBean> list = new ArrayList<>();
                    for (int i = 0; i < allFoodList.size(); i++) {
                        String title = allFoodList.get(i).getTitle();
                        if (title.contains(msg)){
                            list.add(allFoodList.get(i));
                        }

                    }
                    mDatas.clear(); //清空listview的适配器数据内容
                    mDatas.addAll(list); //添加新的数据源
                    foodListAdapter.notifyDataSetChanged(); //提示适配器更新
                }else{
                    System.out.println("没有感应到回车");
                }

                return false;
            }
        });

        searchCancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel: //刷新
                searchEt.setText("");
                mDatas.clear();
                mDatas.addAll(allFoodList);
                foodListAdapter.notifyDataSetChanged();
                break;
            case R.id.goSearch: //搜索
                String msg = searchEt.getText().toString().trim(); //获取输入信息
                if (TextUtils.isEmpty(msg)){
                    Toast.makeText(this, "输入内容不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                //判断食物列表的标题是否包含输入内容，如果包含添加到小集合中
                List<foodBean> list = new ArrayList<>();
                for (int i = 0; i < allFoodList.size(); i++) {
                    String title = allFoodList.get(i).getTitle();
                    if (title.contains(msg)){
                        list.add(allFoodList.get(i));
                    }

                }
                mDatas.clear(); //清空listview的适配器数据内容
                mDatas.addAll(list); //添加新的数据源
                foodListAdapter.notifyDataSetChanged(); //提示适配器更新
                break;
        }
    }
}