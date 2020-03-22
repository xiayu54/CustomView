package com.yuan.customview.day04;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yuan.customview.R;

/**
 * @author XYuan
 * Version  1.0
 * Description
 */
public class ColorTrackActivity extends AppCompatActivity {

    private ColorTrackText mColorTeackText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colortrack);
        mColorTeackText = findViewById(R.id.custom_ctt);
    }

    public void leftToRight(View view) {
        // 属性动画
        mColorTeackText.setDirection(ColorTrackText.Direction.LEFT_TO_RIGHT);
        ValueAnimator animator = ObjectAnimator.ofFloat(0, 1);
        animator.setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentProgress = (float) animation.getAnimatedValue();
                Log.e("TAG", currentProgress + "");
                mColorTeackText.setCurrentProgress(currentProgress);
            }
        });
        animator.start();
    }

    public void rightToLeft(View view) {
        // 属性动画
        mColorTeackText.setDirection(ColorTrackText.Direction.RIGHT_TO_RIGHT);
        ValueAnimator animator = ObjectAnimator.ofFloat(0, 1);
        animator.setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentProgress = (float) animation.getAnimatedValue();
                mColorTeackText.setCurrentProgress(currentProgress);
            }
        });
        animator.start();
    }
}
