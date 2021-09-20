package com.github.knightliao.middle.ruleengine.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.github.knightliao.middle.ruleengine.IExpressExecutor;
import com.github.knightliao.middle.ruleengine.factory.ExpressExecutorFactory;
import com.github.knightliao.middle.ruleengine.test.dto.UserInfoTest;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/9/11 13:27
 */
public class ExpressExecutorTestCase {

    private IExpressExecutor expressExecutor = ExpressExecutorFactory.getExpressExecutorQl();

    @Test
    public void test() {

        Map<String, Object> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);

        map.put("user1", new UserInfoTest(1, "1", 3));
        map.put("user2", null);

        Object value2 = expressExecutor.execute(
                "if($user2!=null){ $a + $b + $user2.getId() } else { $a + $b + $user1.getId() }", map);
        Assert.assertEquals(value2, 4L);

        Object value3 = expressExecutor.execute("($a > $b) && (true) ", map);
        Assert.assertFalse((Boolean) value3);
    }
}
