package com.leyou.item.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="tb_category")
@Data    //注解在类上；提供类所有属性的 getting 和 setting 方法，此外还提供了equals、canEqual、hashCode、toString 方法
public class Category {
	@Id
	//@GeneratedValue(strategy= GenerationType.IDENTITY)
	@KeySql(useGeneratedKeys = true)
	private Long id;
	private String name;
	private Long parentId;
	private Boolean isParent;
	private Integer sort;
	// getter和setter略
    // 注意isParent的get和set方法
}