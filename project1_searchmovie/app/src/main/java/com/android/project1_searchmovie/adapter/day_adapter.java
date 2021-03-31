package com.android.project1_searchmovie.adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.project1_searchmovie.R;
import com.android.project1_searchmovie.data.data_list;
import com.android.project1_searchmovie.ui.movie_info;
import com.android.project1_searchmovie.db.myDBHelper;

import java.util.ArrayList;

public class day_adapter extends RecyclerView.Adapter<day_adapter.ViewHolder> {

    ArrayList<data_list> list = null;
    private Activity context = null;

    public day_adapter(Activity context, ArrayList<data_list> list) {
        this.context = context;
        this.list = list;

    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private final LinearLayout linearlayout1;
        private final TextView tv1, tv2, tv3;
        private final Button button_get;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            linearlayout1 = itemView.findViewById(R.id.linearlayout1);
            tv1 = itemView.findViewById(R.id.textview1);
            tv2 = itemView.findViewById(R.id.textview2);
            tv3 = itemView.findViewById(R.id.textview3);
            button_get = itemView.findViewById(R.id.button_get);
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.activity_day_adapter, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.tv1.setText(list.get(position).get_movieNm());
        holder.tv2.setText("개봉일: "+list.get(position).get_openDt());
        holder.tv3.setText("누적 관객수: " + list.get(position).get_audiAcc());
        //holder.tv1.setText("list.get(position).get_movieNm()");

        holder.linearlayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=1&ie=utf8&query="+holder.tv1.getText().toString()));
                context.startActivity(intent);*/

                //Toast.makeText(context, ""+getItemCount()+"번째: "+holder.tv1.getText().toString() + " / " + holder.tv2.getText().toString(), Toast.LENGTH_SHORT).show();

                String movie_info_url = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieInfo.json?key=c4e62bb6b5adb5b5194d59ab8c9ca518&movieCd=" + list.get(position).get_movieCd();

                Intent intent = new Intent(context, movie_info.class);
                intent.putExtra("movie_info_url",movie_info_url);
                context.startActivity(intent);
            }
        });

        holder.button_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDBHelper myHelper;
                myHelper = new myDBHelper(context);

                SQLiteDatabase sqlDB;

                sqlDB = myHelper.getWritableDatabase();
                try {
                    sqlDB.execSQL("INSERT INTO my_pocket VALUES ( '" + holder.tv1.getText().toString() + "', '" + holder.tv2.getText().toString() + "');");

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("알림");
                    builder.setMessage("담기 성공!");
                    builder.setIcon(R.drawable.ic_launcher_foreground);

                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    builder.show();
                }catch (Exception e){

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("알림");
                    builder.setMessage("이미 가지고 있습니다!");
                    builder.setIcon(R.drawable.ic_launcher_foreground);

                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    builder.show();
                }
                sqlDB.close();
                //Toast.makeText(context, "입력됨", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}