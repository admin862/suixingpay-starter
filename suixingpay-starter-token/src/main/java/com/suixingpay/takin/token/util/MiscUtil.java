package com.suixingpay.takin.token.util;

import java.util.Random;
import java.util.concurrent.atomic.LongAdder;

/**
 * 工具类
 * 
 * @author jiayu.qiu
 */
public class MiscUtil {

    private static final char[] CHARS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
            'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
            'V', 'W', 'X', 'Y', 'Z' };

    private static final char[] SPECIAL_CHARS = new char[] { '~', '`', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')',
            '-', '_', '+', '=', '{', '}', '[', ']', '|', '\\', ';', ':', '"', '<', '.', '>', '/', '?' };

    private static final int RADIX = 36;

    private static final int LIMIT = (CHARS.length * CHARS.length - 1);

    /**
     * 随机数种子
     */
    private static final ThreadLocal<Random> RANDOM = new ThreadLocal<Random>() {

        @Override
        protected Random initialValue() {
            return new Random();
        }

    };

    /**
     * 获取随机字符串
     * 
     * @param destLen 目标字符串长度
     * @param whitSpecialChars 是否包含特殊字符
     * @return 随机字符串
     */
    public static String getRandomStr(int destLen, boolean whitSpecialChars) {
        StringBuilder sb = new StringBuilder();
        Random r = RANDOM.get();
        int range = CHARS.length;
        if (whitSpecialChars) {
            range += SPECIAL_CHARS.length;
        }
        for (int i = 0; i < destLen; i++) {
            // 生成指定范围类的随机数0—字符串长度(包括0、不包括字符串长度)
            int rand = r.nextInt(range);
            if (rand < CHARS.length) {
                sb.append(CHARS[rand]);
            } else {
                sb.append(SPECIAL_CHARS[rand - CHARS.length]);
            }
        }
        RANDOM.remove();
        return sb.toString();
    }

    private static final LongAdder ADDER = new LongAdder();;
    private static final long MAX_VALUE = Long.MAX_VALUE - 10000L;

    /**
     * 生成唯一字符串（相对的，重复概率非常底）
     * 
     * @return 串一字符串
     */
    public static String genUniqueStr() {
        StringBuilder sb = new StringBuilder();
        ADDER.increment();
        long serialNum = ADDER.longValue();
        if (serialNum >= MAX_VALUE) {
            synchronized (ADDER) {
                if (ADDER.longValue() >= MAX_VALUE) {
                    ADDER.reset();
                }
            }
        }
        serialNum = serialNum % LIMIT;
        if (serialNum > LIMIT) {
            throw new RuntimeException("serialNum 超过：" + LIMIT);
        }
        int m = (int) (serialNum / CHARS.length);
        int n = (int) (serialNum % CHARS.length);
        sb.append(CHARS[m]);
        sb.append(CHARS[n]);

        sb.append(Long.toString(System.currentTimeMillis(), RADIX));
        Random random = RANDOM.get();
        sb.append(Integer.toString((random.nextInt(RADIX)), RADIX));
        sb.append(Integer.toString((random.nextInt(RADIX)), RADIX));
        RANDOM.remove();
        return sb.toString();
    }
}
