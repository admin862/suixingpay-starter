/**
 * All rights Reserved, Designed By Suixingpay.
 * 
 * @author: matieli<ma_tl@suixingpay.com>
 * @date: 2017年3月8日 下午1:25:06
 * @Copyright ©2017 Suixingpay. All rights reserved. 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.util.text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Utf8;
import com.suixingpay.takin.util.collection.ListUtils;

/**
 * 尽量使用Common Lang StringUtils 本类仅补充少量额外方法.
 * 
 * <pre>
 * http://tool.oschina.net/apidocs/apidoc?api=commons-lang
 * </pre>
 * 
 * @author: matieli[ma_tl@suixingpay.com]
 * @date: 2017年3月8日 下午1:25:06
 * @version: V1.0
 * @see org.apache.commons.lang3.StringUtils
 */
public abstract class StringMoreUtils extends StringUtils {

    private static final Map<String, Pattern> PATTERN_CACHE = new ConcurrentHashMap<>();

    /**
     * 下划线字符
     */
    public static final char UNDERLINE = '_';

    /**
     * 占位符
     */
    public static final String PLACE_HOLDER = "{%s}";

    /**
     * 高性能的Split，针对char的分隔符号，比JDK String自带的高效. from Commons Lange 3.5
     * StringUtils, 做如下优化:
     * <p>
     * 1.最后不做数组转换，直接返回List.
     * </p>
     * <p>
     * 2. 可设定List初始大小.
     * </p>
     * <p>
     * 3. preserveAllTokens 取默认值false
     * </p>
     * 
     * @param str 字符串
     * @param separatorChar 分隔符
     * @param expectParts
     * @return 如果为null返回null, 如果为""返回空数组
     */

    public static List<String> split(final String str, final char separatorChar, int expectParts) {

        if (StringUtils.isEmpty(str)) {
            return null;
        }
        final int len = str.length();
        if (len == 0) {
            return ListUtils.emptyList();
        }
        final List<String> list = new ArrayList<String>(expectParts);
        int i = 0;
        int start = 0;
        boolean match = false;
        while (i < len) {
            if (str.charAt(i) == separatorChar) {
                if (match) {
                    list.add(str.substring(start, i));
                    match = false;
                }
                start = ++i;
                continue;
            }
            match = true;
            i++;
        }
        if (match) {
            list.add(str.substring(start, i));
        }
        return list;
    }

    /**
     * <p>
     * 字符串驼峰转下划线格式
     * </p>
     *
     * @param param 需要转换的字符串
     * @return 转换好的字符串
     */
    public static String camelToUnderline(String param) {
        if (isEmpty(param)) {
            return EMPTY;
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c) && i > 0) {
                sb.append(UNDERLINE);
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }

