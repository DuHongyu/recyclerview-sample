package com.example.recyclerview.utils;

import android.content.Context;
import android.widget.RadioGroup;

/**
 * @author Du
 */
public class SizeUtils {

    private static final SizeUtils INSTANCE = new SizeUtils();

    private SizeUtils() {
    }

    public static SizeUtils getInstance() {
        return INSTANCE;
    }

    public int dip2px(Context context, float dpValue) {

        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public int getTabWidth(int tabWidth, RadioGroup rg_tab) {

        if (tabWidth == 0) {
            if (rg_tab != null && rg_tab.getChildCount() != 0) {
                tabWidth = rg_tab.getWidth() / rg_tab.getChildCount();
            }
        }
        return tabWidth;
    }


}
