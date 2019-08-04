/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年1月19日 下午5:02:53   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.xss;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ValueNode;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年1月19日 下午5:02:53
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年1月19日 下午5:02:53
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private static final String TEXT = "text";
    private final ObjectMapper mapper;

    public XssHttpServletRequestWrapper(HttpServletRequest request, ObjectMapper mapper) {
        super(request);
        this.mapper = mapper;
        if (null == mapper) {
            throw new RuntimeException("mapper is null.");
        }
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        ServletInputStream in = super.getInputStream();
        String contentType = super.getContentType();
        // 如果是非文本或非json
        if (null != contentType
                && !(contentType.startsWith(TEXT) || contentType.contains(MediaType.APPLICATION_JSON_VALUE))) {
            return in;
        }
        String json = readContent(in);
        if (!json.isEmpty()) {
            try {
                JsonNode rootNode = mapper.readTree(json);
                json = escapeHtml(rootNode);
            } catch (Throwable e) {
            }
        }
        final ByteArrayInputStream byteIn = new ByteArrayInputStream(UTF_8.encode(json).array());
        return new ByteArrayServletInputStream(byteIn);
    }

    private String readContent(InputStream inputStream) throws IOException {
        StringBuilder builder = new StringBuilder();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inputStream.read(buffer)) != -1) {
            builder.append(new String(buffer, 0, len, UTF_8));
        }
        return builder.toString();
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        return StringEscapeUtil.escapeIllegalChars(value);
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        return StringEscapeUtil.escapeIllegalChars(value);
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values != null) {
            int length = values.length;
            String[] escapseValues = new String[length];
            for (int i = 0; i < length; i++) {
                escapseValues[i] = StringEscapeUtil.escapeIllegalChars(values[i]);
            }
            return escapseValues;
        }
        return values;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> requestMap = super.getParameterMap();
        Iterator<Map.Entry<String, String[]>> iterator = requestMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String[]> item = iterator.next();
            String[] values = item.getValue();
            for (int i = 0; i < values.length; i++) {
                values[i] = StringEscapeUtil.escapeIllegalChars(values[i]);
            }
        }
        return requestMap;
    }

    /**
     * 把JSON中特殊字符替换掉
     * 
     * @param root
     * @return
     */
    private String escapeHtml(JsonNode root) {
        if (null == root) {
            return "";
        }
        if (root.isNull()) {
            return root.toString();
        }
        // 如果是字符串节点
        if (root instanceof TextNode) {
            String string = StringEscapeUtil.escapeIllegalChars(root.asText());
            TextNode textNode = new TextNode(string);
            return textNode.toString();
        }
        escapeJsonNode(root);
        return root.toString();
    }

    /**
     * 递归解析JSON，并把特殊字符替换掉
     * 
     * @param node
     */
    private static void escapeJsonNode(JsonNode node) {
        if(node.isNull()) {
            return;
        }
        if (node instanceof ObjectNode) {
            ObjectNode parentNode = (ObjectNode) node;
            Iterator<Map.Entry<String, JsonNode>> iterator = node.fields();
            while (iterator.hasNext()) {
                Map.Entry<String, JsonNode> item = iterator.next();
                if (item.getValue() instanceof ValueNode) {
                    if (!item.getValue().isNull() && item.getValue() instanceof TextNode) {
                        String string = StringEscapeUtil.escapeIllegalChars(item.getValue().textValue());
                        TextNode textNode = new TextNode(string);
                        parentNode.set(item.getKey(), textNode);
                    }
                    continue;
                }
                escapeJsonNode(item.getValue());
            }
        } else if (node instanceof ArrayNode) {
            ArrayNode parentNode = (ArrayNode) node;
            Iterator<JsonNode> iterator = node.elements();
            int ind = 0;
            while (iterator.hasNext()) {
                JsonNode item = iterator.next();
                if (item instanceof ValueNode) {
                    if (!item.isNull() && item instanceof TextNode) {
                        String string = StringEscapeUtil.escapeIllegalChars(item.textValue());
                        TextNode textNode = new TextNode(string);
                        parentNode.set(ind, textNode);
                    }
                    ind++;
                    continue;
                }
                escapeJsonNode(item);
                ind++;
            }
        }
    }

}
