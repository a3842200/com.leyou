package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.enumeration.ExceptionEnumeration;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.item.mapper.SkuMapper;
import com.leyou.item.mapper.SpuDetailMapper;
import com.leyou.item.mapper.SpuMapper;
import com.leyou.item.mapper.StockMapper;
import com.leyou.item.pojo.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 45207
 * @create 2018-08-21 17:33
 */
@Service
public class GoodsService {
    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    public PageResult<Spu> queryBrandByPage(Integer page, Integer rows, Boolean saleable, String key) {
        //分页
        PageHelper.startPage(page, rows);

        //过滤
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();

        //过滤逻辑删除
        criteria.andEqualTo("valid", true);

        //搜索条件
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("title", "%" + key + "%");
        }

        //上下架
        if (saleable != null) {
            criteria.andEqualTo("saleable", saleable);
        }

        //查询结果
        List<Spu> spuList = spuMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(spuList)) {
            throw new LyException(ExceptionEnumeration.GOODS_QUERY_ERROR);
        }


        //查询分类名称和品牌(把id转成名字)
        handleCategoryAndBrandName(spuList);

        //分装分页结果
        PageInfo<Spu> pageInfo = new PageInfo<>(spuList);
        return new PageResult<>(pageInfo.getTotal(), spuList);

    }

    private void handleCategoryAndBrandName(List<Spu> spuList) {
        for (Spu spu : spuList) {
            //查询分类
            List<Long> ids = Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3());
            List<Category>  list = categoryService.queryCategoryByIds(ids);
            if (list == null) {
                throw new LyException(ExceptionEnumeration.CATEGORYLIST_QUERY_ERROR);
            }
            List<String> nameList = list.stream().map(category -> category.getName()).collect(Collectors.toList());
            spu.setCname(StringUtils.join(nameList,"/"));

            //查询品牌
            Brand brand = brandService.queryBrandById(spu.getBrandId());
            spu.setBname(brand.getName());

        }
    }

    @Transactional
    public void saveGoods(Spu spu) {
        //新增spu
        //手动添加数据库中需要的而页面没有传递的参数
        spu.setSaleable(true);
        spu.setValid(true);
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(spu.getCreateTime());
        spuMapper.insert(spu);

        //新增spuDetail
        SpuDetail spuDetail = spu.getSpuDetail();
        spuDetail.setSpuId(spu.getId());
        spuDetailMapper.insert(spuDetail);

        //新增skuList和stock
        saveSkuListAndStock(spu);

        //发送消息
        amqpTemplate.convertAndSend("item.insert", spu.getId());
    }


    public SpuDetail querySpuDetailBySpuId(Long spuId) {
        SpuDetail spuDetail = spuDetailMapper.selectByPrimaryKey(spuId);
        if (spuDetail == null) {
            throw new LyException(ExceptionEnumeration.QUERY_SPUDETAIL_ERROR);
        }
        return spuDetail;
    }

    public List<Sku> querySkuListBySpuId(Long spuId) {
        //查询sku
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        List<Sku> skuList = skuMapper.select(sku);
        if (CollectionUtils.isEmpty(skuList)) {
            throw new LyException(ExceptionEnumeration.QUERY_SKULIST_ERROR);
        }
        //查询库存
        for (Sku s : skuList) {
            Stock stock = stockMapper.selectByPrimaryKey(s.getId());
            s.setStock(stock.getStock());
            if (stock == null) {
                throw new LyException(ExceptionEnumeration.QUERY_STOCK_ERROR);
            }
        }
        return skuList;
    }

    @Transactional
    public void updateGoods(Spu spu) {
        //只有spu的id不为空才能进行
        if (spu.getId() == null) {
            throw new LyException(ExceptionEnumeration.REQUEST_PARAM_ERROR);
        }

        //先查询以前的sku
        Sku sku = new Sku();
        sku.setSpuId(spu.getId());
        List<Sku> skuList = skuMapper.select(sku);
        //如果skuList不为空,需要先删除再新增,因为修改功能中用户对sku的修改是不可控的
        if (CollectionUtils.isNotEmpty(skuList)) {
            //删除sku
            skuMapper.delete(sku);
            //删除库存
            List<Long> ids = skuList.stream().map(s -> s.getId()).collect(Collectors.toList());
            stockMapper.deleteByIdList(ids);
        }

        //skuList和stock已经为空了
        //修改spu(控制一些不能修改的参数)
        spu.setCreateTime(null);
        spu.setValid(null);
        spu.setSaleable(null);
        spu.setLastUpdateTime(new Date());
        spuMapper.updateByPrimaryKeySelective(spu);

        //修改spuDetail(一对一关系)
        spuDetailMapper.updateByPrimaryKey(spu.getSpuDetail());

        //新增skuList和stock(之前做过,这里使用提取的方法)
        saveSkuListAndStock(spu);

        //发送消息
        amqpTemplate.convertAndSend("item.update", spu.getId());

    }


    ////新增skuList和stock
    private void saveSkuListAndStock(Spu spu) {
        //新增sku
        List<Sku> skuList = spu.getSkus();
        ArrayList<Stock> stockList = new ArrayList<>();

        for (Sku sku : skuList) {
            //对sku新增
            sku.setSpuId(spu.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            //因为批量新增无法回显,只能在循环里一个一个新增了
            skuMapper.insert(sku);

            //生成stock
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stockList.add(stock);
        }
        //批量新增sku(因为页面新增一个spu会有多个sku), 能够批量新增是因为skuMapper继承了BaseMapper(其中有对应的批量新增集合等方法),无法回显,舍弃了
        //skuMapper.insertList(skuList);


        //批量新增库存(库存和sku是一对一,spu和stock是一对多,所以这里也用批量)
        stockMapper.insertList(stockList);
    }

    public Spu querySpuById(Long id) {
        //查询spu
        Spu spu = spuMapper.selectByPrimaryKey(id);
        //查询spu下的sku集合(本页方法中有,直接调用)
        spu.setSkus(querySkuListBySpuId(id));
        //查询detail(同上)
        spu.setSpuDetail(querySpuDetailBySpuId(id));
        return spu;
    }
}
