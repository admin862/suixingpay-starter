# 序列化与反序列化工具
作者：邱家榆

## 1. 摘要

序列化 (Serialization)是将对象的状态信息转换为可以存储或传输的形式的过程。一般将一个对象存储至一个储存媒介，例如档案或是记亿体缓冲等。在网络传输过程中，可以是字节、XML、JSON等格式。而字节、XML、JSON编码格式可以还原完全相等的对象。这个相反的过程又称为反序列化。

字节形式的序列化工具，除了我们常见的JDK 内部实现的序列化方式外，其实还有很多种，比如：hessian、kryo、fst-serializer、protobuffer等，在这里不作介绍，有兴趣的同学可以再去深入研究和对比。了解后你会发现因 JDK 内部实现的序列化存在性能差，数据包大等问题，才催生出各种开源的解决方案。

在这里给大家列举一些使用序列化与反序列化的场景：

1. RPC调用，比如：dubbo用的hessian;
2. 缓存，本地缓存（内存够时，把数据写入磁盘）；分布式缓存；
3. MQ消息，比如RabbitMQ是支持发送字节内容的，必要时也需要通过优化序列化来提升性能；
4. 深度复制: java 中深度复制除了通过Cloneable接口实现外，还可以通过序列化反序列化工具来实现；在管理本地缓存时经常会使用到；
5. 保存系统状态；

## 2. 导入依赖包

    compile "com.suixingpay.starter:suixingpay-starter-serializer:xxx"

通过 [http://172.16.60.188:8081/nexus/index.html#nexus-search;quick~suixingpay-starter-serializer](http://172.16.60.188:8081/nexus/index.html#nexus-search;quick~suixingpay-starter-serializer)  获取最新版本的jar。

## 3. 使用方法 

第一种方式：使用推荐的序列化方式：

    @Autowired
    private ISerializer serializer;
    
    private void test(){
      String obj = new String();
      byte[] data = serializer.serialize(obj);
      String obj2 = serializer.deserialize(data);
      
    }
    
 注意： 默认使用hessian 进行序列化与反序列化
 
 第二种方式：手动实例化：
 
     ISerializer serializer = new HessianSerializer();
     ISerializer serializer = new JdkSerializer(); // 不建议使用
 
