package io.github.yutoeguma;

import lombok.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author yuto.eguma
 */
@Data
public class HttpRequest {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static final String CR = "\r";
    private static final String LF = "\n";
    private static final String CRLF = CR + LF;
    private static final String SP = " ";

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private String header;
    private String body;

    private String requestLine;
    private String method;
    private String requestTarget;
    private String httpVersion;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public HttpRequest(InputStream in) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            this.setHeader(reader);
            this.divideHeader();
            this.setBody(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    // ===================================================================================
    //                                                                              Setter
    //                                                                              ======
    private void setHeader(BufferedReader reader) throws IOException {

        StringBuilder headerBuilder = new StringBuilder();
        String headerLine = reader.readLine();
        while (headerLine != null && !headerLine.isEmpty()) {
            headerBuilder.append(headerLine).append(CRLF);
            headerLine = reader.readLine();
        }
        this.header = headerBuilder.toString();
    }

    private void divideHeader() {
        List<String> headerLineList = Arrays.asList(this.header.split(CRLF));

        /* request-line */
        this.requestLine = headerLineList.get(0);
        this.setRequestLineElement();

        // TODO yuto ここでしっかりヘッダーを分ける (2017/02/18)
    }

    private void setRequestLineElement() {
        List<String> requestLineList = Arrays.asList(this.requestLine.split(SP));
        if (requestLineList.size() != 3) {
            // TODO yuto 何Exceptionを返す？ (2017/02/18)
        }
        this.method = requestLineList.get(0);
        this.requestTarget = requestLineList.get(1);
        this.httpVersion = requestLineList.get(2);
    }

    private void setBody(BufferedReader reader) {
        // TODO yuto ここで コンテンツの大きさを取得、body に値をセットする (2017/02/18)
    }
}
