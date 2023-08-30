package com.example.project.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.List;
import com.example.project.R;

import static com.example.project.R.id.tv_delete;
import static com.example.project.R.id.tv_record;

public class SearchRecordAdapter extends BaseRecycleAdapter<String> {
    public SearchRecordAdapter(List<String> datas, Context mContext) {
        super(datas, mContext);
    }

    @Override
    protected void bindData(BaseViewHolder holder, final int position) {

        TextView textView= (TextView) holder.getView(tv_record);
        textView.setText(datas.get(position));

        //
        holder.getView(tv_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null!=mRvItemOnclickListener){
                    mRvItemOnclickListener.RvItemOnclick(position);
                }
            }
        });


        holder.getView(tv_record).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null!=mItemOnclickListener){
                    mItemOnclickListener.ItemOnclick(position);
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.search_item;
    }
}
