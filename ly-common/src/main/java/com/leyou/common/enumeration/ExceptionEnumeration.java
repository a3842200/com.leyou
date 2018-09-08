package com.leyou.common.enumeration;

/**
 * @author 45207
 * @create 2018-08-19 14:32
 */

//单例是指一个类里只有一个对象,枚举是指一个类李有有限个对象
public enum ExceptionEnumeration {
    FILE_TYPE_ERROR(400, "文件类型错误"),
    FILE_CONTENT_ERROR(400, "文件类型错误"),
    FILE_UPLOAD_ERROR(500, "文件上传失败"),
    GOODS_GROUP_SPECIFICATION_ERROR(204, "该分类下没有规格组"),
    GOODS_PARAM_SPECIFICATION_ERROR(204, "该规格组下没有规格参数"),
    BRAND_ADD_ERROR(500, "新增品牌失败"),
    CATEGORY_BRAND_ADD_ERROR(500, "新增品牌和分类失败"),
    SPECPARAM_ADD_ERROR(500, "新增规格参数失败"),
    GOODS_QUERY_ERROR(204, "没有查询到商品信息(SpuList)"),
    BRAND_QUERY_ERROR(204, "没有查询到品牌信息"),
    BRANDLIST_QUERY_ERROR(204, "没有查询到品牌列表信息"),
    CATEGORY_QUERY_ERROR(204, "该分类不存在"),
    CATEGORYLIST_QUERY_ERROR(204, "该分类列表不存在"),
    CHILD_CATEGORY_QUERY_ERROR(204, "该分类下没有子分类"),
    QUERY_SPUDETAIL_ERROR(204, "没有查询到spuDetail信息"),
    QUERY_SKULIST_ERROR(204, "没有查询到skuList信息"),
    QUERY_STOCK_ERROR(500, "商品库存查询失败"),
    REQUEST_PARAM_ERROR(400, "商品id不能而为空")


    ;

    private int code;   //异常状态码
    private String msg;    //异常说明

    ExceptionEnumeration(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
