package io.github.yutoeguma;

import lombok.Data;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

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

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private String header; /** ヘッダーはほぼ文字列 */
    private byte[] body; /** 返すコンテンツは文字列とは限らない */
    private HttpStatus status;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public HttpResponse(HttpStatus status) {
        this.header = "HTTP/1.1 " + status.getStatusCode() + " " + status.toString();
        this.body = new byte[0];
    }

    public HttpResponse(HttpStatus status, byte[] body) {
        this.header = "HTTP/1.1 " + status.getStatusCode() + " " + status.toString();
        this.body = body;
    }

    // ===================================================================================
    //                                                                              Getter
    //                                                                              ======
    /**
     * バイナリでレスポンスを返す
     *
     * @return 返す値
     */
    private byte[] getRespBinary() {
        List<Byte> byteList = new ArrayList<>();

        // ヘッダーのバイトコード化
        for (byte b : this.getHeader().getBytes()) {
            byteList.add(b);
        }
        for (byte b : (CRLF + CRLF).getBytes()) {
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

    public void writeResponce(OutputStream os) throws IOException {
        os.write(this.getRespBinary());
    }
}
