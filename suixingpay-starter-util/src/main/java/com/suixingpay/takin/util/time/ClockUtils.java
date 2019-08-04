/**
 * All rights Reserved, Designed By Suixingpay.
 * 
 * @author: matieli<ma_tl@suixingpay.com>
 * @date: 2017年3月7日 下午2:52:34
 * @Copyright ©2017 Suixingpay. All rights reserved. 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.util.time;

import java.util.Date;

/**
 * 日期提供者, 使用它而不是直接取得系统时间, 方便测试. 平时使用DEFAULT，测试时替换为DummyClock，可准确控制时间变化而不用Thread.sleep()等待时间流逝.
 * 
 * @author: matieli[ma_tl@suixingpay.com]
 * @date: 2017年3月8日 下午1:19:20
 * @version: V1.0
 */
public abstract class ClockUtils {

    private static Clock INSTANCE = new DefaultClock();

    /**
     * 切换为DummyClock，使用系统时间为初始时间, 单个测试完成后需要调用useDefaultClock()切换回去.
     * 
     * @return
     */
    public static synchronized DummyClock useDummyClock() {
        DummyClock clock = new DummyClock();
        INSTANCE = clock;
        return clock;
    }

    /**
     * 切换为DummyClock，单个测试完成后需要调用useDefaultClock()切换回去.
     * 
     * @param timeStampMills
     * @return
     */
    public static synchronized DummyClock useDummyClock(long timeStampMills) {
        DummyClock clock = new DummyClock(timeStampMills);
        INSTANCE = clock;
        return clock;
    }

    /**
     * 切换为DummyClock，单个测试完成后需要调用useDefaultClock()切换回去.
     * 
     * @param date
     * @return
     */
    public static synchronized DummyClock useDummyClock(Date date) {
        DummyClock clock = new DummyClock(date);
        INSTANCE = clock;
        return clock;
    }

    /**
     * 重置为默认Clock
     */
    public static synchronized void useDefaultClock() {
        INSTANCE = new DefaultClock();
    }

    /**
     * 系统当前时间
     * 
     * @return 当前时间
     */
    public static Date currentDate() {
        return INSTANCE.currentDate();
    }

    /**
     * 系统当前时间戳
     * 
     * @return
     */
    public static long currentTimeMillis() {
        return INSTANCE.currentTimeMillis();
    }

    /**
     * 操作系统启动到现在的纳秒数，与系统时间是完全独立的两个时间体系
     * 
     * @return
     */
    public static long nanoTime() {
        return INSTANCE.nanoTime();
    }

    /**
     * Clock
     * 
     * @author: matieli<ma_tl@suixingpay.com>
     * @date: 2017年3月14日 下午7:13:14
     * @version: V1.0
     */
    public interface Clock {

        /**
         * 系统当前时间
         */
        Date currentDate();

        /**
         * 系统当前时间戳
         */
        long currentTimeMillis();

        /**
         * 操作系统启动到现在的纳秒数，与系统时间是完全独立的两个时间体系
         */
        long nanoTime();
    }

    /**
     * 默认时间提供者，返回当前的时间，线程安全。
     * 
     * @author: matieli<ma_tl@suixingpay.com>
     * @date: 2017年3月8日 下午1:20:52
     * @version: V1.0
     */
    public static class DefaultClock implements Clock {

        @Override
        public Date currentDate() {
            return new Date();
        }

        @Override
        public long currentTimeMillis() {
            return System.currentTimeMillis();
        }

        @Override
        public long nanoTime() {
            return System.nanoTime();
        }
    }

    /**
     * 可配置的时间提供者，用于测试.
     * 
     * @author: matieli<ma_tl@suixingpay.com>
     * @date: 2017年3月8日 下午1:21:07
     * @version: V1.0
     */
    public static class DummyClock implements Clock {

        private long time;
        private long nanoTme;

        public DummyClock() {
            this(System.currentTimeMillis());
        }

        public DummyClock(Date date) {
            this(date.getTime());
        }

        public DummyClock(long time) {
            this.time = time;
            this.nanoTme = System.nanoTime();
        }

        @Override
        public Date currentDate() {
            return new Date(time);
        }

        @Override
        public long currentTimeMillis() {
            return time;
        }

        /**
         * 获取nanotime
         * 
         * @return
         * @see com.suixingpay.hummer.util.time.ClockUtils.Clock#nanoTime()
         */
        @Override
        public long nanoTime() {
            return nanoTme;
        }

        /**
         * 重新设置日期.
         * 
         * @param newDate 新日期
         */
        public void updateNow(Date newDate) {
            time = newDate.getTime();
        }

        /**
         * 重新设置时间.
         * 
         * @param newTime 新时间
         */
        public void updateNow(long newTime) {
            this.time = newTime;
        }

        /**
         * 滚动时间.
         * 
         * @param millis
         */
        public void increaseTime(int millis) {
            time += millis;
        }

        /**
         * 滚动时间.
         * 
         * @param millis
         */
        public void decreaseTime(int millis) {
            time -= millis;
        }

        /**
         * 设置nanotime.
         * 
         * @param nanoTime
         */
        public void setNanoTime(long nanoTime) {
            this.nanoTme = nanoTime;
        }
    }
}
