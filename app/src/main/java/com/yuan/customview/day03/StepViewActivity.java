package com.yuan.customview.day03;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yuan.customview.R;

/**
 * @author XYuan
 * Version  1.0
 * Description
 */
public class StepViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stepview);
        final StepView mStepView = findViewById(R.id.custom_sv);
        mStepView.setProgressMax(5000);
        // 属性动画
        ValueAnimator animator = ObjectAnimator.ofFloat(0, 3413);
        animator.setDuration(1500);
        // 设置插值器 速度由快变慢
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentProgress = (Float) animation.getAnimatedValue();
                mStepView.setCurrentProgress((int) currentProgress);
            }
        });
        animator.start();
    }
}
