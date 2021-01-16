package com.example.user;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class user_order_record2_db2 extends StringRequest {
    final static private String URL = "http://edit0.dothome.co.kr/user_order_record2_db2.php";
    private Map<String, String> map;

    public user_order_record2_db2(String s_name, String user_id, int t_price, int date, String memo, Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);

        map = new HashMap<>();

        map.put("s_name",s_name);
        map.put("user_id",user_id);
        map.put("t_price",t_price+"");
        map.put("date",date+"");
        map.put("memo",memo);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
