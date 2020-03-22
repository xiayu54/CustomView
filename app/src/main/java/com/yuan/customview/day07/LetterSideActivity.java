package com.yuan.customview.day07;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yuan.customview.R;

/**
 * @author XYuan
 * Version  1.0
 * Description
 */
public class LetterSideActivity extends AppCompatActivity implements LetterSideView.LetterTouchListener {

    private TextView mTvLetter;
    private LetterSideView mLetterSideView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letterside);
        mTvLetter = findViewById(R.id.tv_letter);
        mLetterSideView = findViewById(R.id.custom_letter_side);
        mLetterSideView.setOnTouchLetterListener(this);
    }

    @Override
    public void onTouch(CharSequence letter, boolean isShow) {
        if (isShow){
            mTvLetter.setVisibility(View.VISIBLE);
            mTvLetter.setText(letter);
        }else {
            mTvLetter.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mTvLetter.setVisibility(View.GONE);
                }
            }, 1000);
        }
    }
}
