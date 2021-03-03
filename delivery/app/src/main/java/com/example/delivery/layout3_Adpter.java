package com.example.delivery;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class layout3_Adpter extends RecyclerView.Adapter<layout3_Adpter.CustomViewHolder> {
    final String TAG = "Connect";
    private ArrayList<layout3_list> mList = null;
    private Activity context = null;
    private String rider_id;

    public layout3_Adpter(Activity context, String rider_id, ArrayList<layout3_list> list) {
        this.context = context;
        this.mList = list;
        this.rider_id = rider_id;

    }


    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView date, s_address, items;
        protected TextView price, d_type_tv1;
        protected LinearLayout privatereview;
        protected String u_number, s_number, d_type;
        protected Button getorder;

        public CustomViewHolder(View view) {
            super(view);
            this.date = (TextView) view.findViewById(R.id.date);
            this.price = (TextView) view.findViewById(R.id.price);
            this.privatereview = (LinearLayout) view.findViewById(R.id.privatereview);
            this.d_type_tv1 = (TextView) view.findViewById(R.id.d_type);
        }
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout3_info, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder viewholder, final int position) {
        viewholder.d_type = mList.get(position).getMember_d_type();
        if(viewholder.d_type.equals("0")){
            viewholder.d_type_tv1.setText("픽업");
            viewholder.date.setText(mList.get(position).getMember_temp_date());
            viewholder.price.setText(mList.get(position).getMember_r_price());
        }
        else{
            viewholder.d_type_tv1.setText("배달");
            viewholder.date.setText(mList.get(position).getMember_temp_date());
            viewholder.price.setText(mList.get(position).getMember_r_price());
        }
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }
}

