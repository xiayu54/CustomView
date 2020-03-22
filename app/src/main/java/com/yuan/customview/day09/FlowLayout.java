package com.yuan.customview.day09;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author XYuan
 * Version  1.0
 * Description  自定义的 流式布局 FlowLayout 一般用在标签上面
 *  根据标签长度，自动换行
 *  1、布局中使用
 *  2、onMeasure 测量宽高
 *  3、onLayout 摆放布局
 *  4、子 View 的 margin 值
 */
public class FlowLayout extends ViewGroup {

    // 最好放在一个集合在，不然 onMeasure onLayout 要计算两次
    private List<List<View>> mChildViews = new ArrayList<>();

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 清空一下,上一次的
        mChildViews.clear();
        // 1 先测量 指定宽高
        // 1.1 for 循环测量子 View
        int childCount = getChildCount();
        // 获取到父布局的宽度
        int width = MeasureSpec.getSize(widthMeasureSpec);
        // 考虑 padding 值
        int height = getPaddingTop() + getPaddingBottom();
        // 获取内容一行的宽度
        int lineWidth = getPaddingLeft();

        // 定义一个 arrayList,存放子 View
        ArrayList<View> childViews = new ArrayList<>();
        mChildViews.add(childViews);
        // 定义一个最大高度变量，在子 View 高度不一致情况下
        int maxHeight = 0;
        for (int i = 0; i < childCount; i++){
            // 获取子 View
            View childView = getChildAt(i);

            // 细节上的问题 前提是不等于 Gone 的时候，才去进行摆放
            if (childView.getVisibility() == GONE){
                continue;
            }
            // 这段话执行后，就可以获取子 View 的宽高了，因为 measureChild 中会调用子 View 的 measure 方法
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);

            // 4、获取子 View 的 margin 值，只能通过 layoutParams 获取 所以我们需要有自己的 layoutParams，用系统的 MarginLayoutParams
            // 想想 LinearLayout 为什么会有 margin 值？？我们可以去看看源码
            // 1 LinearLayout 有自己的 layoutParams 重写 generateLayoutParams 方法
            ViewGroup.MarginLayoutParams params = (MarginLayoutParams) childView.getLayoutParams();
            // 接着就可以拿 margin 了

            // 1.2 根据子 View 计算和指定自己的布局
            // 什么时候需要换行，一行不够的情况下，才去换行
            // TODO: 2020/3/18  还需要考虑 margin 的情况、以及子 View 高度不一致的情况
            if (lineWidth + (childView.getMeasuredWidth() + params.leftMargin + params.rightMargin) >  width){
                // 换行
                height += childView.getMeasuredHeight() + params.bottomMargin + params.topMargin;
                // 换行后 第一次以为宽度要清 0，后来发现 清0 有问题，也需要添加宽度
                lineWidth = childView.getMeasuredWidth() + params.leftMargin + params.rightMargin; //
                // 需要换行的情况下 将 childViews 添加到 mChildViews 中
                childViews = new ArrayList<>();
                mChildViews.add(childViews);
            }else {
                // 不换行，宽度累加
                lineWidth += childView.getMeasuredWidth() + params.leftMargin + params.rightMargin;
                //不需要换行的情况下，添加到 集合中
//                childViews.add(childView);
                maxHeight = Math.max(childView.getMeasuredHeight() + params.topMargin + params.bottomMargin, maxHeight);
            }
            // 需要添加到 集合中
            childViews.add(childView);
        }
        // 最后添加
        height += maxHeight;
        Log.e("TAG", "width：" + width + "   height：" + height);
        // 重新设置宽高
        setMeasuredDimension(width, height);
    }

    /**
     * 重写这个方法
     * @param attrs
     * @return
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // 2 再摆放
//        int childeCount = getChildCount();
        int left, top = getPaddingTop(), right, bottom;
        // for 循环摆放所有的子 View
/*        for (int i = 0; i < childeCount; i++){
            View childView = getChildAt(i);
            // 计算摆放
            childView.layout();
        }*/

        // for 循环 mChildViews
        for (List<View> childViews : mChildViews) {
            left = getPaddingLeft();

            for (View childView : childViews) {
                // 细节上的问题 前提是不等于 Gone 的时候，才去进行摆放
                if (childView.getVisibility() == GONE){
                    continue;
                }
                ViewGroup.MarginLayoutParams params = (MarginLayoutParams) childView.getLayoutParams();
                left += params.leftMargin;
                // 同一行的 top margin 不能累加，我们需要用一个变量
                int childTop = top + params.topMargin;
                right = left + childView.getMeasuredWidth();    // left + 自身的宽度
                bottom = childTop + childView.getMeasuredHeight();
                Log.e("TAG", childView.toString());
                Log.e("TAG", "left：" + left + "   top：" + childTop + "  right：" + right + "   bottom：" + bottom);
                // 摆放子 View
                childView.layout(left, childTop, right, bottom);

                // left 叠加，每放一个 left 就要移一下
                 left += childView.getMeasuredWidth() + params.leftMargin;
            }

            // 轮询 一次，累加一次 top
            ViewGroup.MarginLayoutParams params = (MarginLayoutParams) childViews.get(0).getLayoutParams();
            top += childViews.get(0).getMeasuredHeight() + params.topMargin + params.bottomMargin;
        }

    }

    // 流式布局中，不需要 onDraw，自己去绘制
    // 背景也不需要，是 View ViewGroup 自带的
    private FlowAdapter mAdapter;

    public void setAdapter(FlowAdapter adapter){
        // 为 null，报异常
        if (adapter == null){
            throw new NullPointerException("FlowAdapter 为 空");
        }
        // 每一次进来先清空所有子 View
        removeAllViews();

        mAdapter = adapter;
        // 获取条目个数
        int childCount = adapter.getCount();
        for (int i= 0; i < childCount; i++){
            // 通过位置获取 view
            View view = adapter.getView(i,this);
            addView(view);
        }
    }
}
