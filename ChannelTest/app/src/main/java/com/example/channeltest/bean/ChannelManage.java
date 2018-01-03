package com.example.channeltest.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ggz on 2017/12/27.
 */

public class ChannelManage {

    //默认的用户选择频道列表
    public static List<ChannelItem> defaultUserChannels;
    // 默认的其他频道列表
    public static List<ChannelItem> defaultOtherChannels;

    // 判断数据库中是否存在用户数据
    private boolean userExist = false;

    static {
        defaultUserChannels = new ArrayList<ChannelItem>();
        defaultOtherChannels = new ArrayList<ChannelItem>();

        defaultUserChannels.add(new ChannelItem(1, "1", 1, true));
        defaultUserChannels.add(new ChannelItem(2, "2", 2, true));
        defaultUserChannels.add(new ChannelItem(3, "3", 3, true));
        defaultUserChannels.add(new ChannelItem(4, "4", 4, true));
        defaultUserChannels.add(new ChannelItem(5, "5", 5, true));
        defaultUserChannels.add(new ChannelItem(6, "6", 6, true));
        defaultUserChannels.add(new ChannelItem(7, "7", 7, true));
//        defaultUserChannels.add(new ChannelItem(1, "推荐", 1, true));
//        defaultUserChannels.add(new ChannelItem(2, "热点", 2, true));
//        defaultUserChannels.add(new ChannelItem(3, "社会", 3, true));
//        defaultUserChannels.add(new ChannelItem(4, "数码", 4, true));
//        defaultUserChannels.add(new ChannelItem(5, "游戏", 5, true));
//        defaultUserChannels.add(new ChannelItem(6, "体育", 6, true));
//        defaultUserChannels.add(new ChannelItem(7, "美女", 7, true));
        defaultOtherChannels.add(new ChannelItem(8, "财经", 1, false));
        defaultOtherChannels.add(new ChannelItem(9, "汽车", 2, false));
        defaultOtherChannels.add(new ChannelItem(10, "房产", 3, false));
        defaultOtherChannels.add(new ChannelItem(11, "时尚", 4, false));
        defaultOtherChannels.add(new ChannelItem(12, "情感", 5, false));
        defaultOtherChannels.add(new ChannelItem(13, "女人", 6, false));
        defaultOtherChannels.add(new ChannelItem(14, "旅游", 7, false));
        defaultOtherChannels.add(new ChannelItem(15, "健康", 8, false));
        defaultOtherChannels.add(new ChannelItem(16, "电影", 9, false));
        defaultOtherChannels.add(new ChannelItem(17, "科技", 10, false));
        defaultOtherChannels.add(new ChannelItem(18, "娱乐", 11, false));
    }



    /**
     * 获取用户的频道
     * @return 数据库存在用户配置 ? 数据库内的用户选择频道 : 默认用户选择频道 ;
     */
    public static List<ChannelItem> getUserChannel(){
        return defaultUserChannels;
    }

    public static List<ChannelItem> getOtherChannel(){
        return defaultOtherChannels;
    }

}
