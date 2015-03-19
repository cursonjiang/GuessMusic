package com.curson.guessmusic.uitl;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;

import java.io.IOException;

public class MyPlayer {

    private static MediaPlayer mMusicMediaPlayer;

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
