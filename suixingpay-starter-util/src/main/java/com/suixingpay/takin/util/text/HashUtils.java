/**
 * All rights Reserved, Designed By Suixingpay.
 * 
 * @author: matieli<ma_tl@suixingpay.com>
 * @date: 2017年3月14日 上午10:56:52
 * @Copyright ©2017 Suixingpay. All rights reserved. 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.util.text;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.zip.CRC32;

import org.apache.commons.lang3.Validate;

import com.google.common.hash.Hashing;

/**
 * 封装各种Hash算法的工具类.
 * <p>
 * 1.集合类的HashCode, 返回int
 * </p>
 * <p>
 * 2.SHA-1, 安全性较高, 返回byte[](可用Encodes进一步被编码为Hex, Base64)
 * </p>
 * <p>
 * 性能优化，使用ThreadLocal的MessageDigest(from ElasticSearch)
 * </p>
 * <p>
 * 支持带salt并且进行迭代达到更高的安全性.
 * </p>
 * <p>
 * MD5的安全性较低, 只在文件Checksum时支持.
 * </p>
 * <p>
 * 3.crc32, murmur32这些不追求安全性, 性能较高, 返回int.
 * </p>
 * 其中crc32基于JDK, murmurhash基于guava
 * 
 * @author: matieli[ma_tl@suixingpay.com]
 * @date: 2017年3月14日 上午10:56:52
 * @version: V1.0
 */
public abstract class HashUtils {

    /**
     * createThreadLocalMessageDigest
     * 
     * @param digest
     * @return
     */
    private static ThreadLocal<MessageDigest> createThreadLocalMessageDigest(final String digest) {
        return new ThreadLocal<MessageDigest>() {
            @Override
            protected MessageDigest initialValue() {
                try {
                    return MessageDigest.getInstance(digest);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(
                            "unexpected exception creating MessageDigest instance for [" + digest + "]", e);
                }
            }
        };
    }

    private static final ThreadLocal<MessageDigest> MD5_DIGEST = createThreadLocalMessageDigest("MD5");
    private static final ThreadLocal<MessageDigest> SHA_1_DIGEST = createThreadLocalMessageDigest("SHA-1");
    private static final ThreadLocal<MessageDigest> SHA_256_DIGEST = createThreadLocalMessageDigest("SHA-256");

    private static SecureRandom RANDOW = new SecureRandom();

    ////////////////// HashCode //////////////////
    /**
     * 多个对象的HashCode串联
     * 
     * @param objects
     * @return
     */
    public static int hashCode(Object... objects) {
        return Arrays.hashCode(objects);
    }

    /**
     * 集合的HashCode串联
     * 
     * @param list
     * @return
     */
    public static int hashCode(final Collection<?> list) {
        if (list == null) {
            return 0;
        }
        int hashCode = 1;
        final Iterator<?> it = list.iterator();

        while (it.hasNext()) {
            final Object obj = it.next();
            hashCode = 31 * hashCode + (obj == null ? 0 : obj.hashCode());
        }
        return hashCode;
    }

    ////////////////// SHA1 ///////////////////
    /**
     * 对输入字符串进行sha1散列.
     * 
     * @param input
     * @return
     */
    public static byte[] sha1(byte[] input) {
        return digest(input, get(SHA_1_DIGEST), null, 1);
    }

    /**
     * 对输入字符串进行sha1散列, 编码默认为UTF8.
     * 
     * @param input
     * @return
     */
    public static byte[] sha1(String input) {
        return digest(input.getBytes(StandardCharsets.UTF_8), get(SHA_1_DIGEST), null, 1);
    }

    /**
     * 对输入字符串进行sha1散列，带salt达到更高的安全性.
     * 
     * @param input
     * @param salt
     * @return
     */
    public static byte[] sha1(byte[] input, byte[] salt) {
        return digest(input, get(SHA_1_DIGEST), salt, 1);
    }

    /**
     * 对输入字符串进行sha1散列，带salt达到更高的安全性.
     * 
     * @param input
     * @param salt
     * @return
     */
    public static byte[] sha1(String input, byte[] salt) {
        return digest(input.getBytes(StandardCharsets.UTF_8), get(SHA_1_DIGEST), salt, 1);
    }

