package com.curson.guessmusic.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.curson.guessmusic.R;
import com.curson.guessmusic.model.ViewHolder;

import java.util.ArrayList;

/**
 * 自定义GridView
 * Created by Curson on 15/3/16.
 */
public class MyGridView extends GridView {

    private Context mContext;

    private MyGridAdapter mGridAdapter;

    private ArrayList<ViewHolder> mArrayList = new ArrayList<>();

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mGridAdapter = new MyGridAdapter();
        this.setAdapter(mGridAdapter);
    }

    /**
     * 更新数据
     *
     * @param list
     */
    public void updateData(ArrayList<ViewHolder> list) {
        mArrayList = list;
        setAdapter(mGridAdapter);
    }

    class MyGridAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return mArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.self_ui_gridview_item, null);
                holder = mArrayList.get(position);

                holder.mIndex = position;
                holder.mViewButton = (Button) convertView.findViewById(R.id.item_btn);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.mViewButton.setText(holder.mContent);
            return convertView;
        }
    }

}
