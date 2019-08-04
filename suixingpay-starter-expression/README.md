# 表达式处理器

作者：邱家榆

---

## 1. 表达式处理器的作用

在我们开发过程中，表达式的应用其实结果会遇到，只是很多人都没有注意，比如, Spring xml配置中:

    <property name="itemName" value="#{otherBean.name}" />

@Value 中的使用：

    @Value("#{itemBean.name}")
    private String itemName;

以上只是简单的应用，比较复杂的如Spring-cache中的使用：

    @Cacheable(value = "models", key = "#testModel.name", condition = "#testModel.address !=  '' ")
    public TestModel getFromMem(TestModel testModel) throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        testModel.setName(testModel.getName().toUpperCase());
        return testModel;
    }
在这用表达式用于生成缓存key，用于是否要缓存的条件判断等。和它类似用法的有：缓存组件（suixingpay-starter-cache）和 分布式锁以及幂等组件（suixingpay-starter-distributed-lock），在它之用表达式起到了关键性的作用，用组件的使用提供了更加的可能。 

## 2. 导入依赖包

    compile "com.suixingpay.starter:expression:xxx"

通过 [http://172.16.60.188:8081/nexus/index.html#nexus-search;quick~suixingpay-starter-expression](http://172.16.60.188:8081/nexus/index.html#nexus-search;quick~suixingpay-starter-expression)  获取最新版本的jar。

## 3. 使用方法 

    @Autowired
    private AbstractExpressionParser expressionParser;
 
 注意： 默认支持Spring EL表达式
 
	public void expressionParser() throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        map.put("variable","Hello World!");
        String value = expressionParser.getValue("#variable", map, String.class);
        //value的值为 Hello World!
    }