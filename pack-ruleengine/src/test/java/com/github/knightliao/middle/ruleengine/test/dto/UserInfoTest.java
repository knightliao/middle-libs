package com.github.knightliao.middle.ruleengine.test.dto;

import lombok.Data;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/9/11 02:33
 */
@Data
public class UserInfoTest {

    private long id;
    private long tag;
    private String name;

    public UserInfoTest(long aId, String aName, long aUserTag) {
        this.id = aId;
        this.tag = aUserTag;
        this.name = aName;
    }

}
