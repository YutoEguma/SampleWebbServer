package io.github.yutoeguma.http;

import io.github.yutoeguma.contents.ContentsLoadResult;
import io.github.yutoeguma.contents.ContentsLoader;
import io.github.yutoeguma.enums.HttpMethod;
import io.github.yutoeguma.enums.HttpStatus;
import io.github.yutoeguma.http.message.HttpRequest;
import io.github.yutoeguma.http.message.HttpResponse;
import io.github.yutoeguma.routing.Routing;

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
        String requestTarget = request.getRequestTarget();
        HttpMethod httpMethod = request.getHttpMethod();

        // ルーティングが定義されている場合は Action クラスの処理を行う
        Routing routing = Routing.routeOf(requestTarget, httpMethod);
        if (routing != null) {
            return routing.doAction(request);
        }

        // ルーティングが定義されていない場合は、ファイルを読み込む
        ContentsLoadResult contentsLoadResult = contentsLoader.loadContents(requestTarget);

        // memo : ファイルの読み込み処理の結果と、その場合のレスポンスのマッピング
        switch (contentsLoadResult.getLoadResultType()) {
            case loadSuccess:
                return new HttpResponse(HttpStatus.OK, contentsLoadResult);
            case forbidden:
                return new HttpResponse(HttpStatus.FORBIDDEN);
            case notExists:
                return new HttpResponse(HttpStatus.NOT_FOUND);
            case loadFailure:
                return new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR);

            // memo : マッピングを上で定義し忘れていたら、ここで落とす
            default:
                throw new IllegalStateException("予期しない ContentsLoadResultTypeです。 contentsLoadResultType: "
                        + contentsLoadResult.getLoadResultType());
        }
    }
}
