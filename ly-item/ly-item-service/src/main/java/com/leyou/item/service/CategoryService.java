package com.leyou.item.service;

import com.leyou.common.enumeration.ExceptionEnumeration;
import com.leyou.common.exception.LyException;
import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author 45207
 * @create 2018-08-17 20:18
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 根据父id查询子分类
     * @param pid
     * @return
     */
    public List<Category> queryCategoryListByPid(Long pid) {
        Category c = new Category();
        c.setParentId(pid);
        List<Category> list = categoryMapper.select(c);
        if(CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnumeration.CHILD_CATEGORY_QUERY_ERROR);
        }
        return list;
    }

    public List<Category> queryCategoryByIds(List<Long> ids){
        List<Category> list = categoryMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(list)) {
            throw new LyException(ExceptionEnumeration.CATEGORY_QUERY_ERROR);
        }
        return list;
    }
}
