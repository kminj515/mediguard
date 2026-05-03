package com.example.mediguard.domain.shop.exeption;

import com.example.mediguard.domain.shop.enums.ShopErrorCode;
import com.example.mediguard.global.exception.CustomException;

public class ShopException extends CustomException {
    public ShopException(ShopErrorCode errorCode) {
        super(errorCode);
    }
}
