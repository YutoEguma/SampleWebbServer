package io.github.yutoeguma;

import io.github.yutoeguma.enums.HttpStatus;
import io.github.yutoeguma.exeptions.BadRequestException;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 通信を受け付けたら
 * HTTPプロトコルとしてリクエストを解析しレスポンスをクライアントに返すタスク
 *
 * @author yuto.eguma
 */
public class CreateResponseTask implements Runnable {

    private static Logger logger = Logger.getLogger(CreateResponseTask.class);
    private static HttpRequestHandler handler = HttpRequestHandler.get();

    private Socket socket;

    public CreateResponseTask(Socket socket) {
        this.socket = socket;
    }

    /**
     * レスポンスを返すタスク
     */
    @Override
    public void run() {

        try (OutputStream os = socket.getOutputStream()) {
            HttpResponse response;

            try {
                HttpRequest request = new HttpRequest(socket.getInputStream());
                response = handler.handle(request);
                logger.info(request.getRequestHeaderAttr());

                // リクエスト解析中に発生する Exception は以下でハンドリングする
            } catch (BadRequestException e) {
                logger.info("Bad Request", e);
                response = new HttpResponse(HttpStatus.BAD_REQUEST);
            } catch (IOException e) {
                logger.error("IOException occurred, Internal Server Error", e);
                response = new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (Exception e) {
                logger.error("Internal Server Error", e);
                response = new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            response.writeResponse(os);
            logger.info(response.getResponseHeaderAttr());
        } catch (IOException e) {
            logger.error("IOException occurred");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                logger.error("socket not closed");
            }
        }
    }
}
