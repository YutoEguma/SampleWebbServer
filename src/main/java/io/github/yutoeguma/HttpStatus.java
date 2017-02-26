package io.github.yutoeguma;

import lombok.Getter;

/**
 * HTTP通信のレスポンスとそのステータスコードを定義するEnumです
 *
 * @author yuto.eguma
 */
public enum HttpStatus {

    OK(200),
    BAD_REQUEST(400),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500);

    @Getter
    private int statusCode;

    HttpStatus(int statusCode) {
        this.statusCode = statusCode;
    }
}
