package com.example.recyclerview.callback;


import androidx.recyclerview.widget.ItemTouchHelper;

/**
 * @author Du
 */
public class DefaultItemTouchHelper extends ItemTouchHelper {
    public DefaultItemTouchHelper(ItemTouchHelper.Callback callback) {
        super(callback);
    }
}
