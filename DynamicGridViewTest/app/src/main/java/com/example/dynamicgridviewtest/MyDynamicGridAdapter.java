package com.example.dynamicgridviewtest;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.askerov.dynamicgrid.BaseDynamicGridAdapter;

import java.util.List;

/**
 * Created by ggz on 2018/1/3.
 */

public class MyDynamicGridAdapter extends BaseDynamicGridAdapter {

    public MyDynamicGridAdapter(Context context, List<?> items, int columnCount) {
        super(context, items, columnCount);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_grid, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.build(getItem(position).toString());


        Log.d("-----", "name " + getItem(position).toString() +
                " position " + position);


        return convertView;
    }

    private class ViewHolder {
        private TextView titleText;
        private ImageView image;

        private ViewHolder(View view) {
            titleText = (TextView) view.findViewById(R.id.item_title);
            image = (ImageView) view.findViewById(R.id.item_img);
        }

        void build(String title) {
            titleText.setText(title);
            image.setImageResource(R.mipmap.ic_launcher);
        }
    }
}
