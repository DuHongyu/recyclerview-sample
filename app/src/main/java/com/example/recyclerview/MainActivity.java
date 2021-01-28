package com.example.recyclerview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
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

import com.example.recyclerview.adpter.FunctionAdapter;
import com.example.recyclerview.adpter.FunctionBlockAdapter;
import com.example.recyclerview.callback.DefaultItemCallback;
import com.example.recyclerview.callback.DefaultItemTouchHelper;
import com.example.recyclerview.decorate.SpaceItemDecoration;
import com.example.recyclerview.entity.FunctionItem;
import com.example.recyclerview.utils.SFUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Du
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int MAX_COUNT = 10;

    private int itemWidth = 0;
    private int lastRow = 0;
    private int scrollPosition = 0;

    private int tabWidth = 0;

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

    private SFUtils sfUtils;

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

        sfUtils = new SFUtils(this);
        allData = sfUtils.getAllFunctionWithState();
        selData = sfUtils.getSelectFunctionItem();

        blockAdapter = new FunctionBlockAdapter(this, selData);
        recyclerViewExist.setLayoutManager(new GridLayoutManager(this, 5));
        recyclerViewExist.setAdapter(blockAdapter);
        recyclerViewExist.addItemDecoration(new SpaceItemDecoration(4, dip2px(this, 10)));

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
        SpaceItemDecoration spaceDecoration = new SpaceItemDecoration(4, dip2px(this, 10));
        recyclerViewAll.addItemDecoration(spaceDecoration);

        DefaultItemCallback callbackTwo = new DefaultItemCallback(functionAdapter);
        DefaultItemTouchHelper helperTwo = new DefaultItemTouchHelper(callbackTwo);
        helperTwo.attachToRecyclerView(recyclerViewAll);

        itemWidth = getAtyWidth(this) / 4 + dip2px(this, 2);

        resetEditHeight(selData.size());

        initTab();

    }

    public int dip2px(Context context, float dpValue) {

        Log.d(TAG, "进入dip2px方法：");

        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public int getAtyWidth(Context context) {

        Log.d(TAG, "进入getAtyWidth方法：");

        try {

            DisplayMetrics mDm = new DisplayMetrics();

            ((Activity) context).getWindowManager().getDefaultDisplay()
                    .getMetrics(mDm);

            return mDm.widthPixels;
        } catch (Exception e) {
            return 0;
        }
    }

    private void resetEditHeight(int size) {

        Log.d(TAG, "进入resetEditHeight方法：");

        try {
            if (size == 0) {
                size = 1;
            }
            int row = size / 5 + (size % 5 > 0 ? 1 : 0);
            if (row <= 0) {
                row = 1;
            }
            if (lastRow != row) {
                lastRow = row;

                ViewGroup.LayoutParams params = recyclerViewExist.getLayoutParams();

                params.height = itemWidth * row;

                recyclerViewExist.setLayoutParams(params);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initTab() {

        Log.d(TAG, "进入initTab方法：");

        try {

            List<FunctionItem> tabs = sfUtils.getTabNames();

            Log.d(TAG, "tabs：" + tabs.size());

            if (tabs != null && tabs.size() > 0) {
                currentTab = tabs.get(0).name;
                int padding = dip2px(this, 10);
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
                    moveToPosition(position);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    private void moveToPosition(int position) {

        Log.d(TAG, "进入moveToPosition方法：");

        int first = gridManager.findFirstVisibleItemPosition();
        int end = gridManager.findLastVisibleItemPosition();
        if (first == -1 || end == -1) {
            return;
        }

        if (position <= first) {
            gridManager.scrollToPosition(position);
        } else if (position >= end) {

            isMove = true;
            scrollPosition = position;
            gridManager.smoothScrollToPosition(recyclerViewAll, null, position);
        } else {

            int n = position - gridManager.findFirstVisibleItemPosition();
            if (n > 0 && n < allData.size()) {
                int top = gridManager.findViewByPosition(position).getTop();
                recyclerViewAll.scrollBy(0, top);
            }
        }
    }

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
                        resetEditHeight(selData.size());
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
                    resetEditHeight(selData.size());
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
                    scrollTab(currentTab);
                }
            }
        }
    };

    private void scrollTab(String newTab) {

        Log.d(TAG, "scrollTab方法：");

        try {
            int position = scrollTab.indexOf(currentTab);
            int targetPosition = scrollTab.indexOf(newTab);
            currentTab = newTab;
            if (targetPosition != -1) {
                int x = (targetPosition - position) * getTabWidth();
                RadioButton radioButton = ((RadioButton) rg_tab.getChildAt(targetPosition));
                radioButton.setOnCheckedChangeListener(null);
                radioButton.setChecked(true);
                radioButton.setOnCheckedChangeListener(onCheckedChangeListener);
                horizontalScrollView.scrollBy(x, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getTabWidth() {

        Log.d(TAG, "getTabWidth方法：");

        if (tabWidth == 0) {
            if (rg_tab != null && rg_tab.getChildCount() != 0) {
                tabWidth = rg_tab.getWidth() / rg_tab.getChildCount();
            }
        }
        return tabWidth;
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }
}