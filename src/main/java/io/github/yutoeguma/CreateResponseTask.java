package io.github.yutoeguma;

import io.github.yutoeguma.exeption.BadRequestException;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 通信を受け付けたら
 * HTTPプロトコルとしてリクエストを解析し
 * レスポンスをクライアントに返すタスク
 *
 * @author yuto.eguma
 */
public class CreateResponseTask implements Runnable {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static Logger logger = Logger.getLogger(CreateResponseTask.class);
    private static HttpRequestHandler handler = HttpRequestHandler.get();

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private Socket socket;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public CreateResponseTask(Socket socket) {
        this.socket = socket;
    }

    // ===================================================================================
    //                                                                                Task
    //                                                                               =====

    /**
     * レスポンスを書き込む
     */
    @Override
    public void run() {

        try (OutputStream os = socket.getOutputStream()) {
            HttpResponse response;

            // リクエスト解析中に発生する Exception はここでハンドリングする
            try {
                HttpRequest request = new HttpRequest(socket.getInputStream());
                response = handler.handle(request);
            } catch (BadRequestException e) {
                logger.info("Bad Request", e);
                response = new HttpResponse(HttpStatus.BAD_REQUEST);
            } catch (IOException e) {
                logger.error("IOException occurred in create response, Internal Server Error", e);
                response = new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (Exception e) {
                logger.error("Internal Server Error", e);
                response = new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            response.writeResponse(os);
        } catch (IOException e) {
            logger.error("IOException occurred in writing response");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                logger.error("socket not closed");
            }
        }
    }
}
