## Android WtfNews

王奥丞
2014011367

#### 功能说明

1. 支持不同分类的新闻展示
2. 支持新闻metadata和图片的二级缓存（内存/数据库）
3. 支持分类的筛选
4. 支持新闻按时间筛选
5. 支持新闻收藏/分享
6. 支持下拉获取更之前的新闻


#### 设计

1. 用RxJava/RxAndroid的异步IO模型，而不使用多线程编程模型。这样防止多线程开发中的竞争/死锁等问题。

2. Picasso处理图片的加载/缩放/缓存
3. 用SugarORM封装数据库操作，不用手写SQL可以简化数据库查询。（但是这个库bug很多，比如和Instance run不兼容，他内部用到了Dexfile类列出apk包内的所有java类，而instance run将apk拆成几份，增量运行，这样sugar就无法获得所有java类，造成no such table exception。)
4. 用了android databinding。这是android官方支持的一套类似angularjs的databinding库，可以在xml中绑定数据，大大减少了java中findViewById的数量。
5. 用了butterknife作为databinding的补充，将UI事件绑定到某个类的成员函数.

