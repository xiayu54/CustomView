package com.yuan.customview.day01;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.yuan.customview.R;

/**
 * @author XYuan
 * Version  1.0
 * Description  自定义 View 开始前的准备知识
 */
public class TextView extends View {

    private String mText;
    private int mTextSize = 14;
    private int mTextColor = Color.BLACK;

    // 构造方法的调用
    /**
     * TextView textView = new TextView(this);
     * 会在代码中 new 的时候调用
     * @param context 上下文环境
     */
    public TextView(Context context) {
        this(context, null);
    }

    /**
        <com.yuan.customview.day01.TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="111"/>
     *  在布局 layout 中使用
     */
    public TextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     *    <com.yuan.customview.day01.TextView
     *         style="@style/TextView_Default"
     *         android:text="111"/>
     *
     *  在布局 layout 中使用，但是会有 style,多个布局 中，有相同 style 时，写一个 style 是一个很好的方式
     */
    public TextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 获取自定义属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TextView);
        mText = array.getString( R.styleable.TextView_text);
        mTextSize = array.getDimensionPixelSize(R.styleable.TextView_textSize, mTextSize);
        mTextColor = array.getColor(R.styleable.TextView_color, mTextColor);
        // 记得先回收
        array.recycle();
    }

    /**
     * 自定义 view 的测量方法
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 布局的宽高 都是由这个方法指定
        // 指定控件的宽高，需要测量
        // 获取宽高的模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getMode(heightMeasureSpec);
        /**
         *  3 种模式
         *  MeasureSpec.AT_MOST：在布局中指定了 wrap_content
         *  MeasureSpec.EXACTLY：在布局中指定确切的值 150dp  match_parent
         *  MeasureSpec.UNSPECIFIED：尽可能的大 很少会用到 ListView，ScrollView 在测量子布局的时候会调用 UNSPECIFIED
         */
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 用于绘制
        // 画文本
//        canvas.drawText();
        // 画弧
//        canvas.drawArc();
        // 画圆
//        canvas.drawCircle();
    }

    /**
     * 处理与用户交互的事件，手指触摸等等
     * @param event 事件分发事件拦截
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                // 手指按下
                break;
            case MotionEvent.ACTION_MOVE:
                // 手指滑动
                break;
            case MotionEvent.ACTION_UP:
                // 手指抬起
                break;
        }
        return super.onTouchEvent(event);
    }
}
