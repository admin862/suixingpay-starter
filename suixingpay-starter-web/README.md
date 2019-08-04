# web

作者：邱家榆

---

使用现在业内流行的RESTful 来定义接口规范，参考文档：

* [RESTful HTTP的实践](http://www.infoq.com/cn/articles/designing-restful-http-apps-roth)
* <a href="./doc/REST_cn.pdf" target="_blank">架构风格与基于网络的软件架构设计</a>
* <a href="./doc/Fielding-PhD-thesis-on-REST.pdf" target="_blank">架构风格与基于网络的软件架构设计(中文修订版)</a>
* [理解RESTful架构](http://www.ruanyifeng.com/blog/2011/09/restful.html)
* [RESTful API 设计指南](http://www.ruanyifeng.com/blog/2014/05/restful_api)

导入依赖包

        compile "com.suixingpay.starter:suixingpay-starter-web:xxx"

通过 [http://172.16.60.188:8081/nexus/index.html#nexus-search;quick~suixingpay-starter-web](http://172.16.60.188:8081/nexus/index.html#nexus-search;quick~suixingpay-starter-web)  获取最新版本的jar。


## 1. BaseController

在BaseController 实现了一些常用的方法，比如抛异常，把字符串转json等：

	public abstract class BaseController {
	
	    /**
	     * 将字符串转为Json 字符串。 由于Spring MVC的方法，直接返回字符串时，不会转成json格式的。
	     * 如果接口只返回字符串给客户端，客户端又希望是json格式的，则需要调用此方法，将字符串转成json字符串。
	     * 
	     * @param string
	     * @return
	     */
	    protected String toJson(String string) {
	        return JsonUtil.objectToJson(string);
	    }
	
	    /**
	     * 请求错误，比如：参数验证失败
	     * 
	     * @throws BaseException
	     */
	    protected void badRequest() throws BaseException {
	        throw new BadRequestException();
	    }
	
	    /**
	     * 请求错误，比如：参数验证失败
	     * 
	     * @param message
	     * @throws BaseException
	     */
	    protected void badRequest(String message) throws BaseException {
	        throw new BadRequestException(message);
	    }
	
	    /**
	     * 没有权限
	     * 
	     * @throws BaseException
	     */
	    protected void forbidden() throws BaseException {
	        throw new ForbiddenException();
	    }
	
	    /**
	     * 没有权限
	     * 
	     * @param message
	     * @throws BaseException
	     */
	    protected void forbidden(String message) throws BaseException {
	        throw new ForbiddenException(message);
	    }
	
	    /**
	     * 没有找到数据
	     * 
	     * @throws BaseException
	     */
	    protected void notFound() throws BaseException {
	        throw new NotFoundException();
	    }
	
	    /**
	     * 没有找到数据
	     * 
	     * @param message
	     * @throws BaseException
	     */
	    protected void notFound(String message) throws BaseException {
	        throw new NotFoundException(message);
	    }
	
	    /**
	     * 未修改
	     * 
	     * @throws BaseException
	     */
	    protected void notModified() throws BaseException {
	        throw new NotModifiedException();
	    }
	
	    /**
	     * 未修改
	     * 
	     * @param message
	     * @throws BaseException
	     */
	    protected void notModified(String message) throws BaseException {
	        throw new NotModifiedException(message);
	    }
	
	    /**
	     * 业务错误或系统错误
	     * 
	     * @throws BaseException
	     */
	    protected void error() throws BaseException {
	        throw new SystemException();
	    }
	
	    /**
	     * 业务错误或系统错误
	     * 
	     * @param message
	     * @throws BaseException
	     */
	    protected void error(String message) throws BaseException {
	        throw new SystemException(message);
	    }
	
	    /**
	     * 未登录
	     * 
	     * @throws BaseException
	     */
	    protected void unauthorized() throws BaseException {
	        throw new UnauthorizedException();
	    }
	
	    /**
	     * 未登录
	     * 
	     * @param message
	     * @throws BaseException
	     */
	    protected void unauthorized(String message) throws BaseException {
	        throw new UnauthorizedException(message);
	    }
	}
	

**注**：由于Spring MVC的方法，直接返回字符串时，不会转成json格式的。 如果接口只返回字符串给客户端，客户端又希望是json格式的，可以有两种处理方式：1、调用toJson方法转成json后再返回给客户端；2：返回Optional&lt;String&gt;代替。

## 2. 通用REST Controller: GenericRestController

使用类似RPC调用风格，对接口返回值无需二次封装，请结合suixingpay-starter-exception 相关说明进行理解。

GenericRestController 源码：

	/**
	 * RestController
	 * 
	 * @author: qiujiayu[qiu_jy@suixingpay.com]
	 * @date: 2018年1月22日 下午1:10:40
	 * @version: V1.0
	 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年1月22日 下午1:10:40
	 * @param <T> Domain
	 * @param <Pk> 主键
	 * @param <C> 查询条件
	 */
	public abstract class GenericRestController<T, Pk extends Serializable, C extends T> extends BaseController {
	
	    protected Logger logger = LoggerFactory.getLogger(getcalsses());
	
	    protected Class<?> getcalsses() {
	        return this.getClass();
	    }
	
	    @PostMapping
	    @ApiOperation(value = "新增", notes = "新增")
	    public void add(@Validated @RequestBody T object) throws BaseException {
	        getService().add(object);
	    }
	
	    @PutMapping(value = "/{id}")
	    @ApiOperation(value = "修改", notes = "修改")
	    public void update(@PathVariable("id") Pk id, @Validated @RequestBody T object) throws BaseException {
	        getService().updateById(object);
	    }
	
	    @DeleteMapping(value = "/{id}")
	    @ApiOperation(value = "删除", notes = "删除")
	    public void delete(@PathVariable("id") Pk id) {
	        getService().deleteById(id);
	    }
	
	    @GetMapping(value = "/{id}")
	    @ApiOperation(value = "查询明细", notes = "查询明细")
	    public T info(@PathVariable("id") Pk id) {
	        T t = getService().findOneById(id);
	        if (t == null) {
	            throw new NotFoundException();
	        }
	        return t;
	    }
	
	    @GetMapping
	    @ApiOperation(value = "查询列表", notes = "查询列表")
	    public Page<T> list(C t, //
	            @RequestParam(value = "pageNum", defaultValue = "1", required = false) Integer pageNum, //
	            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
	        return getService().findByPage(t, pageNum, pageSize, null);
	    }
	    protected abstract AbstractService<T, Pk> getService();
	}
	
如果是**单表**操作的Controller可以继承GenericRestController， 否则都继承BaseController，例子如下：

User的查询条件：

	@Data
	@EqualsAndHashCode(callSuper=true)
	public class UserCondition extends User {
	
	    private static final long serialVersionUID = 376950899236276850L;
	    
	    private Integer minAge;
	    
	    private Integer maxAge;
	
	}
	
Controller中调用 方法：

	@RestController
	@RequestMapping("user")
	public class UserController extends GenericRestController<User, Long, UserCondition> {
	
	    @Autowired
	    private UserService userService;
	    
	    @Override
	    protected AbstractService<User, Long> getService() {
	        return userService;
	    }
	    ... ...
	}
	


## 3. 类型自动转换功能

### 1. 字符串转日期

Spring MVC 中为我们提供了@DateTimeFormat注解，用于自动将客户端提交给服务端的字符串转为Date的，这种方式一方面不够智能，只能支持一种格式的日期或时间，另一方面需要手动加注解，会比较繁琐。为此在此组件中，我们提供“智能”的日期转换功能，支持下列所有格式的字符串转为日期：

* yyyy-MM-dd HH:mm:ss
* yyyy-MM-dd HH:mm
* yyyy-MM-dd
* yyyyMMddHHmmss
* yyyyMMddHHmm
* yyyyMMdd
* yyMMddHHmmss
* yyMMdd
* HH:mm:ss
* 时间戳

例子：

    private static final String CN_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @GetMapping("/date")
    public String dateTest(@RequestParam Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(CN_DATE_TIME_FORMAT);
        return toJson(formatter.format(date));
    }

但是这种方式有个弊端，它会使得@DateTimeFormat注解 失效，如果还是希望使用@DateTimeFormat注解，那可以通过下面的配置关闭此功能：

	suixingpay: 
	  web:
	    converter:
	      string-to-date:
	        enabled: false # 设置为false关闭此功能，默认值为true

### 2. 枚举类型自动转换

首先在接口如果有枚举的话，建议使用枚举名称作为参数值，因为Spring MVC默认就支持将枚举名称自动转为枚举，但是有些系统使用数字来定义类型的，比如用1，表示男性，2表示女性：

    enum Sex implements BaseEnum {
        MAN(1, "男性"),
        WOMAN(2, "女性");

        private int code;
        private String name;

        private Sex(int code, String name) {
            this.code = code;
            this.name = name;
        }

        @Override
        public int getCode() {
            return code;
        }

        @Override
        public String getName() {
            return name;
        }
    }
    
**注**：BaseEnum 的相关说明请到 suixingpay-starter-data 中查找。

老的方式：

    @GetMapping("/enum")
    public Sex enumTest(@RequestParam int sexCode) {
        return BaseEnumUtil.getByCode(Sex.class, sexCode);
    }

上面需要手动来转换，如果枚举比较多，使用起来会是比较麻烦的，为此我们增加了针对实现BaseEnum的枚举，增加了自动转换功能，一方面支持通过code转为枚举，同时也支持使用枚举名称转为枚举：

    @GetMapping("/enum")
    public Sex enumTest(@RequestParam Sex sex) {
        return sex;
    }
    
如果不希望使用此功能，可以通过下面配置进行关闭：

	suixingpay: 
	  web:
	    converter:
	      string-to-base-enum:
	        enabled: false # 设置为false关闭此功能，默认值为true
