# 跨站请求防范

作者：邱家榆

---

## 1. XSS(跨站脚本攻击)

跨站脚本攻击(Cross Site Scripting)，为不和层叠样式表(Cascading Style Sheets, CSS)的缩写混淆，故将跨站脚本攻击缩写为XSS。恶意攻击者往Web页面里插入恶意Script代码，当用户浏览该页之时，嵌入其中Web里面的Script代码会被执行，从而达到恶意攻击用户的目的。

## 2. 解决思路

1. 基于filter拦截,将特殊字符替换为html转意字符 (如: < 转意为 &amplt;、 > 转意为 &ampgt;、 " 转意为 &ampquot;、' 转意为 &amp; #39;) , 需要拦截的点如下:
  * 请求头 requestHeader
  * 请求体 requestBody
  * 请求参数 requestParameter
2. 涉及到json转换的地方,也将特殊字符替换为html转意字符;

## 3. 导入依赖包

    compile "com.suixingpay.starter:suixingpay-starter-xss:xxx"

通过 [http://172.16.60.188:8081/nexus/index.html#nexus-search;quick~suixingpay-starter-xss](http://172.16.60.188:8081/nexus/index.html#nexus-search;quick~suixingpay-starter-xss)  获取最新版本的jar。

## 4. 使用方法 

跨域请求配置

	suixingpay: 
	  xss:
	    enabled: true # 是否启用，默认值为true
	    filter-name: sxfXssFilter # 过滤器名称，默认值为sxfXssFilter
	    order: 0 # 执行顺序，默认值为0
	    url-patterns: # 匹配路径, 默认值为[/*]
	    - '/*'
	    ignoreUrls: # 忽略路径
	    - '/js/*'
	    - '/css/*'
	    - ’/images/*‘
	    - '/static/*'
	    - '/webjars/*'

## 5. 测试效果

测试时，要对使用参数形式和JSON形式提交的两种情况进行测试，测试方案，在表单输入框中输入，上面提到的哪些特殊字符

__注意：当需要获取 请求头header 参数时，需要从 HttpServletRequest 对象的 getHeader() 方法获取才能进行特殊符号转义__