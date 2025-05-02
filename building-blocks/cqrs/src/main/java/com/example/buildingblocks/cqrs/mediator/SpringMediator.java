package com.example.buildingblocks.cqrs.mediator;

import com.example.buildingblocks.cqrs.handler.RequestHandler;
import com.example.buildingblocks.cqrs.request.Request;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
@Component
public class SpringMediator implements Mediator {
    private final Map<Class<?>, RequestHandler<?, ?>> handlers = new HashMap<>();

    public SpringMediator(ApplicationContext context) {
        // Autowire all RequestHandler beans
        Map<String, RequestHandler> beans = context.getBeansOfType(RequestHandler.class);
        for (RequestHandler<?, ?> handler : beans.values()) {
            Class<?> requestType = extractRequestType(handler);
            handlers.put(requestType, handler);
        }

        // Also register self-handling commands/queries
        Map<String, Request> requestBeans = context.getBeansOfType(Request.class);
        for (Request<?> requestBean : requestBeans.values()) {
            if (requestBean instanceof RequestHandler<?, ?> handler) {
                handlers.put(requestBean.getClass(), handler);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R, T extends Request<R>> R send(T request) {
        RequestHandler<T, R> handler = (RequestHandler<T, R>) handlers.get(request.getClass());
        if (handler == null) {
            throw new IllegalArgumentException("No handler found for: " + request.getClass());
        }
        return handler.handle(request);
    }

    private Class<?> extractRequestType(RequestHandler<?, ?> handler) {
        // Try to get the type from the class itself
        Class<?> requestType = extractTypeFromClass(handler.getClass());
        if (requestType != null) {
            return requestType;
        }

        // If the class is proxied, try to get the type from the superclass
        Class<?> currentClass = handler.getClass();
        while (currentClass != null && currentClass != Object.class) {
            requestType = extractTypeFromClass(currentClass);
            if (requestType != null) {
                return requestType;
            }

            // Try interfaces of the current class
            for (Class<?> iface : currentClass.getInterfaces()) {
                requestType = extractTypeFromClass(iface);
                if (requestType != null) {
                    return requestType;
                }
            }

            currentClass = currentClass.getSuperclass();
        }

        throw new IllegalStateException("Cannot resolve request type for: " + handler.getClass());
    }

    private Class<?> extractTypeFromClass(Class<?> clazz) {
        for (Type type : clazz.getGenericInterfaces()) {
            if (type instanceof ParameterizedType pt &&
                    pt.getRawType() == RequestHandler.class) {
                return (Class<?>) pt.getActualTypeArguments()[0];
            }
        }
        return null;
    }
}
