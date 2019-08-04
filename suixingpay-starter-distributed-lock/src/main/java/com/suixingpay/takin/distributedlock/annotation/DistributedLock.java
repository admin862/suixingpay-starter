/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: jiayu.qiu[qiu_jy@suixingpay.com] 
 * @date: 2017年7月19日 下午10:58:32   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.distributedlock.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在需要使用分布锁的方法加上此注解后，会开启分布式锁功能
 * 
 * @author: jiayu.qiu[qiu_jy@suixingpay.com]
 * @date: 2017年7月19日 下午10:58:32
 * @version: V1.0
 * @review: jiayu.qiu[qiu_jy@suixingpay.com]/2017年7月19日 下午10:58:32
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedLock {
    /**
     * 锁的key,支持Spring EL表达式。
     * 
     * @return
     */
    String key() default "";

    /**
     * 锁的租约时间，单位是秒，默认120秒。当前线程在获取到锁以后，在租约时间到期以后，会自动释放锁。如果在租约时间到期之前，方法执行完毕了，也会释放锁。
     * 
     * @return
     */
    int leaseTime() default 120;

    /**
     * 尝试次数。如果尝试了指定的次没有获取到就立即抛出异常。<br>
     * 
     * @return
     */
    int tryCnt() default 0;

    /**
     * 尝试重新获取锁的时间间隔，单位是毫秒，默认为10。
     * 
     * @return
     */
    int interval() default 10;

    /**
     * 如果获取分布式锁失败时，快速失败的方法。<br>
     * 注意：参数和返回值类型必须与当前方法完全一样。<br>
     * 如果没有设置fallbackMethod，则抛出异常。
     * 
     * @return
     */
    String fallbackMethod() default "";

    /**
     * 检查业务是否已经处理过，避免重复提交。如果设置为true，会使用key()+"_last_result"作为缓存key,缓存上次执行结果。<br>
     * 当缓存中存在值时，根据returnLastResult设置决定是直接抛异常，还是返回缓存中的值。<br>
     * 注意：此缓存失效后，如果还有重复提交，需要使用乐观锁进行处理。<br>
     * 默认值为false<br>
     * 
     * @return
     */
    boolean checkBusinessFinished() default false;

    /**
     * 验证业务已完成方法（最终去数据库中验证的方法）<br>
     * 当checkBusinessFinished 设置为true时有效。当Redis中的数据过期后才会调此方法。<br>
     * 设置的方法参数必须与当前方法完全一样<br>
     * 返回值必须是{link BusinessResult}
     * 
     * @return
     */
    String checkBusinessFinishedMethod() default "";

    /**
     * 设置为true时，如果业务已经处理完成，则直接返回上次处理结果。设置为false时，如果业务已经处理完成，则抛异常。
     * 
     * @return
     */
    boolean returnLastResult() default true;

    /**
     * 业务已完成异常消息，支持Spring EL表达式。
     * 
     * @return
     */
    String businessFinishedMessage() default "";

    /**
     * 业务状态缓存的过期时间，单位：秒，如果为0则表示永久缓存
     * 
     * @return 时间
     */
    int businessStatusExpire() default 120;
}