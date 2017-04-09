package io.github.yutoeguma;

import io.github.yutoeguma.enums.HttpStatus;
import io.github.yutoeguma.exeptions.IncorrectHttpRequestException;
import io.github.yutoeguma.http.message.HttpRequest;
import io.github.yutoeguma.http.HttpRequestHandler;
import io.github.yutoeguma.http.message.HttpResponse;
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
public class WriteResponseTask implements Runnable {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static Logger logger = Logger.getLogger(WriteResponseTask.class);
    private static HttpRequestHandler handler = HttpRequestHandler.get();

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private Socket socket;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public WriteResponseTask(Socket socket) {
        this.socket = socket;
    }

    /**
     * レスポンスを返すタスク
     */
    @Override
    public void run() {

        try (
            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream()
        ) {
            HttpResponse response = createResponse(is);
            os.write(response.getRespBinary());
            logger.info(response.getResponseHeaderAttr());

        } catch (IOException e) {
            // レスポンスの書き込み処理の中で起きる例外
            // クライアントからの接続遮断など
            logger.error("IOException occurred", e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                logger.error("socket not closed", e);
            }
        }
    }

    /**
     * リクエストの解析を行い、それに応じたレスポンスを返す
     * @param is InputStream
     * @return HTTPResponse
     */
    private HttpResponse createResponse(InputStream is) {
        try {
            HttpRequest request = new HttpRequest(is);
            return handler.handle(request);

        } catch (IncorrectHttpRequestException e) {
            // memo : リクエストメッセージが HTTP の仕様に沿っていない場合には、400をレスポンスで返すようにする
            logger.info("Bad Request", e);
            return new HttpResponse(HttpStatus.BAD_REQUEST);
        }
    }
}
