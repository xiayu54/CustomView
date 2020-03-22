package com.yuan.customview.day05;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yuan.customview.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author XYuan
 * Version  1.0
 * Description
 */
public class ProgressBarActivity extends AppCompatActivity{

    private ProgressBar mProgressBar;
    private ShapeView mShapeView;

    Handler mHandler = new Handler();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progressbar);

        mProgressBar = findViewById(R.id.custom_pb);
        mShapeView = findViewById(R.id.custom_shape);

        ValueAnimator animator = ObjectAnimator.ofFloat(0, 5000);
        mProgressBar.setMaxProgress(5000);
        animator.setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentProgress = (float) animation.getAnimatedValue();
                mProgressBar.setCurrentProgress((int) currentProgress);
            }
        });
        animator.start();

    }

    public void startChange(View view) {
        // 每秒执行一次
        Timer timer = new Timer();
        timer.schedule(new MyTask(), 0,1000);
    }

    class MyTask extends TimerTask{
        @Override
        public void run() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mShapeView.post(new Runnable() {
                        @Override
                        public void run() {
                            mShapeView.change();
                        }
                    });
                }
            }).start();
        }
    }

}
