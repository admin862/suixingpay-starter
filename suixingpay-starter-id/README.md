# id starter使用说明
作者：任金昊

## quick start
###1. 引入依赖
	compile('com.suixingpay.starter:suixingpay-starter-id:xxx')
	compile('com.suixingpay.starter:suixingpay-starter-redis:xxx')
	compile('com.suixingpay.starter:suixingpay-starter-exception:xxx')
	compile('com.suixingpay.starter:suixingpay-starter-util:xxx')
###2. 配置
该模块默认引用redis,需添加redis配置，详见suixingpay-stater-redis
###3. 代码调用
	@Autowired
	private IdGenerator idGenerator;
	
	//生成默认id，其中d为namespace，详细含义下面解释
	String id = idGenerator.generate("d");
	//id中添加客户化业务参数
	String id = idGenerator.generate("d", "xxxx");

## 深入理解
###1. id结构

	yyyyMMdd+六位应用id+namespace+业务参数[可选]+序号

###2. 六位应用id
应用id为六位16进制数字，基于redis获取及存储，一个应用实例以spring.application.name+spring.application.index为唯一标识，服务启动时从redis获取一个自增数值作为应用其应用id，一次获取成功后即存入redis，再次启动服务直接取该值，不再获取新的自增数值

###3. namespace
namespace主要用于标识配置组（详细参数将在后面介绍），同时也可具有一定业务含义，建议使用1-3位字母定义

###4. 序号
序号共提供两种实现，分别为基于时间和基于文件记录，可通过下面的参数设置
	
	suixingpay:
	  id:
	    sequenceStrategy:
该参数可选值为time及record，默认为time
####4.1. 基于时间的序号
#####4.1.1. 基本原理说明
基本结构图如下
	     
	-------------------------------      -----------------------------      ----------------------------
    |      TimeSequenceManager     |     |           SPool            |     |          NmPool           |                                                        
    |    结构：Map<Long, SPool>     |---->|   结构：Map<String, NmPool> |---->| 结构：AtomicInteger current|
    |                              |     |       long currentSecond   |     |    int max                |
    --------------------------------     -----------------------------      -----------------------------
    
TimeSequenceManager实现String getNextSequence(String namespace)方法来对外提供seq，基本操作步骤如下

1）获取当前时间（s）,System.currentTimeMillis() / 1000

2）获取当前秒对应的SPool，没有则创建

3）通过SPool获取namespace对应的NmPool，没有则创建

4）通过NmPool获取自增数值，若自增数值大于max则返回-1，否则正常返回

5.1）若正常返回seq值，按照配置格式化seq数值，长度不足左补零，返回

5.2）若返回-1，则阻塞至下一秒后，重新调用getNextSequence(String namespace)方法

#####4.1.2. 参数配置
与该策略相关的配置参数如下

	suixingpay:
	  id:
	    configs:
	      namespace1: #命名空间
	        capacityPs:#该命名空间对应的NmPool的最大值位数，默认6，即其max=999999（步骤4），且格式化的seq值标准长度为6（步骤5.1），该参数可直接理解为一个namespace一秒可以生成sequence的容量

#####4.1.3. 优缺点
优点：依赖时间，仅跟时间有关，且时间单位为秒，相对于投产等操作来说，基本可以忽略，相当于不受任何影响

缺点：序号连续性差；当前秒的long值所占位数较多

####4.2. 基于文件记录的序号
#####4.2.1. 基本思想
先从文件中加载一个序号块（chunk），在该chunk的范围内，程序在内存中自增获取sequence，当加载的chunk使用完毕，到文件中加载下一块，再由内存中自增使用。当服务重启时，可直接从文件中加载下一chunk，确保与服务重启前序号不重复，默认文件路径为./seq.json

#####4.2.2. 参数配置
该策略相关配置参数如下

	suixingpay:
	  id:
	    configs:
	      namespace1://命名空间
	        chunkSize://每次加载chunk的大小，默认10000
	        seqLength://序号格式化的标准长度，不足左补零

#####4.2.3. 异步加载chunk
实现中使用异步加载chunk的方式优化性能，因此为了提高同步性，每使用一个chunk，提前加载下一chunk，内存中永远维护两个chunk，示意图如下：
             
              |       memory         | 	
	          | |-------|  |-------| |
	          | 0       9 10      19 |
	          |   使用中     提前加载  |
	          |                      |
	|-------| | |-------|  |-------| | 
	0       9 |10      19  20     29 | 
	   使用完  |  使用中      提前加载  |
	 	
提前加载的chunk的任务由另一个线程异步执行，相关参数如下：
	
	suixingpay:
	  id:
	    recordTaskDelay: 10#ms，默认10，定时任务启动的延迟时间
	    recordTaskRate: 5#ms，默认5，定时任务的执行间隔
由于可能存在已加载的chunk都已经使用完了，但是新的chunk还没加载好的情况，因此添加参数
	
	suixingpay:
	  id:
	    recordWait: 1000#ms，默认1000
该参数用于定义等待新的chunk加载好的时间，若该时间内新的chunk仍然没加载好，则抛出异常

#####4.2.4. 优缺点
优点：序号连续，除重启前后，序号全部为连续的

缺点：依赖文件，若seq.json不慎丢失，则无法继续保证与之前的序号不重复

###5.自定义实现
配置参数
	
	suixingpay:
	  id:
	    useDefaultImpl: false
该参数将不使用默认实现，开发人员可自定义IdGenerator实现类