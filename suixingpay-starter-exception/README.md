# 统一异常处理

作者：汤启华、邱家榆

------


## 1. 包含两方面的统一

1. 统一定义异常类型
2. 统一拦截异常，统一处理并以统一的格式返回给客户端；

## 2. 统一定义异常类型

使用现在业内流行的RESTful 来定义接口规范([理解RESTful架构](http://www.ruanyifeng.com/blog/2011/09/restful.html)、[RESTful API 设计指南](http://www.ruanyifeng.com/blog/2014/05/restful_api))，其中对于业务状态码也有相应的约定，即使用**http 的状态码**表示接口执行结果的状态，下面列出一些例子：

| 状态码 | 含义 | 异常类 |
| --- | --- | --- |
| 200 | 执行成功 | 无|
| 304 | 未修改 | NotModifiedException |
| 400 | 请求出现错误，比如请求头不对、参数验证失败等，所有不想明确区分的客户端请求出错都可以返回 400 | BadRequestException |
| 401 | 未登录或登录已过期 | UnauthorizedException |
| 403 | 没有权限 | ForbiddenException |
| 404 | 未找到记录 | NotFoundException |
| 500 | 业务处理错误 | **SystemException** |
| 503 | 服务不可用 | ServiceUnavailableException |

以上是统一定义的所有异常，它们都是com.suixingpay.takin.exception.BaseException 的子类。
需要扩展异常类需要继承 BaseException，下面给出例子：

    /
    **
    * 404 业务异常
    */
    public class NotFoundException extends BaseException {
        private static final long serialVersionUID = 6586938551931874299L;
        public NotFoundException() {
            super(ExceptionCode.NOT_FOUND, ExceptionCode.NOT_FOUND.defaultMessage());
        }
        public NotFoundException(String message) {
            super(ExceptionCode.NOT_FOUND, message);
        }
        public NotFoundException(Throwable throwable) {
            super(ExceptionCode.NOT_FOUND, throwable);
        }
    }
**注意**：这里定义的状态码非常少，但足以满足需要，请不要再自行定义其它状态码。状态码越多，维护成本越高，理解成本也越高，并不是越多越好，也地增加前端处理的复杂度。**http 的状态码**表示接口执行结果的状态可以统一状态码的使用规范，使用网关接入时，网关不需要考虑兼容各个服务的异常状态码。

## 3. 全局异常处理

1. Controller 层直接把异常往外抛，由统一异常处理器来处理：

  例如：

		@PostMapping("/login")
		public String login(){
			//用户验证操作......
			if (验证不通过){
			    throw new BadRequestException("用户名或密码错误");
			}
			//用户登录操作......
		}

  **减少代码中对异常的处理,使代码更加简洁**

2. 异常信息以统一格式返回给客户端

  接口执行结果状态码，与http状态码保持同步，比如触发BadRequestException时，http的状态码为：400；使用http状态码后，在结合swagger使用时，swagger能很好展现各个状态码代表的含义。详细内容，请查看suixingpay-starter-swagger2的说明。
  
  ResponseBody中直接返回错误信息，而不是使用JSON格式

3. 消除接口统一返回格式设计：

  在设计接口时，经常会看来下面的格式的返回设计：
  
        public class Response<T> {
            private T data;
            private int code;
            private String message;
        }
        
  这样的设计能很好统一返回格式，但是存在以下几个问题：
  * 所有接口都要实例化Response，并设置返回值，状态码等，增加了编码量；
  * 客户端在解析返回值时，把调用接口成功处理的逻辑会与调用接口失败的逻辑耦合；
  * 作为服务端RPC调用时，也会增加客户端处理复杂度；
  
  基于上面问题的考虑，接口设计成与本地调用一样的风格会更好些，如下所示：
  
	     @RestController
	     @RequestMapping("/user")
		  public class UserController {
		
		    @PostMapping
		    @ApiOperation(value = "新增", notes = "新增")
		    public User add(@Validated @RequestBody User user) throws BaseException {
		        getService().save(user);
		        return object;
		    }
		
		    @PutMapping(value = "/{id}")
		    @ApiOperation(value = "修改", notes = "修改")
		    public void update(@PathVariable("id") Long id, @Validated @RequestBody User user) throws BaseException {
		        getService().updateById(user);
		    }
		
		    @DeleteMapping(value = "/{id}")
		    @ApiOperation(value = "删除", notes = "删除")
		    public void delete(@PathVariable("id") Long id) {
		        getService().deleteById(id);
		    }
		
		    @GetMapping(value = "/{id}")
		    @ApiOperation(value = "查询明细", notes = "查询明细")
		    public User info(@PathVariable("id") Long id) {
		        User user = getService().findOneById(id);
		        if (user == null) {
		            throw new NotFoundException();
		        }
		        return user;
		    }
		
		    @GetMapping
		    @ApiOperation(value = "查询列表", notes = "查询列表")
		    public Page<T> list(User t, //
		            @RequestParam(value = "pageNum", defaultValue = "1", required = false) Integer pageNum, //
		            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
		        return getService().findByPage(t, pageNum, pageSize, null);
		    }
		    
		    @GetMapping(value = "hello")//produces=MediaType.APPLICATION_JSON_UTF8_VALUE
		    @ApiOperation(value = "hello", notes = "hello")
		    public String hello2() {
		        // 如果只返回字符串，而前端需要使用json来解析这个字符串，需要手动转成Json字符串
		        return toJson("Hello");
		    }
		
		}

   注：需要注意的地方时，如果接口返回值为字符串时，Spring MVC不会转成JSON格式，客户端解析时会出错，所以服务端需要针对这种情况单独处理。
   
4. 全局异常处理会影响系统的性能，但为什么还要这样使用？如果只考虑这方面，就太片面。对于一个正常的系统来说，其百分之九十的请求都应该是要正常处理的，所以剩下需要异常处理的请求是比较少的，因此其对系统的影响是非常小的。

## 4. 引入依赖包

    compile "com.suixingpay.starter:suixingpay-starter-exception:xxx"

从[http://172.16.60.188:8081/nexus/index.html#nexus-search;gav~com.suixingpay.starter~suixingpay-starter-exception](http://172.16.60.188:8081/nexus/index.html#nexus-search;gav~com.suixingpay.starter~suixingpay-starter-exception)获取最新版本

**注意**:引入com.suixingpay.starter:suixingpay-starter-web:xxx时就依赖了此包

## 5. 配置说明

**yml配置**

	suixingpay:
	  exception:
	    enabled: true #是否启用全局异常处理器
	    char-set: UTF-8 #字符集
	    error-page-url: /errorpage # 错误页面路径, 如果是前后端分离系统，不需要配置此项，如果配置此项，非ajax请求的异常都会跳转到此页面
	    send-error-type: forward # 前后端分离忽略此项，页面跳转方式 forward或redirect
	    code-name: errorCode # 前后端分离忽略此项，通过forward或redirect 到 error-page-url（错误页面）时，错误码的参数名称
	    error-message-name: errorMessage # 前后端分离忽略此项，通过forward或redirect 到 error-page-url（错误页面）时，错误信息参数名称
	    show-error-field-name: true #是否显示验证失败的字段名称
	    return-unkown-exception-message: true #是否返回未知异常信息，例如网络连接超时等。
        unkown-exception-message: "未知错误" #自定义未知异常信息，如果不自定义，默认为null，会获取Exception中的Message

## 6. 参考资料

[https://docs.spring.io/spring-boot/docs/1.5.6.RELEASE/reference/htmlsingle/#boot-features-error-handling](https://docs.spring.io/spring-boot/docs/1.5.6.RELEASE/reference/htmlsingle/#boot-features-error-handling)