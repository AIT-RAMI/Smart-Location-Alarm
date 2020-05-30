package com.example.smartlocationalarm;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class myDeviderItemDecoration extends RecyclerView.ItemDecoration {
    public static final int Horizontal_List = LinearLayoutManager.HORIZONTAL;
    public static final int Vertical_List = LinearLayoutManager.VERTICAL;
    private static final int[] ATR = new int[]{
            android.R.attr.listDivider
    };
    private Drawable myDivider;
    private int myOrientation;
    private Context context;
    private int margin;

    public myDeviderItemDecoration(int Orientation, Context context, int margin) throws IllegalAccessException {
        this.context = context;
        this.margin = margin;
        final TypedArray a = context.obtainStyledAttributes(ATR);
        myDivider = a.getDrawable(0);
        a.recycle();
        setOrientation(Orientation);
    }

    private void setOrientation(int Orientation) throws IllegalAccessException {
        if (myOrientation != Horizontal_List && myOrientation != Vertical_List) {
            throw new IllegalAccessException("invalid");
        }
        myOrientation = Orientation;
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (myOrientation == Vertical_List) {
            drawVerticalRecycler(c, parent);
        } else {
            drawHorizontalRecycler(c, parent);
        }
    }

    private void drawHorizontalRecycler(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + myDivider.getIntrinsicHeight();
            myDivider.setBounds(left + dpTopx(margin), top, right - dpTopx(margin), bottom);
            myDivider.draw(c);
        }
    }

    private void drawVerticalRecycler(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + myDivider.getIntrinsicHeight();
            myDivider.setBounds(left + dpTopx(margin), top, right - dpTopx(margin), bottom);
            myDivider.draw(c);
        }
    }

    private int dpTopx(int dp) {

        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));

    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (myOrientation == Vertical_List) {
            outRect.set(0, 0, 0, myDivider.getIntrinsicHeight());
        } else {
            outRect.set(0, 0, myDivider.getIntrinsicWidth(), 0);
        }
    }
}
