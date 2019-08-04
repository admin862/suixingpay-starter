package com.suixingpay.takin.util.json;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ValueNode;

public class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();
    static {
        // 忽略大小写
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    }

    /**
     * 生成JSON字符串
     * 
     * @param obj
     * @return
     * @throws JsonProcessingException
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static String objectToJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成JSON字符串
     * 
     * @param obj
     * @param serializationView
     * @return
     * @throws JsonProcessingException
     * @throws Exception
     */
    public static String objectToJson(Object obj, Class<?> serializationView) throws JsonProcessingException {
        if (null != serializationView) {
            return mapper.writerWithView(serializationView).writeValueAsString(obj);
        } else {
            return mapper.writeValueAsString(obj);
        }
    }

    /**
     * 将JSON字符串转化为 指定类型
     * 
     * @param json json
     * @param c 期望类型
     * @return T 结果
     */
    public static <T> T jsonToObject(String json, Class<T> c) {
        T t = null;
        try {
            if (json != null && json.trim().length() > 0) {
                t = mapper.readValue(json, c);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return t;
    }

    /**
     * @param json
     * @return
     */
    public static <T> Map<String, T> jsonToMap(String json) {
        return jsonToObject(json, new TypeReference<Map<String, T>>() {
        });
    }

    /**
     * 将JSON字符串转化为 指定类型
     * 
     * @param json json
     * @param c 期望类型
     * @return T 结果
     */
    public static <T> T jsonToObject(String json, TypeReference<T> c) {
        T t = null;
        try {
            if (json != null && json.trim().length() > 0) {
                t = mapper.readValue(json, c);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return t;
    }

    /**
     * 获取Json树
     * 
     * @param json
     * @return
     */
    public static JsonNode toJsonTree(String json) {
        try {
            if (json != null && json.trim().length() > 0) {
                return mapper.readTree(json);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static void printJsonNode(JsonNode node) {
        if (null != node) {
            if (node instanceof ObjectNode) {
                ObjectNode parentNode = (ObjectNode) node;
                Iterator<Map.Entry<String, JsonNode>> iterator = node.fields();
                while (iterator.hasNext()) {
                    Map.Entry<String, JsonNode> item = iterator.next();
                    if (item.getValue() instanceof ValueNode) {
                        System.out.println("    " + item.getKey() + "==" + item.getValue());
                        if (item.getValue() instanceof TextNode) {
                            TextNode textNode = new TextNode(item.getValue().textValue() + "--->");
                            parentNode.set(item.getKey(), textNode);
                        }
                        continue;
                    }
                    printJsonNode(item.getValue());
                }
            } else if (node instanceof ArrayNode) {
                ArrayNode parentNode = (ArrayNode) node;
                Iterator<JsonNode> iterator = node.elements();
                int ind = 0;
                System.out.println("[");
                while (iterator.hasNext()) {
                    JsonNode item = iterator.next();
                    if (item instanceof ValueNode) {
                        System.out.println(item);
                        if (item instanceof TextNode) {
                            TextNode textNode = new TextNode(item.textValue() + "--->");
                            parentNode.set(ind, textNode);
                        }
                        ind++;
                        continue;
                    }
                    printJsonNode(item);
                    ind++;
                }
                System.out.println("]");
            } else {
                System.out.println("-->" + node);
            }
        }
    }

}
