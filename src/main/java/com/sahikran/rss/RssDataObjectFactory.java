package com.sahikran.rss;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class RssDataObjectFactory {
    public static <T> T fromPropertyMap(Class<T> wrap, Map<String, Object> rssDataMap){
        Object proxy = 
                Proxy.newProxyInstance(
                    RssDataObjectFactory.class.getClassLoader(),
                    new Class<?>[]{wrap}, 
                    new Handler(rssDataMap));
        return ((T) proxy);
    }

    private static final class Handler implements InvocationHandler {
        private final Map<String, Object> rssDataMap;

        public Handler(Map<String, Object> rssDataMap){
            // create a new hashmap based on the received input
            this.rssDataMap = new HashMap<>(Objects.requireNonNull(rssDataMap, "rss data map cant be null"));
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            // directly invoke the methods equals and hashcode and no additional processing is required
            if(method.getDeclaringClass().equals(Object.class)){
                return method.invoke(rssDataMap, args);
            }
            String methodName = method.getName();
            if(methodName.length() <= 3 || !methodName.startsWith("get")){
                // ignore
                return null;
            }
            String propertyName = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
            if(!rssDataMap.containsKey(propertyName)){
                // ignore
                return null;
            }
            
            Object valueObject = rssDataMap.get(propertyName);
            if(valueObject == null){
                return null;
            }
            if(isMethodTypeAList(method)){
                return List.of(valueObject);
            }
            return valueObject;
        }

    }

    /**
     * verify if the return type of the method matches is of type List Class
     * @param method
     * @return false or true
     */
    private static boolean isMethodTypeAList(Method method){
        return method.getReturnType().getTypeName().equals(List.class.getTypeName());
    }
}
