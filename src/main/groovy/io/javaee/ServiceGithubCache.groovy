package io.javaee

import javax.annotation.PostConstruct
import javax.ejb.Lock
import javax.ejb.LockType
import javax.ejb.Schedule
import javax.ejb.Singleton
import java.util.logging.Logger

@Singleton
@Lock(LockType.READ)
class ServiceGithubCache {

    private Logger logger = Logger.getLogger(this.class.name)

    private Map<String, Map<ParamsKey, Object>> cache = [:].asSynchronized()

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
        def methodCache = this.cache.get(methodName)
        def key = new ParamsKey(
                params: arguments
        )
        if (methodCache.containsKey(key)) {
            return methodCache.get(key);
        }
        throw new ExceptionCache('cache entry not found')
    }

    @Lock(LockType.WRITE)
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
        this.cache = [:].asSynchronized()

        // every time you annotate a new method with "@Interceptors(InterceptorGithub)",
        // don't forget to add it here too.
        cache.put('getRepoDescription', [:].asSynchronized())
        cache.put('getRepoContributors', [:].asSynchronized())
        cache.put('getRepoPage', [:].asSynchronized())
        cache.put('getRepoRaw', [:].asSynchronized())
        cache.put('getContributor', [:].asSynchronized())
        logger.fine("Cache reset.")
    }

}

