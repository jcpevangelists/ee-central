package io.javaee

import javax.annotation.PostConstruct
import javax.ejb.Lock
import javax.ejb.LockType
import javax.ejb.Schedule
import javax.ejb.Singleton
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.logging.Logger

@Singleton
@Lock(LockType.READ)
class CachedHolder {
    private Logger logger = Logger.getLogger(this.class.name)

    private ConcurrentMap<String, Map<ParamsKey, Object>> cache = new ConcurrentHashMap<>()

    private class ParamsKey {
        public Object[] params

        boolean equals(o) {
            if (this.is(o)) {
                return true
            }
            if (getClass() != o.class) {
                return false
            }
            ParamsKey paramsKey = (ParamsKey) o
            if (!Arrays.equals(params, paramsKey.params)) {
                return false
            }
            return true
        }

        int hashCode() {
            return (params != null ? Arrays.hashCode(params) : 0)
        }
    }

    Object get(String methodName, Object[] arguments) {
        this.cache.putIfAbsent(methodName, [:])
        def methodCache = this.cache.get(methodName)
        def key = new ParamsKey(
                params: arguments
        )
        if (methodCache.containsKey(key)) {
            return methodCache.get(key);
        }
        throw new ExceptionCache('cache entry not found')
    }

    void set(String methodName, Object[] arguments, Object value) {
        def methodCache = this.cache.get(methodName)
        methodCache.put(new ParamsKey(
                params: arguments
        ), value);
    }

    @Schedule(hour = "*")
    @Lock(LockType.WRITE)
    @PostConstruct
    void reset() {
        // simply create a new map. Running consumers will continue to use the old cache until they are done.
        if (!this.cache.isEmpty()) {
            this.cache = new ConcurrentHashMap<>()
            logger.info("Cache cleared.")
        }
    }
}
