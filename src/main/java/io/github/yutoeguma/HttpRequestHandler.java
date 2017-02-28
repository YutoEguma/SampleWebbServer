package io.github.yutoeguma;

import io.github.yutoeguma.exeption.BadRequestException;
import io.github.yutoeguma.exeption.ContentsNotFoundException;
import java.nio.file.NoSuchFileException;
import java.util.function.Function;

/**
 * Httpリクエストを受け取り、レスポンスに変換するクラスです
 */
public class HttpRequestHandler implements Function<HttpRequest, HttpResponse> {

    private static ContentsLoader contentsLoader = ContentsLoader.getContentsLoader();

    private static HttpRequestHandler httpRequestHandler = new HttpRequestHandler();

    public static HttpRequestHandler get() {
        return httpRequestHandler;
    }

    @Override
    public HttpResponse apply(HttpRequest request) {
        try {
            return new HttpResponse(HttpStatus.OK, contentsLoader.getContents(request.getRequestTarget()));
        } catch (BadRequestException e) { // 400
            return new HttpResponse(HttpStatus.BAD_REQUEST);
        } catch (ContentsNotFoundException | NoSuchFileException e) { // 404
            return new HttpResponse(HttpStatus.NOT_FOUND);
        } catch (Exception e) { // 500
            return new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
