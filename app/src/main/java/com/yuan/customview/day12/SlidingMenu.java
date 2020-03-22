package com.yuan.customview.day12;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;

import androidx.core.view.ViewCompat;

import com.yuan.customview.R;

import java.io.Closeable;

/**
 * @author XYuan
 * Version  1.0
 * Description  自定义的 仿 QQ5.0 的侧滑效果
 *  实现步骤：
 *      1、继承自定义 HorizontalScrollView，写好两个布局（menu，content）,y运行起来看效果
 *      2、运行之后布局是乱套的，menu、content宽度不对，应对方法：指定内容(屏幕的宽度) 和 菜单的宽度(屏幕的宽度 - 自定义属性的宽度)
 *      3、默认是关闭的，手指抬起的时候，要判断是关闭还是打开状态，采用代码滚动到对应位置
 *      4、处理快速滑动
 *      5、处理内容部分缩放；菜单部分有位移和透明度，时时刻刻监听当前滚动的位置
 *      6、充分考虑前几次看的源码
 */
public class SlidingMenu extends HorizontalScrollView {
    // menu 侧滑栏离屏幕右边的距离
    private int mMenuRightMargin;
    // 菜单栏的宽度
    private int mMenuWidth;

    private View mMenuView, mContentView;

    public SlidingMenu(Context context) {
        this(context, null);
    }

    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 初始化自定义属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SlidingMenu);
        mMenuRightMargin = (int) array.getDimension(R.styleable.SlidingMenu_menuRightMargin, dip2px(context,50));
        // 菜单栏的宽度 = 屏幕宽度 - 自定义属性的距离
        mMenuWidth = getScreenWidth(context) - mMenuRightMargin;
        // 记得回收
        array.recycle();
    }

    /**
     * 2、宽度不对，指定宽度
     *  这个方法在 onCreate 中调用
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // 获取 LinearLayout
        ViewGroup container = (ViewGroup) getChildAt(0);
        // 判断数量，只能有两个子 View
        int childCount = container.getChildCount();
        if (childCount != 2){
            new RuntimeException("只能有两个子 View");
        }
        // 设置 菜单栏宽度
        mMenuView = container.getChildAt(0);
        // 无法直接设置。只能通过 LayoutParams
        ViewGroup.LayoutParams menuParams = mMenuView.getLayoutParams();
        menuParams.width = mMenuWidth;
        // 7.0 以上 ，必须执行以下方法才行
        mMenuView.setLayoutParams(menuParams);
        // 设置 content 宽度
        mContentView = container.getChildAt(1);
        // 无法直接设置。只能通过 LayoutParams
        ViewGroup.LayoutParams contentParams = mContentView.getLayoutParams();
        contentParams.width = getScreenWidth(getContext());
        // 7.0 以上 ，必须执行以下方法才行
        mContentView.setLayoutParams(contentParams);
        // 初始化进来应该是关闭，得把 menu 移走，发现在这里无效
//        scrollTo(mMenuWidth, 0);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        // 在这里才有效
        scrollTo(mMenuWidth, 0);
    }

    /**
     * 时时刻刻监听滑动
     * 4、不断获取滚动位置，处理缩放、透明等
     * @param l
     * @param t
     * @param oldl
     * @param oldt
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        Log.e("TAG", "L" +l);       // mWidthMenu -> 0
        // 需要一个变化的 梯度
        float scale = 1f * l / mMenuWidth;      // 1 -> 0
        // 右边的缩放
        float rightScale = 0.7f + 0.3f * scale;
        // 设置缩放的中心点，默认是页面的中心点
        mContentView.setPivotX(0);
        mContentView.setPivotY(getMeasuredHeight() / 2);
        mContentView.setScaleX(rightScale);
        mContentView.setScaleY(rightScale);

        // 设置左边的菜单的缩放和透明
        // 透明度 半透明 -> 不透明
        float leftAlpha = 0.4f + 0.6f * (1 - scale);
        mMenuView.setAlpha(leftAlpha);
        // 缩放 小 ->  大
        float leftScale = 0.7f + 0.3f * (1 - scale);
        mMenuView.setScaleY(leftScale);
        mMenuView.setScaleY(leftScale);
        // 设置平移效果，让 退出 一开始就紧靠在content 左边
        mMenuView.setTranslationX(0.15f * l);

    }

    /**
     * 3 手指抬起时，关闭或打开
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // 手指抬起时
        if (ev.getAction() == MotionEvent.ACTION_UP){
            // 根据移动距离判断打开，还是关闭
            int currentScrollX = getScrollX();
            if (currentScrollX > mMenuWidth / 2){
                // 关闭
                closeMenu();
            }else {
                openMenu();
            }
            return true;// 返回 true，才不会执行 super
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 打开菜单栏
     */
    private void openMenu() {
        // 带动画的移动
        smoothScrollTo(0, 0);
    }

    /**
     * 关闭菜单栏，滚动到 mMenuWidth 的位置
     */
    private void closeMenu() {
        smoothScrollTo(mMenuWidth, 0);
    }

    /**
     * 获取 屏幕宽度
     * @param context
     * @return
     */
    private int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * dip to px
     * @param context
     * @param dpValue
     * @return
     */
    private int dip2px(Context context, int dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
