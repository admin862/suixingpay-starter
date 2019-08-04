# 跨域请求配置

作者： 邱家榆

---

## 1. 什么是CORS?

Cross-origin resource sharing(跨域资源共享),是一个W3C标准,它允许你向一个不同源的服务器发出XMLHttpRequest请求,从而克服了ajax只能请求同源服务的限制.并且也可以通过灵活的设置,来指定什么样的请求是可以被授权的.

## 2. 什么是跨域?

假设你在http://xxx.com/test/下有一个js文件,从这个js里发出一个ajax请求请求后端服务,按照如下情况判定:

| 请求地址 | 原因 | 结果 |
| ---| --- | --- |
| http://xxx.com/xxxx/action | 同一域名,不同文件夹 | 非跨域 |
| http://xxx.com/test/action | 同一域名,同一文件夹 | 非跨域 |
| http://a.xxx.com/test/action |不同域名,文件路径相同 | 跨域 |
| http://xxx.com:8080/test/action	 | 同一域名,不同端口 | 跨域 |
| https://xxx.com/test/action | 同一域名,不同协议 |跨域 |

## 3. 还有那些其他的跨域解决方案?

* **JSONP**: 动态添加一个&lt;script&gt;标签，而script标签的src属性是没有跨域的限制的。这样说来，这种跨域方式其实与ajax XmlHttpRequest协议无关了,而缺点也很明显,它只支持GET请求而不支持POST等其它类型的HTTP请求；它只支持跨域HTTP请求这种情况，不能解决不同域的两个页面之间如何进行JavaScript调用的问题
* **NGINX代理**: 通过一个代理服务器,将跨域的请求转发,如:前端JS在http://www.demo.com/a.js,后端是http://www.abc.com/app/action,通过代理可将后端的地址转换成http://www.demo/app/action,这样,从前端发起的请求,就不存在跨域的情况了
* **CORS**: 是支持所有类型的HTTP请求,并且也只是服务端进行设置即可,但是缺点就是老的浏览器不支持CORS(如:IE7,7,8,等)

## 4. CORS的响应头

* **Access-Control-Allow-Origin** : 必须的,允许的域名,如果设置*,则表示接受任何域名
* **Access-Control-Allow-Credentials** : 非必须的,表示是否允许发送Cookie,注意,当设置为true的时候,客户端的ajax请求,也需要将withCredentials属性设置为true
* **Access-Control-Expose-Headers** : 非必须的,表示客户端能拿到的header,默认情况下XMLHttpRequest的getResponseHeader方法只能拿到几个基本的header,如果有自定义的header要获取的话,则需要设置此值
* **Access-Control-Request-Method** : 必须的,表示CORS上会使用到那些HTTP方法
* **Access-Control-Request-Headers** : 必须的,表示CORS上会有那些额外的的有信息

## 5. CORS将请求分为两种类型

两种类型分别为**简单请求**和**非简单请求**,同时满足以下两大条件的请求被定义为是简单请求:

1. 请求方法是以下三种之一:
  * HEAD
  * GET
  * POST
2. HTTP头信息不超出以下几种字段:
  * Accept
  * Accept-Language
  * Content-Language
  * Last-Event-ID
  * Content-Type：只限于三个值application/x-www-form-urlencoded、multipart/form-data、text/plain

对于非简单请求,浏览器会自动发一个预检请求,这个请求是OPTIONS方法的,主要是询问服务器当前请求是否在允许范围内

## 6. 导入依赖包

    compile  "com.suixingpay.starter:suixingpay-starter-cors:xxx"

通过 [http://172.16.60.188:8081/nexus/index.html#nexus-search;quick~suixingpay-starter-cors](http://172.16.60.188:8081/nexus/index.html#nexus-search;quick~suixingpay-starter-cors)  获取最新版本的jar。

## 7. 配置说明 

跨域请求配置

	suixingpay: 
	  cors:
	    enabled: true             # 是否启用，默认为true
	    mapping: /**              # 扫描地址, 默认值为：/**
	    allow-credentials: true   #允许cookie, 默认为true
	    allowed-origins: '*'       #允许的域（如果字符串之中包含空格或特殊字符，需要放在引号之中）,默认值为：*
	    allowed-methods: GET,POST,DELETE,PUT,PATCH  #允许的方法， 默认值为：GET,POST,DELETE,PUT,PATCH
	    allowed-headers: '*'       #允许的头信息（如果字符串之中包含空格或特殊字符，需要放在引号之中），默认值为：*

只要引入jar包，以上配置就会自动生效。如果有个别配置需要调整，只需要调整对应的属性即可。

## 8. 参考资料

[跨域资源共享 CORS 详解](http://www.ruanyifeng.com/blog/2016/04/cors.html)


