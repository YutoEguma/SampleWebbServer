package io.github.yutoeguma;

import io.github.yutoeguma.exeption.ContentsNotFoundException;
import java.io.IOException;

/**
 * Httpリクエストを受け取り、レスポンスに変換するクラスです
 */
public class HttpRequestHandler {

    private static ContentsLoader contentsLoader = ContentsLoader.getContentsLoader();

    private static HttpRequestHandler httpRequestHandler = new HttpRequestHandler();

    public static HttpRequestHandler get() {
        return httpRequestHandler;
    }

    /**
     * コンテンツを取得してレスポンスを返す
     *
     * @param request HTTPリクエスト
     * @return コンテンツ取得によるレスポンス
     */
    public HttpResponse handle(HttpRequest request) {
        // レスポンス作成中に発生する Exception はここでハンドリングする
        try {
            return new HttpResponse(HttpStatus.OK, contentsLoader.getContents(request.getRequestTarget()));
        } catch (ContentsNotFoundException e) {
            return new HttpResponse(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
