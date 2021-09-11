package com.github.knightliao.middle.ruleengine.factory;

import com.github.knightliao.middle.ruleengine.IExpressExecutor;
import com.github.knightliao.middle.ruleengine.impl.ql.ExpressExecutorQlImpl;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/9/11 13:29
 */
public class ExpressExecutorFactory {

    public static IExpressExecutor getExpressExecutorQl() {
        return new ExpressExecutorQlImpl();
    }
}
