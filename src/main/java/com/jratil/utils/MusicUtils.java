package com.jratil.utils;

import javafx.scene.image.Image;
import org.apache.commons.io.IOUtils;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.tag.id3.framebody.FrameBodyAPIC;

import java.io.*;
import java.net.URLDecoder;

/**
 * @author wenjunjun9
 * @created 2020/9/9 13:06:20
 * @description
 */
public class MusicUtils {

    public static Image getMusicCover(String path) throws Exception {

        path = URLDecoder.decode(path, "UTF-8");

        File tempFile = File.createTempFile(path.substring(0, path.indexOf(".mp3")), path.substring(path.lastIndexOf(".")));
        tempFile.deleteOnExit();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(tempFile);
            IOUtils.copy(MusicUtils.class.getResourceAsStream(path), fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                fos.close();
            }
        }

        MP3File mp3File = (MP3File) AudioFileIO.read(tempFile);
        AbstractID3v2Frame frame = (AbstractID3v2Frame) mp3File.getID3v2Tag().getFrame("APIC");
        FrameBodyAPIC body = (FrameBodyAPIC) frame.getBody();
        byte[] imageData = body.getImageData();
        Image image = new Image(new ByteArrayInputStream(imageData));

        return image;
    }
}
