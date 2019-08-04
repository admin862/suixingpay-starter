# 事务管理

作者:邱家榆

---

## 1. 事务的重要性

在现在NoSQL 盛行的时代，而DBMS（数据库管理系统）的市场占用率经久不衰，其主要原因是DBMS的ACID特性能很好保证数据的强一致性，而市面上还没有一款NoSQL能有同等效果。

## 2. 了解JDBC 事务管理

在使用JDBC 操作数据库时，其事务管理是非常繁琐的事情：

	import java.sql.Connection;
	import java.sql.DriverManager;
	import java.sql.ResultSet;
	import java.sql.SQLException;
	import java.sql.Statement;
	
	import org.apache.commons.dbcp.BasicDataSource;
	
	import com.mysql.jdbc.PreparedStatement;
	
	public class JDBCTest {
	    private static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	    private static String DB_URL = "jdbc:mysql://10.120.177.10:3306/datebaseclass";
	    private static String USER = "test";
	    private static String PASS = "test";
	
	    public static void main(String[] args) throws ClassNotFoundException {
	        test();
	    }
	
	    public static void test() {
	        Connection conn = null;
	        PreparedStatement ptmt = null;
	        ResultSet rs = null;
	        try {
	            // 1 加载Jdbc Driver
	            Class.forName(JDBC_DRIVER);
	            // 2 创建数据库连接
	            conn = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);
	            // 3 关键：关闭自动提交，默认为true,每执行完一条SQL会自动提交
	            conn.setAutoCommit(false);
	            ptmt = (PreparedStatement) conn.prepareStatement("update Product set Inventory=Inventory-1 where ProductName = 'bag'");
	            ptmt.execute();
	            ptmt = (PreparedStatement) conn.prepareStatement("INSERT INTO `Order` (buyer, ProductName) VALUES ('XiaoMing', 'bag')");
	            ptmt.execute();
	            conn.commit();
	        } catch (ClassNotFoundException e) {
	            // Class没有发现异常
	            System.out.println(e.toString());
	        } catch (SQLException e) {
	            // Class没有发现异常
	            if (conn != null) {
	                try {
	                    conn.rollback();
	                } catch (SQLException e1) {
	
	                    System.out.println(e.toString());
	                }
	            }
	            System.out.println(e.toString());
	        } finally {
	            try {
	                if (rs != null) {
	                    rs.close();
	                }
	                if (ptmt != null) {
	                    ptmt.close();
	                }
	                if (conn != null) {
	                    conn.close();
	                }
	            } catch (SQLException e) {
	                System.out.println(e.toString());
	            }
	        }
	
	    }
	}
	
因为开启事务，提交，回滚等格式比较固定，随着Spring AOP技术的发展，慢慢大家开始使用AOP来管理事务

## 3. 使用Spring XML配置事务AOP

  实例代码如下：
  
		  <!--事务配置 -->
		  <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
		    <property name="dataSource" ref="dataSource" />
		  </bean>
		  <!-- Spring AOP config proxy-target-class="true"-->
		  <aop:config >
		    <!-- preinst 切入点 -->
		    <!-- 因为此模块中只有添加订单一个事务，所以只要对DAO进行事务代理就可以了 -->
		    <aop:pointcut id="transactionPointcut" expression="execution(* com.suixingpay.portal.service..*.*(..))" />
		    <aop:advisor advice-ref="txAdvice" pointcut-ref="transactionPointcut" />
		  </aop:config>
		  <tx:advice id="txAdvice" transaction-manager="transactionManager">
		    <tx:attributes>
		      <tx:method name="add*" propagation="REQUIRED" isolation="REPEATABLE_READ" rollback-for="Exception" />
		      <tx:method name="insert*" propagation="REQUIRED" isolation="REPEATABLE_READ" rollback-for="Exception" />
		      <tx:method name="update*" propagation="REQUIRED" isolation="REPEATABLE_READ" rollback-for="Exception" />
		      <tx:method name="delete*" propagation="REQUIRED" isolation="REPEATABLE_READ" rollback-for="Exception" />
		      <tx:method name="remove*" propagation="REQUIRED" isolation="REPEATABLE_READ" rollback-for="Exception" />
		      <tx:method name="addXXXLog" propagation="REQUIRED_NEW" isolation="REPEATABLE_READ" rollback-for="Exception" />
		      <tx:method name="get*" read-only="true"  />
		      <tx:method name="find*" read-only="true" />
		      <tx:method name="count*" read-only="true" />
		      <tx:method name="query*" read-only="true" />
		      <tx:method name="select*" read-only="true" />
		      <tx:method name="list*" read-only="true" />
		      <tx:method name="check*" read-only="true" />
		      <tx:method name="*" read-only="true" /><!--为了避免对未知情况开启事务，造成资源浪费，也是为了方便检查事务配置是否正确-->
		    </tx:attributes>
		  </tx:advice>


  上面的这些配置相对比较固定，但如果有多个数据源时，需要多次重复配置，那么就会针对每个写数据源创建对应的AOP拦截器，也就是在执行方法时会同时开启多个数据源的事务，势必造成浪费。
  
  优点：业务代码中不需要关心事务，全通过AOP来管理，所以使用起来非常简单；
  
  缺点：AOP配置复杂；如果对AOP原理不熟悉的话，还非常容易配置错误；
  
