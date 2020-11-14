package com.jratil.model;

/**
 * @author wenjunjun9
 * @created 2020/9/10 11:21:29
 * @description
 */
public class MvInfo {

    private String mvName;

    private String mvPath;

    public MvInfo() {
    }

    public MvInfo(String mvName, String mvPath) {
        this.mvName = mvName;
        this.mvPath = mvPath;
    }

    public String getMvName() {
        return mvName;
    }

    public void setMvName(String mvName) {
        this.mvName = mvName;
    }

    public String getMvPath() {
        return mvPath;
    }

    public void setMvPath(String mvPath) {
        this.mvPath = mvPath;
    }
}
