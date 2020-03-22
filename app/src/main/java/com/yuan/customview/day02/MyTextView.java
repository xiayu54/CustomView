package com.yuan.customview.day02;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.yuan.customview.R;

/**
 * @author XYuan
 * Version  1.0
 * Description
 */
public class MyTextView extends View {

    private String mText;
    private int mTextColor = Color.BLACK;
    private int mTextSize = 15;     // 这里给的数值是 像素px 的，我们需要转换成 sp
    private Paint mPaint;

    public MyTextView(Context context) {
        this(context, null);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MyTextView);
        mText = array.getString(R.styleable.MyTextView_MyText);
        mTextColor = array.getColor(R.styleable.MyTextView_MyTextColor, mTextColor);
        mTextSize = array.getDimensionPixelSize(R.styleable.MyTextView_MyTextSize, sp2px(mTextSize));
        // 先回收
        array.recycle();

        // 初始化 画笔
        mPaint = new Paint();
        // 抗锯齿
        mPaint.setAntiAlias(true);
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mTextSize);
    }

    /**
     *  sp 转像素 px
     * @param sp
     * @return
     */
    private int sp2px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取大小，指定宽高
        // 指定宽度为 固定大小、match_parent 时，直接获取   EXACTLY 模式
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        // 当宽度给的是 wrap_content 时，需要计算
        if (widthMode == MeasureSpec.AT_MOST){
            // 计算宽度 与字体长度、大小有关  需要用画笔测量
            Rect bounds = new Rect();
            // 测量获取文本的 Rect
            mPaint.getTextBounds(mText, 0, mText.length(), bounds);
            width = bounds.width() + getPaddingLeft() + getPaddingRight();
        }

        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == MeasureSpec.AT_MOST){
            Rect bounds = new Rect();
            mPaint.getTextBounds(mText, 0, mText.length(), bounds);
            height = bounds.height() + getPaddingTop() + getPaddingBottom();
        }
        // 重新设置一下宽高值
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画文字 text x y paint
        // x: 开始的位置
        // y: 基线的位置，baseLine
        // baseLine = 中心点 + dy
        // Metrics 中 top 是 baseLine 到最上面的距离 负值，bottom 是 baseLine 是 到 最下面的距离 正值
        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        int dy = (fontMetrics.bottom - fontMetrics.top) / 2  - fontMetrics.bottom;
        int baseLine = getHeight() / 2 + dy;
        canvas.drawText(mText, getPaddingLeft(), baseLine , mPaint);
    }
}
