package com.example.recyclerview.entity;

import java.util.ArrayList;

/**
 * @author Du
 */
public class TabItem {
    private String tabName = "";
    private ArrayList<Item> items;

    public TabItem(String tabName, ArrayList<Item> items) {
        this.tabName = tabName;
        this.items = items;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }
}
