package io.github.yutoeguma;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * webappディレクトリ以下のデータを
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

    public String getContents(String contentsPath) throws IOException {

         Path path = Paths.get( WEBAPP_PATH + contentsPath + "index.html");
         BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
         return reader.lines().collect(Collectors.joining());

        /* index.html が存在すればそれ */
//        File file = new File(WEBAPP_PATH + contentsPath);
//        if (file.isFile()) {
//            // TODO yuto ファイルならそれを返す (2017/02/19)
//        } else if (file.isDirectory()) {
//            // TODO yuto ディレクトリなら index.html を返す (2017/02/19)
//        } else {
//            // TODO yuto 404に相当する例外を投げる (2017/02/19)
//        }
    }
}
