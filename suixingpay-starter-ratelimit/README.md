# ratelimit starter使用说明
作者：任金昊

# 说明
该模块通过限流手段，解决高并发下，请求量持续超过系统负载能力时可能产生的响应时间变长、内存溢出等问题，提高应用的高可用性

# quick start
##1. 引入依赖
	compile('com.suixingpay.starter:suixingpay-starter-ratelimit:xxx')
##2. 配置
该模块共实现三种限流算法，分别为漏桶算法、令牌桶算法、信号量算法，开发人员可通过配置以下参数选择，默认为令牌桶算法
	
	suixingpay:
	  ratelimit:
	    rateLimitStrategy: #可选值leaky（漏桶）、token（令牌桶）、semaphore（信号量）
	    
###2.1. 漏桶算法与令牌桶算法
####2.1.1. 算法说明
基本含义见https://blog.csdn.net/tianyaleixiaowu/article/details/74942405

在本实现中，漏桶算法及令牌桶算法仅用于做容量限制，所有通过容量限制的请求，再另外根据qps控制执行速率
####2.1.2. 参数
	
	suixingpay:
	  ratelimit:
	    #漏桶算法参数
	    leakyBucket:
	      bucketSize:#桶容量，默认300
	      outflowTimeWindowMs:#流出时间窗口（ms），该参数与outflowSize配合用于计算流出速率，默认1000
	      outflowSize:#outflowTimeWindowMs内的流出数量，默认300
	      inflowWaitTimeoutMs:#流入等待时间（ms），当桶容量满时，新请求将阻塞该时间以等待桶的空余空间，默认0
	      qps:成功进入桶中的请求，将以qps的定义均匀分布在一秒内，如qps=500，那么这些请求将每隔2ms执行一个，默认500
	    #令牌桶算法
	    tokenBucket:
	      bucketSize:#桶容量，默认300
	      addTimeWindowMs:#添加令牌时间窗口（ms），该参数与addSize配合用于计算添加速率，默认1000
	      addSize:#addTimeWindowMs内的添加数量，默认300
	      acquireTimeoutMs:#请求等待时间（ms），当桶内没有令牌时，新请求将阻塞该时间以等待桶的可用令牌，默认0
	      qps:成功获取令牌的请求，将以qps的定义均匀分布在一秒内，如qps=500，那么这些请求将每隔2ms执行一个，默认500
####2.1.3. 注意事项
开发人员应该在了解各参数含义的基础上谨慎配置，否则可能会出现影响性能的情况，如bucketSize=10000，qps=100，这可能会导致最多有10000个请求被成功接收，但由于qps过小，致使后面的请求被阻塞很长时间，大大增加了它的响应时间

###2.2. 信号量算法
####2.2.1. 算法说明
该算法将建立一个信号池并放满信号，接受一个请求后，先去信号池中请求获取信号，然后执行请求，执行完成后将信号放回池中，若池中没有信号可用，则拒绝该请求。该算法实现的效果是，任意时间点，限制最多允许执行的请求数量
####2.2.2. 参数

	suixingpay:
	  ratelimit:
	    semaphore:
	      maxSemaphore:#最大信号量，即信号池大小，默认300
		 acquireTimeoutMs:#请求等待时间（ms），无信号可用时，阻塞该时间以等待其他请求归还可用信号，默认0

###2.3. 其他
本模块默认基于filter实现，用于对web服务的http请求进行限流，包含以下参数

	suixingpay:
	  ratelimit:
	    urlPatterns:#过滤器拦截的urlPatterns，默认/*
	    errorStatusCode:#被降级请求的错误码，默认503
	    errorMsg:#被降级请求的错误描述，默认“由于访问频率限制，该请求被降级”
	    
除默认限流http请求外，也可单独使用RateLimitContext进行任何服务端请求限流
	
	@Autowired
	private RateLimitContext rateLimitContext;
	
	...
	rateLimitContext.execute(Invocation invocation);//需要将目标逻辑实现为Invocation
	    