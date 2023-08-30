package com.example.project.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project.R;
import com.example.project.bean.foodBean;

import java.util.List;

public class FoodListAdapter extends BaseAdapter {
    Context context;
    List<foodBean> mDatas;

    public FoodListAdapter(Context context, List<foodBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_infolist_lv, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();

        }
        foodBean foodBean = mDatas.get(i);
        holder.title.setText(foodBean.getTitle());
        holder.detail.setText("Calorie：" + foodBean.getDetail() + " kcal/100g");
        holder.iv.setImageResource(foodBean.getPicId());
        return view;
    }

    class ViewHolder {
        ImageView iv;
        TextView title, detail;

        public ViewHolder(View view) {
            iv = view.findViewById(R.id.item_info_iv);
            title = view.findViewById(R.id.item_info_tv);
            detail = view.findViewById(R.id.item_info_detail);
        }

    }
}
