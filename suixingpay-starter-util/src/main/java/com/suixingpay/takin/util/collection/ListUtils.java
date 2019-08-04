/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: matieli<ma_tl@suixingpay.com> 
 * @date: 2017年3月8日 下午1:25:06   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.util.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import com.google.common.collect.Lists;

/**
 * 关于List的工具集合.
 * <p>
 * 1. 常用函数(如是否为空，sort/binarySearch/shuffle/reverse(via JDK Collection)
 * </p>
 * <p>
 * 2. 各种构造函数(from guava and JDK Collection)
 * </p>
 * <p>
 * 3. Array 转 ArrayList的特色函数 (from Guava)
 * </p>
 * <p>
 * 5. 集合运算，集合是否一致，交集，并集, 差集, 补集，from Commons Collection，但对其不合理的地方做了修正
 * </p>
 * 
 * @author: matieli[ma_tl@suixingpay.com]
 * @date: 2017年3月8日 下午1:55:10
 * @version: V1.0
 */
public abstract class ListUtils {
    /**
     * 判断是否为空.
     * 
     * @param list
     * @return boolean
     */
    public static boolean isEmpty(List<?> list) {
        return (list == null) || list.isEmpty();
    }

    /**
     * 判断是否不为空.
     * 
     * @param list
     * @return boolean
     */
    public static boolean isNotEmpty(List<?> list) {
        return (list != null) && !(list.isEmpty());
    }

