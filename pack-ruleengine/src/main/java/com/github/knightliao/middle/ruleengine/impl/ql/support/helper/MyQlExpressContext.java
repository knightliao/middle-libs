package com.github.knightliao.middle.ruleengine.impl.ql.support.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ql.util.express.IExpressContext;

public class MyQlExpressContext extends HashMap<String, Object> implements
        IExpressContext<String, Object> {

    private static final String PRE_TAG = "$";

    public MyQlExpressContext(Map<String, Object> map) {
        // 需要经过 put处理才能生效
        for (String key : map.keySet()) {
            this.put(key, map.get(key));
        }
    }

    /**
     * 抽象方法：根据名称从属性列表中提取属性值
     */
    @Override
    public Object get(Object name) {

        return super.get(name);
    }

    @Override
    public Object put(String name, Object object) {
        return super.put(PRE_TAG + name, object);
    }

    public boolean containAllKey(List<String> requireKeys) {

        return keySet().containsAll(requireKeys);
    }
}
