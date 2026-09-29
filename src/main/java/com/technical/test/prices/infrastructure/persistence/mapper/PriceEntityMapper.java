package com.technical.test.prices.infrastructure.persistence.mapper;

import com.technical.test.prices.domain.model.BrandId;
import com.technical.test.prices.domain.model.Price;
import com.technical.test.prices.domain.model.ProductId;
import com.technical.test.prices.infrastructure.persistence.entity.PriceEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PriceEntityMapper {

    @Mapping(source = "brand.brandId", target = "brandId")
    @Mapping(source = "product.productId", target = "productId")
    Price toDomain(PriceEntity entity);

    List<Price> toDomain(List<PriceEntity> entities);

    default BrandId toBrandId(Long value) {
        return new BrandId(value);
    }

    default ProductId toProductId(Long value) {
        return new ProductId(value);
    }
}
