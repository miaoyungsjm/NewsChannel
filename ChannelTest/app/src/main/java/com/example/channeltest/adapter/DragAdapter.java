package com.example.channeltest.adapter;

import android.content.Context;
import android.util.Log;
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

public class DragAdapter extends BaseAdapter {
    private final static String TAG = "DragAdapter";

    private Context context;
    private List<ChannelItem> channelList;
    private int layoutId;

    /** TextView 频道内容 */
    private TextView gv_item_tv;

    /** 是否改变 */
    private boolean isChanged = false;
    /** 是否可见 */
    private boolean isVisible = true;
    /** 是否显示底下的ITEM */
    private boolean isItemShow = false;

    /** 控制的postion */
    private int holdPosition;
    /** 要删除的position */
    private int remove_position = -1;



    public DragAdapter() {
        super();
    }

    public DragAdapter(Context context, List<ChannelItem> channelList, int layoutId) {
        this.context = context;
        this.channelList = channelList;
        this.layoutId = layoutId;
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
//            view = LayoutInflater.from(context).inflate(layoutId, parent, false);
//
//            viewHolder = new ViewHolder();
//            viewHolder.gv_item_tv = (TextView) view.findViewById(R.id.gv_item_tv);
//
//            view.setTag(viewHolder);
//
//        } else {
//            view = convertView;     //重获缓冲的 view
//            viewHolder = (ViewHolder) view.getTag();    //重获 view 中绑定的控件 ViewHolder
//        }

        View view = LayoutInflater.from(context).inflate(layoutId, null);

        ChannelItem channel = getItem(position);

        gv_item_tv = (TextView) view.findViewById(R.id.gv_item_tv);
        gv_item_tv.setText(channel.getName());

        // 第一个item不可操作
        if (position == 0) {
            Log.d(TAG, "------------------------------------");
            gv_item_tv.setEnabled(false);
        }
        //
        if (isChanged && (position == holdPosition) && !isItemShow) {
            gv_item_tv.setText("");
//            gv_item_tv.setSelected(true);
//            gv_item_tv.setEnabled(true);
            isChanged = false;
        }
        //
        if (!isVisible && (position == -1 + channelList.size())) {
            gv_item_tv.setText("");

        }
        //
        if(remove_position == position){
            gv_item_tv.setText("");
        }


        Log.d(TAG, "name " + channel.getName() +
                " orderId " + channel.getOrderId() +
                " position " + position);

        return view;
    }

//    class ViewHolder {
//        TextView gv_item_tv;
//    }

    // ------------------------------------

    /** 添加频道列表 */
    public void addItem(ChannelItem channel) {
        channelList.add(channel);
        notifyDataSetChanged();
    }

    /** 设置删除的position */
    public void setRemove(int position) {
        remove_position = position;
        notifyDataSetChanged();
    }

    /** 拖动变更频道排序 */
    public void exchange(int dragPostion, int dropPostion) {
        holdPosition = dropPostion;
        ChannelItem dragItem = getItem(dragPostion);
        Log.d(TAG, "startPostion = " + dragPostion + "   endPosition = " + dropPostion);
        if (dragPostion < dropPostion) {
            channelList.add(dropPostion + 1, dragItem);
            channelList.remove(dragPostion);
        } else {
            channelList.add(dropPostion, dragItem);
            channelList.remove(dragPostion + 1);
        }
        isChanged = true;
        notifyDataSetChanged();
    }

    /** 设置是否可见 */
    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    /** 设置显示底下的ITEM */
    public void setShowDropItem(boolean show) {
        isItemShow = show;
    }

}
