package com.example.recyclerview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.didichuxing.doraemonkit.DoraemonKit;
import com.example.recyclerview.adpter.imp.ItemAdapter;
import com.example.recyclerview.adpter.imp.ItemBlockAdapter;
import com.example.recyclerview.callback.DefaultItemCallback;
import com.example.recyclerview.callback.DefaultItemTouchHelper;
import com.example.recyclerview.widgets.SpaceItemDecoration;
import com.example.recyclerview.entity.Item;
import com.example.recyclerview.utils.SizeUtils;
import com.example.recyclerview.utils.PositionControlUtils;
import com.example.recyclerview.utils.HandleDataUtils;

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
    private List<Item> allData;
    private List<Item> selData;

    private final List<String> scrollTab = new ArrayList<>();
    private RecyclerView recyclerViewExist, recyclerViewAll;
    private HorizontalScrollView horizontalScrollView;
    private RadioGroup rgTab;
    private ItemBlockAdapter blockAdapter;
    private ItemAdapter itemAdapter;
    private GridLayoutManager gridManager;
    private HandleDataUtils handleDataUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        addListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handleDataUtils.saveSelectFunctionItem(selData);
        handleDataUtils.saveAllFunctionWithState(allData);
    }

    public void init() {
        getSupportActionBar().hide();
        recyclerViewExist = (RecyclerView) findViewById(R.id.recyclerViewExist);
        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizonLScrollView);
        rgTab = (RadioGroup) findViewById(R.id.rg_tab);
        recyclerViewAll = (RecyclerView) findViewById(R.id.recyclerViewAll);
        handleDataUtils = new HandleDataUtils(this);
        allData = handleDataUtils.getAllFunctionWithState();
        selData = handleDataUtils.getSelectFunctionItem();
        blockAdapter = new ItemBlockAdapter(this, selData);
        recyclerViewExist.setLayoutManager(new GridLayoutManager(this, 5));
        recyclerViewExist.setAdapter(blockAdapter);
        recyclerViewExist.addItemDecoration(new SpaceItemDecoration(4, SizeUtils.getInstance().dip2px(this, 10)));
        DefaultItemCallback callback = new DefaultItemCallback(blockAdapter);
        DefaultItemTouchHelper helper = new DefaultItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerViewExist);

        gridManager = new GridLayoutManager(this, 5);
        gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                Item fi = allData.get(position);
                return fi.isTitle ? 5 : 1;
            }
        });

        itemAdapter = new ItemAdapter(this, allData);
        recyclerViewAll.setLayoutManager(gridManager);
        recyclerViewAll.setAdapter(itemAdapter);
        SpaceItemDecoration spaceDecoration = new SpaceItemDecoration(4, SizeUtils.getInstance().dip2px(this, 10));
        recyclerViewAll.addItemDecoration(spaceDecoration);

/*        recyclerViewAll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "进入setOnTouchListener方法"+v.getId());
                if (v.getId()!=0){
                    //发现只有点击了空白处，v.getId，才能打印出东西
                    Log.d(TAG, "进入setOnTouchListener方法");
                    //可以在此处做文章
                    for(int i = 0;i<allData.size();i++){
                        allData.get(i).isDisplay = false;
                        allData.get(i).isScale = false;
                        itemAdapter.notifyDataSetChanged();
                    }
                }
                return false;
            }
        });*/

        DefaultItemCallback callback1 = new DefaultItemCallback(itemAdapter);
/*        for(int i = 0;i<allData.size();i++){
            if(allData.get(i).isTitle){
                callback1.change();
            }
        }*/
        DefaultItemTouchHelper helper1 = new DefaultItemTouchHelper(callback1);
        helper1.attachToRecyclerView(recyclerViewAll);

        itemWidth = PositionControlUtils.getPositionControlUtils().getActivityWidth(this) / 4 + SizeUtils.getInstance().dip2px(this, 2);
        PositionControlUtils.getPositionControlUtils().resetEditHeight(recyclerViewExist, selData.size(), itemWidth, lastRow);
        initTab();
    }

    private void initTab() {
        try {
            Log.d(TAG, "进入initTab方法");
            List<Item> tabs = handleDataUtils.getTabNames();
            Log.d(TAG, "tabs:" + tabs);
            if (tabs != null && tabs.size() > 0) {
                Log.d(TAG, "tabs:" + tabs);
                currentTab = tabs.get(0).name;
                int padding = SizeUtils.getInstance().dip2px(this, 10);
                int size = tabs.size();
                for (int i = 0; i < size; i++) {
                    Item item = tabs.get(i);
                    if (item.isTitle) {
                        Log.d(TAG, "RADIOBUTTON方法");
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
                    PositionControlUtils.getPositionControlUtils().moveToPosition(recyclerViewAll, gridManager, position, isMove, scrollPosition, allData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    public void addListener() {

        itemAdapter.setOnItemAddListener(new ItemAdapter.OnItemAddListener() {
            @Override
            public boolean add(Item item) {

                if (selData != null && selData.size() < MAX_COUNT) {
                    try {
                        selData.add(0, item);
                        Log.d(TAG, "selData的大小：" + selData.size());
                        PositionControlUtils.getPositionControlUtils().resetEditHeight(recyclerViewExist, selData.size(), itemWidth, lastRow);
                        blockAdapter.notifyDataSetChanged();
                        item.isSelect = true;
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                } else {
                    for (int i = 0; i < allData.size(); i++) {
                        Log.d(TAG, "进入需要的for循环：");
                        assert selData != null;
                        if (allData.get(i).name.equals(selData.get(selData.size() - 1).name)) {
                            allData.get(i).isSelect = false;
                        }
                    }
                    assert selData != null;
                    selData.remove(selData.size() - 1);

                    selData.add(0, item);
                    blockAdapter.notifyDataSetChanged();
                    item.isSelect = true;
                    return true;
                }
            }
        });

        blockAdapter.setOnItemRemoveListener(new ItemBlockAdapter.OnItemRemoveListener() {
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

        itemAdapter.setOnLongItemClickListener(new ItemAdapter.OnLongItemClickListener() {
            @Override
            public boolean onLongItemClick(List<Item> itemList) {
                try {
                    Log.d(TAG, "进入长按更改isDisplay循环：");
/*                    for (int i = 0; i < allData.size(); i++) {
                        Item data = allData.get(i);
                        Log.d(TAG, "执行更改isDisplay参数值：");
                        data.isDisplay = true;
                        Log.d(TAG, "allData.isDisplay参数值：" + allData.get(i).isDisplay);
                        itemAdapter.notifyDataSetChanged();
                        break;
                    }*/

                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        recyclerViewAll.addOnScrollListener(onScrollListener);
    }


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
                        if (allData.get(i).isTitle) {
                            currentTab = allData.get(i).name;
                        }
                    }
                    int tabWidth = 0;
                    PositionControlUtils.getPositionControlUtils().scrollTab(scrollTab, currentTab, tabWidth, rgTab, horizontalScrollView, onCheckedChangeListener);
                }
            }
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        closeKeyBoard();
        return super.onTouchEvent(event);
    }

    public void closeKeyBoard() {
        Log.d(TAG, "进入失去焦点方法：");
        if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
            if(this.getCurrentFocus() != null){
                for(int i = 0;i<allData.size();i++){
                    allData.get(i).isDisplay = false;
                    allData.get(i).isScale = false;
                    itemAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}