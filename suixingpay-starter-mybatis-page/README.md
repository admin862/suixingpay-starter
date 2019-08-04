# Mybatis 分页插件
作者：邱家榆

---

## 1. 为什么自研分页插件？

网上已经有一些非常不错的 Mybatis 分页插件，比如：[Mybatis-PageHelper](https://github.com/pagehelper/Mybatis-PageHelper)，在这也和它做个简单的对比。

首先来看一下Mybatis-PageHelper的用法，[https://github.com/pagehelper/Mybatis-PageHelper/blob/master/wikis/zh/HowToUse.md](https://github.com/pagehelper/Mybatis-PageHelper/blob/master/wikis/zh/HowToUse.md)中列出是非常多用法：

	//第一种，RowBounds方式的调用
	List<Country> list = sqlSession.selectList("x.y.selectIf", null, new RowBounds(0, 10));
	
	//第二种，Mapper接口方式的调用，推荐这种使用方式。
	PageHelper.startPage(1, 10);
	List<Country> list = countryMapper.selectIf(1);
	
	//第三种，Mapper接口方式的调用，推荐这种使用方式。
	PageHelper.offsetPage(1, 10);
	List<Country> list = countryMapper.selectIf(1);
	
	//第四种，参数方法调用
	//存在以下 Mapper 接口方法，你不需要在 xml 处理后两个参数
	public interface CountryMapper {
	    List<Country> selectByPageNumSize(
	            @Param("user") User user,
	            @Param("pageNum") int pageNum, 
	            @Param("pageSize") int pageSize);
	}
	//配置supportMethodsArguments=true
	//在代码中直接调用：
	List<Country> list = countryMapper.selectByPageNumSize(user, 1, 10);
	
从这些例子就可以看出它的功能有多强大，使用有多灵活，但这是你需要的东西吗？

使用方法太多不一定是好事，因为后期维护的人也要熟悉所有用法，因为可会遇到每个人用法都不一样的情况。

其实只有第一种用法才是正真需要的，官网推荐的第二种用法，反而是最不推荐的，因为我们要考虑以下几点：

  * 首先Mapper中的方法省略了分页所需要的参数，代码简化了，但使代码更不好理解，可读性变差，语文更不清晰；
  * PageHelper.offsetPage(1, 10);这个方法可以在任务地方调用，有的人喜欢在Controller层调用，有的人会在Service调用；后期维护人员要定位问题时，需要找到所有相关的地方；
  * 因为它是通过ThreadLocal传参，在异步处理时就无法获得分页参数； 
  * 在使用AOP时，无法获取分页相关的参数；
  * 在使用缓存时，需要把count和list 两个分成两次执行，对于有经验的人都知道，当表中的数据量达到一定级别后，count查询的性能也是会越来越差，在使用缓存时，还是要减少执行count的次数。
  * 分页查询需要考虑兼容以下两种方式：

    1. 先执行count获取符合查询条件的总记录数，同时获取当前页的数据；这种一般用于数据量小或管理后台中；
    2. “上拉式分页”，这种分页方式在APP中非常常见，列表往上拉时，加载下页的数据，当拉取到的数据量小于pageSize时，达到最后一页，不再往下拉。此事方式省去了count查询，性能更优，所以常用于APP这种互联网应用中。

基于上面的考虑，其实我们需要的不是增强型的分页插件，而是需要一个精简版的分页插件。接下来介绍精简版分页插件的用法。

## 2. 引入jar

    compile "com.suixingpay.starter:suixingpay-starter-mybatis-page:xxx"

通过 [http://172.16.60.188:8081/nexus/index.html#nexus-search;quick~suixingpay-starter-mybatis-page](http://172.16.60.188:8081/nexus/index.html#nexus-search;quick~suixingpay-starter-mybatis-page)  获取最新版本的jar。

引入jar后，自动生效，无须任务配置。

## 3. 用法

用法：

	public interface UserMapper {
	    Long count(User user);
	    List<User> find(User user, Pagination pagination);
	}
	
分页查询其实是通过 Pagination 其实只是RowBounds的一个子类：

	public class Pagination extends RowBounds {
	
	    /**
	     * 只是排序，不做分页查询
	     */
	    private boolean orderByOnly = false;
	
	    /**
	     * 排序
	     */
	    private Sort sort;
	
	    public Pagination() {
	        super(NO_ROW_OFFSET, NO_ROW_LIMIT);
	    }
	
	    public Pagination(int offset, int limit) {
	        super(offset, limit);
	    }
	}

通过上面代码可以看出，它还支持排序功能，同时如果orderByOnly设置为true时，只是做排序，会忽略offset 和 limit。

UserMapper.xml的内容如下：

	  <select id="count" parameterType="com.suixingpay.takin.demo.domain.User" resultType="java.lang.Long">
	    select count(*) from
	    <include refid="table" />
	    <include refid="dynamicWhere" />
	  </select>
	
	  <select id="find" parameterType="com.suixingpay.takin.demo.domain.User" resultMap="Result">
	    select
	    <include refid="columns" />
	    from
	    <include refid="table" />
	    <include refid="dynamicWhere" />
	  </select>

上面的代码中已经没有分页查询使用的limit 和 offset 设置。

## 4. 为什么count查询不能使用插件呢？

主要还是考虑性能问题，因为在插件中实现count的话，只能是 sql 嵌套sql，如下所示：

    select count(*) from (select a, b,c from t where ... order by a desc )
    
而如果我们自己写的话，通常是写出下面的格式：

    select count(*) from t where ...
    
以上两种查询方式，在数据量比较大的情况下性能差异非常之大的。

## 5. 利用 interface 中的 default 函数，减化分页查询

代码如下：

    package com.suixingpay.takin.mybatis.service;
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
	     * @throws CloudtpException
	     */
	    @Transactional(readOnly = true)
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
	        Pagination pagination = new Pagination(offset, size);
	        pagination.setSort(sort);
	        if (null != totalCnt && totalCnt.intValue() > 0 && offset < totalCnt) {
	            list = find(condition, pagination);
	        } else {
	            list = new ArrayList<>();
	        }
	        return new PageImpl<>(list, pagination, totalCnt);
	    }
	
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
	    List<R> find(C condition, Pagination pagination);
    }
    
以上代码已在suixingpay-starter-data 中。