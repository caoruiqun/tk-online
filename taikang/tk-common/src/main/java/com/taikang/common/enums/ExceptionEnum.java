package com.taikang.common.enums;

public enum ExceptionEnum {

//    PRICE_CANNOT_BE_NULL(400,"价格不能为空！"),
    CATEGORY_NOT_FOUND(404,"商品分类没查到！"),
    BRAND_NOT_FOUND(404,"品牌没有查到！"),
    BRAND_ADD_ERROR(500,"品牌新增失败！"),
    FILE_UPLOAD_ERROR(500,"文件上传失败！"),
    INVALID_FILE_TYPE(400,"无效的文件类型！"),
    GROUP_NOT_FOUND(404,"商品规格组未找到！"),
    PARAM_NOT_FOUND(404,"商品规格参数未找到！"),
    GOODS_NOT_FOUND(404,"商品未找到！"),
    GOOD_SAVE_ERROR(500,"商品新增失败！"),
    GOOD_DETAIL_ERROR(404,"商品详情未查到！"),
    GOOD_SKU_ERROR(404,"商品SKU未查到！"),
    GOOD_STOCK_ERROR(404,"商品库存未查到！"),
    GOOD_UPDATE_ERROR(500,"商品更新失败！"),
    GOOD_ID_ERROR(400,"商品id不能为空！"),
    INVALID_USERNAME_PASSWORD(400,"用户名或密码错误！"),
    CREATE_TOKEN_ERROR(500,"生成用户凭证失败！"),
    UNAUTHORIZED(403,"未授权！"),
    CART_NOT_FUND(404,"购物车不存在！"),
    ORDER_NOT_FOUND(404,"订单不存在！"),
    ORDER_DETAIL_NOT_FOUND(404,"订单详情不存在！"),
    ORDER_STATUS_NOT_FOUND(404,"订单状态不存在！"),
    INVAILD_USER_DATA_TYPE(400,"用户数据类型无效！"),
    INVAILD_VERIFY_CODE(400,"无效的验证码！")
    ;

    private Integer code;
    private String msg;

    ExceptionEnum() {
    }

    ExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
