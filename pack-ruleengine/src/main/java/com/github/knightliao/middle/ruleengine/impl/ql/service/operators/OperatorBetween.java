package com.github.knightliao.middle.ruleengine.impl.ql.service.operators;

import java.math.BigDecimal;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import com.github.knightliao.middle.lang.security.MyAssertUtils;
import com.ql.util.express.Operator;
import com.ql.util.express.OperatorOfNumber;
import com.ql.util.express.instruction.op.OperatorEqualsLessMore;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/9/11 11:29
 */
public class OperatorBetween extends Operator {

    public static final int PARAM_LENGTH = 2;
    public static final int RIGHT_LENGTH = 2;

    public static final int NUMBER_TYPE_INT = 3;
    public static final int NUMBER_TYPE_LONG = 4;
    public static final int NUMBER_TYPE_FLOAT = 5;
    public static final int NUMBER_TYPE_DOUBLE = 6;
    public static final int NUMBER_TYPE_BIGDECIMAL = 7;

    public OperatorBetween() {

    }

    public OperatorBetween(String aliasName, String aName, String aErrorInfo) {

        this.name = name;
        this.aliasName = aliasName;
        this.errorInfo = aErrorInfo;
    }

    @Override
    public Object executeInner(Object[] objects) throws Exception {

        //
        MyAssertUtils.assertTrue(objects != null,
                "between required two params ");
        MyAssertUtils.assertTrue(objects.length == PARAM_LENGTH,
                "between required two params " + Arrays.toString(objects));

        //
        if (objects[0] == null || objects[1] == null) {
            return false;
        }

        //
        Object obj = objects[0];
        Object rightObj = objects[1];
        String content = rightObj.toString();
        if (content.contains("，")) {
            content = content.replaceAll("，", ",");
        }

        ///
        String[] arr = {};
        if (content.contains(",")) {
            arr = content.split(",");
        }

        //
        MyAssertUtils.assertTrue(
                (arr.length == RIGHT_LENGTH && !StringUtils.isBlank(arr[0]) && !StringUtils.isBlank(arr[1])),
                "between right value not be support " + Arrays.toString(objects));

        //
        arr[0] = StringUtils.trim(arr[0]);
        arr[1] = StringUtils.trim(arr[1]);
        if (obj instanceof String) {
            return OperatorEqualsLessMore.executeInner(">=", obj, arr[0])
                    && OperatorEqualsLessMore.executeInner("<=", obj, arr[1]);

        } else if (obj instanceof Number) {
            return compareByNumber(obj, arr[0], arr[1]);

        } else {

            throw new Exception("between op object type not support " + Arrays.toString(objects));
        }
    }

    private boolean compareByNumber(Object obj, String left, String right) throws Exception {

        int type = OperatorOfNumber.getSeq(obj.getClass());

        if (type <= NUMBER_TYPE_INT) {
            return OperatorEqualsLessMore.executeInner(">=", obj, Integer.valueOf(left))
                    && OperatorEqualsLessMore.executeInner("<=", obj, Integer.valueOf(right));

        } else if (type == NUMBER_TYPE_LONG) {
            return OperatorEqualsLessMore.executeInner(">=", obj, Long.valueOf(left))
                    && OperatorEqualsLessMore.executeInner("<=", obj, Long.valueOf(right));

        } else if (type == NUMBER_TYPE_FLOAT || type == NUMBER_TYPE_DOUBLE) {
            return OperatorEqualsLessMore.executeInner(">=", obj, Double.valueOf(left))
                    && OperatorEqualsLessMore.executeInner("<=", obj, Double.valueOf(right));

        } else if (type == NUMBER_TYPE_BIGDECIMAL) {
            return OperatorEqualsLessMore.executeInner(">=", obj, new BigDecimal(left))
                    && OperatorEqualsLessMore.executeInner("<=", obj, new BigDecimal(right));
        }

        return false;
    }
}
