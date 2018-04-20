package io.github.yutoeguma.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cabos
 */
@Getter
public enum HttpMethod {

    GET("GET"),
    POST("POST");

    private String method;

    private static Map<String, HttpMethod> _map = new HashMap<>();

    static {
        for (HttpMethod m: HttpMethod.values()) {
            _map.put(m.getMethod(), m);
        }
    }

    HttpMethod(String method) {
        this.method = method;
    }

    public static HttpMethod methodOf(String s) {
       return  _map.get(s);
    }
}
