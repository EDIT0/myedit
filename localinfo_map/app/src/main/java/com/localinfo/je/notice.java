package com.localinfo.je;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import net.daum.mf.map.api.MapPoint;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class notice extends Fragment {
    private final String TAG = "notice";

    ViewGroup viewGroup;

    public static ArrayList<notice_items.items> notice_list_array = new ArrayList<>();

    RecyclerView recyclerView;
    public Recycler_Adapter recycler_adapter;

    Button user_guide;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_notice, container, false);

        recyclerView = viewGroup.findViewById(R.id.recyclerview);

        recycler_adapter = new Recycler_Adapter(getActivity(), notice_list_array);

        RecyclerDecoration spaceDecoration = new RecyclerDecoration(10);
        recyclerView.addItemDecoration(spaceDecoration);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(recycler_adapter);

        recycler_adapter.notifyDataSetChanged();

        user_guide = viewGroup.findViewById(R.id.user_guide);
        user_guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), User_guide.class);
                startActivity(intent);
            }
        });


        return viewGroup;
    }


    public class RecyclerDecoration extends RecyclerView.ItemDecoration {
        private final int divHeight;

        public RecyclerDecoration(int divHeight) {
            this.divHeight = divHeight;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1)

                outRect.bottom = divHeight;

        }
    }

}