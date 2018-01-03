package com.example.channeltest.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.channeltest.R;
import com.example.channeltest.adapter.DragAdapter;
import com.example.channeltest.until.DisplayUtil;

/**
 * Created by ggz on 2017/12/27.
 */

public class DragGridView extends GridView {
    private static final String TAG = "DragGridView";

    /** 点击时候的X位置 */
    public int downX;
    /** 点击时候的Y位置 */
    public int downY;
    /** 点击时候对应整个界面的X位置 */
    public int windowX;
    /** 点击时候对应整个界面的Y位置 */
    public int windowY;
    /** 屏幕上的X */
    private int win_view_x;
    /** 屏幕上的Y*/
    private int win_view_y;
    /** 拖动的里x的距离  */
    int dragOffsetX;
    /** 拖动的里Y的距离  */
    int dragOffsetY;
    /** 长按时候对应postion */
    public int dragPosition;
    /** Up后对应的ITEM的Position */
    private int dropPosition;
    /** 开始拖动的ITEM的Position*/
    private int startPosition;
    /** item高 */
    private int itemHeight;
    /** item宽 */
    private int itemWidth;
    /** 拖动的时候对应ITEM的VIEW */
    private View dragImageView = null;
    /** 长按的时候ITEM的VIEW*/
    private ViewGroup dragItemView = null;
    /** WindowManager管理器 */
    private WindowManager windowManager = null;
    /** */
    private WindowManager.LayoutParams windowParams = null;
    /** item总量*/
    private int itemTotalCount;
    /** 一行的ITEM数量*/
    private int nColumns = 4;
    /** 行数 */
    private int nRows;
    /** 剩余部分 */
    private int remainder;
    /** 是否在移动 */
    private boolean isMoving = false;
    /** */
    private int holdPosition;
    /** 拖动的时候放大的倍数 */
    private double dragScale = 1.08D;
//    /** 震动器  */
//    private Vibrator mVibrator;
    /** 每个ITEM之间的水平间距 */
    private int mHorizontalSpacing = 10;
    /** 每个ITEM之间的竖直间距 */
    private int mVerticalSpacing = 10;
    /* 移动时候最后个动画的ID */
    private String LastAnimationID;


    /** DragGrid 构造函数 */
    public DragGridView(Context context) {
        super(context);
        init(context);
    }

