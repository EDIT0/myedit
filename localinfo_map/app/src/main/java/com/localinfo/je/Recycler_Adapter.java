package com.localinfo.je;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class Recycler_Adapter extends RecyclerView.Adapter<Recycler_Adapter.ViewHolder> {

    ArrayList<notice_items.items> list_array = null;
    private Activity context = null;

    public Recycler_Adapter(Activity context, ArrayList<notice_items.items> list) {
        this.context = context;
        this.list_array = list;

    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private RelativeLayout relativeLayout;
        private TextView title, date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            relativeLayout = itemView.findViewById(R.id.relativelayout);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.activity_recycler_adapter, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.title.setText(list_array.get(position).getTitle());
        holder.date.setText(list_array.get(position).getDate());

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, notice_content.class);
                intent.putExtra("title",list_array.get(position).title);
                intent.putExtra("content",list_array.get(position).content);
                intent.putExtra("date",list_array.get(position).date);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list_array.size();
    }

}