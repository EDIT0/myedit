package com.example.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class user_frag_first extends Fragment {
    TextView main_title;
    TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7;

    Button intoitemlist;

    String o_c_time;
    String closed_day;
    String o_name;
    String o_nin;
    String s_address;
    int t_order;
    TextView gongji;

    String user_name1, user_address1, user_id1, user_address_detail1;
    Double user_lat1, user_long1;
    String a;

    public user_frag_first() {
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_user_frag_first, container, false);

            Intent intent = getActivity().getIntent();
            user_name1 = intent.getStringExtra("user_name");
            user_address1 = intent.getStringExtra("user_address");
            user_lat1 = intent.getDoubleExtra("user_lat",0.0);
            user_long1 = intent.getDoubleExtra("user_long",0.0);
            user_id1 = intent.getStringExtra("user_id");
            user_address_detail1 = intent.getStringExtra("user_address_detail");
            final String re_set_title = intent.getStringExtra("title");

            tv1 = rootView.findViewById(R.id.o_c_time);
            tv2 = rootView.findViewById(R.id.closed_day);
            tv3 = rootView.findViewById(R.id.t_order);
            tv4 = rootView.findViewById(R.id.o_name);
            tv5 = rootView.findViewById(R.id.s_name);
            tv5.setText(re_set_title);
            tv6 = rootView.findViewById(R.id.s_address);
            tv7 = rootView.findViewById(R.id.o_nin);

            Response.Listener<String> responseListener1 = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if(success){
                            o_c_time = jsonObject.getString("o_c_time");
                            closed_day = jsonObject.getString("closed_day");
                            t_order = jsonObject.getInt("t_order");
                            o_name = jsonObject.getString("o_name");
                            s_address = jsonObject.getString("s_address");
                            o_nin = jsonObject.getString("o_nin");

                            tv1.setText(o_c_time);
                            tv2.setText(closed_day);
                            tv3.setText(String.valueOf(t_order));
                            tv4.setText(o_name);
                            tv6.setText(s_address);
                            tv7.setText(o_nin);

                        }
                        else{
                            Toast.makeText(getActivity(),"불러오기 실패",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            };
            user_image2_db registerRequest1 = new user_image2_db(re_set_title, responseListener1);
            RequestQueue queue1 = Volley.newRequestQueue(getActivity());
            queue1.add(registerRequest1);

            main_title = rootView.findViewById(R.id.title1);
            main_title.setText(re_set_title);

            intoitemlist = (Button)rootView.findViewById(R.id.intoitemlist);
            intoitemlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), user_itempage.class);
                    intent.putExtra("user_name",user_name1);
                    intent.putExtra("user_address",user_address1);
                    intent.putExtra("user_lat",user_lat1);
                    intent.putExtra("user_long",user_long1);
                    intent.putExtra("title",main_title.getText().toString());
                    intent.putExtra("user_id",user_id1);
                    intent.putExtra("user_address_detail",user_address_detail1);
                    startActivityForResult(intent,0);
                }
            });

            gongji = (TextView) rootView.findViewById(R.id.gongjiforuser);
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if(success){
                            String gongji1 = jsonObject.getString("content");
                            a = gongji1;
                            gongji.setText(a);
                        }
                        else{
                            Toast.makeText(getActivity(),"사장님공지가 아직 없습니다!",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            };
            user_image_db registerRequest = new user_image_db(re_set_title, responseListener);
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(registerRequest);

        return rootView;
    }


}