package com.curson.guessmusic.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.curson.guessmusic.R;
import com.curson.guessmusic.model.IWordButtonClickListener;
import com.curson.guessmusic.model.ViewHolder;

import java.util.ArrayList;

/**
 * 自定义GridView
 * Created by Curson on 15/3/16.
 */
public class MyGridView extends GridView {

    private static final String TAG = "MyGridView";

    private IWordButtonClickListener mWordButtonClickListener;

    private Animation mScaleAnimation;

    private ArrayList<ViewHolder> mArrayList = new ArrayList<>();

    private MyGridAdapter mGridAdapter;

    private Context mContext;

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
        //重新设置数据源
        setAdapter(mGridAdapter);
    }


    /**
     * 适配器
     */
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
            final ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.self_ui_gridview_item, null);
                holder = mArrayList.get(position);

                //加载动画
                mScaleAnimation = AnimationUtils.loadAnimation(mContext, R.anim.scale);
                //设置动画的延迟时间
                mScaleAnimation.setStartOffset(position * 100);

                holder.mIndex = position;
                holder.mViewButton = (Button) convertView.findViewById(R.id.item_btn);
                holder.mViewButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mWordButtonClickListener.onWordButtonClick(holder);
                    }
                });
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.mViewButton.setText(holder.mContent);

            //播放动画
            convertView.startAnimation(mScaleAnimation);

            return convertView;
        }
    }

    /**
     * 注册监听接口
     *
     * @param listener
     */
    public void registOnWordButtonClick(IWordButtonClickListener listener) {
        mWordButtonClickListener = listener;
    }

}
