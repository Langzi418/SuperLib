## 程序实现思想

后端使用Jsoup抓取网页数据，前端使用Android组件来显示数据。



## 功能模块如下

### 程序主界面及检索条件

![](https://ws1.sinaimg.cn/large/005wR1ytgy1g2bodds72mj308r0brq3d.jpg)![](https://ws1.sinaimg.cn/large/005wR1ytgy1g2boemykwjj30910d6t93.jpg)

### 用户登陆

这里使用模拟登陆，根据登陆后HTML页的结果来判断登陆的结果。

![](https://ws1.sinaimg.cn/large/005wR1ytgy1g2bogfwi47j30f30amjs5.jpg)

### 用户收藏

使用SQLite数据库存储数据，数据库定义采用一个ORM框架，GreenDAO。

![](https://ws1.sinaimg.cn/large/005wR1ytgy1g2boip224hj307q0c93zd.jpg)

### 图书检索

![1555935804071](C:\Users\peng\AppData\Roaming\Typora\typora-user-images\1555935804071.png)

### 图书信息

这里调用了豆瓣评论API。

![1555935875168](C:\Users\peng\AppData\Roaming\Typora\typora-user-images\1555935875168.png)



![1555935901704](C:\Users\peng\AppData\Roaming\Typora\typora-user-images\1555935901704.png)



### 热门检索

这是一个模拟3D云图。

![](https://ws1.sinaimg.cn/large/005wR1ytgy1g2bolt4rcrj308z0cbq3x.jpg)

### 热门图书

![](https://ws1.sinaimg.cn/large/005wR1ytgy1g2bom7oq4mj30900cu74y.jpg)

### 个人信息



![1555936003646](C:\Users\peng\AppData\Roaming\Typora\typora-user-images\1555936003646.png)![](https://ws1.sinaimg.cn/large/005wR1ytgy1g2bonlagyej308v0chdgm.jpg)



