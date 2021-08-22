package com.github.knightliao.middle.api.rpc.dto;

import lombok.Data;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/20 01:27
 */
@Data
public class BaseRpcRequest {

    private long uid;

    public static BaseRpcRequest build(long uid) {
        BaseRpcRequest baseRpcRequest = new BaseRpcRequest();
        baseRpcRequest.setUid(uid);
        return baseRpcRequest;
    }
}
