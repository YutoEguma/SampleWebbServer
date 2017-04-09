package io.github.yutoeguma;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author yuto.eguma
 */
public class SampleWebServer {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static final int portNum = 8090;
    private static final Logger logger = Logger.getLogger(SampleWebServer.class);

    // ===================================================================================
    //                                                                                Main
    //                                                                                ====
    public static void main(String[] args) throws IOException {
        logger.info("Sample Server\n");
        ServerSocket serverSocket = new ServerSocket(portNum);
        while (true) {
            Socket socket = serverSocket.accept();
            Thread thread = new Thread(new WriteResponseTask(socket));
            thread.start();
        }
    }
}
