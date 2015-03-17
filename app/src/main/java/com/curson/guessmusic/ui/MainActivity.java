package com.curson.guessmusic.ui;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.curson.guessmusic.R;
import com.curson.guessmusic.model.Contacts;
import com.curson.guessmusic.model.ViewHolder;
import com.curson.guessmusic.view.MyGridView;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private MyGridView mGridView;

    //已选择文字框UI容器
    private LinearLayout mViewWordsContainer;

    //已选择文字
    private ArrayList<ViewHolder> mSelectWordsBtn;

    //文字框容器
    private ArrayList<ViewHolder> mAllWords;

    //标记动画是否处于运行当中
    private boolean mIsRunning = false;

    //play 按键事件
    private ImageButton mBtnPlayStart;

    //盘片
    private ImageView mViewPan;

    //盘片相关动画
    private Animation mPanAnim;
    private LinearInterpolator mPanLin;

    //拨杆
    private ImageView mViewPanBar;

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

        initView();
        initAnimation();
        initListener();
        initCurrentStageData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mViewPan = (ImageView) findViewById(R.id.mViewPan);
        mViewPanBar = (ImageView) findViewById(R.id.mViewPanBar);
        mBtnPlayStart = (ImageButton) findViewById(R.id.btn_play_start);
        mGridView = (MyGridView) findViewById(R.id.gridview);
        mViewWordsContainer = (LinearLayout) findViewById(R.id.word_select_container);
    }

    /**
     * 处理圆盘中间的播放按钮,就是开始播放音乐
     */
    private void handlePlayButton() {
        if (mViewPanBar != null) {
            if (!mIsRunning) {
                //如果动画没有处于运行状态,拨杆开始执行动画
                mIsRunning = true;
                mViewPanBar.startAnimation(mBarInAnim);
                mBtnPlayStart.setVisibility(View.INVISIBLE);//播放按钮隐藏
            }
        }
    }

    /**
     * 初始化事件
     */
    private void initListener() {
        //播放监听事件
        mBtnPlayStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //播放音乐
                handlePlayButton();
            }
        });

        //盘片动画监听
        mPanAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //盘片动画执行完毕之后,拨杆回到原来位置
                mViewPanBar.startAnimation(mBarOutAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //拨杆进去动画监听
        mBarInAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //拨杆进去之后,开始执行盘片动画
                mViewPan.startAnimation(mPanAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //拨杆出来动画监听
        mBarOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //整套动画播放完毕
                mIsRunning = false;
                mBtnPlayStart.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 初始化盘片和拨杆的动画
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
        mBarInAnim.setFillAfter(true);//设置为动画结束之后的状态


        //拨杆 出来
        mBarOutAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_d_45);
        mBarOutLin = new LinearInterpolator();
        mBarOutAnim.setInterpolator(mBarOutLin);
        mBarOutAnim.setFillAfter(true);//设置为动画结束之后的状态
    }

    /**
     * 初始化数据
     */
    private void initCurrentStageData() {
        //初始化已选择框
        mSelectWordsBtn = initSelectWord();
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(140, 140);
        for (int i = 0; i < mSelectWordsBtn.size(); i++) {
            mViewWordsContainer.addView(mSelectWordsBtn.get(i).mViewButton, params);
        }
        //获取数据
        mAllWords = initAllWord();
        //更新数据
        mGridView.updateData(mAllWords);
    }

    /**
     * 初始化待选文字框
     *
     * @return
     */
    private ArrayList<ViewHolder> initAllWord() {
        ArrayList<ViewHolder> data = new ArrayList<>();
        //获得所有待选文字
        for (int i = 0; i < Contacts.COUNTS_WORDS; i++) {
            ViewHolder button = new ViewHolder();
            button.mContent = "哈";
            data.add(button);
        }
        return data;
    }

    /**
     * 初始化已选择文字框
     *
     * @return
     */
    private ArrayList<ViewHolder> initSelectWord() {
        ArrayList<ViewHolder> data = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.self_ui_gridview_item, null);
            ViewHolder button = new ViewHolder();
            button.mViewButton = (Button) view.findViewById(R.id.item_btn);
            button.mViewButton.setTextColor(Color.WHITE);
            button.mViewButton.setText("");
            button.mIsVisiable = false;
            button.mViewButton.setBackgroundResource(R.mipmap.game_wordblank);
            data.add(button);
        }
        return data;
    }

    @Override
    protected void onPause() {
        //停止动画
        mViewPan.clearAnimation();
        super.onPause();
    }
}
