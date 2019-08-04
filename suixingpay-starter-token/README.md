# 使用Token进行身份鉴权

作者：邱家榆

---

## 1. 摘要

网站应用一般使用Session进行登录用户信息的存储及验证，而在移动端使用Token则更加普遍。它们之间并没有太大区别，Token比较像是一个更加精简的自定义的Session。Session的主要功能是保持会话信息，而Token则只用于登录用户的身份鉴权。所以在移动端使用Token会比使用Session更加简易并且有更高的安全性，同时也更加符合RESTful中无状态的定义。

更详细内容，请参考：[深入了解 Token 认证的来龙去脉](https://my.oschina.net/jamesfancy/blog/1613994)

## 2. 导入依赖包

    compile "com.suixingpay.starter:suixingpay-starter-token:xxx"

通过 [http://172.16.60.188:8081/nexus/index.html#nexus-search;quick~suixingpay-starter-token](http://172.16.60.188:8081/nexus/index.html#nexus-search;quick~suixingpay-starter-token)  获取最新版本的jar。

## 3. 使用方法 

1. 配置token:

        suixingpay: 
          token:
            single-mode: false #是否启用同一时刻单一会话模式，默认为false
            max-timeout: 12*60*60 #token过期时间：token生成超过maxTimeout，强制token失效，单位秒
            max-tokens-for-one-user: 5 #一个用户允许同时在线的token数量，数量超出时会剔除时间线越早的token，默认值为5，如果设置的值小于等1，则使用默认值
            namespace: 命名空间，默认值为 spring.application.name的值
            pathPatterns: # 需要token验证的路径
            - /**
            exclude-path-patterns: # 排除路径
            - /login
            - /logout
            - /swagger-ui.html
            - /v2/api-docs 
            - /swagger-resources/**
            timeout: 1800 #token过期时间，单位秒, 默认值为1800
            clientRepositoryTypes: # http请求时，传token的方法：
            - PARAM   # 通过http参数传token;
            - HEADER  # 通过http header 传token;
            - COOKIE  # 通过Cookie 传token;
            # 默认同时支持以上三种方式获取token值（通过配置可以实现上面三种方式的各种组合），如果以上三种方式还不能满足要求，可以实现TokenClientRepository接口自定义
            tokenName: SXF-TOKEN # http请求中，用于传输token的参数、header、Cookie名称
            order: 0 # 执行顺序，默认值为Integer.MAX_VALUE
            cookie: # cookie 相关的配置
              domain: # cookie所属的子域 默认值为null,如果要跨域使用时，需要设置此值
              httpOnly: false # 是否将cookie设置成HttpOnly，默认为false
              maxAge: 0 # 设置cookie的最大生存期, 默认值为0
              path: / #设置cookie路径
              secure: false # 是否只允许HTTPS访问, 默认值为false
    除了通过上面的suixingpay.token.exclude-path-patterns 设置排除路径外，还可以在Controller中的方法加@IgnoreToken 注解进行排除。
    
    注意：当token过期时，登出操作需要特殊处理。如果没有把登出的路径给排除，那么客户端会收到401状态码；如果排除了登出路径，那行会返回200状态码。

2. 实现TokenInfo接口，用于保存登录信息，比如：用户ID, 用户角色等，注意：用户ID 已为TokenInfo的成员，且强制为String类型，不需在子类进行声明，例如：

        @Data
        public class AuthenticationUser extends TokenInfo {
            private static final long serialVersionUID = 1L;
            private String[] roles;
    
            @Override
            public String[] getRoles() {
                return roles;
            }
        }
        
    建议在token中只保存用户ID, 需要用户获取用户其它信息时，通过用户ID再去缓存中获取，以保证数据的一致性；如果用户权限验证比较复杂，请实现com.suixingpay.takin.common.token.auth.AuthenticationChecker 接口。
    
3. 登录逻辑处理成功后，保存登录信息到token中，例如：

        @Autowired
        private TokenHelper tokenHelper;
        
        @GetMapping("/login")
        public String login(HttpServletResponse response, String name, String password) {
            // 处理登录逻辑
            User user = doLogin(name, password);
            // 获取角色
            String[] roles = ... ...;
            
            // ... ...
            // 登录成功后, 把登录信息保存到token中
    
            AuthenticationUser tokenInfo = new AuthenticationUser();
            tokenInfo.setUserId(String.valueOf(userId));
            tokenInfo.setRoles(roles);
            return tokenHelper.login(response, userId, tokenInfo); // 将token返回给客户端，如果需要的话。
        }
    登录成功，把登录信息保存到服务器仓库中，并把token返回给客户端
4. 登出时，删除token信息，例如：

        @GetMapping("/logout")
        public boolean logout(HttpServletRequest request, HttpServletResponse response) {
            return tokenHelper.logout(request, response);
        }
    登出成功，把token信息清除，如果客户端有cookie也一并清除
5. 获取当前登录用户的token信息，例如：

        @GetMapping("/")
        public String index(@ApiIgnore AuthenticationUser tokenInfo) {
	        if (null == tokenInfo) {// 如果是忽略路径，可能会是null
	            return "unlogin";
	        }
	        return "Hello " + tokenInfo.getName() + "!";
	    }
    通过AuthenticationUser获取当前登录用户信息，使用@ApiIgnore 不会显示到swagger页面中。
6. 通过@PreAuthorize注解进行权限验证，例如：

        @PreAuthorize({"admin"}) // 必须有admin权限的用户才能访问此路径
        @GetMapping("/admin")
        public String admin() {
            return "You are admin!";
        }
    @PreAuthorize的value可以是一个数组;
7. 清除该用户下所有Token，例如：

		  String userId = "1234567890"
		  tokenHelper.logoutAll(userId);

8. 如果没有登录或token已过期，会返回401 http状态， 如果是没有权限，则返回403状态；

9. 权限验证这块，如果角色比较多，或有些比较个性的需求或默认实现不能满足业务需求的情况下，请实现AuthenticationChecker 接口进行扩展。

10. 服务端默认使用Redis来存储token信息，需要通过spring.redis来配置redis相关设置。如果需要改为其它方式存储token信息，请实现ServerTokenRepository 和 UserTokenRepository接口。


