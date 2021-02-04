package com.example.recyclerview.listener;

import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerview.adpter.imp.ItemAdapter;
import com.example.recyclerview.adpter.imp.ItemBlockAdapter;
import com.example.recyclerview.entity.Item;
import com.example.recyclerview.utils.PositionControlUtils;

import java.util.List;

/**
 * @author Administrator
 */
public class AddListeners {
    private String currentTab;


    private static final int MAX_COUNT = 10;
    ItemAdapter itemAdapter;
    ItemBlockAdapter itemBlockAdapter;

    public AddListeners(ItemAdapter itemAdapter, ItemBlockAdapter itemBlockAdapter) {
        this.itemAdapter = itemAdapter;
        this.itemBlockAdapter = itemBlockAdapter;
    }

    public void addListener(List<Item> allData, List<Item> selData, RecyclerView recyclerViewExist, int itemWidth, int lastRow) {
        itemAdapter.setOnItemAddListener(new ItemAdapter.OnItemAddListener() {
            @Override
            public boolean add(Item item) {

                if (selData != null && selData.size() < MAX_COUNT) {
                    try {
                        selData.add(0, item);
                        PositionControlUtils.getPositionControlUtils().resetEditHeight(recyclerViewExist, selData.size(), itemWidth, lastRow);
                        itemBlockAdapter.notifyDataSetChanged();
                        item.isSelect = true;
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                } else {
                    for (int i = 0; i < allData.size(); i++) {
                        assert selData != null;
                        if (allData.get(i).name.equals(selData.get(selData.size() - 1).name)) {
                            allData.get(i).isSelect = false;
                        }
                    }
                    assert selData != null;
                    selData.remove(selData.size() - 1);

                    selData.add(0, item);
                    itemBlockAdapter.notifyDataSetChanged();
                    item.isSelect = true;
                    return true;
                }
            }
        });

        itemBlockAdapter.setOnItemRemoveListener(new ItemBlockAdapter.OnItemRemoveListener() {
            @Override
            public void remove(Item item) {
                try {
                    PositionControlUtils.getPositionControlUtils().resetEditHeight(recyclerViewExist, selData.size(), itemWidth, lastRow);
                    if (item != null && item.name != null) {
                        for (int i = 0; i < allData.size(); i++) {
                            Item data = allData.get(i);
                            if (data != null && data.name != null) {
                                if (item.name.equals(data.name)) {
                                    data.isSelect = false;
                                    itemAdapter.notifyDataSetChanged();
                                    break;
                                }
                            }
                        }
                        itemAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