    public DragGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
//        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        //将布局文件中设置的间距dip转为px
        mHorizontalSpacing = DisplayUtil.dip2px(context, mHorizontalSpacing);
        mVerticalSpacing = DisplayUtil.dip2px(context, mVerticalSpacing);
    }

    /** View 的测量 */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // TouchDown ，获取 点击位置 downX 和 对应整个界面的位置 windowX
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            downX = (int) ev.getX();
            downY = (int) ev.getY();
            windowX = (int) ev.getX();
            windowY = (int) ev.getY();

            setOnItemClickListener(ev);
        }
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        boolean bool = true;
        if (dragImageView != null && dragPosition != AdapterView.INVALID_POSITION){

            // 移动时候的对应x,y位置
            bool = super.onTouchEvent(ev);
            int x = (int) ev.getX();    // x == windowX
            int y = (int) ev.getY();
//            Log.i(TAG, " x " + x + " windowX " + windowX + " rawX " + ev.getRawX());

            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = (int) ev.getX();
                    downY = (int) ev.getY();
                    windowX = (int) ev.getX();
                    windowY = (int) ev.getY();
                    break;

                case MotionEvent.ACTION_MOVE:
                    onDrag(x, y ,(int) ev.getRawX() , (int) ev.getRawY());
                    if (!isMoving){
                        OnMove(x, y);
                    }
                    if (pointToPosition(x, y) != AdapterView.INVALID_POSITION){
                        break;
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    stopDrag();
                    onDrop(x,y);
                    requestDisallowInterceptTouchEvent(false);//是否禁用事件拦截功能
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }


    /**
     * 长按点击监听
     */
    public void setOnItemClickListener(final MotionEvent ev) {

        setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                int x = (int) ev.getX();// x == rawx
                int y = (int) ev.getY();

                startPosition = position;// 第一次点击的postion
                dragPosition = position;
                if (startPosition < 1) {    // 第一个 item0 不可长按
                    return false;
                }

                // 获取点击的 item
                ViewGroup dragViewGroup = (ViewGroup) getChildAt(dragPosition - getFirstVisiblePosition());// 可见的item
                TextView dragTextView = (TextView)dragViewGroup.findViewById(R.id.gv_item_tv);

//                dragTextView.setSelected(true);
//                dragTextView.setEnabled(false);

                itemHeight = dragViewGroup.getHeight();     // 获取 item 的宽高
                itemWidth = dragViewGroup.getWidth();
                itemTotalCount = DragGridView.this.getCount();  // 获取 item 的数量

                // 算出行数
                int row = itemTotalCount / nColumns;
                remainder = (itemTotalCount % nColumns);    // 算出最后一行多余的数量
                if (remainder != 0) {
                    nRows = row + 1;
                } else {
                    nRows = row;
                }

                // 如果特殊的这个不等于拖动的那个,并且不等于-1
                if (dragPosition != AdapterView.INVALID_POSITION) {

                    // 获取相对于dragViewGroup的距离
                    win_view_x = windowX - dragViewGroup.getLeft();//VIEW相对自己的X
                    win_view_y = windowY - dragViewGroup.getTop();//VIEW相对自己的y

                    // 偏移量？结果：dragOffsetX == dragOffsetY == 0
//                    dragOffsetX = (int) (ev.getRawX() - x);
//                    dragOffsetY = (int) (ev.getRawY() - y);
//                    Log.i(TAG, " dragOffsetX = " + dragOffsetX);

                    dragItemView = dragViewGroup;

                    // DrawingCache
                    dragViewGroup.destroyDrawingCache();
                    dragViewGroup.setDrawingCacheEnabled(true);
                    Bitmap dragBitmap = Bitmap.createBitmap(dragViewGroup.getDrawingCache());

//                    mVibrator.vibrate(50);//设置震动时间

                    startDrag(dragBitmap, (int)ev.getRawX(),  (int)ev.getRawY());
                    hideDropItem();     // 隐藏底下的ITEM

                    dragViewGroup.setVisibility(View.INVISIBLE);

                    isMoving = false;
                    requestDisallowInterceptTouchEvent(true);

                    return true;
                }
                return false;
            }
        });
    }

    /** 创建拖拽界面 dragImageView */
    public void startDrag(Bitmap dragBitmap, int rawx, int rawy) {

        stopDrag();

        windowParams = new WindowManager.LayoutParams();// 获取WINDOW界面的
        // Gravity.TOP|Gravity.LEFT;这个必须加
        windowParams.gravity = Gravity.TOP | Gravity.LEFT;
        // 效果一：中间位置
//		windowParams.x = rawx - (int)((itemWidth / 2) * dragScale);
//		windowParams.y = rawy - (int) ((itemHeight / 2) * dragScale);
        // 效果二：左上角位置
        windowParams.x = rawx - win_view_x;
        windowParams.y = rawy  - win_view_y;
        // 效果三：？？？
//		this.windowParams.x = (rawx - this.win_view_x + this.viewX);//位置的x值
//		this.windowParams.y = (rawy - this.win_view_y + this.viewY);//位置的y值
        // 设置拖拽item的宽和高
        windowParams.width = (int) (dragScale * dragBitmap.getWidth());// 放大dragScale倍，可以设置拖动后的倍数
        windowParams.height = (int) (dragScale * dragBitmap.getHeight());// 放大dragScale倍，可以设置拖动后的倍数
        // 设置 Window 属性
        windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        windowParams.format = PixelFormat.TRANSLUCENT;
        windowParams.windowAnimations = 0;

        ImageView iv = new ImageView(getContext());
        iv.setImageBitmap(dragBitmap);
        dragImageView = iv;

        windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);// "window"
        windowManager.addView(dragImageView, windowParams);

    }

    /** 停止拖动 ，释放并初始化 */
    private void stopDrag() {
        if (dragImageView != null) {
            windowManager.removeView(dragImageView);
            dragImageView = null;
        }
    }

    /** 隐藏底下的ITEM */
    private void hideDropItem() {
        ((DragAdapter) getAdapter()).setShowDropItem(false);
    }


    /** 在拖动的情况 */
    private void onDrag(int x, int y , int rawx , int rawy) {
        if (dragImageView != null) {
            windowParams.alpha = 0.6f;
//			windowParams.x = rawx - itemWidth / 2;
//			windowParams.y = rawy - itemHeight / 2;
            windowParams.x = rawx - win_view_x;
            windowParams.y = rawy - win_view_y;
            windowManager.updateViewLayout(dragImageView, windowParams);
        }
    }

    /** 在松手下放的情况 */
    private void onDrop(int x, int y) {
        // 根据拖动到的x,y坐标获取拖动位置下方的ITEM对应的POSTION
        int tempPostion = pointToPosition(x, y);
//		if (tempPostion != AdapterView.INVALID_POSITION) {
        dropPosition = tempPostion;
        DragAdapter mDragAdapter = (DragAdapter) getAdapter();
        //显示刚拖动的ITEM
        mDragAdapter.setShowDropItem(true);
        //刷新适配器，让对应的ITEM显示
        mDragAdapter.notifyDataSetChanged();
//		}
    }

    /** 移动的时候触发*/
    public void OnMove(int x, int y) {
        // 拖动的VIEW下方的POSTION
        int dPosition = pointToPosition(x, y);
        // 判断下方的POSTION是否是最开始1个不能拖动的
        if (dPosition > 0) {

            // 如果 dPosition 和原来一样，不做处理
            if ((dPosition == -1) || (dPosition == dragPosition)){
                return;
            }
            dropPosition = dPosition;   // 将要"放下的" postion

            // 计算要移动的 item 数目
            if (dragPosition != startPosition){
                dragPosition = startPosition;
            }
            int movecount;
            // "拖动的" == "开始拖的"，并且"拖动的" != "放下的"
            if ((dragPosition == startPosition) || (dragPosition != dropPosition)){
                // 移需要移动的ITEM数量
                movecount = dropPosition - dragPosition;
            }else{
                // 移需要移动的ITEM数量为0
                movecount = 0;
            }
            if(movecount == 0){
                return;
            }

            int movecount_abs = Math.abs(movecount);

            // dragGroup设置为不可见
            ViewGroup dragGroup = (ViewGroup) getChildAt(dragPosition - getFirstVisiblePosition());
            dragGroup.setVisibility(View.INVISIBLE);

            // 换位计算
            float to_x = 1;     // 当前下方positon
            float to_y;     // 当前下方右边positon
            //x_vlaue移动的距离百分比（相对于自己长度的百分比）
            float x_vlaue = ((float) mHorizontalSpacing / (float) itemWidth) + 1.0f;
            //y_vlaue移动的距离百分比（相对于自己宽度的百分比）
            float y_vlaue = ((float) mVerticalSpacing / (float) itemHeight) + 1.0f;
            Log.i(TAG, "x_vlaue " + x_vlaue + "  y_vlaue " + y_vlaue);

            for (int i = 0; i < movecount_abs; i++) {
                to_x = x_vlaue;
                to_y = y_vlaue;
                //像左
                if (movecount > 0) {
                    // 判断是不是同一行的
                    holdPosition = dragPosition + i + 1;
                    if (dragPosition / nColumns == holdPosition / nColumns) {
                        to_x = - x_vlaue;
                        to_y = 0;
                    } else if (holdPosition % 4 == 0) {
                        to_x = 3 * x_vlaue;
                        to_y = - y_vlaue;
                    } else {
                        to_x = - x_vlaue;
                        to_y = 0;
                    }
                }else{
                    //向右,下移到上，右移到左
                    holdPosition = dragPosition - i - 1;
                    if (dragPosition / nColumns == holdPosition / nColumns) {
                        to_x = x_vlaue;
                        to_y = 0;
                    } else if((holdPosition + 1) % 4 == 0){
                        to_x = -3 * x_vlaue;
                        to_y = y_vlaue;
                    }else{
                        to_x = x_vlaue;
                        to_y = 0;
                    }
                }
                ViewGroup moveViewGroup = (ViewGroup) getChildAt(holdPosition);
                Animation moveAnimation = getMoveAnimation(to_x, to_y);
                moveViewGroup.startAnimation(moveAnimation);
                //如果是最后一个移动的，那么设置他的最后个动画ID为LastAnimationID
                if (holdPosition == dropPosition) {
                    LastAnimationID = moveAnimation.toString();
                }
                moveAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        isMoving = true;
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // 如果为最后个动画结束，那执行下面的方法
                        if (animation.toString().equalsIgnoreCase(LastAnimationID)) {
                            DragAdapter mDragAdapter = (DragAdapter) getAdapter();
                            mDragAdapter.exchange(startPosition,dropPosition);
                            startPosition = dropPosition;
                            dragPosition = dropPosition;
                            isMoving = false;
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        }
    }


    /** 获取移动动画 */
    public Animation getMoveAnimation(float toXValue, float toYValue) {
        TranslateAnimation mTranslateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0F,
                Animation.RELATIVE_TO_SELF,toXValue,
                Animation.RELATIVE_TO_SELF, 0.0F,
                Animation.RELATIVE_TO_SELF, toYValue);// 当前位置移动到指定位置
        mTranslateAnimation.setFillAfter(true);// 设置一个动画效果执行完毕后，View对象保留在终止的位置。
        mTranslateAnimation.setDuration(300L);
        return mTranslateAnimation;
    }
}
