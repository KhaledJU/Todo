package com.example.firebase;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class List_Adapter extends RecyclerView.Adapter<List_Adapter.ViewHolder> {

    private List<List_item> mList_items;
    private Context context;

    public List_Adapter(List<List_item> mList_items, Context context) {
        this.mList_items = mList_items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        List_item list_item = mList_items.get(position);
        holder.checkBox.setText(list_item.getTask());
        holder.checkBox.setChecked(list_item.isDone());
        holder.textView.setText(list_item.getDate());

        String format="dd-mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        String now = simpleDateFormat.format(new Date());
        String taskDate = list_item.getDate();

        try {
            Date dateNow = simpleDateFormat.parse(now);
            Date dateTaskDate= simpleDateFormat.parse(taskDate);
            if (dateNow.compareTo(dateTaskDate) > 0) {
                holder.textView.setTextColor(Color.RED);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mList_items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;
        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.CheckDone);
            imageView = itemView.findViewById(R.id.ButtonDelete);
            textView = itemView.findViewById(R.id.TextDate);
        }
    }
}
