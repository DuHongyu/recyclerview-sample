package com.example.recyclerview.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerview.entity.Item;

import java.util.List;

/**
 * @author Du
 */
public class PositionControlUtils {

    private static final PositionControlUtils instance = new PositionControlUtils();
    private static final String TAG = "PositionControlUtils";

    private PositionControlUtils() {
    }

    public static PositionControlUtils getPositionControlUtils() {
        return instance;
    }

    public void resetEditHeight(RecyclerView recyclerViewExist, int size, int itemWidth, int lastRow) {

        Log.d(TAG,"执行resetEditHeight，且size = "+size);

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

    public int getActivityWidth(Context context) {

        try {

            DisplayMetrics mDm = new DisplayMetrics();

            ((Activity) context).getWindowManager().getDefaultDisplay()
                    .getMetrics(mDm);

            return mDm.widthPixels;
        } catch (Exception e) {
            return 0;
        }
    }

    public void moveToPosition(RecyclerView recyclerViewAll, GridLayoutManager gridManager, int position, boolean isMove, int scrollPosition, List<Item> allData) {

        Log.d(TAG,"moveToPosition");
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

    public void scrollTab(List<String> scrollTab, String currentTab, int tabWidth, RadioGroup rg_tab, HorizontalScrollView horizontalScrollView, CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        Log.d(TAG,"scrollTab");
        try {
            int position = scrollTab.indexOf(currentTab);
            int targetPosition = scrollTab.indexOf(currentTab);
            if (targetPosition != -1) {
                int x = (targetPosition - position) * SizeUtils.getInstance().getTabWidth(tabWidth, rg_tab);
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
}
