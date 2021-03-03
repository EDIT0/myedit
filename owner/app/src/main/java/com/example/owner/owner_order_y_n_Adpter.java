package com.example.owner;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class owner_order_y_n_Adpter extends RecyclerView.Adapter<owner_order_y_n_Adpter.CustomViewHolder> {

    private ArrayList<owner_order_y_n_list> mList = null;
    private Activity context = null;

    String owner_name;
    String owner_address;
    Double owner_lat;
    Double owner_long;
    String store_name;

    public owner_order_y_n_Adpter(Activity context, ArrayList<owner_order_y_n_list> list, String owner_name, String owner_address,
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
        protected TextView item;
        protected TextView u_number;
        protected TextView u_address;
        protected Button yes,no;


        public CustomViewHolder(View view) {
            super(view);
            this.item = (TextView) view.findViewById(R.id.item);
            this.u_number = (TextView) view.findViewById(R.id.u_number);
            this.u_address = (TextView) view.findViewById(R.id.u_address);
            this.yes = (Button) view.findViewById(R.id.yes);
            this.no = (Button) view.findViewById(R.id.no);
        }
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.owner_order_y_n_info, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, final int position) {
        viewholder.u_address.setText(mList.get(position).getMember_u_address());
        viewholder.item.setText(mList.get(position).getMember_item());
        viewholder.u_number.setText(mList.get(position).getMember_u_number());
        viewholder.yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){
                                Toast.makeText(context.getApplicationContext(),"주문받기 성공!",Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(context.getApplicationContext(),owner_order_y_n.class);

                                intent.putExtra("u_address",mList.get(position).getMember_u_address());
                                intent.putExtra("item",mList.get(position).getMember_item());
                                intent.putExtra("u_number",mList.get(position).getMember_u_number());

                                intent.putExtra("owner_name",owner_name);
                                intent.putExtra("owner_address",owner_address);
                                intent.putExtra("owner_lat",owner_lat);
                                intent.putExtra("owner_long",owner_long);
                                intent.putExtra("store_name",store_name);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                owner_order_yes_db registerRequest = new owner_order_yes_db(store_name, mList.get(position).getMember_u_address(), responseListener);
                RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
                queue.add(registerRequest);
            }
        });

        viewholder.no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){
                                Toast.makeText(context.getApplicationContext(),"주문거절 완료!",Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(context.getApplicationContext(),owner_order_y_n.class);

                                intent.putExtra("u_address",mList.get(position).getMember_u_address());
                                intent.putExtra("item",mList.get(position).getMember_item());
                                intent.putExtra("u_number",mList.get(position).getMember_u_number());

                                intent.putExtra("owner_name",owner_name);
                                intent.putExtra("owner_address",owner_address);
                                intent.putExtra("owner_lat",owner_lat);
                                intent.putExtra("owner_long",owner_long);
                                intent.putExtra("store_name",store_name);
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
                owner_order_no_db registerRequest = new owner_order_no_db(store_name, mList.get(position).getMember_u_address(), responseListener);
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
