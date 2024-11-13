package cn.edu.xmu.javaee.productdemoaop.repository;

import cn.edu.xmu.javaee.productdemoaop.repository.model.OnSaleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OnSaleRepository extends JpaRepository<OnSaleModel, Long> {

    @Query(value = "SELECT *" +
            "FROM goods_onsale " +
            "WHERE product_id = :productId AND begin_time <= NOW() AND end_time > NOW() ", nativeQuery = true)
    List<OnSaleModel> selectLastOnSaleByProductId(@Param("productId") Long productId);

}
