package io.github.yutoeguma.exeptions;

import lombok.NoArgsConstructor;

// TODO yuto.eguma 名前を変える (2017/03/27)
// TODO yuto.eguma このExceptionが投げられたら何もしないようにハンドリングする (2017/03/27)
/**
 *
 * リクエストの形式が正しくない際に返す例外です
 * これが投げられると、400を返します
 *
 * @author yuto.eguma
 */
@NoArgsConstructor
public class BadRequestException extends RuntimeException {

    public BadRequestException(String msg) {
        super(msg);
    }
}
