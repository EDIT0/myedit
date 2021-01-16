package com.example.user;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class user_searchhash_Adpter_plus extends RecyclerView.Adapter<user_searchhash_Adpter_plus.CustomViewHolder> {

    private ArrayList<user_searchhash_list_plus> mList = null;
    private Activity context = null;
    public user_main1 um1 = new user_main1();

    String user_name;
    String user_address;
    Double user_lat;
    Double user_long;
    String user_id;
    String user_address_detail;

    static int a = 1;

    public user_searchhash_Adpter_plus(Activity context, ArrayList<user_searchhash_list_plus> list, String user_name, String user_address,
                                       Double user_lat, Double user_long, String user_id, String user_address_detail) {
        this.context = context;
        this.mList = list;
        this.user_name = user_name;
        this.user_address = user_address;
        this.user_lat = user_lat;
        this.user_long = user_long;
        this.user_id = user_id;
        this.user_address_detail = user_address_detail;
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView o_id;
        protected TextView s_name;
        protected TextView o_pw,data2,data3;
        protected LinearLayout intothestore;
        protected CardView cv1;


        public CustomViewHolder(View view) {
            super(view);
            this.s_name = (TextView) view.findViewById(R.id.s_name);
            this.cv1 = view.findViewById(R.id.cardview);
        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_searchhash_plus_info, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, final int position) {
        viewholder.s_name.setText("#"+mList.get(position).getMember_s_name());

        a = (int)(Math.random()*5);

        if(a==0){
            viewholder.s_name.setBackgroundResource(R.drawable.a);
            a++;
        }
        else if(a==1){
            viewholder.s_name.setBackgroundResource(R.drawable.b);
            a++;
        }
        else if(a==2){
            viewholder.s_name.setBackgroundResource(R.drawable.c);
        }
        else if(a==3){
            viewholder.s_name.setBackgroundResource(R.drawable.d);
        }
        else if(a==4){
            viewholder.s_name.setBackgroundResource(R.drawable.e);
        }
        else{

        }

        viewholder.cv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), user_searchhash.class);
                intent.putExtra("user_name", user_name);
                intent.putExtra("user_address", user_address);
                intent.putExtra("user_lat", user_lat);
                intent.putExtra("user_long", user_long);
                intent.putExtra("user_id", user_id);
                intent.putExtra("user_address_detail", user_address_detail);
                intent.putExtra("data1","#"+mList.get(position).getMember_s_name());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }
}
