package io.github.yutoeguma.contents;

import io.github.yutoeguma.enums.ContentsLoadResultType;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * コンテンツを取得するクラス
 *
 * @author yuto.eguma
 */
public class ContentsLoader {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    /** コンテンツの配置されているディレクトリ */
    private static String CONTENTS_ROOT = new File("./").getAbsoluteFile().getParent() + "/src/main/resources/public";
    /** デフォルトのコンテンツ */
    private static byte[] DEFAULT_CONTENTS = new byte[0];
    /** logger */
    private static final Logger logger = Logger.getLogger(ContentsLoader.class);
    /** contentsLoader の唯一のインスタンス */
    private static ContentsLoader contentsLoader = new ContentsLoader();

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    private ContentsLoader() {}

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========

    /**
     * contentsLoader を取得します
     * @return contentsLoader (Singleton)
     */
    public static ContentsLoader get() {
        return contentsLoader;
    }

    /**
     * コンテンツを返します
     *
     * @param contentsPath コンテンツへのPath
     * @return コンテンツ
     */
    public ContentsLoadResult loadContents(String contentsPath) {

        try {
            File file = new File(CONTENTS_ROOT + contentsPath);
            if (file.isFile()) {
                // ファイルが指定されていればそれを返す
                return loadContentsIfCanRead(file);
            } else if (file.isDirectory()) {
                // ディレクトリであれば indexファイルを探して返す
                File indexFile = new File(file.getAbsolutePath() + "/index.html");
                if (indexFile.isFile()) {
                    return loadContentsIfCanRead(indexFile);
                }
            }
            return new ContentsLoadResult(contentsPath, DEFAULT_CONTENTS, ContentsLoadResultType.notExists);
        } catch (IOException e) {
            logger.error("ファイルの読み込み中にエラーが発生しました", e);
            return new ContentsLoadResult(contentsPath, DEFAULT_CONTENTS, ContentsLoadResultType.loadFailure);
        }

    }

    /**
     * ファイルが読み込み可能であればロードし、コンテンツを返す<br>
     * ファイルが読み込み可能でない場合は、読み込めなかった結果を返す
     *
     * @param file 読み込むファイル
     * @return コンテンツ読み込み結果
     * @throws IOException ファイルの読み込みに失敗した時
     */
    private ContentsLoadResult loadContentsIfCanRead(File file) throws IOException {
        String filePath = file.getAbsolutePath();
        if (file.canRead()) {
            byte[] detail = Files.readAllBytes(Paths.get(filePath));
            return new ContentsLoadResult(filePath, detail, ContentsLoadResultType.loadSuccess);
        } else {
            return new ContentsLoadResult(filePath, DEFAULT_CONTENTS, ContentsLoadResultType.forbidden);
        }
    }
}
