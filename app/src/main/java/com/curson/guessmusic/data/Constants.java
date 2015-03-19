package com.curson.guessmusic.data;

/**
 * 常量
 * Created by Curson on 15/3/17.
 */
public class Constants {

    /**
     * 关卡的数据
     */
    public static final int INDEX_LOAD_DATA_STAGE = 0;

    /**
     * 金币的数据
     */
    public static final int INDEX_LOAD_DATA_COINS = 1;

    /**
     * 保存数据文件名
     */
    public static final String FILE_NAME_SAVE_DATA = "data.dat";

    /**
     * 音乐索引
     */
    public final static int INDEX_STONE_ENTER = 0;
    public final static int INDEX_STONE_CANCEL = 1;
    public final static int INDEX_STONE_COIN = 2;

    //音效文件名称
    public final static String[] SONG_NAMES = {"enter.mp3", "cancel.mp3", "coin.mp3"};

    public static final int ID_DIALOG_DELETE_WORD = 1;

    public static final int ID_DIALOG_TIP_ANSWER = 2;

    public static final int ID_DIALOG_LACK_COINS = 3;

    /**
     * 金币总数
     */
    public static final int TOTAL_COINS = 1000;

    /**
     * 闪烁次数
     */
    public static final int SPASH_TIMES = 6;

    /**
     * 答案正确
     */
    public static final int STATUS_ANSWER_RIGHT = 1;

    /**
     * 答案错误
     */
    public static final int STATUS_ANSWER_WRONG = 2;

    /**
     * 答案不完整
     */
    public static final int STATUE_ANSWER_LACK = 3;

    /**
     * 待选文字个数
     */
    public static final int COUNTS_WORDS = 24;

    /**
     * 文件名字
     */
    public static final int INDEX_FILE_NAME = 0;

    /**
     * 歌曲名字
     */
    public static final int INDEX_SONG_NAME = 1;

    /**
     * 歌曲信息
     */
    public static final String SONG_INFO[][] = {
            {"__00000.m4a", "征服"},
            {"__00001.m4a", "童话"},
            {"__00002.m4a", "同桌的你"},
            {"__00003.m4a", "七里香"},
            {"__00004.m4a", "传奇"},
            {"__00005.m4a", "大海"},
            {"__00006.m4a", "后来"},
            {"__00007.m4a", "你的背包"},
            {"__00008.m4a", "再见"},
            {"__00009.m4a", "老男孩"},
            {"__00010.m4a", "龙的传人"}
    };

}
