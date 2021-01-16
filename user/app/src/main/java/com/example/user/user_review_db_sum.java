package com.example.user;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class user_review_db_sum extends StringRequest {
    final static private String URL = "http://edit0.dothome.co.kr/user_review_db_sum.php";
    private Map<String, String> map;

    public user_review_db_sum(String user_id, Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);

        map = new HashMap<>();

        map.put("user_id",user_id);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
