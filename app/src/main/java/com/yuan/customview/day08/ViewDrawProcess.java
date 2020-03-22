package com.yuan.customview.day08;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @author XYuan
 * Version  1.0
 * Description  介绍一下 View 的绘制流程
 */
public class ViewDrawProcess extends View {
    public ViewDrawProcess(Context context) {
        super(context);
    }

    public ViewDrawProcess(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewDrawProcess(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     *  View 的绘制流程
     *  View 绘制 是在activity 启动，onResume() 之后开始的，调用了 addView，在 WindowManagerGlobal 类中 又调用了
     *  toot.setView 进入了 ViewRootImpl 类中，执行了 requestLayout() 方法 ，然后经过一系列的方法后，最后调用了 performTraversals 方法
     *  在 performTraversals 方法中，依次执行了 performMeasure、performLayout、performDraw 方法
     *
     *  测量
     *      performMeasure --> Measure() --> onMeasure()
     *      用来指定和测量 所有控件 的宽高，对于 ViewGroup，先 for 循环遍历所有的子 View，获取子 View 的宽高，根据子 View 的宽高来计算
     *          自己的宽高；对于 View 来说，它们的宽高由 父布局 和 自己一起决定的(getChildMeasure 方法中)
     *  摆放
     *      performLayout --> Layout() --> onLayout()
     *      用来摆放子 View 的位置，for 循环所有的子 View，用 child.layout 摆放 childView
     *
     *  绘制
     *      performDraw --> draw() --> onDraw()
     *      用来绘制自己还有子 View，对于 ViewGroup，首先绘制自己的背景，然后 for 循环 绘制子 View，调用子 View 的draw() 方法。
     *          对于 View，绘制自己的背景，绘制自己显示的内容。
     *
     *
     *
     *
     */
}
