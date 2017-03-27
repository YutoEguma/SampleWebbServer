package io.github.yutoeguma;

import io.github.yutoeguma.enums.HttpStatus;
import io.github.yutoeguma.exeptions.ContentsNotFoundException;

import java.io.IOException;

/**
 * Httpリクエストを受け取り、レスポンスに変換するクラスです
 */
public class HttpRequestHandler {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static ContentsLoader contentsLoader = ContentsLoader.get();
    private static HttpRequestHandler httpRequestHandler = new HttpRequestHandler();

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public static HttpRequestHandler get() {
        return httpRequestHandler;
    }

    // ===================================================================================
    //                                                                             Handler
    //                                                                             =======
    /**
     * コンテンツを取得してレスポンスを返す
     *
     * @param request HTTPリクエスト
     * @return コンテンツ取得によるレスポンス
     */
    public HttpResponse handle(HttpRequest request) {
        // レスポンス作成中に発生する Exception はここでハンドリングする
        // TODO yuto.eguma 500以外なら例外を投げない作りにする (2017/03/27)
        // memo : コンテンツが存在しないものリクエストに対して例外を投げているのはおかしい...
        try {
            Contents contents = contentsLoader.getContents(request.getRequestTarget());
            return new HttpResponse(HttpStatus.OK, contents);
        } catch (ContentsNotFoundException e) {
            return new HttpResponse(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
