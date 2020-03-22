package com.yuan.customview.day06;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.yuan.customview.R;

/**
 * @author XYuan
 * Version  1.0
 * Description  评分控件
 */
public class RatingBar extends View {
    // 未被选中的星星
    private Bitmap mNormalStarBitmap;
    // 选择的星星
    private Bitmap mSelectStarBitmap;
    // 星星的数量
    private int mStarNum = 5;
    // 当前选中的星星数目
    private int mSelectedStarNum = 0;

    public RatingBar(Context context) {
        this(context, null);
    }

    public RatingBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatingBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RatingBar);
        // 没有直接获取 Bitmap 的，可以先拿 id，再创建
        int normalStarId = array.getResourceId(R.styleable.RatingBar_normalStar, 0);
        if (normalStarId == 0){
            throw new RuntimeException("没有添加正常状态的图片资源，请设置属性 normalStar");
        }
        // 通过资源 id，转换成 Bitmap
        mNormalStarBitmap = BitmapFactory.decodeResource(getResources(), normalStarId);
        int selectStarId = array.getResourceId(R.styleable.RatingBar_selectedStar, 0);
        if (selectStarId == 0){
            throw new RuntimeException("没有添加选中状态的图片资源，请设置属性 selectedStar");
        }
        mSelectStarBitmap = BitmapFactory.decodeResource(getResources(), selectStarId);
        mStarNum = array.getInt(R.styleable.RatingBar_starNum, mStarNum);
        // 回收
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 指定控件宽高 还得考虑间隔 和 padding
        int height = mNormalStarBitmap.getHeight();
        int width = mNormalStarBitmap.getWidth() * mStarNum + getPaddingLeft() * (mStarNum - 1);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 绘制 5 张图片
        for (int i = 0; i < mStarNum; i++){
            // x y 绘制开始的位置
            // x 就是 数量 * 星星的宽度
            int x = mNormalStarBitmap.getWidth() * i + getPaddingLeft() * i;
            Log.e("TAG", "x = " + x + ", " + getPaddingLeft() + ", " + mNormalStarBitmap.getWidth() * i);
            // 根据变化的 mSelectedStarNum 不断修改 星星的状态
            if (i < mSelectedStarNum){  // 小于选中的都变为选中状态
                canvas.drawBitmap(mSelectStarBitmap, x, 0, null);
            }else {
                canvas.drawBitmap(mNormalStarBitmap, x, 0, null);
            }

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("TAG", event.getAction() + "");
        switch (event.getAction()){
            // 无论手指 按下、滑动、抬起 都会触发事件，都是判断手指当前的位置，根据位置去刷新控件，可以统一处理
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
//            case MotionEvent.ACTION_UP:
                // 在这里，我们只需要获取的相对当前控件位置，来判断星星的刷新
                float moveX = event.getX();     // event.getX() 是获取相对于当前控件的位置，event.getRawX() 是获取屏幕的位置

                int selectedStarNum = (int) ((moveX / mNormalStarBitmap.getWidth()) + 1);
                // 处理一下范围问题
                if (selectedStarNum < 0){
                    selectedStarNum = 0;
                }
                if (selectedStarNum > mStarNum){
                    selectedStarNum = mStarNum;
                }
                // 1、手指在同一个星星的位置时，不需要重绘
                if (selectedStarNum == mSelectedStarNum){
                    return true;
                }
                mSelectedStarNum = selectedStarNum;
                // 刷新 不断调用 onDraw
                invalidate();
                // invalidate() 会不断调用 onDraw 方法，每一次 onDraw 都会 有上到下，再有下到上，重新绘制一次所有的 view，会损耗性能
                // 所以我们要尽可能的减少 onDraw 的调用，优化一下
                // 1、手指在同一个星星的位置，不需要重绘
                // 2、up 事件和 down 事件是一样的， 可以不要
                break;
        }
        return true;    // 默认 fasle 不消费，第一次 Down false，Down以后的事件都进不来
    }
}
