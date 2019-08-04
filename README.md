# 随行付-开发基础框架
作者：邱家榆

---

通过这个地址:[http://192.168.120.68/root/suixingpay-starter-demo](http://192.168.120.68/root/suixingpay-starter-demo)下载实例代码

## 1. 随行付基础开发框架，开发原则：

1. 使用组件化开发思想，将各个组件进行解耦；应用可以根据自己的需要进行自由组合；
2. 使用Spring boot 开箱即用的思想进行开发，每个组件都具有_**自动装配**_功能（引入jar包后组件自动生效），通过调整组件配置参数以满足不同场景的需求；
3. 所有类的实例化，都通过@Bean注解来完成，不能使用@Component、@Controller、@Service、@ Repository；除 @Configuration类中能使用@Autowired 或 @Resource 注入依赖外，其它地方都不能使用，而使用手动进行注入；
4. 要避免冲突，比如：bean name避免与应用的冲突；避免应用扫包范围过大，造成重复注入问题；
5. 所有的配置都使用@ConfigurationProperties进行装载，不能使用@Value进行注入；
6. 允许开关组件中的部分或全部功能；
7. 必须具有可扩展性；
8. 考虑问题尽量全面，除了功能点要尽可能考虑到外，还需要满足尽可能多的使用场景，比如：现在缓存用得起来越多，在设计上也要考虑兼容使用缓存的场景；
9. 所有组件能使用默认配置的，一定要加上默认配置，以方便使用者的使用，减少使用复杂度；
10. 所有组件都需要有相关的使用说明文档，以方便使用者学习和理解；
11. 一定要避免循环依赖；
12. 尽量保证向下兼容；
13. 为了方便使用，不对各个组件单独维护版本号，所有组件统一版本号，使用者不需要关心各个组件之间的兼容性；
14. 为了与开源项目配置进行区分，框架新开发功能的配置都以“suixingpay.”开头，开源项目的配置保持不变；

## 2. 部份组件的简介：

* suixingpay-starter-util  基础工具类
* suixingpay-starter-exception  异常类的定义以及全局异常处理
* suixingpay-starter-data  数据建模
* suixingpay-starter-serializer  序列化 
* suixingpay-starter-expression 表达式处理器
* suixingpay-starter-logback 统一日志配置以日志追踪
* suixingpay-starter-kaptcha  验证码生成工具
* suixingpay-starter-rabbitmq  RabbitMQ 开发框架
* suixingpay-starter-redis  Redis工具类
* suixingpay-starter-cache 缓存
* suixingpay-starter-token 基于Token登录验证
* suixingpay-starter-cors  跨域请求设置
* suixingpay-starter-xss  防xss攻击
* suixingpay-starter-swagger2  swagger配置
* suixingpay-starter-mybatis-page  Mybatis 分页插件
* suixingpay-starter-mybatis-dynamic-datasource  Mybatis 读写分离插件
* suixingpay-starter-mybatis  Mybatis
* suixingpay-starter-distributed-lock  分布式锁及幂等
* suixingpay-starter-validator  数据验证扩展
* suixingpay-starter-druid  阿里druid连接池扩展
* suixingpay-starter-web  控制器工具插件
* suixingpay-starter-transaction  数据库事务管理
* suixingpay-starter-id  全局id生成器
* suixingpay-starter-schedule  定时任务
* suixingpay-starter-drools  规则引擎
* suixingpay-starter-ratelimit  限流

注意： suixingpay-starter-web 和 suixingpay-starter-mybatis 等从3.0.0版本不再兼容之前的版本，升级之前要注意。

## 3. change log


* 3.0.3 change log

  * suixingpay-starter-distributed-lock 基于单机版本Redis实现分布锁，获取锁失败时，也拿到锁的bug;
  * suixingpay-starter-transaction 增加事务PROPAGATION_SUPPORTS传播机制的支持；
  * suixingpay-starter-mybatis 将AbstractService改成 interface；
  * suixingpay-starter-logback 增加异步日志输出；
  * suixingpay-starter-rabbitmq 生产者增加路由信息：生产者appname、消息生产时间戳（即消息消费者可以获取消息生产者的application.name，以及消息产生的时间）；MessageHandler.onMessage方法增加MessageProperties类型参数，通过它的一些方法可以扩展信息，比如：getAppId()获取生产者、getTimestamp()获取生产时间以及getHeaders()获取消息头等；解决RetryThread中NPE；
  * suixingpay-starter-drools  新规则引擎；
  * suixingpay-starter-cache 去除对ognl自动实例化；

* 3.0.2 change log

  * suixingpay-starter-util 和 suixingpay-starter-rabbitmq:去除mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE); HashUtils 增加sha256；
  * suixingpay-starter-mybatis 和 suixingpay-starter-web: GenericMapper、AbstractService 和 GenericRestController 去BaseDomain 约束；Pagination 增加of方法；
  * suixingpay-starter-token: TokenHelper.login方法调整；
  * suixingpay-starter-cache: 增加 suixingpay.autoload.cache.enable-read-and-write 和 suixingpay.autoload.cache.enable-delete 两个开关；
  * suixingpay-starter-mybatis: 增加IntegerArrayTypeHandler, IntegerListTypeHandler, IntegerSetTypeHandler, LongArrayTypeHandler, LongListTypeHandler, LongSetTypeHandler, StringArrayTypeHandler, StringListTypeHandler, StringSetTypeHandler, LongAscArrayTypeHandler 和 IntegerAscArrayTypeHandler

* 3.0.1 change log

  * suixingpay-starter-rabbitmq 增加重试功能，保证消息不会丢失；
  * suixingpay-starter-manager 增加suixingpay.manager.urls配置项；
  * suixingpay-starter-logback 增加MAX\_HISTORY参数，其默认值为180；增加TraceUtil 工具类；
  * suixingpay-starter-transaction 将事务默认隔离级别从READ\_COMMITTED调整为REPEATABLE\_READ；并增加suixingpay.transaction.isolation 配置项，方便调整隔离级别；
  * suixingpay-starter-druid 增加管理功能
* 3.0.0 重构基础框架，使用“微组件”化的设计思想，将组件进行拆分解耦，使用者根据需求进行自由组合；