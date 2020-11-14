package com.jratil.controller;

import com.jratil.anotation.Autowired;
import com.jratil.model.MusicInfo;
import com.jratil.model.MvInfo;
import com.jratil.utils.PathUtils;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wenjunjun9
 * @created 2020/9/4 14:22:43
 * @description
 */
public class LeftController {
    public ScrollPane scrollPane;
    public ListView<MvInfo> mvListView;
    public ListView<MusicInfo> myMusicListView;
    public TitledPane collectionList;
    public TitledPane createList;

    @Autowired
    private PlayController playController;

    @Autowired
    private MusicListController musicListController;

    @FXML
    public void initialize() throws IOException {
        List<MusicInfo> musicList = new ArrayList<>();
        List<MvInfo> mvInfoList = new ArrayList<>();

        musicList = PathUtils.getMusicInfoList("/music");
        mvInfoList = PathUtils.getMvInfoList("/mv");

        myMusicListView.getItems().addAll(musicList);
        myMusicListView.setCellFactory(musicListCell());
        myMusicListView.setOnMouseClicked(musicListListener());

        mvListView.getItems().addAll(mvInfoList);
        mvListView.setCellFactory(mvListCell());
        mvListView.setOnMouseClicked(mvListListener());
    }

    private EventHandler<MouseEvent> mvListListener() {
        return event -> {
            if(event.getButton() == MouseButton.PRIMARY) {
                MvInfo mvInfo = mvListView.getSelectionModel().getSelectedItem();
                if (event.getClickCount() == 2) {
                    playController.toggleMv(mvInfo, true);
                    ObservableList<MvInfo> currentMvList = mvListView.getItems();
//                    musicListController.toggleMList(currentMvList);
                } else {
                    playController.toggleMv(mvInfo, false);
                }
            }
        };
    }

    private EventHandler<MouseEvent> musicListListener() {
        return event -> {
            if(event.getButton() == MouseButton.PRIMARY) {
                MusicInfo musicInfo = myMusicListView.getSelectionModel().getSelectedItem();
                if (event.getClickCount() == 2) {
                    playController.toggleMusic(musicInfo, true);
                    ObservableList<MusicInfo> currentMusicList = myMusicListView.getItems();
                    musicListController.toggleMusicList(currentMusicList);
                } else {
                    playController.toggleMusic(musicInfo, false);
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

    private Callback<ListView<MvInfo>, ListCell<MvInfo>> mvListCell() {
        Callback<ListView<MvInfo>, ListCell<MvInfo>> cell = TextFieldListCell.forListView(new StringConverter<MvInfo>() {
            @Override
            public String toString(MvInfo object) {
                return object.getMvName();
            }

            @Override
            public MvInfo fromString(String string) {
                return null;
            }
        });
        return cell;
    }
}
