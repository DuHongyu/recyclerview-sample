package com.example.recyclerview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.recyclerview.adpter.imp.FunctionAdapter;
import com.example.recyclerview.adpter.imp.FunctionBlockAdapter;
import com.example.recyclerview.callback.DefaultItemCallback;
import com.example.recyclerview.callback.DefaultItemTouchHelper;
import com.example.recyclerview.decorate.SpaceItemDecoration;
import com.example.recyclerview.entity.FunctionItem;
import com.example.recyclerview.utils.DipUtils;
import com.example.recyclerview.utils.PositionControlUtils;
import com.example.recyclerview.utils.SfUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Du
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int MAX_COUNT = 10;

    private int itemWidth = 0;
    private final int lastRow = 0;
    private final int scrollPosition = 0;

    private String currentTab;

    private boolean isMove = false;
    private boolean isDrag = false;

    private List<FunctionItem> allData;
    private List<FunctionItem> selData;
    private final List<String> scrollTab = new ArrayList<>();

    private RecyclerView recyclerViewExist, recyclerViewAll;
    private HorizontalScrollView horizontalScrollView;
    private RadioGroup rg_tab;

    private FunctionBlockAdapter blockAdapter;
    private FunctionAdapter functionAdapter;
    private GridLayoutManager gridManager;

    private SfUtils sfUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        addListener();
    }

    public void init() {
        Log.d(TAG, "进入init方法：");

        getSupportActionBar().hide();

        recyclerViewExist = (RecyclerView) findViewById(R.id.recyclerViewExist);
        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizonLScrollView);
        rg_tab = (RadioGroup) findViewById(R.id.rg_tab);
        recyclerViewAll = (RecyclerView) findViewById(R.id.recyclerViewAll);

        sfUtils = new SfUtils(this);
        allData = sfUtils.getAllFunctionWithState();
        selData = sfUtils.getSelectFunctionItem();

        blockAdapter = new FunctionBlockAdapter(this, selData);
        recyclerViewExist.setLayoutManager(new GridLayoutManager(this, 5));
        recyclerViewExist.setAdapter(blockAdapter);
        recyclerViewExist.addItemDecoration(new SpaceItemDecoration(4, DipUtils.getDipUtils().dip2px(this,10)));

        DefaultItemCallback callback = new DefaultItemCallback(blockAdapter);
        DefaultItemTouchHelper helper = new DefaultItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerViewExist);

        gridManager = new GridLayoutManager(this, 5);
        gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                FunctionItem fi = allData.get(position);
                return fi.isTitle ? 5 : 1;
            }
        });

        functionAdapter = new FunctionAdapter(this, allData);
        recyclerViewAll.setLayoutManager(gridManager);
        recyclerViewAll.setAdapter(functionAdapter);
        SpaceItemDecoration spaceDecoration = new SpaceItemDecoration(4, DipUtils.getDipUtils().dip2px(this,10));
        recyclerViewAll.addItemDecoration(spaceDecoration);

        DefaultItemCallback callbackTwo = new DefaultItemCallback(functionAdapter);
        DefaultItemTouchHelper helperTwo = new DefaultItemTouchHelper(callbackTwo);
        helperTwo.attachToRecyclerView(recyclerViewAll);

        itemWidth = PositionControlUtils.getPositionControlUtils().getActivityWidth(this) / 4 + DipUtils.getDipUtils().dip2px(this,2);

        PositionControlUtils.getPositionControlUtils().resetEditHeight(recyclerViewExist,selData.size(),itemWidth,lastRow);

        initTab();

    }

    private void initTab() {

        Log.d(TAG, "进入initTab方法：");

        try {

            List<FunctionItem> tabs = sfUtils.getTabNames();

            Log.d(TAG, "tabs：" + tabs.size());

            if (tabs != null && tabs.size() > 0) {
                currentTab = tabs.get(0).name;
                int padding = DipUtils.getDipUtils().dip2px(this,10);
                int size = tabs.size();

                for (int i = 0; i < size; i++) {
                    FunctionItem item = tabs.get(i);
                    if (item.isTitle) {
                        scrollTab.add(item.name);
                        RadioButton rb = new RadioButton(this);
                        rb.setPadding(padding, 0, padding, 0);
                        rb.setButtonDrawable(null);
                        rb.setGravity(Gravity.CENTER);
                        rb.setText(item.name);

                        rb.setTag(item.subItemCount);

                        rb.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        try {

                            rb.setTextColor(getResources().getColorStateList(R.color.bg_block_text));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        rb.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.bg_block_tab));

                        rb.setOnCheckedChangeListener(onCheckedChangeListener);

                        rg_tab.addView(rb);

                    }
                }

                ((RadioButton) rg_tab.getChildAt(0)).setChecked(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            Log.d(TAG, "进入onCheckedChanged方法：");

            try {
                int position = (int) buttonView.getTag();
                String text = buttonView.getText().toString();
                if (!currentTab.equals(text) && isChecked) {
                    currentTab = text;
                    PositionControlUtils.getPositionControlUtils().moveToPosition(recyclerViewAll,gridManager,position,isMove,scrollPosition,allData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    public void addListener() {

        Log.d(TAG, "进入addListener方法：");

        //该处为保存按钮添加点击事件
        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "进入addListener/onClick方法：");

                //使用SF工具保存选择的模块
                sfUtils.saveSelectFunctionItem(selData);
                //保存所有选择状态
                sfUtils.saveAllFunctionWithState(allData);
            }
        });

        functionAdapter.setOnItemAddListener(new FunctionAdapter.OnItemAddListener() {
            @Override
            public boolean add(FunctionItem item) {

                Log.d(TAG, "进入addListener/setOnItemAddListener/add方法：");

                if (selData != null && selData.size() < MAX_COUNT) {
                    try {
                        selData.add(0, item);
                        Log.d(TAG, "selData的大小：" + selData.size());
                        PositionControlUtils.getPositionControlUtils().resetEditHeight(recyclerViewExist,selData.size(),itemWidth,lastRow);
                        blockAdapter.notifyDataSetChanged();
                        item.isSelect = true;
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                } else {
                    Toast.makeText(MainActivity.this, "选中的模块不能超过" + MAX_COUNT + "个", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        });

        blockAdapter.setOnItemRemoveListener(new FunctionBlockAdapter.OnItemRemoveListener() {
            @Override
            public void remove(FunctionItem item) {

                Log.d(TAG, "进入addListener/setOnItemRemoveListener/remove方法：");
                try {
                    if (item != null && item.name != null) {
                        for (int i = 0; i < allData.size(); i++) {
                            FunctionItem data = allData.get(i);
                            if (data != null && data.name != null) {
                                if (item.name.equals(data.name)) {
                                    data.isSelect = false;
                                    break;
                                }
                            }
                        }
                        functionAdapter.notifyDataSetChanged();
                    }
                    PositionControlUtils.getPositionControlUtils().resetEditHeight(recyclerViewExist,selData.size(),itemWidth,lastRow);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        recyclerViewAll.addOnScrollListener(onScrollListener);
    }


    private final RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

            Log.d(TAG, "onScrollListener/onScrollStateChanged方法：");

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

            Log.d(TAG, "onScrollListener/onScrolled方法：");

            super.onScrolled(recyclerView, dx, dy);
            if (isDrag) {
                int position = gridManager.findFirstVisibleItemPosition();
                if (position > 0) {
                    for (int i = 0; i < position + 1; i++) {
                        if (allData.get(i).isTitle) {
                            currentTab = allData.get(i).name;
                        }
                    }
                    int tabWidth = 0;
                    PositionControlUtils.getPositionControlUtils().scrollTab(scrollTab,currentTab, tabWidth,rg_tab,horizontalScrollView,onCheckedChangeListener);
                }
            }
        }
    };
}