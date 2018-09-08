package com.leyou.item.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tb_brand")
@Data
public class Brand {
    @Id
    @KeySql(useGeneratedKeys = true)    //这种方式更好,官方推荐,相比@KeySql(userGeneratedKeys = true)少写一条sql查询
    private Long id;
    private String name;// 品牌名称
    private String image;// 品牌图片
    private Character letter;
    // getter setter 略
}