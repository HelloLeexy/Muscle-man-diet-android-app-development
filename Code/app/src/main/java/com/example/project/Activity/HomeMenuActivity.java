package com.example.project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Adapter.BaseRecycleAdapter;
import com.example.project.Helper.DbOperation;
import com.example.project.R;
import com.example.project.Adapter.SearchRecordAdapter;

public class HomeMenuActivity extends AppCompatActivity{
    private TextView goserarch;
    private TextView cancel;
    private EditText info_et_search;
    private RecyclerView mRecyclerView;
    private TextView mtv_deleteAll;
    private SearchRecordAdapter mAdapter;

    private DbOperation mDbDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_menu);
        initView();
    }


    private void initView() {
        mDbDao =new DbOperation(this);
        goserarch = (TextView) findViewById(R.id.goSearch);
        info_et_search = (EditText) findViewById(R.id.info_et_search);
        mtv_deleteAll = (TextView) findViewById(R.id.tv_deleteAll);
        cancel = (TextView) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                info_et_search.setText("");
                mAdapter.updata(mDbDao.queryData(""));
            }
        });

        info_et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (info_et_search.getText().toString().trim().length() != 0){
                        String msg = info_et_search.getText().toString().trim();

                        boolean hasData = mDbDao.hasData(msg);

                        if (!hasData){
                            mDbDao.insertData(msg);

                        }else {
                            Toast.makeText(HomeMenuActivity.this, "该内容已在历史记录中", Toast.LENGTH_SHORT).show();
                        }

                        Intent intent = new Intent(HomeMenuActivity.this, FoodListActivity.class);
                        intent.putExtra("foodname",msg);
                        startActivity(intent);

                        //
                        mAdapter.updata(mDbDao.queryData(""));

                    }else {
                        Toast.makeText(HomeMenuActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    System.out.println("没有感应到回车");
                }

                return false;
            }
        });

        mtv_deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDbDao.deleteData();
                mAdapter.updata(mDbDao.queryData(""));
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter =new SearchRecordAdapter(mDbDao.queryData(""),this);
        mAdapter.setRvItemOnclickListener(new BaseRecycleAdapter.RvItemOnclickListener() {
            @Override
            public void RvItemOnclick(int position) {
                mDbDao.delete(mDbDao.queryData("").get(position));

                mAdapter.updata(mDbDao.queryData(""));
            }
        });

        mAdapter.setItemOnclickListener(new BaseRecycleAdapter.ItemOnclickListener(){

            @Override
            public void ItemOnclick(int position) {
                String msg = mDbDao.queryData("").get(position);
                Intent intent = new Intent(HomeMenuActivity.this, FoodListActivity.class);
                intent.putExtra("foodname",msg);
                startActivity(intent);
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        //事件监听
        goserarch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (info_et_search.getText().toString().trim().length() != 0){
                    String msg = info_et_search.getText().toString().trim();

                    boolean hasData = mDbDao.hasData(msg);

                    if (!hasData){
                        mDbDao.insertData(msg);

                    }else {
                        Toast.makeText(HomeMenuActivity.this, "该内容已在历史记录中", Toast.LENGTH_SHORT).show();
                    }

                    Intent intent = new Intent(HomeMenuActivity.this, FoodListActivity.class);
                    intent.putExtra("foodname",msg);
                    startActivity(intent);

                    //
                    mAdapter.updata(mDbDao.queryData(""));

                }else {
                    Toast.makeText(HomeMenuActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}