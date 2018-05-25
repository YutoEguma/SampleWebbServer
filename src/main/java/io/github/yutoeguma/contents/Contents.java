package io.github.yutoeguma.contents;

/**
 * @author cabos
 */
public class Contents {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    /** コンテンツの場所 */
    final private String filePath;
    /** コンテンツの内容 */
    final private byte[] detail;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public Contents(String filePath) {
        this(filePath, new byte[0]);
    }


    public Contents(String filePath, byte[] detail) {
        this.filePath = filePath;
        this.detail = detail;
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

    public String getFilePath() {
        return filePath;
    }

    public byte[] getDetail() {
        return detail;
    }
}
