package com.example.channeltest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;

import com.example.channeltest.adapter.DragAdapter;
import com.example.channeltest.adapter.OtherAdapter;
import com.example.channeltest.bean.ChannelItem;
import com.example.channeltest.bean.ChannelManage;
import com.example.channeltest.view.DragGridView;
import com.example.channeltest.view.OtherGridView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private static final String TAG = "MainActivity";

    /** 用户栏目的 GRIDVIEW */
    private DragGridView userGridView;
    /** 其它栏目的 GRIDVIEW */
    private OtherGridView otherGridView;
    /** 用户栏目对应的适配器，可以拖动 */
    DragAdapter dragAdapter;
    /** 其它栏目对应的适配器 */
    OtherAdapter otherAdater;
    /** 用户栏目列表 */
    ArrayList<ChannelItem> userChannelList = new ArrayList<ChannelItem>();
    /** 其它栏目列表 */
    ArrayList<ChannelItem> otherChannelList = new ArrayList<ChannelItem>();

    /** 是否在移动，由于这边是动画结束后才进行的数据更替，设置这个限制为了避免操作太频繁造成的数据错乱。 */
    boolean isMove = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.subscribe_activity);

        initView();
        initData();
    }

    /** 初始化数据 */
    private void initData() {
        //获取默认频道
        userChannelList = (ArrayList<ChannelItem>)ChannelManage.getUserChannel();
        otherChannelList = (ArrayList<ChannelItem>)ChannelManage.getOtherChannel();

        dragAdapter = new DragAdapter(this, userChannelList, R.layout.subscribe_gridview_item);
        userGridView.setAdapter(dragAdapter);

        otherAdater = new OtherAdapter(this, otherChannelList, R.layout.subscribe_gridview_item);
        otherGridView.setAdapter(otherAdater);

        //设置 GRIDVIEW 的 ITEM 的点击监听
        userGridView.setOnItemClickListener(this);
        otherGridView.setOnItemClickListener(this);
    }

    private void initView() {
        userGridView = (DragGridView) findViewById(R.id.userGridView);
        otherGridView = (OtherGridView) findViewById(R.id.otherGridView);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //如果点击的时候，之前动画还没结束，那么就让点击事件无效
        if(isMove){
            return;
        }
        switch (parent.getId()) {
            case R.id.userGridView:

                break;

            case R.id.otherGridView:

                break;
        }
    }
}
