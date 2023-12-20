package com.devstack.pos.bo.custom.impl;

import com.devstack.pos.bo.custom.ProductDetailBo;
import com.devstack.pos.dao.DaoFactory;
import com.devstack.pos.dao.custom.ProductDetailDao;
import com.devstack.pos.dto.CustomerDto;
import com.devstack.pos.dto.ProductDetailDto;
import com.devstack.pos.dto.ProductDetailJoinDto;
import com.devstack.pos.entity.Customer;
import com.devstack.pos.entity.ProductDetail;
import com.devstack.pos.enums.DaoType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDetailBoImpl implements ProductDetailBo {

    ProductDetailDao productDetailDao = DaoFactory.getInstance().getDao(DaoType.PRODUCT_DETAIL);

    @Override
    public boolean saveProductDetail(ProductDetailDto productDetailDto) throws SQLException, ClassNotFoundException {
        return productDetailDao.save(new ProductDetail(
                productDetailDto.getCode(),
                productDetailDto.getBarcode(),
                productDetailDto.getQtyOnHand(),
                productDetailDto.getSellingPrice(),
                productDetailDto.getShowPrice(),
                productDetailDto.getBuyingPrice(),
                productDetailDto.getProductCode(),
                productDetailDto.isDiscountAvailability()
        ));
    }

    @Override
    public boolean updateProductDetail(ProductDetailDto productDetailDto) throws SQLException, ClassNotFoundException {
        return productDetailDao.update(new ProductDetail(
                productDetailDto.getCode(),
                productDetailDto.getBarcode(),
                productDetailDto.getQtyOnHand(),
                productDetailDto.getSellingPrice(),
                productDetailDto.getShowPrice(),
                productDetailDto.getBuyingPrice(),
                productDetailDto.getProductCode(),
                productDetailDto.isDiscountAvailability()
        ));
    }

    @Override
    public boolean deleteProductDetail(String code) throws SQLException, ClassNotFoundException {
        return productDetailDao.delete(code);
    }

    @Override
    public ProductDetailDto findProductDetail(String code) throws SQLException, ClassNotFoundException {
        ProductDetail productDetail = productDetailDao.find(code);

        if (productDetail != null) {
            return new ProductDetailDto(
                    productDetail.getCode(),
                    productDetail.getBarcode(),
                    productDetail.getQtyOnHand(),
                    productDetail.getSellingPrice(),
                    productDetail.getShowPrice(),
                    productDetail.getBuyingPrice(),
                    productDetail.getProductCode(),
                    productDetail.isDiscountAvailability()
            );
        }
        return null;
    }

    @Override
    public List<ProductDetailDto> findAllProductDetails() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public List<ProductDetailDto> findAllProductDetails(int productCode) throws SQLException, ClassNotFoundException {
        List<ProductDetailDto> productDetailDtos = new ArrayList<>();

        for(ProductDetail productDetail : productDetailDao.findAll(productCode)){
            productDetailDtos.add(
                    new ProductDetailDto(
                            productDetail.getCode(),
                            productDetail.getBarcode(),
                            productDetail.getQtyOnHand(),
                            productDetail.getSellingPrice(),
                            productDetail.getShowPrice(),
                            productDetail.getBuyingPrice(),
                            productDetail.getProductCode(),
                            productDetail.isDiscountAvailability()
                    )
            );
        }
        return productDetailDtos;
    }

    @Override
    public ProductDetailJoinDto findProductJoinDetail(String code) throws SQLException, ClassNotFoundException {
        return productDetailDao.findProductJoinDetail(code);
    }

    @Override
    public boolean manageQty(String barcode, int qty) throws SQLException, ClassNotFoundException {
        return productDetailDao.manageQty(barcode,qty);
    }
}
