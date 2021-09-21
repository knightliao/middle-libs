package com.github.knightliao.middle.lang.ables.data;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/9/21 02:02
 */
public interface IDataAware<DataType> extends IExtAware {

    String getId();

    void setId();

    String getType();

    void setType();

    DataType getData();

    void setData(DataType data);

    boolean isEmpty();

    default boolean isNoEmpty() {
        return !isEmpty();
    }
}
