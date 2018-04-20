package io.github.yutoeguma.routing;

import io.github.yutoeguma.http.message.HttpRequest;
import io.github.yutoeguma.http.message.HttpResponse;

/**
 * @author cabos
 */
public interface ActionMethodProvider {

    HttpResponse doAction(HttpRequest request);
}
