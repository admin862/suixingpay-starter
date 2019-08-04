# drools starter使用说明
作者：任金昊

# 说明
该模块将原生drools通过maven加载kmodule扩展为支持读取本地目录下的drl文件并打包加载，并针对风控需求，实现根据数据库配置信息自动生成drl文件

# quick start
##1. 引入依赖
	compile('com.suixingpay.starter:suixingpay-starter-drools:xxx')
##2. 开发

###2.1. 配置

	suixingpay:
	  drools:
	    kJarPath: #jar包生成位置，默认/home/app/sxpservice/drools/
	    kJarName: #jar包名称，默认kJar
	    drlPath: #drl文件生成路径，默认/home/app/sxpservice/drools/drl
	    system: #应用系统，默认rcs
###2.2. 使用
	
	@Autowired
	private KieContainer kContainer;//获取container,注意只能用@Autowired获取，不能使用KieServices创建
	...
	kContainer.newKieSession(sessionName);//创建statefulKsession,sessionName含义见2.5
###2.3. DrlWriter
DrlWriter用于生成drl文件到drlPath，仅供rcs可用，使用该类前，需要引入配置
	
	@Configuration
	public class Conf extends DrlGenConfiguration {
	
	}
	
	@Autowired
	private DrlWriter drlWriter;
	...
	drlWriter.write(ruleCode);//按给定ruleCode查询数据库信息生成drl
###2.4. LocalKjarRefresher
	LocalKjarRefresher用于生成jar包并刷新
	
	@Autowired
	private LocalKjarRefresher refresher;
	...
	refresher.refresh();//将drlPath下的所有drl文件生成jar包并生效
	refresher.refresh(String version);//读取kJarPath下已经存在的jar包并生效该jar包，该方法可用于版本回退
###2.5. 多base支持
支持通过定义多个KieBase来分组package，从而使drl文件被分组执行，分组方式为，读取drlPath下的所有存在drl文件的子目录，以该目录作为一个KieBase的package。如下面例子所表示的关系，其sessionName即为package

	    fileSystem     /home/app/sxpservice/drools/drl/aa/bb/cc/test.drl（其中drlPath=/home/app/sxpservice/drools/drl/）
	    -------------------------------------------------------------------------------------------------------------------
	    jar           kjar
	                   |
	                   --------META-INF
	                   |
	                   --------aa
	                            |
	                            ---------bb
	                                      |
	                                      ----------cc
	                                                 |
	                                                 -----------test.drl
	    --------------------------------------------------------------------------------------------------------------------
	    kmodule.xml    <kmodule ...>
	                   	......
	                   	<kbase package="aa.bb.cc" ...>
	                   		<ksession name="aa.bb.cc" .../>
	                   	</kbase>
	                   	......
	                   </kmodule>
                   
###2.6. 查看当前使用的kjar版本
	请求http://ip:port/kjar
###2.7. 其他
生成的drl中，只有被规则处理的类（com.suixingpay.riskengine.message.ContextMessage）是insert产生，其他类（如xxxService）由于其为工具类性质，因此全部使用global引入，其变量名为首字母小写的类名
#流程原理
##1. 生成drl文件（风控使用）
	----------------------------         --------------------------          ------------------
	| 读取数据库配置，封装为Rule对象|------->| 使用freemarker生成drl文件|--------> | 文件保存在drlPath |
	|（DatabaseRuleInfoGetter） |        |     (DrlGenerator)      |          |   (DrlWriter)  |
	|---------------------------         ---------------------------         ------------------
##2. 刷新新版本jar包
     ---------------------                   -----------------------------------------
     |  调用refresh方法   | refresh(version)  |     读取指定releaseId jar包，并更新kmodule|
     |LocalKjarRefresher |------------------>|           LocalKjarRefresher           |
     |--------------------                   ------------------------------------------
             |                                                 /|\
			 |                                                  |
			 |					         ——-----------------------------------------
			 |	refresh()			         | 读取drlPath下的所有drl,生成jar包到kjarPath |
			 |---------------------------------------------> |            KJarGenerator                |
								                             |------------------------------------------