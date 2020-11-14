package com.jratil.controller;

import com.jratil.constant.RotateConstant;
import com.jratil.model.MvInfo;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * @author wenjunjun9
 * @created 2020/9/6 13:22:54
 * @description
 */
public class CenterController {

    public ImageView coverImageView;
    public Label lyricLabel;
    public HBox firstBox;
    public AnchorPane secondBox;

    private RotateTransition rotateTransition;

    @FXML
    public void initialize() {
        setCoverImage(new Image("/image/cover/cover.jpg"));
    }

    public void setRotate(double sec) {
        // 根据时间来计算旋转角度
        double byAngle = sec / 0.1;
//        double toAngle = sec / 0.1 + fromAngle;
        rotateTransition = new RotateTransition(Duration.seconds(sec), coverImageView);
        // 选择角度
        rotateTransition.setByAngle(byAngle);
        // 设置匀速旋转
        rotateTransition.setInterpolator(Interpolator.LINEAR);
        // 旋转一次
        rotateTransition.setCycleCount(1);
        rotateTransition.play();
    }

    public void toggleRotate(int status) {
        toggleRotate(status, 0);
    }

    public void toggleRotate(int status, double sec) {
        if (rotateTransition == null) {
            setRotate(sec);
            return;
        }

        switch (status) {
            case RotateConstant.PLAY: {
                rotateTransition.play();
                break;
            }
            case RotateConstant.PAUSE:
            case RotateConstant.STOP: {
                rotateTransition.stop();
                break;
            }
            case RotateConstant.REPLAY: {
                rotateTransition.stop();
                setRotate(sec);
                break;
            }
        }
    }

    public void setLyricLabel(String text) {
        lyricLabel.setText(text);
    }
    public void setCoverImage(Image image) {
        coverImageView.setImage(image);
        // 给图片设置圆角以及阴影
        Rectangle rectangle = new Rectangle(coverImageView.getFitWidth(), coverImageView.getFitHeight());
        rectangle.setArcWidth(coverImageView.getFitWidth());
        rectangle.setArcHeight(coverImageView.getFitHeight());
        coverImageView.setClip(rectangle);

        // snapshot the rounded image.
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        WritableImage newImage = coverImageView.snapshot(parameters, null);
        coverImageView.setClip(null);
        coverImageView.setEffect(new DropShadow(20, Color.BLACK));
        coverImageView.setImage(newImage);
    }

    public void toggleBox(MvInfo mvInfo) {
        if (firstBox.isVisible()) {
            firstBox.setVisible(false);
            secondBox.setVisible(true);
        } else {
            firstBox.setVisible(true);
            secondBox.setVisible(false);
        }
    }
}
