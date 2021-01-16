package com.example.user;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class user_itempage_Adpter extends RecyclerView.Adapter<user_itempage_Adpter.CustomViewHolder> {

    private ArrayList<user_itempage_list> mList = null;
    private Activity context = null;

    String user_name;
    String user_address;
    Double user_lat;
    Double user_long;
    String user_id;
    String title;

    public user_itempage_Adpter(Activity context, ArrayList<user_itempage_list> list, String user_name, String user_address,
                        Double user_lat, Double user_long, String title, String user_id) {
        this.context = context;
        this.mList = list;
        this.user_name = user_name;
        this.user_address = user_address;
        this.user_lat = user_lat;
        this.user_long = user_long;
        this.title = title;
        this.user_id = user_id;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView menu;
        protected TextView price;
        protected LinearLayout layout_get_item;
        protected Button b1,b2;
        protected TextView tv1;

        public CustomViewHolder(View view) {
            super(view);
            this.menu = (TextView) view.findViewById(R.id.item_name);
            this.price = (TextView) view.findViewById(R.id.price);
            this.layout_get_item = (LinearLayout) view.findViewById(R.id.layout_get_item);
            this.b1 = (Button) view.findViewById(R.id.putitem);
            this.b2 = (Button) view.findViewById(R.id.deleteitem);
            this.tv1 = view.findViewById(R.id.count);
        }


    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_itempage_info, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder viewholder, final int position) {
        final String a,b;
        viewholder.menu.setText(mList.get(position).getMember_menu());
        viewholder.price.setText(mList.get(position).getMember_price()+"원");
        a = mList.get(position).getMember_menu();
        b = mList.get(position).getMember_price();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if(success){
                        int count = jsonObject.getInt("count");
                        viewholder.tv1.setText(String.valueOf(count));
                    }
                    else{
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        user_itempage_temp1_count_db registerRequest = new user_itempage_temp1_count_db(a, Integer.parseInt(b), user_id, title,responseListener);
        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
        queue.add(registerRequest);

        viewholder.b1.setOnClickListener(new View.OnClickListener() {
            String menu = a;
            int price = Integer.parseInt(b);

            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){
                                int count = jsonObject.getInt("count");
                                viewholder.tv1.setText(String.valueOf(count));
                            }
                            else{
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };
                user_itempage_temp1_db registerRequest = new user_itempage_temp1_db(menu, price, user_id, title,responseListener);
                RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
                queue.add(registerRequest);
            }
        });

        viewholder.b2.setOnClickListener(new View.OnClickListener() {
            String menu = a;
            int price = Integer.parseInt(b);

            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){
                                int count = jsonObject.getInt("count");
                                viewholder.tv1.setText(String.valueOf(count));
                            }
                            else{
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };
                user_itempage_temp1_del_db registerRequest = new user_itempage_temp1_del_db(menu, price, user_id, title,responseListener);
                RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
                queue.add(registerRequest);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}


