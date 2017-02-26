package io.github.yutoeguma;

import io.github.yutoeguma.exeption.ContentsNotFoundException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * webapp 以下のコンテンツを取得するクラス
 *
 * @author yuto.eguma
 */
public class ContentsLoader {

    private static String WEBAPP_PATH = new File("./").getAbsoluteFile().getParent() + "/src/main/resources/webapp";

    private static ContentsLoader contentsLoader = new ContentsLoader();

    private ContentsLoader() {}

    public static ContentsLoader getContentsLoader() {
        return contentsLoader;
    }

    public byte[] getContents(String contentsPath) throws IOException {

        File file = new File(WEBAPP_PATH + contentsPath);
        if (file.isFile()) {
            // ファイルが指定されていればそれを返す
            return Files.readAllBytes(Paths.get(file.getAbsolutePath()));

        } else if (file.isDirectory()) {
            // ディレクトリであれば indexファイルを探して返す
            return Files.readAllBytes(Paths.get(file.getAbsolutePath() + "/index.html"));

        } else {
            // 存在しなければExceptionを返す
            throw new ContentsNotFoundException("対象のファイルが存在しませんでした contentsPath : " + contentsPath);
        }
    }
}
