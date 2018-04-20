package io.github.yutoeguma.dummy;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cabos
 */
public class SessionStorage {

    public static final String SESSION_KEY = "CABOSESSIONID";
    private static SessionStorage storage = new SessionStorage();

    public static SessionStorage get() {
        return storage;
    }

    private Map<String, String> map = new HashMap<>();

    public void put(String sessionId, String value) {
        this.map.put(sessionId, value);
    }

    public String get(String sessionId) {
        return this.map.get(sessionId);
    }
}
