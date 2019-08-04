# 校验组件

作者： 马铁利、邱家榆

---

## 快速开始

数据校验是软件开发中保证数据合法性的必要手段，而数据校验的代码非常繁琐，会有非常多的条件判断，揉在一起后会显得非常杂乱，不易于理解和维护。为此我们选择了 hibernate validator 来帮助解决这些问题。

以下是对hibernate-validator中部分注解进行描述：

| 注解 | 验证的数据类型 | 说明 |
| --- | --- | --- |
| @AssertTrue | Boolean,boolean|用于boolean字段，该字段只能为true|  
| @AssertFalse | Boolean,boolean | 该字段的值只能为false |
| @DecimalMax | BigDecimal，BigInteger, byte, short, int, long，等任何Number或CharSequence（存储的是数字）子类型| 只能小于或等于该值|
| @DecimalMin | BigDecimal，BigInteger, byte, short, int, long，等任何Number或CharSequence（存储的是数字）子类型| 只能大于或等于该值|
| @Max | BigDecimal，BigInteger, byte, short, int, long，等任何Number或CharSequence（存储的是数字）子类型 | 该字段的值只能小于或等于该值|
| @Min | BigDecimal，BigInteger, byte, short, int, long，等任何Number或CharSequence（存储的是数字）子类型 | 该字段的值只能大于或等于该值|
| @Range(min=,max=,message=) | BigDecimal，BigInteger, byte, short, int, long，等任何Number或CharSequence（存储的是数字）子类型 | 被注释的元素必须在合适的范围内|
| @Digits(integer=,fraction=) |BigDecimal，BigInteger, byte, short, int, long，等任何Number或CharSequence（存储的是数字）子类型 | 检查是否是一种数字的整数、分数,小数位数的数字|
| @Negative | BigDecimal，BigInteger, byte, short, int, long，等任何Number或CharSequence（存储的是数字）子类型 | 检查是否为负数 |
| @NegativeOrZero | BigDecimal，BigInteger, byte, short, int, long，等任何Number或CharSequence（存储的是数字）子类型 | 检查是否为负数或0 |
| @Positive | BigDecimal，BigInteger, byte, short, int, long，等任何Number或CharSequence（存储的是数字）子类型 | 检查是否为正数 |
| @PositiveOrZero | BigDecimal，BigInteger, byte, short, int, long，等任何Number或CharSequence（存储的是数字）子类型 | 检查是否为正数或0 |
| @Email | CharSequence子类型（如String） | 检查是否是一个有效的email地址|
| @Future | java.util.Date,java.util.Calendar, java.time.Instant, java.time.LocalDate, java.time.LocalDateTime, java.time.LocalTime, java.time.MonthDay, java.time.OffsetDateTime, java.time.OffsetTime, java.time.Year, java.time.YearMonth, java.time.ZonedDateTime, java.time.chrono.HijrahDate, java.time.chrono.JapaneseDate, java.time.chrono.MinguoDate, java.time.chrono.ThaiBuddhistDate; Additionally supported by HV, if the Joda Time date/time API is on the classpath: any implementations of ReadablePartial and ReadableInstant | 检查该字段的日期是否是属于将来的日期|
| @FutureOrPresent | java.util.Date,java.util.Calendar, java.time.Instant, java.time.LocalDate, java.time.LocalDateTime, java.time.LocalTime, java.time.MonthDay, java.time.OffsetDateTime, java.time.OffsetTime, java.time.Year, java.time.YearMonth, java.time.ZonedDateTime, java.time.chrono.HijrahDate, java.time.chrono.JapaneseDate, java.time.chrono.MinguoDate, java.time.chrono.ThaiBuddhistDate; Additionally supported by HV, if the Joda Time date/time API is on the classpath: any implementations of ReadablePartial and ReadableInstant | 检查该字段的日期是否是属于现在或将来的日期 |
| @Past | java.util.Date,java.util.Calendar, java.time.Instant, java.time.LocalDate, java.time.LocalDateTime, java.time.LocalTime, java.time.MonthDay, java.time.OffsetDateTime, java.time.OffsetTime, java.time.Year, java.time.YearMonth, java.time.ZonedDateTime, java.time.chrono.HijrahDate, java.time.chrono.JapaneseDate, java.time.chrono.MinguoDate, java.time.chrono.ThaiBuddhistDate; Additionally supported by HV, if the Joda Time date/time API is on the classpath: any implementations of ReadablePartial and ReadableInstant | 检查该字段的日期是在过去|
| @PastOrPresent | java.util.Date,java.util.Calendar, java.time.Instant, java.time.LocalDate, java.time.LocalDateTime, java.time.LocalTime, java.time.MonthDay, java.time.OffsetDateTime, java.time.OffsetTime, java.time.Year, java.time.YearMonth, java.time.ZonedDateTime, java.time.chrono.HijrahDate, java.time.chrono.JapaneseDate, java.time.chrono.MinguoDate, java.time.chrono.ThaiBuddhistDate; Additionally supported by HV, if the Joda Time date/time API is on the classpath: any implementations of ReadablePartial and ReadableInstant | 检查该字段的日期是在过去或现在|
| @Length(min=,max=) | CharSequence子类型 | length()方法返回的是使用的是UTF-16编码的字符代码单元数量，不一定是实际上我们认为的字符个数。检查所属的字段的长度是否在min和max之间,只能用于字符串|
| @CodePointLength | CharSequence子类型 | codePointCount()方法返回的是代码点个数，是实际上的字符个数。检查所属的字段的长度是否在min和max之间,只能用于字符串|
| @NotNull | 任意类型 | 不能为null|
| @Null | 任意类型 | 检查该字段为空|
| @NotBlank | CharSequence子类型 | 不能为空，检查时会将空格忽略|
| @NotEmpty | CharSequence子类型、Collection、Map、数组 |不能为空，这里的空是指空字符串|
| @Pattern(regex=,flag=) | CharSequence | 被注释的元素必须符合指定的正则表达式|
| @Size(min=, max=) | 字符串、Collection、Map、数组等 | 检查该字段的size是否在min和max之间，可以是字符串、数组、集合、Map等|
| @URL(protocol=,host,port) | CharSequence | 检查是否是一个有效的URL，如果提供了protocol，host等，则该URL还需满足提供的条件|
| @Valid | 任何非原子类型 | 该注解主要用于字段为一个包含其他对象的集合或map或数组的字段，或该字段直接为一个其他对象的引用，这样在检查当前对象的同时也会检查该字段所引用的对象;递归验证，用于对象、数组和集合，会对对象的元素、数组的元素进行一一校验|
| @CreditCardNumber | CharSequence  | 对信用卡号进行一个大致的验证 |
| @Currency | javax.money.MonetaryAmount | 检查货币单位 |
| @DurationMax | java.time.Duration | 最大持续时间 |
| @DurationMin | java.time.Duration | 最小持续时间 |
| @EAN | CharSequence | EAN 条形码 |
| @ISBN | CharSequence | 是否是 ISBN |
| @SafeHtml | CharSequence | Checks whether the annotated value contains potentially malicious fragments such as &lt;script/&gt;. In order to use this constraint, the jsoup library must be part of the class path. With the whitelistType attribute a predefined whitelist type can be chosen which can be refined via additionalTags or additionalTagsWithAttributes. The former allows to add tags without any attributes, whereas the latter allows to specify tags and optionally allowed attributes as well as accepted protocols for the attributes using the annotation @SafeHtml.Tag. In addition, baseURI allows to specify the base URI used to resolve relative URIs. |
| @UniqueElements | Collection | Checks that the annotated collection only contains unique elements. The equality is determined using the equals() method. The default message does not include the list of duplicate elements but you can include it by overriding the message and using the {duplicates} message parameter. The list of duplicate elements is also included in the dynamic payload of the constraint violation. |
@NotNull、@NotBlank、@NotEmpty 这几个用法注意区分

