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
import android.widget.TextView;

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
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity implements IWordButtonClickListener {

    //当前歌曲名称
    private TextView mCurrentSongNamePassView;

    //当前关卡的索引
    private TextView mCurrentStagePassView;

    //金币View
    private TextView mViewCurrentCoins;

    //当前金币的数量
    private int mCurrentCoins = Constants.TOTAL_COINS;

    //过关界面
    private View mPassView;

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

        //初始化游戏数据
        initCurrentStageData();

        //处理删除按键事件
        handleDeleteWord();

        //处理提示按键事件
        handleTipAnswer();
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
        mViewCurrentCoins = (TextView) findViewById(R.id.txt_bar_coins);
        mViewCurrentCoins.setText(String.valueOf(mCurrentCoins));

        //注册监听
        mGridView.registOnWordButtonClick(this);
    }

    @Override
    public void onWordButtonClick(ViewHolder button) {
        setSelectWord(button);

        //获得答案状态
        int checkResult = checkTheAnswer();

        //检查答案
        if (checkResult == Constants.STATUS_ANSWER_RIGHT) {
            //过关并获得响应奖励
            handlePassEvent();
        } else if (checkResult == Constants.STATUS_ANSWER_WRONG) {
            //错误提示并闪烁文字
            sparkTheWrods();
        } else if (checkResult == Constants.STATUE_ANSWER_LACK) {
            //设置文字为白色(Normal)
            for (int i = 0; i < mSelectWordsBtn.size(); i++) {
                mSelectWordsBtn.get(i).mViewButton.setTextColor(Color.WHITE);
            }
        }
    }

    /**
     * 处理过关界面及事件
     */
    private void handlePassEvent() {
        //显示过关界面
        mPassView = findViewById(R.id.pass_view);
        mPassView.setVisibility(View.VISIBLE);

        //停止未完成的动画
        mViewPan.clearAnimation();

        //当前关卡的索引
        mCurrentStagePassView = (TextView) findViewById(R.id.text_current_stage_pass);
        if (mCurrentStagePassView != null) {
            mCurrentStagePassView.setText(String.valueOf(mCurrentStageIndex + 1));
        }

        //显示歌曲名称
        mCurrentSongNamePassView = (TextView) findViewById(R.id.text_current_song_name_pass);
        if (mCurrentSongNamePassView != null) {
            mCurrentSongNamePassView.setText(mCurrentSong.getSongName());
        }
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
     * 初始化游戏数据
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

    /**
     * 检查答案
     *
     * @return
     */
    private int checkTheAnswer() {
        //先检查长度
        for (int i = 0; i < mSelectWordsBtn.size(); i++) {
            //如果有空的,说明答案还不完整
            if (mSelectWordsBtn.get(i).mContent.length() == 0) {
                return Constants.STATUE_ANSWER_LACK;
            }
        }

        //答案完整,继续检查正确性
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mSelectWordsBtn.size(); i++) {
            sb.append(mSelectWordsBtn.get(i).mContent);
        }
        return (sb.toString().equals(mCurrentSong.getSongName())) ?
                Constants.STATUS_ANSWER_RIGHT : Constants.STATUS_ANSWER_WRONG;
    }

    /**
     * 闪烁文字
     */
    private void sparkTheWrods() {
        //定时器相关
        TimerTask task = new TimerTask() {
            //文字状态
            boolean mChange = false;
            //记录闪烁的次数
            int mSparkTimes = 0;

            @Override
            public void run() {
                //主线程更新
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (++mSparkTimes > Constants.SPASH_TIMES) {
                            return;
                        }
                        //执行闪烁逻辑:交替显示红色和白色文字
                        for (int i = 0; i < mSelectWordsBtn.size(); i++) {
                            //mChange为true,闪烁红色,false闪烁白色
                            mSelectWordsBtn.get(i).mViewButton.setTextColor(mChange ? Color.RED : Color.WHITE);
                        }
                        mChange = !mChange;
                    }
                });
            }
        };
        Timer timer = new Timer();
        //执行定时器
        timer.schedule(task, 1, 150);
    }

    /**
     * 提示一个文字
     */
    private void tipAnswer() {
        boolean tipWord = false;
        for (int i = 0; i < mSelectWordsBtn.size(); i++) {
            if (mSelectWordsBtn.get(i).mContent.length() == 0) {
                //根据当前答案框的条件选择对应的文字并填入
                onWordButtonClick(findIsAnswerWord(i));
                tipWord = true;
                //减少金币
                if (!handleCoins(-getTipWordCoins())) {
                    //金币不够,显示对话框
                    return;
                }
                break;
            }
        }
        //没有找到可以填充的答案
        if (!tipWord) {
            //闪烁文字提示用户
            sparkTheWrods();
        }


    }

    /**
     * 删除一个文字
     */
    private void deleteOneWord() {
        //减少金币
        if (!handleCoins(-getDeleteWordCoins())) {
            //金币不够,显示对话框
            return;
        }
        //将这个索引对应的WordButton设置为不可见
        setButtonVisiable(findNotAnswerWord(), View.INVISIBLE);
    }

    /**
     * 找到一个不是答案的文字,并且当前是可见的
     *
     * @return
     */
    private ViewHolder findNotAnswerWord() {
        Random random = new Random();
        ViewHolder words;
        while (true) {
            int index = random.nextInt(Constants.COUNTS_WORDS);
            words = mAllWords.get(index);
            //如果不隐藏,并且不是答案
            if (words.mIsVisiable && !isTheAnswerWord(words)) {
                return words;
            }
        }
    }

    /**
     * 找到一个是答案的文字
     *
     * @param index 当前需要填入答案框的索引
     * @return
     */
    private ViewHolder findIsAnswerWord(int index) {
        ViewHolder words;
        for (int i = 0; i < Constants.COUNTS_WORDS; i++) {
            words = mAllWords.get(i);
            if (words.mContent.equals(String.valueOf(mCurrentSong.getNameCharacters()[index]))) {
                return words;
            }
        }
        return null;
    }

    /**
     * 判断某个文字是否为答案
     *
     * @param word
     * @return
     */
    private boolean isTheAnswerWord(ViewHolder word) {
        boolean result = false;
        for (int i = 0; i < mCurrentSong.getSongNameLength(); i++) {
            if (word.mContent.equals(String.valueOf(mCurrentSong.getNameCharacters()[i]))) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * 增加或减少指定数量的金币
     *
     * @param data
     * @return true 增加/减少成功, false 失败
     */
    private boolean handleCoins(int data) {
        //判断当前总的金币数量是否可被减少
        if (mCurrentCoins + data >= 0) {
            //可以减少
            mCurrentCoins += data;
            mViewCurrentCoins.setText(String.valueOf(mCurrentCoins));
            return true;
        } else {
            //金币不够
            return false;
        }
    }

    /**
     * 从配置文件中获取删除需要的金币数量
     *
     * @return
     */
    private int getDeleteWordCoins() {
        return this.getResources().getInteger(R.integer.pay_delete_word);
    }

    /**
     * 从配置文件中获取提示需要的金币数量
     *
     * @return
     */
    private int getTipWordCoins() {
        return this.getResources().getInteger(R.integer.pay_tip_answer);
    }

    /**
     * 处理删除待选文字事件
     */
    private void handleDeleteWord() {
        ImageButton button = (ImageButton) findViewById(R.id.btn_delete_word);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteOneWord();
            }
        });
    }

    /**
     * 处理提示按键事件
     */
    private void handleTipAnswer() {
        ImageButton button = (ImageButton) findViewById(R.id.btn_tip_answer);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipAnswer();
            }
        });
    }

    @Override
    protected void onPause() {
        //停止动画
        mViewPan.clearAnimation();
        super.onPause();
    }
}
