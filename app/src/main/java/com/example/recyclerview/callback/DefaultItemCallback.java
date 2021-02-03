package com.example.recyclerview.callback;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerview.adpter.ItemTouchHelperAdapter;


/**
 * @author Administrator
 */
public class DefaultItemCallback extends ItemTouchHelper.Callback {

    private static final String TAG = "DefaultItemCallback";

    private final ItemTouchHelperAdapter touchHelperAdapter;

    RecyclerView.ViewHolder viewHolderAll;

    public DefaultItemCallback(ItemTouchHelperAdapter touchHelperAdapter) {
        this.touchHelperAdapter = touchHelperAdapter;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        viewHolderAll = viewHolder;
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        return makeMovementFlags(dragFlags, 0);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        Log.d(TAG,"执行onMove方法");
        if(viewHolder.getItemViewType() == 0){
            Log.d(TAG,"执行title的onMove方法");
            return false;
        }
        touchHelperAdapter.onItemMove(viewHolder, viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            touchHelperAdapter.onItemSelect(viewHolder);
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (!recyclerView.isComputingLayout()) {
            touchHelperAdapter.onItemClear(viewHolder);
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public boolean isLongPressDragEnabled() {
        if(viewHolderAll.getItemViewType() == 0){
            Log.d(TAG,"执行isLongPressDragEnabled方法");
            return false;
        }
        Log.d(TAG,"未执行isLongPressDragEnabled方法");
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }
}
