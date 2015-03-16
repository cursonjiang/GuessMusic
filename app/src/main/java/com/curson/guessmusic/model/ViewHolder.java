package com.curson.guessmusic.model;

import android.widget.Button;

/**
 * 文字按钮
 * Created by Curson on 15/3/16.
 */
public class ViewHolder {

    //索引
    public int mIndex;
    //是否隐藏
    public boolean mIsVisiable;
    //内容
    public String mContent;

    public Button mViewButton;

    public ViewHolder() {
        mIsVisiable = true;
        mContent = "";
    }
}
