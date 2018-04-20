package io.github.yutoeguma.app;

import io.github.yutoeguma.contents.ContentsLoader;
import io.github.yutoeguma.dummy.DummyDB;
import io.github.yutoeguma.dummy.SessionStorage;
import io.github.yutoeguma.enums.HttpStatus;
import io.github.yutoeguma.http.message.HttpRequest;
import io.github.yutoeguma.http.message.HttpResponse;
import io.github.yutoeguma.routing.Routing;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @author cabos
 */
public class LoginAction {

    private static final Logger logger = Logger.getLogger(LoginAction.class);

    private static ContentsLoader loader = ContentsLoader.get();
    private static SessionStorage storage = SessionStorage.get();

    public static HttpResponse login$get(HttpRequest request) {

        // ログイン情報があればリダイレクト
        String sessionInfo = storage.get(getSessionId(request));
        if (sessionInfo != null) {
            logger.info(sessionInfo);
            HttpResponse httpResponse = new HttpResponse(HttpStatus.FOUND);
            httpResponse.addResponseHeader("Location", Routing.LOGIN_CHECK_GET.getUrl());
            return httpResponse;
        }
        return new HttpResponse(HttpStatus.OK, loader.loadContents("/login.html"));
    }

    public static HttpResponse login$post(HttpRequest request) {

        // body から form の値を取り出す
        Map<String, String> formValue = getFormValue(request);
        String account = formValue.get("account");
        String password = formValue.get("password");

        // DBの中から会員を検索
        Optional<DummyDB.Member> optMember = DummyDB.Member.searchMember(account)
                .filter(member -> member.getPassword().equals(password)); // パスワードも検証

        HttpResponse response = optMember.map(member -> { // 見つかった場合
            // セッションにログイン情報を格納
            String key = UUID.randomUUID().toString();
            storage.put(key, account);

            // レスポンスを作成
            HttpResponse httpResponse = new HttpResponse(HttpStatus.FOUND);
            httpResponse.addResponseHeader("Location", Routing.LOGIN_CHECK_GET.getUrl()); // リダイレクト先の指定
            httpResponse.addResponseHeader("Set-Cookie", SessionStorage.SESSION_KEY + "=" + key + ";path=/;"); // クッキーのセット

            logger.info(httpResponse);
            return httpResponse;

            // 見つからなかった場合
        }).orElse(new HttpResponse(HttpStatus.BAD_REQUEST, loader.loadContents("/login.html")));

        logger.info(response);

        return response;
    }

    static private Map<String, String> getFormValue(HttpRequest request) { // e.g post request body -> "account=cabos&password=pass"
        Map<String, String> map = new HashMap<>();
        for (String e: request.getBody().split("&")) {
            String[] formKeyValue = e.split("=");
            if (formKeyValue.length >= 2) {
                map.put(formKeyValue[0], formKeyValue[1]);
            }
        }
        return map;
    }

    private static String getSessionId(HttpRequest request) {
        String cookie = request.getRequestHeaderAttr().get("Cookie");
        logger.info(cookie);
        if (cookie == null) {
            return null;
        }
        Map<String, String> map = new HashMap<>();
        List<String> strings = Arrays.asList(cookie.split(";"));
        strings.stream().map(c -> c.trim()).forEach(c -> {
            List<String> cookieKeyValue = Arrays.asList(c.split("="));
            if (cookieKeyValue.size() == 2) {
                map.put(cookieKeyValue.get(0), cookieKeyValue.get(1));
            }
        });
        return map.get("CABOSESSIONID");
    }
}
