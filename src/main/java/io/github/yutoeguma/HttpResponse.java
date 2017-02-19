package io.github.yutoeguma;

import lombok.Data;

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
    private String header;
    private String body;

    @Override
    public String toString() {
        return (new StringBuilder()).append(header).append(CRLF).append(CRLF).append(body).toString();
    }
}
