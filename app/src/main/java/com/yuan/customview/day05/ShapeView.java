package com.yuan.customview.day05;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.yuan.customview.R;

/**
 * @author XYuan
 * Version  1.0
 * Description  自定义形状变换的view
 */
public class ShapeView extends View {
    // 正方形颜色
    private int mSquareColor = Color.GRAY;
    // 圆形颜色
    private int mCircleColor = Color.GRAY;
    // 三角形颜色
    private int mTriangleColor = Color.GRAY;
    // 画笔
    private Paint mPaint;
    // 当前形状
    private Shape mCurrentShape = Shape.Circle;
    // 三角形的路线，不必每次都 new
    private Path mPath;
    // 枚举三种类型
    private enum Shape{
        Square, Circle, Triangle
    }
    public ShapeView(Context context) {
        this(context, null);
    }

    public ShapeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ShapeView);
        mSquareColor = array.getColor(R.styleable.ShapeView_squareColor, mSquareColor);
        mCircleColor = array.getColor(R.styleable.ShapeView_circleColor, mCircleColor);
        mTriangleColor = array.getColor(R.styleable.ShapeView_triangleColor, mTriangleColor);
        // 回收
        array.recycle();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 保证是正方形
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(Math.min(width, height), Math.min(width, height));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.e("TAG", "进入 onDraw");
        // 绘制
        switch (mCurrentShape){
            case Square:
                // 画正方形
                mPaint.setColor(mSquareColor);
                canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
                break;
            case Circle:
                // 画圆形
                mPaint.setColor(mCircleColor);
                int center = getWidth() / 2;
                canvas.drawCircle(center, center, center, mPaint);
                break;
            case Triangle:
                // 画圆形
                mPaint.setColor(mTriangleColor);
                if (mPath == null){
                    mPath = new Path();
                    // 绘制一个等边三角形
                    mPath.moveTo(getWidth() / 2, 0);    // 三角形绘制开始的位置
                    mPath.lineTo(0, (float) ((getWidth() / 2 )* Math.sqrt(3))); // 连线
                    mPath.lineTo(getWidth(), (float) ((getWidth() / 2 )* Math.sqrt(3)));
                    mPath.close();  // 将起始点 与 结束点闭合
                }
                canvas.drawPath(mPath, mPaint);
                break;
        }
    }

    /**
     *  改变形状
     */
    public void change(){
        Log.e("TAG", "进入 change");
        switch (mCurrentShape){
            case Square:
                mCurrentShape = Shape.Circle;
                break;
            case Circle:
                mCurrentShape = Shape.Triangle;
                break;
            case Triangle:
                mCurrentShape = Shape.Square;
                break;
        }
        invalidate();
    }
}
