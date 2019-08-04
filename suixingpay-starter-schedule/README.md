# schedule starter使用说明
作者：任金昊

## 说明
该模块基于spring schedule封装，支持热加载，隔离运行。

## quick start
###1. 引入依赖
	compile('com.suixingpay.starter:suixingpay-starter-schedule:xxx')
	compile('com.suixingpay.starter:suixingpay-starter-util:xxx')
###2. 开发
####2.1. spring boot主类添加注解@EnableHotLoadingSchedule
	
	@SpringBootApplication
	@EnableHotLoadingSchedule
	public class DemoApplication {
	
####2.2. 开发定时任务

	@Component
	public class Demo {
			
		//定义名称为t1的任务
		@HotLoadingSchedule("t1")
		public void test1() throws InterruptedException {
			Timestamp t = new Timestamp(System.currentTimeMillis());
			System.out.println(t.toString());
		}
	}
		
####2.3. 配置任务参数
配置方式默认为数据库，默认表名为T_SXP_SCHEDULE，表结构见META-INF/default.sql

其中jobname字段对应@HotLoadingSchedule注解的value值，serverid字段表示该任务在哪些应用上执行，格式为ip:port，

如：配置172.16.34.43:8080，则该任务只在172.16.34.43:8080上运行，若需要在多台机器上运行，则配置多组ip:port，用逗号隔开。

注意：该模块未提供多个应用执行时的并发控制，需开发人员自己控制

updatetime字段用于实现动态刷新功能，若修改了一个任务的某些配置，通过修改updatetime（增大）标识该任务需要重新加载

其他字段含义同spring schedule
	
####2.4. 自定义表结果
	开发人员可通过定义DefaultTableInfoProvider的子类，重写其方法，来更改模块使用的表名或字段名
	
	@Component
	public class MposTableInfoProvider extends DefaultTableInfoProvider {
		.....
	}
	
####2.5. 自定义参数配置方式
	开发人员可以通过定义HotLoadingSchedulesInfoProvider接口的实现类，自定义参数配置方式，如读文件等
	
	@Component
	public class MposSchedulesInfoProvider implements HotLoadingSchedulesInfoProvider {
		.....
	}

####2.6. 隔离运行
	定时任务默认为单线程运行，此时所有任务由一个线程执行，若某个任务阻塞，其他任务执行也会收到影响，可通过配置参数
	suixingpay:
	  schedule:
	    runInIsolation: true
解决该问题，该模式将创建与定义任务数相等的线程数，保证任何时刻任何任务至少有一个可用线程

####2.7. 定时任务刷新频率
	定时任务配置变化通过另外一个线程定时扫描，该线程扫描任务有两个参数可供配置
	suixingpay:
	  schedule:
	    refreshInitialDelay: #ms，扫描初始延时时间，默认100000
	    refreshInterval: #ms，扫描间隔，默认30000