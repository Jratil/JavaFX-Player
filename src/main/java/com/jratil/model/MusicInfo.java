package com.jratil.model;

/**
 * @author wenjunjun9
 * @created 2020/9/5 17:27:15
 * @description
 */
public class MusicInfo {

    String musicName;

    String musicPath;

    public MusicInfo() {
    }

    public MusicInfo(String musicName, String musicPath) {
        this.musicName = musicName;
        this.musicPath = musicPath;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getMusicPath() {
        return musicPath;
    }

    public void setMusicPath(String musicPath) {
        this.musicPath = musicPath;
    }
}
