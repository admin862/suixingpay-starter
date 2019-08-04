/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: jiayu.qiu[qiu_jy@suixingpay.com] 
 * @date: 2017年7月19日 下午10:58:32   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.expression.spring;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.suixingpay.takin.expression.AbstractExpressionParser;
import com.suixingpay.takin.expression.util.StringUtil;

/**
 * Spring EL表达式解析处理
 * 
 * @author: jiayu.qiu[qiu_jy@suixingpay.com]
 * @date: 2017年7月19日 下午10:58:32
 * @version: V1.0
 * @review: jiayu.qiu[qiu_jy@suixingpay.com]/2017年7月19日 下午10:58:32
 */
public class SpringELParser extends AbstractExpressionParser {
    
    private static final Logger LOGGER=LoggerFactory.getLogger(SpringELParser.class);
    
    private static final String type = "SpringEL";

    private final ExpressionParser parser = new SpelExpressionParser();

    private final ConcurrentHashMap<String, Expression> expCache = new ConcurrentHashMap<String, Expression>(128);

    private static Method hash = null;

    private static Method empty = null;

    static {
        try {
            hash = StringUtil.class.getDeclaredMethod("getUniqueHashStr", new Class[] { Object.class });
            empty = StringUtil.class.getDeclaredMethod("isEmpty", new Class[] { Object.class });
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private final ConcurrentHashMap<String, Method> funcs = new ConcurrentHashMap<String, Method>(8);

    /**
     * @param name 方法名
     * @param method 方法
     */
    @Override
    public void addFunction(String name, Method method) {
        funcs.put(name, method);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getValue(String exp, Map<String, Object> variables, Class<T> valueType) throws Exception {
        if (null == exp || exp.trim().length() == 0) {
            return null;
        }
        if (valueType.equals(String.class)) {
            if (exp.indexOf("#") == -1 && exp.indexOf("'") == -1) {// 如果不是表达式，直接返回字符串
                return (T) exp;
            }
        }
        StandardEvaluationContext context = new StandardEvaluationContext();

        context.registerFunction(HASH, hash);
        context.registerFunction(EMPTY, empty);
        Iterator<Map.Entry<String, Method>> it = funcs.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Method> entry = it.next();
            context.registerFunction(entry.getKey(), entry.getValue());
        }
        context.setVariables(variables);
        Expression expression = expCache.get(exp);
        if (null == expression) {
            expression = parser.parseExpression(exp);
            expCache.put(exp, expression);
        }
        return expression.getValue(context, valueType);
    }

    @Override
    public String getExpressionType() {
        return type;
    }

}
