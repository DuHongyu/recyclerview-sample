package com.example.recyclerview.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.recyclerview.entity.Item;
import com.example.recyclerview.entity.TabItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Du
 */
public class HandleDataUtils {

    private static final String TAG = "HandleDataUtils";

    private final Context context;
    private SharedPreferences sp = null;

    public HandleDataUtils(Context context) {
        this.context = context;
        sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
    }

    public List<Item> getAllFunctionWithState() {
        String allData = sp.getString("allData", "");
        List<Item> items = new ArrayList<>();
        List<TabItem> tabItems = null;
        if ("".equals(allData)) {
            try {
                InputStream is = context.getAssets().open("data.txt");
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String str = null;
                StringBuilder suffer = new StringBuilder();
                while ((str = br.readLine()) != null) {
                    suffer.append(str);
                }
                br.close();
                isr.close();
                is.close();
                allData = suffer.toString();
                Gson gson = new Gson();
                tabItems = gson.fromJson(allData, new TypeToken<List<TabItem>>() {
                }.getType());
                if (tabItems != null) {
                    for (int i = 0; i < tabItems.size(); i++) {
                        Item item = new Item(tabItems.get(i).getTabName(), true, false,false);
                        items.add(item);
                        items.addAll(tabItems.get(i).getItems());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Gson gson = new Gson();
            items = gson.fromJson(allData, new TypeToken<List<Item>>() {
            }.getType());
        }
        return items;
    }

    public List<Item> getSelectFunctionItem() {
        String selData = sp.getString("selData", "");
        Log.d(TAG, "selData:" + selData);
        List<Item> items = null;
        if ("".equals(selData)) {
            items = new ArrayList<>();
        } else {
            Gson gson = new Gson();
            items = gson.fromJson(selData, new TypeToken<List<Item>>() {
            }.getType());
        }
        return items;
    }

    public List<Item> getTabNames() {
        Log.d(TAG, "进入getTabNames");
        String allData = sp.getString("allData", "");

        List<Item> items = new ArrayList<>();
        List<TabItem> tabItems = null;
        if (allData != null) {
            try {
                InputStream is = context.getAssets().open("data.txt");

                Log.d(TAG, "InputStream:" + is);
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String str = null;
                StringBuilder stringBuffer = new StringBuilder();
                while ((str = br.readLine()) != null) {
                    stringBuffer.append(str);
                }
                br.close();
                isr.close();
                is.close();
                allData = stringBuffer.toString();
                Gson gson = new Gson();
                tabItems = gson.fromJson(allData, new TypeToken<List<TabItem>>() {
                }.getType());
                if (tabItems != null) {
                    int tabItemsCount = 0;
                    for (int i = 0; i < tabItems.size(); i++) {
                        Item item = new Item(tabItems.get(i).getTabName(), true, tabItemsCount);
                        tabItemsCount = tabItemsCount + tabItems.get(i).getItems().size() + 1;
                        items.add(item);
                        Log.d(TAG, "functionItems:" + items.size());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "allData:" + allData);
        Log.d(TAG, "functionItems:" + items.size());
        return items;
    }

    public void saveSelectFunctionItem(List<Item> selData) {
        Gson gson = new Gson();
        sp.edit().putString("selData", gson.toJson(selData)).apply();
    }

    public void saveAllFunctionWithState(List<Item> allData) {
        Gson gson = new Gson();
        sp.edit().putString("allData", gson.toJson(allData)).apply();
    }

}