    /**
     * 对输入字符串进行sha1散列，带salt而且迭代达到更高更高的安全性.
     * 
     * @param input
     * @param salt
     * @param iterations
     * @return
     * @see #generateSalt(int)
     */
    public static byte[] sha1(byte[] input, byte[] salt, int iterations) {
        return digest(input, get(SHA_1_DIGEST), salt, iterations);
    }

    /**
     * 对输入字符串进行sha1散列，带salt而且迭代达到更高更高的安全性.
     * 
     * @param input
     * @param salt
     * @param iterations
     * @return
     * @see #generateSalt(int)
     */
    public static byte[] sha1(String input, byte[] salt, int iterations) {
        return digest(input.getBytes(StandardCharsets.UTF_8), get(SHA_1_DIGEST), salt, iterations);
    }

    ////////////////// SHA256 ///////////////////
    /**
     * 对输入字符串进行sha256散列.
     * 
     * @param input
     * @return
     */
    public static String sha256(byte[] input) {
        byte[] tmp = digest(input, get(SHA_256_DIGEST), null, 1);
        return bytes2Hex(tmp);
    }

    /**
     * 对输入字符串进行sha256散列, 编码默认为UTF8.
     * 
     * @param input
     * @return
     */
    public static String sha256(String input) {
        byte[] tmp = digest(input.getBytes(StandardCharsets.UTF_8), get(SHA_256_DIGEST), null, 1);
        return bytes2Hex(tmp);
    }

    /**
     * 对输入字符串进行sha256散列，带salt达到更高的安全性.
     * 
     * @param input
     * @param salt
     * @return
     */
    public static String sha256(byte[] input, byte[] salt) {
        byte[] tmp = digest(input, get(SHA_256_DIGEST), salt, 1);
        return bytes2Hex(tmp);
    }

    /**
     * 对输入字符串进行sha256散列，带salt达到更高的安全性.
     * 
     * @param input
     * @param salt
     * @return
     */
    public static String sha256(String input, String salt) {
        byte[] s=(null != salt && salt.length()>0) ? salt.getBytes(StandardCharsets.UTF_8): null;
        byte[] tmp = digest(input.getBytes(StandardCharsets.UTF_8), get(SHA_256_DIGEST), s, 1);
        return bytes2Hex(tmp);
    }

    /**
     * 对输入字符串进行sha256散列，带salt而且迭代达到更高更高的安全性.
     * 
     * @param input
     * @param salt
     * @param iterations
     * @return
     * @see #generateSalt(int)
     */
    public static String sha256(byte[] input, byte[] salt, int iterations) {
        byte[] tmp = digest(input, get(SHA_256_DIGEST), salt, iterations);
        return bytes2Hex(tmp);
    }

    /**
     * 对输入字符串进行sha256散列，带salt而且迭代达到更高更高的安全性.
     * 
     * @param input
     * @param salt
     * @param iterations
     * @return
     * @see #generateSalt(int)
     */
    public static String sha256(String input, String salt, int iterations) {
        byte[] s=(null != salt && salt.length()>0) ? salt.getBytes(StandardCharsets.UTF_8): null;
        byte[] tmp = digest(input.getBytes(StandardCharsets.UTF_8), get(SHA_256_DIGEST), s, iterations);
        return bytes2Hex(tmp);
    }

    /**
     * get
     * 
     * @param messageDigest
     * @return
     */
    private static MessageDigest get(ThreadLocal<MessageDigest> messageDigest) {
        MessageDigest instance = messageDigest.get();
        instance.reset();
        return instance;
    }

    /**
     * 对字符串进行散列, 支持md5与sha1算法.
     * 
     * @param input
     * @param digest
     * @param salt
     * @param iterations
     * @return
     */
    private static byte[] digest(byte[] input, MessageDigest digest, byte[] salt, int iterations) {
        // 带盐
        if (salt != null) {
            digest.update(salt);
        }

        // 第一次散列
        byte[] result = digest.digest(input);

        // 如果迭代次数>1，进一步迭代散列
        for (int i = 1; i < iterations; i++) {
            digest.reset();
            result = digest.digest(result);
        }

        return result;
    }
    