Spring 提供的几种事务控制:

1. PROPAGATION_REQUIRED（加入已有事务）

    尝试加入已经存在的事务中，如果没有则开启一个新的事务。

2. RROPAGATION_REQUIRES_NEW（独立事务）

    挂起当前存在的事务，并开启一个全新的事务，新事务与已存在的事务之间彼此没有关系。

3. PROPAGATION_NESTED（嵌套事务）

    在当前事务上开启一个子事务（Savepoint），如果递交主事务。那么连同子事务一同递交。如果递交子事务则保存点之前的所有事务都会被递交。

4. PROPAGATION_SUPPORTS（跟随环境）

    是指 Spring 容器中如果当前没有事务存在，就以非事务方式执行；如果有，就使用当前事务。

5. PROPAGATION_NOT_SUPPORTED（非事务方式）

    是指如果存在事务则将这个事务挂起，并使用新的数据库连接。新的数据库连接不使用事务。

6. PROPAGATION_NEVER（排除事务）

    当存在事务时抛出异常，否则就已非事务方式运行。

7. PROPAGATION_MANDATORY（需要事务）

    如果不存在事务就抛出异常，否则就已事务方式运行。
  
## 4. Spring boot中事务配置

  需要使用@EnableTransactionManagement 开启事务(其实是进行事务AOP配置，只会创建一个拦截器)

	@EnableTransactionManagement
	@SpringBootApplication
	public class ProfiledemoApplication {
	
	    public static void main(String[] args) {
	        SpringApplication.run(ProfiledemoApplication.class, args);
	    }
	}
  
   还需要在服务层加上@Transactional，进行管理事务：
   
	    @Service
		 // 默认开启只读
		@Transactional(readOnly = true, rollbackFor = Throwable.class)
		public class UserServiceImpl implements UserService {
			
		    @Autowired
		    private UserMapper userMapper;
		 		
		    @Override
		    public UserDO getById(@NonNull Integer id) {
		        return userMapper.getById(id);
		    }
		
		    // 开启事务
		    @Transactional(rollbackFor = Throwable.class)
		    public void addUser(@NonNull UserDO user) {
		        if (this.userMapper.addUser(user) <= 0) {
		            throw new RuntimeException("add user fail");
		        }
		    }
		}
		
  
  不难发现当代码量增加时，需要添加 @Transactional 的地方也可能会增加，最主要的是容易遗漏。同时对于在抽象类中的实现，无法用于多数据源的情况。
  
  如果是多数据源情况下使用，还需要针对每个数据源创建一个PlatformTransactionManager,  而@Transactional 只能设置一个PlatformTransactionManager名字，也就是一个方法只能开启一个数据源的事务。如果一个方法同时对两个库进行写数据，不能保证数据一致性。
	
  优化：AOP 配置非常简单；
  
  缺点：使用比较复杂，容易造成遗漏；如果有些实现是在父类，那就需要override代码进行添加注解；

