package com.leyou.item.mapper;

import com.leyou.item.pojo.Category;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * IdlistMapper 第一个参数是当前实体类, 第二个是主键类型,只要继承即可使用
 * @author 45207
 * @create 2018-08-17 20:12
 */
public interface CategoryMapper extends Mapper<Category>, IdListMapper<Category, Long> {
}
