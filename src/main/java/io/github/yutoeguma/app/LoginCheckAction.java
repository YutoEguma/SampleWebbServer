package io.github.yutoeguma.app;

import io.github.yutoeguma.contents.ContentsLoader;
import io.github.yutoeguma.dummy.SessionStorage;
import io.github.yutoeguma.enums.HttpStatus;
import io.github.yutoeguma.http.message.HttpRequest;
import io.github.yutoeguma.http.message.HttpResponse;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cabos
 */
public class LoginCheckAction {

    private static final Logger logger = Logger.getLogger(LoginCheckAction.class);
    private static ContentsLoader loader = ContentsLoader.get();
    private static SessionStorage storage = SessionStorage.get();

    static public HttpResponse check$get(HttpRequest request) {
        logger.info(request);
        String sessionId = getSessionId(request);
        if (sessionId == null) {
            return new HttpResponse(HttpStatus.BAD_REQUEST, loader.loadContents("/ng.html"));
        }
        return new HttpResponse(HttpStatus.OK, loader.loadContents("/ok.html"));
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
