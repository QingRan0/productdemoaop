//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.javaee.productdemoaop.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.productdemoaop.dao.bo.OnSale;
import cn.edu.xmu.javaee.productdemoaop.dao.bo.Product;
import cn.edu.xmu.javaee.productdemoaop.dao.bo.User;
import cn.edu.xmu.javaee.productdemoaop.mapper.generator.ProductPoMapper;
import cn.edu.xmu.javaee.productdemoaop.mapper.generator.po.ProductPo;
import cn.edu.xmu.javaee.productdemoaop.mapper.generator.po.ProductPoExample;
import cn.edu.xmu.javaee.productdemoaop.mapper.manual.ProductAllMapper;
import cn.edu.xmu.javaee.productdemoaop.mapper.manual.ProductJoinMapper;
import cn.edu.xmu.javaee.productdemoaop.mapper.manual.po.ProductAllPo;
import cn.edu.xmu.javaee.productdemoaop.mapper.manual.po.ProductJoinPo;
import cn.edu.xmu.javaee.productdemoaop.repository.ProductRepository;
import cn.edu.xmu.javaee.productdemoaop.repository.model.ProductModel;
import cn.edu.xmu.javaee.productdemoaop.util.CloneFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ming Qiu
 **/
@Repository
public class ProductDao {

    private final static Logger logger = LoggerFactory.getLogger(ProductDao.class);

    private ProductPoMapper productPoMapper;

    private OnSaleDao onSaleDao;

    private ProductAllMapper productAllMapper;

    private ProductJoinMapper productJoinPoMapper;

    private ProductRepository productRepository;

    @Autowired
    public ProductDao(ProductPoMapper productPoMapper, OnSaleDao onSaleDao, ProductAllMapper productAllMapper, ProductJoinMapper productJoinPoMapper, ProductRepository productRepository) {
        this.productPoMapper = productPoMapper;
        this.onSaleDao = onSaleDao;
        this.productAllMapper = productAllMapper;
        this.productJoinPoMapper = productJoinPoMapper;
        this.productRepository = productRepository;
    }

    /**
     * 用GoodsPo对象找Goods对象
     * @param name
     * @return  Goods对象列表，带关联的Product返回
     */
    public List<Product> retrieveProductByName(String name, boolean all) throws BusinessException {
        List<Product> productList = new ArrayList<>();
        ProductPoExample example = new ProductPoExample();
        ProductPoExample.Criteria criteria = example.createCriteria();
        criteria.andNameEqualTo(name);
        List<ProductPo> productPoList = productPoMapper.selectByExample(example);
        for (ProductPo po : productPoList){
            Product product = null;
            if (all) {
                product = this.retrieveFullProduct(po);
            } else {
                product = CloneFactory.copy(new Product(), po);
            }
            productList.add(product);
        }
        logger.debug("retrieveProductByName: productList = {}", productList);
        return productList;
    }

    /**
     * 用GoodsPo对象找Goods对象
     * @param  productId
     * @return  Goods对象列表，带关联的Product返回
     */
    public Product retrieveProductByID(Long productId, boolean all) throws BusinessException {
        Product product = null;
        ProductPo productPo = productPoMapper.selectByPrimaryKey(productId);
        if (null == productPo){
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, "产品id不存在");
        }
        if (all) {
            product = this.retrieveFullProduct(productPo);
        } else {
            product = CloneFactory.copy(new Product(), productPo);
        }

