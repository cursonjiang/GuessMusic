package com.curson.guessmusic.model;

/**
 * 歌曲
 * Created by Curson on 15/3/17.
 */
public class Song {
    //歌曲名称
    private String mSongName;

    //歌曲文件名称
    private String mSongFileName;

    //歌曲名字长度
    private int mSongNameLength;

    /**
     * 字符串转换为字符数组
     *
     * @return
     */
    public char[] getNameCharacters() {
        return mSongName.toCharArray();
    }

    public String getSongName() {
        return mSongName;
    }

    public void setSongName(String songName) {
        mSongName = songName;
        this.mSongNameLength = mSongName.length();
    }

    public String getSongFileName() {
        return mSongFileName;
    }

    public void setSongFileName(String songFileName) {
        mSongFileName = songFileName;
    }

    public int getSongNameLength() {
        return mSongNameLength;
    }
}
