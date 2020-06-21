## 毕业设计项目：基于JavaFX+Spring+Mybatis的音乐播放器的设计与实现

#### 项目组成（三部分）

1. [播放器客户端](https://github.com/quanbisen/neteasemusicplayer)
2. [资源管理客户端](https://github.com/quanbisen/playermanager)
3. [服务器端](https://github.com/quanbisen/playerserver)

### 资源管理客户端

#### 运行

1. 克隆代码

```shell
git clone https://github.com/quanbisen/playermanager.git
```

2. 下载[JDK14](https://www.oracle.com/java/technologies/javase-jdk14-downloads.html),[JavaFX14 SDK](https://gluonhq.com/products/javafx/)
3. 用IntelliJ IDEA打开，导入Maven管理的依赖包
3. 添加JVM运行参数
```jvm
--module-path /PATH_TO_FXHOME/lib --add-modules=javafx.base,javafx.controls,javafx.fxml,javafx.graphics
```
4. 运行（这里Maven添加了JavaFX14 SDK的依赖，实际上编译运行是使用自己下载的JavaFX14 SDK的，所以PATH_TO_FXHOME为下载的JFX14的路径）

#### 功能结构及实现结果

##### 功能结构图

<div align="center">
    <img src="https://images.cnblogs.com/cnblogs_com/quanbisen/1779769/o_200603112437资源管理客户端功能模块图.jpg">
    </img>
</div>

##### 1. 歌曲管理

![](https://images.cnblogs.com/cnblogs_com/quanbisen/1779769/o_200603112419歌曲管理功能模块实现图.jpg)



##### 2. 歌手管理

![](https://images.cnblogs.com/cnblogs_com/quanbisen/1779769/o_200603112413歌手管理功能模块实现图.jpg)

##### 3. 专辑管理

![](https://images.cnblogs.com/cnblogs_com/quanbisen/1779769/o_200603112345专辑管理功能模块实现图.jpg)
