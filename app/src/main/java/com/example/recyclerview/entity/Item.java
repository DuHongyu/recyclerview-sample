package com.example.recyclerview.entity;

/**
 * @author Du
 */
public class Item {

    public String name;
    public boolean isSelect = false;
    public String imageUrl = "";
    public String background = "";
    public boolean isTitle = false;
    public int subItemCount = 0;
    public boolean isDisplay = false;
    public boolean isScale = false;


    public Item(String name, boolean isSelect, String imageUrl, String background) {
        this.name = name;
        this.isSelect = isSelect;
        this.imageUrl = imageUrl;
        this.background = background;
    }

    public Item(String name, boolean isTitle, int subItemCount) {
        this.name = name;
        this.isTitle = isTitle;
        this.subItemCount = subItemCount;
    }

    public Item(String name, boolean isTitle,boolean isDisplay,boolean isScale) {
        this.name = name;
        this.isTitle = isTitle;
        this.isDisplay = isDisplay;
    }

}
