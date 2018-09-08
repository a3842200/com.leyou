package com.leyou.common.mapper;

import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author 45207
 * @create 2018-08-22 14:09
 */

//T:当前实体类   PK:主键类型
@RegisterMapper
public interface BaseMapper<T,PK> extends Mapper<T>, IdListMapper<T, PK>, InsertListMapper<T> {
}
