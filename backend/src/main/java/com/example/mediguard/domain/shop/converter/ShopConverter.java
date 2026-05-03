package com.example.mediguard.domain.shop.converter;

import com.example.mediguard.domain.shop.dto.ExchangeResponseDto;
import com.example.mediguard.domain.shop.entity.Product;

import java.time.LocalDateTime;

public class ShopConverter {
    public static ExchangeResponseDto toResponseDto(Product product, String couponCode, LocalDateTime validUntil) {
        return ExchangeResponseDto.builder()
                .productId(product.getProductId())
                .brand(product.getBrand())
                .name(product.getName())
                .thumbnail(product.getThumbnail())
                .couponCode(couponCode)
                .validUntil(validUntil.toString())
                .build();
    }


}