## 5. 使用 Spring boot starter 来简化事务配置
1. 导入依赖包

        compile "com.suixingpay.starter:suixingpay-starter-transaction:xxx"

  通过 [http://172.16.60.188:8081/nexus/index.html#nexus-search;quick~suixingpay-starter-transaction](http://172.16.60.188:8081/nexus/index.html#nexus-search;quick~suixingpay-starter-transaction)  获取最新版本的jar。

  注意：为了避免事务的重复配置，此starter在没有使用@EnableTransactionManagement 的情况下才会生效。

2. Spring boot 配置说明
  
  以下列出默认配置：

		suixingpay: 
		  transaction:
		    enabled: true
		    isolation: REPEATABLE_READ # 事务隔离性，默认值为：REPEATABLE_READ, 3.0.1及以后版本支持，3.0.0版本默认使用READ_COMMITTED隔离级别
		    dataSourceName: dataSource # 数据源名称
		    dynamicDatasource: false # 是否是动态数据源，默认值为false
		    transactionBeanNames: # 自定义事务 BeanName 拦截， 默认值："*Service", "*ServiceImpl"
		    readOnlyTransactionAttributes: # 自定义方法名的事务属性相关联,可以使用通配符(*)字符关联相同的事务属性的设置方法; 只读事务，默认值："get*", "count*", "find*",
	            "query*", "select*", "list*", "check*", "*"
		    requiredTransactionAttributes: # 自定义方法名的事务属性相关联,可以使用通配符(*)字符关联相同的事务属性的设置方法;传播方式：PROPAGATION_REQUIRED。 默认值："add*", "save*", "insert*","delete*", "update*", "edit*", "batch*", "create*", "remove*"
		    requiredNewTransactionAttributes: # 自定义方法名的事务属性相关联,可以使用通配符(*)字符关联相同的事务属性的设置方法;传播方式：PROPAGATION_REQUIRES_NEW。
		    proxyTargetClass:true # 设置AOP代理类型
		    aopOrder: # AOP 拦截顺序，默认值为：Integer.MAX_VALUE
		    aopOptimize: false # 是否优化AOP,默认false

  如果上面默认配置，符合实际情况，只需要引用jar包，事务自动生效。

  隔离性isolation可设置的值有：

  * DEFAULT: 使用数据库设置的隔离级别;
  * READ\_UNCOMMITTED: 可以读取未提交的数据，未提交的数据称为脏数据，所以又称脏读。此时：幻读，不可重复读和脏读均允许；
  * READ\_COMMITTED: 只能读取已经提交的数据；此时：允许幻读和不可重复读，但不允许脏读，所以此隔离级别要求解决脏读；
  * REPEATABLE\_READ: 同一个事务中多次执行同一个select,读取到的数据没有发生改变；此时：允许幻读，但不允许不可重复读和脏读， 所以此隔离级别要求解决不可重复读；
  * SERIALIZABLE:  幻读，不可重复读和脏读都不允许，所以serializable要求解决幻读；

参考：[【深入浅出事务】：事务的隔离级别（超详细）](https://segmentfault.com/a/1190000004469395)

## 6. 多“写”数据库支持

首先建议应用尽量只依赖一个写数据库，以避免提高事务管理的复杂性，主要是无法保证多库之间的数据一致性。

如果需要支持多数据库，那么上面transactionBeanNames 将会失效。同时不能把多个库的操作放到一个Service的方法之中，而是要将不同数据库的操作，放到不同package下的service中，并在Controller中分别调用， 下面举个例子，有个业务同时要操作数据库a和数据库b:

首先是多数据源的配置：

	@Configuration
	public class MultiDataSourceConfigurer {
	    
	    @Bean
	    @ConfigurationProperties("spring.datasource.druid.a")
	    public DataSource dataSourceA() {
	        return DruidDataSourceBuilder.create().build();
	    }
	
	    @Bean
	    @ConfigurationProperties("spring.datasource.druid.b")
	    public DataSource dataSourceB() {
	        return DruidDataSourceBuilder.create().build();
	    }
	}
	
Mybatis配置：

TransactionManager 配置（注意：如果使用了@EnableTransactionManagement，下面的配置将失效）：

	suixingpay: 
	  transaction:
	    enabled: true
	    multi:
	    - 
	      dataSourceName: dataSourceA #数据源Bean名称，必填项
	      dynamicDatasource: false # 是否是动态数据源，默认值为false
	      basePackage: com.suixingpay.demo.service.a # 需要开启事务的package, 必填项
	      transactionManagerName: aTransactionManager # transactionManager Bean名称, 选填，如果没有填写，默认使用dataSourceName+"TransactionManager",然后通过此bean name获取TransactionManager，如果没有获取到则自动新建一个，并注册到Spring 容器中
	    - 
	      dataSourceName: dataSourceB
	      basePackage: com.suixingpay.demo.service.b
	      transactionManagerName: bTransactionManager

Service 层实现：

    package com.suixingpay.service.a;
    @Service
    public class AService {
        public void aTask() {
	        ... ... // 数据库A的处理逻辑
        }
    }
    
    package com.suixingpay.service.b;
    @Service
    public class BService {
        public void bTask() {
	        ... ... // 数据库B的处理逻辑
        }
    }

Controller 层调用Service

    @RestController
    public class MyController {
	
        @Autowired
        private AService aService;
        @Autowired
        private BService bService;
	    
        @PostMapping
        public void task() {
	        aService.aTask();
	        bService.bTask();
        }
    }
	
通过上面的例子可以看出，如果应用要支持多数据源，在事务管理上是非常复杂的，而且很容易出错，所以建议还一个应用只依赖一个数据库原则，如果需要调用其它数据库，通过RPC来实现。

建议一个数据库提供一个“数据服务”，专门用于实现与数据库和缓存打交道，把操作数据库及缓存的复杂性屏蔽在此服务以内。同时可以把一些通用的业务实现也下沉到此服务中。

## 7.Mybatis多数据源配置
当需要多数据库来支撑业务需求的时候并使用Mybatis作为DAO实现，与Spring boot整合的极简配置就不能满足需求了，需要我们自己来手动配置SqlSessionFactory，每个datasource对应一个SqlSessionFactory，如下：

含读写分离数据源的java配置示例：

	@Configuration
	@MapperScan(basePackages = {ASqlSessionFactoryConfig.MAPPER_PACKAGE}, sqlSessionFactoryRef = ASqlSessionFactoryConfig.SESSIONFACTORY_NAME)
	public class ASqlSessionFactoryConfig {
	
		/**SqlSessionFactory名称.*/
		public final static String SESSIONFACTORY_NAME = "ASqlSessionFactory";
		/**mapper包路径，必须与其他SqlSessionFactory-mapper路径区分.*/
		public final static String MAPPER_PACKAGE = "com.tangqh.codelearn.mapper.a";
		/**mapper.xml文件路径，必须与其他SqlSessionFactory-mapper路径区分.*/
		public final static String MAPPER_XML_PATH = "classpath:mybatis/*.xml";
	
		//写数据源
		@Bean
		@ConfigurationProperties("spring.datasource.druid.a.write")
		public DataSource writeDataSource(){
			return DruidDataSourceBuilder.create().build();
		}
		//读数据源
		@Bean
		@ConfigurationProperties("spring.datasource.druid.a.query")
		public DataSource queryDataSource(){
			return DruidDataSourceBuilder.create().build();
		}
	
		@Bean
		@Primary //多数据源时(包括读写分离)，必须得用@primary声明一个主数据源
		public DataSource dataSource() {
			DynamicDataSource dataSource = new DynamicDataSource();
			dataSource.setWriteDataSource(writeDataSource());
			dataSource.setQueryDataSource(queryDataSource());
			return dataSource;
		}
	
		@Primary //同上，必须得有一个主SqlsessionFactory
		@Bean(name = "ASqlSessionFactory")
		public SqlSessionFactory ASqlSessionFactory()
				throws Exception {
			final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
			sessionFactory.setDataSource(dataSource());
			sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_XML_PATH));
			return sessionFactory.getObject();
		}
		
		//动态数据源事务管理器，必须创建，并且需要在yml配置suixingpay.transaction.multi.transactionManagerName，值为bean id，即transactionManager
		@Bean
		public DynamicDataSourceTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
			DynamicDataSourceTransactionManager transactionManager = new DynamicDataSourceTransactionManager();
			transactionManager.setDataSource(dataSource);
			return transactionManager;
		}
	}	
