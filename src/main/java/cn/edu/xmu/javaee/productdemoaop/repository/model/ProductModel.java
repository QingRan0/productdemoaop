package cn.edu.xmu.javaee.productdemoaop.repository.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "goods_product")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "shop_id")
    private Long shopId;

    @Column(name = "goods_id")
    private Long goodsId;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "template_id")
    private Long templateId;

    @Column(name = "sku_sn", length = 255)
    private String skuSn;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "original_price")
    private Long originalPrice;

    @Column(name = "weight")
    private Long weight;

    @Column(name = "barcode", length = 255)
    private String barcode;

    @Column(name = "unit", length = 255)
    private String unit;

    @Column(name = "origin_place", length = 255)
    private String originPlace;

    @Column(name = "creator_id")
    private Long creatorId;

    @Column(name = "creator_name", length = 255)
    private String creatorName;

    @Column(name = "modifier_id")
    private Long modifierId;

    @Column(name = "modifier_name", length = 255)
    private String modifierName;

    @Column(name = "gmt_create", nullable = false)
    private LocalDateTime gmtCreate;

    @Column(name = "gmt_modified")
    private LocalDateTime gmtModified;

    @Column(name = "status")
    private Byte status;

    @Column(name = "commission_ratio")
    private Integer commissionRatio;

    @Column(name = "shop_logistic_id")
    private Long shopLogisticId;

    @Column(name = "free_threshold")
    private Long freeThreshold;
}
