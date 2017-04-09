package io.github.yutoeguma.contents;

import io.github.yutoeguma.enums.ContentsLoadResultType;
import lombok.Getter;

/**
 * @author yuto
 */
@Getter
public class ContentsLoadResult {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    /** コンテンツの場所 */
    private String filePath;
    /** コンテンツの内容 */
    private byte[] detail;
    /** コンテンツを読み込んだ結果 */
    private ContentsLoadResultType loadResultType;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public ContentsLoadResult(String filePath, byte[] detail, ContentsLoadResultType loadResult) {
        this.filePath = filePath;
        this.detail = detail;
        this.loadResultType = loadResult;
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========

    /**
     * コンテンツの拡張子を取得する
     * "." で区切り、一番最後の文字列を抜き出す
     *
     * @return 拡張子を取得する
     */
    public String getExtension() {
        String[] strArray = this.filePath.split("\\.");
        return strArray[strArray.length - 1];
    }
}
