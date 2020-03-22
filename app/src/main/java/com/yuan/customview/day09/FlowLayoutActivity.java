package com.yuan.customview.day09;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yuan.customview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author XYuan
 * Version  1.0
 * Description
 */
public class FlowLayoutActivity extends AppCompatActivity {

    private FlowLayout mFlowLayout;
    private List<String> mList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_layout);
        mFlowLayout = findViewById(R.id.custom_flow_layout);
        mList = new ArrayList<>();
        mList.add("HTML颜色代码");
        mList.add("16进制");
        mList.add("三对数字");
        mList.add("红、绿、蓝");
        mList.add("三种基本色");
        mList.add("Web页面");
        mList.add("代码组成");
        mList.add("背景");
        mList.add("黄色系");
        mList.add("文字和表格");
        mList.add("红色系 绿色系");


        mFlowLayout.setAdapter(new FlowAdapter() {
            @Override
            public int getCount() {
                return mList.size();
            }

            @Override
            public View getView(int position, ViewGroup parent) {
                TextView flowTv = (TextView) LayoutInflater.from(FlowLayoutActivity.this)
                        .inflate(R.layout.item_flowlayout, parent, false);
                flowTv.setText(mList.get(position));
                // 这里可以自己去设置点击事件
                return flowTv;
            }
        });



    }
}
