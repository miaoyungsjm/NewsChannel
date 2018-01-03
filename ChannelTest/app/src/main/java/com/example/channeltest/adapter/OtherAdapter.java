package com.example.channeltest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.channeltest.R;
import com.example.channeltest.bean.ChannelItem;

import java.util.List;

/**
 * Created by ggz on 2017/12/27.
 */

public class OtherAdapter extends BaseAdapter {
    private final static String TAG = "OtherAdapter";

    private Context context;
    private List<ChannelItem> channelList;
    private int layoutId;

    /** TextView 频道内容 */
    private TextView gv_item_tv;



    public OtherAdapter() {
        super();
    }

    public OtherAdapter(Context context, List<ChannelItem> channelList, int resourceId) {
        this.context = context;
        this.channelList = channelList;
        this.layoutId = resourceId;
    }

    @Override
    public int getCount() {
        return channelList != null ? channelList.size() : 0 ;
    }

    @Override
    public ChannelItem getItem(int position) {
        return channelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

//        View view;
//        ViewHolder viewHolder;
//
//        if (convertView == null){
//            view = LayoutInflater.from(context).inflate(resourceId, parent, false);
//
//            viewHolder = new OtherAdapter.ViewHolder();
//            viewHolder.text_item = (TextView) view.findViewById(R.id.gv_item_tv);
//
//            view.setTag(viewHolder);
//
//        } else {
//            view = convertView;     //重获缓冲的 view
//            viewHolder = (OtherAdapter.ViewHolder) view.getTag();    //重获 view 中绑定的控件 ViewHolder
//        }
//
//        ChannelItem channel = getItem(position);
//        viewHolder.text_item.setText(channel.getName());

        View view = LayoutInflater.from(context).inflate(layoutId, null);

        ChannelItem channel = getItem(position);

        gv_item_tv = (TextView) view.findViewById(R.id.gv_item_tv);
        gv_item_tv.setText(channel.getName());



        return view;
    }

//    class ViewHolder {
//        TextView text_item;
//    }

}
