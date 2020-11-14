package com.jratil.controller;

import com.jratil.anotation.Autowired;
import com.jratil.model.MusicInfo;
import com.jratil.utils.PathUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wenjunjun9
 * @created 2020/9/6 10:29:52
 * @description
 */
public class MusicListController {

    public Button playListButton;
    public Button historyButton;
    public Button closeButton;
    public ListView<MusicInfo> playListView;
    public AnchorPane rootPane;

    @Autowired
    private PlayController playController;

    private List<MusicInfo> musicInfoList = new ArrayList<>();

    public List<MusicInfo> getMusicInfoList() {
        return musicInfoList;
    }

    @FXML
    public void initialize() throws IOException {
        initMusicList(null);

        rootPane.setVisible(false);

        playListView.setCellFactory(musicListCell());
        playListView.setOnMouseClicked(musicListListener());

        closeButton.setOnAction((event) -> rootPane.setVisible(false));
    }

    public void toggleShow() {
        rootPane.setVisible(!rootPane.isVisible());
    }

    public void toggleMusicList(List<MusicInfo> list) {
        playListView.getItems().removeAll(musicInfoList);
        this.musicInfoList = list;
        playListView.getItems().addAll(musicInfoList);
    }

    private void initMusicList(List<MusicInfo> list) throws IOException {
        if (list == null || list.size() == 0) {
            musicInfoList = new ArrayList<>();
            musicInfoList = PathUtils.getMusicInfoList("/music");
        }
        playListView.getItems().addAll(musicInfoList);
    }

    private EventHandler<MouseEvent> musicListListener() {
        return event -> {
            if(event.getButton() == MouseButton.PRIMARY) {
                MusicInfo musicInfo = playListView.getSelectionModel().getSelectedItem();
                if (event.getClickCount() == 2) {
                    playController.toggleMusic(musicInfo, true);
                }
            }
        };
    }

    private Callback<ListView<MusicInfo>, ListCell<MusicInfo>> musicListCell() {
        Callback<ListView<MusicInfo>, ListCell<MusicInfo>> cell = TextFieldListCell.forListView(new StringConverter<MusicInfo>() {
            @Override
            public String toString(MusicInfo object) {
                return object.getMusicName();
            }

            @Override
            public MusicInfo fromString(String string) {
                return null;
            }
        });
        return cell;
    }
}