        logger.debug("retrieveProductByID: product = {}",  product);
        return product;
    }


    private Product retrieveFullProduct(ProductPo productPo) throws DataAccessException{
        assert productPo != null;
        Product product =  CloneFactory.copy(new Product(), productPo);
        List<OnSale> latestOnSale = onSaleDao.getLatestOnSale(productPo.getId());
        product.setOnSaleList(latestOnSale);

        List<Product> otherProduct = this.retrieveOtherProduct(productPo);
        product.setOtherProduct(otherProduct);

        return product;
    }

    private List<Product> retrieveOtherProduct(ProductPo productPo) throws DataAccessException{
        assert productPo != null;

        ProductPoExample example = new ProductPoExample();
        ProductPoExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsIdEqualTo(productPo.getGoodsId());
        criteria.andIdNotEqualTo(productPo.getId());
        List<ProductPo> productPoList = productPoMapper.selectByExample(example);
        return productPoList.stream().map(po->CloneFactory.copy(new Product(), po)).collect(Collectors.toList());
    }

    /**
     * 创建Goods对象
     * @param product 传入的Goods对象
     * @return 返回对象ReturnObj
     */
    public Product createProduct(Product product, User user) throws BusinessException{

        Product retObj = null;
        product.setCreator(user);
        product.setGmtCreate(LocalDateTime.now());
        ProductPo po = CloneFactory.copy(new ProductPo(), product);
        int ret = productPoMapper.insertSelective(po);
        retObj = CloneFactory.copy(new Product(), po);
        return retObj;
    }

    /**
     * 修改商品信息
     * @param product 传入的product对象
     * @return void
     */
    public void modiProduct(Product product, User user) throws BusinessException{
        product.setGmtModified(LocalDateTime.now());
        product.setModifier(user);
        ProductPo po = CloneFactory.copy(new ProductPo(), product);
        int ret = productPoMapper.updateByPrimaryKeySelective(po);
        if (ret == 0 ){
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST);
        }
    }

    /**
     * 删除商品，连带规格
     * @param id 商品id
     * @return
     */
    public void deleteProduct(Long id) throws BusinessException{
        int ret = productPoMapper.deleteByPrimaryKey(id);
        if (ret == 0) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST);
        }
    }

    public List<Product> findProductByName_manual(String name) throws BusinessException {
        List<Product> productList;
        ProductPoExample example = new ProductPoExample();
        ProductPoExample.Criteria criteria = example.createCriteria();
        criteria.andNameEqualTo(name);
        List<ProductAllPo> productPoList = productAllMapper.getProductWithAll(example);
        productList =  productPoList.stream().map(o->CloneFactory.copy(new Product(), o)).collect(Collectors.toList());
        logger.debug("findProductByName_manual: productList = {}", productList);
        return productList;
    }

    public List<Product> findProductByName_join(String name) throws BusinessException {
        // 定义存储结果的列表
        List<Product> productList;

        // 调用 productJoinPoMapper 执行查询
        List<ProductJoinPo> productJoinPoList = productJoinPoMapper.getProductPartsByName(name);

        // 如果查询结果为空，抛出 BusinessException 异常
        if (productJoinPoList.isEmpty()) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, "产品不存在");
        }

        // 处理查询结果，构造 productAllPo 对象
        ProductJoinPo joinPo = productJoinPoList.get(0);  // 获取第一个结果
        ProductAllPo productAllPo = ProductAllPo.builder()
                .id(joinPo.getId())
                .skuSn(joinPo.getSkuSn())
                .name(joinPo.getName())
                .originalPrice(joinPo.getOriginalPrice())
                .weight(joinPo.getWeight())
                .barcode(joinPo.getBarcode())
                .unit(joinPo.getUnit())
                .originPlace(joinPo.getOriginPlace())
                .creatorId(joinPo.getCreatorId())
                .creatorName(joinPo.getCreatorName())
                .modifierId(joinPo.getModifierId())
                .modifierName(joinPo.getModifierName())
                .gmtCreate(joinPo.getGmtCreate())
                .gmtModified(joinPo.getGmtModified())
                .commissionRatio(joinPo.getCommissionRatio())
                .freeThreshold(joinPo.getFreeThreshold())
                .status(joinPo.getStatus())
                .otherProduct(new ArrayList<>())  // 初始化 empty list
                .onSaleList(new ArrayList<>())   // 初始化 empty list
                .build();

        // 遍历查询结果，填充 ProductAllPo 中的 otherProduct 和 onSaleList
        for (ProductJoinPo po : productJoinPoList) {
            productAllPo.getOtherProduct().add(po.getOtherProduct());
            productAllPo.getOnSaleList().add(po.getOnSaleList());
        }

        // 将处理后的 productAllPo 添加到结果列表中
        List<ProductAllPo> productPoList = new ArrayList<>();
        productPoList.add(productAllPo);

        // 将 productAllPo 转换为 Product 对象
        productList =  productPoList.stream().map(o->CloneFactory.copy(new Product(), o)).collect(Collectors.toList());
        logger.debug("findProductByName_join: productList = {}", productList);
        return productList;
    }

    /**
     * 用GoodsPo对象找Goods对象
     * @param  productId
     * @return  Goods对象列表，带关联的Product返回
     */
    public Product findProductByID_manual(Long productId) throws BusinessException {
        Product product = null;
        ProductPoExample example = new ProductPoExample();
        ProductPoExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(productId);
        List<ProductAllPo> productPoList = productAllMapper.getProductWithAll(example);

        if (productPoList.size() == 0){
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, "产品id不存在");
        }
        product = CloneFactory.copy(new Product(), productPoList.get(0));
        logger.debug("findProductByID_manual: product = {}", product);
        return product;
    }

    public List<Product> findProductByName_jpa(String name) {
        logger.debug("retrieveProductByName_jpa: name = {}", name);

        // 根据商品名称查询所有商品
        List<ProductModel> productModels = productRepository.findByNameContaining(name);
        List<Product> products = new ArrayList<>();

        // 遍历每个 ProductModel，以此创建 Product 实例
        for (ProductModel productModel : productModels) {
            Product product = this.retrieveFullProduct(productModel);
            products.add(product);
        }

        return products;
    }

    private Product retrieveFullProduct(ProductModel productModel) {
        Product product = CloneFactory.copy(new Product(), productModel);

        // 查询 OnSale => Product.onSaleList
        List<OnSale> onSales = onSaleDao.getLastOnSaleByProductId(productModel.getId());
        product.setOnSaleList(onSales);

        // 查询 OtherProduct => Product.otherProduct
        List<Product> otherProducts = this.retrieveOtherProduct(productModel);
        product.setOtherProduct(otherProducts);

        return product;
    }

    private List<Product> retrieveOtherProduct(ProductModel productModel) {
        List<ProductModel> otherProductModels = productRepository.findByGoodsId(productModel.getGoodsId());
        List<Product> otherProducts = new ArrayList<>();
        for (ProductModel otherProductModel : otherProductModels) {
            if (otherProductModel.getId().equals(productModel.getId())) {
                continue;
            }
            Product otherProduct = CloneFactory.copy(new Product(), otherProductModel);
            otherProducts.add(otherProduct);
        }
        return otherProducts;
    }
}
