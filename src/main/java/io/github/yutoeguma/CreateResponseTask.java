package io.github.yutoeguma;

import io.github.yutoeguma.exeption.BadRequestException;
import io.github.yutoeguma.exeption.ContentsNotFoundException;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.NoSuchFileException;

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
    private static ContentsLoader contentsLoader = ContentsLoader.getContentsLoader();

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
        try (
                OutputStream outputStream = socket.getOutputStream()
        ) {
            logger.info("=============> request");
            HttpRequest request = new HttpRequest(socket.getInputStream());

            logger.info("=============> response");
            try {
                HttpResponse response = new HttpResponse(HttpStatus.OK,
                        contentsLoader.getContents(request.getRequestTarget()));
                outputStream.write(response.getRespBinary());

            } catch (BadRequestException e) { // 400
                HttpResponse response = new HttpResponse(HttpStatus.BAD_REQUEST);
                outputStream.write(response.getRespBinary());
            } catch (ContentsNotFoundException | NoSuchFileException e) { // 404
                HttpResponse response = new HttpResponse(HttpStatus.NOT_FOUND);
                outputStream.write(response.getRespBinary());

            } catch (Exception e) { // 500
                HttpResponse response = new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR);
                outputStream.write(response.getRespBinary());
            }

        } catch (IOException e) {
            // 今来なら以下は来ないはず、来てもレスポンスを返せない
            logger.error("IOException Internal Server Error", e);
        } catch (Exception e) {
            logger.error("Internal Server Error", e);
        }
    }
}
