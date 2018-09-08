package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.enumeration.ExceptionEnumeration;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author 45207
 * @create 2018-08-18 12:58
 */
@Service
public class BrandService {

    @Autowired
    private BrandMapper brandMapper;

    /**
     * 分页查询品牌信息
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @param key
     * @return
     */
    public PageResult<Brand> queryBrandByPage(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        //分页
        PageHelper.startPage(page, rows);
        //过滤
        Example example = new Example(Brand.class);
        if (StringUtils.isNotBlank(key)){
            example.createCriteria().orLike("name", "%" + key + "%")
                    .orEqualTo("letter", key.toUpperCase());
        }
        //排序
        if (StringUtils.isNotBlank(sortBy)){
            //e.g:  example.setOrderByClause(id DESC)
            example.setOrderByClause(sortBy + (desc ? " DESC":" ASC"));
        }

        //查询结果
        List<Brand> list = brandMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnumeration.BRANDLIST_QUERY_ERROR);
        }

        //封装分页对象
        PageInfo<Brand> info = new PageInfo<>(list);

        //返回
        return new PageResult<>(info.getTotal(),list);
    }

    /**
     * 使用事务保存品牌信息(tb_brand表)
     * 和 tb_brand_category表
     * @param brand
     * @param cids
     */
    @Transactional
    public void saveBrand(Brand brand, List<Long> cids) {
        brand.setId(null);
        int count = brandMapper.insert(brand);
        if (count==0){
            throw new LyException(ExceptionEnumeration.BRAND_ADD_ERROR);
        }
        for (Long cid : cids) {
            int count2 = brandMapper.saveCategoryBrand(cid, brand.getId());
            if (count2==0){
                throw new LyException(ExceptionEnumeration.CATEGORY_BRAND_ADD_ERROR);
            }
        }

    }

    public Brand queryBrandById(Long id){

        Brand brand = brandMapper.selectByPrimaryKey(id);
        if (brand == null) {
            throw new LyException(ExceptionEnumeration.BRAND_QUERY_ERROR);
        }
        return brand;
    }

    public List<Brand> queryBrandByCid(Long cid) {
        List<Brand> list = brandMapper.queryBrandByCid(cid);
        if (list == null) {
            throw new LyException(ExceptionEnumeration.BRANDLIST_QUERY_ERROR);
        }
        return list;
    }

    public List<Brand> queryBrandsByIds(List<Long> ids) {
        List<Brand> brandList = brandMapper.selectByIdList(ids);
        if(CollectionUtils.isEmpty(brandList)){
            throw new LyException(ExceptionEnumeration.BRANDLIST_QUERY_ERROR);
        }
        return brandList;
    }
}
