/*
 * Copyright 2016 The original authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.sundr;

import java.util.HashMap;
import java.util.Map;

public class CachingFunction<X,Y> implements Function<X,Y> {

    private final Map<X,Y> cache = new HashMap<X, Y>();
    private final Function<X,Y> function;

    public CachingFunction(Function<X, Y> function) {
        this.function = function;
    }

    public Y apply(X item) {
        Y result;
        synchronized (cache) {
            result = cache.get(item);
            if (result == null) {
                result = function.apply(item);
                cache.put(item, result);
            }
        }
        return result;
    }

    public static <X, Y> CachingFunction<X, Y> wrap(Function<X, Y> function) {
        return new CachingFunction<X, Y>(function);
    }
}