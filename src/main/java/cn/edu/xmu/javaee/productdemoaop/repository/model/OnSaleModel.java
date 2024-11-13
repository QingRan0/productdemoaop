package cn.edu.xmu.javaee.productdemoaop.repository.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "goods_onsale")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OnSaleModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "shop_id", nullable = false)
    private Long shopId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "begin_time", nullable = false)
    private LocalDateTime beginTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "type", nullable = false)
    private Byte type;

    @Column(name = "creator_id", nullable = false)
    private Long creatorId;

    @Column(name = "creator_name", length = 255)
    private String creatorName;

    @Column(name = "modifier_id")
    private Long modifierId;

    @Column(name = "modifier_name", length = 255)
    private String modifierName;

    @Column(name = "gmt_create", nullable = false)
    private LocalDateTime gmtCreate;

    @Column(name = "gmt_modified", nullable = false)
    private LocalDateTime gmtModified;

    @Column(name = "max_quantity", nullable = false)
    private Integer maxQuantity;

    @Column(name = "invalid", nullable = false)
    private Byte invalid;
}
