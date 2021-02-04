package com.example.recyclerview.adpter.imp;

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
        Item item = data.get(position);
        ImageView imageView = setImage(item.imageUrl, holder.iv);
        final Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        holder.itemView.setOnLongClickListener(v -> {
            vibrator.vibrate(30);
            holder.btn.clearAnimation();
            imageView.clearAnimation();
            for(int i = 0;i<data.size();i++){
                data.get(i).isDisplay = true;
                data.get(i).isScale = true;
                notifyDataSetChanged();
            }
            return true;
        });
        holder.text.setText(item.name);
        holder.btn.setImageResource(R.drawable.ic_block_delete);
        holder.btn.setVisibility(View.INVISIBLE);
        if(item.isDisplay){
            holder.btn.setVisibility(View.VISIBLE);
            holder.itemView.setScaleX(0.9f);
            holder.itemView.setScaleY(0.9f);
        }else {
            holder.btn.setVisibility(View.INVISIBLE);
            holder.itemView.setScaleX(1.0f);
            holder.itemView.setScaleY(1.0f);
        }
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item item = data.remove(index);
                if (listener != null) {
                    listener.remove(item);
                }
                notifyDataSetChanged();
            }
        });
    }

    public ImageView setImage(String url, ImageView iv) {
        try {
            int rid = context.getResources().getIdentifier(url, "drawable", context.getPackageName());
            iv.setImageResource(rid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iv;
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


    /**
     * OnItemRemoveListener
     *
     * @author Du
     * @version 1.0
     */
    public interface OnItemRemoveListener {
        void remove(Item item);
    }

    private OnItemRemoveListener listener;

    public void setOnItemRemoveListener(OnItemRemoveListener listener) {
        this.listener = listener;
    }
}
