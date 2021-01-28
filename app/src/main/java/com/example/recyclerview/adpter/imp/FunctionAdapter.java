package com.example.recyclerview.adpter.imp;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerview.R;
import com.example.recyclerview.adpter.ItemTouchHelperAdapter;
import com.example.recyclerview.entity.FunctionItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @author Du
 */
public class FunctionAdapter extends RecyclerView.Adapter implements ItemTouchHelperAdapter {

    private List<FunctionItem> data = new ArrayList<>();

    private final LayoutInflater inflater;
    private final Context context;

    public FunctionAdapter(Context context, @NonNull List<FunctionItem> data) {
        this.context = context;
        if (data != null) {
            this.data = data;
        }
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if (0 == viewType) {
            holder = new TitleViewHolder(inflater.inflate(R.layout.layout_function_text, parent, false));
        } else {
            holder = new FunctionViewHolder(inflater.inflate(R.layout.layout_grid_item, parent, false));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (0 == getItemViewType(position)) {
            TitleViewHolder holder = (TitleViewHolder) viewHolder;
            holder.text.setText(data.get(position).name);
        } else {
            final int index = position;
            FunctionViewHolder holder = (FunctionViewHolder) viewHolder;
            FunctionItem fi = data.get(position);
            setImage(fi.imageUrl, holder.iv);
            holder.text.setText(fi.name);
            holder.btn.setImageResource(fi.isSelect ? R.drawable.ic_block_selected : R.drawable.ic_block_add);
            holder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FunctionItem f = data.get(index);
                    if (!f.isSelect) {
                        if (listener != null) {
                            if (listener.add(f)) {
                                f.isSelect = true;
                                notifyDataSetChanged();
                            }
                        }
                    }
                }
            });
        }
    }

    public void setImage(String url, ImageView iv) {
        try {
            int rid = context.getResources().getIdentifier(url, "drawable", context.getPackageName());
            iv.setImageResource(rid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).isTitle ? 0 : 1;
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

    private static class TitleViewHolder extends RecyclerView.ViewHolder {

        private final TextView text;

        public TitleViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text);
        }
    }

    private static class FunctionViewHolder extends RecyclerView.ViewHolder {

        private final ImageView iv;
        private final ImageView btn;
        private final TextView text;

        public FunctionViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.iv);
            text = (TextView) itemView.findViewById(R.id.text);
            btn = (ImageView) itemView.findViewById(R.id.btn);
        }
    }

    public interface OnItemAddListener {
        boolean add(FunctionItem item);
    }

    private OnItemAddListener listener;

    public void setOnItemAddListener(OnItemAddListener listener) {
        this.listener = listener;
    }

}
