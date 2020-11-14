package com.jratil;

import com.jratil.anotation.Autowired;
import com.jratil.controller.CenterController;
import com.jratil.controller.LeftController;
import com.jratil.controller.MusicListController;
import com.jratil.controller.PlayController;
import com.jratil.utils.PathUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author wenjunjun9
 * @created 2020/9/4 14:23:17
 * @description
 */
public class Main extends Application {

    private Map<String, Object> controllerMap = new HashMap<>();

    private static final String scanPackage = "com.jratil.controller";

    private PlayController playController;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        AnchorPane mainPane = FXMLLoader.load(getClass().getResource("/fxml/main_pane.fxml"));

        AnchorPane musicPane = initMusicController();
        AnchorPane playerPane = initPlayerController();
        AnchorPane musicListPane = initMusicListController();
        AnchorPane centerPane = initCenterController();

        mainPane.getChildren().addAll(centerPane, musicPane, playerPane, musicListPane);

        initAutowired();


        Scene scene = new Scene(mainPane);
        JMetro jMetro = new JMetro();
        jMetro.setScene(scene);

        scene.getStylesheets().addAll("/css/slider_style.css", "/css/style.css");

//        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setTitle("Player");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1200);
        primaryStage.setMinHeight(747);
        primaryStage.getIcons().add(new Image("/image/icon.png"));
        primaryStage.show();

        playController.slider.prefWidthProperty().bind(primaryStage.widthProperty().subtract(1040 - 411));
    }

    private AnchorPane initMusicListController() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/music_list_pane.fxml"));
        AnchorPane pane = loader.load();
        MusicListController musicListController = loader.getController();

        saveInstance(musicListController);

        pane.setMaxHeight(500);
        pane.setMinHeight(500);
        pane.setMinWidth(500);
        AnchorPane.setBottomAnchor(pane, 70.0);
        AnchorPane.setTopAnchor(pane, 130.0);
        AnchorPane.setRightAnchor(pane, 0.0);

        return pane;
    }

    private AnchorPane initPlayerController() throws IOException {
        FXMLLoader playLoader = new FXMLLoader(getClass().getResource("/fxml/play_pane.fxml"));
        AnchorPane playerPane = playLoader.load();
        playController = playLoader.getController();

        saveInstance(playController);

        playerPane.setMaxHeight(66);
        playerPane.setMinHeight(70);
        playerPane.setMinWidth(1000);
        AnchorPane.setBottomAnchor(playerPane, 0.0);
        AnchorPane.setLeftAnchor(playerPane, 0.0);
        AnchorPane.setRightAnchor(playerPane, 0.0);

        return playerPane;
    }

    private AnchorPane initMusicController() throws IOException {
        FXMLLoader musicLoader = new FXMLLoader(getClass().getResource("/fxml/left_pane.fxml"));
        AnchorPane musicPane = musicLoader.load();
        LeftController leftController = musicLoader.getController();

        saveInstance(leftController);

        musicPane.setMaxWidth(250.0);
        musicPane.setMaxHeight(630);
        musicPane.setMinWidth(250);
        musicPane.setPrefWidth(250);
        AnchorPane.setBottomAnchor(musicPane, 70.0);
        AnchorPane.setLeftAnchor(musicPane, 0.0);
        AnchorPane.setTopAnchor(musicPane, 0.0);

        return musicPane;
    }

    private AnchorPane initCenterController() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/center_pane.fxml"));
        AnchorPane pane = loader.load();
        CenterController centerController = loader.getController();

        saveInstance(centerController);

        pane.setMinWidth(790);
        pane.setPrefWidth(790);
        pane.setMinHeight(677);
        pane.setPrefHeight(677);
        AnchorPane.setBottomAnchor(pane, 70.0);
        AnchorPane.setLeftAnchor(pane, 250.0);
        AnchorPane.setRightAnchor(pane, 0.0);
        AnchorPane.setTopAnchor(pane, 0.0);

        return pane;
    }

    private void saveInstance(Object o) {
        String name = o.getClass().getName();
        controllerMap.put(name, o);
    }

    /**
     * 自动注入
     * 在 controller 定义要使用的另一个 controller，并且加上 {@link Autowired} 注解
     * 则在启动时会自动解析并且注入对应的已经存在的 controller
     * 前提是在 {@link Main#start(Stage)} 中定义在好所有的布局以及调用{@link Main#saveInstance(Object)} 储存 controller
     * <p>
     * <p>
     * -- 如果使用 fxml 的 <include/> 包含的 controller 的 fxml 文件
     * -- 那么在其他地方通过 {@link FXMLLoader} 获取到的 controller 和自动生成的 controller 不是同一个实例
     * 貌似可以通过 <include/> 引入其他布局文件，然后通过 <code> @FXML Controller controller; </code> 来获取到 controller 实例
     *
     * <br>
     *
     * @throws IllegalAccessException illegalAccessException
     */
    private void initAutowired() throws IllegalAccessException, IOException {

        List<String> classFiles;

        if (PathUtils.isJar()) {
            classFiles = PathUtils.getControllerClasses();
        } else {
            URL url = this.getClass().getResource("/" + scanPackage.replaceAll("\\.", "/"));
            File filePath = new File(url.getFile());

            classFiles = Stream.of(filePath.listFiles()).map(file -> {
                String className = scanPackage + "." + file.getName().replace(".class", "");
                Pattern pattern = Pattern.compile("\\$\\S+"); //上面 className 中最后的 '$1' 这种东西
                Matcher matcher = pattern.matcher(className);
                className = matcher.replaceAll("");
                return className;
            }).collect(Collectors.toList());
        }

        for (String className : classFiles) {

            Class clazz = controllerMap.get(className).getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (!field.isAnnotationPresent(Autowired.class)) {
                    continue;
                }
                Autowired autowired = field.getAnnotation(Autowired.class);
                field.setAccessible(true);
                String fieldName = field.getType().getName();
                if ("".equals(autowired.value())) {
                    Object o = controllerMap.get(clazz.getName());
                    field.set(o, controllerMap.get(fieldName));
                } else {
                    field.set(autowired.value(), controllerMap.get(fieldName));
                }
            }
        }
    }
}
