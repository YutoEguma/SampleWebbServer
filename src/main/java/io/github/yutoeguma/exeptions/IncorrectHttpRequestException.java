package io.github.yutoeguma.exeptions;

import lombok.NoArgsConstructor;

/**
 *
 * リクエストの形式が正しくない際に返す例外です
 * これが投げられると、400を返します
 *
 * @author yuto.eguma
 */
@NoArgsConstructor
public class IncorrectHttpRequestException extends RuntimeException {

    public IncorrectHttpRequestException(String msg) {
        super(msg);
    }
}
