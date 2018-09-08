package com.leyou.item.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Table(name = "tb_spu")
@Data
public class Spu {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private Long brandId;
    private Long cid1;// 1级类目
    private Long cid2;// 2级类目
    private Long cid3;// 3级类目
    private String title;// 标题
    private String subTitle;// 子标题
    private Boolean saleable;// 是否上架

    @JsonIgnore //忽略json数据(把值过滤)
    private Boolean valid;// 是否有效，逻辑删除用
    @JsonIgnore
    private Date createTime;// 创建时间
    @JsonIgnore
    private Date lastUpdateTime;// 最后修改时间

    // @Transient表示该属性并非一个到数据库表的字段的映射,ORM框架将忽略该属性.
    //如果一个属性并非数据库表的字段映射,就务必将其标示为@Transient,否则,ORM框架默认其注解为@Basic
    @Transient  //顺时的意思,本应该出现在VO对象中,把数据库里的忽略
    private String cname;   //cname用于响应页面中的商品分类字段
    @Transient
    private String bname;   //bname用于响应品牌字段
    @Transient
    private SpuDetail spuDetail;
    @Transient
    private List<Sku> skus;
}