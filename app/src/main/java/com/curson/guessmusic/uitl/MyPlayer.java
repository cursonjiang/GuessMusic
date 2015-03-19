package com.curson.guessmusic.uitl;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;

import com.curson.guessmusic.data.Constants;

import java.io.IOException;

public class MyPlayer {

    //音效
    private static MediaPlayer[] mToneMediaPlayer = new MediaPlayer[Constants.SONG_NAMES.length];

    private static MediaPlayer mMusicMediaPlayer;

    /**
     * 播放音效
     *
     * @param context
     * @param index
     */
    public static void playTone(Context context, int index) {
        AssetManager assetManager = context.getAssets();
        if (mToneMediaPlayer[index] == null) {
            mToneMediaPlayer[index] = new MediaPlayer();
            try {
                AssetFileDescriptor fileDescriptor = assetManager.openFd(Constants.SONG_NAMES[index]);
                mToneMediaPlayer[index].setDataSource(
                        fileDescriptor.getFileDescriptor(),
                        fileDescriptor.getStartOffset(),
                        fileDescriptor.getLength());
                mToneMediaPlayer[index].prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mToneMediaPlayer[index].start();
    }

    /**
     * 播放歌曲
     *
     * @param context
     * @param fileName
     */
    public static void PlaySong(Context context, String fileName) {
        if (mMusicMediaPlayer == null) {
            mMusicMediaPlayer = new MediaPlayer();
        }
        //强制重置
        mMusicMediaPlayer.reset();

        //加载声音文件
        AssetManager assetManager = context.getAssets();
        try {
            AssetFileDescriptor fileDescriptor = assetManager.openFd(fileName);
            mMusicMediaPlayer.setDataSource(
                    fileDescriptor.getFileDescriptor(),
                    fileDescriptor.getStartOffset(),
                    fileDescriptor.getLength());
            mMusicMediaPlayer.prepare();
            mMusicMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 暂停音乐
     *
     * @param context
     */
    public static void StopSong(Context context) {
        if (mMusicMediaPlayer != null) {
            mMusicMediaPlayer.stop();
        }
    }
}
