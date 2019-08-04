# MyBatis读写分离数据源插件
作者：邱家榆

---

## 1. 读写分离使用场景

当业务发展到一定程度，单库无法满足性能需求时，通过读写分离提升系统的性能。


## 2. 引入jar

    compile "com.suixingpay.starter:suixingpay-starter-mybatis-dynamic-datasource:xxx"

通过 [http://172.16.60.188:8081/nexus/index.html#nexus-search;quick~suixingpay-starter-mybatis-dynamic-datasource](http://172.16.60.188:8081/nexus/index.html#nexus-search;quick~suixingpay-starter-mybatis-dynamic-datasource)  获取最新版本的jar。


## 3. 用法

1. 配置数据源

	  	@Configuration
		public class DataSourceConfigurer {
		    
		    @Bean("writeDataSource")
		    @ConfigurationProperties("spring.datasource.druid.write")
		    public DataSource writeDataSource() {
		        return DruidDataSourceBuilder.create().build();
		    }
		
		    @Bean("queryDataSource")
		    @ConfigurationProperties("spring.datasource.druid.query")
		    public DataSource queryDataSource() {
		        return DruidDataSourceBuilder.create().build();
		    }
		    
		    @Primary // 注意必须将它设为 Primary
		    @Bean("dataSource")
		    public DataSource dataSource(){
		        DynamicDataSource dataSource = new DynamicDataSource();
		        dataSource.setWriteDataSource(writeDataSource());
		        dataSource.setQueryDataSource(queryDataSource());
		        return dataSource;
		    }
		}

2. 配置事务管理器

		@Configuration
		public class DynamicDataSourceTransactionManagerConfig {
		
		    @Primary
		    @Bean
		    public DynamicDataSourceTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource){
		       DynamicDataSourceTransactionManager transactionManager = new DynamicDataSourceTransactionManager();
		       transactionManager.setDataSource(dataSource);
		       return transactionManager;
		    }
		}
		
3. 原理

   通过事务区分使用读库还是写库：如果是只读事务，使用读库，否则使用写库。

4. 配置事务

  更多内容，请查看 suixingpay-starter-transaction 的说明文档。
  
## 4. 扩展

现在缓存的使用越来越普及，如果为了性能上能有更好的提升，使用缓存代替读写分离会更好些。