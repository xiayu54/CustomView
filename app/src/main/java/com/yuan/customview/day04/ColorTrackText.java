package com.yuan.customview.day04;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.yuan.customview.R;

/**
 * @author XYuan
 * Version  1.0
 * Description
 */
@SuppressLint("AppCompatCustomView")
public class ColorTrackText extends TextView {
    // 不变色的颜色
    private int mOriginColor;
    // 变色的颜色
    private int mChangeColor;
    // 中间的位置
    private float mCurrentProgress = 0.0f;
    // 绘制不变色字体的画笔
    private Paint mOriginPaint;
    // 绘制变色字体的画笔
    private Paint mChangePaint;
    // 初始化朝向
    private Direction mDirection = Direction.LEFT_TO_RIGHT;
    // 枚举不同的朝向
    public enum Direction{
        LEFT_TO_RIGHT, RIGHT_TO_RIGHT
    }


    public ColorTrackText(Context context) {
        this(context, null);
    }

    public ColorTrackText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorTrackText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ColorTrackText);
        mOriginColor = array.getColor(R.styleable.ColorTrackText_originColor, getTextColors().getDefaultColor());
        mChangeColor = array.getColor(R.styleable.ColorTrackText_changeColor, getTextColors().getDefaultColor());
        // 记得先回收
        array.recycle();
        // 初始化画笔
        mOriginPaint = getPaint(mOriginColor);
        mChangePaint = getPaint(mChangeColor);
    }

    /**
     *  根据颜色获取画笔
     * @param color
     * @return
     */
    private Paint getPaint(int color){
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setTextSize(getTextSize());
        paint.setColor(color);
        return paint;
    }

    //继承 textView ，onMeasure 测量这一步就不需要我们自己做了


    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        // 自己绘制
        // 计算中间的位置
        int middle = (int) (mCurrentProgress * getWidth());
        if (mDirection == Direction.LEFT_TO_RIGHT){ // 从左到右变色
            // 绘制不变色字体
            drawText(canvas, middle, getWidth(), mOriginPaint);
            // 绘制变色字体
            drawText(canvas, 0, middle, mChangePaint);
        }else { // 从右到左
            // 绘制不变色字体
            drawText(canvas, 0, getWidth() - middle, mOriginPaint);
            // 绘制变色字体
            drawText(canvas, getWidth() - middle, getWidth(), mChangePaint);
        }


    }

    /**
     *  绘制字体
     */
    private void drawText(Canvas canvas, int start, int end, Paint paint){
        // 自己绘制

        // 先截取
        canvas.save();
        canvas.clipRect(start, 0, end, getHeight());
        String text = getText().toString();
        // 通过 paint 获取字体宽高
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int x = getWidth() / 2 - bounds.width() / 2;
        Paint.FontMetricsInt metricsInt = paint.getFontMetricsInt();
        int dy = (metricsInt.bottom - metricsInt.top) / 2 - metricsInt.bottom;
        int baseLine = getHeight() / 2 + dy;
        canvas.drawText(text,x, baseLine, paint);
        canvas.restore();
    }

    public void setDirection(Direction direction){
        this.mDirection = direction;
    }

    public void setCurrentProgress(float currentProgress){
        this.mCurrentProgress = currentProgress;
        invalidate();
    }
}
