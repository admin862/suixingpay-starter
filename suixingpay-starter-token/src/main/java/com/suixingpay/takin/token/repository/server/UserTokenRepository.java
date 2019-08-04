package com.suixingpay.takin.token.repository.server;

import java.util.Set;

/**
 * 用户登录token repository
 *
 * @author: tangqihua[tang_qh@suixingpay.com]
 * @date: 2018年03月09日 15时16分
 * @version: V1.0
 * @review: tangqihua[tang_qh@suixingpay.com]/2018年03月09日 15时16分
 */
public interface UserTokenRepository {
    /**
     * 获取指定用户的所有登录token<br>
     * 必须按token创建时间升序显示<br>
     * 
     * @param userId
     * @return
     */
    Set<String> getAll(String userId);

    /**
     * 添加用户登录token
     *
     * @param userId
     * @param token
     * @param timeout 单位：秒
     */
    void addToken(String userId, String token, int timeout);

    /**
     * 将token从集合移除
     *
     * @param userId
     * @param token
     * @return
     */
    boolean removeToken(String userId, String token);

    /**
     * 当启用同一时刻单一回话模式时。<br>
     * 设置token并返回旧token。<br>
     * 注意必须保证此方法是原子性操作，如果是用Redis，直接用Redis的getSet方法就可以保证其原子性；如果是使用数据库，那么需要使用分布式锁来保证其原子性；<br>
     *
     * @param userId
     * @param token
     * @param timeout 单位：秒
     * @return
     */
    String getSet(String userId, String token, int timeout);

    /**
     * 当启用同一时刻单一回话模式时。<br>
     * 获取当前用户token<br>
     * 
     * @param userId
     * @return
     */
    String getSingleModeToken(String userId);

    /**
     * 删除token
     *
     * @param userId
     * @return
     */
    boolean del(String userId);

    /**
     * @param userId
     * @param tokens
     * @return
     */
    long removeTokens(String userId, Set<String> tokens);

}
