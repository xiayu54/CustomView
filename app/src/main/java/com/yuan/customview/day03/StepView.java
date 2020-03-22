package com.yuan.customview.day03;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.yuan.customview.R;

/**
 * @author XYuan
 * Version  1.0
 * Description  自定义环形步数显示器
 */
public class StepView extends View {
    private int mOuterColor = Color.BLUE;
    private int mInnerColor = Color.RED;
    private int mBorderWidth = 15;      // 代表的是15个px
    private int mTextColor = Color.RED;
    private int mTextSize = 10;
    // 画笔
    private Paint mOuterPaint;
    private Paint mInnerPaint;
    private Paint mTextPaint;
    // 内环进度
    // 总共
    private int mProgressMax = 0;
    // 当前
    private int mCurrentProgress = 0;
    public StepView(Context context) {
        this(context, null);
    }

    public StepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 1 分析效果
        // 2 确定自定义属性，写在 attrs.xml 文件中
        // 3 在布局中获取
        // 4 在自定义 view 中获取自定义属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.StepView);
        mOuterColor = array.getColor(R.styleable.StepView_outerColor, mOuterColor);
        mInnerColor = array.getColor(R.styleable.StepView_innerColor, mInnerColor);
        mBorderWidth = (int) array.getDimension(R.styleable.StepView_borderWidth, mBorderWidth);
        mTextSize = array.getDimensionPixelSize(R.styleable.StepView_stepTextSize, mTextSize);
        mTextColor = array.getColor(R.styleable.StepView_stepTextColor, mTextColor);
        // 回收
        array.recycle();
        // 5 onMeasure()
        // 6 onDraw() 画外圆弧 内圆弧 文字
        // 7 其他处理

        // 外环画笔
        mOuterPaint = new Paint();
        mOuterPaint.setAntiAlias(true);
        mOuterPaint.setColor(mOuterColor);
        mOuterPaint.setStrokeWidth(mBorderWidth);
        mOuterPaint.setStrokeCap(Paint.Cap.ROUND);      // 设置左右进出口为圆形
        mOuterPaint.setStyle(Paint.Style.STROKE);     // FILL 画笔实心 ，  STROKE 画笔空心
        // 内环
        mInnerPaint = new Paint();
        mInnerPaint.setAntiAlias(true);
        mInnerPaint.setColor(mInnerColor);
        mInnerPaint.setStrokeWidth(mBorderWidth);
        mInnerPaint.setStrokeCap(Paint.Cap.ROUND);      // 设置左右进出口为圆形
        mInnerPaint.setStyle(Paint.Style.STROKE);     // FILL 画笔实心 ，  STROKE 画笔空心
        // 文字
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 5 onMeasure
        // 调用者在布局中可能会用 wrap_content 导致宽高不一致
        // 需要判断一下
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.AT_MOST){
            throw new IllegalArgumentException("width、height 需设为固定值");
        }
        // 宽高不一致，取最小值，确保是正方形
        setMeasuredDimension(width > height ? height:width,width > height ? height:width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 6 onDraw() 画外圆弧 内圆弧 文字
        // 6.1 画外环
        // 边缘没有显示完整，圆环宽度 mBorderWidth 占了地方
//        RectF rectF = new RectF(0, 0, getWidth(), getHeight());
        int center = getWidth() / 2;
        int radius = getWidth() / 2 - mBorderWidth/2;
        RectF rectF = new RectF(center - radius, center - radius, center + radius, center + radius);
        // startAngle 其实角度 ，sweepAngle 扫描角度范围
        canvas.drawArc(rectF, 135, 270, false, mOuterPaint);
        // 6.2 画内环 内环是百分比，用户设置的
        // 第一次进来是 0 ，处理一下
        if (mProgressMax == 0) return;
        float sweepAngle = (float) mCurrentProgress / mProgressMax;
        canvas.drawArc(rectF, 135, sweepAngle * 270, false, mInnerPaint);
        // 6.3 画文字
        String text = mCurrentProgress + "";
        // 获取文字大小
        Rect textBounds = new Rect();
        mTextPaint.getTextBounds(text, 0, text.length(), textBounds);
        int x = getWidth() / 2 - textBounds.width() / 2;
        // 获取 字体的 fontMetrics
        Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
        int dy = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        int baseLine = getHeight() / 2 + dy;
        canvas.drawText(text, x, baseLine, mTextPaint);
    }

    // 7 其他 写方法 让使用者自己设置 进度
    public synchronized void setProgressMax(int progressMax){
       mProgressMax =  progressMax;
    }

    public synchronized void setCurrentProgress(int currentProgress){
        mCurrentProgress =  currentProgress ;
        // 不断调用 onDraw
        invalidate();
    }
}
