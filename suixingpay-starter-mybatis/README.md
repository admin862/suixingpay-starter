# Mybatis

作者：马铁利、邱家榆

---

此模块并没有对Mybatis进行封装及扩展，而是定义了一些针对单表操作的通用接口。通过使用这些能用接口，以减少编码量。
	
## 1. 通用Mapper的使用

通用Mapper中定义了常用的CURD功能：

	public interface GenericMapper<T, Pk extends Serializable> {
	
	    /**
	     * 
	     * 添加数据
	     * @param entity
	     * @return
	     */
	    Integer add(T entity);
	
	    /**
	     * 
	     * 修改数据
	     * @param entity
	     * @return
	     */
	    Integer updateById(T entity);
	
	    /**
	     * 
	     * 删除数据
	     * @param pk
	     * @return
	     */
	    Integer deleteById(Pk pk);
	
	    /**
	     * 
	     * 批量删除功能
	     * @param pkList
	     * @return
	     */
	    Integer deleteBatchIds(List<? extends Pk> pkList);
	
	    /**
	     * 
	     * 查询所有数据功能
	     * @return
	     */
	    List<T> findAll();
	
	    /**
	     * 
	     * 根据ID查询详情
	     * @param pk
	     * @return
	     */
	    T findOneById(Pk pk);
	
	    /**
	     * 根据查询条件统计总记录数据
	     * 
	     * @param condition
	     * @return
	     */
	    Long count(T condition);
	
	    /**
	     * 根据查询条件获取数据
	     * 
	     * @param condition 查询条件
	     * @param pagination 分页信息
	     * @return
	     */
	    List<T> find(T condition, Pagination pagination);
	
	}
	
使用例子：

	public interface UserMapper extends GenericMapper<User, Long> {
	
	}
	
## 2. 通用Service的使用
	
通常抽象Service：

public abstract class AbstractService<T, Pk extends Serializable>
        implements GenericService<T, Pk>, PageableService<T, T> {

    protected final Logger logger = LoggerFactory.getLogger(getcalsses());

    @SuppressWarnings("rawtypes")
    public Class getcalsses() {
        return this.getClass();
    }

    protected abstract GenericMapper<T, Pk> getMapper();

    @Override
    public Integer add(T entity) {
        return getMapper().add(entity);
    }

    @Override
    public Integer updateById(T entity) {
        return getMapper().updateById(entity);
    }

    @Override
    public Integer deleteById(Pk pk) {
        return getMapper().deleteById(pk);
    }

    @Override
    public Integer deleteBatchIds(List<? extends Pk> pkList) {
        return getMapper().deleteBatchIds(pkList);
    }

    @Override
    public List<T> findAll() {
        return getMapper().findAll();
    }

    @Override
    public T findOneById(Pk pk) {
        return getMapper().findOneById(pk);
    }

    @Override
    public Long count(T condition) {
        return getMapper().count(condition);
    }

    @Override
    public List<T> find(T condition, IPagination pagination) {
        return getMapper().find(condition, (Pagination) pagination);
    }

    @Override
    public List<T> find(T condition) {
        return getMapper().find(condition);
    }

    @Override
    public final IPagination buildPagination(int offset, int size) {
        return new Pagination(offset, size);
    }

}
	
使用例子：
	
	@Service
	public class UserService extends AbstractService<User, Long> {
	
	    @Autowired
	    private UserMapper userMapper;
	
	    @Override
	    protected GenericMapper<User, Long> getMapper() {
	        return userMapper;
	    }
	}
	
如果业务中有些复杂的查询条件，默认的Domain无法满足，那就可以通过继承Domain进行扩展，比如，需要查询某一年龄区间的用户：

	@Data
	@EqualsAndHashCode(callSuper=true)
	public class UserCondition extends User {
	
	    private static final long serialVersionUID = 376950899236276850L;
	    
	    private Integer minAge;
	    
	    private Integer maxAge;
	
	}
	
UserMapper.xml 的实现：
	
	<sql id="dynamicWhere">
		<where>
		  <if test="name != null"> AND name =#{name}  </if>
		  <if test="age != null"> AND age = #{age}  </if>
		  <if test="minAge != null"> AND age &gt;= #{minAge}  </if>
		  <if test="maxAge != null"> AND age &lt;= #{maxAge}  </if>
		  <if test="phone != null"> AND phone = #{phone}  </if>
		  <if test="email != null"> AND email = #{email}  </if>
		</where>
	</sql>
	  
	<select id="count" parameterType="UserCondition" resultType="java.lang.Long">
		select count(*) from
		<include refid="table" />
		<include refid="dynamicWhere" />
	</select>
		
	<select id="find" parameterType="UserCondition" resultMap="result">
		select
		<include refid="columns" />
		from
		<include refid="table" />
		<include refid="dynamicWhere" />
	</select>
	
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
	
关于 GenericRestController 的实现请查看suixingpay-starter-web 的说明。

## 4. 类型转换

1. 枚举转换

  如果数据库中保存的是枚举的name的话，MyBatis不需要做任何处理，直接就可以互相转换。但有些系统还是使用数字来表示的，那就需要使用 TypeHandler 来进行转换，使用起来会比较麻烦，在这我们提供针对BaseEnum的抽象TypeHandler：BaseEnumTypeHandler。

  Sex枚举：
  
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

  SexTypeHandler:

		public class SexTypeHandler extends BaseEnumTypeHandler<Sex> {
		}

2. JSON转换

  有时为了保证数据库的扩展性，会将一些数据使用JSON格式进行保存，为此我们提供了AbstractJsonTypeHandler。比如需要将JSON转为Map，只需要继承AbstractJsonTypeHandler：
  
		public JsonToMapTypeHandler extends AbstractJsonTypeHandler<Map<String, Object>> {
		
			public JsonToMapTypeHandler() {
				supper(new TypeReference<HashMap<String, Object>>() {  
				});
			}
		}
		
3. 常用的类型转换器

  IntegerArrayTypeHandler, IntegerListTypeHandler, IntegerSetTypeHandler, LongArrayTypeHandler, LongListTypeHandler, LongSetTypeHandler, StringArrayTypeHandler, StringListTypeHandler, StringSetTypeHandler
  
关于TypeHandler在Mapper.xml文件中如何配置，请大家自己去了解一下，在这不过多介绍。
	
## 5. 后记

使用通用接口，能减少代码量，但也带来了其它问题，比如，后期如果需要加缓存时，需要@Override 原有实现。

导入依赖包

        compile "com.suixingpay.starter:suixingpay-starter-mybatis:xxx"

  通过 [http://172.16.60.188:8081/nexus/index.html#nexus-search;quick~suixingpay-starter-mybatis](http://172.16.60.188:8081/nexus/index.html#nexus-search;quick~suixingpay-starter-mybatis)  获取最新版本的jar。