    /**
     * 获取第一个元素, 如果List为空返回 null.
     * 
     * @param list
     * @return T
     */
    public static <T> T getFirst(List<T> list) {
        if (isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    /**
     * 获取最后一个元素，如果List为空返回null.
     * 
     * @param list
     * @return T
     */
    public static <T> T getLast(List<T> list) {
        if (isEmpty(list)) {
            return null;
        }

        return list.get(list.size() - 1);
    }

    ///////////////// from Guava的构造函数///////////////////

    /**
     * 根据等号左边的类型，构造类型正确的ArrayList.
     * 
     * @return
     * @see com.google.common.collect.Lists#newArrayList()
     */
    public static <T> ArrayList<T> newArrayList() {
        return new ArrayList<T>();
    }

    /**
     * 根据等号左边的类型，构造类型正确的ArrayList, 并初始化元素.
     * 
     * @param elements
     * @return
     * @see com.google.common.collect.Lists#newArrayList(Object...)
     */
    @SafeVarargs
    public static <T> ArrayList<T> newArrayList(T... elements) {
        return Lists.newArrayList(elements);
    }

    /**
     * 根据等号左边的类型，构造类型正确的ArrayList, 并初始化元素.
     * 
     * @param elements
     * @return
     * @see com.google.common.collect.Lists#newArrayList(Iterable)
     */
    public static <T> ArrayList<T> newArrayList(Iterable<T> elements) {
        return Lists.newArrayList(elements);
    }

    /**
     * 根据等号左边的类型，构造类型正确的ArrayList, 并初始化数组大小.
     * 
     * @param initSize
     * @return
     * @see com.google.common.collect.Lists#newArrayListWithCapacity(int)
     */
    public static <T> ArrayList<T> newArrayListWithCapacity(int initSize) {
        return new ArrayList<T>(initSize);
    }

    /**
     * 根据等号左边的类型，构造类型正确的LinkedList.
     * 
     * @return
     * @see com.google.common.collect.Lists#newLinkedList()
     */
    public static <T> LinkedList<T> newLinkedList() {
        return new LinkedList<T>();
    }

    /**
     * 根据等号左边的类型，构造类型正确的CopyOnWriteArrayList.
     * 
     * @return
     * @see com.google.common.collect.Lists#newCopyOnWriteArrayList()
     */
    public static <T> CopyOnWriteArrayList<T> newCopyOnWriteArrayList() {
        return new CopyOnWriteArrayList<T>();
    }

    /**
     * 根据等号左边的类型，构造类型转换的CopyOnWriteArrayList, 并初始化元素.
     * 
     * @param elements
     * @return
     */
    @SafeVarargs
    public static <T> CopyOnWriteArrayList<T> newCopyOnWriteArrayList(T... elements) {
        return new CopyOnWriteArrayList<T>(elements);
    }

    ///////////////// from JDK Collections的常用构造函数 ///////////////////

    /**
     * 返回一个空的结构特殊的List，节约空间.
     * <p>
     * 注意返回的List不可写, 写入会抛出UnsupportedOperationException.
     * </p>
     * 
     * @return
     * @see java.util.Collections#emptyList()
     */
    public static final <T> List<T> emptyList() {
        return Collections.emptyList();
    }

    /**
     * 如果list为null，转化为一个安全的空List.
     * <p>
     * 注意返回的List不可写, 写入会抛出UnsupportedOperationException.
     * </p>
     * 
     * @param list
     * @return
     * @see java.util.Collections#emptyList()
     */
    public static <T> List<T> emptyListIfNull(final List<T> list) {
        return list == null ? Collections.emptyList() : list;
    }

    /**
     * 返回只含一个元素但结构特殊的List，节约空间.
     * <p>
     * 注意返回的List不可写, 写入会抛出UnsupportedOperationException.
     * </p>
     * 
     * @param o
     * @return
     * @see java.util.Collections#singletonList(Object)
     */
    public static <T> List<T> singletonList(T o) {
        return Collections.singletonList(o);
    }

    /**
     * 返回包装后不可修改的List.
     * <p>
     * 如果尝试写入会抛出UnsupportedOperationException.
     * </p>
     * 
     * @param list
     * @return
     * @see java.util.Collections#unmodifiableList(List)
     */
    public static <T> List<T> unmodifiableList(List<? extends T> list) {
        return Collections.unmodifiableList(list);
    }

    /**
     * 返回包装后同步的List，所有方法都会被synchronized原语同步.
     * <p>
     * 用于CopyOnWriteArrayList与 ArrayDequeue均不符合的场景
     * </p>
     * 
     * @param list
     * @return
     */
    public static <T> List<T> synchronizedList(List<T> list) {
        return Collections.synchronizedList(list);
    }

    ///////////////// from JDK Collections的常用函数 ///////////////////

    /**
     * 升序排序, 采用JDK认为最优的排序算法, 使用元素自身的compareTo()方法
     *
     * @see java.util.Collections#sort(List)
     */
    public static <T extends Comparable<? super T>> void sort(List<T> list) {
        Collections.sort(list);
    }

    /**
     * 倒序排序, 采用JDK认为最优的排序算法,使用元素自身的compareTo()方法
     * 
     * @param list
     * @see java.util.Collections#sort(List)
     */

    public static <T extends Comparable<? super T>> void sortReverse(List<T> list) {
        Collections.sort(list, Collections.reverseOrder());
    }

    /**
     * 升序排序, 采用JDK认为最优的排序算法, 使用Comparetor.
     * 
     * @param list
     * @param c
     * @see java.util.Collections#sort(List, Comparator)
     */

    public static <T> void sort(List<T> list, Comparator<? super T> c) {
        Collections.sort(list, c);
    }

    /**
     * 倒序排序, 采用JDK认为最优的排序算法, 使用Comparetor
     * 
     * @param list
     * @param c
     * @see java.util.Collections#sort(List, Comparator)
     */
    public static <T> void sortReverse(List<T> list, Comparator<? super T> c) {
        Collections.sort(list, Collections.reverseOrder(c));
    }

    /**
     * 二分法快速查找对象, 使用Comparable对象自身的比较.
     * <p>
     * list必须已按升序排序.
     * </p>
     * <p>
     * 如果不存在，返回一个负数，代表如果要插入这个对象，应该插入的位置
     * </p>
     * 
     * @param sortedList
     * @param key
     * @return
     * @see java.util.Collections#binarySearch(List, Object)
     */
    public static <T> int binarySearch(List<? extends Comparable<? super T>> sortedList, T key) {
        return Collections.binarySearch(sortedList, key);
    }

    /**
     * 二分法快速查找对象，使用Comparator.
     * <p>
     * list必须已按升序排序.
     * </p>
     * <p>
     * 如果不存在，返回一个负数，代表如果要插入这个对象，应该插入的位置
     * </p>
     * 
     * @param sortedList
     * @param key
     * @param c
     * @return
     * @see java.util.Collections#binarySearch(List, Object, Comparator)
     */
    public static <T> int binarySearch(List<? extends T> sortedList, T key, Comparator<? super T> c) {
        return Collections.binarySearch(sortedList, key, c);
    }

    /**
     * 随机乱序，使用默认的Random.
     * 
     * @param list
     * @see java.util.Collections#shuffle(List)
     */
    public static void shuffle(List<?> list) {
        Collections.shuffle(list);
    }

    /**
     * 返回一个倒转顺序访问的List，仅仅是一个倒序的View，不会实际多生成一个List
     * 
     * @param list
     * @return
     * @see com.google.common.collect.Lists#reverse(List)
     */
    public static <T> List<T> reverse(final List<T> list) {
        return Lists.reverse(list);
    }

    /**
     * 随机乱序，使用传入的Random.
     * 
     * @param list
     * @param rnd
     * @see java.util.Collections#shuffle(List, Random)
     */
    public static void shuffle(List<?> list, Random rnd) {
        Collections.shuffle(list, rnd);
    }

    ///////////////// 集合运算 ///////////////////

    /**
     * list1,list2的并集（在list1或list2中的对象），产生新List
     * <p>
     * 对比Apache Common Collection4 ListUtils, 优化了初始大小
     * </p>
     * 
     * @param list1
     * @param list2
     * @return
     */
    public static <E> List<E> union(final List<? extends E> list1, final List<? extends E> list2) {
        final ArrayList<E> result = new ArrayList<E>(list1.size() + list2.size());
        result.addAll(list1);
        result.addAll(list2);
        return result;
    }

    /**
     * list1, list2的交集（同时在list1和list2的对象），产生新List
     * <p>
     * from Apache Common Collection4 ListUtils，但其做了不合理的去重，因此重新改为性能较低但不去重的版本
     * </p>
     * <p>
     * 与List.retainAll()相比，考虑了的List中相同元素出现的次数, 如"a"在list1出现两次，而在list2中只出现一次，则交集里会保留一个"a".
     * </p>
     * 
     * @param list1
     * @param list2
     * @return
     */
    public static <T> List<T> intersection(final List<? extends T> list1, final List<? extends T> list2) {
        final List<T> result = new ArrayList<T>();

        List<? extends T> smaller = list1;
        List<? extends T> larger = list2;
        if (list1.size() > list2.size()) {
            smaller = list2;
            larger = list1;
        }

        List<T> newSmaller = new ArrayList<T>(smaller);
        for (final T e : larger) {
            if (newSmaller.contains(e)) {
                result.add(e);
                newSmaller.remove(e);
            }
        }
        return result;
    }

    /**
     * list1, list2的差集（在list1，不在list2中的对象），产生新List.
     * <p>
     * 于List.removeAll()相比，会计算元素出现的次数，如"a"在list1出现两次，而在list2中只出现一次，则差集里会保留一个"a".
     * 
     * @param list1
     * @param list2
     * @return
     */
    public static <T> List<T> difference(final List<? extends T> list1, final List<? extends T> list2) {
        final ArrayList<T> result = new ArrayList<T>(list1);
        final Iterator<? extends T> iterator = list2.iterator();

        while (iterator.hasNext()) {
            result.remove(iterator.next());
        }

        return result;
    }

    /**
     * list1, list2的补集（在list1或list2中，但不在交集中的对象，又叫反交集）产生新List.
     * <p>
     * from Apache Common Collection4 ListUtils，但其并集－交集时，没有对交集*2，所以做了修改
     * </p>
     * 
     * @param list1
     * @param list2
     * @return
     */
    public static <T> List<T> disjoint(final List<? extends T> list1, final List<? extends T> list2) {
        List<T> intersection = intersection(list1, list2);
        List<T> towIntersection = union(intersection, intersection);
        return difference(union(list1, list2), towIntersection);
    }
}
