package com.example.project.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project.Activity.ShowDetailActivity;
import com.example.project.R;
import com.example.project.bean.foodBean;

import java.util.ArrayList;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.ViewHolder> {
    //ArrayList<FoodDomain> foodDomains;
    ArrayList<foodBean> foodBeans;

//    public PopularAdapter(ArrayList<FoodDomain> FoodDomains) {
//        this.foodDomains = FoodDomains;
//    }

    public PopularAdapter(ArrayList<foodBean> FoodBeans) {
        this.foodBeans = FoodBeans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_popular, parent, false);

        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //holder.title.setText(foodDomains.get(position).getTitle());
        //holder.fee.setText(String.valueOf(foodDomains.get(position).getFee()));

        holder.title.setText(foodBeans.get(position).getTitle());
        holder.fee.setText(foodBeans.get(position).getDetail().substring(0, foodBeans.get(position).getDetail().indexOf(".")));

//        int drawableResourceId = holder.itemView.getContext().getResources().getIdentifier(foodDomains.get(position).getPic(),
//                "drawable", holder.itemView.getContext().getPackageName());

        int drawableResourceId = holder.itemView.getContext().getResources().getIdentifier(String.valueOf(foodBeans.get(position).getPicId()),
                "drawable", holder.itemView.getContext().getPackageName());

        Glide.with(holder.itemView.getContext())
                .load(drawableResourceId)
                .into(holder.pic);

        holder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), ShowDetailActivity.class);
                intent.putExtra("object", foodBeans.get(position));
                holder.itemView.getContext().startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return foodBeans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, fee;
        ImageView pic;
        TextView addBtn;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            fee = itemView.findViewById(R.id.fee);
            pic = itemView.findViewById(R.id.pic);
            addBtn = itemView.findViewById(R.id.addBtn);
        }
    }
}
