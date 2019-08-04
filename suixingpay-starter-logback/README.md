# 日志公共配置使用说明
作者：邱家榆

---

## 1. 摘要

日志是非常重要组件，能帮我们收集系统运行时的信息，利用日志可以帮助我们了解系统的运行情况，帮助我们排查问题，所以每个系统都离不开它。因为它重要所以使用率非常高，那么很多重复的东西就可以提取出来，达到复用，避免重复开发，也能形成一种规范。那么哪些东西是可以复制的，哪些是不能复用的？

复用的有：

1. 日志的输入格式；
2. 日志切割的规则；
3. 日志文件名的统一；
4. 敏感信息的过渡；
5. 参数统一配置；统一优化性能；
6. 设置追踪ID;

不能复用：

1. 日志级别控制；不同环境下需要设置的日志级别是不一样的，开发、测试环境为了方便调试，会把日志级别调得比较低，方便更好排查问题；而生产环境中则为了保证性能，尽量只输出错误日志；
2. 日志输出的方式不一样，开发环境为了方便，会把日志输出到控制台，而其它环境尽量只输出到文件中；

为了解决复用的问题和不能复用的问题，学习了Spring boot中 org/springframework/boot/logging/logback/base.xml 中的设计，把能复用的代码放到 com/suixingpay/takin/logback/base.xml。 这样可以统一日志格式，方便后期做日志分析；可以很好复用代码，让日志设置变得更加简单。

## 2. 导入依赖包

    compile "com.suixingpay.starter:suixingpay-starter-logback:xxx"

