/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年2月8日 下午2:24:28   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.mybatis.page.demo;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.suixingpay.takin.mybatis.page.demo.domain.SystemLog;
import com.suixingpay.takin.mybatis.page.demo.mapper.SystemLogMapper;

/**  
 * TODO
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年2月8日 下午2:24:28
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年2月8日 下午2:24:28
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = PageDemoApplication.class)
public class PageTest {
    @Autowired
    private SystemLogMapper systemLogMapper;
    
//    @Test
//    public void testPage() {
//        SystemLog cSystemLog = new SystemLog();
//        cSystemLog.setAction("GET");
//        List<SystemLog> systemLogs = systemLogMapper.find(cSystemLog, new Pagination(0, 10));
//        for(SystemLog systemLog: systemLogs) {
//            System.out.println(systemLog);
//        }
//    }
    
    @Test
    public void testFind() {
        SystemLog cSystemLog = new SystemLog();
        cSystemLog.setAction("GET");
        cSystemLog.setUuid("b1e648019fb5475091caa8585ef030c6");
        List<SystemLog> systemLogs = systemLogMapper.find(cSystemLog);
        for(SystemLog systemLog: systemLogs) {
            System.out.println(systemLog);
        }
    }
}


