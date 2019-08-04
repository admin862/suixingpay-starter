# Redis操作工具

作者：邱家榆

------

Spring 自带的 RedisTemplate 已经是非常不错的Redis操作工具，但是存在以下几个问题：

1. 不支持命名空间；使用命名空间，可以隔离应用之间的数据，避免冲突；方便后期迁移数据；方便通过key反向找到应用及其负责人；
2. 增加时间监控；
3. 对Lua支持更好；
4. 内嵌Hessian序列化工具；

使用说明：

## 1. 在项目中增加jar 包的依赖

    compile "com.suixingpay.starter:suixingpay-starter-redis:xxx"
    
[获取最新版本](http://172.16.60.188:8081/nexus/index.html#nexus-search;quick~suixingpay-starter-redis)

## 2. 配置redis

 按org.springframework.boot.autoconfigure.data.redis.RedisProperties 进行配置
 
 1. 单机版Redis配置
 
        spring: 
          redis: 
            host: 172.16.135.249
            port: 6379
            pool: 
              maxWait: 1500
              maxActive: 2048
              minIdle: 20
              maxIdle: 200
        
 2. 集群配置
 
         spring: 
          redis: 
            cluster:
              nodes:
              - 10.7.100.95:7000
              - 10.7.100.95:7001
              - 10.7.100.95:7002
              - 10.7.100.95:7003
              - 10.7.100.95:7004
              - 10.7.100.95:7005
              - 10.7.100.95:7006
            pool: 
              maxWait: 1500
              maxActive: 2048
              minIdle: 20
              maxIdle: 200
              
  配置时，把集群的所有节点都加到配置文件中来。当master节点不可用时，slave节点自动切为master节点时，应用还能正常使用Redis。

以上Redis的两种配置，只要选择一种即可；

## 3. Redis操作工具配置


    suixingpay: 
      redis: 
          namespaceEnable: true        # 是否启用命名空间，默认为true
          namespace: test              # 命名空间, 当namespaceEnable 为true 时，会使用spring.application.name作为命名空间
          slowLogSlowerThan: 10        # Redis 慢操作，单位：毫秒，默认值为10, 如果大于0时会打印慢日志 

**注意**：如果namespaceEnable设置为true时，会启用suixingpay.redis.namespace、spring.application.name 按优先级设置为namespace。当namespace设置后，缓存key都会以${namespace}+":"+${key}


## 4. 使用方法：

一种是使用自动配置的：

    @Autowired
    private IRedisOperater redis;
    
    @Test
    public void testGet() {
        String key = "redis-test-k1";
        String val = "aaaaaaaaaaaaaaa";
        redis.setex(key, val, 10);
        Object r = redis.get(key);
        Assert.assertEquals(val, r);
    }

当自动配置的规则不能满足需求时，可以通过下面方式创建：

    @Autowired
    RedisConnectionFactory connectionFactory;
    @Autowired
    ISerializer redisISerializer;
    
    String namespace=null;
    IRedisOperater redis=RedisOperaterFactory.create(config, connectionFactory);

## 5. 特别说明

默认使用内嵌Hessian进行序列化，所以写入Redis的数据需要实现java.io.Serializable接口。

 **注意**：必须使用Spring boot 1.4.2及以上的版本，Gradle必须使用2.14.1及以上
