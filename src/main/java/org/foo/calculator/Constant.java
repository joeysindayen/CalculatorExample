package org.foo.calculator;

import java.util.Map;
import java.util.TreeMap;

import org.foo.util.MathRegistry;
import org.foo.util.Resolver;

public enum Constant implements Expression<Double> {
    E {
        @Override
        public Double evaluate() {
            return Math.E;
        }
    },
    PI {
        @Override
        public Double evaluate() {
            return Math.PI;
        }
    },
    random {
        @Override
        public Double evaluate() {
            return Math.random();
        }
    };

    private static final Map<String, Expression<Double>> REGISTRY = new TreeMap<String, Expression<Double>>();
    static {
        for (Constant constant : values()) {
            REGISTRY.put(constant.name(), constant);
        }
    }
    public static final Resolver<String, Expression<Double>> RESOLVER = MathRegistry.asResolver(REGISTRY);

    @Override
    public String toString() {
        return name() + "=" + evaluate();
    }
}
