/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2017年8月21日 上午11:31:54   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.redis;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.redis.core.ZSetOperations.TypedTuple;

/**
 * Redis操作接口
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年8月21日 上午11:31:54
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年8月21日 上午11:31:54
 */
public interface IRedisOperater {
    String KEY_CHAR_SET = "UTF-8";
    String OK = "OK";

    /**
     * namespace
     * 
     * @return
     */
    String getNamespace();

    /**
     * Key添加namespace并序列化
     * 
     * @param key
     * @return
     */
    default String key(String key) {
        if (null == key || key.length() == 0) {
            throw new IllegalArgumentException("key is empty!");
        }
        return getNamespace() + key;
    }

    /**
     * 对hash的field进行序列化
     * 
     * @param field
     * @return
     */
    default byte[] field(Object field) {
        if (null == field) {
            throw new IllegalArgumentException("field is empty!");
        }
        String str = String.valueOf(field);
        if (str.length() == 0) {
            throw new IllegalArgumentException("field is empty!");
        }
        try {
            return str.getBytes(KEY_CHAR_SET);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 判断是否存在
     * 
     * @param key
     * @return
     */
    Boolean exists(String key);

    /**
     * 往缓存写数据(会覆写旧值)
     * 
     * @param key 缓存Key
     * @param value 缓存数据
     */
    void set(final String key, final Object value);

    /**
     * 往缓存写数据(会覆写旧值)
     * 
     * @param key
     * @param value
     * @param expire 缓存时长，单位秒
     */
    void setex(final String key, final Object value, final int expire);

    /**
     * SETNX 是『SET if Not eXists』(如果不存在，则 SET)的简写
     * 
     * @param key
     * @param value
     * @param expire 缓存时长，单位秒, expire 大于0时设置过期时间
     * @return 设置是否成功 。
     */
    boolean setnx(final String key, final Object value, final int expire);

    /**
     * 根据缓存Key获得缓存中的数据
     * 
     * @param key 缓存key
     * @return 缓存数据
     */
    Object get(final String key);

    /**
     * 将给定 key 的值设为 value ，并返回 key 的旧值(old value)。
     * 
     * @param key 缓存Key
     * @param value 缓存数据
     * @return Object
     */
    Object getSet(final String key, final Object value);

    /**
     * 删除缓存
     * 
     * @param key 缓存key
     * @return 被删除 key 的数量。
     */
    Long delete(final String key);

    /**
     * 设置过期时间
     * 
     * @param key
     * @param expire 缓存时长，单位秒
     * @return boolean
     */
    boolean expire(final String key, final int expire);

    /**
     * 将 key 中储存的数字值增一。 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作。
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。 本操作的值限制在 64 位(bit)有符号数字表示之内。
     * 
     * @param key
     * @return
     */
    long incr(String key);

    /**
     * 将 key 所储存的值加上增量 increment 。 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCRBY
     * 命令。 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。 本操作的值限制在 64 位(bit)有符号数字表示之内。
     * 
     * @param key
     * @param increment
     * @return
     */
    long incrBy(String key, int increment);

    /**
     * 将 key 中储存的数字值减一。 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECR 操作。
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。 本操作的值限制在 64 位(bit)有符号数字表示之内。
     * 
     * @param key
     * @return
     */
    long decr(String key);

    /**
     * 将 key 所储存的值减去减量 decrement 。 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECRBY
     * 操作。 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。 本操作的值限制在 64 位(bit)有符号数字表示之内。
     * 
     * @param key
     * @param decrement
     * @return
     */
    long decrBy(String key, int decrement);

    /**
     * 判断是否存在
     * 
     * @param key
     * @param field
     * @return
     */
    Boolean hexists(String key, Object field);

    /**
     * 将哈希表 key 中的域 field 的值设为 value 。 如果 key 不存在，一个新的哈希表被创建并进行 HSET 操作。 如果域
     * field 已经存在于哈希表中，旧值将被覆盖。
     * 
     * @param key
     * @param field
     * @param value
     * @return 如果 field 是哈希表中的一个新建域，并且值设置成功，返回 1 。如果哈希表中域 field 已经存在且旧值已被新值覆盖，返回
     *         0 。
     */
    long hset(String key, Object field, Object value);

    /**
     * 将哈希表 key 中的域 field 的值设置为 value ，当且仅当域 field 不存在。 若域 field 已经存在，该操作无效。 如果
     * key 不存在，一个新哈希表被创建并执行 HSETNX 命令。
     * 
     * @param key
     * @param field
     * @param value
     * @return 是否设置成功
     */
    boolean hsetnx(String key, Object field, Object value);

    /**
     * 同时将多个 field-value (域-值)对设置到哈希表 key 中。 此命令会覆盖哈希表中已存在的域。 如果 key
     * 不存在，一个空哈希表被创建并执行 HMSET 操作。
     * 
     * @param key
     * @param fieldValues
     */
    void hmset(String key, Map<Object, Object> fieldValues);

    /**
     * 返回哈希表 key 中给定域 field 的值。
     * 
     * @param key
     * @param field
     * @return
     */
    Object hget(String key, Object field);

    /**
     * 返回哈希表 key 中所有域的值，不建议使用，建议使用hmget，如果一次获取过多数据会影响Redis性能；
     * 
     * @param key
     * @return
     */
    Map<String, Object> hgetAll(String key);

    /**
     * 返回哈希表 key 中，一个或多个给定域的值。 如果给定的域不存在于哈希表，那么返回一个 nil 值。 因为不存在的 key
     * 被当作一个空哈希表来处理，所以对一个不存在的 key 进行 HMGET 操作将返回一个只带有 nil 值的表。
     * 
     * @param key
     * @param field
     * @return
     */
    List<Object> hmget(String key, Object... field);

    /**
     * 删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略。
     * 
     * @param key
     * @param field
     * @return 被成功移除的域的数量，不包括被忽略的域。
     */
    long hdel(String key, Object... field);

    /**
     * 为哈希表 key 中的域 field 的值加上增量 increment 。 增量也可以为负数，相当于对给定域进行减法操作。 如果 key
     * 不存在，一个新的哈希表被创建并执行 HINCRBY 命令。 如果域 field 不存在，那么在执行命令前，域的值被初始化为 0 。
     * 对一个储存字符串值的域 field 执行 HINCRBY 命令将造成一个错误。 本操作的值被限制在 64 位(bit)有符号数字表示之内。
     * 
     * @param key
     * @param field
     * @param increment
     * @return
     */
    long hincrBy(String key, Object field, int increment);

    /**
     * 将一个或 member 元素及其 score 值加入到有序集 key 当中。 如果某个 member 已经是有序集的成员，那么更新这个
     * member 的 score 值，并通过重新插入这个 member 元素，来保证该 member 在正确的位置上。 score
     * 值可以是整数值或双精度浮点数。 如果 key 不存在，则创建一个空的有序集并执行 ZADD 操作。 当 key
     * 存在但不是有序集类型时，返回一个错误。
     * 
     * @param key
     * @param score
     * @param member
     * @return 被成功添加的新成员的数量，不包括那些被更新的、已经存在的成员。
     */
    long zdd(String key, double score, Object member);

    /**
     * 返回有序集 key 中， score 值在 min 和 max 之间(默认包括 score 值等于 min 或 max )的成员的数量。 关于参数
     * min 和 max 的详细使用方法，请参考 ZRANGEBYSCORE 命令。
     * 
     * @param key
     * @param min
     * @param max
     * @return score 值在 min 和 max 之间的成员的数量。
     */
    long zcount(String key, double min, double max);

    /**
     * ZRANGE key start stop <br/>
     * 返回有序集 key 中，指定区间内的成员。<br/>
     * 其中成员的位置按 score 值递增(从小到大)来排序。<br/>
     * 具有相同 score 值的成员按字典序(lexicographical order )来排列。<br/>
     * 下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。<br/>
     * 你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推。<br/>
     * 超出范围的下标并不会引起错误。比如说，当 start 的值比有序集的最大下标还要大，或是 start > stop 时， ZRANGE
     * 命令只是简单地返回一个空列表。<br/>
     * 另一方面，假如 stop 参数的值比有序集的最大下标还要大，那么 Redis 将 stop 当作最大下标来处理。<br/>
     * 
     * @param key
     * @param start
     * @param end
     * @return 指定区间内，有序集成员的列表。
     */
    Set<Object> zrange(String key, final long start, final long end);

    /**
     * ZRANGE key start stop WITHSCORES <br/>
     * 返回有序集 key 中，指定区间内的成员。<br/>
     * 其中成员的位置按 score 值递增(从小到大)来排序。<br/>
     * 具有相同 score 值的成员按字典序(lexicographical order )来排列。<br/>
     * 下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。<br/>
     * 你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推。<br/>
     * 超出范围的下标并不会引起错误。比如说，当 start 的值比有序集的最大下标还要大，或是 start > stop 时， ZRANGE
     * 命令只是简单地返回一个空列表。<br/>
     * 另一方面，假如 stop 参数的值比有序集的最大下标还要大，那么 Redis 将 stop 当作最大下标来处理。<br/>
     * 成员和它的 score 值一并返回，返回列表以 value1,score1, ..., valueN,scoreN 的格式表示。<br/>
     * 
     * @param key
     * @param start
     * @param end
     * @return 指定区间内，带有 score 值的有序集成员的列表。
     */
    Set<TypedTuple<Object>> zrangeWithScores(String key, final long start, final long end);
    

    /**
     * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。<br/>
     * 有序集成员按 score值递增(从小到大)次序排列。 <br/>
     * 具有相同 score 值的成员按字典序(lexicographical order)来排列(该属性是有序集提供的，不需要额外的计算)。 <br/>
     * 可选的 LIMIT 参数指定返回结果的数量及区间(就像SQL中的 SELECT LIMIT offset, count )，<br/>
     * 注意当 offset 很大时，定位 offset 的操作可能需要遍历整个有序集，此过程最坏复杂度为 O(N) 时间。<br/>
     * 
     * @param key
     * @param start
     * @param stop
     * @return 指定区间内，带有 score 值(可选)的有序集成员的列表。
     */
    Set<Object> zrangeByScore(String key, double min, double max);

    /**
     * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。<br/>
     * 有序集成员按 score 值递增(从小到大)次序排列。<br/>
     * 具有相同 score 值的成员按字典序(lexicographical order)来排列(该属性是有序集提供的，不需要额外的计算)。<br/>
     * 可选的 LIMIT 参数指定返回结果的数量及区间(就像SQL中的 SELECT LIMIT offset, count )，注意当offset很大时，<br/>
     * 定位 offset 的操作可能需要遍历整个有序集，此过程最坏复杂度为 O(N) 时间。 <br/>
     * WITHSCORES 参数决定结果集是单单返回有序集的成员，还是将有序集成员及其 score 值一起返回。<br/>
     * 该选项自 Redis 2.0 版本起可用。<br/>
     * 
     * @param key
     * @param start
     * @param stop
     * @return
     */
    Set<TypedTuple<Object>> zrangeByScoreWithScores(String key, double min, double max);

    /**
     * 移除有序集 key 中的一个或多个成员，不存在的成员将被忽略。 当 key 存在但不是有序集类型时，返回一个错误。
     * 
     * @param key
     * @param member
     * @return 被成功移除的成员的数量，不包括被忽略的成员。
     */
    long zrem(String key, Object... member);

    /**
     * 执行Lua脚本
     * 
     * @param script
     * @param key
     * @param arg
     * @return
     */
    Object eval(byte[] script, String key, Object... arg);

    /**
     * 根据给定的 sha1 校验码，对缓存在服务器中的脚本进行求值。 将脚本缓存到服务器的操作可以通过 SCRIPT LOAD 命令进行。
     * 这个命令的其他地方，比如参数的传入方式，都和 EVAL 命令一样。
     * 
     * @param sha1
     * @param key
     * @param arg
     * @return
     */
    Object evalSha(byte[] sha1, String key, Object... arg);

    /**
     * 将脚本 script 添加到脚本缓存中，但并不立即执行这个脚本。 EVAL 命令也会将脚本添加到脚本缓存中，但是它会立即对输入的脚本进行求值。
     * 如果给定的脚本已经在缓存里面了，那么不做动作。 在脚本被加入到缓存之后，通过 EVALSHA 命令，可以使用脚本的 SHA1
     * 校验和来调用这个脚本。 脚本可以在缓存中保留无限长的时间，直到执行 SCRIPT FLUSH 为止。 关于使用 Redis 对 Lua
     * 脚本进行求值的更多信息，请参见 EVAL 命令。
     * 
     * @param script
     * @param key
     * @return 给定 script 的 SHA1 校验和
     */
    byte[] scriptLoad(byte[] script, String key);

    /**
     * 将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略。 假如 key 不存在，则创建一个只包含
     * member 元素作成员的集合。 当 key 不是集合类型时，返回一个错误。
     * 
     * @param key
     * @param member 需要添加的数据，允许同时添加多个
     * @return 被添加到集合中的新元素的数量，不包括被忽略的元素。
     */
    long sadd(String key, Object... member);

    /**
     * 返回集合 key 的基数(集合中元素的数量)。
     * 
     * @param key
     * @return 集合的基数。当 key 不存在时，返回 0
     */
    long scard(String key);

    /**
     * 判断 member 元素是否集合 key 的成员。 如果 member 元素是集合的成员，返回 1 。 如果 member 元素不是集合的成员，或
     * key 不存在，返回 0 。
     * 
     * @param key
     * @param member
     * @return
     */
    boolean sisMember(String key, Object member);

    /**
     * 返回集合 key 中的所有成员。 不存在的 key 被视为空集合。
     * 
     * @param key
     * @return 集合中的所有成员。
     */
    Set<Object> smembers(String key);

    /**
     * 移除并返回集合中的一个随机元素。 如果只想获取一个随机元素，但不想该元素从集合中被移除的话，可以使用 SRANDMEMBER 命令。
     * 
     * @param key
     * @return 被移除的随机元素。当 key 不存在或 key 是空集时，返回 nil 。
     */
    Object spop(String key);

    /**
     * 如果命令执行时，只提供了 key 参数，那么返回集合中的一个随机元素。<br/>
     * 从 Redis 2.6 版本开始， SRANDMEMBER 命令接受可选的 count 参数：<br/>
     * 如果 count 为正数，且小于集合基数，那么命令返回一个包含 count 个元素的数组，数组中的元素各不相同。如果 count
     * 大于等于集合基数，那么返回整个集合。<br/>
     * 如果 count 为负数，那么命令返回一个数组，数组中的元素可能会重复出现多次，而数组的长度为 count 的绝对值。<br/>
     * 该操作和 SPOP 相似，但 SPOP 将随机元素从集合中移除并返回，而 SRANDMEMBER
     * 则仅仅返回随机元素，而不对集合进行任何改动。<br/>
     * 
     * @param key
     * @param count
     * @return 只提供 key 参数时，返回一个元素；如果集合为空，返回 nil 。如果提供了 count
     *         参数，那么返回一个数组；如果集合为空，返回空数组。
     */
    List<Object> srandMember(String key, int count);

    /**
     * 返回集合中的一个随机元素
     * 
     * @param key
     * @return
     */
    Object srandMember(String key);

    /**
     * 移除集合 key 中的一个或多个 member 元素，不存在的 member 元素会被忽略。 当 key 不是集合类型，返回一个错误。
     * 
     * @param key
     * @param member
     * @return 被成功移除的元素的数量，不包括被忽略的元素。
     */
    long srem(String key, Object... member);

}