    public static String bytes2Hex(byte[] bts) {
        StringBuilder des = new StringBuilder();
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = Integer.toHexString(bts[i] & 0xFF);
            if (tmp.length() == 1) {
                des.append("0");
            }
            des.append(tmp);
        }
        return des.toString();
    }

    /**
     * 用SecureRandom生成随机的byte[]作为salt.
     * 
     * @param numBytes salt数组的大小
     * @return
     */
    public static byte[] generateSalt(int numBytes) {
        Validate.isTrue(numBytes > 0, "numBytes argument must be a positive integer (1 or larger)", numBytes);

        byte[] bytes = new byte[numBytes];
        RANDOW.nextBytes(bytes);
        return bytes;
    }

    /**
     * 对文件进行sha1散列.
     * 
     * @param input 流
     * @return byte[] 散列
     * @throws IOException
     */
    public static byte[] sha1File(InputStream input) throws IOException {
        return digestFile(input, get(SHA_1_DIGEST));
    }

    /**
     * 对文件进行md5散列，被破解后MD5已较少人用.
     * 
     * @param input 流
     * @return byte[] 散列
     * @throws IOException
     */
    public static byte[] md5File(InputStream input) throws IOException {
        return digestFile(input, get(MD5_DIGEST));
    }

    /**
     * digestFile
     * 
     * @param input
     * @param messageDigest
     * @return byte[] 散列
     * @throws IOException
     */
    private static byte[] digestFile(InputStream input, MessageDigest messageDigest) throws IOException {
        int bufferLength = 8 * 1024;
        byte[] buffer = new byte[bufferLength];
        int read = input.read(buffer, 0, bufferLength);

        while (read > -1) {
            messageDigest.update(buffer, 0, read);
            read = input.read(buffer, 0, bufferLength);
        }

        return messageDigest.digest();
    }

    ////////////////// 基于JDK的CRC32 ///////////////////

    /**
     * 对输入字符串进行crc32散列返回int, 返回值有可能是负数. Guava也有crc32实现,
     * 但返回值无法返回long，所以统一使用JDK默认实现
     * 
     * @param input 字符串
     * @return int
     */
    public static int crc32AsInt(String input) {
        return crc32AsInt(input.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 对输入字符串进行crc32散列返回int, 返回值有可能是负数. Guava也有crc32实现,
     * 但返回值无法返回long，所以统一使用JDK默认实现
     * 
     * @param input
     * @return
     */
    public static int crc32AsInt(byte[] input) {
        CRC32 crc32 = new CRC32();
        crc32.update(input);
        // CRC32 只是 32bit int，为了CheckSum接口强转成long，此处再次转回来
        return (int) crc32.getValue();
    }

    /**
     * 对输入字符串进行crc32散列，与php兼容，在64bit系统下返回永远是正数的long Guava也有crc32实现,
     * 但返回值无法返回long，所以统一使用JDK默认实现
     * 
     * @param input
     * @return
     */
    public static long crc32AsLong(String input) {
        return crc32AsLong(input.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 对输入字符串进行crc32散列，与php兼容，在64bit系统下返回永远是正数的long Guava也有crc32实现,
     * 但返回值无法返回long，所以统一使用JDK默认实现
     * 
     * @param input
     * @return
     */
    public static long crc32AsLong(byte[] input) {
        CRC32 crc32 = new CRC32();
        crc32.update(input);
        return crc32.getValue();
    }

    ////////////////// 基于Guava的MurMurHash ///////////////////
    /**
     * 对输入字符串进行murmur32散列, 默认以类加载时的时间戳为seed
     * 
     * @param input
     * @return
     */
    public static int murmur32AsInt(byte[] input) {
        return Hashing.murmur3_32().hashBytes(input).asInt();
    }

    /**
     * 对输入字符串进行murmur32散列, 返回值可能是负数, 默认以类加载时的时间戳为seed
     * 
     * @param input
     * @return
     */
    public static int murmur32AsInt(String input) {
        return Hashing.murmur3_32().hashString(input, StandardCharsets.UTF_8).asInt();
    }

    /**
     * 对输入字符串进行murmur32散列, 返回值可能是负数, 带有seed
     * 
     * @param input
     * @param seed
     * @return
     */
    public static int murmur32AsInt(byte[] input, int seed) {
        return Hashing.murmur3_32(seed).hashBytes(input).asInt();
    }

    /**
     * 对输入字符串进行murmur32散列, 返回值可能是负数, 带有seed
     * 
     * @param input
     * @param seed
     * @return
     */
    public static int murmur32AsInt(String input, int seed) {
        return Hashing.murmur3_32(seed).hashString(input, StandardCharsets.UTF_8).asInt();
    }
}
