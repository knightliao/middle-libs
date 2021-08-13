package com.github.knightliao.middle.idgen;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/13 15:04
 */
public interface IIdgenService {

    Long getSequenceId(long key);

    Long getSequenceId(String key);
}
