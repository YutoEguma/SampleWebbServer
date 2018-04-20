package io.github.yutoeguma.http.message;

import io.github.yutoeguma.contents.ContentsLoadResult;
import io.github.yutoeguma.enums.ContentType;
import io.github.yutoeguma.enums.HttpStatus;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yuto.eguma
 */
@Data
public class HttpResponse {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static final String CR = "\r";
    private static final String LF = "\n";
    private static final String CRLF = CR + LF;
    private static final String SP = " ";

    private static final String CONTENT_TYPE = "Content-Type: ";
    private static final String CONTENT_LENGTH = "Content-Length: ";

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    /** response-line */
    private String responseLine;
    /** レスポンスヘッダの要素のMap */
    private Map<String, String> responseHeaderAttr = new HashMap<>();
    /** レスポンスボディ */
    private byte[] body;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public HttpResponse(HttpStatus status) {
        this.responseLine = "HTTP/1.1" + SP + status.getStatusCode() + SP + status.toString();
        this.responseHeaderAttr.put(CONTENT_TYPE, ContentType.textPlain.getContentType());
        this.responseHeaderAttr.put(CONTENT_LENGTH, "0");
        this.body = new byte[0];
    }

    public HttpResponse(HttpStatus status, ContentsLoadResult contentsLoadResult) {
        this.responseLine = "HTTP/1.1" + SP + status.getStatusCode() + SP + status.toString();
        this.responseHeaderAttr.put(CONTENT_TYPE, ContentType.extensionOf(contentsLoadResult.getExtension()).getContentType());
        this.responseHeaderAttr.put(CONTENT_LENGTH, String.valueOf(contentsLoadResult.getDetail().length));
        this.body = contentsLoadResult.getDetail();
    }

    // ===================================================================================
    //                                                                              Getter
    //                                                                              ======
    /**
     * バイナリでレスポンスを返す
     */
    public byte[] getRespBinary() {
        List<Byte> byteList = new ArrayList<>();

        // ヘッダーのバイトコード化
        for (byte b : this.getHeader().getBytes()) {
            byteList.add(b);
        }
        for (byte b : (CRLF).getBytes()) {
            byteList.add(b);
        }

        // byte型レスポンスの作成
        byte[] byteArray = new byte[byteList.size() + body.length];
        for (int i = 0; i < byteList.size(); i++) {
            byteArray[i] = byteList.get(i);
        }
        for (int i = 0; i < this.body.length; i++) {
            byteArray[byteList.size() + i] = this.body[i];
        }
        return byteArray;
    }

    /**
     * ヘッダーを取得する
     */
    public String getHeader() {
        return this.responseLine + CRLF
                + responseHeaderAttr.entrySet().stream()
                .map(entry -> entry.getKey() + entry.getValue() + CRLF).collect(Collectors.joining());
    }

    public void addResponseHeader(String key, String value) {
        this.responseHeaderAttr.put(key + ": ", value);
    }
}
