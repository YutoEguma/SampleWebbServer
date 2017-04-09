package io.github.yutoeguma;

import io.github.yutoeguma.enums.HttpStatus;
import io.github.yutoeguma.exeptions.ContentsNotFoundException;
import java.io.IOException;
import java.util.Optional;

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
        try {
            Optional<Contents> contentsOpt = contentsLoader.loadContents(request.getRequestTarget());
            // @formatter off
            return contentsOpt
                    .map(contents -> new HttpResponse(HttpStatus.OK, contents))
                    .orElse(new HttpResponse(HttpStatus.NOT_FOUND));
            // @formatter on
        } catch (IOException e) {
            return new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
