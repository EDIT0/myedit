package com.example.delivery;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.delivery.MainActivity.rider_address1;
import static com.example.delivery.MainActivity.rider_id1;
import static com.example.delivery.MainActivity.rider_lat1;
import static com.example.delivery.MainActivity.rider_long1;
import static com.example.delivery.MainActivity.rider_name1;


public class layout1_Adpter extends RecyclerView.Adapter<layout1_Adpter.CustomViewHolder> {
    final String TAG = "Connect";

    private ArrayList<layout1_list> mList = null;
    private Activity context = null;
    private String rider_id;

    public layout1_Adpter(Activity context, String rider_id, ArrayList<layout1_list> list) {
        this.context = context;
        this.mList = list;
        this.rider_id = rider_id;
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView u_address,s_address,items;
        protected TextView price,d_type_tv1,test1,distance;
        protected LinearLayout privatereview;
        protected String u_number,s_number,d_type;
        protected Button getorder;

        public CustomViewHolder(View view) {
            super(view);
            this.u_address = (TextView) view.findViewById(R.id.u_address);
            this.s_address = (TextView) view.findViewById(R.id.s_address);
            this.items = (TextView) view.findViewById(R.id.items);
            this.price = (TextView) view.findViewById(R.id.price);
            this.privatereview = (LinearLayout) view.findViewById(R.id.privatereview);
            this.d_type_tv1 = (TextView) view.findViewById(R.id.d_type);
            this.getorder = (Button) view.findViewById(R.id.getorder);
            this.test1 = (TextView) view.findViewById(R.id.test1);
            this.distance = view.findViewById(R.id.distance);

        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout1_info, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder viewholder, final int position) {
        viewholder.d_type = mList.get(position).getMember_d_type();
        if(viewholder.d_type.equals("0")){
            viewholder.d_type_tv1.setText("픽업");
            viewholder.u_address.setText(mList.get(position).getMember_u_address());
            viewholder.s_address.setText(mList.get(position).getMember_s_address());
        }
        else{
            viewholder.d_type_tv1.setText("배달");
            viewholder.u_address.setText(mList.get(position).getMember_s_address());
            viewholder.s_address.setText(mList.get(position).getMember_u_address());
        }
        viewholder.price.setText(mList.get(position).getMember_price()+"원");
        viewholder.items.setText(mList.get(position).getMember_items());
        viewholder.u_number = mList.get(position).getMember_u_number();
        viewholder.s_number = mList.get(position).getMember_s_number();

        if(mList.get(position).getMember_a().equals("null")){
            viewholder.test1.setVisibility(View.GONE);
            viewholder.distance.setVisibility(View.GONE);
        }
        else {
            viewholder.test1.setVisibility(View.VISIBLE);
            viewholder.distance.setVisibility(View.VISIBLE);
            viewholder.test1.setText(mList.get(position).getMember_a() + "km");
        }

        viewholder.getorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){
                                Intent intent = new Intent(context.getApplicationContext(),MainActivity.class);
                                intent.putExtra("rider_name",rider_name1);
                                intent.putExtra("rider_address",rider_address1);
                                intent.putExtra("rider_lat",rider_lat1);
                                intent.putExtra("rider_long",rider_long1);
                                intent.putExtra("rider_id",rider_id1);
                                context.startActivity(intent);
                            }
                            else{
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };
                layout1_Adpter_db registerRequest = new layout1_Adpter_db(rider_id,mList.get(position).getMember_u_address(),
                        mList.get(position).getMember_s_address(),mList.get(position).getMember_price(),mList.get(position).getMember_items(),
                        mList.get(position).getMember_d_type(),responseListener);
                RequestQueue queue = Volley.newRequestQueue(context.getApplication());
                queue.add(registerRequest);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }
}

