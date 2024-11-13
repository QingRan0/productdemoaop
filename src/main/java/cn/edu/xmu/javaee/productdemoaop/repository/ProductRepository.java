package cn.edu.xmu.javaee.productdemoaop.repository;

import cn.edu.xmu.javaee.productdemoaop.repository.model.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<ProductModel, Long> {

    List<ProductModel> findByNameContaining(String name);

    List<ProductModel> findByGoodsId(Long goodsId);
}