自定义注解：

| 注解 | 说明 |
| --- | --- |
| @Phone | 验证手机号 |
| @IdNumber | 验证身份证号 |

    compile "com.suixingpay.starter:suixingpay-starter-validator:xxx"

到[http://172.16.60.188:8081/nexus/index.html#nexus-search;quick~suixingpay-starter-validator](http://172.16.60.188:8081/nexus/index.html#nexus-search;quick~suixingpay-starter-validator)获取最新版本的jar.

## 常规校验

	@ApiModel
	@Getter
	@Setter
	@Accessors(chain = true)
	public class User extends BaseDomain<Long> {
	
	    private static final long serialVersionUID = 1L;
	
	    @NotBlank(message = "{user.name.null}")
	    @ApiModelProperty("姓名")
	    private String name;
	
	    @Min(18)
	    @Max(100)
	    @ApiModelProperty("年龄")
	    private Integer age;
	
	    @Phone
	    // @Pattern(regexp = "^1(3|4|5|7|8)\\d{9}$", message = "手机号码格式错误")
	    @NotBlank(message = "手机号码不能为空")
	    private String phone;
	    @Email(message = "邮箱格式错误")
	    private String email;
	
	    @IdNumber // (groups={CreateGroup.class})
	    @ApiModelProperty("身份证号")
	    private String idNumber;
	
	}
	
	@RestController
	@RequestMapping("user")
	public class UserController {
	    @PostMapping
	    @ApiOperation(value = "新增", notes = "新增")
	    public User add(@Validated @RequestBody User user) throws BaseException {
	        getService().save(user);
	        return user;
	    }
	
	    @PutMapping(value = "/{id}")
	    @ApiOperation(value = "修改", notes = "修改")
	    public void update(@PathVariable("id") Long id, @Validated @RequestBody User user) throws BaseException {
	        getService().updateById(user);
	    }
	}

## 分组校验

下面例子为，添加操作和修改操作时，需要校验的参数是不一样的。

	@ApiModel
	@Getter
	@Setter
	@Accessors(chain = true)
	public class User extends BaseDomain<Long> {
	
	    private static final long serialVersionUID = 1L;
	
	    @NotNull(message = "{user.name.null}", groups = { CreateGroup.class })
	    @NotBlank(message = "{user.name.null}", groups = { CreateGroup.class })
	    @Length(min = 1, max = 10, message = "姓名长度必须是{min}与{max}之间", groups = { CreateGroup.class, UpdateGroup.class })
	    @ApiModelProperty("姓名")
	    private String name;
	
	    @Min(value = 18, groups = { CreateGroup.class, UpdateGroup.class })
	    @Max(value = 100, groups = { CreateGroup.class, UpdateGroup.class })
	    @ApiModelProperty("年龄")
	    private Integer age;
	
	    @Phone(groups = { CreateGroup.class, UpdateGroup.class })
	    // @Pattern(regexp = "^1(3|4|5|7|8)\\d{9}$", message = "手机号码格式错误")
	    @NotBlank(message = "手机号码不能为空")
	    @Length(min = 1, max = 10, message = "手机号码长度必须是{min}与{max}之间", groups = { CreateGroup.class, UpdateGroup.class })
	    private String phone;
	
	    @Email(message = "邮箱格式错误")
	    @Length(min = 3, max = 30, message = "邮箱长度必须是{min}与{max}之间", groups = { CreateGroup.class, UpdateGroup.class })
	    private String email;
	
	    @IdNumber(groups={CreateGroup.class})
	    @ApiModelProperty("身份证号")
	    @Length(min = 15, max = 18, message = "身份证号长度必须是{min}与{max}之间", groups = { CreateGroup.class, UpdateGroup.class })
	    private String idNumber;
	
	}
	

	@RestController
	@RequestMapping("user")
	public class UserController {
	    @PostMapping
	    @ApiOperation(value = "新增", notes = "新增")
	    public User add(@Validated(CreateGroup.class) @RequestBody User user) throws BaseException {
	        getService().save(user);
	        return user;
	    }
	
	    @PutMapping(value = "/{id}")
	    @ApiOperation(value = "修改", notes = "修改")
	    public void update(@PathVariable("id") Long id, @Validated(UpdateGroup.class) @RequestBody User user) throws BaseException {
	        getService().updateById(user);
	    }
	}
	
## 通过脚本验证

如果需要校验的业务逻辑比较复杂，简单的@NotBlank，@Min注解已经无法满足需求了，这时可以使用@ScriptAssert来指定进行校验的方法，通过方法来进行复杂业务逻辑的校验，然后返回true或false来表明是否校验成功。例如下面的例子：

	//通过script 属性指定进行校验的方法，传递校验的参数
	@ScriptAssert(lang="javascript",script="com.learn.validate.domain.Student.checkParams(_this.name,_this.age,_this.classes)" messgae="")  
	public class Student {  
	  private String name;  
	  private int age;  
	  private String classess;  
	  //注意进行校验的方法要写成静态方法，否则会出现   
	  //TypeError: xxx is not a function 的错误  
	  public static boolean checkParams(String name,int age,String classes) {  
	    if(name!=null && age>8 && classes!=null){  
	      return true;  
	    }else{  
	      return false;  
	    }  
	  }
	}  

## 自定义校验

## 手动校验

	@Autowired
	private Validator validator;
	
	public <T> void validate(T obj) {
	    Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
	    // 抛出检验异常
	    if (constraintViolations.size() > 0) {
	        throw new Exception(String.format("参数校验失败:%s", constraintViolations.iterator().next().getMessage()));
	    }
	}

## 方法级校验

	@RestController
	@Validated
	@RequestMapping("valid")
	public class ValidController {
	    @RequestMapping("/check")
	    @ResponseBody
	    public String check(@Min(value = 2,message = "age必须大于2") int age) {
	        return "" + age;
	    }
	}

## 高级特性

### i18n

把多语言配置放到 ValidationMessages.properties 文件中;

ValidationMessages.properties 必须是用UTF-8编码保存，不能使用native2ascii转成ascii编码


### domain中key约定优于配置(如：user.name.null)(未做)


## 参考资料：

* [https://www.cnblogs.com/mr-yang-localhost/p/7812038.html](https://www.cnblogs.com/mr-yang-localhost/p/7812038.html)

* [https://www.cnblogs.com/softidea/p/6043879.html](https://www.cnblogs.com/softidea/p/6043879.html)

* [http://blog.csdn.net/jin861625788/article/details/73181075](http://blog.csdn.net/jin861625788/article/details/73181075)

* [https://www.ibm.com/developerworks/cn/java/j-cn-hibernate-validator/index.html?lnk=hm](https://www.ibm.com/developerworks/cn/java/j-cn-hibernate-validator/index.html?lnk=hm)

* [https://www.cnblogs.com/mr-yang-localhost/p/7812038.html](https://www.cnblogs.com/mr-yang-localhost/p/7812038.html)

* [https://www.cnblogs.com/atai/p/6943404.html](https://www.cnblogs.com/atai/p/6943404.html)

* [http://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/](http://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/)

