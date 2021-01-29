package com.example.recyclerview.adpter.imp;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerview.R;
import com.example.recyclerview.adpter.ItemTouchHelperAdapter;
import com.example.recyclerview.entity.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @author Du
 */
public class ItemBlockAdapter extends RecyclerView.Adapter<ItemBlockAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private List<Item> data = new ArrayList<>();
    private final LayoutInflater inflater;


    private final Context context;

    public ItemBlockAdapter(Context context, @NonNull List<Item> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        if (data != null) {
            this.data = data;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.layout_grid_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int index = position;
        Item fi = data.get(position);
        setImage(fi.imageUrl, holder.iv);
        holder.text.setText(fi.name);
        holder.btn.setImageResource(R.drawable.ic_block_delete);
        holder.btn.setVisibility(View.INVISIBLE);
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item fi = data.remove(index);
                if (listener != null) {
                    listener.remove(fi);
                }
                notifyDataSetChanged();
            }
        });
    }

    public void setImage(String url, ImageView iv) {
        try {
            int rid = context.getResources().getIdentifier(url,"drawable",context.getPackageName());
            iv.setImageResource(rid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onItemMove(RecyclerView.ViewHolder holder, int fromPosition, int targetPosition) {
        if (fromPosition < data.size() && targetPosition < data.size()) {
            Collections.swap(data, fromPosition, targetPosition);
            notifyItemMoved(fromPosition, targetPosition);
        }
    }

    @Override
    public void onItemSelect(RecyclerView.ViewHolder holder) {
        holder.itemView.setScaleX(0.8f);
        holder.itemView.setScaleY(0.8f);
    }

    @Override
    public void onItemClear(RecyclerView.ViewHolder holder) {
        holder.itemView.setScaleX(1.0f);
        holder.itemView.setScaleY(1.0f);
    }

    @Override
    public void onItemDismiss(RecyclerView.ViewHolder holder) {

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView iv;
        private final ImageView btn;
        private final TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.iv);
            text = (TextView) itemView.findViewById(R.id.text);
            btn = (ImageView) itemView.findViewById(R.id.btn);
        }
    }

    public interface OnItemRemoveListener {
        void remove(Item item);
    }

    private OnItemRemoveListener listener;

    public void setOnItemRemoveListener(OnItemRemoveListener listener) {
        this.listener = listener;
    }
}