package com.yuan.customview.day05;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import com.yuan.customview.R;

/**
 * @author XYuan
 * Version  1.0
 * Description  自定义进度条
 */
public class ProgressBar extends View {

    // 进度条外圆颜色
    private int mOuterColor = Color.BLUE;
    // 进度条内环颜色
    private int mInnerColor = Color.GREEN;
    // 进度条宽度
    private int mWidth = 5; // 像素，需要转换
    // 进度文字颜色
    private int mTextColor = Color.GREEN;
    // 进度文字大小
    private int mTextSize = 15;
    // 画笔
    private Paint mOuterPaint, mInnerPaint, mTextPaint;
    // 进度条当前进度
    private int mCurrentProgress = 0;
    // 进度条最大值
    private int mMaxProgress = 0;


    public ProgressBar(Context context) {
        this(context, null);
    }

    public ProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ProgressBar);
        mOuterColor = array.getColor(R.styleable.ProgressBar_progressBarOuterColor, mOuterColor);
        mInnerColor = array.getColor(R.styleable.ProgressBar_progressBarInnerColor, mInnerColor);
        mWidth = (int) array.getDimension(R.styleable.ProgressBar_progressBarWidth, dp2Px(mWidth));
        mTextColor = array.getColor(R.styleable.ProgressBar_progressBarTextColor, mTextColor);
        mTextSize = array.getDimensionPixelSize(R.styleable.ProgressBar_progressBarTextSize, sp2Px(mTextSize));
        // 记得回收
        array.recycle();
        // 外圆画笔
        mOuterPaint = new Paint();
        mOuterPaint.setAntiAlias(true);
        mOuterPaint.setColor(mOuterColor);
        mOuterPaint.setStrokeWidth(mWidth);
        mOuterPaint.setStyle(Paint.Style.STROKE);   // 圆环空心
        // 内环画笔
        mInnerPaint = new Paint();
        mInnerPaint.setAntiAlias(true);
        mInnerPaint.setColor(mInnerColor);
        mInnerPaint.setStrokeWidth(mWidth);
        mInnerPaint.setStyle(Paint.Style.STROKE);
        // 文字画笔
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        // 设置宽高值，保证是正方形
        setMeasuredDimension(Math.min(width, height), Math.min(width, height));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 进行绘制
        // 绘制进度条外圆
        // 获取中心点
        int center = getWidth() / 2;
        // 发现边缘遮住了圆环宽度的一半，半径需要减去
        canvas.drawCircle(center, center, center - mWidth /2, mOuterPaint);
        // 绘制 进度条变化的圆环
        // 计算百分比，用 float
        float percent = (float) mCurrentProgress / mMaxProgress;
        RectF rectF = new RectF(mWidth / 2, mWidth / 2, getWidth() - mWidth / 2, getHeight() - mWidth / 2);
        canvas.drawArc(rectF, 0, percent * 360, false, mInnerPaint);
        // 绘制进度条百分比
        String text = ((int)(percent * 100)) + "%";
        Rect bounds = new Rect();
        mTextPaint.getTextBounds(text, 0 ,text.length(), bounds);
        int x = getWidth() / 2 - bounds.width() / 2;
        Paint.FontMetricsInt fontMetricsInt = mTextPaint.getFontMetricsInt();
        int dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
        int baseLine = getHeight() / 2 + dy;
        canvas.drawText(text, x, baseLine, mTextPaint);
    }


    private int dp2Px(int width) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, getResources().getDisplayMetrics());
    }

    private int sp2Px(int textSize) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize, getResources().getDisplayMetrics());
    }

    public void setMaxProgress(int maxProgress){
        if (maxProgress < 0){
            new IllegalArgumentException("进度条最大值不能小于 0");
        }
        this.mMaxProgress = maxProgress;
    }

    public void setCurrentProgress(int currentProgress){
        if (currentProgress < 0){
            new IllegalArgumentException("进度条值不能小于 0");
        }
        this.mCurrentProgress = currentProgress;
        // 刷新
        invalidate();
    }






}
