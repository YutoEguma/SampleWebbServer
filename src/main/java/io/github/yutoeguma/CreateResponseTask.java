package io.github.yutoeguma;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 通信を受け付けたら
 * HTTPプロトコルとしてリクエストを解析し
 * レスポンスを返すタスク
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
            HttpRequest request = new HttpRequest(socket.getInputStream());
            HttpResponse response = handler.apply(request);
            response.writeResponce(os);
        } catch (IOException e) {
            logger.error("IOException Internal Server Error", e);
        } catch (Exception e) {
            logger.error("Internal Server Error", e);
        }
    }
}
