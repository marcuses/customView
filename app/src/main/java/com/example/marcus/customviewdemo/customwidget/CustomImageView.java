package com.example.marcus.customviewdemo.customwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.example.marcus.customviewdemo.R;

/**
 * Created by marcusyang on 16-12-5.
 */

public class CustomImageView extends View{
    private String mStrTitleText;
    private int mTitleTextColor;
    private float mTitleTextSize;
    private Drawable mImageDrawable;
    private int mScaleType;

    private Paint mPaint;
    private Rect mTextBound;

    public CustomImageView(Context context) {
        this(context, null);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView, defStyleAttr, 0);

        if (a != null){
            int attrsCount = a.getIndexCount();
            for (int index = 0; index < attrsCount; index++){
                int attr = a.getIndex(index);
                switch (attr){
                    case R.styleable.CustomImageView_titleText:
                        mStrTitleText = a.getString(attr);
                        break;
                    case R.styleable.CustomImageView_titleTextColor:
                        mTitleTextColor = a.getColor(attr, Color.BLACK);
                        break;
                    case R.styleable.CustomImageView_scaletype:
                        mScaleType = a.getInt(attr, 0);
                        break;
                    case R.styleable.CustomImageView_src:
                        mImageDrawable = a.getDrawable(attr);
                        break;
                    case R.styleable.CustomImageView_titleTextSize:
                        mTitleTextSize = (a.getDimension(attr,
                                TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_SP,
                                        16,
                                        getResources().getDisplayMetrics())));
                        break;
                    default:
                        break;
                }
            }
            a.recycle();
        }

        if (mStrTitleText == null){
            mStrTitleText = "";
        }

        mTextBound = new Rect();
        mPaint = new Paint();
        mPaint.setTextSize(mTitleTextSize);
        mPaint.getTextBounds(mStrTitleText, 0,
                mStrTitleText.length(), mTextBound);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        if (widthMode != MeasureSpec.EXACTLY) {
            int titleWidth = getPaddingLeft() + getPaddingRight() + mTextBound.width();
            int imageWidth = mImageDrawable == null ? 0 : mImageDrawable.getIntrinsicWidth();
            int desireWidth = Math.max(titleWidth, imageWidth);
            if (widthMode == MeasureSpec.AT_MOST) {
                width = Math.min(width, desireWidth);
            }
        }

        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode != MeasureSpec.EXACTLY) {
            int imageHeight = mImageDrawable == null ? 0 : mImageDrawable.getIntrinsicHeight();
            int desireHeight = getPaddingBottom() + getPaddingTop() + mTextBound.height() + imageHeight;
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(desireHeight, height);
            }
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Rect rect = new Rect(0 , 0, getMeasuredWidth(), getMeasuredHeight());
        mPaint.setColor(Color.GRAY);
        canvas.drawRect(rect, mPaint);

        rect.left = getPaddingLeft();
        rect.top = getPaddingTop();
        rect.right = getMeasuredWidth() - getPaddingRight();
        rect.bottom = getMeasuredHeight() - getPaddingBottom();

        int width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        if (mTextBound.width() > width){
            TextPaint paint = new TextPaint(mPaint);
            String msg = TextUtils.ellipsize(mStrTitleText, paint, width, TextUtils.TruncateAt.END).toString();
            canvas.drawText(msg, getPaddingLeft(), getMeasuredHeight() - getPaddingBottom(), mPaint);
        } else {
            canvas.drawText(mStrTitleText, getMeasuredWidth() / 2 - mTextBound.width() / 2, getMeasuredHeight() - getPaddingBottom(), mPaint);
        }

        rect.bottom -= mTextBound.height();

        if (mScaleType == 1){
            mImageDrawable.setBounds(rect);
            mImageDrawable.draw(canvas);
        }
    }
}
