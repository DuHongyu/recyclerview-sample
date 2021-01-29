package com.example.recyclerview.widgets;

import android.graphics.Canvas;
import android.graphics.Rect;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


/**
 * @author Du
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int space = 1;

    public SpaceItemDecoration(int row, int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.left = space;
        outRect.bottom = space;
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

    }

}