不含动态数据源的java配置示例：

	@Configuration
	@MapperScan(basePackages = {BSqlSessionFactoryConfig.MAPPER_PACKAGE}, sqlSessionFactoryRef = BSqlSessionFactoryConfig.SESSIONFACTORY_NAME)
	public class BSqlSessionFactoryConfig {
	
		/**SqlSessionFactory名称.*/
		public final static String SESSIONFACTORY_NAME = "BSqlSessionFactory";
		/**mapper包路径，必须与其他SqlSessionFactory-mapper路径区分.*/
		public final static String MAPPER_PACKAGE = "com.tangqh.codelearn.mapper.b";
		/**mapper.xml文件路径，必须与其他SqlSessionFactory-mapper路径区分.*/
		public final static String MAPPER_XML_PATH = "classpath:mybatis2/*.xml";
	
		@Bean
		@ConfigurationProperties("spring.datasource.druid.write2")
		public DataSource dataSource2() {
			return DruidDataSourceBuilder.create().build();
		}
	
		@Bean(name = "BSqlSessionFactory")
		public SqlSessionFactory ASqlSessionFactory()
				throws Exception {
			final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
			sessionFactory.setDataSource(dataSource2());
			sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_XML_PATH));
			return sessionFactory.getObject();
		}
		
		//此处可以声明对应的TransactionManager，默认不存在会自动新建 普通数据源事务管理器 并注入spring容器中
	
	}




		
