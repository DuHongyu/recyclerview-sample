package com.example.recyclerview.adpter;


import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Administrator
 */
public interface ItemTouchHelperAdapter {

    /**
     * onItemMove.
     *
     * @param holder          - holder
     * @param fromPosition    - fromPosition
     * @param targetPosition  - targetPosition
     * @author Du
     * @version 1.0
     */
    void onItemMove(RecyclerView.ViewHolder holder, int fromPosition, int targetPosition);

    /**
     * onItemSelect
     *
     * @param holder  - holder
     * @author Du
     * @version 1.0
     */
    void onItemSelect(RecyclerView.ViewHolder holder);

    /**
     * onItemClear
     *
     * @param holder  - holder
     * @author Du
     * @version 1.0
     */
    void onItemClear(RecyclerView.ViewHolder holder);

    /**
     * onItemDismiss
     *
     * @param holder  - holder
     * @author Du
     * @version 1.0
     */
    void onItemDismiss(RecyclerView.ViewHolder holder);

    int getItemViewType(int position);

}
