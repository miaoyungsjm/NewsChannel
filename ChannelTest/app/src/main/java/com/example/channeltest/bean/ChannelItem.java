package com.example.channeltest.bean;

/**
 * Created by ggz on 2017/12/27.
 */

public class ChannelItem {

    // 栏目对应ID
    private int id;
    // 栏目对应NAME
    private String name;
    // 栏目在整体中的排序顺序  rank
    private int orderId;
    // 栏目是否选中
    private boolean selected;

    public ChannelItem() {
    }

    public ChannelItem (int id, String name, int orderId, boolean selected) {
        this.id = id;
        this.name = name;
        this.orderId = orderId;
        this.selected = selected;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getOrderId() {
        return orderId;
    }

    public boolean getSelected() {
        return selected;
    }

    public void setId (int paramInt) {
        id = paramInt;
    }

    public void setName (String paramString) {
        name = paramString;
    }

    public void setOrderId (int paramInt) {
        orderId = paramInt;
    }

    public void setSelected (boolean paramBoolean) {
        selected = paramBoolean;
    }


}
