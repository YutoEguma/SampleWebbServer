package io.github.yutoeguma;

import io.github.yutoeguma.exeption.BadRequestException;
import io.github.yutoeguma.exeption.ContentsNotFoundException;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.NoSuchFileException;

/**
 * @author yuto.eguma
 */
public class SampleWebServer {

    private static final int portNum = 8090;
    private static final Logger logger = Logger.getLogger("ROOT");


    public static void main(String[] args) {
        logger.info("Sample Server\n");
        ContentsLoader contentsLoader = ContentsLoader.getContentsLoader();

        while (true) {
            try (
                    ServerSocket serverSocket = new ServerSocket(portNum);
                    Socket socket = serverSocket.accept();
                    OutputStream outputStream = socket.getOutputStream()
            ) {
                logger.info("=============> request");
                HttpRequest request = new HttpRequest(socket.getInputStream());
                logger.info(request.toString());

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
}
