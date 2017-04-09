package io.github.yutoeguma.enums;

/**
 * コンテンツ取得に成功したか、失敗したか
 */
public enum ContentsLoadResultType {

    /** 取得に成功 */
    loadSuccess,

    /** ファイルが存在しない */
    notExists,

    /** 読み込む権限がない */
    forbidden,

    /** 読み込み時に例外が発生した */
    loadFailure
}
