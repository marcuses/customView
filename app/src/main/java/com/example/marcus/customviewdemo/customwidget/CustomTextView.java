package com.example.marcus.customviewdemo.customwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.icu.util.Measure;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.example.marcus.customviewdemo.R;

/**
 * Created by marcus on 12/2/16.
 */
public class CustomTextView extends View{
    private String mStrTitleText;
    private int mTitleTextColor;
    private int mTitleTextSize;

    private Rect mBound;
    private Paint mPaint;

    public CustomTextView(Context context) {
        this(context, null);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.CustomTextView, defStyleAttr, 0);

        if (a != null) {
            final int nAttrCount = a.getIndexCount();
            for (int index = 0; index < nAttrCount; index++) {
                int attr = a.getIndex(index);
                switch (attr){
                    case R.styleable.CustomTextView_titleText:
                        mStrTitleText = a.getString(attr);
                        break;
                    case R.styleable.CustomTextView_titleTextColor:
                        mTitleTextColor = a.getColor(attr, Color.BLACK);
                        break;
                    case R.styleable.CustomTextView_titleTextSize:
                        mTitleTextSize = a.getDimensionPixelSize(attr,  (int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                        break;
                }
            }

            a.recycle();
        }

        mPaint = new Paint();
        mPaint.setTextSize(mTitleTextSize);

        if (mStrTitleText == null){
            mStrTitleText = "";
        }
        mBound = new Rect();
        mPaint.getTextBounds(mStrTitleText, 0, mStrTitleText.length(), mBound);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(),mPaint);

        mPaint.setColor(mTitleTextColor);
        canvas.drawText(mStrTitleText, getMeasuredWidth() / 2 - mBound.width() / 2, getMeasuredHeight() / 2 + mBound.height() / 2, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMeasureMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        if (widthMeasureMode == MeasureSpec.UNSPECIFIED || widthMeasureMode == MeasureSpec.AT_MOST){
            width = mBound.width() + getPaddingLeft() + getPaddingRight();
        }

        int heightMeasureMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMeasureMode == MeasureSpec.UNSPECIFIED || heightMeasureMode == MeasureSpec.AT_MOST){
            height = mBound.height() + getPaddingBottom() + getPaddingTop();
        }
        setMeasuredDimension(width, height);
    }
}
