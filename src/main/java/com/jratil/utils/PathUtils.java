package com.jratil.utils;

import com.jratil.model.MusicInfo;
import com.jratil.model.MvInfo;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author wenjunjun9
 * @created 2020/9/8 15:28:30
 * @description 解决打包后不能从 jar 包获取到 music 文件的问题
 */
public class PathUtils {

    public static List<MvInfo> getMvInfoList(String relativePath) throws IOException {
        if (isJar()) {
            return getMvInfoFromJar(relativePath);
        }
        return getMvInfoFromLocal(relativePath);
    }

    private static List<MvInfo> getMvInfoFromJar(String path) throws IOException {
        List<MvInfo> mvInfoList = new ArrayList<>();

        Enumeration<JarEntry> entries = getJarEntries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String innerPath = entry.getName();

            if (innerPath.startsWith("mv/") && !innerPath.endsWith("mv/")) {
                String mvName = innerPath.substring(innerPath.indexOf("/") + 1);
                String mvPath = "/mv/" + URLEncoder.encode(mvName, "UTF-8");
                MvInfo mvInfo = new MvInfo(mvName, mvPath);
                mvInfoList.add(mvInfo);
            }
        }
        return mvInfoList;
    }

    private static List<MvInfo> getMvInfoFromLocal(String path) {
        List<MvInfo> mvInfoList = new ArrayList<>();
        File files = new File(PathUtils.class.getResource(path).getPath());
        for (File file : files.listFiles()) {
            String mvName = file.getName();
            String mvPath = file.getAbsolutePath();
            mvPath = mvPath.replaceAll("\\\\", "/");
            mvPath = mvPath.substring(mvPath.indexOf("/mv"));

            MvInfo mvInfo = new MvInfo(mvName, mvPath);
            mvInfoList.add(mvInfo);
        }
        return mvInfoList;
    }

    public static List<MusicInfo> getMusicInfoList(String relativePath) throws IOException {
        if (isJar()) {
            return getMusicInfoFromJar(relativePath);
        }else {
            return getMusicInfoFromLocal(relativePath);
        }
    }

    /**
     * 获取开发时存放的音乐文件
     *
     * @param relativePath 开发时音乐文件的目录
     * @return 获取到的音乐文件包装后的 {@link MusicInfo}
     */
    private static List<MusicInfo> getMusicInfoFromLocal(String relativePath) {
        List<MusicInfo> musicList = new ArrayList<>();
        File files = new File(PathUtils.class.getResource(relativePath).getPath());
        for (File file : files.listFiles()) {
            String musicName = file.getName();
            String musicPath = file.getAbsolutePath();
            musicPath = musicPath.replaceAll("\\\\", "/");
            musicPath = musicPath.substring(musicPath.indexOf("/music"));

            MusicInfo musicInfo = new MusicInfo(musicName, musicPath);
            musicList.add(musicInfo);
        }
        return musicList;
    }

    /**
     *  从 Jar 包获取文件的方法
     * @param relativePath 音乐文件的目录
     * @return
     * @throws IOException
     */
    private static List<MusicInfo> getMusicInfoFromJar(String relativePath) throws IOException {
        List<MusicInfo> musicList = new ArrayList<>();

        Enumeration<JarEntry> entries = getJarEntries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String innerPath = entry.getName();

            if (innerPath.startsWith("music/") && !innerPath.endsWith("music/")) {
                String musicName = innerPath.substring(innerPath.indexOf("/") + 1);
                String musicPath = "/music/" + URLEncoder.encode(musicName, "UTF-8");
                MusicInfo musicInfo = new MusicInfo(musicName, musicPath);
                musicList.add(musicInfo);
            }
        }
        return musicList;
    }

    /**
     * {@link com.jratil.Main} 方法在初始化注入 controller 时使用，获取到 jar 包的 class 文件
     *
     * @return
     * @throws IOException
     */
    public static List<String> getControllerClasses() throws IOException {
        List<String> list = new ArrayList<>();

        Enumeration<JarEntry> entries = getJarEntries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String innerPath = entry.getName();
            if (innerPath.startsWith("com/jratil/controller") && innerPath.endsWith("Controller.class")) {
                innerPath = innerPath.replaceAll("/", ".").replaceAll("\\\\", ".").replace(".class", "");
                list.add(innerPath);
            }
        }
        return list;
    }

    private static Enumeration<JarEntry> getJarEntries() throws IOException {
        String jarPath = getJarPath();
        File file = new File(jarPath);
        JarFile jarFile = new JarFile(file);
        return jarFile.entries();
    }

    /**
     * 如果不是 jar包，会获取到 /target/classes 目录
     * 因此可以通过此判断是否是 jar 还是开发
     * @return
     */
    public static String getJarPath() {
        return PathUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    }

    /**
     * 判断是不是从 Jar 包读取的情况
     * 如果是，则该路径下的不是文件夹
     * @return
     */
    public static boolean isJar() {
        return !(new File(getJarPath()).isDirectory());
    }
}
