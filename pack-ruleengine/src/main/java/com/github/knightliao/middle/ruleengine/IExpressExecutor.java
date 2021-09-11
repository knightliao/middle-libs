package com.github.knightliao.middle.ruleengine;

import java.util.List;
import java.util.Map;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/9/11 11:19
 */
public interface IExpressExecutor {

    Object execute(String expressStr, Map<String, Object> params);

    Object execute(String expressStr, Map<String, Object> params, List<String> errorList);
}
