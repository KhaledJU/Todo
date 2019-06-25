package com.example.firebase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
    }

    @Override
    public int getItemCount() {
        return mList_items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.CheckDone);
            imageView = itemView.findViewById(R.id.ButtonDelete);
        }
    }
}
