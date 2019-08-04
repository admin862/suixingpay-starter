# 验证码starter使用说明
作者：邱家榆

---

## 1. 摘要

Kaptcha是谷歌的一个很有用的验证码生成工具，能够帮助我们生成各种样式的验证码，由于它是可配置的。为了更加适应Spring boot中使用，开发了此starter，同时增加了使用Redis进行保存验证码及实现验证码验证全部功能，下面为你介绍使用方法。


## 2. 导入依赖包

    compile "com.suixingpay.starter:suixingpay-starter-kaptcha:xxx"

通过 [http://172.16.60.188:8081/nexus/index.html#nexus-search;quick~suixingpay-starter-kaptcha](http://172.16.60.188:8081/nexus/index.html#nexus-search;quick~suixingpay-starter-kaptcha)  获取最新版本的jar。

## 3. 验证码相关参数配置

	spring:
	  redis: # 默认使用Redis存储验证码，如果不使用Redis存储，需要实现KaptchaRepository 接口
	    host: 172.16.60.189
	    port: 6379
	    
	suixingpay:
	  kaptcha: # 以下配置如果使用默认值，可以不设置
	    #验证码在缓存中的存活时长
	    expire: 120
	    #是否有边框, 默认为no 可选yes 或者 no
	    border: no
	    # 边框颜色, 默认为Color.BLACK
	    borderColor: black
	    # 边框粗细度 默认为1
	    borderThickness: 1
	    # 验证码图片的宽度 默认200
	    imageWidth: 200
	    # 验证码图片的高度 默认50
	    imageHeight: 50
	    # 验证码文本字符位数 默认为5
	    textproducerCharLength: 5
	    # 验证码文本字体样式,默认为Arial,Courier
	    textproducerFontNames: Arial,Courier
	    # 验证码文本字符大小 默认为40
	    textproducerFontSize: 40
	    #  验证码文本字符颜色 默认为Color.BLUE
	    textproducerFontColor: BLUE
	    #  验证码文本字符内容范围 默认为aAbBcCdDeEfFgG2345678hHjJkKmMLnNpPrRsStTuUvVwWxXyY
	    textproducerCharString: aAbBcCdDeEfFgG2345678hHjJkKmMLnNpPrRsStTuUvVwWxXyY
	    # 验证码文本生成器 默认为DefaultTextCreator
	    textproducerImpl: 
	    # 验证码文本字符间距 默认为2
	    textproducerCharSpace: 2
	    # 验证码生成器 默认为DefaultKaptcha
	    producerImpl: 
	    # 验证码噪点生成对象 默认为DefaultNoise
	    noiseImpl:  
	    # 验证码噪点颜色 默认为Color.BLACK
	    noiseColor: BLACK 
	    # 验证码样式引擎 默认为WaterRipple
	    obscurificatorImpl: 
	    # 验证码文本字符渲染 默认为DefaultWordRenderer
	    wordImpl:  
	    # 验证码背景生成器 默认为DefaultBackground
	    backgroundImpl: 
	    # 验证码背景颜色渐进色 默认为Color.LIGHT_GRAY
	    backgroundClearFrom: LIGHT_GRAY
	    # 验证码背景颜色渐出色 默认为Color.WHITE
	    backgroundClearTo: WHITE

## 4. 生成验证码

	@RestController
	@RequestMapping("/captcha")
	public class CaptchaController {
	    @Autowired
	    public KaptchaHelper kaptchaHelper;
	    /**
	     * 生成验证码
	     */
	    @GetMapping("/gen")
	    public Kaptcha getCaptchaCode(HttpServletResponse response) throws Exception {
	        response.setDateHeader("Expires", 0);
	        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
	        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
	        response.setHeader("Pragma", "no-cache");
	        return kaptchaHelper.createImage();
	    }
	}

## 5. 页面展示验证

以登录页为例子：

	<!DOCTYPE html>
	<html lang="zh-CN">
	<head>
	<title>登录</title>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<script type="text/javascript" src="/webjars/jquery/3.2.1/jquery.min.js"></script>
	</head>
	<body>
	  <form id="loginForm" action="auth/login" method="post">
	    <input type="text" id="name" name="name" placeholder="请输入登录名" /><br> 
	    <input type="password" id="pass" name="pass" placeholder="请输入密码" /><br> 
	    <input type="text" id="code" name="code" placeholder="请输入验证码" /><br>
	    <span> 
	    <img id="codeimg" width="200" height="50" src="" /> 
	    <input type="hidden" id="codekey" name="codekey" />
	    </span><br>
	    <button type="submit">登录</button>
	  </form>
	</body>
	</html>
	<script type="text/javascript">
	$.getJSON("captcha/gen",function(res){
	    $('#codeimg').attr('src', res.base64Code);
	    $('#codekey').val(res.key);
	  });
	</script>

## 6. 验证码校验

	@RestController
	@RequestMapping("/auth")
	public class AuthController {
	    @Autowired
	    public KaptchaHelper kaptchaHelper;
	    @PostMapping("/login")
	    public Boolean login(//
	            String name, //
	            String pass, //
	            String codekey, //
	            String code) {
	        return kaptchaHelper.check(codekey, code);
	    }
	}