    /**
     * <p>
     * 字符串下划线转驼峰格式
     * </p>
     *
     * @param param 需要转换的字符串
     * @return 转换好的字符串
     */
    public static String underlineToCamel(String param) {
        if (isEmpty(param)) {
            return EMPTY;
        }
        String temp = param.toLowerCase();
        int len = temp.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = temp.charAt(i);
            if (c == UNDERLINE) {
                if (++i < len) {
                    sb.append(Character.toUpperCase(temp.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * <p>
     * SQL 参数填充
     * </p>
     *
     * @param content 填充内容
     * @param args 填充参数
     * @return
     */
    public static String sqlArgsFill(String content, Object... args) {
        if (isEmpty(content)) {
            return null;
        }
        if (args != null) {
            int length = args.length;
            if (length >= 1) {
                for (int i = 0; i < length; i++) {
                    content = content.replace(String.format(PLACE_HOLDER, i), sqlParam(args[i]));
                }
            }
        }
        return content;
    }

    /**
     * 获取SQL PARAMS字符串
     *
     * @param obj
     * @return
     */
    public static String sqlParam(Object obj) {
        String repStr;
        if (obj instanceof Collection) {
            repStr = quotaMarkList((Collection<?>) obj);
        } else {
            repStr = quotaMark(obj);
        }
        return repStr;
    }

    /**
     * <p>
     * 使用单引号包含字符串
     * </p>
     *
     * @param obj 原字符串
     * @return 单引号包含的原字符串
     */
    public static String quotaMark(Object obj) {
        String srcStr = String.valueOf(obj);
        if (obj instanceof CharSequence) {
            return StringEscape.escapeString(srcStr);
        }
        return srcStr;
    }

    /**
     * <p>
     * 使用单引号包含字符串
     * </p>
     *
     * @param coll 集合
     * @return 单引号包含的原字符串的集合形式
     */
    public static String quotaMarkList(Collection<?> coll) {
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("(");
        int _size = coll.size();
        int i = 0;
        Iterator<?> iterator = coll.iterator();
        while (iterator.hasNext()) {
            String tempVal = quotaMark(iterator.next());
            sqlBuild.append(tempVal);
            if (i + 1 < _size) {
                sqlBuild.append(",");
            }
            i++;
        }
        sqlBuild.append(")");
        return sqlBuild.toString();
    }

    /**
     * <p>
     * 首字母转换小写
     * </p>
     *
     * @param param 需要转换的字符串
     * @return 转换好的字符串
     */
    public static String firstToLowerCase(String param) {
        if (isEmpty(param)) {
            return EMPTY;
        }
        StringBuilder sb = new StringBuilder(param.length());
        sb.append(param.substring(0, 1).toLowerCase());
        sb.append(param.substring(1));
        return sb.toString();
    }

    /**
     * <p>
     * 判断字符串是否为纯大写字母
     * </p>
     *
     * @param str 要匹配的字符串
     * @return
     */
    public static boolean isUpperCase(String str) {
        return match("^[A-Z]+$", str);
    }

    /**
     * <p>
     * 正则表达式匹配
     * </p>
     *
     * @param regex 正则表达式字符串
     * @param str 要匹配的字符串
     * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
     */
    public static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 判断字符串某字母结尾
     * 
     * @param sequence
     * @param c
     * @return true or false
     */
    public static boolean endWith(CharSequence sequence, char c) {
        if (StringUtils.isEmpty(sequence)) {
            return false;
        }
        return sequence.charAt(sequence.length() - 1) == c;
    }

    /**
     * 计算字符串被UTF8编码后的字节数 via guava
     * 
     * @param sequence 字符串
     * @return int 字节数
     * @see Utf8#encodedLength(CharSequence)
     */
    public static int utf8EncodedLength(CharSequence sequence) {
        return Utf8.encodedLength(sequence);
    }

    /**
     * <p>
     * 判断对象是否为空
     * </p>
     *
     * @param object
     * @return
     */
    public static boolean checkValNotNull(Object object) {
        if (object instanceof CharSequence) {
            return isNotEmpty((CharSequence) object);
        }
        return object != null;
    }

    /**
     * <p>
     * 判断对象是否为空
     * </p>
     *
     * @param object
     * @return
     */
    public static boolean checkValNull(Object object) {
        return !checkValNotNull(object);
    }

    /**
     * <p>
     * 字符串第一个字母大写
     * </p>
     *
     * @param str
     * @return
     */
    public static String capitalize(final String str) {
        return concatCapitalize(null, str);
    }

    /**
     * <p>
     * 拼接字符串第二个字符串第一个字母大写
     * </p>
     *
     * @param concatStr
     * @param str
     * @return
     */
    public static String concatCapitalize(String concatStr, final String str) {
        if (isEmpty(concatStr)) {
            concatStr = EMPTY;
        }
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }

        final char firstChar = str.charAt(0);
        if (Character.isTitleCase(firstChar)) {
            // already capitalized
            return str;
        }

        StringBuilder sb = new StringBuilder(strLen);
        sb.append(concatStr);
        sb.append(Character.toTitleCase(firstChar));
        sb.append(str.substring(1));
        return sb.toString();
    }

    /**
     * 是否为CharSequence类型
     *
     * @param cls
     * @return
     */
    public static Boolean isCharSequence(Class<?> cls) {
        return cls != null && CharSequence.class.isAssignableFrom(cls);
    }

    /**
     * 是否为CharSequence类型
     *
     * @param propertyType
     * @return
     */
    public static Boolean isCharSequence(String propertyType) {
        Class<?> cls;
        try {
            cls = Class.forName(propertyType);
        } catch (ClassNotFoundException e) {
            return false;
        }
        return isCharSequence(cls);
    }

    /**
     * <p>
     * Splits the provided text into an array, separators specified. This is an
     * alternative to using StringTokenizer.
     * </p>
     * <p>
     * <p>
     * The separator is not included in the returned String array. Adjacent
     * separators are treated as one separator. For more control over the split
     * use the StrTokenizer class.
     * </p>
     * <p>
     * <p>
     * A {@code null} input String returns {@code null}. A {@code null}
     * separatorChars splits on whitespace.
     * </p>
     * <p>
     * 
     * <pre>
     * StringUtils.split(null, *)         = null
     * StringUtils.split("", *)           = []
     * StringUtils.split("abc def", null) = ["abc", "def"]
     * StringUtils.split("abc def", " ")  = ["abc", "def"]
     * StringUtils.split("abc  def", " ") = ["abc", "def"]
     * StringUtils.split("ab:cd:ef", ":") = ["ab", "cd", "ef"]
     * </pre>
     *
     * @param str the String to parse, may be null
     * @param separatorChars the characters used as the delimiters, {@code null}
     *            splits on whitespace
     * @return an array of parsed Strings, {@code null} if null String input
     */
    public static String[] split(final String str, final String separatorChars) {
        List<String> strings = splitWorker(str, separatorChars, -1, false);
        return strings.toArray(new String[strings.size()]);
    }

    /**
     * Performs the logic for the {@code split} and
     * {@code splitPreserveAllTokens} methods that return a maximum array
     * length.
     *
     * @param str the String to parse, may be {@code null}
     * @param separatorChars the separate character
     * @param max the maximum number of elements to include in the array. A zero
     *            or negative value implies no limit.
     * @param preserveAllTokens if {@code true}, adjacent separators are treated
     *            as empty token separators; if {@code false}, adjacent
     *            separators are treated as one separator.
     * @return an array of parsed Strings, {@code null} if null String input
     */
    public static List<String> splitWorker(final String str, final String separatorChars, final int max,
            final boolean preserveAllTokens) {
        // Performance tuned for 2.0 (JDK1.4)
        // Direct code is quicker than StringTokenizer.
        // Also, StringTokenizer uses isSpace() not isWhitespace()

        if (str == null) {
            return null;
        }
        final int len = str.length();
        if (len == 0) {
            return Collections.emptyList();
        }
        final List<String> list = new ArrayList<>();
        int sizePlus1 = 1;
        int i = 0, start = 0;
        boolean match = false;
        boolean lastMatch = false;
        if (separatorChars == null) {
            // Null separator means use whitespace
            while (i < len) {
                if (Character.isWhitespace(str.charAt(i))) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        } else if (separatorChars.length() == 1) {
            // Optimise 1 character case
            final char sep = separatorChars.charAt(0);
            while (i < len) {
                if (str.charAt(i) == sep) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        } else {
            // standard case
            while (i < len) {
                if (separatorChars.indexOf(str.charAt(i)) >= 0) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        }
        if (match || preserveAllTokens && lastMatch) {
            list.add(str.substring(start, i));
        }
        return list;
    }

    public static final Pattern compileRegex(String regex) {
        Pattern pattern = (Pattern) PATTERN_CACHE.get(regex);
        if (pattern == null) {
            pattern = Pattern.compile(regex);
            PATTERN_CACHE.put(regex, pattern);
        }

        return pattern;
    }
}
