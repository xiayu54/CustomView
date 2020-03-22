package com.yuan.customview.day09;

import android.view.View;
import android.view.ViewGroup;

/**
 * @author XYuan
 * Version  1.0
 * Description  第 10 天 完善的 流式布局 适配器
 */
public abstract class FlowAdapter {
    // 获取 View 的条目
    public abstract int getCount();
    // 根据位置获取 view
    public abstract View getView(int position, ViewGroup parent);
}
