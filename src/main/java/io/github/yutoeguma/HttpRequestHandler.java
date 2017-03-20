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
