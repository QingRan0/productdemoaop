package cn.edu.xmu.javaee.productdemoaop.mapper.manual;

import org.apache.ibatis.jdbc.SQL;

public class ProductJoinPoSqlProvider {
    public String getProductByNameWithJoin(String name) {
        return new SQL() {{
            SELECT("p.id");
            SELECT("p.sku_sn");
            SELECT("p.name");
            SELECT("p.original_price");
            SELECT("p.weight");
            SELECT("p.barcode");
            SELECT("p.unit");
            SELECT("p.origin_place");
            SELECT("p.commission_ratio");
            SELECT("p.free_threshold");
            SELECT("p.status");
            SELECT("p.creator_id");
            SELECT("p.creator_name");
            SELECT("p.modifier_id");
            SELECT("p.modifier_name");
            SELECT("p.gmt_create");
            SELECT("p.gmt_modified");
            SELECT("os.id AS onSale_id");
            SELECT("os.product_id AS onSale_product_id");
            SELECT("os.price AS onSale_price");
            SELECT("os.begin_time AS onSale_begin_time");
            SELECT("os.end_time AS onSale_end_time");
            SELECT("os.quantity AS onSale_quantity");
            SELECT("os.max_quantity AS onSale_max_quantity");
            SELECT("os.creator_id AS onSale_creator_id");
            SELECT("os.creator_name AS onSale_creator_name");
            SELECT("os.modifier_id AS onSale_modifier_id");
            SELECT("os.modifier_name AS onSale_modifier_name");
            SELECT("os.gmt_create AS onSale_gmt_create");
            SELECT("os.gmt_modified AS onSale_gmt_modified");
            SELECT("gp.id AS otherProduct_id");
            SELECT("gp.goods_id AS otherProduct_goods_id");
            SELECT("gp.sku_sn AS otherProduct_sku_sn");
            SELECT("gp.name AS otherProduct_name");
            SELECT("gp.original_price AS otherProduct_original_price");
            SELECT("gp.weight AS otherProduct_weight");
            SELECT("gp.barcode AS otherProduct_barcode");
            SELECT("gp.unit AS otherProduct_unit");
            SELECT("gp.origin_place AS otherProduct_origin_place");
            SELECT("gp.creator_id AS otherProduct_creator_id");
            SELECT("gp.creator_name AS otherProduct_creator_name");
            SELECT("gp.modifier_id AS otherProduct_modifier_id");
            SELECT("gp.modifier_name AS otherProduct_modifier_name");
            SELECT("gp.gmt_create AS otherProduct_gmt_create");
            SELECT("gp.gmt_modified AS otherProduct_gmt_modified");

            FROM("goods_product p");
            INNER_JOIN("goods_onsale os ON p.id = os.product_id AND os.begin_time <= NOW() AND os.end_time > NOW()");
            INNER_JOIN("goods_product gp ON p.goods_id = gp.goods_id");

            WHERE("p.name = #{name}");
        }}.toString();
    }
}
