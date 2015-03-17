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
import com.curson.guessmusic.data.Constants;
import com.curson.guessmusic.model.IWordButtonClickListener;
import com.curson.guessmusic.model.Song;
import com.curson.guessmusic.model.ViewHolder;
import com.curson.guessmusic.uitl.MyLog;
import com.curson.guessmusic.view.MyGridView;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends ActionBarActivity implements IWordButtonClickListener {

    //当前关的索引
    private int mCurrentStageIndex = -1;

    //当前的歌曲
    private Song mCurrentSong;

    private static final String TAG = "MainActivity";

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

        //注册监听
        mGridView.registOnWordButtonClick(this);
    }

    @Override
    public void onWordButtonClick(ViewHolder button) {
        setSelectWord(button);
    }

    /**
     * 清除已选文字
     *
     * @param button
     */
    private void clearTheAnswer(ViewHolder button) {
        //清除已选框文字
        button.mViewButton.setText("");
        button.mContent = "";
        button.mIsVisiable = false;

        //设置待选框的可见性
        setButtonVisiable(mAllWords.get(button.mIndex), View.VISIBLE);
    }

    /**
     * 设置答案
     *
     * @param button
     */
    private void setSelectWord(ViewHolder button) {
        for (int i = 0; i < mSelectWordsBtn.size(); i++) {
            if (mSelectWordsBtn.get(i).mContent.length() == 0) {
                //设置答案文字框内容及可见性
                mSelectWordsBtn.get(i).mViewButton.setText(button.mContent);
                mSelectWordsBtn.get(i).mIsVisiable = true;
                mSelectWordsBtn.get(i).mContent = button.mContent;
                //记录索引
                mSelectWordsBtn.get(i).mIndex = button.mIndex;

                //Log.....
                MyLog.d(TAG, String.valueOf(mSelectWordsBtn.get(i).mIndex));

                //设置待选框可见性
                setButtonVisiable(button, View.INVISIBLE);

                break;
            }
        }
    }

    /**
     * 设置待选文字框是否可见
     *
     * @param button
     * @param visibility
     */
    private void setButtonVisiable(ViewHolder button, int visibility) {
        button.mViewButton.setVisibility(visibility);
        button.mIsVisiable = (visibility == View.VISIBLE) ? true : false;

        //Log
        MyLog.d(TAG, String.valueOf(button.mIsVisiable));
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
     * 读取当前关的歌曲信息
     *
     * @param stageIndex
     * @return
     */
    private Song readStageSongInfo(int stageIndex) {
        Song song = new Song();
        String[] stage = Constants.SONG_INFO[stageIndex];

        //因为是二维数组,0是歌曲,1是歌曲名字
        song.setSongFileName(stage[Constants.INDEX_FILE_NAME]);
        song.setSongName(stage[Constants.INDEX_SONG_NAME]);
        return song;
    }

    /**
     * 初始化数据
     */
    private void initCurrentStageData() {
        //读取当前关的歌曲信息
        mCurrentSong = readStageSongInfo(++mCurrentStageIndex);
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
        String[] words = generateWords();

        for (int i = 0; i < Constants.COUNTS_WORDS; i++) {
            ViewHolder button = new ViewHolder();
            button.mContent = words[i];
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
        for (int i = 0; i < mCurrentSong.getSongNameLength(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.self_ui_gridview_item, null);
            final ViewHolder button = new ViewHolder();
            button.mViewButton = (Button) view.findViewById(R.id.item_btn);
            button.mViewButton.setTextColor(Color.WHITE);
            button.mViewButton.setText("");
            button.mIsVisiable = false;
            button.mViewButton.setBackgroundResource(R.mipmap.game_wordblank);
            button.mViewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearTheAnswer(button);
                }
            });
            data.add(button);
        }
        return data;
    }

    /**
     * 生成所有的待选文字
     *
     * @return
     */
    private String[] generateWords() {

        Random random = new Random();

        String[] words = new String[Constants.COUNTS_WORDS];

        //存入歌名
        for (int i = 0; i < mCurrentSong.getSongNameLength(); i++) {
            words[i] = String.valueOf(mCurrentSong.getNameCharacters()[i]);
        }

        //获取随机文字并存入数组
        for (int i = mCurrentSong.getSongNameLength(); i < Constants.COUNTS_WORDS; i++) {
            words[i] = String.valueOf(getRandomChat());
        }

        //打乱文字数序:首先从所有元素中随机选取一个,与第一个元素进行交换
        //然后在第二个之后,选择一个元素与第二个交换,直到最后一个元素
        //这样能够确保每隔元素在每隔位置的概率都是1/n
        for (int i = Constants.COUNTS_WORDS - 1; i >= 0; i--) {
            int index = random.nextInt(i + 1);
            String buf = words[index];
            words[index] = words[i];
            words[i] = buf;
        }

        return words;
    }

    /**
     * 生成随机汉字
     *
     * @return
     */
    private char getRandomChat() {
        String str = "";
        int hightPos;
        int lowPos;

        Random random = new Random();
        hightPos = (176 + Math.abs(random.nextInt(39)));
        lowPos = (161 + Math.abs(random.nextInt(93)));

        byte[] b = new byte[2];
        b[0] = (Integer.valueOf(hightPos)).byteValue();
        b[1] = (Integer.valueOf(lowPos)).byteValue();

        try {
            str = new String(b, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str.charAt(0);
    }

    @Override
    protected void onPause() {
        //停止动画
        mViewPan.clearAnimation();
        super.onPause();
    }
}
