package io.github.yutoeguma.routing;

import io.github.yutoeguma.app.LoginAction;
import io.github.yutoeguma.app.LoginCheckAction;
import io.github.yutoeguma.enums.HttpMethod;
import io.github.yutoeguma.http.message.HttpRequest;
import io.github.yutoeguma.http.message.HttpResponse;
import lombok.Getter;

/**
 * @author cabos
 */
@Getter
public enum Routing {

    LOGIN_GET("/login/", HttpMethod.GET, LoginAction::login$get),
    LOGIN_POST("/login/", HttpMethod.POST, LoginAction::login$post),
    LOGIN_CHECK_GET("/check/", HttpMethod.GET, LoginCheckAction::check$get)
    ;

    private String url;
    private HttpMethod method;
    private ActionMethodProvider provider;

    Routing(String url, HttpMethod method, ActionMethodProvider provider) {
        this.url = url;
        this.method = method;
        this.provider = provider;
    }

    public static Routing routeOf(String url, HttpMethod method) {
        for (Routing routing: Routing.values()) {
            if (routing.url.equals(url) && routing.method == method) {
                return routing;
            }
        }
        return null;
    }

    public HttpResponse doAction(HttpRequest request) {
        return this.provider.doAction(request);
    }
}
