# 数据建模

作者： 邱家榆

---

此模块用于数据建模，定义一些数据相关的抽象。
	
## 1. BaseDomain的使用

BaseDomain定义了id, creater, updater, createdDate, lastUpdated, version 等通用属性，建议在建表时将这些字段都加上，其实现如下：

	@ApiModel
	@Getter
	@Setter
	@Accessors(chain = true)
	public abstract class BaseDomain<Pk extends Serializable> implements Serializable {
	
	    private static final long serialVersionUID = 1L;
	    @ApiModelProperty("id")
	    Pk id;
	    @ApiModelProperty(value = "创建人", readOnly = true)
	    String creater;
	    @ApiModelProperty(value = "修改人", readOnly = true)
	    String updater;
	    @ApiModelProperty(value = "创建时间", readOnly = true)
	    Date createdDate;
	    @ApiModelProperty(value = "修改时间", readOnly = true)
	    Date lastUpdated;
	    @ApiModelProperty(value = "版本号", readOnly = true)
	    Integer version;
	}
	
使用例子：

	@ApiModel
	@Data
	@EqualsAndHashCode(callSuper=true)
	@Accessors(chain = true)
	public class User extends BaseDomain<Long> {
	
	    /**
	     * @Fields serialVersionUID: 1L
	     */
	    private static final long serialVersionUID = 1L;
	
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
	
	    @IdNumber (groups={CreateGroup.class})
	    @ApiModelProperty("身份证号")
	    @Length(min = 15, max = 18, message = "身份证号长度必须是{min}与{max}之间", groups = { CreateGroup.class, UpdateGroup.class })
	    private String idNumber;
	
	}
	
## 2. 分页查询

定义分页查询需要使用的一些类，比如分页查询条件、查询结果返回、以及分页Service:

分页查询条件：

	public interface IPagination {
	
	    void setSort(Sort sort);
	    
	    /**
	     * 
	     * 排序
	     * @return
	     */
	    Sort getSort();
	
	    /**
	     * 
	     * 是否只是排序，不进行分页查询
	     * @return
	     */
	    boolean idOrderByOnly();
	
	    /**
	     * offset
	     * 
	     * @return
	     */
	    public int getOffset();
	
	    /**
	     * 
	     * limit
	     * @return
	     */
	    public int getLimit();
	}
	
分页查询结果：

	@ApiModel
	public class PageImpl<T> implements Page<T>, Serializable {
	
	    private static final long serialVersionUID = 867755909294344406L;
	    
	    @ApiModelProperty("当前页面数据")
	    private final List<T> content = new ArrayList<T>();
	    
	    @ApiModelProperty("总记录数")
	    private long total;
	    
	    @ApiModelProperty("分页条件")
	    private IPagination pagination;
	
	    public PageImpl(List<T> content, IPagination pagination, long total) {
	        this.content.addAll(content);
	        this.pagination = pagination;
	        this.total = !content.isEmpty() && pagination != null && pagination.getOffset() + pagination.getLimit() > total
	                ? pagination.getOffset() + content.size()
	                : total;
	    }
	
	    public PageImpl(List<T> content) {
	        this(content, null, null == content ? 0 : content.size());
	    }
	
	    @Override
	    public int getNumber() {
	        return pagination == null ? 0 : pagination.getOffset() / pagination.getLimit() + 1;
	    }
	
	    @Override
	    public int getSize() {
	        return pagination == null ? 0 : pagination.getLimit();
	    }
	
	    @Override
	    public int getNumberOfElements() {
	        return content.size();
	    }
	
	    @Override
	    public List<T> getContent() {
	        return content;
	    }
	
	    @Override
	    public boolean hasContent() {
	        return content.size() > 0;
	    }
	
	    @Override
	    public Sort getSort() {
	        return pagination == null ? null : pagination.getSort();
	    }
	
	    @Override
	    public boolean isFirst() {
	        return !hasPrevious();
	    }
	
	    @Override
	    public boolean isLast() {
	        return !hasNext();
	    }
	
	    @Override
	    public int getTotalPages() {
	        return getSize() == 0 ? 1 : (int) Math.ceil((double) total / (double) getSize());
	    }
	
	    @Override
	    public long getTotalElements() {
	        return total;
	    }
	
	    @Override
	    public boolean hasNext() {
	        return getNumber() + 1 < getTotalPages();
	    }
	
	    @Override
	    public boolean hasPrevious() {
	        return getNumber() > 0;
	    }
	
	    @Override
	    public Pageable nextPageable() {
	        return null;
	    }
	
	    @Override
	    public Pageable previousPageable() {
	        return null;
	    }
	
	    @Override
	    public Iterator<T> iterator() {
	        return content.iterator();
	    }
	
	    @Override
	    public <S> Page<S> map(Converter<? super T, ? extends S> converter) {
	        return new PageImpl<S>(getConvertedContent(converter), pagination, total);
	    }
	
	    protected <S> List<S> getConvertedContent(Converter<? super T, ? extends S> converter) {
	        Assert.notNull(converter, "Converter must not be null!");
	        List<S> result = new ArrayList<S>(content.size());
	        for (T element : this) {
	            result.add(converter.convert(element));
	        }
	        return result;
	    }
	
	}

