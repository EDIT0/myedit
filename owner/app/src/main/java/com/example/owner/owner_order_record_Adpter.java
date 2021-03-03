package com.example.owner;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class owner_order_record_Adpter extends RecyclerView.Adapter<owner_order_record_Adpter.CustomViewHolder> {

    private ArrayList<owner_order_record_list> mList = null;
    private Activity context = null;

    String owner_name;
    String owner_address;
    Double owner_lat;
    Double owner_long;
    String store_name;

    public owner_order_record_Adpter(Activity context, ArrayList<owner_order_record_list> list, String owner_name, String owner_address,
                                     Double owner_lat, Double owner_long, String store_name) {
        this.context = context;
        this.mList = list;
        this.owner_name = owner_name;
        this.owner_address = owner_address;
        this.owner_lat = owner_lat;
        this.owner_long = owner_long;
        this.store_name = store_name;
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView u_id;
        protected TextView u_address;
        protected TextView date;

        protected Button b1;

        public CustomViewHolder(View view) {
            super(view);
            this.u_id = (TextView) view.findViewById(R.id.u_id);
            this.u_address = (TextView) view.findViewById(R.id.u_address);
            this.date = (TextView) view.findViewById(R.id.date);
            this.b1 = (Button) view.findViewById(R.id.detail);

        }
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.owner_order_record_info, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, final int position) {
        viewholder.u_id.setText(mList.get(position).getMember_u_id());
        viewholder.u_address.setText(mList.get(position).getMember_u_address());
        viewholder.date.setText("2021"+mList.get(position).getMember_date());

        viewholder.b1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), owner_order_record2.class);
                intent.putExtra("owner_name",owner_name);
                intent.putExtra("owner_address",owner_address);
                intent.putExtra("owner_lat",owner_lat);
                intent.putExtra("owner_long",owner_long);
                intent.putExtra("user_id",String.valueOf(mList.get(position).getMember_u_id()));
                intent.putExtra("store_name",store_name);
                intent.putExtra("date",String.valueOf(mList.get(position).getMember_date()));
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivityForResult(intent,0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}

