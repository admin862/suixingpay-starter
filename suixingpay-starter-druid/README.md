# druid数据源组件
作者：邱家榆，汤启华

---
## 快速开始
### 1. 基本配置
- gradle配置

        compile 'com.suixingpay.starter:suixingpay-starter-druid:xxx'
    [获取最新版本](http://172.16.60.188:8081/nexus/index.html#nexus-search;quick~suixingpay-starter-druid)
    
- yml配置

        spring:
          datasource: 
            druid:
              username: fd
              password: 123456
              url: jdbc:mysql://172.16.60.247:3306/demo?useUnicode=true&characterEncoding=utf-8&useSSL=false

### 2. 加解密
#### 1.方式一: 使用 filter
- 执行java命令

        java -cp suixingpay-starter-druid-1.0.0.jar com.suixingpay.takin.druid.decrypt.SecretGenerate you_password rsa长度(默认512)


- 输出 

        privateKey:
        publicKey:
        密文password:

- 配置参数

        spring:
          datasource: 
            druid:
              publickey: MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAIiwHpFrDijV+GzwRTzWJk8D3j3jFfhsMFJ/7k1NTvBuLgL+TdIHgaMNOIEjHpXzuvX38J3FtOK8hLrySncVGOMCAwEAAQ==
              connection-properties: config.decrypt=true;config.decrypt.key=${spring.datasource.druid.publickey}
              username: fd
              password: WVMjPhfXQrIsWRo0/RCqAVvYtTU9WNVToKJohb8AlUmHwnV6vwFL+FM2CNFDMJwGHW1iCmyaUlF+sgvFdogqEA==
              url: jdbc:mysql://172.16.60.247:3306/demo?useUnicode=true&characterEncoding=utf-8&useSSL=false
              filter:
                config:
                  enabled: true # 必须设置为true
              web-stat-filter:
                enabled: false
              stat-view-servlet:
                enabled: true
	**注意**：在配置多数据源加密时，除开数据源需要用到的配置其余都要跟在 spring.datasource.druid 节点后面（关于filter等的配置必须跟在spring.datasource.druid 节点后，否则解密无法生效）
例如：

	    spring:
		  datasource:
		    druid:
		      filter:
		        config:
		          enabled: true
		      web-stat-filter:
		        enabled: true
		      stat-view-servlet:
		        enabled: true
		      write:
		        url: jdbc:mysql://172.16.187.100:3306/user_master?useUnicode=true&characterEncoding=UTF-8
		        username: root
		        password: lHg65jGgOvfSBCWnrOlOWClzsfchMSlG/MGBE0h6DQeUNoCWTYiiTD+StV5ekF0mHgQebh2cCHv9Als4sKNTTQ==
		        driver-class-name: com.mysql.jdbc.Driver
		        publickey: MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKD3Y9XJ2rL2ovKSB/pXEK3laqnyIle9cVT6PAFU38Gl11Dl90/udkajDaRRa0vVqGccD0ZC02syV+MXkemm77kCAwEAAQ==
		        connection-properties: config.decrypt=true;config.decrypt.key=${spring.datasource.druid.write.publickey}

#### 2.方式二	：基于passwordCallbackClassName
访问[数据库密码加密页面](http://172.16.132.241:8080/) 
选择**新密钥**进行加密

例如，密文是D8B5719A5BCF49700985958D719A06E3，将获得的密文在配置文件yml进行配置,如下：

	Spring:
	  datasource:
	    druid:        
	      query:
	        url: jdbc:oracle:thin:@172.16.135.252:1521/ACCOUNT
	        username: query
	        password: D8B5719A5BCF49700985958D719A06E3
	        driver-class-name: oracle.jdbc.OracleDriver
	        filters: stat,wall
	        connectionProperties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000;password=D8B5719A5BCF49700985958D719A06E3
	        passwordCallbackClassName: com.suixingpay.takin.druid.DBPasswordCallback

### 3. 数据源配置

	spring:
	  datasource: 
	    druid: 
		  url: jdbc:mysql://172.16.60.247:3306/demo?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&zeroDateTimeBehavior=convertToNull
		  username: fd
		  password: 123456
		  driver-class-name: com.mysql.jdbc.Driver
		  filters: stat,wall
		  initialSize: 5
		  minIdle: 5
		  maxActive: 50
		  maxWait: 60000
		  timeBetweenEvictionRunsMillis: 60000
		  maxEvictableIdleTimeMillis:  3000000 # 因为公司防火墙长连接保持最长时间为1小时；此参数一定要结合实际情况进行配置。
		  validationQuery: select 1 from dual
		  testWhileIdle: true
		  testOnBorrow: false
		  testOnReturn: false
		  poolPreparedStatements: true
		  maxPoolPreparedStatementPerConnectionSize: 20
		  connectionProperties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000

### 4. 监控配置
    spring:
      datasource: 
        druid:
          stat-view-servlet:
            enabled: true
	        reset-enable: false #是否允许清空统计数据
	        url-pattern:  /druid/* #配置url-pattern来访问内置监控页面
	      web-stat-filter:
	        enabled: true
	#       exclusions: "*.js"  #经常需要排除一些不必要的url，比如*.js,/jslib/*等等
	#       principal-cookie-name: #配置principalCookieName，使得druid知道当前的user是谁
	#       principal-session-name: #配置principalSessionName，使得druid能够知道当前的session的用户是谁
	        profile-enable: true #配置profileEnable能够监控单个url调用的sql列表
	 #      session-stat-enable: true  #session统计功能
	 #      session-stat-max-count: 1000 #默认sessionStatMaxCount是1000个
	        url-pattern: /* #配置被filter作用的url
	      aop-patterns: com.suixingpay.demo.service.*
	      
默认监控页面访问 [http://localhost:8080/druid/index.html](http://localhost:8080/druid/index.html)，可由spring.datasource.druid.stat-view-servlet.url-pattern 配置

参考文章

* [druid-spring-boot-starter 说明](https://github.com/alibaba/druid/tree/master/druid-spring-boot-starter) 
* [druid wiki](https://github.com/alibaba/druid/wiki/%E9%A6%96%E9%A1%B5)

### 5.框架管理模块
当项目存在 **suixingpay-starter-manager** 依赖时，druid监控web默认受权限控制，更多详情请查阅 框架管理器 的说明文档。