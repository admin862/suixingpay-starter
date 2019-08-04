/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: jiayu.qiu[qiu_jy@suixingpay.com] 
 * @date: 2017年7月19日 下午10:58:32   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.expression;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 表达式解析处理
 * 
 * @author: jiayu.qiu[qiu_jy@suixingpay.com]
 * @date: 2017年7月19日 下午10:58:32
 * @version: V1.0
 * @review: jiayu.qiu[qiu_jy@suixingpay.com]/2017年7月19日 下午10:58:32
 */
public abstract class AbstractExpressionParser {

    protected static final String HASH = "hash";

    protected static final String EMPTY = "empty";

    /**
     * 为了简化表达式，方便调用Java static 函数，在这里注入表达式自定义函数
     * 
     * @param name 自定义函数名
     * @param method 调用的方法
     */
    public abstract void addFunction(String name, Method method);

    /**
     * 将表达式转换期望的值
     * 
     * @param exp 生成缓存Key的表达式
     * @param variables 上下文
     * @return T value 返回值
     * @param <T> 泛型
     * @throws Exception 异常
     */
    public abstract <T> T getValue(String exp, Map<String, Object> variables, Class<T> valueType) throws Exception;

    /**
     * 表达式类型名称
     * 
     * @return
     */
    public abstract String getExpressionType();

}