分页查询Service:

	/**
	 * 分页查询服务<br/>
	 * 支持两种方式的分页：<br/>
	 * 1：获取总记录数后进行分页；如果需要显示总页数据，则选此方式<br/>
	 * 2：客户端一直加载数据，直到获取数据量小于pageSize时，认为是最后一页；客户端下拉加载数据通常选有此方式<br/>
	 * 
	 * @author jiayu.qiu
	 * @param <C> 查询条件
	 * @param <R> 查询结果类
	 */
	public interface PageableService<C, R> {
	
	    /**
	     * 分页查询 <br/>
	     * 先查询总记录数据，然后再获取当前页数据<br/>
	     * 
	     * @param condition 查询条件
	     * @param pageNum 从1开始的页码
	     * @param pageSize
	     * @param sort
	     * @return 分页结果
	     */
	    default Page<R> findByPage(C condition, int page, int size, Sort sort) {
	        if (page <= 0) {
	            throw new BadRequestException("page 必须大于0");
	        }
	        if (size <= 0) {
	            throw new BadRequestException("size 必须大于0");
	        }
	        Long totalCnt = count(condition);
	        if (null == totalCnt || totalCnt.intValue() <= 0) {
	            return null;
	        }
	        List<R> list = null;
	        int offset = (page - 1) * size;
	        IPagination pagination = buildPagination(offset, size);
	        pagination.setSort(sort);
	        if (null != totalCnt && totalCnt.intValue() > 0 && offset < totalCnt) {
	            list = find(condition, pagination);
	        } else {
	            list = new ArrayList<>();
	        }
	        return new PageImpl<>(list, pagination, totalCnt);
	    }
	
	    IPagination buildPagination(int offset, int size);
	
	    /**
	     * 根据查询条件，获得总记录数
	     * 
	     * @param condition 查询条件
	     * @return 总记录数
	     */
	    Long count(C condition);
	
	    /**
	     * 根据查询条件，获得数据列表
	     * 
	     * @param condition 查询条件
	     * @param pagination
	     * @return 结果集
	     */
	    List<R> find(C condition, IPagination pagination);
	}
	
## 3. 通用Service的使用

通用Service 代码：

	public interface GenericService<T extends BaseDomain<Pk>, Pk extends Serializable> {
	
	    /**
	     * 添加数据
	     * 
	     * @param entity
	     * @return
	     */
	    Integer add(T entity);
	
	    /**
	     * 修改数据
	     * 
	     * @param entity
	     * @return
	     */
	    Integer updateById(T entity);
	
	    /**
	     * 删除数据
	     * 
	     * @param pk
	     * @return
	     */
	    Integer deleteById(Pk pk);
	
	    /**
	     * 批量删除
	     * 
	     * @param pkList
	     * @return
	     */
	    Integer deleteBatchIds(List<? extends Pk> pkList);
	
	    /**
	     * 查询所有数据
	     * 
	     * @return
	     */
	    List<T> findAll();
	
	    /**
	     * 根据条件进行查询
	     * 
	     * @param entity
	     * @return
	     */
	    List<T> find(T entity);
	
	    /**
	     * 根据主键获取详细数据
	     * 
	     * @param pk
	     * @return
	     */
	    T findOneById(Pk pk);
	}
	
## 4. 枚举

首先为了处理方便，建议在数据库存储枚举的名称，这样Mybatis可以自动转换。但很多系统还是会使用数字来代替，比如：性别，用1表示男性，2表示女性，将这些数据持久化，而代码中为了提高可读性使用枚举表示。我们为这种情况提供了相关的接口和工具类：

	public interface BaseEnum {
	
	    /**
	     * 
	     * @return
	     */
	    int getCode();
	
	    /**
	     * 
	     * 显示名称
	     * @return
	     */
	    String getDisplayName();
	}
	
以性别为例子：

    enum Sex implements BaseEnum {
        MAN(1, "男性"),
        WOMAN(2, "女性");

        private int code;
        private String displayName;

        private Sex(int code, String displayName) {
            this.code = code;
            this.displayName = displayName;
        }

        @Override
        public int getCode() {
            return code;
        }

        @Override
        public String getDisplayName() {
            return displayName;
        }
    }
	
同时提示了转换工具类：

	public class EnumUtil {
	    public static void isEnumType(Class<?> targetType) {
	        if (null == targetType || !targetType.isEnum()) {
	            throw new IllegalArgumentException(
	                    "The target type " + targetType.getName() + " does not refer to an enum");
	        }
	    }
	
	    /**
	     * 根据code获得枚举
	     * 
	     * @param cls
	     * @param code
	     * @return
	     */
	    public static <T extends Enum<? extends BaseEnum>> T getByCode(Class<T> cls, int code) {
	        ... ...
	    }
	
	    /**
	     * 
	     * @param cls
	     * @param name
	     * @return
	     */
	    public static <T extends Enum<?>> T getByName(Class<T> cls, String name) {
	        ... ...
	    }
	}
	
使用例子：

	Sex sex = EnumUtil. getByCode(Sex.class, 1);
	Sex sex = EnumUtil. getByName(Sex.class, "MAN");
	
在suixingpay-starter-web 我们还为BaseEnum 提供了类型自动转换功能。

**注意**：如果数据库是新设计的，建议存储枚举名称，而不是用数字来代替，一方面可读性会更强性，另外一方面Mybatis中可以不需要自定义Typehandler 来处理
	
## 5. 后记

此组件是用于对数据相关的对象进行高度抽象，目的是为了更好的解耦，比如分页查询避免与Mybatis或JPA等ORM进行高度耦合。

导入依赖包

        compile "com.suixingpay.starter:suixingpay-starter-data:xxx"

  通过 [http://172.16.60.188:8081/nexus/index.html#nexus-search;quick~suixingpay-starter-data](http://172.16.60.188:8081/nexus/index.html#nexus-search;quick~suixingpay-starter-data)  获取最新版本的jar。