通过 [http://172.16.60.188:8081/nexus/index.html#nexus-search;quick~suixingpay-starter-logback](http://172.16.60.188:8081/nexus/index.html#nexus-search;quick~suixingpay-starter-logback)  获取最新版本的jar。

## 3. 配置logback文件

要使用 springProfile的话，必须使用logback-spring.xml 或者 通过 logging.config 指定配置文件，因为spring boot 先加载完配置文件后，获取ActiveProfiles后，才会让springProfile功能生效。

如果项目中引用了一些带有logback.xml文件的jar，那么需要在项目中建个没有配置内容的logback.xml：

    <?xml version="1.0" encoding="UTF-8"?>
    <!--debug="false"关闭logback内部监控 -->
    <configuration>
    </configuration>
    

下面是实例代码，根据项目的实际情况进行调整：

例子1：使用Spring Profile的例子(保存到logback-spring.xml文件)：

    <?xml version="1.0" encoding="UTF-8"?>
    <!--debug="false"关闭logback内部监控 -->
    <configuration debug="false">
      <property name="LOG_PATH" value="${APP_HOME:-.}/logs" /><!-- 日志保存路径，默认值为./logs -->
      <include resource="com/suixingpay/takin/logback/base.xml" />
      <jmxConfigurator/>
      <!-- 根据不同的环境，设置不同的level以及 appender-->
      <!--开发环境配置 -->
      <springProfile name="default,dev">
        <logger name="org.springframework.web" level="ERROR" />
        <logger name="org.springboot.sample" level="ERROR" />
        <logger name="com.jarvis" level="TRACE" />
        <logger name="com.suixingpay.takin.cache" level="TRACE" />
        <root level="INFO">
          <appender-ref ref="CONSOLE" />
          <appender-ref ref="FILE" />
          <appender-ref ref="ERROR" />
        </root>
      </springProfile>
    
      <!--test， rc 环境配置 -->
      <springProfile name="test,rc">
        <logger name="org.springframework.web" level="ERROR" />
        <logger name="org.springboot.sample" level="ERROR" />
        <logger name="com.jarvis" level="DEBUG" />
        <logger name="com.suixingpay.takin.cache" level="DEBUG" />
        <root level="INFO">
          <appender-ref ref="FILE" />
          <appender-ref ref="ERROR" />
        </root>
      </springProfile>
    
      <!--prod环境配置 -->
      <springProfile name="prod">
        <root level="ERROR">
          <appender-ref ref="FILE" />
          <appender-ref ref="ERROR" />
        </root>
      </springProfile>
    </configuration>
    
例子2 不使用Spring profile(保存到logback.xml文件)：

    <?xml version="1.0" encoding="UTF-8"?>
    <!--debug="false"关闭logback内部监控 -->
    <configuration debug="false">
      <property name="LOG_PATH" value="${APP_HOME:-.}/logs" />
      <!-- 引用公共配置 -->
      <include resource="com/suixingpay/takin/logback/base.xml" />
      <jmxConfigurator/>
      <root level="INFO">
        <appender-ref ref="CONSOLE" />
        <appender-ref ref="FILE" />
        <appender-ref ref="ERROR" />
      </root>
    </configuration>

## 4. 说明

spring boot logging的说明[https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-logging.html](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-logging.html), 其定义好了日志的输出方式以及输出格式，以及保存日志文件的路径等。甚至可以在配置文件中配置日志文件及日志存放路径（logging.file及logging.path）。

 com/suixingpay/takin/logback/base.xml 中取消了通过logging.file设置日志文件的方法，而保留了logging.path 设置日志保存路径。最终日志文件路径为：${logging.path}/${LOG\_FILE\_NAME}.log，默认LOG\_FILE\_NAME为“./logs”。另外增加了error日志文件：${logging.path}/${LOG\_FILE\_NAME}.error.log。日志文件滚动后，存放路径为：${logging.path}/%d{yyyyMMdd}/${LOG\_FILE\_NAME}.%i.log。

其它可设置参数：

1. LOG_PATH: 日志存放路径，默认值：./logs；
2. LOG\_FILE\_NAME: 日志文件名(不带扩展名)，默认值是server；
3. MAX\_FILE\_SIZE: 日志文件分隔大小，默认是：100MB；不建议修改
4. FILE\_LOG\_PATTERN: 日志文件中的输出格式；不建议修改；
5. CONSOLE\_LOG\_PATTERN: 日志控制台中的输出格式；不修改修改；
6. SENSITIVE: 屏蔽敏感词功能，默认是关闭的,设置为Y打开；
7. FILE\_IMMEDIATE_FLUSH: 是否立即flush到日志文件,默认false ,不会立即flush, 会有8K左右的缓冲区，如果设置为true，则会会立即flush（写入磁盘）；
8. ERROR\_IMMEDIATE_FLUSH: 是否立即flush到错误日志文件,默认true, 会立即flush（写入磁盘）,如果设置为false，会有8K左右的缓冲区；
9. MAX\_HISTORY: 日志存入天数，其默认值为180

以上参数可以通过在logback-spring.xml 中使用\<property\> 标签设置，也可以使用环境变量设置。

如果要设置以上参数，必须在<include resource="com/suixingpay/takin/logback/base.xml" /> 之前设置，比如：

    <?xml version="1.0" encoding="UTF-8" ?>
    <configuration debug="false" scan="true" scanPeriod="30 seconds">
        <property name="LOG_PATH" value="${APP_HOME:-.}/logs" />
        <!-- 引用公共配置 -->
        <include resource="com/suixingpay/takin/logback/base.xml" />
        ... ...
    </configuration>​

日志输出格式：|%d{yyyy-MM-dd HH:mm:ss.SSS}|%X{apmTraceId}|%X{tradenum}|%-5level-[%msg]-[%logger{36}]-[%thread]%n

* apmTraceId：为APM系统预留的Trace ID;
* tradenum：追踪码。从3.0.0版本已提供默认实现。
tradenum默认开启，也可以通过yml进行配置
	
		suixingpay: 
		  logback:
		    trace:
		      enabled: true # 是否启用trace
		      trace-id: SXF-TRACE-ID               #客户端通过参数或Header传过来的追踪ID的参数名称
		      #filter-name: sxfTraceFilter            # 过滤器名称，默认值为：sxfTraceFilter
		      invalid-chars: "[^A-Za-z0-9_-]"         #避免客户端传入的traceId中的特殊字符打乱日志格式，需要把一些非法字符过滤掉
		      order: -1000                            #排序
		      trace-id-max-length: 16                 #traceId 保留的最大长度，如果超过这个长度，则会截取前面的traceIdMaxLength 长度字符，如果小于等于0，则不限制，默认值为16；
		      url-patterns: 
		      - /*                                    #启用日志Filter的有效路径
		      ignoreUrls: # 忽略路径     

以上配置都是默认配置，可自行按需进行修改

还有建议不要把logback.xml或logback-spring.xml打到jar中进行共享，一方面不利于维护，另一方面也容易造成冲突。

使用Mybatis时，如果需要打印SQL,需要把mapper的日志级别设置为TRACE:

    <!-- 打印SQL,需要把mapper的日志设置为TRACE -->
    <logger name="com.suixingpay.takin.demo.mapper" level="TRACE" />

## 5. 异步日志AsyncAppender原理介绍

当Logging Event进入AsyncAppender后，AsyncAppender会调用appender方法，append方法中在将event填入Buffer(这里选用的数据结构为BlockingQueue)中前，会先判断当前buffer的容量以及丢弃日志特性是否开启，当消费能力不如生产能力时，AsyncAppender会超出Buffer容量的Logging Event的级别，进行丢弃，作为消费速度一旦跟不上生产速度，中转buffer的溢出处理的一种方案。AsyncAppender有个线程类Worker，它是一个简单的线程类，是AsyncAppender的后台线程，所要做的工作是：从buffer中取出event交给对应的appender进行后面的日志推送。

从上面的描述中可以看出，AsyncAppender并不处理日志，只是将日志缓冲到一个BlockingQueue里面去，并在内部创建一个工作线程从队列头部获取日志，之后将获取的日志循环记录到附加的其他appender上去，从而达到不阻塞主线程的效果。因此AsynAppender仅仅充当事件转发器，必须引用另一个appender来做事。

因为AsynAppender不能单独使用，需要另挂一个Appender，比如RollingFileAppender。这个Appender继承了OutputStreamFileAppender，OutputStreamFileAppender只是把BufferedOutPutStream包装一下，加入一些layout以及一些格式方面的东西。但是OutputStreamFileAppender在同步方面使用大量synchronized，由于AsynAppender已经做了，同步了，再次同步已经没必要了，而且synchronized的性能又不那么好，看来有优化的余地。

在使用AsyncAppender的时候，有些选项还是要注意的。由于使用了BlockingQueue来缓存日志，因此就会出现队列满的情况。正如上面原理中所说的，在这种情况下，AsyncAppender会做出一些处理：默认情况下，如果队列80%已满，AsyncAppender将丢弃TRACE、DEBUG和INFO级别的event，从这点就可以看出，该策略有一个惊人的对event丢失的代价性能的影响。

另外其他的一些选项信息，也会对性能产生影响，下面列出常用的几个属性配置信息：

| 属性名 | 类型 | 描述 |
| --- | --- | --- |
| queueSize | int | BlockingQueue的最大容量，默认情况下，大小为256。|
| discardingThreshold | int | 默认情况下，当BlockingQueue还有20%容量，他将丢弃TRACE、DEBUG和INFO级别的event，只保留WARN和ERROR级别的event。为了保持所有的events，设置该值为0。|
| includeCallerData | boolean | 提取调用者数据的代价是相当昂贵的。为了提升性能，默认情况下，当event被加入到queue时，event关联的调用者数据不会被提取。默认情况下，只有"cheap"的数据，如线程名。|

基础框架中相对应的参数名为：

| logback属性名 | 自定义属性名 | 默认值 |
| --- | --- | --- |
| queueSize | QUEUE_SIZE | 2048 |
| discardingThreshold | DISCARDING_THRESHOLD | 0 不丢失日志 |
| includeCallerData | INCLUDE_CALLER_DATA | true |

如果要使用异步输出日志，只需要将Appender调整一下即可，使用 **ASYNC_FILE** 代替 FILE; 为了避免错误日志丢失，错误日志不提供异步方式。例如：

    <?xml version="1.0" encoding="UTF-8"?>
    <!--debug="false"关闭logback内部监控 -->
    <configuration debug="false">
      <property name="LOG_PATH" value="${APP_HOME:-.}/logs" /><!-- 日志保存路径，默认值为./logs -->
      <!-- <property name="QUEUE_SIZE" value="1028" /> -->
      <include resource="com/suixingpay/takin/logback/base.xml" />
      <jmxConfigurator/>
      <!-- 根据不同的环境，设置不同的level以及 appender-->
      <!--开发环境配置 -->
      <springProfile name="default,dev">
        <logger name="org.springframework.web" level="ERROR" />
        <logger name="org.springboot.sample" level="ERROR" />
        <logger name="com.jarvis" level="TRACE" />
        <logger name="com.suixingpay.takin.cache" level="TRACE" />
        <root level="INFO">
          <appender-ref ref="CONSOLE" />
          <appender-ref ref="ASYNC_FILE" />
          <appender-ref ref="ERROR" />
        </root>
      </springProfile>
    
      <!--test， rc 环境配置 -->
      <springProfile name="test,rc">
        <logger name="org.springframework.web" level="ERROR" />
        <logger name="org.springboot.sample" level="ERROR" />
        <logger name="com.jarvis" level="DEBUG" />
        <logger name="com.suixingpay.takin.cache" level="DEBUG" />
        <root level="INFO">
          <appender-ref ref="ASYNC_FILE" />
          <appender-ref ref="ERROR" />
        </root>
      </springProfile>
    
      <!--prod环境配置 -->
      <springProfile name="prod">
        <root level="ERROR">
          <appender-ref ref="ASYNC_FILE" />
          <appender-ref ref="ERROR" />
        </root>
      </springProfile>
    </configuration>

## 6. 更新日志：

1. 3.0.3 增加异步日志输出方式：ASYNC_FILE
1. 3.0.1 增加MAX_HISTORY参数；增加TraceUtil 工具类；
1. 3.0.0 增加TraceFilter
1. 2.1.0 删除base.xml中默认的<root>配置；直接使用logback.xml时，不能覆盖这个配置(使用logback-spring.xml是正常的，所以建议尽量使用logback-spring.xml来配置)；
1. 2.0.0 升级 ch.qos.logback:logback-classic
1. 1.0.6版本增加ch.qos.logback:logback-classic 依赖，避免低版本的Spring boot中无法使用问题；
1. 1.0.5版本日志输入增加了UTF-8编码，避免中文乱码; 增加了文超之前写的，屏蔽敏感词功能；

