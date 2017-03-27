package io.github.yutoeguma;

import io.github.yutoeguma.enums.HttpStatus;
import io.github.yutoeguma.exeptions.BadRequestException;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
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

        HttpResponse response;
        try (
                OutputStream os = socket.getOutputStream();
                InputStream is = socket.getInputStream()
            ) {
                HttpRequest request = new HttpRequest(is);
                response = handler.handle(request);
                response.writeResponse(os);

            // リクエスト解析中に発生する Exception は以下でハンドリングする
            // TODO yuto.eguma ここに存在するExceptionは本当にレスポンスを返すべき？ (2017/03/27)
            } catch (BadRequestException e) {
                logger.info("Bad Request", e);
                response = new HttpResponse(HttpStatus.BAD_REQUEST);
            } catch (IOException e) {
                logger.error("IOException occurred, Internal Server Error", e);
                response = new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (Exception e) {
                logger.error("Internal Server Error", e);
                response = new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    logger.error("socket not closed");
                }
            }
            logger.info(response.getResponseHeaderAttr());
        }
}
