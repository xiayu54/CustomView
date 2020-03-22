package com.yuan.customview.day07;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.yuan.customview.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * @author XYuan
 * Version  1.0
 * Description
 */
public class LetterSideView extends View {
    // 未选择字体颜色
    private int mNormalColor = Color.BLACK;
    // 选中字体颜色
    private int mSelectedColor = Color.RED;
    // 字体大小
    private int mLetterSize = 12;
    // 未选择的字母画笔
    private Paint mNormalPaint;
    // 选中的字母画笔
    private Paint mSelectPaint;
    // 绘制圆形的画笔
    private Paint mSelectCirclePaint;
    // 初始化字母列表
    private static String[] mLetters = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#" };
    // 当前字母的位置
    private String mCurrentLetter;

    public LetterSideView(Context context) {
        this(context, null);
    }

    public LetterSideView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterSideView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LetterSideView);
        mNormalColor = array.getColor(R.styleable.LetterSideView_normalColor, mNormalColor);
        mSelectedColor = array.getColor(R.styleable.LetterSideView_selectedColor, mSelectedColor);
        mLetterSize = array.getDimensionPixelSize(R.styleable.LetterSideView_letterSize, (int) sp2Px(mLetterSize));
        // 回收
        array.recycle();
        // 定义未选择的画笔
        mNormalPaint = new Paint();
        mNormalPaint.setAntiAlias(true);
        mNormalPaint.setDither(true);
        mNormalPaint.setTextSize(mLetterSize);
        mNormalPaint.setColor(mNormalColor);
        // 定义选中字母的画笔
        mSelectPaint = new Paint();
        mSelectPaint.setAntiAlias(true);
        mSelectPaint.setDither(true);
        mSelectPaint.setTextSize(mLetterSize);
        mSelectPaint.setColor(mSelectedColor);
        // 绘制圆形的的画笔
        mSelectCirclePaint = new Paint();
        mSelectCirclePaint.setStyle(Paint.Style.FILL);
        mSelectCirclePaint.setAntiAlias(true);
        mSelectCirclePaint.setColor(Color.parseColor("#33CC33"));
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 计算指定宽度   画笔获取字母的 宽度 获取 A 字母的宽度
        int textWidth = (int) mNormalPaint.measureText("A");
        int width = getPaddingLeft() + textWidth + getPaddingRight();
        // 高度 match_parent 可以直接获取
        int height = MeasureSpec.getSize(heightMeasureSpec);
        // 设置宽高
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 绘制
        Log.e("TAG", "onDraw");
        Paint.FontMetricsInt fontMetricsInt = mNormalPaint.getFontMetricsInt();
        int dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
        // 获取每个 字母 的高度
        int itemHeight =  (getHeight() - getPaddingTop() - getPaddingBottom()) / mLetters.length;
        for (int i = 0; i < mLetters .length; i++) {
            // x 轴字母在中间，宽度的一半 - 文字的一半
            int textWidth = (int) mNormalPaint.measureText(mLetters[i]);
            int x = getWidth() / 2 - textWidth / 2;
            // 计算每个字母的中心位置
            int centerY = getPaddingTop() + itemHeight / 2 + itemHeight * i;
            // 基线
            int baseLine = centerY + dy;
            int radio = (int) mNormalPaint.measureText("M");
            if (mLetters[i].equals(mCurrentLetter)){
                canvas.drawCircle(getWidth() / 2, centerY, radio, mSelectCirclePaint);
                canvas.drawText(mLetters[i], x, baseLine, mSelectPaint);
            }else {
                canvas.drawText(mLetters[i], x, baseLine, mNormalPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            // 按下 和活动都会触发事件
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float y = event.getY();   // 获取 y 轴位置
                // 获取每个字母的高度
                int itemHeight =  (getHeight() - getPaddingTop() - getPaddingBottom()) / mLetters.length;
                int currentLetter = (int) (y / itemHeight);
                if (currentLetter < 0){
                    currentLetter = 0;
                }
                if (currentLetter > (mLetters.length - 1)){
                    currentLetter = mLetters.length - 1;
                }
                // 判断重绘 d同一位置时，不需要重绘
                if (mCurrentLetter == mLetters[currentLetter]){
                    return true;
                }
                mCurrentLetter = mLetters[currentLetter];
                // 实时回调
                if (mLetterTouchListener != null){
                    mLetterTouchListener.onTouch(mCurrentLetter, true);
                }
                // 重新绘制
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                // 实时回调
                if (mLetterTouchListener != null){
                    mLetterTouchListener.onTouch(mCurrentLetter, false);
                }
                break;
        }
        return true;
    }

    /**
     * sp 转 像素px
     * @param textSize
     * @return
     */
    private float sp2Px(int textSize) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize, getResources().getDisplayMetrics());
    }

    private LetterTouchListener mLetterTouchListener;
    public void setOnTouchLetterListener(LetterTouchListener touchLetter){
        this.mLetterTouchListener = touchLetter;
    }
    // 回调显示 选中字母
    public interface LetterTouchListener{
        void onTouch(CharSequence letter, boolean isShow);
    }
}
