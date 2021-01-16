package com.example.user;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class user_image2_db extends StringRequest {
    final static private String URL = "http://edit0.dothome.co.kr/user_image2_db.php";
    private Map<String, String> map;

    public user_image2_db(String store_name, Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);

        map = new HashMap<>();

        map.put("store_name",store_name);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
