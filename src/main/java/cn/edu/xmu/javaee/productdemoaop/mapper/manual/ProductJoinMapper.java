package cn.edu.xmu.javaee.productdemoaop.mapper.manual;

import cn.edu.xmu.javaee.productdemoaop.mapper.manual.po.ProductJoinPo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Mapper
public interface ProductJoinMapper {

    @SelectProvider(type = ProductJoinPoSqlProvider.class, method = "getProductByNameWithJoin")
    @Results({
            @Result(column = "product_id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "sku_sn", property = "skuSn", jdbcType = JdbcType.VARCHAR),
            @Result(column = "name", property = "name", jdbcType = JdbcType.VARCHAR),
            @Result(column = "original_price", property = "originalPrice", jdbcType = JdbcType.BIGINT),
            @Result(column = "weight", property = "weight", jdbcType = JdbcType.BIGINT),
            @Result(column = "barcode", property = "barcode", jdbcType = JdbcType.VARCHAR),
            @Result(column = "unit", property = "unit", jdbcType = JdbcType.VARCHAR),
            @Result(column = "origin_place", property = "originPlace", jdbcType = JdbcType.VARCHAR),
            @Result(column = "commission_ratio", property = "commissionRatio", jdbcType = JdbcType.INTEGER),
            @Result(column = "free_threshold", property = "freeThreshold", jdbcType = JdbcType.BIGINT),
            @Result(column = "status", property = "status", jdbcType = JdbcType.SMALLINT),
            @Result(column = "creator_id", property = "creatorId", jdbcType = JdbcType.BIGINT),
            @Result(column = "creator_name", property = "creatorName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "modifier_id", property = "modifierId", jdbcType = JdbcType.BIGINT),
            @Result(column = "modifier_name", property = "modifierName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "gmt_create", property = "gmtCreate", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "gmt_modified", property = "gmtModified", jdbcType = JdbcType.TIMESTAMP),

            // Mapping onSaleList to OnSalePo
            @Result(column = "onSale_id", property = "onSaleList.id", jdbcType = JdbcType.BIGINT),
            @Result(column = "onSale_product_id", property = "onSaleList.productId", jdbcType = JdbcType.BIGINT),
            @Result(column = "onSale_price", property = "onSaleList.price", jdbcType = JdbcType.BIGINT),
            @Result(column = "onSale_begin_time", property = "onSaleList.beginTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "onSale_end_time", property = "onSaleList.endTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "onSale_quantity", property = "onSaleList.quantity", jdbcType = JdbcType.INTEGER),
            @Result(column = "onSale_max_quantity", property = "onSaleList.maxQuantity", jdbcType = JdbcType.INTEGER),
            @Result(column = "onSale_creator_id", property = "onSaleList.creatorId", jdbcType = JdbcType.BIGINT),
            @Result(column = "onSale_creator_name", property = "onSaleList.creatorName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "onSale_modifier_id", property = "onSaleList.modifierId", jdbcType = JdbcType.BIGINT),
            @Result(column = "onSale_modifier_name", property = "onSaleList.modifierName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "onSale_gmt_create", property = "onSaleList.gmtCreate", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "onSale_gmt_modified", property = "onSaleList.gmtModified", jdbcType = JdbcType.TIMESTAMP),

            // Mapping otherProduct to ProductPo
            @Result(column = "otherProduct_id", property = "otherProduct.id", jdbcType = JdbcType.BIGINT),
            @Result(column = "otherProduct_sku_sn", property = "otherProduct.skuSn", jdbcType = JdbcType.VARCHAR),
            @Result(column = "otherProduct_name", property = "otherProduct.name", jdbcType = JdbcType.VARCHAR),
            @Result(column = "otherProduct_original_price", property = "otherProduct.originalPrice", jdbcType = JdbcType.BIGINT),
            @Result(column = "otherProduct_weight", property = "otherProduct.weight", jdbcType = JdbcType.BIGINT),
            @Result(column = "otherProduct_barcode", property = "otherProduct.barcode", jdbcType = JdbcType.VARCHAR),
            @Result(column = "otherProduct_unit", property = "otherProduct.unit", jdbcType = JdbcType.VARCHAR),
            @Result(column = "otherProduct_origin_place", property = "otherProduct.originPlace", jdbcType = JdbcType.VARCHAR),
            @Result(column = "otherProduct_creator_id", property = "otherProduct.creatorId", jdbcType = JdbcType.BIGINT),
            @Result(column = "otherProduct_creator_name", property = "otherProduct.creatorName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "otherProduct_modifier_id", property = "otherProduct.modifierId", jdbcType = JdbcType.BIGINT),
            @Result(column = "otherProduct_modifier_name", property = "otherProduct.modifierName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "otherProduct_gmt_create", property = "otherProduct.gmtCreate", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "otherProduct_gmt_modified", property = "otherProduct.gmtModified", jdbcType = JdbcType.TIMESTAMP)
    })
    List<ProductJoinPo> getProductPartsByName(@Param("name") String name);
}
