package com.leyou.item.controller;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 45207
 * @create 2018-08-21 17:33
 */
@RestController
@RequestMapping
public class GoodsController {

    @Autowired
    private GoodsService goodsService;


    /**
     * 分页查询spu
     * @param page
     * @param rows
     * @param saleable
     * @param key
     * @return
     */
    @GetMapping("spu/page")
    public ResponseEntity<PageResult<Spu>> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1")Integer page,
            @RequestParam(value = "rows", defaultValue = "5")Integer rows,
            @RequestParam(value = "saleable", required = false)Boolean saleable,
            @RequestParam(value = "key", required = false)String key
    ){
        return ResponseEntity.ok(goodsService.queryBrandByPage(page, rows, saleable, key));

    }

    /**
     * 新增商品
     * @param spu
     * @return
     */
    @PostMapping("goods")
    public ResponseEntity<Void> saveGoods(@RequestBody Spu spu){
        goodsService.saveGoods(spu);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 修改商品
     * @param spu
     * @return
     */
    @PutMapping("goods")
    public ResponseEntity<Void> updateGoods(@RequestBody Spu spu){
        goodsService.updateGoods(spu);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    /**
     * 根据spuID查询SpuDetail
     * @param spuId
     * @return
     */
    @GetMapping("spu/detail/{spuId}")
    public ResponseEntity<SpuDetail> querySpuDetailBySpuId(@PathVariable("spuId")Long spuId){
        return ResponseEntity.ok(goodsService.querySpuDetailBySpuId(spuId));
    }


    /**
     * 根据spu查询skuList
     * @param spuId
     * @return
     */
    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>> querySkuListBySouId(@RequestParam("id")Long spuId){
        return ResponseEntity.ok(goodsService.querySkuListBySpuId(spuId));
    }

    @GetMapping("spu/{id}")
    public ResponseEntity<Spu> querySpuById(@PathVariable("id") Long id){
        Spu spu = goodsService.querySpuById(id);
        if (spu == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(spu);
    }

}
