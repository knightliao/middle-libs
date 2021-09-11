package com.github.knightliao.middle.ruleengine.impl.ql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.github.knightliao.middle.lang.constants.PackConstants;
import com.github.knightliao.middle.lang.exceptions.exceptions.others.SystemException;
import com.github.knightliao.middle.ruleengine.IExpressExecutor;
import com.github.knightliao.middle.ruleengine.impl.ql.service.operators.OperatorBetween;
import com.github.knightliao.middle.ruleengine.impl.ql.support.helper.MyQlExpressContext;
import com.github.knightliao.middle.ruleengine.support.constants.RuleEngineConstants;
import com.ql.util.express.ExpressRunner;
import com.ql.util.express.InstructionSet;

import lombok.extern.slf4j.Slf4j;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/9/11 11:22
 */
@Slf4j
public class ExpressExecutorQlImpl implements IExpressExecutor {

    private static volatile boolean isInit = false;
    private final static ExpressRunner expressRunner = new ExpressRunner();

    @Override
    public Object execute(String expressStr, Map<String, Object> params) {
        return execute(expressStr, params, new ArrayList<>());
    }

    @Override
    public Object execute(String expressStr, Map<String, Object> params, List<String> errorList) {

        try {

            //
            init(expressRunner);

            //
            MyQlExpressContext myQlExpressContext = new MyQlExpressContext(params);

            //
            String statement = prepare(expressStr);

            //
            final InstructionSet is = expressRunner.getInstructionSetFromLocalCache(expressStr);
            List<String> requireKeys = null;
            if (is != null) {
                requireKeys = genRequireKeys(is);
                if (myQlExpressContext.containAllKey(requireKeys)) {
                    return expressRunner.execute(statement, myQlExpressContext, errorList, true, false);
                }
            }
            throw new RuntimeException(
                    "express require keys not found " + requireKeys + " | " + myQlExpressContext.toString());

        } catch (Exception e) {
            throw new RuntimeException("express failed " + expressStr + " " + params, e);
        }

    }

    private List<String> genRequireKeys(InstructionSet is) throws Exception {
        List<String> requiredKeys = Arrays.asList(is.getOutAttrNames());
        if (CollectionUtils.isNotEmpty(requiredKeys)) {
            requiredKeys.remove(PackConstants.NULL_LOWERCASE);
        }
        return requiredKeys;
    }

    private String prepare(String statement) {
        return statement.replace("（", "(").
                replace("）", ")").
                replace("(", "(").
                replace("；", ";").
                replace("，", ",");
    }

    private void init(ExpressRunner expressRunner) {

        if (isInit) {
            return;
        }

        synchronized(expressRunner) {
            if (isInit) {
                return;
            }

            try {
                expressRunner.addOperator(RuleEngineConstants.OPERATOR_BETWEEN,
                        new OperatorBetween(RuleEngineConstants.OPERATOR_BETWEEN,
                                RuleEngineConstants.OPERATOR_BETWEEN, "$1 不在区间 $2 中"));

            } catch (Exception e) {

                String error = "express runner init exception ";
                throw new SystemException(error, e);
            }

            isInit = true;
        }
    }
}
