package com.example.marcus.customviewdemo.customwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.marcus.customviewdemo.R;

/**
 * Created by marcus on 12/20/16.
 */
public class CustomCircle extends View {
    private Paint mPaint;
    private float mCircleRadius = 20;
    private float mCircleWidth = 5;
    private int mFirstColor = Color.RED;
    private int mSecondColor = Color.BLUE;
    private long mCircleDrawSpeed = 500;
    private float mCurrentAngle = 0;


    public CustomCircle(Context context) {
        this(context, null);
    }

    public CustomCircle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        initAttrs(context, attrs, defStyleAttr);
        new Thread(){
            @Override
            public void run() {
                while (true) {
                    mCurrentAngle++;
                    if (mCurrentAngle == 360) {
                        mCurrentAngle = 0;
                        int tmpColor = mFirstColor;
                        mFirstColor = mSecondColor;
                        mSecondColor = tmpColor;
                    }
                    postInvalidate();
                    try {
                        Thread.sleep(mCircleDrawSpeed);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private void initPaint(){
        mPaint = new Paint();
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomCircle, defStyleAttr, 0);
        if (a == null){
            return ;
        }

        int attrsCount = a.getIndexCount();
        for (int index = 0; index < attrsCount; index++){
            int attr = a.getIndex(index);
            switch (attr){
                case R.styleable.CustomCircle_circleDrawSpeed:
                    mCircleDrawSpeed = a.getInt(attr, 100);
                    break;
                case R.styleable.CustomCircle_circleFirstColor:
                    mFirstColor = a.getColor(attr, Color.RED);
                    break;
                case R.styleable.CustomCircle_circleRadius:
                    mCircleRadius = a.getDimension(attr, 0);
                    break;
                case R.styleable.CustomCircle_circleSecondColor:
                    mSecondColor = a.getColor(attr, Color.BLUE);
                    break;
                case R.styleable.CustomCircle_circleWidth:
                    mCircleWidth = a.getDimension(attr, 0);
                    break;
                default:
                    break;
            }
        }
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode != MeasureSpec.EXACTLY){
            height = (int)(mCircleRadius + mCircleWidth) * 2 + getPaddingBottom() + getPaddingTop();
        }

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        if (widthMode != MeasureSpec.EXACTLY){
            width = (int)(mCircleWidth + mCircleRadius) * 2 + getPaddingRight() + getPaddingLeft();
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int center = getWidth() / 2;
        RectF rect = new RectF(center - mCircleRadius, center - mCircleRadius, center + mCircleRadius, center + mCircleRadius);
        mPaint.setColor(Color.BLACK);
        canvas.drawRect(rect, mPaint);

        mPaint.setStrokeWidth(mCircleWidth);
        mPaint.setStyle(Paint.Style.STROKE);

        if (mCurrentAngle != 0) {
            mPaint.setColor(mFirstColor);
            canvas.drawArc(rect, 0, mCurrentAngle, false, mPaint);
        }

        mPaint.setColor(mSecondColor);
        canvas.drawArc(rect, mCurrentAngle, 360 - mCurrentAngle, false, mPaint);

    }
}
