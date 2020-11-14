# JavaFX-Player
使用JavaFX编写的一个音乐播放器  
> 学了四天，做了一天半，所以有些简陋  
> 后续可能会更新爬取其他音乐平台的歌曲链接，因为 `Media` 支持直接使用 `http://url` 的方式解析，所以也是很简单的  
> 对于歌词滚动可能不会做，因为需要获取歌曲的歌词信息，在使用音乐平台api可能能获取到这些信息  

> 注: 使用 `mvn jfx:native` 可以打包成 `.exe` 文件,按道理可以打包成 Mac Linux 使用的,需要自己改 maven 配置  

> 获取歌曲封面用的 `jaudiotagger` 框架，因为`Media`中的`metadata`有时候获取不到封面，所以采用这个框架。
>
> 使用该框架的 `MP3File`获取，该类需要传入一个 `File file`参数，因为打包后要从 `jar`中读取文件的原因，所以使用`getResourceAsStream(“path”)`方法获取文件流，然后创建一个临时文件，再用`apache commons-io`框架把流放到临时文件中，再传给`MP3File`获取
>
> 此时出现问题，因为歌曲名都是中文的，在调用获取封面的方法（`getMusicCover(String path)`）时文件名会乱码或者编码成非中文，而调用获取这个方法时会从 `Media`获取到文件所在的路径，所以在获取音乐列表时（`getMusicInfoFromJar(String path)`），将里面的歌曲路径使用`URLEncoder`编码了，获取封面方法中再解码回中文，就OK了

仿照 Spring 使用主动注入的方式，在需要用到另一个 `Controller` 的 `Controller`类中主动注入,如下代码  
``` java
public class MusicListController {
    ...
    @Autowired
    private PlayController playController;
    ...
}
```
其主要实现在 [`Main.java`](https://github.com/Jratil/JavaFX-Player/blob/master/src/main/java/com/jratil/Main.java) 中 

> 对于打包后读取不到 jar包中的 `resources/music/***.mp3` 文件，使用了读取jar文件路径，具体在 [`PathUtils.java`](https://github.com/Jratil/JavaFX-Player/blob/master/src/main/java/com/jratil/utils/PathUtils.java) 中    
>
> 因为`File` 不能读取到 `Jar`包里的文件（Jar包相当于压缩包），但是Media可以使用 `Jar文件路径（jar:file:!/C:/data/**.mp3）`所以需要分别考虑

![预览图](https://raw.githubusercontent.com/Jratil/JavaFX-Player/master/main.png)  
![预览图](https://raw.githubusercontent.com/Jratil/JavaFX-Player/master/main2.png)  

