package com.verdantartifice.primalmagick.common.util;

import org.apache.commons.lang3.function.TriFunction;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Collection of utility methods pertaining to functions and functional programming.
 * 
 * @author Daedalus4096
 */
public class FunctionUtils {
    public static <T, U, V, R> TriFunction<T, U, V, R> memoize(TriFunction<T, U, V, R> pMemoTriFunction) {
        return new TriFunction<T, U, V, R>() {
            private final Map<Triple<T, U, V>, R> cache = new ConcurrentHashMap<>();
            
            @Override
            public R apply(T arg1, U arg2, V arg3) {
                return this.cache.computeIfAbsent(ImmutableTriple.of(arg1, arg2, arg3), args -> {
                    return pMemoTriFunction.apply(args.getLeft(), args.getMiddle(), args.getRight());
                });
            }

            @Override
            public String toString() {
               return "memoize/3[function=" + pMemoTriFunction + ", size=" + this.cache.size() + "]";
            }
        };
    }
}
