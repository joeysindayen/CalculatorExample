package org.foo.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class MathRegistry {
    private static Map<String, Method> mathFunctions;

    public static <K, V> Resolver<K, V> asResolver(final Map<K, V> map) {
        return new Resolver<K, V>() {
            @Override
            public V resolve(K key) {
                return map.get(key);
            }
        };
    }

    public synchronized static Map<String, Method> getMathStaticFunctions() {
        if (mathFunctions == null) {
            Map<String, Method> map = new HashMap<String, Method>();
            for (Method method : Math.class.getMethods()) {
                if (isPublicStaticDouble(method) && hasDoubleParameters(method)) {
                    map.put(method.getName(), method);
                }
            }
            mathFunctions = map;
        }
        return Collections.unmodifiableMap(mathFunctions);
    }

    private static boolean hasDoubleParameters(Method method) {
        for (Class<?> type : method.getParameterTypes()) {
            if (!double.class.equals(type)) {
                return false;
            }
        }
        return method.getParameterTypes().length > 0;
    }

    private static boolean isPublicStaticDouble(Method method) {
        return Modifier.isPublic(method.getModifiers()) //
            && Modifier.isStatic(method.getModifiers()) //
            && double.class.equals(method.getReturnType());
    }
}
