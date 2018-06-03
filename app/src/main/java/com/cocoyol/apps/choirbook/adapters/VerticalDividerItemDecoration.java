package com.cocoyol.apps.choirbook.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class VerticalDividerItemDecoration extends RecyclerView.ItemDecoration {
    private static final int[] ATTRS = new int[]{ android.R.attr.listDivider };
    private Drawable mDivider;

    private int paddingLeft;
    private int paddingRight;

    public VerticalDividerItemDecoration(Context context, int paddingLeft, int paddingRight) {
        this.paddingLeft =  paddingLeft;
        this.paddingRight = paddingRight;

        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        int dividerLeft = parent.getPaddingLeft() + paddingLeft;
        int dividerRight = parent.getWidth() - parent.getPaddingRight() - paddingRight;

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int dividerTop = child.getBottom() + params.bottomMargin;
            int dividerBottom = dividerTop + mDivider.getIntrinsicHeight();

            mDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
            mDivider.draw(c);
        }
    }
}
