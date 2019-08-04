/**
 * All rights Reserved, Designed By Suixingpay.
 * 
 * @author: matieli[ma_tl@suixingpay.com]
 * @date: 2017年3月8日 下午1:25:06
 * @Copyright ©2017 Suixingpay. All rights reserved. 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.util.collection;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import com.google.common.collect.ObjectArrays;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

/**
 * 数组工具类. Arrays的其他函数，如sort(), toString() 请直接调用
 * 
 * @author: matieli<ma_tl@suixingpay.com>
 * @date: 2017年3月8日 下午1:56:07
 * @version: V1.0
 */
public abstract class ArrayUtils {
    /**
     * 将传入的数组乱序
     * 
     * @param array
     * @return T[]
     */
    public static <T> T[] shuffle(T[] array) {
        List<T> list = new ArrayList<T>(array.length);
        Collections.addAll(list, array);
        Collections.shuffle(list);
        return list.toArray(array);
    }

    /**
     * 将传入的数组乱序
     * 
     * @param array
     * @param random
     * @return T[]
     */
    public static <T> T[] shuffle(T[] array, Random random) {
        List<T> list = new ArrayList<T>(Arrays.asList(array));
        Collections.shuffle(list, random);
        return list.toArray(array);
    }

    /**
     * 添加元素到数组末尾，没有银弹，复制扩容.
     * 
     * @param element
     * @param array
     * @return T[]
     */
    public static <T> T[] concat(T element, T[] array) {
        return ObjectArrays.concat(element, array);
    }

    /**
     * 传入类型与大小创建数组.
     * 
     * @param type
     * @param length
     * @return T[]
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] newArray(Class<T> type, int length) {
        return (T[]) Array.newInstance(type, length);
    }

    /**
     * list.toArray() 返回的是Object[] 如果要有类型的数组话，就要使用list.toArray(new
     * String[list.size()])，这里对调用做了简化
     * 
     * @param list
     * @param type
     * @return T[]
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(List<T> list, Class<T> type) {
        return list.toArray((T[]) Array.newInstance(type, list.size()));
    }

    /**
     * 原版将数组转换为List.
     * <p>
     * 注意转换后的List不能写入, 否则抛出UnsupportedOperationException
     * </p>
     * 
     * @param args
     * @see java.util.Arrays#asList(Object...)
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> asList(T... args) {
        return Arrays.asList(args);
    }

    /**
     * <p>
     * 一个独立元素＋一个数组组成新的list，只是一个View，不复制数组内容，而且独立元素在最前.
     * </p>
     * <p>
     * 注意转换后的List不能写入, 否则抛出UnsupportedOperationException
     * </p>
     * 
     * @param first
     * @param rest
     * @return
     * @see com.google.common.collect.Lists#asList(Object, Object[])
     */
    public static <E> List<E> asList(E first, E[] rest) {
        return Lists.asList(first, rest);
    }

    /**
     * Arrays.asList()的加强版, 返回一个底层为原始类型int的List
     * <p>
     * 与保存Integer相比节约空间，同时只在读取数据时AutoBoxing.
     * </p>
     * 
     * @param backingArray
     * @return
     * @see java.util.Arrays#asList(Object...)
     * @see com.google.common.primitives.Ints#asList(int...)
     */
    public static List<Integer> intAsList(int... backingArray) {
        return Ints.asList(backingArray);
    }

    /**
     * Arrays.asList()的加强版, 返回一个底层为原始类型long的List
     * <p>
     * 与保存Long相比节约空间，同时只在读取数据时AutoBoxing.
     * </p>
     * 
     * @param backingArray
     * @return
     * @see java.util.Arrays#asList(Object...)
     * @see com.google.common.primitives.Longs#asList(long...)
     */
    public static List<Long> longAsList(long... backingArray) {
        return Longs.asList(backingArray);
    }

    /**
     * Arrays.asList()的加强版, 返回一个底层为原始类型double的List
     * <p>
     * 与保存Double相比节约空间，同时也避免了AutoBoxing.
     * </p>
     * 
     * @param backingArray
     * @return
     * @see java.util.Arrays#asList(Object...)
     * @see com.google.common.primitives.Doubles#asList(double...)
     */
    public static List<Double> doubleAsList(double... backingArray) {
        return Doubles.asList(backingArray);
    }

    public static boolean isEmpty(final Object[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isNotEmpty(final Object[] array) {
        return !isEmpty(array);
    }

}
