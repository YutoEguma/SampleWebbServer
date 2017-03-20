package io.github.yutoeguma;

import io.github.yutoeguma.exeptions.BadRequestException;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yuto.eguma
 */
@Getter
@Setter
public class HttpRequest {

    private static final String CR = "\r";
    private static final String LF = "\n";
    private static final String CRLF = CR + LF;
    private static final String SP = " ";

    private String body;

    /** リクエストライン */
    private String requestLine;
    private String method;
    private String requestTarget;
    private String httpVersion;
    /** リクエストヘッダー */
    private Map<String, String> requestHeaderAttr = new HashMap<>();

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public HttpRequest(InputStream in) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            List<String> messageLineList = this.readRequestMessageLineList(reader);
            this.setRequestLine(messageLineList);
            this.setRequestHeader(messageLineList);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * リクエストメッセージを1行ごとに分けた文字列のリストに変換する
     *
     * @param reader BufferedReader
     * @return リクエストメッセージの1行ごとのリスト
     */
    private List<String> readRequestMessageLineList(BufferedReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        String headerLine = reader.readLine();
        while (headerLine != null && !headerLine.isEmpty()) {
            sb.append(headerLine).append(CRLF);
            headerLine = reader.readLine();
        }
        return Arrays.asList(sb.toString().split(CRLF));
    }

    /**
     * メッセージリストからリクエストラインを取り出す
     * @param messageLineList リクエストメッセージの1行ごとのリスト
     */
    private void setRequestLine(List<String> messageLineList) {
        this.requestLine = messageLineList.get(0);
        this.setRequestLineElement();
    }

    /**
     * リクエストメッセージをさらに分割する
     */
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

    /**
     * メッセージのリストを field-name と field-value に分割して Map として取得する
     * @param messageLineList リクエストメッセージの1行ごとのリスト
     */
    private void setRequestHeader(List<String> messageLineList) {
        this.requestHeaderAttr = messageLineList.stream().skip(1)
                .map(line -> line.split(":" + SP))
                .filter(strArray -> strArray.length == 2)
                .collect(Collectors.toMap(strArray -> strArray[0], strArray -> strArray[1]));
    }
}
