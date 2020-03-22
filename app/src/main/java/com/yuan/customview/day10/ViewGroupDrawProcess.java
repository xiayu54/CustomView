package com.yuan.customview.day10;

/**
 * @author XYuan
 * Version  1.0
 * Description
 */
public class ViewGroupDrawProcess {

    /**
     * 自定义 View 和 ViewGroup 一般 流程
     *
     * 自定义 View 流程
     * 1、观察自定义效果
     * 2、设置自定义属性
     * 3、在布局中使用自定义属性
     * 4、代码中获取自定义属性，从而达到配置的效果
     * 5、onMeasure 方法，用于计算自己的宽高，前提是继承于 View，如果继承的是已有的控件，则系统已经计算好了
     * 6、onDraw() 绘制自己的显示
     * 7、onTouch() 与用户交互的
     *
     *
     * 自定义 ViewGroup 流程
     * 1、自定义属性，获取自定义属性，达到配置的效果（很少会有 ）
     * 2、onMeasure() ,for 循环测量子 View，根据子 View 的宽高来计算自己的宽高
     * 3、onLayout() 摆放子 View，前提不是 Gone 的情况
     * 4、onDraw() 一般不需要，默认情况下，不会调用的，如果要绘制的话，需要实现 dispatchDraw() 方法
     * 5、在很多情况下，基本不会继承 ViewGroup,往往会继承系统已经提供好的 ViewGroup，如：viewPager、LinearLayout、Scrollview、RelativeLayout.....
     */
}
