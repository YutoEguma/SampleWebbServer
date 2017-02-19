package io.github.yutoeguma;

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
                    BufferedWriter respWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            ) {
                logger.info("=============> request");
                HttpRequest request = new HttpRequest(socket.getInputStream());
                logger.info(request.getBody());
                logger.info(request.getHeader());

                logger.info("=============> response");
                HttpResponse response = new HttpResponse();
                try {
                    response.setHeader("HTTP/1.1 200 OK");
                    response.setBody(contentsLoader.getContents(request.getRequestTarget()));
                    logger.info(response.toString());
                    respWriter.write(response.toString());
                } catch (NoSuchFileException e) {
                    response.setHeader("HTTP/1.1 404 Not Found");
                    logger.info(response.toString());
                    respWriter.write(response.toString());
                }

            } catch (IOException e) {
                logger.error("IOException Internal Server Error", e);
            } catch (Exception e) {
                logger.error("Internal Server Error", e);
            }
        }
    }
}
