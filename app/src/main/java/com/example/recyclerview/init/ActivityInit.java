package com.example.recyclerview.init;

import android.app.Activity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerview.R;
import com.example.recyclerview.adpter.imp.ItemAdapter;
import com.example.recyclerview.adpter.imp.ItemBlockAdapter;
import com.example.recyclerview.callback.DefaultItemCallback;
import com.example.recyclerview.callback.DefaultItemTouchHelper;
import com.example.recyclerview.entity.Item;
import com.example.recyclerview.listener.AddListeners;
import com.example.recyclerview.utils.HandleDataUtils;
import com.example.recyclerview.utils.PositionControlUtils;
import com.example.recyclerview.utils.SizeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
public class ActivityInit {

    private final int lastRow = 0;
    private int itemWidth = 0;
    private final Activity activity;
    GridLayoutManager gridManager;
    RecyclerView recyclerViewExist;
    ItemBlockAdapter itemBlockAdapter;
    ItemAdapter itemAdapter;
    RadioGroup rgTab;
    private String currentTab;
    private final List<String> scrollTab = new ArrayList<>();
    private final int scrollPosition = 0;
    private boolean isMove = false;
    private boolean isDrag = false;
    RecyclerView recyclerViewAll;
    HorizontalScrollView horizontalScrollView;
    List<Item> initAllData;

    public ActivityInit(Activity activity) {
        this.activity = activity;
        gridManager = new GridLayoutManager(activity, 5);
    }

    public void initRecyclerViewBlockAdapter(int recyclerViewId, List<Item> allData, List<Item> selData) {
        recyclerViewExist = activity.findViewById(recyclerViewId);
        gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                Item fi = allData.get(position);
                return fi.isTitle ? 5 : 1;
            }
        });

        itemBlockAdapter = new ItemBlockAdapter(activity, selData);
        recyclerViewExist.setLayoutManager(new GridLayoutManager(activity, 5));
        recyclerViewExist.setAdapter(itemBlockAdapter);
        DefaultItemCallback callback = new DefaultItemCallback(itemBlockAdapter);
        DefaultItemTouchHelper helper = new DefaultItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerViewExist);
    }

    public void initRecyclerViewItemAdapter(int recyclerViewId, List<Item> allData, List<Item> selData) {
        recyclerViewAll = activity.findViewById(recyclerViewId);
        itemAdapter = new ItemAdapter(activity, allData);
        recyclerViewAll.setLayoutManager(gridManager);
        recyclerViewAll.setAdapter(itemAdapter);
        DefaultItemCallback callback1 = new DefaultItemCallback(itemAdapter);
        DefaultItemTouchHelper helper1 = new DefaultItemTouchHelper(callback1);
        helper1.attachToRecyclerView(recyclerViewAll);
    }

    public void initView(int horizontalScrollViewId, int rgTabId, List<Item> allData, List<Item> selData) {
        horizontalScrollView = activity.findViewById(horizontalScrollViewId);
        rgTab = activity.findViewById(rgTabId);
        itemWidth = PositionControlUtils.getPositionControlUtils().getActivityWidth(activity) / 5;
        PositionControlUtils.getPositionControlUtils().resetEditHeight(recyclerViewExist, selData.size(), itemWidth, lastRow);
        AddListeners addListeners = new AddListeners(itemAdapter, itemBlockAdapter);
        addListeners.addListener(allData, selData, recyclerViewExist, itemWidth, lastRow);
        initAllData = allData;
    }

    public void btnViewExit(List<Item> allData,List<Item> selectData) {
        for (int i = 0; i < allData.size(); i++) {
            allData.get(i).isDisplay = false;
            allData.get(i).isScale = false;
            itemAdapter.notifyDataSetChanged();
        }
        for (int i = 0; i < selectData.size(); i++) {
            selectData.get(i).isDisplay = false;
            selectData.get(i).isScale = false;
            itemBlockAdapter.notifyDataSetChanged();
        }
    }

    public void initTab(HandleDataUtils handleDataUtils) {
        try {
            List<Item> tabs = handleDataUtils.getTabNames();
            if (tabs != null && tabs.size() > 0) {
                currentTab = tabs.get(0).name;
                int padding = SizeUtils.getInstance().dip2px(activity, 10);
                int size = tabs.size();
                for (int i = 0; i < size; i++) {
                    Item item = tabs.get(i);
                    if (item.isTitle == true) {
                        scrollTab.add(item.name);
                        RadioButton rb = new RadioButton(activity);
                        rb.setPadding(padding, 0, padding, 0);
                        rb.setButtonDrawable(null);
                        rb.setGravity(Gravity.CENTER);
                        rb.setText(item.name);
                        rb.setTag(item.subItemCount);
                        rb.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        try {

                            rb.setTextColor(activity.getResources().getColorStateList(R.color.bg_block_text));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        rb.setCompoundDrawablesWithIntrinsicBounds(null, null, null, activity.getResources().getDrawable(R.drawable.bg_block_tab));

                        rb.setOnCheckedChangeListener(onCheckedChangeListener);

                        rgTab.addView(rb);

                    }
                }

                ((RadioButton) rgTab.getChildAt(0)).setChecked(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        private final CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    int position = (int) buttonView.getTag();
                    String text = buttonView.getText().toString();
                    if (!currentTab.equals(text) && isChecked) {
                        currentTab = text;
                        PositionControlUtils.getPositionControlUtils().moveToPosition(recyclerViewAll, gridManager, position, isMove, scrollPosition, initAllData);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };

        private final RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                try {

                    if (isMove && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        isMove = false;

                        View view = gridManager.findViewByPosition(scrollPosition);
                        if (view != null) {

                            int top = (int) view.getTop();
                            recyclerView.scrollBy(0, top);
                        }
                    }

                    isDrag = newState == RecyclerView.SCROLL_STATE_DRAGGING;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

                super.onScrolled(recyclerView, dx, dy);
                if (isDrag) {
                    int position = gridManager.findFirstVisibleItemPosition();
                    if (position > 0) {
                        for (int i = 0; i < position + 1; i++) {
                            if (initAllData.get(i).isTitle) {
                                currentTab = initAllData.get(i).name;
                            }
                        }
                        int tabWidth = 0;
                        PositionControlUtils.getPositionControlUtils().scrollTab(scrollTab, currentTab, tabWidth, rgTab, horizontalScrollView, onCheckedChangeListener);
                    }
                }
            }
        };
}
