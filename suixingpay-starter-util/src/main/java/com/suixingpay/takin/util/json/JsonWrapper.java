package com.suixingpay.takin.util.json;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * 手动解释Json工具类
 * @author jiayu.qiu
 */
public class JsonWrapper {

    private final JsonNode root;

    public JsonWrapper(String json) throws Exception {
        this.root=JsonUtil.toJsonTree(json);
    }

    public JsonWrapper(JsonNode root) {
        this.root=root;
    }

    /**
     * 根据路径取值，如：{\"data\":{\"id\":1}}，wrapper.getValeByPath("data.id")
     * @param path 节点路径，如：data.id
     * @return 节点字符串格式的值
     */
    public Optional<String> getValueByPath(String path) {
        Optional<JsonNode> node=getJsonNode(path);
        String value=null;
        if(node.isPresent()) {
            value=node.get().asText();
        }
        if("null".equals(value)) {
            value=null;
        }
        return null == value ? Optional.empty() : Optional.of(value);
    }

    /**
     * 根据路径节点，如：{\"data\":{\"id\":1}}，wrapper.getJsonNode("data.id")
     * @param path 节点路径，如：data.id
     * @return 节点
     */
    public Optional<JsonNode> getJsonNode(String path) {
        if(path == null || path.isEmpty() || this.root == null) {
            return Optional.empty();
        }
        JsonNode node=this.root;
        StringTokenizer st=new StringTokenizer(path, ".");
        while(st.hasMoreTokens()) {
            String nodeName=st.nextToken().trim();
            if(nodeName.isEmpty() || ((node=node.get(nodeName)) == null)) {
                return Optional.empty();
            }
        }
        return Optional.ofNullable(node);
    }

    /**
     * 根据路径节点数组
     * @param path 节点路径，如：data.id
     * @return 节点返回数组
     */
    public List<JsonNode> getNodeList(String path) {
        Optional<JsonNode> node=getJsonNode(path);
        List<JsonNode> result=new ArrayList<JsonNode>();
        if(!node.isPresent()) {
            return result;
        }

        Iterator<JsonNode> iter=node.get().elements();
        while(iter.hasNext()) {
            result.add(iter.next());
        }
        return result;
    }

    /**
     * 根据路径节点数组
     * @param path 节点路径，如：data.id
     * @return 节点返回数组
     */
    public List<JsonWrapper> getListWrapper(String path) {
        Optional<JsonNode> node=getJsonNode(path);
        List<JsonWrapper> result=new ArrayList<JsonWrapper>();
        if(!node.isPresent()) {
            return result;
        }

        Iterator<JsonNode> iter=node.get().elements();
        while(iter.hasNext()) {
            result.add(new JsonWrapper(iter.next()));
        }
        return result;
    }

    public boolean isEmpty() {
        return (this.root == null) || (this.root.size() == 0);
    }

    public JsonNode getRoot() {
        return root;
    }

    
}
