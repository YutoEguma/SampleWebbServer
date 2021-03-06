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
    /** 読み込んでいるコンテンツ */
    private Contents contents;
    /** コンテンツを読み込んだ結果 */
    private ContentsLoadResultType loadResultType;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public ContentsLoadResult(String filePath, byte[] detail, ContentsLoadResultType loadResult) {
        this.contents = new Contents(filePath, detail);
        this.loadResultType = loadResult;
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
}
