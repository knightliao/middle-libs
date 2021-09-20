package com.github.knightliao.middle.utils.test.common;

import org.junit.Test;

import com.github.knightliao.middle.utils.devlopment.IdeaDetectUtil;
import com.github.knightliao.test.support.utils.TestUtils;

/**
 * @author knightliao
 * @date 2016/12/10 14:47
 */
public class TestCommonTestCase {

    @Test
    public void test() {

        TestUtils.testAllClassUnderPackage("com.github.knightliao.middle.utils");
    }

    @Test
    public void testIDea() {

        System.out.println(IdeaDetectUtil.isInIDea());
    }
}
