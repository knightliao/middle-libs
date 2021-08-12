package com.github.knightliao.middle.idem;

import com.github.knightliao.middle.idem.exception.IdemException;
import com.github.knightliao.middle.idem.service.callback.IIdemBizCallback;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/12 12:18
 */
public interface IIdemService {

    <T> T executeWithResult(String idemKey, IIdemBizCallback<T> callback, int lockTimeMills, Class<T> myClass);

    <T> boolean execute(String idemKey, IIdemBizCallback<T> callback, int lockTimeMills) throws IdemException;
}
