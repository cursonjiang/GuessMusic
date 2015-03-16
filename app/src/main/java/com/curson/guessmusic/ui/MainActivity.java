package com.curson.guessmusic.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import com.curson.guessmusic.R;


public class MainActivity extends ActionBarActivity {

    //唱片相关动画
    private Animation mPanAnim;
    private LinearInterpolator mPanLin;

    //拨杆动画 进去
    private Animation mBarInAnim;
    private LinearInterpolator mBarInLin;

    //拨杆动画 出来
    private Animation mBarOutAnim;
    private LinearInterpolator mBarOutLin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initAnimation();
    }

    /**
     * 初始化动画
     */
    private void initAnimation() {
        //盘片
        mPanAnim = AnimationUtils.loadAnimation(this, R.anim.rotate);
        mPanLin = new LinearInterpolator();
        mPanAnim.setInterpolator(mPanLin);

        //拨杆 进去
        mBarInAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_45);
        mBarInLin = new LinearInterpolator();
        mPanAnim.setInterpolator(mBarInLin);

        //拨杆 出来
        mBarOutAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_d_45);
        mBarOutLin = new LinearInterpolator();
        mBarOutAnim.setInterpolator(mBarOutLin);
    }


}
