package com.yuan.customview.day10;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yuan.customview.R;

/**
 * @author XYuan
 * Version  1.0
 * Description  view的 touch 事件，根据不同 touch现象，看方法的调用
 */
public class TouchTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_test);
        TouchView touchView = findViewById(R.id.custom_touch_view);
//        touchView.setEnabled(false);
        touchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("TAG", "onTouch----");
                return false;
            }
        });
        touchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG", "onClick");
            }
        });

        /**
         * 0 表示 down，1 表示 up，2 表示 move
         *
         * 现象分析：
         *  1、onTouchEvent、OnTouchListener、OnClickListener 三种都实现的情况下，点击一下屏幕 前提 OnTouchListener 返回 false
         *     结果： OnTouchListener.DOWN --> onTouchEvent.DOWN --> OnTouchListener.UP --> onTouchEvent.UP --> OnClickListener
         *
         *  2、onTouchEvent、OnTouchListener、OnClickListener 三种都实现的情况下，点击一下屏幕 前提 OnTouchListener 返回 true
         *     结果：OnTouchListener.DOWN --> OnTouchListener.UP
         *
         *  3、onTouchEvent、OnClickListener 实现两种 的情况下，点击一下屏幕 前提 onTouchEvent 返回 true
         *     结果：onTouchEvent.DOWN --> onTouchEvent.UP         不会执行 onClick
         *
         *  4、onTouchEvent、OnTouchListener、OnClickListener、dispatchTouchEvent 都实现的情况下，点击一下屏幕 前提 dispatchTouchEvent 返回 true
         *     结果：都不会走          dispatchTouchEvent 返回 true，super 里面的方法都不会走
         *
         *
         *
         *
         *  View 与 Touch 相关的有两个非常重要的方法
         *  1、dispatchTouchEvent() 方法:
         *      源码：
         *          boolean result = false;     //一开始 为 false
         *          ListenerInfo li = mListenerInfo;    // 我们所有的事件都放在了 ListenerInfo 对象中
         *          ListenerInfo：存放了关于 View 的所有 Listener 信息，如 OnClickListener、OnTouchListener、OnLongClickListener 等等
         *
         *      if (li != null && li.mOnTouchListener != null
         *               && (mViewFlags & ENABLED_MASK) == ENABLED     // 是否为 enable
         *               && li.mOnTouchListener.onTouch(this, event)) { // 如果是 false，result = false；如果是 true，result = true；
         *                 result = true;
         *      }
         *
         *     if (!result && onTouchEvent(event)) { // 如果 result 是 false，onToucheEvent 会执行；如果 result 是 true，就不会执行onTouchEvent
         *             result = true;
         *      }
         *      // 返回 result
         *      return result；
         *
         *      点击事件 click 的调用是 在 View 的 onTouchEvent() 源码中
         *      case MotionEvent.ACTION_UP:
         *                  里面调用了 performClick()
         *              最后调用了 li.mOnClickListener.onClick(this); 点击事件
         *  2、onTouchEvent() 方法(一般都会被我们重写)
         *
         *
         *  View 没有事件的拦截
         */
    }
}
