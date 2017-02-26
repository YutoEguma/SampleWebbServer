package io.github.yutoeguma;

import io.github.yutoeguma.exeption.BadRequestException;
import lombok.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
            throw new BadRequestException("リクエストの形式が不正です request line : "
                    + requestLineList.stream().collect(Collectors.joining()));
        }
        this.method = requestLineList.get(0);
        this.requestTarget = requestLineList.get(1);
        this.httpVersion = requestLineList.get(2);
    }

    private void setBody(BufferedReader reader) {
        // TODO yuto ここで コンテンツの大きさを取得、body に値をセットする (2017/02/18)
    }

    @Override
    public String toString() {
        return (new StringBuilder()).append(this.header)
            .append(CRLF)
            .append(CRLF)
            .append(this.body).toString();
    }
}
