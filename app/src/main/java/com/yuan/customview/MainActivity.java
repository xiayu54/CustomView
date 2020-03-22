package com.yuan.customview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;

import com.yuan.customview.day01.TextView;
import com.yuan.customview.day02.TextViewActivity;
import com.yuan.customview.day03.StepViewActivity;
import com.yuan.customview.day04.ColorTrackActivity;
import com.yuan.customview.day05.ProgressBarActivity;
import com.yuan.customview.day06.RatingBarActivity;
import com.yuan.customview.day07.LetterSideActivity;
import com.yuan.customview.day09.FlowLayoutActivity;
import com.yuan.customview.day10.TouchTestActivity;
import com.yuan.customview.day12.SlidingMenuActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = new TextView(this);

    }

    public void jumpView(View view) {
        Intent intent = null;
        switch (view.getId()){
            case R.id.btn_2:
                intent = new Intent(MainActivity.this, TextViewActivity.class);
                break;
            case R.id.btn_3:
                intent = new Intent(MainActivity.this, StepViewActivity.class);
                break;
            case R.id.btn_4:
                intent = new Intent(MainActivity.this, ColorTrackActivity.class);
                break;
            case R.id.btn_5:
                intent = new Intent(MainActivity.this, ProgressBarActivity.class);
                break;
            case R.id.btn_6:
                intent = new Intent(MainActivity.this, RatingBarActivity.class);
                break;
            case R.id.btn_7:
                intent = new Intent(MainActivity.this, LetterSideActivity.class);
                break;
            case R.id.btn_9:
                intent = new Intent(MainActivity.this, FlowLayoutActivity.class);
                break;
            case R.id.btn_10:
                intent = new Intent(MainActivity.this, TouchTestActivity.class);
                break;
            case R.id.btn_12:
                intent = new Intent(MainActivity.this, SlidingMenuActivity.class);
                break;
        }
        startActivity(intent);
    }
}
